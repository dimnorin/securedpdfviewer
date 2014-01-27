package ua.com.znannya.test;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.search.InitialLoadManager;

public class TestInitialLoad {
	private String server = "127.0.0.1";

	public static void main(String[] args) {
		new TestInitialLoad();

	}

	public TestInitialLoad(){
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
		
		InitialLoadManager initialManager = new InitialLoadManager(userXmppConnection);
		try{
			List<String> codes = new ArrayList<String>();
			List<String> years = new ArrayList<String>();
			List<String> publishers = new ArrayList<String>();
			initialManager.doLoad(codes, years, publishers);
                        System.out.println("Codes: " + codes.size());
                        System.out.println("Years: " + years.size());
                        System.out.println("Publishers: " + publishers);
                        
		}catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
