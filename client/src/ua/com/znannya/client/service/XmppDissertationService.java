package ua.com.znannya.client.service;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.pdf.PdfFileManager;
import org.jivesoftware.smack.znannya.search.FilesManager;
import org.jivesoftware.smack.znannya.search.SearchManager;

import ua.com.znannya.client.app.XmppConnector;

/**
 * DissertationService implementation for XMPP server.
 */
public class XmppDissertationService implements DissertationService
{
  public XmppDissertationService() {
	  init();
  }
  
  private void init(){
  }
  
  private EntriesCollection doSearch(String query, int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount)
  {
    EntriesCollection dissertations = null;
    XMPPConnection conn = XmppConnector.getInstance().getConnection();
    SearchManager dissManager = new SearchManager(conn);
//    try {
//      dissertations = dissManager.doSearch(query, startIndex, numResults, orderField, isAsc, isCount);
//    }
//    catch (XMPPException ex) {
//      Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error executing dissertations search.", ex);
//    }
    if (dissertations != null) {
      Logger.getLogger(getClass().getName()).info("Found " + dissertations.getCount() + " dissertations");
    }
    return dissertations;
  }

  private EntriesCollection doSearch(String name, String city, String description, String author, String DACNTI_code,
                                 String year, String code, String UDK_idx, int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount)
  {
    EntriesCollection dissertations = null;
    XMPPConnection conn = XmppConnector.getInstance().getConnection();
    SearchManager dissManager = new SearchManager(conn);
    try {
      dissertations = dissManager.doDissertationsSearch(name, city, description, author, DACNTI_code, year, code, UDK_idx, startIndex, numResults, orderField, isAsc, isCount);
    }
    catch (XMPPException ex) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error executing dissertations search.", ex);
    }
    if (dissertations != null) {
      Logger.getLogger(getClass().getName()).info("Found " + dissertations.getCount() + " dissertations");
    }
    return dissertations;
  }

  public EntriesCollection findSimple(String criterion){
	  return findSimple(criterion, 0, PAGE_SIZE, "", true, true);
  }
  
	public EntriesCollection findSimple(String criterion, int startIndex,
			int fetchSize, String orderField, boolean ascending, boolean isFeatch) {
		EntriesCollection dissertations = doSearch(criterion, startIndex,
				fetchSize, orderField, ascending, isFeatch);
		if (dissertations != null) {
			return dissertations;
		}
		return new EntriesCollection();
	}

  public EntriesCollection findComplex(ComplexSearchCriteria criteria)
  {
	  return findComplex(criteria, 0, PAGE_SIZE, "", true, true);
  }
  
  public EntriesCollection findComplex(ComplexSearchCriteria criteria,
			int startIndex, int fetchSize, String orderField, boolean ascending, boolean isCount) {
	    EntriesCollection dissertations = doSearch(criteria.getName(), criteria.getCity(),
	            criteria.getDescription(), criteria.getAuthor(), criteria.getGasnti(), 
	            criteria.getYear(), criteria.getCode(), criteria.getUgk(), startIndex, fetchSize, orderField, ascending, isCount);
	    if (dissertations != null) {
	      return dissertations;
	    }
	    return new EntriesCollection();
  }

  public List<File> getFileList(Entry dissertation)
  {
    FilesManager filesManager = new FilesManager(XmppConnector.getInstance().getConnection());
    List<File> files = new ArrayList<File>();
    try{
    	files = filesManager.getFiles(dissertation.getID(), EntryType.diss);
    }catch (XMPPException e) {
    	 Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error executing files get.", e);
	}
    Logger.getLogger(getClass().getName()).info("Get " + files.size() + " files");
    return files;
  }

  public void requestFileContent(File file, String pages) throws XMPPException
  {    
    try {
		getPdfFileManager().requestPdfFileContent(file, pages, EntryType.diss);
	} catch (XMPPException e) {
		Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error getting pdf file.", e);
		throw e;
	}
  }
  
  public PdfFileManager getPdfFileManager() {
	return new PdfFileManager(XmppConnector.getInstance().getConnection());	  
  }
  
}
