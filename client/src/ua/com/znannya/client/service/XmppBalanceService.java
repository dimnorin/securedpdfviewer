package ua.com.znannya.client.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

import ua.com.znannya.client.app.XmppConnector;

public class XmppBalanceService implements IBalanceService {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	public String getAvailableBalance() {
		XMPPConnection conn = XmppConnector.getInstance().getConnection();
		TimeTrackManager timeTrackManager = new TimeTrackManager(conn);
		String balance = "0";
		try {
			balance = timeTrackManager.getBalance();
		} catch (XMPPException e) {
			logger.log(Level.WARNING, "Error getting balance from server.", e);
		}
		return balance;
	}

}
