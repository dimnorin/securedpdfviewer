package org.jivesoftware.smack;

import java.util.HashMap;
import java.util.Map;

public class ZnConfiguration {
	/**
	 * Delay before next time track update
	 */
	public static final String TTRACK_DELAY 				= "zn.ttrack.delay";
	/**
	 * Cost for minute of viewing document
	 */
//	public static final String TTRACK_MINUTE_COST			= "zn.ttrack.minute.cost";
	/**
	 * Max of simultaneously open files
	 */
	public static final String MAX_OPEN_FILES				= "zn.max.open.files";
	/**
	 * Size of the chunk of the downloading file
	 */
	public static final String FILE_BLOCK_SIZE              = "zn.send.file.block.size";
	
//	public static final String ORDER_PAGE_COST              = "zn.order.page.cost";
	/**
	 * Minimal client version server can work with
	 */
	public static final String MIN_CLIENT_VERSION           = "zn.client.min.version";
	/**
	 * Time between uploaded file status update 
	 */
	public static final int FILE_STATUS_UPDATE_DELAY		= 300;
	/**
	 * Current client version hardcoded
	 */
	public static final String CURRENT_CLIENT_VERSION		= "0.1.0";
	
	
	
	 private static Map<String, String> znConfigs = new HashMap<String, String>();
	 
	 
	 public static String get(String key){
		 return znConfigs.get(key);
	 }
	 
	 public static void put(String key, String value){
		 znConfigs.put(key, value);
	 }
	 
}
