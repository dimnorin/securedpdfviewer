package ua.com.znannya.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;

/**
 *
 */
public class TestAuthRegService implements AuthRegService
{
  private Map<String, String> credentials = new HashMap<String, String>();
  
  public TestAuthRegService()
  {
    credentials.put("test", "111");   // sample data
  }

  public XMPPException authenticate(String login, String password)
  {
    String pass = credentials.get(login);
    if (pass != null && pass.equals(password)) {
      Logger.getLogger(getClass().getName()).info("Logged in user: " + login);
      return null;
    }
    return new XMPPException();
  }

  public void register(UserData userData)
  {
    credentials.put(userData.getLogin(), userData.getPassword());
    Logger.getLogger(getClass().getName()).info("Registered user " + userData);
  }

}
