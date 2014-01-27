package ua.com.znannya.client.util;

import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.LogRecord;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.feedback.FeedbackManager;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;

public class ErrorUtil {
	public static void showError(XMPPException ex){
		if(ex.getXMPPError() != null && ex.getXMPPError().getMessage() != null){
    		String msg =  ex.getXMPPError().getMessage() ;
    		showError(msg);
    	}
	}
	
	public static void showError(String msg){
		try{
			msg = ZnclApplication.getApplication().getUiTextResources().getString(msg);
		}catch (MissingResourceException e) {}
		JOptionPane.showMessageDialog(ZnclApplication.getApplication().getMainFrame(), msg);
	}
	
	public static boolean askIfSendFeedback(LogRecord logRecord){
		ResourceBundle uiTextRes = ZnclApplication.getApplication().getUiTextResources();
		int reply = JOptionPane.showConfirmDialog(ZnclApplication.getApplication().getMainFrame(), 
				uiTextRes.getObject("fatalError"), uiTextRes.getString("error"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		return reply == JOptionPane.YES_OPTION;
	}
}
