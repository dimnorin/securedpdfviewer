package ua.com.znannya.client.ctrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.AdviceOfDayDialog;

public class AdviceOfDayDialogController implements ActionListener{
	private final String ADVICE_PROPERTIES_PATH = "/help/advices";
	private final String INI_LOCAL_PATH = "./config.cfg";
	private final String ADVICE_IMAGES_PATH = "/icons/";
	
	private AdviceOfDayDialog adviceDayDialog = null;
	private Properties properties;
	private Properties localProperties;
	private boolean showStart;
	
	private int countAdvaces;
	private int currAdvace;
	
	public AdviceOfDayDialogController(){
		
		getAdviceDayDialog().getBtnClose().addActionListener( this );
		getAdviceDayDialog().getBtnNextAdvice().addActionListener( this );
		getAdviceDayDialog().getBtnPrevAdvice().addActionListener( this );
		getAdviceDayDialog().getChkBoxShowStart().addActionListener( this );

		properties = new Properties();
		localProperties = new Properties();
		try {
				properties.load(ZnclApplication.class.getResource(ADVICE_PROPERTIES_PATH).openStream());
				if ( new java.io.File(INI_LOCAL_PATH).exists() )
					localProperties.load( new FileInputStream(INI_LOCAL_PATH) );				
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, null, e);
		}
		if ( localProperties.getProperty("advice.show") != null )
			showStart = Boolean.parseBoolean( localProperties.getProperty("advice.show") );
		else {
			showStart = true;
		}
		countAdvaces = properties.size() / 4;
		currAdvace = new Random().nextInt( countAdvaces  ) + 1;
	}
	
	private void changeAdvice(){
		String advice = properties.getProperty(Locale.getDefault().getLanguage()+".advice." + currAdvace + ".text");
		String imagePath = ADVICE_IMAGES_PATH + properties.getProperty(Locale.getDefault().getLanguage()+".advice." + currAdvace + ".img");
		getAdviceDayDialog().setAdvice(advice, imagePath);
	}
	
	/**
	 * show the dialog of advice if flag "advice.show" in resource is true
	 */
	public void startShowDialog(){
		if ( showStart ){
			changeAdvice();
			getAdviceDayDialog().setVisible(true);
		}
	}
	/**
	 * show the dialog of advice and ignore flag "advice.show" in resource 
	 */
	public void showDialog(){
		if ( !showStart )
			getAdviceDayDialog().getChkBoxShowStart().setSelected(false);
		else
			getAdviceDayDialog().getChkBoxShowStart().setSelected(true);
		
		changeAdvice();
		getAdviceDayDialog().setVisible(true);
	}

	public AdviceOfDayDialog getAdviceDayDialog() {
		if ( adviceDayDialog == null )
			adviceDayDialog = new AdviceOfDayDialog( ZnclApplication.getApplication().getMainFrame() );
		return adviceDayDialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if ( src == getAdviceDayDialog().getBtnClose() )
			adviceDayDialog.dispose();
		
		if  ( src == getAdviceDayDialog().getBtnNextAdvice() ){
			currAdvace++;
			if ( currAdvace > countAdvaces )
				currAdvace = 1;
			changeAdvice();
		}
		
		if ( src == getAdviceDayDialog().getBtnPrevAdvice() ){
			currAdvace--;
			if ( currAdvace < 1 )
				currAdvace = countAdvaces;
			changeAdvice();
		}
		
		if ( src == getAdviceDayDialog().getChkBoxShowStart() ){
			if ( getAdviceDayDialog().getChkBoxShowStart().isSelected() )
				localProperties.put("advice.show", "true");
			else
				localProperties.put("advice.show", "false");
			try{
				localProperties.store(new FileOutputStream(INI_LOCAL_PATH), "");
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
			} 
		}
	}
	
}
