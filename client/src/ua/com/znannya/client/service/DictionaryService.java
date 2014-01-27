package ua.com.znannya.client.service;

import java.util.List;

import org.jivesoftware.smack.packet.znannya.PropertiesCollection.Property;

/**
 * Service for obtaining reference dictionary data, f.e. values for complex search options.
 */
public interface DictionaryService
{
  /**
   * Obtains a list of Codes.
   * @return List of Strings
   */
  List<String> getCodes();

  /**
   * Obtains a list of year values suitable for Dissertation search.
   * @return List of Strings
   */
  List<String> getYears();

  /**
   * Forces service implementation to reload the data from its source.
   */
  void refreshData();
}
