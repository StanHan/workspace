package demo.java.nio;

/*
 * @(#)TimeQuery.java	1.2 01/12/13
 * Ask a list of hosts what time it is.  Demonstrates NIO socket channels
 * (connection and reading), buffer handling, charsets, and regular
 * expressions.
 *
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.regex.*;

public class TimeQuery {

	public static void main(String[] args) {
		String host = "128.0.183.135";
		try {
			query(host);
		} catch (IOException x) {
			System.err.println(host + ": " + x);
		}
	}

	// The standard daytime port
	private static int DAYTIME_PORT = 8013;

	// The port we'll actually use
	private static int port = DAYTIME_PORT;

	// Charset and decoder for US-ASCII
	private static Charset charset = Charset.forName("US-ASCII");
	private static CharsetDecoder charsetDecoder = charset.newDecoder();

	// Direct byte buffer for reading
	private static ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

	// Ask the given host what time it is
	//
	private static void query(String host) throws IOException {
		InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
		SocketChannel socketChannel = null;

		try {

			// Connect
			socketChannel = SocketChannel.open();
			socketChannel.connect(inetSocketAddress);

			// Read the time from the remote host. For simplicity we assume
			// that the time comes back to us in a single packet, so that we
			// only need to read once.
			byteBuffer.clear();
			socketChannel.read(byteBuffer);

			// Print the remote address and the received time
			byteBuffer.flip();
			CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
			System.out.print(inetSocketAddress + " : " + charBuffer);

		} finally {
			// Make sure we close the channel (and hence the socket)
			if (socketChannel != null)
				socketChannel.close();
		}
	}

	public static void test(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java TimeQuery [port] host...");
			return;
		}
		int firstArg = 0;

		// If the first argument is a string of digits then we take that
		// to be the port number
		if (Pattern.matches("[0-9]+", args[0])) {
			port = Integer.parseInt(args[0]);
			firstArg = 1;
		}

		for (int i = firstArg; i < args.length; i++) {
			String host = args[i];
			try {
				query(host);
			} catch (IOException x) {
				System.err.println(host + ": " + x);
			}
		}
	}

}
