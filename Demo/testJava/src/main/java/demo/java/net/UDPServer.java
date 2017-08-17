package demo.java.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
	DatagramSocket datagramSocket;
	byte buffer[] = new byte[100];
	DatagramPacket datagramPacket = new DatagramPacket(buffer, 100);

	UDPServer() {
		try {
			datagramSocket = new DatagramSocket(9999);
			for (int i = 0; i < 10; i++) {
				datagramSocket.receive(datagramPacket);
				String message = PacketToString(datagramPacket);
				System.out.println(message);

				DatagramPacket dp2 = StringToPacket(datagramPacket.getAddress(), datagramPacket.getPort(), i + "   " + i);
				datagramSocket.send(dp2);
				Thread.sleep(1000);
			}
			datagramSocket.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

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

	public static void main(String s[]) {
		new UDPServer();
	}
}