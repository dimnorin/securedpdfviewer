package ua.com.znannya.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.pdf.IFileDownloadStatusListener;
import org.jivesoftware.smack.znannya.pdf.PdfFileManager;
import org.jivesoftware.smack.znannya.search.FilesManager;


public class TestGetPdfFile {

	private String server = "127.0.0.1";
//	private String server = "91.192.46.115";
	
	public static void main(String[] args) {
		new TestGetPdfFile();
		System.out.println(System.getProperty("user.dir"));

	}

	public TestGetPdfFile(){
		System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;

		
		ConnectionConfiguration xmppConfig = new ConnectionConfiguration(server, 3128);
		xmppConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
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
		
		try{
			FilesManager filesManager = new FilesManager(userXmppConnection);
			File file = null;
			try{
		    	file = filesManager.getFile(1, EntryType.diss);
		    }catch (XMPPException e) {
		    	 Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error executing files get.", e);
			}
		    System.out.println("Got file:"+file);
			
		    List<File> files = new ArrayList<File>();
		    try{
		    	files = filesManager.getFiles(1, EntryType.diss);
		    }catch (XMPPException e) {
		    	 Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error executing files get.", e);
			}
			
			PdfFileManager pdfFileManager = new PdfFileManager(userXmppConnection);
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
			
			pdfFileManager.requestPdfFileContent(files.get(0), null, EntryType.diss);

		}catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
