package ua.com.znannya.client.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.DummySSLSocketFactory;
import org.jivesoftware.smack.XMPPException;

import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.StringUtil;

/**
 *
 */
public class XmppConnector
{
  private static XmppConnector instance = new XmppConnector();
//  private String serverHost = StringUtil.decode("d\\Y\\d]Y_aY\\\\`");//"91.192.46.115"; 
  private String serverHost = "127.0.0.1";
  private XMPPConnection _xmppConnection;

  protected XmppConnector()
  {
  }

  public static XmppConnector getInstance()
  {
	  if ( instance == null )
		  instance = new XmppConnector();
    return instance;
  }

  public synchronized XMPPConnection getConnection()
  {
    if (_xmppConnection == null || !_xmppConnection.isConnected()) {
      /*System.setProperty("smack.debugEnabled", "true");
      XMPPConnection.DEBUG_ENABLED = true;*/
	      ConnectionConfiguration xmppConfig = new ConnectionConfiguration(serverHost, 3128);
	      xmppConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
	      xmppConfig.setTruststorePath(StringUtil.decode("Z41DZDCY41D")); ///dat/ts.dat
	
	//      xmppConfig.setTruststorePath("security/truststore");
	      xmppConfig.setTruststorePassword(StringUtil.decode("J>/DBECD@1CC")); //zn_trustpass
	      xmppConfig.setCompressionEnabled(true);
	      xmppConfig.setSASLAuthenticationEnabled(true);
	      xmppConfig.setVerifyRootCAEnabled(true);
	      xmppConfig.setSelfSignedCertificateEnabled(true);
	      xmppConfig.setSocketFactory(new DummySSLSocketFactory(serverHost, xmppConfig));
	      _xmppConnection = new XMPPConnection(xmppConfig);
    	try {
    		_xmppConnection.connect();
    	}
    	catch (XMPPException ex) {
    		ErrorUtil.showError(ZnclApplication.getApplication().getUiTextResources().getString("error.connect.failed"));
    		Logger.getLogger(getClass().getName()).log(Level.WARNING, "An error occured while making a connection with server " + serverHost, ex);
    	}
      
    	if (_xmppConnection != null && _xmppConnection.isConnected()) {
    		Logger.getLogger(getClass().getName()).info("Connection was established with XMPP server " + serverHost);
//        Logger.getLogger(getClass().getName()).info("Secure connection: " + _xmppConnection.isSecureConnection());
    	}
    	ZnclApplication.getApplication().getControllerManager().getAuthRegDialogController().beginAuthorization();
    }

    return _xmppConnection;
  }
  
  public void disconnectConnection(){
	  if (_xmppConnection != null && _xmppConnection.isConnected()) {
		  _xmppConnection.disconnect();
		  instance = null;
	  }
  }
  
  public String getServerHost(){
	  return serverHost;
  }
}
