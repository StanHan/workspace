package demo.java.util.concurrent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkService implements Runnable {
	public static void main(String[] args) {
		NetworkService networkService = null;
		try {
			networkService = new NetworkService(3000, 2);
			new Thread(networkService).start();

			Socket socket2 = new Socket("127.0.0.1", 3000);
			byte[] buffer1 = new byte[2];
			for (int i = 0; i < 2; i++) {
				Socket socket1 = new Socket("127.0.0.1", 3000);
				String string = "hello world!";
				OutputStream os1 = socket1.getOutputStream();
				os1.write(string.getBytes());
				InputStream is1 = socket1.getInputStream();
				int byte_read = 0;
				if ((byte_read = is1.read(buffer1)) != -1) {
					System.out.println("client1 : " + new String(buffer1, 0, byte_read));
					socket1.close();
				}
			}
			socket2.close();
			boolean isClosed = networkService.close();
			System.out.println(isClosed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final ServerSocket serverSocket;
	private final ExecutorService executorService;

	public NetworkService(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void run() {
		try {
			for (;;) {
				executorService.execute(new SocketHandler(serverSocket.accept()));
			}
		} catch (IOException ex) {
			executorService.shutdown();
		}
	}

	public boolean close() throws IOException{
		this.serverSocket.close();
		this.shutdownAndAwaitTermination(executorService);
		return executorService.isTerminated()&&serverSocket.isClosed();
	}
	
	private void shutdownAndAwaitTermination(ExecutorService executorService) {
		executorService.shutdown(); // Disable new tasks from being submitted
		if (executorService.isTerminated()) {
			System.out.println("executorService was closed.");
			return;
		}
		try {
			// Wait a while for existing tasks to terminate
			if (!executorService.awaitTermination(6, TimeUnit.SECONDS)) {
				// Cancel currently executing tasks
				executorService.shutdownNow();
				// Wait a while for tasks to respond to being cancelled
				if (!executorService.awaitTermination(6, TimeUnit.SECONDS)) {
					System.err.println("Pool did not terminate");
				}
			}
			
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executorService.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		} finally {
			// (Re-)Cancel if current thread also interrupted
			executorService.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

}

class SocketHandler implements Runnable {
	private final Socket socket;

	SocketHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		// read and service request on socket
		InetAddress inetAddress = socket.getInetAddress();

		System.out.println(inetAddress);
		String threadName = Thread.currentThread().getName();
		byte[] buffer = new byte[1024];
		StringBuffer stringBuffer = new StringBuffer();

		try {
			InputStream inputStream = socket.getInputStream();
			OutputStream ouputStream = socket.getOutputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			int byteRead = 0;
			while ((byteRead = bufferedInputStream.read(buffer)) != -1) {
				System.out.println(threadName + " : " + new String(buffer, 0, byteRead));
				ouputStream.write("received.".getBytes());
			}
			System.out.println(stringBuffer);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
