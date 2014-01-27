package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JPopupMenu.Separator;
import javax.swing.border.Border;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.util.GuiUtil;

/**
 * A form dialog for complex searches.
 */
public class ComplexSearchDialogTake2 extends JDialog
{
  private static final int LENGTH_TEXT_FEILD = 13;
	
  private ResourceBundle uiTextResources;
  private JTextField tfldAuthor = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldName =					new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldDescr = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldISSBN = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldPublichers = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldGasnti = 				new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldIndexUGK = 			new JTextField( LENGTH_TEXT_FEILD );
  private JTextField tfldCity = 				new JTextField( LENGTH_TEXT_FEILD );
  private JComboBox cbxPublYear = 				new JComboBox();
  private JComboBox cbxRegYear = 				new JComboBox();
  private JComboBox cbxCode = 					new JComboBox();
  private JCheckBox chkBxDes;
  private JCheckBox chkBxBooks;
  private JCheckBox chkBxPeriodicls;
  private JButton btnCancel, btnSearch;

  public ComplexSearchDialogTake2(JFrame owner)
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
	  chkBxDes = new JCheckBox( getChkDessertationAction() );
	  chkBxBooks = new JCheckBox( getChkBooks_PeriodiclAction() );
	  chkBxBooks.setText( uiTextResources.getString( "complexSearchDialog.tbPnlBooks" ) );
	  chkBxPeriodicls = new JCheckBox( getChkBooks_PeriodiclAction() );
	  chkBxPeriodicls.setText( uiTextResources.getString( "complexSearchDialog.tbPnlPeriodicals" ) );
	  btnCancel = new JButton(uiTextResources.getString("common.btnCancel"));
	  btnSearch = new JButton(uiTextResources.getString("complexSearchDialog.btnSearch"));
	  tfldISSBN.setEnabled(false);
	  tfldPublichers.setEnabled(false); 
	  tfldGasnti.setEnabled(false);
	  tfldIndexUGK.setEnabled(false);
	  tfldCity.setEnabled(false);
	  cbxPublYear.setEnabled(false);
	  cbxRegYear.setEnabled(false);
	  cbxCode.setEnabled(false);
  }

  private void layoutComponents()
  {
	  JPanel pnlChkBoxes = new JPanel();
	  Border loweredbevel = BorderFactory.createEtchedBorder();
	  pnlChkBoxes.setBorder( BorderFactory.createTitledBorder(loweredbevel, "Что ищем:") );
	  pnlChkBoxes.setLayout( new BoxLayout(pnlChkBoxes, BoxLayout.LINE_AXIS) );

	  pnlChkBoxes.add( chkBxDes );
	  pnlChkBoxes.add(Box.createHorizontalStrut(20));
	  pnlChkBoxes.add( chkBxBooks );
	  pnlChkBoxes.add(Box.createHorizontalStrut(20));
	  pnlChkBoxes.add( chkBxPeriodicls );
	  
	  JPanel pnlButtons = new JPanel();
	  pnlButtons.setLayout( new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS) );
	  pnlButtons.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
	  pnlButtons.add(Box.createHorizontalGlue());
	  pnlButtons.add(btnCancel);
	  pnlButtons.add(Box.createHorizontalStrut(20));
	  pnlButtons.add(btnSearch);
	  
	  
	  Container contMain = getContentPane();
	  contMain.setLayout( new BorderLayout() );	  
	  contMain.add(pnlChkBoxes, BorderLayout.NORTH);
	  contMain.add(getFieldsPanel(), BorderLayout.CENTER);
	  contMain.add(pnlButtons, BorderLayout.SOUTH);
	  getRootPane().setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
	  pack();
  }
  
  /**
   * disabling or enabling the fields necessary for a dissertations search
   * @param status
   */
  private void setEnablDessFilds( boolean status){
	  tfldGasnti.setEnabled( status );
	  tfldIndexUGK.setEnabled( status );
	  tfldCity.setEnabled( status );
	  cbxRegYear.setEnabled( status );
	  cbxCode.setEnabled( status );
  }
  
  /**
   * disabling or enabling the fields necessary for a books/periodical search
   * @param status
   */  
  private void setEnablBooksFilds( boolean status ){
	  cbxPublYear.setEnabled( status );
	  tfldPublichers.setEnabled( status );
	  tfldISSBN.setEnabled( status );
  }
  
  private JPanel getFieldsPanel(){
	  JPanel pnlFields = new JPanel();
	  Border loweredbevel = BorderFactory.createEtchedBorder();
	  pnlFields.setBorder( BorderFactory.createTitledBorder(loweredbevel, "Параметры поиска") );
	  pnlFields.setLayout( new GridBagLayout() );
	  GridBagConstraints grBgConst = new GridBagConstraints();
	  grBgConst.insets = new Insets(7, 5, 7, 5);
	  grBgConst.anchor = GridBagConstraints.WEST;
	  grBgConst.fill = GridBagConstraints.HORIZONTAL;
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblName")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 0;
	  pnlFields.add( tfldName, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 0;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblCity")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 0;
	  pnlFields.add( tfldCity, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblDescription")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 1;
	  pnlFields.add( tfldDescr, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 1;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblRegYear")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 1;
	  pnlFields.add( cbxRegYear, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblAuthor")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 2;
	  pnlFields.add( tfldAuthor, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 2;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblCode")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 2;
	  pnlFields.add( cbxCode, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 3;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblGasnti")), grBgConst );
	  grBgConst.gridx = 1; grBgConst.gridy = 3;
	  pnlFields.add( tfldGasnti, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 3;
	  pnlFields.add( new JLabel(uiTextResources.getString("complexSearchDialog.lblIndexUGK")), grBgConst );
	  grBgConst.gridx = 3; grBgConst.gridy = 3;
	  pnlFields.add( tfldIndexUGK, grBgConst );
	  
	  grBgConst.gridwidth = 4;
	  grBgConst.gridx = 0; grBgConst.gridy = 4;
	  pnlFields.add( new JSeparator( Separator.HORIZONTAL ), grBgConst );

	  grBgConst.gridwidth = 1;
	  grBgConst.gridx = 0; grBgConst.gridy = 5;
	  pnlFields.add( new JLabel( uiTextResources.getString("complexSearchDialog.lblPublichers") ), grBgConst);
	  grBgConst.gridx = 1; grBgConst.gridy = 5;
	  pnlFields.add( tfldPublichers, grBgConst );
	  grBgConst.gridx = 2; grBgConst.gridy = 5;
	  pnlFields.add( new JLabel( uiTextResources.getString("complexSearchDialog.lblPublYear") ), grBgConst);
	  grBgConst.gridx = 3; grBgConst.gridy = 5;
	  pnlFields.add( cbxPublYear, grBgConst );
	  
	  grBgConst.gridx = 0; grBgConst.gridy = 6;
	  pnlFields.add( new JLabel( uiTextResources.getString("complexSearchDialog.lblISSBN") ), grBgConst);
	  grBgConst.gridx = 1; grBgConst.gridy = 6;
	  pnlFields.add( tfldISSBN, grBgConst );	  
	  
	  return pnlFields;
  }
  
  /**
   * @return Action disable or enable the fields during setting of chekBox. Action identical for books and
   * periodical therefore the name needs to be set in hand
   */
  private Action getChkBooks_PeriodiclAction(){
	  return new AbstractAction( ) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if ( chkBxBooks.isSelected() || chkBxPeriodicls.isSelected() ){
				setEnablBooksFilds( true );
			} else
				setEnablBooksFilds( false );
		}
	};
  }

private Action getChkDessertationAction(){
	  return new AbstractAction( uiTextResources.getString( "complexSearchDialog.tbPnlDissertations" ) ) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if ( chkBxDes.isSelected() ){
				setEnablDessFilds( true );
			} else
				setEnablDessFilds( false );
		}
	};
  }

public JTextField getTfldName() {
	  return tfldName;
  }

  public JTextField getTfldCity() {
	return tfldCity;
  }

  public JTextField getTfldDescription()
  {
	  return tfldDescr;	  
  }

  public JTextField getTfldAuthor()
  {
	  return tfldAuthor;
  }

  public JTextField getTfldGasnti()
  {
    return tfldGasnti;
  }

  public JComboBox getCbxRegYear()
  {
    return cbxRegYear;
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

  public static void main(String[] a)
  {
	  GuiUtil.setSysLAF();
	  GuiUtil.testUiComponent(new ComplexSearchDialogTake2(null));
  }
}

