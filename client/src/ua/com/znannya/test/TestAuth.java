package ua.com.znannya.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

public class TestAuth {
	private String server = "127.0.0.1";

	public static void main(String[] args) {
		new TestAuth();

	}

	public TestAuth(){
		/*System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;*/

		
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
			
			userXmppConnection.login("user1", "pass1");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
