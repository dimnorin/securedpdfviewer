package ua.com.znannya.client.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import ua.com.znannya.client.util.StringUtil;


public class EurekaServer extends Thread{
	
	public static final String portFileName 				= System.getProperty("user.dir") + File.separator + "server";

	private String portFile;
	private ServerSocket socket;
	private int authKey;
	private boolean ok;
	private boolean abort;
	
	private static Logger logger = Logger.getLogger(EurekaServer.class.getName());
	
	EurekaServer(String portFile)
	{
		super("Eureka server daemon [" + portFile + "]");
		setDaemon(true);
		this.portFile = portFile;
		new File(portFile).deleteOnExit();

		try
		{
			// Bind to any port on localhost; accept 2 simultaneous
			// connection attempts before rejecting connections
			socket = new ServerSocket(0, 2, InetAddress.getByName("127.0.0.1"));
			authKey = new Random().nextInt(Integer.MAX_VALUE);
			int port = socket.getLocalPort();

			FileWriter out = new FileWriter(portFile);

			try
			{
				out.write("b\n");
				out.write(String.valueOf(port));
				out.write("\n");
				out.write(String.valueOf(authKey));
				out.write("\n");
			}
			finally
			{
				out.close();
			}

			ok = true;

			logger.log(Level.INFO ,"Eureka server started on port "
				+ socket.getLocalPort());
			logger.log(Level.INFO, "Authorization key is "
				+ authKey);
			start();
		}
		catch(IOException io)
		{
			/* on some Windows versions, connections to localhost
			 * fail if the network is not running. To avoid
			 * confusing newbies with weird error messages, log
			 * errors that occur while starting the server
			 * as NOTICE, not ERROR */
			logger.log(Level.WARNING, "", io);
		}
	} 

	public void run()
	{
		for(;;)
		{
			if(abort)
				return;

			Socket client = null;
			try
			{
				client = socket.accept();

				// Stop script kiddies from opening the edit
				// server port and just leaving it open, as a
				// DoS
				client.setSoTimeout(1000);

				logger.log(Level.INFO, client + ": connected");

				DataInputStream in = new DataInputStream(client.getInputStream());

				if(!handleClient(client,in))
					abort = true;
			}
			catch(Exception e)
			{
				if(!abort)
					logger.log(Level.SEVERE,"",e);
//				abort = true;
			}
			finally
			{
				/* if(client != null)
				{
					try
					{
						client.close();
					}
					catch(Exception e)
					{
						Log.log(Log.ERROR,this,e);
					}

					client = null;
				} */
			}
		}
	} 

	/**
	 * @param args A list of files. Null entries are ignored, for convinience
	 * @return whether download dialog is opened
	 */
	public static boolean handleClient(String[] args)
	{
		if ( args.length > 0 ){
			logger.log(Level.INFO,"Trying to open reference for: "+args[0]);
	    	ZnclApplication.getApplication().getControllerManager()
	    				.getFileDownloadDialogController().startDownload(args);
	    	return true;
	    }
		return false;
	} 

	boolean isOK()
	{
		return ok;
	}

	public int getPort()
	{
		return socket.getLocalPort();
	}

	void stopServer()
	{
		abort = true;
		try
		{
			socket.close();
		}
		catch(IOException io)
		{
		}

		new File(portFile).delete();
	} 

	//Private members

	private boolean handleClient(final Socket client, DataInputStream in)
		throws Exception
	{
		int key = in.readInt();
		if(key != authKey)
		{
			logger.log(Level.WARNING,client + ": wrong"
				+ " authorization key (got " + key
				+ ", expected " + authKey + ")");
			in.close();
			client.close();

			return false;
		}
		else
		{
			// Reset the timeout
			client.setSoTimeout(0);

			logger.log(Level.WARNING, client + ": authenticated"
				+ " successfully");

			final String script = in.readUTF();
			logger.log(Level.FINE ,script);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run(){
					handleClient(new String[]{script});
				}
			});

			return true;
		}
	} //}}}

	//}}}
}
