package org.jivesoftware.smack.znannya.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ZnConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.znannya.DH;
import org.jivesoftware.smack.packet.znannya.PdfFile;
import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.security.SecurityController;

public class PdfFileManager {
	 private static final int BUFFER_SIZE 					= 8000;
	
	private XMPPConnection connection;
	private static Timer timer = new Timer();
	private InputStream is;
	private FileTransferListener fileTransferListener;
	private FileTransferManager fileTransferManager;
	
	private long amountReaded = 0;
	private long fileSize = 0;
	private boolean isPagesMode = false;
	
	private static List<IFileDownloadStatusListener> listeners = new ArrayList<IFileDownloadStatusListener>();
	private static Logger logger = Logger.getLogger(PdfFileManager.class.getName());

	/**
	 * Creates a new InitialLoadManager instance.
	 * 
	 * @param connection a connection to a XMPP server.
	 */
	public PdfFileManager(XMPPConnection connection) {
		this.connection = connection;
	}

	
/*	public InputStream getPdfFileContent(File file) throws XMPPException {
		PdfFile reg = new PdfFile(file.getFileID());
		reg.setType(IQ.Type.GET);
		reg.setTo(connection.getServiceName());
		PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(reg);
		byte[] content = new byte[0];
		String md5 = null;
		try{
			while(content.length < file.getSize()){
				PdfFile result = (PdfFile) collector.nextResult(SmackConfiguration.getPacketReplyTimeout() * 36);
				if (result == null) {
					throw new XMPPException("No response from server.");
				} else if (result.getType() == IQ.Type.ERROR) {
					throw new XMPPException(result.getError());
				}
				if(md5 == null){
					md5 = new String(Base64.decode(result.getContent()));
					continue;
				}
				byte[] partContent = Base64.decode(result.getContent());
				byte[] nContent = new byte[content.length+partContent.length];
				System.arraycopy(content, 0, nContent, 0, content.length);
				System.arraycopy(partContent, 0, nContent, nContent.length-partContent.length, partContent.length);
				content = nContent;
				notifyGotNewPart();
			}
		}catch(XMPPException e){
			notifyFailedDownload(e.getXMPPError());
			throw e;
		}finally{
			// Stop queuing results
			collector.cancel();
		}
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content, 0, content.length);
			String md5Check = new String(digest.digest());
			notifyFinishDownload(md5.equals(md5Check));
		}catch (NoSuchAlgorithmException e) {
			throw new XMPPException(e);
		}
		
		InputStream is = new DecryptionByteArrayInputStream(content, connection.getKey());
		return is;
	}*/
	
	/**
	 * Ask server to get pdf file content.
	 * 
	 * @param fileID - file id to locate needed file
	 * @param pages - ordered pages to get from pdf file. Null can be passed to get pdf file  for view. To get all ordered pages, ALL should be passed.
	 * @throws XMPPException
	 */
	public void requestPdfFileContent(File file, String pages, EntryType entryType) throws XMPPException {
		logger.log(Level.INFO, "Request file:"+file+", pages:"+pages+", entry type:"+entryType);
		ServiceDiscoveryManager discoveryManager = new ServiceDiscoveryManager(XmppConnector.getInstance().getConnection());
		fileSize = file.getSize();
		isPagesMode = pages != null;
		
		fileTransferManager = new FileTransferManager(XmppConnector.getInstance().getConnection());
		fileTransferListener = new FileTransferListener() {
			
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				fireIncomingFileTransfer(request.accept());
			}
			
		};
		
		fileTransferManager.addFileTransferListener(fileTransferListener);
		
		PdfFile reg = new PdfFile(file.getFileID(), pages, entryType);
		reg.setType(IQ.Type.GET);
		reg.setTo(connection.getServiceName());
//		PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
//		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(reg);
		/*PdfFile result = (PdfFile) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		// Stop queuing results
		collector.cancel();
		if (result == null) {
           throw new XMPPException("No response from server.");
		}
		else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
		}*/
	}
	
	private void fireIncomingFileTransfer(IncomingFileTransfer incomingFileTransfer1){
		logger.log(Level.INFO, "Fire incoming file transfer");
		final IncomingFileTransfer incomingFileTransfer = incomingFileTransfer1;
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				if(!incomingFileTransfer.isDone())
					fireUpdateProgress(((float)amountReaded)/fileSize);
				else{
					cancel();
					if(incomingFileTransfer.getException() != null)
						fireFailedDownload(((XMPPException)incomingFileTransfer.getException()).getXMPPError());
					else{
						fireFinishDownload(is);
					}
				}
			}
		};
		
		timer.schedule(tt, ZnConfiguration.FILE_STATUS_UPDATE_DELAY, ZnConfiguration.FILE_STATUS_UPDATE_DELAY);
		
		try{
			incomingFileTransfer.setStatus(Status.in_progress);
			byte[] arr = read(incomingFileTransfer.recieveFile());
			if(!isPagesMode)
				is = new DecryptionByteArrayInputStream(arr, connection.getKey());
			else
				is = new ByteArrayInputStream(arr);
			incomingFileTransfer.setStatus(Status.complete);
			fileTransferManager.removeFileTransferListener(fileTransferListener);
		}catch(IOException ioe){
			fireFailedDownload(new XMPPError(XMPPError.Condition.interna_server_error, ioe.getMessage()));
		}catch (Exception e) {
			//do nothing
		}
	}
	
	private byte[] read(InputStream is) throws IOException{
		byte[] res = new byte[0];
		
		final byte[] b = new byte[BUFFER_SIZE];
        int count = 0;

        do {
            // read more bytes from the input stream
            count = is.read(b);
            if(count > 0){
            	byte[] prev = res;
            	res = new byte[res.length + count];
            	
            	System.arraycopy(prev, 0, res, 0, prev.length);
            	System.arraycopy(b, 0, res, prev.length, count);
            	
            	amountReaded = res.length;
            }
        } while (count >= 0);
        return res;
	}
	
	public synchronized void addListener(IFileDownloadStatusListener listener){
		listeners.add(listener);
	}
	
	public synchronized void removeListener(IFileDownloadStatusListener listener){
		listeners.remove(listener);
	}
	
	private void fireUpdateProgress(double newProgress){
		for(IFileDownloadStatusListener listener : listeners){
			listener.updateProgress(newProgress);
		}
	}
	
	private void fireFinishDownload(InputStream is){
		logger.log(Level.INFO, "Fire finis download");
		for(IFileDownloadStatusListener listener : listeners){
			if(is!=null)
				listener.finishDownload(is, true);
		}
	}
	
	private void fireFailedDownload(XMPPError e){
		logger.log(Level.INFO, "Fire failed download:"+e);
		for(IFileDownloadStatusListener listener : listeners){
			listener.failedDownload(e);
		}
	}
}
