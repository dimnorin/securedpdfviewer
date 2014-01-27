package ua.com.znannya.client.app;

import ua.com.znannya.client.ctrl.AdviceOfDayDialogController;
import ua.com.znannya.client.ctrl.PrivateRoomDialogController;
import ua.com.znannya.client.ctrl.RefundAccountDialogController;
import ua.com.znannya.client.ctrl.AuthRegDialogController;
import ua.com.znannya.client.ctrl.ChoosePagesDialogController;
import ua.com.znannya.client.ctrl.ComplexSearchDialogController;
import ua.com.znannya.client.ctrl.DocumentsPaneController;
import ua.com.znannya.client.ctrl.FileDownloadDialogController;
import ua.com.znannya.client.ctrl.MainFrameController;

/**
 * Manages creation and neccessary initialization of Controller classes.
 * Controller instances are created on demand.
 */
public class ControllerManager
{
  private MainFrameController mainFrameController;
  private ComplexSearchDialogController complexSearchDialogController;
  private AuthRegDialogController authRegDialogController;
  private DocumentsPaneController documentsPaneController;
  private FileDownloadDialogController fileDownloadDialogController;
  private RefundAccountDialogController refundAccountDialogController;
  private ChoosePagesDialogController chPgsController;
  private PrivateRoomDialogController privateRoomDialogController;
  private AdviceOfDayDialogController adviceDayDialogController;
  
  public ControllerManager()
  {
    
  }

  public MainFrameController getMainFrameController()
  {
    if (mainFrameController == null) {
      mainFrameController = new MainFrameController(this);
    }
    return mainFrameController;
  }

  public ComplexSearchDialogController getComplexSearchDialogController()
  {
    if (complexSearchDialogController == null) {
      complexSearchDialogController = new ComplexSearchDialogController();
    }
    return complexSearchDialogController;
  }

  public AuthRegDialogController getAuthRegDialogController()
  {
    if (authRegDialogController == null) {
      authRegDialogController = new AuthRegDialogController();
    }
    return authRegDialogController;
  }

  public DocumentsPaneController getDocumentsPaneController()
  {
    if (documentsPaneController == null) {
      documentsPaneController = new DocumentsPaneController();
    }
    return documentsPaneController;
  }

  public FileDownloadDialogController getFileDownloadDialogController()
  {
    if (fileDownloadDialogController == null) {
      fileDownloadDialogController = new FileDownloadDialogController();
    }
    return fileDownloadDialogController;
  }
  
  public RefundAccountDialogController getRefundAccountDialogController() {
	if (refundAccountDialogController == null)
		refundAccountDialogController = new RefundAccountDialogController();
	  return refundAccountDialogController;
  }
  
  public ChoosePagesDialogController getChoosePagesController() {
	if (chPgsController == null)
		chPgsController = new ChoosePagesDialogController();
	 return chPgsController;
  }

  	public PrivateRoomDialogController getPrivateRoomDialogController() {
		if ( privateRoomDialogController == null )
			privateRoomDialogController = new PrivateRoomDialogController(this);
		return privateRoomDialogController;
	}
  	
    public AdviceOfDayDialogController getAdviceDayDialogController() {
    	if ( adviceDayDialogController == null )
    		adviceDayDialogController = new AdviceOfDayDialogController();
    	return adviceDayDialogController;
    }
}
