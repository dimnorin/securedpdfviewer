package ua.com.znannya.client.security;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.ContextEventQueue;

public class EventQueueController {

	static{
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ContextEventQueue()); 
	}
	
	public static void pushEvent(int eventType){
		try{
    		Thread.sleep(eventType);
    	}catch (Exception e) {}
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(ZnclApplication.getApplication().getMainFrame(), eventType));
	}
}
