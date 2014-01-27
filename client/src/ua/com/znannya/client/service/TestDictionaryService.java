package ua.com.znannya.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.packet.znannya.PropertiesCollection;
import org.jivesoftware.smack.packet.znannya.PropertiesCollection.Property;

/**
 * DictionaryService testing implementation. Gets fake locally generated data.
 */
public class TestDictionaryService implements DictionaryService
{
  private List<String> codes = new ArrayList<String>();
  private List<String> years = new ArrayList<String>();

  public List<String> getCodes()
  {
    pause();    
    return codes;
  }

  public List<String> getYears()
  {
    pause();
    return years;
  }


  // simulates network latency while getting data
  private void pause()
  {
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException ex) {
      Logger.getLogger(TestDissertationService.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void refreshData()
  {
    codes.clear();
    years.clear();
    pause();

    for (int i = 1; i <= 10; i++) {
      codes.add("code " + i);
    }

    for (int i = 2001; i <= 2010; i++) {
      years.add( String.valueOf(i) );
    }
  }
}
