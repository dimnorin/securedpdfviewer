package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.ComplexSearchCriteria;
import ua.com.znannya.client.util.GuiUtil;

/**
 * A form dialog for complex searches.
 */
public class ComplexSearchDialog extends JDialog
{
  private static final int LENGTH_TEXT_FEILD = 13;
	
  private ResourceBundle uiTextResources;
  private JTextField tfldAuthorDess = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldAuthorBooks = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldAuthorPeriodicls = 	new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldNameDess =				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldNameBooks =			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldNamePeriodicls =		new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldDescrDess = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldDescrBooks = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldDescrPeriodicls =		new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldISSBNBooks = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldISSBNPeriodicls = 		new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldGasnti = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldIndexUGK = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldCity = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldUDK = 					new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldBBK =	 				new JTextField( LENGTH_TEXT_FEILD );
  private JComboBox cbxPublYearBooks = 			new JComboBox();
  private JComboBox cbxPublYearPeriodicls = 	new JComboBox();
  private JComboBox cbxRegYear = 				new JComboBox();
  private JComboBox cbxCode = 					new JComboBox();
  private JComboBox cmbxPublichersBooks = 		new JComboBox();
  private JComboBox cmbxPublichersPeriodicls =	new JComboBox();
  private JButton btnCancel, btnSearch;
  private JTabbedPane tabPane;

  public ComplexSearchDialog(JFrame owner)
  {
    super(owner, true);
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
    setSize(500, 300);
    setResizable(false);
    
    setTitle(uiTextResources.getString("complexSearchDialog.windowTitle"));
    setIconImage(ZnclApplication.getApplication().getIcon("Search.png").getImage());
    setLocationRelativeTo(owner);
    initComponents();
    layoutComponents();
  }

  private void initComponents()
  {
	  tabPane = new JTabbedPane();
	  btnCancel = new JButton(uiTextResources.getString("common.btnCancel"));
	  btnSearch = new JButton(uiTextResources.getString("complexSearchDialog.btnSearch"));
  }

  private void layoutComponents()
  {
	  JPanel pnlButtons = new JPanel();
	  pnlButtons.setLayout( new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS) );
	  pnlButtons.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
	  pnlButtons.add(Box.createHorizontalGlue());
	  pnlButtons.add(btnCancel);
	  pnlButtons.add(Box.createHorizontalStrut(20));
	  pnlButtons.add(btnSearch);
	  
	  tabPane.add(getDessertationsPanel(),  uiTextResources.getString("complexSearchDialog.tbPnlDissertations") );
	  tabPane.add(getBooksJPanel(),  uiTextResources.getString("complexSearchDialog.tbPnlBooks") );
	  tabPane.add(getPeriodicalsJPanel(),  uiTextResources.getString("complexSearchDialog.tbPnlPeriodicals") );
	  
