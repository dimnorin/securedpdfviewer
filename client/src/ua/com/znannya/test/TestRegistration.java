package ua.com.znannya.test;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class TestRegistration {
	private String server = "127.0.0.1";
	
	
	public static void main(String[] args){
		new TestRegistration();
	}
	
	public TestRegistration(){
		System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;

		
		ConnectionConfiguration xmppConfig = new ConnectionConfiguration(server, 5223);
		xmppConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		xmppConfig.setTruststorePath("./resources/security/truststore");
		xmppConfig.setTruststorePassword("zn_trustpass");
		xmppConfig.setCompressionEnabled(true);
		xmppConfig.setSASLAuthenticationEnabled(true);
		xmppConfig.setSocketFactory(new DummySSLSocketFactory(server, xmppConfig));// DummySSLSocketFactory can be found here

		XMPPConnection userXmppConnection = new XMPPConnection(xmppConfig);
		try{
			userXmppConnection.connect();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		AccountManager accManager = new AccountManager(userXmppConnection);
		try{
			accManager.createAccount("user1", "pass1", "Antonio Banderas", "ccc@aaa.com", "Paramaunt pictures", "star", "0123456789");
		}catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
