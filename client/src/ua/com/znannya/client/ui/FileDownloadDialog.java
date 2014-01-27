package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jivesoftware.smack.znannya.dao.File;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ctrl.FileDownloadDialogController;

/**
 * Dialog for selected file download confiration.
 */
public class FileDownloadDialog extends JDialog
{
  public static final Dimension VIEW_DIALOG_DIMENSION = new Dimension(425,200);
  public static final Dimension ORDER_DIALOG_DIMENSION = new Dimension(425,240);
  public static final Dimension VIEW_DIALOG_DIMENSION_WITH_DOWNLOADING_PARAMS = new Dimension(425,220);
  
  private ResourceBundle uiTextResources;
  private String questionTemplate;
  private String priceTemplate;
  private String questionOrderTemplate;
  private String costOrderTemplate;
  private String totalOrderTemplate;
  private String saveDocumentPathTemplate;

 
  //private JPanel pnlMain = new JPanel();
  private JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
  
  private JLabel lblQuestionText = new JLabel();
  private JLabel lblAccessPriceText = new JLabel();
  private JLabel lblQuestionOrderText = new JLabel();
  private JLabel lblCostOrderText = new JLabel();
  private JLabel lblTotalText = new JLabel();
  private JLabel lblSpeedText = new JLabel();
  private JLabel lblTimeText = new  JLabel();
  private JLabel lblSaveText = new JLabel();
//  private JLabel lblPerSecond = new JLabel();
  
  private JButton btnYes, btnNo;
  private JProgressBar progressBar = new JProgressBar();

  private JPanel pnlView = new JPanel();
  private JPanel pnlOrder = new JPanel();
  private boolean resizable = false;
  

  private JRadioButton rbAll = new JRadioButton();
  private JRadioButton rbChoosePages = new JRadioButton();
  private ButtonGroup buttonGroup = new ButtonGroup();
  private JButton btOpenChoicePagesDialog = new JButton();
  private JButton btSaveDialog = new JButton();
  private JTextField fldSaveDocumentPath = new JTextField();
  
  private TabMode tabMode;
    

  public FileDownloadDialog(JFrame owner)
  {
    super(owner, true);
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
    setTitle(uiTextResources.getString("fileDownloadDialog.windowTitle"));
    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    setIconImage(ZnclApplication.getApplication().getIcon("Download.png").getImage());
    setSize(VIEW_DIALOG_DIMENSION);
    setModal(false);
    initComponents();
    layoutComponents();
    setLocationRelativeTo(owner);
  }

  private void initComponents()
  {

    questionTemplate = uiTextResources.getString("fileDownloadDialog.viewPane.questionText") + " - %s?";
    priceTemplate = uiTextResources.getString("fileDownloadDialog.price")+"- %,.2f "+uiTextResources.getString("fileDownloadDialog.currencyPerHour");
    questionOrderTemplate = uiTextResources.getString("fileDownloadDialog.orderPane.questionText");
    costOrderTemplate =  uiTextResources.getString("fileDownloadDialog.price")+" - %,.2f " +uiTextResources.getString("fileDownloadDialog.currencyPerPage") + " " + uiTextResources.getString("fileDownloadDialog.viewPane.msgPerSecond");;
    totalOrderTemplate = uiTextResources.getString("fileDownloadDialog.orderPane.totalText")+" - %,.2f " + uiTextResources.getString("fileDownloadDialog.currency");
    saveDocumentPathTemplate = uiTextResources.getString("fileDownloadDialog.orderPane.defaultPath")+"\\%s";
    
  //  setFileName("");
    btnNo = new JButton( uiTextResources.getString("fileDownloadDialog.btnNo") );
    btnYes = new JButton( uiTextResources.getString("fileDownloadDialog.btnYes") );
    progressBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 20));
    progressBar.setMinimum(0);
    progressBar.setMaximum(1);
    
    rbAll.setText(uiTextResources.getString("fileDownloadDialog.orderPane.choiceAll"));
    rbAll.setSelected(true);
    rbChoosePages.setText(uiTextResources.getString("fileDownloadDialog.orderPane.choiceNumbs"));
    buttonGroup.add(rbAll);
    buttonGroup.add(rbChoosePages);
    btOpenChoicePagesDialog.setText("...");
    btSaveDialog.setText("...");
    lblSaveText.setText(uiTextResources.getString("fileDownloadDialog.orderPane.savePath"));
