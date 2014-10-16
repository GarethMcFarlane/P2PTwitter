
public class P2PTwitter {

	public static void main(String args[]) {
		Thread server = new Thread(new P2PTServer());
		Thread client = new Thread(new P2PTClient(args[0]));
		Profile.loadProperties(args[0]);
		server.start();
		client.start();
		
	}
}
