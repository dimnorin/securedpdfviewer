package ua.com.znannya.client.ctrl;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.znannya.search.InitialLoadManager;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.ComplexSearchCriteria;
import ua.com.znannya.client.ui.ComplexSearchDialog;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Manages complex search dialog, fills its list boxes.
 */
public class ComplexSearchDialogController implements ActionListener
{
  private ComplexSearchDialog _complexSearchDialog;

  public ComplexSearchDialogController()
  {
  }

  public void showDialog()
  {
    getDialog().setVisible(true);
  }

  /** Gets ComplexSearchDialog instance. If it's not exists, creates it. */
  public ComplexSearchDialog getDialog()
  {
    if (_complexSearchDialog == null) {
      _complexSearchDialog = new ComplexSearchDialog(ZnclApplication.getApplication().getMainFrame());
      setupListeners();
    }
    return _complexSearchDialog;
  }

  private void setupListeners()
  {
    _complexSearchDialog.getBtnCancel().addActionListener(this);
    _complexSearchDialog.getBtnSearch().addActionListener(this); 
    Toolkit.getDefaultToolkit().addAWTEventListener(new KeyEventListener(), AWTEvent.KEY_EVENT_MASK);
    
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source = e.getSource();
    if (source == getDialog().getBtnCancel())
    {
      getDialog().setVisible(false);
    }
    if (source == getDialog().getBtnSearch())
    {
        search();
    }
  }

  
  
  private void search(){
	  
      ComplexSearchCriteria criteria = new ComplexSearchCriteria();
      criteria.setType( getDialog().getSelectedIndxTab() );
      criteria.setAuthor(getDialog().getTfldAuthor().getText());
      criteria.setName(getDialog().getTfldName().getText());
      criteria.setDescription(getDialog().getTfldDescription().getText());
      
      int selectTab = getDialog().getSelectedIndxTab();
      if ( selectTab == ComplexSearchCriteria.DISSERTATIONS ){
          criteria.setCity(getDialog().getTfldCity().getText());
          criteria.setGasnti(getDialog().getTfldGasnti().getText());
          criteria.setUgk(getDialog().getTfldIndexUGK().getText());
          if (getDialog().getCbxCode().getSelectedItem() != GuiUtil.NONE_CBX_ITEM) {
              criteria.setCode(getDialog().getCbxCode().getSelectedItem().toString());
          }
          if (getDialog().getCbxRegYear().getSelectedItem() != GuiUtil.NONE_CBX_ITEM) {
              criteria.setYear(getDialog().getCbxRegYear().getSelectedItem().toString());
          }
      } else {
    	  criteria.setIsbn( getDialog().getTfldISBN().getText() );
    	  if (getDialog().getCbxPublichers().getSelectedItem() != GuiUtil.NONE_CBX_ITEM) 
    		  criteria.setPublicher( getDialog().getCbxPublichers().getSelectedItem().toString() );
          if (getDialog().getCbxPublYear().getSelectedItem() != GuiUtil.NONE_CBX_ITEM) 
              criteria.setPublYear(getDialog().getCbxPublYear().getSelectedItem().toString());
          if ( selectTab == ComplexSearchCriteria.BOOKS ){
        	  criteria.setUdk( getDialog().getTfldUDK().getText() );
        	  criteria.setBbk( getDialog().getTfldBBK().getText() );
          }
      }
      
      ZnclApplication.getApplication().getControllerManager().getDocumentsPaneController().doComplexSearch(criteria);
      getDialog().setVisible(false);
      ZnclApplication.getApplication().getControllerManager().getMainFrameController().showDocumentsPane();
  }
  
  
  /** 
   * Fills combos with reference data. Returns immediately as everything is 
   * processed in background worker threads.
   */
  public void fillLists()
  {
	  InitialLoadManager initLoadManager = new InitialLoadManager(XmppConnector.getInstance().getConnection());
	  try {
		  List<String> codes = new ArrayList<String>();
		  List<String> years = new ArrayList<String>();
		  List<String> publishers = new ArrayList<String>();
		  initLoadManager.doLoad(codes, years, publishers);
		  GuiUtil.fillCombo( getDialog().getCbxCode(), codes, GuiUtil.NONE_CBX_ITEM);
		  GuiUtil.fillCombo( getDialog().getCbxRegYear(), years, GuiUtil.NONE_CBX_ITEM);
		  GuiUtil.fillCombo( getDialog().getCbxPublYearBooks(), years, GuiUtil.NONE_CBX_ITEM);
		  GuiUtil.fillCombo( getDialog().getCbxPublYearPeriodicls(), years, GuiUtil.NONE_CBX_ITEM);
		  GuiUtil.fillCombo( getDialog().getCbxPublichersBooks(), publishers, GuiUtil.NONE_CBX_ITEM);
		  GuiUtil.fillCombo( getDialog().getCbxPublichersPerioicls(), publishers, GuiUtil.NONE_CBX_ITEM);
	  } catch (XMPPException e) {
		  ErrorUtil.showError(e);
	  }

//    final DictionaryService ds = ZnclApplication.getApplication().getServiceManager().getDictionaryService();
//
//    ds.refreshData();
//
//
//    SwingWorker<List<String>, Void> codesWorker = new SwingWorker<List<String>, Void>()
//    {
//      @Override protected List<String> doInBackground() throws Exception
//      {
//        return ds.getCodes();
//      }
//      @Override protected void done()
//      {
//        try {
//          GuiUtil.fillCombo(getDialog().getCbxCode(), get(), GuiUtil.NONE_CBX_ITEM);
//        }
//        catch (Exception ex) {
//          Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
//      }
//    };
//    codesWorker.execute();
//
//    SwingWorker<List<String>, Void> yearsWorker = new SwingWorker<List<String>, Void>()
//    {
//      @Override protected List<String> doInBackground() throws Exception
//      {
//        return ds.getYears();
//      }
//      @Override protected void done()
//      {
//        try {
//          GuiUtil.fillCombo(getDialog().getCbxRegYear(), get(), GuiUtil.NONE_CBX_ITEM);
//          GuiUtil.fillCombo(getDialog().getCbxPublYearBooks(), get(), GuiUtil.NONE_CBX_ITEM);
//          GuiUtil.fillCombo(getDialog().getCbxPublYearPeriodicls(), get(), GuiUtil.NONE_CBX_ITEM);
//        }
//        catch (Exception ex) {
//          Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
//      }
//    };
//    yearsWorker.execute();

  }
  
  
   private class KeyEventListener implements AWTEventListener {
	  
	   /**
	    * This method handles the pressing key 'Enter' at the time 
	    * when the dialogue ''complexSearchDialog  is active. 
	    */
		public void eventDispatched(AWTEvent event) {
			if (_complexSearchDialog.isActive()) {
				if (event instanceof KeyEvent) {
					KeyEvent keyEvent = (KeyEvent) event;
					if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
						search();
				}
			}

		}

	}

}
