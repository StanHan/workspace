package demo.java.net;

import java.net.*;
import java.io.*;

public class TCPServer {
	ServerSocket serverSocket;
	Socket socket;
	BufferedReader bufferedReader;
	PrintWriter printWriter;

	String str[] = { "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "1010" };

	void init() {
		try {
			serverSocket = new ServerSocket(8888);
			socket = serverSocket.accept();

			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			bufferedReader = new BufferedReader(inputStreamReader);
			printWriter = new PrintWriter(outputStream, true);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	void process() {
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("111111111");
				String message = bufferedReader.readLine();
				System.out.println(message);
				printWriter.println(str[i]);
				Thread.sleep(1000);
			}
			socket.close();
			serverSocket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	TCPServer() {
		init();
		process();
	}

	public static void main(String s[]) {
		new TCPServer();
	}
}