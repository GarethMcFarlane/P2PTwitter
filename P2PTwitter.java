
public class P2PTwitter {

	public static void main(String args[]) {
		P2PTServer server = new P2PTServer();
		P2PTClient client = new P2PTClient(args[0]);
		Profile.loadProperties(args[0]);
		//client.run();
		server.run();
	}
}
