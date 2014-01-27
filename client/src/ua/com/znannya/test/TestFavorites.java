package ua.com.znannya.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.packet.znannya.UserInfo;
import org.jivesoftware.smack.znannya.UserInfoManager;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.favorite.FavoriteManager;

public class TestFavorites {
	private String server = "127.0.0.1";
//	private String server = "91.192.46.115";

	public static void main(String[] args) {
		new TestFavorites();

	}

	public TestFavorites(){
		System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;

		
		ConnectionConfiguration xmppConfig = new ConnectionConfiguration(server, 3128);
		xmppConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		xmppConfig.setTruststorePath("./resources/security/truststore");
		xmppConfig.setTruststorePassword("zn_trustpass");
		xmppConfig.setCompressionEnabled(true);
		xmppConfig.setSASLAuthenticationEnabled(true);
		xmppConfig.setSocketFactory(new DummySSLSocketFactory(server, xmppConfig));// DummySSLSocketFactory can be found here

		XMPPConnection userXmppConnection = new XMPPConnection(xmppConfig);
		try{
			userXmppConnection.connect();
			
			userXmppConnection.login("admin", "admin");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		FavoriteManager favoriteManager = new FavoriteManager(userXmppConnection);
		try{
			favoriteManager.addFavorite(1, EntryType.diss);
//			favoriteManager.addFavorite(3, EntryType.pub);
			favoriteManager.removeFavorite(1, EntryType.diss);
			EntriesCollection entries = favoriteManager.getFavorites();
			System.out.println(entries);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
