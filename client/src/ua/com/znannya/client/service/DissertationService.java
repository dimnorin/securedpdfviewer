package ua.com.znannya.client.service;

import java.io.InputStream;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.File;

/**
 * Service for finding Dissertations, obtaining Dissertation's data.
 */
public interface DissertationService
{
  final static String[] COL_NAMES = {"name", "author", "kind", "code", "organization", "city", "year", "pages", "DACNTI_code", "UDK_idx", "description"};
  final static int PAGE_SIZE = 20;     // maximum found dissertations to display on one page

  /**
   * Simple search by just one string parameter.
   * @param criterion search string
   * @return count of Dissertations found
   */
  EntriesCollection findSimple(String criterion);
  EntriesCollection findSimple(String criterion, int startIndex, int fetchSize, String orderField, boolean ascending, boolean isFeatch);
  
  /**
   * Complex search by set of parameters incapsulated in criteria class.
   * @param criteria a set of search parameters
   * @return count of Dissertations found
   */
  EntriesCollection findComplex(ComplexSearchCriteria criteria);
  EntriesCollection findComplex(ComplexSearchCriteria criteria, int startIndex, int fetchSize, String orderField, boolean ascending, boolean isFeatch);

  /**
   * Get a list of files belonging to a Dissertation.
   * @param dissertation
   * @return List of Files
   */
  List<File> getFileList(Entry dissertation);

  /**
   * Fetch actual content of a given File.
   * @param file
   * @return file's content as a stream of bytes.
   */
  void requestFileContent(File file, String pages) throws XMPPException;
}
