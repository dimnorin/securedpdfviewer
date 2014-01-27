package ua.com.znannya.client.service;

import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.dao.Publication;

public interface PublicationService {
	  final static String[] COL_NAMES = {"name", "author", "isbn", "publisherName", "UDK_idx", "BBK_idx", "city", "year", "description"};
	  final static int PAGE_SIZE = 20;     // maximum found publications to display on one page

	  /**
	   * Simple search by just one string parameter.
	   * @param criterion search string
	   * @return count of Dissertations found
	   */
//	  EntriesCollection findSimple(String criterion);
//	  EntriesCollection findSimple(String criterion, int startIndex, int fetchSize, String orderField, boolean ascending, boolean isFeatch);
	  
	  /**
	   * Complex search by set of parameters incapsulated in criteria class.
	   * @param criteria a set of search parameters
	   * @return count of Dissertations found
	   */
	  EntriesCollection findComplex(ComplexSearchCriteria criteria);
	  EntriesCollection findComplex(ComplexSearchCriteria criteria, int startIndex, int fetchSize, String orderField, boolean ascending, boolean isFeatch);

	  /**
	   * Get a list of files belonging to a publications.
	   * @param dissertation
	   * @return List of Files
	   */
	  List<File> getFileList(Entry publication);

	  /**
	   * Fetch actual content of a given File.
	   * @param file
	   * @return file's content as a stream of bytes.
	   */
	  void requestFileContent(File file, String pages) throws XMPPException;

}
