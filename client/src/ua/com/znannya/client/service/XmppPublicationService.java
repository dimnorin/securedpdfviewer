package ua.com.znannya.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.dao.Publication;
import org.jivesoftware.smack.znannya.pdf.PdfFileManager;
import org.jivesoftware.smack.znannya.search.FilesManager;
import org.jivesoftware.smack.znannya.search.SearchManager;

import ua.com.znannya.client.app.XmppConnector;

public class XmppPublicationService implements PublicationService{

	private EntriesCollection doSearch(int type, String name, String description, String author,  
    		String year, String isbn, String UDK_idx, String BBK_idx, String publisherName, 
    		int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount){

	    EntriesCollection publications = null;
	    XMPPConnection conn = XmppConnector.getInstance().getConnection();
	    SearchManager publManager = new SearchManager(conn);
	    try {
	    	String sType = Publication.TYPE_BOOK;
	    	if(type == ComplexSearchCriteria.BOOKS)
	    		sType = Publication.TYPE_BOOK;
	    	else if(type == ComplexSearchCriteria.PERIODICALS)
	    		sType = Publication.TYPE_MAGAZINE;
	      publications = publManager.doPublicationsSearch(sType, name, description, author, year, isbn, UDK_idx, BBK_idx, publisherName, startIndex, numResults, orderField, isAsc, isCount);
	    }
	    catch (XMPPException ex) {
	      Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error executing publications search.", ex);
	    }
	    if (publications != null) {
	      Logger.getLogger(getClass().getName()).info("Found " + publications.getCount() + " publications");
	    }
	    return publications;
	}
	
	@Override
	public EntriesCollection findComplex(ComplexSearchCriteria criteria) {
		return findComplex(criteria, 0, PAGE_SIZE, "", true, true);
	}

	@Override
	public EntriesCollection findComplex(ComplexSearchCriteria criteria,
			int startIndex, int fetchSize, String orderField,
			boolean ascending, boolean isFeatch) {
		
	    EntriesCollection publications = doSearch(criteria.getType(), criteria.getName(), criteria.getDescription(), criteria.getAuthor(), 
	    		criteria.getPublYear(), criteria.getIsbn(), criteria.getUdk(), criteria.getBbk(), criteria.getPublicher(), 
	    		startIndex, fetchSize, orderField, ascending, isFeatch);
	    if (publications != null) {
	      return publications;
	    }
	    return new EntriesCollection();
	}

	@Override
	public List<File> getFileList(Entry publication) {
	    FilesManager filesManager = new FilesManager(XmppConnector.getInstance().getConnection());
	    List<File> files = new ArrayList<File>();
	    try{
	    	files = filesManager.getFiles(publication.getID(), EntryType.pub);
	    }catch (XMPPException e) {
	    	 Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error executing files get.", e);
		}
	    Logger.getLogger(getClass().getName()).info("Get " + files.size() + " files");
	    return files;
	}

	@Override
	public void requestFileContent(File file, String pages)
			throws XMPPException {
	    try {
			getPdfFileManager().requestPdfFileContent(file, pages, EntryType.pub);
		} catch (XMPPException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error getting pdf file.", e);
			throw e;
		}		
	}
	
	public PdfFileManager getPdfFileManager() {
			return new PdfFileManager(XmppConnector.getInstance().getConnection());	  
	}

}
