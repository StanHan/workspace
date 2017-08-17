package demo.java.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	DatagramSocket socket;
	byte[] buf = new byte[100];
	DatagramPacket dp = new DatagramPacket(buf, 100);

	DatagramPacket StringToPacket(InetAddress ip, int port, String str) {
		int length = str.length();
		byte[] buf = new byte[length];
		buf = str.getBytes();
		DatagramPacket dp = new DatagramPacket(buf, length, ip, port);
		return dp;
	}

	String PacketToString(DatagramPacket dp) {
		int length = dp.getLength();
		byte buf[] = new byte[length];
		buf = dp.getData();
		String str = new String(buf);
		return str;
	}

	UDPClient() {
		try {
			socket = new DatagramSocket();
			for (int i = 0; i < 10; i++) {
				InetAddress ip = InetAddress.getByName("127.0.0.1");
				DatagramPacket dp2 = StringToPacket(ip, 9999, "��ҵ������ʲô " + i);
				socket.send(dp2);
				socket.receive(dp);
				String message = PacketToString(dp);
				System.out.println(message);
				Thread.sleep(1000);
			}
			socket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String s[]) {
		new UDPClient();
	}
}