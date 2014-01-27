package ua.com.znannya.client.service;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.track.ITrackUpdatesListener;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;

public class XmppTimeTrackService implements ITimeTrackService {

	public String startTrack(long fileID, EntryType entryType) {
		XMPPConnection conn = XmppConnector.getInstance().getConnection();
		TimeTrackManager timeTrackManager = new TimeTrackManager(conn);
//		TimeTrackManager.addListener(this);
		
		String balance = "0";
		try{
			balance = timeTrackManager.startTrack(fileID, entryType);
		}catch (XMPPException e) {
			String reason = null;
			if(e.getXMPPError() != null)
				reason = e.getXMPPError().getMessage();
			stopShowingDocument(reason);
		}
		return balance;
	}

	public String stopTrack() {
		XMPPConnection conn = XmppConnector.getInstance().getConnection();
		TimeTrackManager timeTrackManager = new TimeTrackManager(conn);
//		TimeTrackManager.removeListener(this);
		
		String balance = "0";
		try{
			balance = timeTrackManager.stopTrack();
		}catch (XMPPException e) {
			String reason = null;
			if(e.getXMPPError() != null)
				reason = e.getXMPPError().getMessage();
			stopShowingDocument(reason);
		}
		return balance;
	}
	
	public void stopShowingDocument(String reason) {
		ZnclApplication.getApplication().getControllerManager().getDocumentsPaneController().getDocumentsPane().closeDocumentTabs();
		ZnclApplication.getApplication().getControllerManager().getFileDownloadDialogController().closeDialog();
		if(reason != null){
			JOptionPane.showMessageDialog(ZnclApplication.getApplication().getMainFrame(), ZnclApplication.getApplication().getUiTextResources().getString(reason));
			
		}
			
	}

	public class TrackUpdatesImpl implements ITrackUpdatesListener{

		public void gotBalanceUpdate(String availableBalance) {
			ZnclApplication.getApplication().getControllerManager().getMainFrameController().updateBalance(availableBalance);
		}

		public void gotError(XMPPError error) {
			String reason = null;
			if(error != null)
				reason = error.getMessage();
			stopShowingDocument(reason);
		}
	}
	
}
