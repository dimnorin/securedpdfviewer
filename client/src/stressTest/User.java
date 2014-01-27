package stressTest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.znannya.PdfFile;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.dao.Publication;
import org.jivesoftware.smack.znannya.pdf.DHManager;
import org.jivesoftware.smack.znannya.pdf.IFileDownloadStatusListener;
import org.jivesoftware.smack.znannya.pdf.PdfFileManager;
import org.jivesoftware.smack.znannya.search.FilesManager;
import org.jivesoftware.smack.znannya.search.InitialLoadManager;
import org.jivesoftware.smack.znannya.search.SearchManager;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

import ua.com.znannya.client.util.StringUtil;

public class User {
	private String server = "91.192.46.115";
//	private String server = "127.0.0.1";
	private int port = 3128;
	private XMPPConnection userXmppConnection;
	
	public User(){

	}
	
	/**
	 * Set connect between unit and server 
	 * @return false if unable to connect; true if connection is established
	 */
	public boolean connect(){
		try {
			getUserXmppConnection().connect();
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * register new user 
	 * @return false if registration failed and true in other cases
	 * @throws XMPPException 
	 */
	public void register(String login, String pass, String email) throws XMPPException{
		AccountManager accManager = new AccountManager(getUserXmppConnection());
		accManager.createAccount(login, pass, login + " " + login, email, "Paramaunt pictures", "star", "0123456789");
	}
	
	public void deleteUser() throws XMPPException{
		AccountManager accManager = new AccountManager(getUserXmppConnection());
		accManager.deleteAccount();
	}
	
	/**
	 * login to the server with a login and password
	 * @param login of user
	 * @param pass of user
	 * @return false if error in login; true if login success 
	 */
	public boolean login(String login, String pass) {
		try {
			getUserXmppConnection().login(login, pass);
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @return false if initial failed; true in other case
	 * @throws XMPPException 
	 */
	public void loadInitialProperties() throws XMPPException{
		InitialLoadManager initialManager = new InitialLoadManager(userXmppConnection);
		List<String> codes = new ArrayList<String>();
		List<String> years = new ArrayList<String>();
		List<String> publishers = new ArrayList<String>();
		initialManager.doLoad(codes, years, publishers);
	}
	
	public void getKeyOverDHManager() throws XMPPException{
		new DHManager(getUserXmppConnection()).getKey();
	}
	
	public void getBalance() throws XMPPException{
		TimeTrackManager timeTrackManager = new TimeTrackManager(getUserXmppConnection());
		timeTrackManager.getBalance();
	}
	
	/**
	 * Makes the search three times after this three times to download a file
	 * @return true if download and search success else false
	 * @throws XMPPException 
	 */
	public void makeSearchAndGetFiles() throws XMPPException{
		SearchManager dissManager = new SearchManager(getUserXmppConnection());
		dissManager.doDissertationsSearch(null, null, null, "скрипник юрий", null, null, null, null, 0, 10, "year", true, true);
		dissManager.doPublicationsSearch(Publication.TYPE_BOOK, "name", null, null, null, null, null, null, null, 0, 10, "year", true, true);
		dissManager.doDissertationsSearch(null, null, null, "скрипник юрий", null, null, null, null, 0, 10, "year", true, true);

		FilesManager filesManager = new FilesManager(getUserXmppConnection());
			
		List<File> files = new ArrayList<File>();
    	files = filesManager.getFiles(1, EntryType.diss);
			
    	PdfFileManager pdfFileManager = new PdfFileManager(getUserXmppConnection());
		pdfFileManager.addListener(new IFileDownloadStatusListener() {
			@Override
			public void failedDownload(XMPPError e) {
				System.out.println("Got error:"+e);
			}

			@Override
			public void finishDownload(InputStream is, boolean isMd5Matches) {
				try{
					System.out.println("Finish download with result:"+is.available()+", isMd5Matches="+isMd5Matches);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void updateProgress(double newProgress) {
				System.out.println("Got new update:"+newProgress);
			}
		});
		
		getFile(files.get(0), null, EntryType.diss);
//		pdfFileManager.requestPdfFileContent(files.get(0), null, EntryType.diss);
//		pdfFileManager.requestPdfFileContent(files.get(0), null, EntryType.diss);
//		pdfFileManager.requestPdfFileContent(files.get(0), null, EntryType.diss);
	}
	
	private void getFile(File file, String pages, EntryType entryType){
			PdfFile reg = new PdfFile(file.getFileID(), pages, entryType);
			reg.setType(IQ.Type.GET);
			reg.setTo( getUserXmppConnection().getServiceName() );
			getUserXmppConnection().sendPacket(reg);
	}
	
	public void disconnect(){
		getUserXmppConnection().disconnect();
	}
	
	private XMPPConnection getUserXmppConnection(){
		if ( userXmppConnection == null ){
//			System.setProperty("smack.debugEnabled", "true");
//			XMPPConnection.DEBUG_ENABLED = true;
			ConnectionConfiguration xmppConfig = new ConnectionConfiguration(server, port);
			xmppConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			xmppConfig.setTruststorePath(StringUtil.decode("Z41DZDCY41D")); ///dat/ts.dat

//		      xmppConfig.setTruststorePath("security/truststore");
		    xmppConfig.setTruststorePassword(StringUtil.decode("J>/DBECD@1CC")); //zn_trustpass
		    xmppConfig.setCompressionEnabled(true);
		    xmppConfig.setSASLAuthenticationEnabled(true);
		    xmppConfig.setVerifyRootCAEnabled(true);
		    xmppConfig.setSelfSignedCertificateEnabled(true);
		    xmppConfig.setSocketFactory(new DummySSLSocketFactory(server, xmppConfig));
		    SmackConfiguration.setPacketReplyTimeout(20000);
		    userXmppConnection = new XMPPConnection(xmppConfig);
		}
		return userXmppConnection;
	}	
}
