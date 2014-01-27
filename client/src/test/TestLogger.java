package test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.sasl.SASLMechanism;

public class TestLogger {
	private Logger logger = Logger.getLogger(TestLogger.class.getName()); 
	
	public static void main(String[] args) {
		TestLogger app = new TestLogger();
		app.log("Test string");
		app.calculateTime();
		app.initMechanism();
	}

	protected TestLogger() {
		Logger logger = Logger.getLogger("test");
		logger.setLevel(Level.ALL);

		try {
			logger.addHandler(new FileHandler("test"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Handler h : logger.getHandlers()) {
			h.setLevel(Level.ALL);
		}
	}

	private void log(String s) {
		logger.log(Level.INFO, s);
	}
	
	private void initMechanism(){
		try{
			Class mechanismClass = CustomMechanism.class;
	        Constructor constructor = mechanismClass.getConstructor(String.class);
	        ParentMechanism currentMechanism = (ParentMechanism) constructor.newInstance("OLALA");
	        logger.log(Level.INFO, currentMechanism.getName());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy");
		
		Calendar c = Calendar.getInstance();
		c.set(2009, 10, 03);

		for (int i = 0; i < 5; i++) {
			c.add(Calendar.DAY_OF_YEAR, 62);
			
			logger.info("To get: "+sdf.format(c.getTime()));
		}
	}
}
