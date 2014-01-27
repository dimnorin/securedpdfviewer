package ua.com.znannya.client.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.util.ErrorUtil;

/**
 * AuthRegService implementation for XMPP server.
 */
public class XmppAuthRegService implements AuthRegService
{

  public XMPPException authenticate(String login, String password)
  { 
    XMPPConnection conn = XmppConnector.getInstance().getConnection();
    try {
      conn.login(login, password);
      new TimeTrackManager(conn).registerErrorPacketListener();
    }catch (XMPPException ex) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "An error occured during authentication. This user cannot be authenticated.", ex);
      return ex;
    }

    if (conn.isAuthenticated()) {
      Logger.getLogger(getClass().getName()).info("User " + login + " was successfully authenticated.");
    }
    return null;
  }

  public void register(UserData ud)
  {
    XMPPConnection conn = XmppConnector.getInstance().getConnection();
    AccountManager accManager = new AccountManager(conn);
    try {
      accManager.createAccount(
        ud.getLogin(), ud.getPassword(), ud.getFirstName() + " " + ud.getLastName(),
        ud.getEmail(), ud.getOrganization(), ud.getJobTitle(), ud.getPhone()
      );
      Logger.getLogger(getClass().getName()).info("Registered new user: " + ud);
    }
    catch (XMPPException ex) {
    	ErrorUtil.showError(ex);
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error trying to register a new user on server.", ex);
    }
  }
}
