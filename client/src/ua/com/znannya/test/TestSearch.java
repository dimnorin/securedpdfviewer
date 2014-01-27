package ua.com.znannya.test;

import java.util.Arrays;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Publication;
import org.jivesoftware.smack.znannya.search.SearchManager;

public class TestSearch {
	private String server = "127.0.0.1";

	public static void main(String[] args) {
		new TestSearch();

	}

	public TestSearch(){
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
		
		SearchManager dissManager = new SearchManager(userXmppConnection);
		try{
//			EntriesCollection dissertations = dissManager.doSearch("Взаимодействие *", 0, 50, "", true, true);
//			System.out.println("Dissertations count="+dissertations.getCount());
//                        System.out.println( Arrays.toString( dissertations.toArray() ) );
			EntriesCollection dissertations = dissManager.doDissertationsSearch(null, null, null, "скрипник юрий", null, null, null, null, 0, 10, "year", true, true);
			dissertations = dissManager.doPublicationsSearch(Publication.TYPE_BOOK, "name", null, null, null, null, null, null, null, 0, 10, "year", true, true);
//                        System.out.println( Arrays.toString( dissertations.toArray() ) );
			System.out.println("Dissertations count="+dissertations.getCount());

		}catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
