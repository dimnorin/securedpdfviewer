package ua.com.znannya.client.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.File;

/**
 * Testing implementation of DissertationService.
 */
public class TestDissertationService implements DissertationService
{
  private List<Dissertation> dissList = new ArrayList<Dissertation>();
  private List<File> dfList = new ArrayList<File>();

  public TestDissertationService()
  {
    fillDissList();
    Collections.shuffle(dissList);
    dfList.add( new File(1, 1, "f name 1", "ru", 10, "author 1") );
    dfList.add( new File(2, 1, "f name 2", "ru", 20, "author 1") );
    dfList.add( new File(3, 1, "f name 3", "uk", 30, "author 2") );
  }

  public EntriesCollection findSimple(String criterion)
  {
    Logger.getLogger(getClass().getName()).info("Found " + dissList.size());
    EntriesCollection dc = new EntriesCollection();
    dc.setCount(dissList.size());
    dc.add(dissList.get(0));
    return dc;
  }

  public EntriesCollection findComplex(ComplexSearchCriteria criteria)
  {
    Logger.getLogger(getClass().getName()).info("Found " + dissList.size());
    EntriesCollection dc = new EntriesCollection();
    dc.setCount(dissList.size());
    dc.add(dissList.get(0));
    return dc;
  }

  public List<Entry> fetchFoundDissertations(int startIndex, int fetchSize, String orderField, boolean ascending, boolean isCount)
  {
    List<Entry> resultList = new ArrayList<Entry>(dissList);

    if (orderField != null) {
      Collections.sort(resultList, new DissComparator(orderField, ascending));
    }

    if (startIndex < 1) {
      startIndex = 1;
    }
    int endIndex = startIndex + fetchSize - 1;
    if (endIndex > resultList.size()) {
      endIndex = resultList.size();
    }
    
    Logger.getLogger(getClass().getName()).info("Fetching starting at " + startIndex);
    return resultList.subList(startIndex - 1, endIndex);
  }

  public List<File> getFileList(Entry dissertation)
  {
    return dfList;
  }

  public InputStream getFileContent(File disserFile)
  {
    /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      InputStream is = getClass().getResourceAsStream("/ua/com/znannya/client/resources/test/test.pdf");
      int c;
      while ((c = is.read()) != -1) {
        baos.write(c);
      }
      baos.close();
      is.close();
    }
    catch (IOException ex) {
      Logger.getLogger(TestDissertationService.class.getName()).log(Level.SEVERE, null, ex);
    }*/
    try {
      Thread.sleep(2000);   // simulate network latency while getting file data
    }
    catch (InterruptedException ex) {
      Logger.getLogger(TestDissertationService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return getClass().getResourceAsStream("/ua/com/znannya/client/resources/test/test.pdf");
  }


  private void fillDissList()
  {
    for (int i = 1; i < 100; i++)
    {
      Dissertation d = new Dissertation();
      d.setAuthor("author " + i);
      d.setCity("city " + i);
      d.setCode("code " + i);
      d.setDescription("diss desc " + i);
      d.setDACNTI_code("gasnti " + i);
      d.setName("name " + i);
      d.setOrganization("org " + i);
      d.setPages(i * 10);
      d.setKind("kind " + i);
      d.setUDK_idx("UDK " + i);
      d.setYear(String.valueOf(2000 + i));
      dissList.add(d);
    }
  }

public EntriesCollection findComplex(ComplexSearchCriteria criteria,
		int startIndex, int fetchSize, String orderField, boolean ascending, boolean isCount) {
	// TODO Auto-generated method stub
	return null;
}

public EntriesCollection findSimple(String criterion, int startIndex,
		int fetchSize, String orderField, boolean ascending, boolean isCount) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void requestFileContent(File file, String pages) throws XMPPException {
	// TODO Auto-generated method stub
	
}

}


