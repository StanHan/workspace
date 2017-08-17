package demo.java.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
	InetAddress ip;
	int port = 8888;
	Socket socket;

	BufferedReader br;
	PrintWriter pw;

	void init() {
		try {
			ip = InetAddress.getByName("127.0.0.1");
			socket = new Socket(ip, port);
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			OutputStream os = socket.getOutputStream();
			pw = new PrintWriter(os, true);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	void process() {
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("2222222222");

				pw.println("����������Ķ�����ʲô  " + i);

				Thread.sleep(1000);

				String message = br.readLine();
				System.out.println(message);
			}
			socket.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	TCPClient() {
		init();
		process();
	}

	public static void main(String s[]) {
		new TCPClient();
	}
}