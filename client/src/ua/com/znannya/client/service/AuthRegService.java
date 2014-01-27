package ua.com.znannya.client.service;

import org.jivesoftware.smack.XMPPException;

/**
 * Service for users authentication and registration.
 */
public interface AuthRegService
{
  /**
   * Authenticate a user on server with login and password.
   * @param login
   * @param password
   * @return true if authentication was successfull
   */
	XMPPException authenticate(String login, String password);
  
  /**
   * Register a new user on server.
   * @param userData
   */
  void register(UserData userData);
}
