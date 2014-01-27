package ua.com.znannya.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.UserInfo;
import org.jivesoftware.smack.znannya.UserInfoManager;

public class TestUserInfo {
//	private String server = "127.0.0.1";
	private String server = "91.192.46.115";

	public static void main(String[] args) {
		new TestUserInfo();

	}

	public TestUserInfo(){
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
		
		UserInfoManager userInfoManager = new UserInfoManager(userXmppConnection);
		try{
			UserInfo userInfo = userInfoManager.getUserInfo();
			UserInfo userInfo1 = userInfoManager.setUserInfo(userInfo.getName(), null, null, "Admin Admin2", "Admin2 Inc.", "Administrator2", "1234567892", "admin2@aaa.com", "This is extra info2");
			System.out.println("User info="+userInfo);
			System.out.println("User info1="+userInfo1);

		}catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
