import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class P2PTClient implements Runnable {
	private DatagramSocket socket;
	private String unikey;

	public P2PTClient(String unikey) {
		this.unikey = unikey;
		try {
			socket = new DatagramSocket();
		} catch (Exception e) {

		}
	}

	public void run() {
		InputStreamReader fileInputStream = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(fileInputStream);
		Random randomGenerator = new Random();
		String text = null;

		System.out.print("Status: ");
		try {
			socket = new DatagramSocket();
			while (true) {
				// If the user has pressed enter:
				if (bufferedReader.ready()) {
					// Get the line.
					String data = bufferedReader.readLine();
					// If empty, print error message.
					if (data.equals("")) {
						System.out.println("Status is empty.  Retry");
						// If too long, print error message.
					} else if (data.length() > 140) {
						System.out
								.println("Status is too long, 140 characters max.  Retry.");
					} else {
						// Save the text.
						text = unikey + ":" + data;
					}
					// Send tweet to every peer.
					sendTweet(text);
					// Waits for server to finish processing.
					Thread.sleep(100);
					// Output everyone's tweets.
					outputTweets();
					// Ask for another status.
					System.out.print("Status: ");
				} else {
					// If nothing happened, keep sending the same message.
					int r = randomGenerator.nextInt(3000 - 1000) + 1000;
					Thread.sleep(r);
					sendTweet(text);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendTweet(String text) {

		// TODO: add a seperate localhost send if not specified in the file.
		try {
			// Convert the text to bytes.
			byte[] buf = text.getBytes("ISO-8859-1");
			String[] addresses = Profile.getIPs();
			for (String s : addresses) {
				// Sends a packet to every peer in the network.
				InetAddress address = InetAddress.getByName(s);
				DatagramPacket packet = new DatagramPacket(buf, buf.length,
						address, 7014);
				socket.send(packet);
			}
		} catch (Exception e) {
			// ayylmao
		}
	}

	public void outputTweets() {
		// Gets all the peer details.
		String[] tweets = Profile.getTweets();
		String[] unikey = Profile.getUnikeys();
		String[] pseudo = Profile.getPseudos();
		boolean[] seen = Profile.getSeen();
		long[] lastseen = Profile.getLastSeen();

		// Prints out the tweets.
		System.out.println("### P2P tweets ###");
		for (int i = 0; i < tweets.length; ++i) {
			
			String message = pseudo[i] + " (" + unikey[i] + ") : " + tweets[i];
			if (seen[i]) {
				if ((System.currentTimeMillis() - lastseen[i])/1000 < 10) {
					System.out.println("# " + message);
				} else if ((System.currentTimeMillis() - lastseen[i])/1000 < 20) {
					System.out.println("# [" + pseudo[i] + " (" + unikey[i]
							+ ") : " + "idle]");
				}
			} else {
				System.out.println("# [" + message + "]");
			}
			
		}

		System.out.println("### End tweets ###");
	}
}
