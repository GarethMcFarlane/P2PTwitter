import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

public class Profile {
	private static int machines; // Number of peers in the network
	private static String[] peers; // Array of peers
	private static String[] pseudo; // Array of pseudos.
	private static String[] unikey; // Array of unikeys.
	private static String[] tweets; // Array of tweets.
	private static String[] IP; // Array of IP addresses.
	private static boolean[] seen; // Array that determines if peers have been
									// seen.
	public static String reply;
	private static String myUnikey;
	private static long[] lastSeen;

	public static long[] getLastSeen() {
		return lastSeen;
	}

	// Updates the current tweet of a unikey to the new one.
	public synchronized static void updateTweet(String studentkey, String tweet) {
		String key = studentkey;
		if (studentkey.equals(myUnikey)) {
			key = "myself";
		}

		for (int i = 0; i < machines; ++i) {
			if (unikey[i].equals(key)) {
				tweets[i] = tweet;
			}
		}
	}

	// Loads the properties file and fills the arrays.
	public static void loadProperties(String myUnikey) {
		Properties prop = new Properties();
		String propFileName = "participants.properties";
		try {
			FileInputStream inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);

			String initPeers = prop.getProperty("participants");
			peers = initPeers.split(",");
			machines = peers.length;
			pseudo = new String[machines];
			unikey = new String[machines];
			IP = new String[machines];
			tweets = new String[machines];
			seen = new boolean[machines];
			lastSeen = new long[machines];

			Arrays.fill(tweets, "Not yet initialized");

			// load peer data
			int i = 0;
			for (String s : peers) {
				pseudo[i] = prop.getProperty(s + ".pseudo");
				unikey[i] = prop.getProperty(s + ".unikey");
				IP[i] = prop.getProperty(s + ".ip");
				++i;
			}

			inputStream.close();

			updateMyKey(myUnikey);

		} catch (Exception e) {
			// do something lel
		}

	}

	// Returns the array of seen peers.
	public static boolean[] getSeen() {
		return seen;
	}

	// Notifies that a peer has been seen.
	public synchronized static void updateSeen(String keyToUpdate, long time,
			String tweet) {

		String key = keyToUpdate;
		if (keyToUpdate.equals(myUnikey)) {
			key = "myself";
		}
		for (int i = 0; i < machines; ++i) {
			if (unikey[i].equals(key)) {
				if (!tweet.equals(tweets[i])) {
					seen[i] = true;
					lastSeen[i] = time;
				}
			}
		}
	}

	public static String getTweet(String key) {
		for (int i = 0; i < machines; ++i) {
			if (unikey[i].equals(key)) {
				return tweets[i];
			}
		}
		return null;
	}

	// Returns the array of tweets.
	public static String[] getTweets() {
		return tweets;
	}

	// Returns the array of IP address in the network.
	public static String[] getIPs() {
		return IP;
	}

	// Returns the array of pseudos.
	public static String[] getPseudos() {
		return pseudo;
	}

	// Returns the array of unikeys.
	public static String[] getUnikeys() {
		return unikey;
	}

	// Sets your unikey to "myself".
	private static void updateMyKey(String mykey) {
		myUnikey = mykey;
		for (int i = 0; i < machines; ++i) {
			if (unikey[i].equals(mykey)) {
				unikey[i] = "myself";
			}
		}
	}
}
