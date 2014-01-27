package ua.com.znannya.client.app;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jivesoftware.smack.znannya.feedback.FeedbackManager;

import ua.com.znannya.client.util.ErrorUtil;

public class LogManager {
	private static Logger logger = Logger.getLogger("ua");
	private static Logger loggerOrg = Logger.getLogger("org");
	
	private static StringBuilder sb = new StringBuilder();
	private SimpleFormatter sf = new SimpleFormatter();
	
	public LogManager(){
		addLogHandler();
		
		logger.setLevel(Level.ALL);
	    loggerOrg.setLevel(Level.ALL);
	    
	    /*try {
	  	  logger.addHandler(new FileHandler(UALogFile));
	  	  loggerOrg.addHandler(new FileHandler(ORGLogFile));
	  	}
	  	catch (IOException e) {
	  	  e.printStackTrace();
	  	}*/
	    
	    /*for (Handler h : logger.getHandlers()) {
	      h.setLevel(Level.ALL);
	    }*/
	}
	
	  private void addLogHandler(){
		  Handler handler = new Handler() {
				
				@Override
				public void publish(LogRecord logRecord) {
					sb.append(sf.format(logRecord)).append("\n");
					if ( logRecord.getLevel() == Level.SEVERE /*&& ErrorUtil.askIfSendFeedback(logRecord)*/){
						FeedbackManager feedBackManager = new FeedbackManager(XmppConnector.getInstance().getConnection());
						feedBackManager.sendFeedback(logRecord.getMessage(), logRecord.getSourceClassName(), sb.toString() );
					}
				}
				
				@Override
				public void flush() {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void close() throws SecurityException {
					// TODO Auto-generated method stub
				}
			};
			logger.addHandler(handler);
			loggerOrg.addHandler(handler);
	  }
	  
}