//    lblPerSecond.setText(uiTextResources.getString("fileDownloadDialog.viewPane.msgPerSecond"));
    
  }

  private void layoutComponents()
  {
	  
	  
	JPanel rootPnl = new JPanel();
	rootPnl.setLayout(new BorderLayout());
	
	  
	pnlView.setLayout(new BoxLayout(pnlView, BoxLayout.PAGE_AXIS));	
	pnlOrder.setLayout(new BoxLayout(pnlOrder, BoxLayout.PAGE_AXIS));
	
	Box centreBox = new Box(BoxLayout.X_AXIS);
	centreBox.add(Box.createHorizontalStrut(20));
	JLabel lblImage = new JLabel( UIManager.getIcon("OptionPane.questionIcon"));
	lblImage.setAlignmentY(0.1f);
	centreBox.add(lblImage); 
	centreBox.add(Box.createHorizontalStrut(20));
		
    Box boxText = new Box(BoxLayout.Y_AXIS);
    boxText.add(Box.createVerticalStrut(10));
    boxText.add(lblQuestionText);
    boxText.add(Box.createVerticalStrut(5));
    boxText.add(lblAccessPriceText); //TODO uncomment
    boxText.add(Box.createVerticalStrut(5));
    boxText.add(lblSpeedText);
    boxText.add(Box.createVerticalStrut(5));
    boxText.add(lblTimeText);
   /* boxText.add(Box.createVerticalStrut(5));
    boxText.add(lblPerSecond);*/
    boxText.add(Box.createVerticalStrut(10));
    
    boxText.setAlignmentY(0.1f); 
    centreBox.add(boxText);
    centreBox.add(Box.createHorizontalStrut(20));
    
   // boxQuestion.add(boxText);
    //boxQuestion.add(Box.createHorizontalGlue());
    //Box loadBoxText = new Box(BoxLayout.Y_AXIS);
    //loadBoxText.add(getLoadTextArea());

    Box boxButtons = new Box(BoxLayout.LINE_AXIS);
    boxButtons.add(btnNo);
    boxButtons.add(Box.createHorizontalStrut(10));
    boxButtons.add(btnYes);

    pnlView.add(centreBox);
    //pnlView.add(boxButtons);
    pnlView.add(Box.createVerticalStrut(5));
    //pnlView.add(progressBar);
    
    
    Box centreOrderBox = new Box(BoxLayout.X_AXIS);
    // add image
	centreOrderBox.add(Box.createHorizontalStrut(20));
	JLabel lblImage2 = new JLabel( UIManager.getIcon("OptionPane.questionIcon"));
	lblImage2.setAlignmentY(0.1f);
	centreOrderBox.add(lblImage2); 
	centreOrderBox.add(Box.createHorizontalStrut(20));
	// right...
	// create order text box
	Box orderTextBox = new Box(BoxLayout.Y_AXIS);
	// add order question
	orderTextBox.add(Box.createVerticalStrut(10));

	lblQuestionOrderText.setAlignmentX(JComponent.LEFT_ALIGNMENT); // alignment by left
	orderTextBox.add(lblQuestionOrderText);	
	orderTextBox.add(Box.createVerticalStrut(5));
	// below..
	// create selection pages box
	Box selPgsBox = new Box(BoxLayout.X_AXIS);
	selPgsBox.add(rbAll); // add all pages selection radiobutton
	selPgsBox.add(Box.createHorizontalStrut(20)); 
	selPgsBox.add(rbChoosePages); // add choice pages selection radiobutton
	selPgsBox.add(Box.createHorizontalStrut(5));
	selPgsBox.add(btOpenChoicePagesDialog); // add show choice pages dialog button
	
	selPgsBox.setAlignmentX(JComponent.LEFT_ALIGNMENT); // alignment by left
	// add it in orderText box
    orderTextBox.add(selPgsBox);
    orderTextBox.add(Box.createVerticalStrut(5));
    // below...
    // add cost text
    lblCostOrderText.setAlignmentX(JComponent.LEFT_ALIGNMENT); // alignment by left
    orderTextBox.add(lblCostOrderText);
    orderTextBox.add(Box.createVerticalStrut(5));
    lblTotalText.setAlignmentX(JComponent.LEFT_ALIGNMENT);  // alignment by left
    orderTextBox.add(lblTotalText);
    orderTextBox.add(Box.createVerticalStrut(5));
    
    // create save path box
    Box savePathBox = new Box(BoxLayout.X_AXIS);
    // add line save in
    savePathBox.add(lblSaveText);
    savePathBox.add(Box.createHorizontalStrut(5));
    savePathBox.add(fldSaveDocumentPath); // add save path textfield
    savePathBox.add(btSaveDialog); // add save_document dialog's button
    
    savePathBox.setAlignmentX(JComponent.LEFT_ALIGNMENT); //  alignment by left
    // add it in orderText box
    orderTextBox.add(savePathBox);
    orderTextBox.add(Box.createVerticalStrut(10));
    
    orderTextBox.setAlignmentY(0.1f); 
    centreOrderBox.add(orderTextBox);
    centreOrderBox.add(Box.createHorizontalStrut(20));
    
    pnlOrder.add(centreOrderBox);
    //pnlOrder.add(boxButtons);
    pnlOrder.add(Box.createVerticalStrut(5));
    //pnlOrder.add(progressBar);
    

    tabPane.add(uiTextResources.getString("fileDownloadDialog.viewPane.title"), pnlView);
    tabPane.add(uiTextResources.getString("fileDownloadDialog.orderPane.title"), pnlOrder);
    
    rootPnl.add(tabPane,BorderLayout.CENTER);
    
    JPanel bottomPnl = new JPanel();
    bottomPnl.setLayout(new BoxLayout(bottomPnl,BoxLayout.Y_AXIS));
    bottomPnl.add(boxButtons);      
    bottomPnl.add(progressBar);
    rootPnl.add(bottomPnl,BorderLayout.SOUTH);
    
    setContentPane(rootPnl);
    
  }

  public void setFileProperties(File file,FileDownloadDialogController.PagsSelectionMode pagesSelectMode)

  {
	  String fileName = file.getName();
	  float minuteCost = Float.MAX_VALUE;
	  float pageCost = Float.MAX_VALUE;
	  try {
		minuteCost = file.getViewCost();
		pageCost = file.getDownloadCost();		
	  } catch (Exception e) {}
	  float hourCost = minuteCost * 60;
//	  float nds = FileDownloadDialogController.NDS;
	  int selectPages = 0;
	  if (pagesSelectMode == FileDownloadDialogController.PagsSelectionMode.ALL)
		  selectPages = file.getPages();
	  if (pagesSelectMode == FileDownloadDialogController.PagsSelectionMode.SEVERAL)
	      selectPages = ZnclApplication.getApplication().getControllerManager().getChoosePagesController().getSelectedPagesCount();
		   	   	  	
    lblQuestionText.setText(String.format(questionTemplate, fileName));
    lblAccessPriceText.setText(String.format(priceTemplate, hourCost));
    lblQuestionOrderText.setText(String.format(questionOrderTemplate,fileName));
    lblCostOrderText.setText(String.format(costOrderTemplate,pageCost)); 
    setTotalText(selectPages, pageCost);
    setPath(String.format(saveDocumentPathTemplate,fileName));  
  }

 
  
