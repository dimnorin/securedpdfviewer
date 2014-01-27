package ua.com.znannya.client.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.search.FilesManager;

import ua.com.znannya.client.app.EurekaServer;
import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.util.XTEA1Util;


public class URLParseService {
	private static final String EUREKA_EXT = ".eureka";
	private EntryType entryType;
	
	private static Logger logger = Logger.getLogger(EurekaServer.class.getName());
	/**
	 * @param url link on file
	 * @return null if the format of link is not correct else return the file for a load
	 * @throws XMPPException if a file is not found
	 */
	public File parseUrl(String url) throws XMPPException{
		if ( !url.endsWith(EUREKA_EXT) )
			return null;
		String id = getCode(url);
		if(id == null) return null;
		
		int iId = XTEA1Util.decrypt(id.substring(1));
			
		FilesManager filesManager = new FilesManager(XmppConnector.getInstance().getConnection());
		try {
			return filesManager.getFile(iId, entryType);
		} catch (Exception e){
			return null;
		}
	}
	
	private String getCode(String url){
		Pattern p = Pattern.compile("(\\w*)\\W*.*\\"+EUREKA_EXT);
		String source = url.substring(url.lastIndexOf('\\')+1);
		 Matcher m = p.matcher(source);
		 if(m.find()){
			 String code = m.group(1);
			 logger.log(Level.INFO, "Parced from reference: " + code);
			 
			 if ( code.startsWith("D") || code.startsWith("d") )
					entryType = EntryType.diss;
				else
					if ( code.startsWith("P") || code.startsWith("p") )
						entryType = EntryType.pub;
					else
						return null;
			 
			 return code;
		 }
		 logger.log(Level.WARNING, "Failed to parce reference: "+source);
		 return null;
	}
	
	public EntryType getEntryType(){
		return entryType;
	}
	
/*	public static void main(String[] args){
//		URLParseService s = new URLParseService();
//		s.getCode("C:\\TemporarynternetFilesContent.IE5\\TPPEU5OD\\D851470006R946693729[1].eureka");
		
		URLParseService ups = new URLParseService();
		int id = 2;
		String res = ups.encrypt(id);
		int resI = ups.decrypt(res);
		if(id == resI)
			System.out.println("Generated string successfull for id="+resI+", string="+res);
	}*/
}
