package ua.com.znannya.client.ctrl;

import java.awt.AWTEvent;
import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import javax.swing.JFrame;

import ua.com.znannya.client.app.ControllerManager;
import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.AboutDialog;
import ua.com.znannya.client.ui.ContextEventQueue;
import ua.com.znannya.client.ui.LoadDialog;
import ua.com.znannya.client.ui.MainFrame;
import ua.com.znannya.client.ui.widgets.PdfViewerComponent;
import ua.com.znannya.client.util.FileUtil;

/**
 * Controller of the main app's frame. Handles menu, toolbar actions...
 */
public class MainFrameController implements ActionListener
{
  private ControllerManager controllerManager;
  private MainFrame mainFrame;
  private boolean isDocPaneShown = false;
  
  private float balance = 0.0f;
  
	private class InitPdfThread extends Thread{
		@Override
		public void run(){
			PdfViewerComponent pvc = new PdfViewerComponent(null);
		    pvc.loadPdf(ZnclApplication.class.getResourceAsStream("/help/agreement_uk.pdf"));
		}
	}

  public MainFrameController(ControllerManager cm)
  {
    controllerManager = cm;
  }

  private void setupListeners()
  {
	  getMainFrame().getBtnFavorit().addActionListener(this);
	  getMainFrame().getBtnSearch().addActionListener(this);
	  Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
		  @Override
		  public void eventDispatched(AWTEvent event) {
			  KeyEvent keyEvent = (KeyEvent) event;
			  if ( keyEvent.getKeyCode() == KeyEvent.VK_F && keyEvent.getModifiers() == InputEvent.CTRL_MASK && keyEvent.getID() == KeyEvent.KEY_PRESSED )
				  getMainFrame().getBtnSearch().doClick();
		  }
	  }, AWTEvent.KEY_EVENT_MASK );
	  
	  getMainFrame().getBtnPrivateRoom().addActionListener(this);
	  getMainFrame().getBtnHelp().addActionListener(this);
	  getMainFrame().getMiSwitchUser().addActionListener(this);
	  getMainFrame().getMiExit().addActionListener(this);
	  getMainFrame().getBtnExit().addActionListener(this);
	  getMainFrame().getMiContent().addActionListener(this);
	  getMainFrame().getMiAdvices().addActionListener(this);
	  getMainFrame().getMiAbout().addActionListener(this);
  }

  public void createAndShowMainFrame()
  {
	  Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ContextEventQueue()); 

    mainFrame = new MainFrame();
    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    mainFrame.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
    	  ZnclApplication.getApplication().close();
      }
    });
    setupListeners();
    mainFrame.setVisible(true);
    LoadDialog.showLoadImg();
    new InitPdfThread().start();
  }

  /** Shows the documents pane in the main frame if it is not already there. */
  public void showDocumentsPane()
  {
    if (!isDocPaneShown) {
      getMainFrame().getPnlMain().add( controllerManager.getDocumentsPaneController().getDocumentsPane() );
      getMainFrame().validate();
      isDocPaneShown = true;
    }
  }
  
  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();

    if ( source == getMainFrame().getBtnFavorit() ) {
    	ZnclApplication.getApplication().getControllerManager().getDocumentsPaneController().getFavorite();
    	ZnclApplication.getApplication().getControllerManager().getMainFrameController().showDocumentsPane();
    	
    }
  
    if ( source == getMainFrame().getBtnSearch() ) {
	  controllerManager.getComplexSearchDialogController().showDialog();
  	}

  	if ( source == getMainFrame().getBtnPrivateRoom() ) {
	  controllerManager.getPrivateRoomDialogController().showDialog();
  	}

  	if ( source == getMainFrame().getBtnHelp() ) {
  		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
  		getMainFrame().getPopupHelpMenu().show(getMainFrame().getBtnHelp(), mousePoint.x, mousePoint.y);
  		getMainFrame().getPopupHelpMenu().setLocation(mousePoint);
  	}
  	
  	if ( source == getMainFrame().getBtnExit() ) {
  		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
  		getMainFrame().getPopupExitMenu().show(getMainFrame().getBtnExit(), mousePoint.x, mousePoint.y);
  		getMainFrame().getPopupExitMenu().setLocation(mousePoint);
  	}
  
  	//////////////////////////////////////////////////////////////////
  	//call help function
  	if ( source == getMainFrame().getMiContent()){
  		String fileName = "help_" + Locale.getDefault().getLanguage() + ".chm";
  		URL filePath = ZnclApplication.class.getResource("/help/"+fileName);
  		File file = new File(fileName);
  		try {
  			FileUtil.copy(filePath.openStream(), file);
			Desktop.getDesktop().open(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
  	}
  	if ( source == getMainFrame().getMiAdvices()){
  		ZnclApplication.getApplication().getControllerManager().getAdviceDayDialogController().showDialog();
  	}
  	if ( source == getMainFrame().getMiAbout() ){
  		new AboutDialog().setVisible(true);
  	}
  	
  	//////////////////////////////////////////////////////////////////
  	//application's control
  	if ( source == getMainFrame().getMiExit() ){
		ZnclApplication.getApplication().askToClose();
  	}
  	
  	if ( source == getMainFrame().getMiSwitchUser() ){
  		controllerManager.getDocumentsPaneController().getDocumentsPane().closeAllTabs();
  		getMainFrame().getStatusBar().clearBalance();
  		ZnclApplication.getApplication().clearArgs();
  		
  		XmppConnector.getInstance().disconnectConnection();
  		XmppConnector.getInstance().getConnection();
//  		controllerManager.getAuthRegDialogController().getAuthorizationDialog().setVisible(true);
  	}  	
    /*if (source == getMainFrame().getBtnStartSimpleSearch() || source == mainFrame.getTfldSimpleSearch()) {
      controllerManager.getDocumentsPaneController().doSimpleSearch( mainFrame.getTfldSimpleSearch().getText() );
      showDocumentsPane();
    }*/
    
  }
  
  public MainFrame getMainFrame(){
	  return mainFrame;
  }
  
  public void updateBalance(String available){
	  balance = Float.parseFloat(available/*.replaceAll(",", "")*/);
	  getMainFrame().getStatusBar().setAvailableFunds(available);
  }
  
  public float getBalance(){
	  return balance;
  }
}
