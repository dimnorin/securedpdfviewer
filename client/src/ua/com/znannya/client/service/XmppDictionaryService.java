package ua.com.znannya.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.PropertiesCollection.Property;
import org.jivesoftware.smack.znannya.search.InitialLoadManager;

import ua.com.znannya.client.app.XmppConnector;

/**
 * A DictionaryService implementation using XMPP server.
 */
public class XmppDictionaryService implements DictionaryService
{
  private List<String> codes = new ArrayList<String>();
  private List<String> years = new ArrayList<String>();
  private List<String> publishers = new ArrayList<String>();

  public synchronized void refreshData()
  {
    XMPPConnection conn = XmppConnector.getInstance().getConnection();
    InitialLoadManager initialManager = new InitialLoadManager(conn);
    try {
      initialManager.doLoad(codes, years, publishers);
      Logger.getLogger(getClass().getName()).info("Dictionary data loaded from server.");
    }
    catch (XMPPException ex) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error getting data from server.", ex);
    }
  }

  public List<String> getCodes()
  {
    return codes;
  }

  public List<String> getYears()
  {
    return years;
  }

	public List<String> getPublishers() {
		return publishers;
	}
  
}
