package ua.com.znannya.client.ctrl;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ZnConfiguration;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.pdf.IFileDownloadStatusListener;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.DissertationService;
import ua.com.znannya.client.service.PublicationService;
import ua.com.znannya.client.service.URLParseService;
import ua.com.znannya.client.service.XmppDissertationService;
import ua.com.znannya.client.ui.FileDownloadDialog;
import ua.com.znannya.client.ui.FileDownloadDialog.TabMode;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.NumberUtil;
import ua.com.znannya.client.util.StringUtil;

/**
 * Manages FileDownloadDialog and does actual file download.
 */
public class FileDownloadDialogController extends WindowAdapter implements ActionListener {
	
	public enum DocDownloadMode {NONE,DOWNLOAD,DOWNLOAD_AND_SAVE};
	public enum PagsSelectionMode {ALL,SEVERAL};
	
	public static final byte VIEW_TAB_NUMBER = 0;
	public static final byte ORDER_TAB_NUMBER = 1;
	
	public static int countOfOpenedFiles = 0;

	
	
	private FileDownloadDialog _dialog;
	private DissertationService dissertationService;
	private PublicationService publicationService;
	private File file;
	private long time;
	private long downloadedPartSize;
	private ResourceBundle uiTextResources;
	private FileDownloadStatusListener fileDldStsListener;
	private PagsSelectionMode pagsSelMode;
	private DocDownloadMode docDwdMode;
	private EntryType entryType;

	
	private static Logger logger = Logger.getLogger(FileDownloadDialogController.class.getName());
	

	public FileDownloadDialogController() {
		dissertationService = ZnclApplication.getApplication()
				.getServiceManager().getDissertationService();
		publicationService = ZnclApplication.getApplication()
				.getServiceManager().getPublicatiopnService();
		getDialog().getBtnNo().addActionListener(this);
		getDialog().getBtnYes().addActionListener(this);
		getDialog().getOpenChoiceDlgBtn().addActionListener(this);
		getDialog().getSaveDlgBtn().addActionListener(this);
		getDialog().getRbAll().addActionListener(this);
		getDialog().getRbChoosePages().addActionListener(this);
		getDialog().getPane().addMouseListener(new TabClickListener());
		getDialog().getRbChoosePages().addMouseMotionListener(new ToolTipBtnShower());
		DissertationService ds = ZnclApplication.getApplication()
				.getServiceManager().getDissertationService();
		if (ds instanceof XmppDissertationService) {
			XmppDissertationService xmppDS = (XmppDissertationService) ds;
			fileDldStsListener = new FileDownloadStatusListener();
			xmppDS.getPdfFileManager().addListener(fileDldStsListener);
		}

		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		pagsSelMode = PagsSelectionMode.ALL;
		docDwdMode = DocDownloadMode.NONE;
	}

	public FileDownloadDialog getDialog() {
		if (_dialog == null) {
			_dialog = new FileDownloadDialog(ZnclApplication.getApplication()
					.getMainFrame());
			_dialog.addWindowListener(this);
		}
		return _dialog;
	}

	public void closeDialog(){
		docDwdMode = DocDownloadMode.NONE;
		getDialog().setVisible(false);
//		getDialog().setRbChoosePagesText("");
		getDialog().setTotalText(0, file.getDownloadCost());	
	}
	
