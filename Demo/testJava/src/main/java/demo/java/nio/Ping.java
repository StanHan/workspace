package demo.java.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

/*
 * @(#)Ping.java	1.2 01/12/13
 * Connect to each of a list of hosts and measure the time required to complete
 * the connection.  This example uses a selector and two additional threads in
 * order to demonstrate non-blocking connects and the multithreaded use of a
 * selector.
 *
 */
public class Ping {
	public static void main(String[] args) throws InterruptedException, IOException {
		// test(args);
		test1();
	}

	// The default daytime port
	static int DAYTIME_PORT = 13;

	// The port we'll actually use
	static int port = DAYTIME_PORT;

	// Representation of a ping target
	static class Target {
		InetSocketAddress inetSocketAddress;
		SocketChannel socketChannel;
		Exception exception;
		long connectStart;
		long connectFinish = 0;
		boolean shown = false;

		Target(String host) {
			try {
				inetSocketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
			} catch (IOException x) {
				exception = x;
			}
		}

		void show() {
			String result;
			if (connectFinish != 0) {
				result = Long.toString(connectFinish - connectStart) + "ms";
			} else if (exception != null) {
				result = exception.toString();
			} else {
				result = "Timed out";
			}
			System.out.println(inetSocketAddress + " : " + result);
			shown = true;
		}

	}

	// Thread for printing targets as they're heard from
	static class Printer extends Thread {
		LinkedList<Target> linkedList = new LinkedList<Target>();

		Printer() {
			setName("Printer");
			setDaemon(true);
		}

		void add(Target t) {
			synchronized (linkedList) {
				linkedList.add(t);
				linkedList.notify();
			}
		}

		public void run() {
			try {
				for (;;) {
					Target target = null;
					synchronized (linkedList) {
						while (linkedList.size() == 0) {
							linkedList.wait();
						}
						target = (Target) linkedList.removeFirst();
					}
					target.show();
				}
			} catch (InterruptedException x) {
				return;
			}
		}

	}

	// Thread for connecting to all targets in parallel via a single selector
	static class Connector extends Thread {
		Selector selector;
		Printer printer;

		// List of pending targets. We use this list because if we try to
		// register a channel with the selector while the connector thread is
		// blocked in the selector then we will block.
		//
		LinkedList<Target> linkedList = new LinkedList<Target>();

		Connector(Printer pr) throws IOException {
			printer = pr;
			selector = Selector.open();
			setName("Connector");
		}

		// Initiate a connection sequence to the given target and add the
		// target to the pending-target list
		//
		void add(Target target) {
			SocketChannel socketChanel = null;
			try {
				// Open the channel, set it to non-blocking, initiate connect
				socketChanel = SocketChannel.open();
				socketChanel.configureBlocking(false);

				boolean connected = socketChanel.connect(target.inetSocketAddress);

				// Record the time we started
				target.socketChannel = socketChanel;
				target.connectStart = System.currentTimeMillis();

				if (connected) {
					target.connectFinish = target.connectStart;
					socketChanel.close();
					printer.add(target);
				} else {
					// Add the new channel to the pending list
					synchronized (linkedList) {
						linkedList.add(target);
					}

					// Nudge the selector so that it will process the pending
					// list
					selector.wakeup();
				}
			} catch (IOException x) {
				if (socketChanel != null) {
					try {
						socketChanel.close();
					} catch (IOException xx) {
					}
				}
				target.exception = x;
				printer.add(target);
			}
		}

		// Process any targets in the pending list
		//
		void processPendingTargets() throws IOException {
			synchronized (linkedList) {
				while (linkedList.size() > 0) {
					Target target = (Target) linkedList.removeFirst();
					try {

						// Register the channel with the selector, indicating
						// interest in connection completion and attaching the
						// target object so that we can get the target back
						// after the key is added to the selector's
						// selected-key set
						target.socketChannel.register(selector, SelectionKey.OP_CONNECT, target);
					} catch (IOException x) {

						// Something went wrong, so close the channel and
						// record the failure
						target.socketChannel.close();
						target.exception = x;
						printer.add(target);
					}
				}

			}
		}

		// Process keys that have become selected
		void processSelectedKeys() throws IOException {
			for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {

				// Retrieve the next key and remove it from the set
				SelectionKey selectionKey = iterator.next();
				iterator.remove();

				// Retrieve the target and the channel
				Target target = (Target) selectionKey.attachment();
				SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

				// Attempt to complete the connection sequence
				try {
					if (socketChannel.finishConnect()) {
						selectionKey.cancel();
						target.connectFinish = System.currentTimeMillis();
						socketChannel.close();
						printer.add(target);
					}
				} catch (IOException x) {
					socketChannel.close();
					target.exception = x;
					printer.add(target);
				}
			}
		}

		volatile boolean shutdown = false;

		// Invoked by the main thread when it's time to shut down
		//
		void shutdown() {
			shutdown = true;
			selector.wakeup();
		}

		// Connector loop
		//
		public void run() {
			for (;;) {
				try {
					int n = selector.select();
					if (n > 0)
						processSelectedKeys();
					processPendingTargets();
					if (shutdown) {
						selector.close();
						return;
					}
				} catch (IOException x) {
					x.printStackTrace();
				}
			}
		}
	}

	public static void test(String[] args) throws InterruptedException, IOException {
		if (args.length < 1) {
			System.err.println("Usage: java Ping [port] host...");
			return;
		}
		int firstArg = 0;

		// If the first argument is a string of digits then we take that
		// to be the port number to use
		if (Pattern.matches("[0-9]+", args[0])) {
			port = Integer.parseInt(args[0]);
			firstArg = 1;
		}

		// Create the threads and start them up
		Printer printer = new Printer();
		printer.start();
		Connector connector = new Connector(printer);
		connector.start();

		// Create the targets and add them to the connector
		LinkedList<Target> targets = new LinkedList<Target>();
		for (int i = firstArg; i < args.length; i++) {
			Target t = new Target(args[i]);
			targets.add(t);
			connector.add(t);
		}

		// Wait for everything to finish
		Thread.sleep(2000);
		connector.shutdown();
		connector.join();

		// Print status of targets that have not yet been shown
		for (Iterator<Target> i = targets.iterator(); i.hasNext();) {
			Target t = (Target) i.next();
			if (!t.shown)
				t.show();
		}
	}

	public static void test1() throws InterruptedException, IOException {
		String[] args = { "localhost", "127.0.0.1" };
		if (args.length < 1) {
			System.err.println("Usage: java Ping [port] host...");
			return;
		}
		int firstArg = 0;

		// If the first argument is a string of digits then we take that
		// to be the port number to use
		if (Pattern.matches("[0-9]+", args[0])) {
			port = Integer.parseInt(args[0]);
			firstArg = 1;
		}

		// Create the threads and start them up
		Printer printer = new Printer();
		printer.start();
		Connector connector = new Connector(printer);
		connector.start();

		// Create the targets and add them to the connector
		LinkedList<Target> targets = new LinkedList<Target>();
		for (int i = firstArg; i < args.length; i++) {
			Target t = new Target(args[i]);
			targets.add(t);
			connector.add(t);
		}

		// Wait for everything to finish
		Thread.sleep(2000);
		connector.shutdown();
		connector.join();

		// Print status of targets that have not yet been shown
		for (Iterator<Target> i = targets.iterator(); i.hasNext();) {
			Target t = (Target) i.next();
			if (!t.shown)
				t.show();
		}
	}
}
