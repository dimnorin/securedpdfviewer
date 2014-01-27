package ua.com.znannya.client.app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.jivesoftware.smack.ZnConfiguration;

import ua.com.znannya.client.security.EventQueueController;
import ua.com.znannya.client.security.SecurityController;
import ua.com.znannya.client.ui.MainFrame;
import ua.com.znannya.client.ui.widgets.PdfViewerComponent;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Main application class. Singleton.
 */
public class ZnclApplication implements Runnable
{
  private static ZnclApplication application;
  private ControllerManager controllerManager;
  private ServiceManager serviceManager;
  private EurekaServer server;
  private ResourceBundle uiTextRes;
  private static String[] args;
  
  private static Logger logger = Logger.getLogger(ZnclApplication.class.getName());
  
  protected ZnclApplication()
  {
    controllerManager = new ControllerManager();
    serviceManager = new ServiceManager(ServiceManager.Mode.XMPP);
    new LogManager();
  }

  /** Gets app's class instance. */
  public static ZnclApplication getApplication()
  {
    if (application == null) {
      application = new ZnclApplication();
    }
    return application;
  }

  /** Get main app's frame. */
  public MainFrame getMainFrame()
  {
    return getControllerManager().getMainFrameController().getMainFrame();
  }

  public ControllerManager getControllerManager()
  {
    return controllerManager;
  }

  public ServiceManager getServiceManager()
  {
    return serviceManager;
    
  }

  public String getLastVersion(){
	  return ZnConfiguration.CURRENT_CLIENT_VERSION;
  }
  
  public ResourceBundle getUiTextResources()
  {
	  if ( uiTextRes == null ){
		  Locale locale = Locale.getDefault();
		  if(locale.getLanguage().equalsIgnoreCase("ru"))
			  locale = new Locale("ru");
		  else if(locale.getLanguage().equalsIgnoreCase("en"))
			  locale = new Locale("en");
		  else 
			  locale = new Locale("uk");
//		  if(!locale.getLanguage().equalsIgnoreCase("ru") && !locale.getLanguage().equalsIgnoreCase("uk") && !locale.getLanguage().equalsIgnoreCase("en"))
//			  locale = new Locale("uk");
//			  locale = new Locale("ru");
		  Locale.setDefault(locale);
		  uiTextRes = ResourceBundle.getBundle("i18n.ui_text", locale);
	  }
    return uiTextRes;
  }
  
  public void setUiTextResources (Locale locale){
	  Locale.setDefault(locale);
	  uiTextRes = ResourceBundle.getBundle("i18n.ui_text", locale);
  }

  public ImageIcon getIcon(String iconName)
  {
    URL url = ZnclApplication.class.getResource("/icons/" + iconName);
    return new ImageIcon(url);
  }

  public void run()
  {
	  logger.log(Level.INFO, "Starting app");
	  askServerToOpenFileIfLaunched();
	  initServer();
	    
	  getControllerManager().getMainFrameController().createAndShowMainFrame();
	  ResourceBundle uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	  ZnclApplication.getApplication().getMainFrame().getStatusBar().
					setMainStatusText( uiTextResources.getString("mainFrame.statusBar.connectToServer") );	
	  XmppConnector.getInstance().getConnection(); //connect to server
  }

  /**
   * Do not use this one, use askToClose() instead
   */
  public void close()
  {
	  XmppConnector.getInstance().disconnectConnection();
	  server.stopServer();
	  logger.log(Level.INFO, "Closing application.");
	  System.exit(0);
  }
  
  public void askToClose(){
	  EventQueueController.pushEvent(201);
  }

  public static void main(String[] args)
  {
	  ZnclApplication.args = args;
	  GuiUtil.setSysLAF();
	  ZnclApplication app = ZnclApplication.getApplication();
	  SwingUtilities.invokeLater(app);
  }
  
  public String[] getArgs(){
	  return args;
  }
  
  public void clearArgs(){
	  args = new String[0];
  }
  
  private void initServer(){
	  server = new EurekaServer(EurekaServer.portFileName);
	  if(!server.isOK())
		  server = null;
  }
  
  private void askServerToOpenFileIfLaunched(){
	  if(new File(EurekaServer.portFileName).exists())
		{
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(EurekaServer.portFileName));
				String check = in.readLine();
				if(!check.equals("b"))
					throw new Exception("Wrong port file format");

				int port = Integer.parseInt(in.readLine());
				int key = Integer.parseInt(in.readLine());

				Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),port);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				if(args.length > 0){
					out.writeInt(key);
					String script = args[0];
					out.writeUTF(script);
				}

				in.close();
				out.close();

				System.exit(0);
			}catch(Exception e){
				// ok, this one seems to confuse newbies
				// endlessly, so log it as NOTICE, not
				// ERROR
				logger.log(Level.WARNING,"An error occurred"
					+ " while connecting to the Eureka server instance.", e);
				new File(EurekaServer.portFileName).delete();
			}
		}
  }
}
