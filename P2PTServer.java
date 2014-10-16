import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class P2PTServer implements Runnable {
	public static int PORT = 7014;
	private DatagramSocket socket = null;

	public P2PTServer() {
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


	public void run() {
		//receive packets from clients
		//update the tweets
		System.out.println("server running");
		
		
		while (true) {
			try {
				//Byte that will be used to receive the packet.
				byte[] buf = new byte[256];
				//Creates a new packet and receives it.
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				socket.receive(packet);
				
				//Gets the packet in byte form and converts it to a string.
				buf = packet.getData();
				String data = new String(buf, "ISO-8859-1");
				
				//Temp: prints out received message.
				System.out.println("data= " + data);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
