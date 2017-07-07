package demo.java.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.net.*;
import java.util.*;

/**
 * Listen on a port for connections and write back the current time. A non
 * blocking Internet time server implemented using the New I/O (NIO) facilities
 * added to J2SE v 1.4.
 *
 */
public class NBTimeServer {
	private static final int DEFAULT_TIME_PORT = 8900;

	// Constructor with no arguments creates a time server on default port.
	public NBTimeServer() throws Exception {
		acceptConnections(this.DEFAULT_TIME_PORT);
	}

	// Constructor with port argument creates a time server on specified port.
	public NBTimeServer(int port) throws Exception {
		acceptConnections(port);
		acceptConnections(port);
	}

	// Accept connections for current time. Lazy Exception thrown.
	private static void acceptConnections(int port) throws Exception {
		// Selector for incoming time requests
		Selector selector = SelectorProvider.provider().openSelector();

		// Create a new server socket and set to non blocking mode
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		// Bind the server socket to the local host and port

		InetAddress inetAddress = InetAddress.getLocalHost();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
		serverSocketChannel.socket().bind(inetSocketAddress);

		// Register accepts on the server socket with the selector. This
		// step tells the selector that the socket wants to be put on the
		// ready list when accept operations occur, so allowing multiplexed
		// non-blocking I/O to take place.
		SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		int keysAdded = 0;

		// Here's where everything happens. The select method will
		// return when any operations registered above have occurred, the
		// thread has been interrupted, etc.
		while ((keysAdded = selector.select()) > 0) {
			// Someone is ready for I/O, get the ready keys
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();

			// Here's where everything happens. The select method will
			// return when any operations registered above have occurred, the
			// Walk through the ready keys collection and process date requests.
			while (iterator.hasNext()) {
				SelectionKey sk = iterator.next();
				iterator.remove();
				// The key indexes into the selector so you
				// can retrieve the socket that's ready for I/O
				ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
				// Accept the date request and send back the date string
				Socket socket = nextReady.accept().socket();
				// Write the current time to the socket
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				Date now = new Date();
				out.println(now);
				out.close();
			}
		}
	}


	// Entry point.
	public static void main(String[] args) {
		// Parse command line arguments and
		// create a new time server (no arguments yet)
		try {
			NBTimeServer nbt = new NBTimeServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