//  public static void main(String[] a)
//  {
//    GuiUtil.testUiComponent(new FileDownloadDialog(null));
//  }

  public JButton getBtnYes()
  {
    return btnYes;
  }

  public JButton getBtnNo()
  {
    return btnNo;
  }

  public JProgressBar getProgressBar()
  {
    return progressBar;
  }
  
  public JRadioButton getRbAll() {
	return rbAll;
  }
  

  public JRadioButton getRbChoosePages() {
	return rbChoosePages;
  }
  
  public JButton getOpenChoiceDlgBtn() {
	return btOpenChoicePagesDialog;
  }
  
  public JButton getSaveDlgBtn() {
	return btSaveDialog;
  }
  
  
  
  public void setQuickDownloadView(float minuteCost){
	  float hourCost = minuteCost * 60;
	  JPanel pnlQestion = new JPanel();
	  pnlQestion.setLayout( new BoxLayout(pnlQestion, BoxLayout.X_AXIS) );
	  pnlQestion.add( Box.createHorizontalGlue() );
	  pnlQestion.add( new JLabel( uiTextResources.getString("fileDownloadDialog.viewPane.questionText1")) );
	  pnlQestion.add( Box.createHorizontalGlue() );
	  
	  JPanel pnlCost = new JPanel();
	  pnlCost.setLayout( new BoxLayout(pnlCost, BoxLayout.X_AXIS) );
	  pnlCost.add( Box.createHorizontalGlue() );
	  pnlCost.add( new JLabel(String.format(priceTemplate, hourCost)) );
	  pnlCost.add( Box.createHorizontalGlue() );
	  
	  JPanel pnlMessage = new JPanel();
	  pnlMessage.setLayout( new BoxLayout(pnlMessage, BoxLayout.Y_AXIS));
	  
	  pnlMessage.add(Box.createVerticalGlue());
	  pnlMessage.add( pnlQestion);
	  pnlMessage.add(Box.createVerticalStrut(10));
	  pnlMessage.add( pnlCost );
	  pnlMessage.add(Box.createVerticalGlue());

	  JPanel rootPanel = (JPanel) getContentPane();
	  rootPanel.remove(tabPane);
	  rootPanel.add(pnlMessage, BorderLayout.CENTER);

	  tabMode = TabMode.VIEW;
	  progressBar.setVisible(false);
  }

