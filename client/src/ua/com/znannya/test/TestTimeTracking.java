package ua.com.znannya.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.search.InitialLoadManager;
import org.jivesoftware.smack.znannya.track.ITrackUpdatesListener;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

public class TestTimeTracking implements ITrackUpdatesListener{
	private String server = "127.0.0.1";
	
	
	public static void main(String[] args){
//		String s = String.format("%,.2f", 123456.987654f);
//		System.out.println(s);
		new TestTimeTracking();
	}
	
	public TestTimeTracking(){
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
		}catch (XMPPException e) {
			e.printStackTrace();
		}
		
		TimeTrackManager.addListener(this);
		
		try{
			TimeTrackManager timeTrackManager = new TimeTrackManager(userXmppConnection);
			timeTrackManager.startTrack(1, EntryType.diss);
			try{
				int minute = 1000 * 60;
				Thread.sleep(minute * 2);
			}catch (Exception e) {
				e.printStackTrace();
			}
			timeTrackManager.stopTrack();
			
			timeTrackManager.startTrack(2, EntryType.diss);
			timeTrackManager.stopTrack();
		}catch (XMPPException e) {
			e.printStackTrace();
		}
		
		TimeTrackManager.removeListener(this);
	}

	public void gotError(XMPPError error) {
		System.out.println("Stop showing document:"+error);
	}

	public void gotBalanceUpdate(String balance) {
		System.out.println("Balance was updated, new value:"+balance);
	}
}
