package ua.com.znannya.test;

import java.math.BigInteger;
import java.util.Random;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.znannya.feedback.FeedbackManager;

import sun.security.provider.SecureRandom;

public class TestFeedback {

	private String server = "127.0.0.1";

	public static void main(String[] args) {
		new TestFeedback();
	}

	public TestFeedback(){
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
		
		String str = new BigInteger(100000, new Random()).toString(32);

		
		FeedbackManager feedbackManager = new FeedbackManager(userXmppConnection);
		feedbackManager.sendFeedback("Something going wrong", "ua.com.znannya.test.TestFeedback", str);
	}

}