public void setLoadText(String speedTxt, String timeTxt){
	  lblSpeedText.setText(speedTxt);
	  lblTimeText.setText(timeTxt);	  	  
	  if (!resizable){
		  resizable = true;
		  setSize(VIEW_DIALOG_DIMENSION_WITH_DOWNLOADING_PARAMS);
	  }	    
  }
  
  public void setDefault(int size){
	    getProgressBar().setMaximum(size);
		getProgressBar().setValue(0);
		setLoadText("","");
		setSize(VIEW_DIALOG_DIMENSION);
		//getAllBtn().setSelected(true);
		resizable = false;
  }
  
  public void setTotalText(int selPgsCount, float pageCost){
	  
//	  float pageCostNDS = pageCost * ( 1 + FileDownloadDialogController.NDS );
//	  lblTotalText.setText( String.format(totalOrderTemplate, selPgsCount, selPgsCount*pageCost, selPgsCount*pageCostNDS) );
	  lblTotalText.setText( String.format(totalOrderTemplate, selPgsCount, selPgsCount*pageCost) );
  }
  
  public void setRbChoosePagesText(String text){
	  rbChoosePages.setText(uiTextResources.getString("fileDownloadDialog.orderPane.choiceNumbs")+" - "+text);	  
  }
  
  public void resetRbChoosePagesText(){
	  rbChoosePages.setText(uiTextResources.getString("fileDownloadDialog.orderPane.choiceNumbs"));
	  rbAll.setSelected(true);
  }
 

  public void setPath(String path) {	
	  fldSaveDocumentPath.setText(path);
  }
  
  public String getPath() {
	return fldSaveDocumentPath.getText();
  }
  
  public JTabbedPane getPane() {
	return tabPane;
  }
  
  public JPanel getPnlOrder(){
	  return pnlOrder;
  }
  
	public void show(TabMode mode) {
		tabPane.removeAll();
		tabMode = mode;
		if (mode == TabMode.VIEW) {
			tabPane.add(uiTextResources
					.getString("fileDownloadDialog.viewPane.title"), pnlView);
			setSize(VIEW_DIALOG_DIMENSION);
		}
		if (mode == TabMode.ORDER) {
			tabPane.add(uiTextResources
					.getString("fileDownloadDialog.orderPane.title"), pnlOrder);
			setSize(ORDER_DIALOG_DIMENSION);
		}
		if (mode == TabMode.VIEW_AND_ORDER) {
			tabPane.add(uiTextResources
					.getString("fileDownloadDialog.viewPane.title"), pnlView);
			tabPane.add(uiTextResources
					.getString("fileDownloadDialog.orderPane.title"), pnlOrder);
		}
		setVisible(true);
	}
	
	public TabMode getTabMode(){
		return tabMode;
	}
  
	/*public JLabel getLblPerSecond() {
		return lblPerSecond;
	}*/

	public enum TabMode {
		VIEW, 
		ORDER, 
		VIEW_AND_ORDER
	};
  
}