	/**
	 * Sets the file to be downloaded and shows download confirmation dialog.
	 * After user agrees to download file, file is being downloaded in
	 * background, and on success the method showFileContent of
	 * DocumentsPaneController will be called passing the downloaded file's
	 * data.
	 */
	public void startDownload(File file, EntryType entryType) {
		this.entryType = entryType;
		int constraint = Integer.parseInt(ZnConfiguration.get(ZnConfiguration.MAX_OPEN_FILES));
		if (countOfOpenedFiles < constraint) {
			final int size = (int) (file.getSize() / 1024.0f);
			if (docDwdMode==DocDownloadMode.NONE) {
				downloadedPartSize = 0;
				this.file = file;
				setButtonsEnabled(true);
				getDialog().setFileProperties(file, pagsSelMode);
				ZnclApplication.getApplication().getControllerManager().getChoosePagesController().loadPages(file.getPages());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getDialog().setDefault(size);
						getDialog().show(FileDownloadDialog.TabMode.VIEW_AND_ORDER);					
					}
				});
			} else {
				String msgText = uiTextResources
						.getString("fileDownloadDialog.msgWaiting");
				String msgTitle = uiTextResources
						.getString("fileDownloadDialog.msgs.title");
				JOptionPane.showMessageDialog(ZnclApplication.getApplication()
						.getMainFrame(), msgText, msgTitle,
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			String msg = uiTextResources
					.getString("pdf.file.msgConstraint.text");		
			String msgTitle = uiTextResources
					.getString("pdf.file.msgConstraint.titleName");
			msg = String.format(msg,constraint);
			JOptionPane.showMessageDialog(ZnclApplication.getApplication()
					.getMainFrame(),msg, msgTitle,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void startOrderPages(){
		setButtonsEnabled(true);
		getDialog().setFileProperties(file, pagsSelMode);
		getDialog().show(FileDownloadDialog.TabMode.ORDER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getDialog().getBtnNo()) {
	       closeDialog();
		}
		if (e.getSource() == getDialog().getBtnYes()) {
			ZnclApplication.getApplication().getMainFrame().getStatusBar().setMainStatusText(
					uiTextResources.getString("mainFrame.statusBar.download"));
			time = System.currentTimeMillis();

			fileDldStsListener.setDefault();
			if(getDialog().getTabMode() == TabMode.VIEW_AND_ORDER)
				if (getDialog().getPane().getSelectedIndex() == 0)
					docDwdMode = DocDownloadMode.DOWNLOAD;
				else
					docDwdMode = DocDownloadMode.DOWNLOAD_AND_SAVE;
			else if(getDialog().getTabMode() == TabMode.ORDER)
				docDwdMode = DocDownloadMode.DOWNLOAD_AND_SAVE;
			else if(getDialog().getTabMode() == TabMode.VIEW)
				docDwdMode = DocDownloadMode.DOWNLOAD;
			try{
				String pages = null;
				int selectedPagesCount = 0;
				if(docDwdMode.equals(DocDownloadMode.DOWNLOAD_AND_SAVE)){
					
					java.io.File saveFile = new java.io.File(getDialog().getPath());
					if (saveFile.exists())
						showRewriteFileDialog();										
					if(pagsSelMode.equals(PagsSelectionMode.ALL)){
						pages = "ALL";
						selectedPagesCount = file.getPages();
					}else if(pagsSelMode.equals(PagsSelectionMode.SEVERAL)){
						pages = ZnclApplication.getApplication().getControllerManager().getChoosePagesController().getSelectedPagesNumbersText();
						selectedPagesCount = ZnclApplication.getApplication().getControllerManager().getChoosePagesController().getSelectedPagesCount();
					}
					if(pages == null || "".equals(pages.trim())){
						showErrorDialog(uiTextResources.getString("fileDownloadDialog.orderPane.pages.not.selected"));
						docDwdMode = DocDownloadMode.NONE;
						return;
					}
					float pageCost = file.getDownloadCost();
					float balance = ZnclApplication.getApplication().getControllerManager().getMainFrameController().getBalance();
					if(balance < pageCost * selectedPagesCount){
						showErrorDialog(uiTextResources.getString("pdf.pages.money.not.enough"));
						docDwdMode = DocDownloadMode.NONE;
						return;
					}
				}
				setButtonsEnabled(false);
				if ( entryType == EntryType.diss)
					dissertationService.requestFileContent(file, pages);
				else if( entryType == EntryType.pub)
					publicationService.requestFileContent(file, pages);
				else
					logger.log(Level.SEVERE, "Unknown entry type:"+entryType);
			}catch (Exception ex) {
				logger.log(Level.SEVERE, "", ex);
				String failMsg = uiTextResources.getString("download.failed");
				showErrorDialog(failMsg);
				//docDwdMode = DocDownloadMode.NONE;
				//getDialog().setVisible(false);
			}
		}
		
		if (e.getSource() == getDialog().getSaveDlgBtn()){
			showChooseSavePathDialog();
		}
		
		if (e.getSource() == getDialog().getRbAll()){
			pagsSelMode = PagsSelectionMode.ALL;
			getDialog().setTotalText(file.getPages(), file.getDownloadCost());
		}
        if (e.getSource() == getDialog().getRbChoosePages()){
        	pagsSelMode= PagsSelectionMode.SEVERAL;
        	ChoosePagesDialogController chPgsDlgCtrl = ZnclApplication.getApplication().getControllerManager().getChoosePagesController();
        	getDialog().setRbChoosePagesText(chPgsDlgCtrl.getSelectedPagesNumbersText());
        	getDialog().setTotalText(chPgsDlgCtrl.getSelectedPagesCount(), file.getDownloadCost());
        }
        if (e.getSource() == getDialog().getOpenChoiceDlgBtn()){
        	pagsSelMode= PagsSelectionMode.SEVERAL;
        	getDialog().getRbChoosePages().setSelected(true);
        	
			ZnclApplication.getApplication().getControllerManager().getChoosePagesController().showDialog();
        }
	}

	public void setButtonsEnabled(boolean enabled) {
		getDialog().getBtnNo().setEnabled(enabled);
		getDialog().getBtnYes().setEnabled(enabled);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (!(docDwdMode == DocDownloadMode.NONE)){
			String msg = uiTextResources.getString("fileDownloadDialog.msgTerminationQuestion");
			String title = uiTextResources.getString("fileDownloadDialog.windowTitle");
			int act = JOptionPane.showConfirmDialog(ZnclApplication.getApplication()
						.getMainFrame(),msg, title, JOptionPane.YES_NO_OPTION);
			switch (act) {
			case JOptionPane.OK_OPTION:{
				closeDialog();
				break;
			}			
			case JOptionPane.NO_OPTION:{
				break;
			}
			default:
				break;
			}
		}else
			closeDialog();
	}
	
    private void showErrorDialog(String msg){
    	JOptionPane.showMessageDialog(getDialog(),msg,uiTextResources.getString("error"),JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessDialog(String msg){
    	JOptionPane.showMessageDialog(getDialog(),msg,uiTextResources.getString("fileDownloadDialog.msgs.title"),JOptionPane.INFORMATION_MESSAGE);  
    }
    
   private void showFile(InputStream is){
		ZnclApplication.getApplication().getControllerManager()
				.getDocumentsPaneController().showFileContent(
						is, file, entryType);
		getDialog().setLoadText("","");
   }
    
    
   private void showChooseSavePathDialog(){
	   
	   JFileChooser jfc = new JFileChooser();
		java.io.File file = new java.io.File(getDialog().getPath());			
		jfc.setSelectedFile(file);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "PDF Files";
			}
			
			@Override
			public boolean accept(java.io.File f) {
				return f.isDirectory() || f.getName().endsWith(".pdf");
			}
		});
		//jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int state = jfc.showSaveDialog(getDialog());
				
		if (state==JFileChooser.APPROVE_OPTION){
			file = jfc.getSelectedFile();
			 if (file.exists()){
				  showRewriteFileDialog();
			   }
			String path = file.getAbsolutePath();
			if (path!=null){
				getDialog().setPath(path);
			}
		}
	   
	  
   }
   
   
   private void showRewriteFileDialog(){
	   
	   String msg = uiTextResources.getString("save.rewriteQuestion");
		String title = uiTextResources.getString("fileDownloadDialog.windowTitle");
		int act = JOptionPane.showConfirmDialog(ZnclApplication.getApplication()
					.getMainFrame(),msg, title, JOptionPane.YES_NO_OPTION);
		switch (act) {
		case JOptionPane.OK_OPTION:{						
			break;
		}			
		case JOptionPane.CANCEL_OPTION:{	
			showChooseSavePathDialog();
			break;
		}
		case JOptionPane.NO_OPTION:{
			showChooseSavePathDialog();
			break;
		}
		default:
			break;
		}
   }
   
   
   private void writeDocument(InputStream is,java.io.File file) throws Exception{
	    is.reset();
	    int n = is.available();	   
	    byte[] buffer = new byte[n]; 
	    is.read(buffer);	   
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);	
		fos.close();
		showSuccessDialog(uiTextResources.getString("save.success"));
		Desktop.getDesktop().open(file);
   }
   
   private void saveDocument(InputStream is){
	   
	   String path = getDialog().getPath();
	   java.io.File file = new java.io.File(path);
	  try {
		  	if(!file.exists())
		  		file.createNewFile();
			if (file.canWrite()){
				 writeDocument(is, file);
			}else
				showErrorDialog(uiTextResources.getString("save.failed"));
		} catch (Exception e) {
			showErrorDialog(uiTextResources.getString("save.failed"));
		}
	   }
   
    private class FileDownloadStatusListener implements
			IFileDownloadStatusListener {

		long fileSize;
		long elapsedTime;
		long remainingTime;
		String rate;
		String speed;
		String speedUnit;
		String remTime;
		String hour,sec,min;
		
		double previousProgress;

		@Override
		public void updateProgress(double newProgress) {
			if ((newProgress - previousProgress) > 0.07) {
				previousProgress = newProgress;
				computeDownloadingParams(newProgress);

				long hours = remainingTime/3600;
				long mins = (remainingTime-hours*3600)/60;
				long secs = remainingTime - mins*60;
				
				String spdTxt = speed+" - "+rate+" "+speedUnit;
				StringBuilder timeStrBuldr = new StringBuilder(remTime+" - ");
				if (hours!=0)
					timeStrBuldr.append(hours+hour);
				if (mins!=0)
					timeStrBuldr.append(mins+min);
				timeStrBuldr.append(secs+sec);
				
				getDialog().getProgressBar().setValue((int) downloadedPartSize);
				getDialog().setLoadText(spdTxt, timeStrBuldr.toString());
			}
		}

		@Override
		public void failedDownload(XMPPError e) {
			String failMsg = uiTextResources.getString("download.failed");
			showErrorDialog(failMsg);
		}

		@Override
		public void finishDownload(InputStream is, boolean isMd5Matches) {
			ZnclApplication.getApplication().getMainFrame().getStatusBar().setMainStatusText(
					uiTextResources.getString("mainFrame.statusBar.waitingForUser"));
			getDialog().getProgressBar().setValue(0);
			String msg = "";
			if (isMd5Matches) {
				msg = uiTextResources.getString("download.finishedSuccess");
				showSuccessDialog(msg);		
				if (docDwdMode == DocDownloadMode.DOWNLOAD_AND_SAVE){
					saveDocument(is);
				}else if(docDwdMode == DocDownloadMode.DOWNLOAD)
					showFile(is);
			} else {
				msg = uiTextResources.getString("download.finishedFailed");
				showErrorDialog(msg);
			}
			closeDialog();
		}

		void setDefault() {
			fileSize = (long) (file.getSize() / 1024.0f);
			//System.out.println("filesize = " + fileSize + " kyloBytes");
			previousProgress = 0.0;
			elapsedTime = 0;
			remainingTime = 0;
			speed = uiTextResources
					.getString("fileDownloadDialog.viewPane.downloadSpeed");
			speedUnit = uiTextResources
					.getString("fileDownloadDialog.viewPane.downloadSpeed.unit");
			remTime = uiTextResources
					.getString("fileDownloadDialog.viewPane.remainingTime");
			sec = uiTextResources
					.getString("fileDownloadDialog.viewPane.remainingTime.sec");
			min = uiTextResources
			.getString("fileDownloadDialog.viewPane.remainingTime.min");
			hour = uiTextResources.getString("fileDownloadDialog.viewPane.remainingTime.hour");
		}

		/**
		 * This method calculates the speed of downloads and time remaining
		 * until the download is complete, using information about the parts of
		 * the download file
		 */
		private void computeDownloadingParams(double newProgress) {
			long t = System.currentTimeMillis() - time;
			elapsedTime += t;
			time = System.currentTimeMillis();

			float dwnPrtSze = (float) (fileSize * newProgress);
			long partSize = (long) (dwnPrtSze - downloadedPartSize);

			float sec = t / 1000.0f;

			if (!NumberUtil.formatFloat(sec, 3).equalsIgnoreCase("0.000"))
				rate = NumberUtil.formatFloat((partSize / sec), 1);
			else
				rate = ">1000";

			downloadedPartSize = (long) dwnPrtSze;

			remainingTime = (long) (((((float) fileSize * elapsedTime) / downloadedPartSize) - elapsedTime)/1000.f); // in sec.
		}
	}
    
    
    private class TabClickListener extends MouseAdapter{
    	
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		try{
	    		JTabbedPane pane = getDialog().getPane();
	    		Rectangle boundsOrdTab = pane.getBoundsAt(ORDER_TAB_NUMBER);
	    		Rectangle boundsViewTab = pane.getBoundsAt(VIEW_TAB_NUMBER);
	    		Point p = e.getPoint();
	    		
	    		if (boundsOrdTab.contains(p))    			
	    			getDialog().setSize(FileDownloadDialog.ORDER_DIALOG_DIMENSION);
	    		if (boundsViewTab.contains(p)){
	    			if (docDwdMode == DocDownloadMode.NONE)
	    				getDialog().setSize(FileDownloadDialog.VIEW_DIALOG_DIMENSION);   			
	    			else
	    				getDialog().setSize(FileDownloadDialog.VIEW_DIALOG_DIMENSION_WITH_DOWNLOADING_PARAMS);  			
	    		}   
    		}catch (IndexOutOfBoundsException e1) {}
    	}
    }
    
    private class ToolTipBtnShower extends MouseMotionAdapter{
    	 	
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		JRadioButton btn = getDialog().getRbChoosePages();
    		ChoosePagesDialogController chDlgCtrl = ZnclApplication.getApplication().getControllerManager().getChoosePagesController(); 
    		String toolTipText = chDlgCtrl.getSelectedPagesNumbersText();
    		int width = btn.getFontMetrics(btn.getFont()).stringWidth(toolTipText);
    		if (width>btn.getWidth())
    		   btn.setToolTipText(StringUtil.convertTextToHTML(toolTipText));
    		else
    			btn.setToolTipText(null);
    	}    
    }

    /**
	 * Load of file if an application is started on reference
	 */
	public void startDownload(String[] args){
    	File file;
		try {
			URLParseService urlParser = ZnclApplication.getApplication().getServiceManager().getUrlParseService();
			file = urlParser.parseUrl(args[0]);
			if ( file == null ){
				JOptionPane.showMessageDialog(ZnclApplication.getApplication().getMainFrame(),
						uiTextResources.getString("authorizationDialog.msgFailLink"));
				return;
			}
			FileDownloadDialogController fddc = ZnclApplication.getApplication()
						.getControllerManager().getFileDownloadDialogController();
			fddc.startDownload(file, urlParser.getEntryType());						
		} catch (XMPPException e) {
			ErrorUtil.showError(e);
		}				    	
	}

	public File getFile() {
		return file;
	}
}
