package ua.com.znannya.test;

import java.text.SimpleDateFormat;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.znannya.StatsCollection;
import org.jivesoftware.smack.znannya.dao.BriefStatistics;
import org.jivesoftware.smack.znannya.stats.StatisticsManager;

public class TestStatistics {
	private String server = "127.0.0.1";

	public static void main(String[] args) {
		new TestStatistics();

	}

	public TestStatistics(){
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		StatisticsManager statsManager = new StatisticsManager(userXmppConnection);
		try{
			long from = sdf.parse("10-08-2010").getTime();
			long to = sdf.parse("15-08-2010").getTime();
			BriefStatistics statistics = statsManager.getBriefStatistics(from, to);
			System.out.println("BriefStatistics ="+statistics);
			
			StatsCollection stats = statsManager.getFullStatistics(from, to);
			System.out.println("StatsCollection ="+stats);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
