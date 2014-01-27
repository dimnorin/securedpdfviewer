package ua.com.znannya.client.app;

import ua.com.znannya.client.service.DissertationService;
import ua.com.znannya.client.service.AuthRegService;
import ua.com.znannya.client.service.DictionaryService;
import ua.com.znannya.client.service.IBalanceService;
import ua.com.znannya.client.service.ITimeTrackService;
import ua.com.znannya.client.service.PublicationService;
import ua.com.znannya.client.service.TestAuthRegService;
import ua.com.znannya.client.service.TestDictionaryService;
import ua.com.znannya.client.service.TestDissertationService;
import ua.com.znannya.client.service.URLParseService;
import ua.com.znannya.client.service.XmppBalanceService;
import ua.com.znannya.client.service.XmppAuthRegService;
import ua.com.znannya.client.service.XmppDictionaryService;
import ua.com.znannya.client.service.XmppDissertationService;
import ua.com.znannya.client.service.XmppPublicationService;
import ua.com.znannya.client.service.XmppTimeTrackService;

/**
 * Class for management of service implementations.
 * Creates service implementation instances on demand based on "mode" constructor parameter.
 */
public class ServiceManager
{
  private Mode mode;
  private AuthRegService authRegService;
  private DissertationService dissertationService;
  private PublicationService publicationService;
  private DictionaryService dictionaryService;
  private IBalanceService balanceService;
  private ITimeTrackService ttrackService;
  private URLParseService urlParseService;
  
  public enum Mode { TEST, XMPP }

  public ServiceManager(Mode m)
  {
    mode = m;
  }

  public AuthRegService getAuthRegService()
  {
    if (authRegService == null)
    {
      switch (mode)
      {
        case TEST: authRegService = new TestAuthRegService();
        break;
        case XMPP: authRegService = new XmppAuthRegService();
      }
    }
    return authRegService;
  }

  public DissertationService getDissertationService()
  {
    if (dissertationService == null)
    {
      switch (mode)
      {
        case TEST: dissertationService = new TestDissertationService();
        break;
        case XMPP: dissertationService = new XmppDissertationService();
      }
    }
    return dissertationService;
  }
  
  public PublicationService getPublicatiopnService(){
	  if ( publicationService == null )
		  switch (mode)
		  {
		  	case XMPP: publicationService = new XmppPublicationService();
		  }
	  return publicationService;
  }

  public DictionaryService getDictionaryService()
  {
    if (dictionaryService == null)
    {
      switch (mode)
      {
        case TEST: dictionaryService = new TestDictionaryService();
        break;
        case XMPP: dictionaryService = new XmppDictionaryService();
      }
    }
    return dictionaryService;
  }
  
  public IBalanceService getBalanceService()
  {
    if (balanceService == null)
    {
      switch (mode)
      {
        case XMPP: balanceService = new XmppBalanceService();
      }
    }
    return balanceService;
  }
  
  public ITimeTrackService getTimeTrackService()
  {
    if (ttrackService == null)
    {
      switch (mode)
      {
        case XMPP: ttrackService = new XmppTimeTrackService();
      }
    }
    return ttrackService;
  }

public URLParseService getUrlParseService() {
	if ( urlParseService == null )
		urlParseService = new URLParseService();
	return urlParseService;
}
}