	  Container contMain = getContentPane();
	  contMain.setLayout( new BorderLayout() );
	  contMain.add(tabPane, BorderLayout.CENTER);
	  contMain.add(pnlButtons, BorderLayout.SOUTH);
	  pack();
  }
  
  private JPanel getDessertationsPanel(){
	  JPanel pnlDisFields = new JPanel();
	  pnlDisFields.setLayout( new GridBagLayout() );
	  GridBagConstraints grBgConst = new GridBagConstraints();
	  grBgConst.insets = new Insets(7, 5, 7, 5);
	  grBgConst.anchor = GridBagConstraints.WEST;
	  grBgConst.fill = GridBagConstraints.HORIZONTAL;
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 0;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblName")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 0;
	  pnlDisFields.add( tfldNameDess, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 0;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblCity")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 0;
	  pnlDisFields.add( tfldCity, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 1;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblDescription")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 1;
	  pnlDisFields.add( tfldDescrDess, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 1;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblRegYear")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 1;
	  pnlDisFields.add( cbxRegYear, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 2;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblAuthor")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 2;
	  pnlDisFields.add( tfldAuthorDess, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 2;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblCode")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 2;
	  pnlDisFields.add( cbxCode, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 3;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblGasnti")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 3;
	  pnlDisFields.add( tfldGasnti, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 3;
	  pnlDisFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblIndexUGK")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 3;
	  pnlDisFields.add( tfldIndexUGK, grBgConst );
	  
	  return pnlDisFields;
  }
  
  private JPanel getBooksJPanel(){
	  JPanel pnlFields = new JPanel();
	  pnlFields.setLayout( new GridBagLayout() );
	  GridBagConstraints grBgConst = new GridBagConstraints();
	  grBgConst.insets = new Insets(7, 5, 7, 5);
	  grBgConst.anchor = GridBagConstraints.WEST;
	  grBgConst.fill = GridBagConstraints.HORIZONTAL;
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblName")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 0;
	  pnlFields.add( tfldNameBooks, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblPublYear")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 0;
	  pnlFields.add( cbxPublYearBooks, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblDescription")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 1;
	  pnlFields.add( tfldDescrBooks, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblISBN")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 1;
	  pnlFields.add( tfldISSBNBooks, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblAuthor")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 2;
	  pnlFields.add( tfldAuthorBooks, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblPublichers")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 2;
	  pnlFields.add( cmbxPublichersBooks, grBgConst ); 

	  grBgConst.gridx = 0; grBgConst.gridy = 3;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblUDK")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 3;
	  pnlFields.add( tfldUDK, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 3;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblBBK")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 3;
	  pnlFields.add( tfldBBK, grBgConst ); 
	  
	  return pnlFields;
  }
  private JPanel getPeriodicalsJPanel(){
	  JPanel pnlFields = new JPanel();
	  pnlFields.setLayout( new GridBagLayout() );
	  GridBagConstraints grBgConst = new GridBagConstraints();
	  grBgConst.insets = new Insets(7, 5, 7, 5);
	  grBgConst.anchor = GridBagConstraints.WEST;
	  grBgConst.fill = GridBagConstraints.HORIZONTAL;
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblName")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 0;
	  pnlFields.add( tfldNamePeriodicls, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblPublYear")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 0;
	  pnlFields.add( cbxPublYearPeriodicls, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblDescription")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 1;
	  pnlFields.add( tfldDescrPeriodicls, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblISBN")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 1;
	  pnlFields.add( tfldISSBNPeriodicls, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblAuthor")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 2;
	  pnlFields.add( tfldAuthorPeriodicls, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblPublichers")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 2;
	  pnlFields.add( cmbxPublichersPeriodicls, grBgConst ); 
	  
	  return pnlFields;
  }
  
  public JTextField getTfldName() {
	  int selectIndx = tabPane.getSelectedIndex();
	  if ( selectIndx == ComplexSearchCriteria.DISSERTATIONS )
		  return tfldNameDess;
	  else
		  if ( selectIndx == ComplexSearchCriteria.BOOKS)
			  return tfldNameBooks;
		  else
			  return tfldNamePeriodicls;
  }

  public JTextField getTfldCity() {
	return tfldCity;
  }

  public JTextField getTfldDescription()
  {
	  int selectIndx = tabPane.getSelectedIndex();
	  if ( selectIndx == ComplexSearchCriteria.DISSERTATIONS )
		  return tfldDescrDess;
	  else
		  if ( selectIndx == ComplexSearchCriteria.BOOKS)
			  return tfldDescrBooks;
		  else
			  return tfldDescrPeriodicls;	  
  }

  public JTextField getTfldAuthor()
  {
	  int selectIndx = tabPane.getSelectedIndex();
	  if ( selectIndx == ComplexSearchCriteria.DISSERTATIONS )
		  return tfldAuthorDess;
	  else
		  if ( selectIndx == ComplexSearchCriteria.BOOKS)
			  return tfldAuthorBooks;
		  else
			  return tfldAuthorPeriodicls;
  }

  public JTextField getTfldGasnti()
  {
    return tfldGasnti;
  }

  public JComboBox getCbxRegYear()
  {
    return cbxRegYear;
  }

  public JComboBox getCbxPublYear(){
	  if ( tabPane.getSelectedIndex() == ComplexSearchCriteria.BOOKS )
		  return cbxPublYearBooks;
	  else
		  return cbxPublYearPeriodicls;
  }
  
  public JComboBox getCbxPublYearBooks(){
	  return cbxPublYearBooks;
  }

  public JComboBox getCbxPublYearPeriodicls(){
	  return cbxPublYearPeriodicls;
  }
  
  public JComboBox getCbxPublichers(){
	  if ( tabPane.getSelectedIndex() == ComplexSearchCriteria.BOOKS )
		  return cmbxPublichersBooks;
	  else
		  return cmbxPublichersPeriodicls;
  }
  
  public JComboBox getCbxPublichersBooks(){
	  return cmbxPublichersBooks;
  }
  public JComboBox getCbxPublichersPerioicls(){
	  return cmbxPublichersPeriodicls;
  }
  
  public JTextField getTfldISBN(){
	  if ( tabPane.getSelectedIndex() == ComplexSearchCriteria.BOOKS )
		  return tfldISSBNBooks;
	  else
		  return tfldISSBNPeriodicls;	  
  }
  
  public JTextField getTfldUDK(){
	  return tfldUDK;
  }
  
  public JTextField getTfldBBK(){
	  return tfldBBK;
  }
  
  public JComboBox getCbxCode()
  {
    return cbxCode;
  }

  public JTextField getTfldIndexUGK()
  {
    return tfldIndexUGK;
  }

  public JButton getBtnCancel()
  {
    return btnCancel;
  }

  public JButton getBtnSearch()
  {
    return btnSearch;
  }
  
  public int getSelectedIndxTab(){
	  return tabPane.getSelectedIndex();
  }

  public static void main(String[] a)
  {
	  GuiUtil.setSysLAF();
	  GuiUtil.testUiComponent(new ComplexSearchDialog(null));
  }
}

