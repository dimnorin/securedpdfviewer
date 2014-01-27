package ua.com.znannya.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import org.jivesoftware.smack.znannya.dao.BriefStatistics;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.util.GuiUtil;

public class PrivateRoomDialog extends JDialog{
	private static final int LENGTH_TEXT_FEILD = 25;
	private static final int WIDTH = 500;
	private static final int HEIGHT = 300;
		
	private JPasswordField pfldOldPass = 	new JPasswordField( LENGTH_TEXT_FEILD );
	private JPasswordField pfldNewPass = 	new JPasswordField( LENGTH_TEXT_FEILD );
	private JPasswordField pfldRepPass = 	new JPasswordField( LENGTH_TEXT_FEILD );
	
	private JTextField tfldName = 			new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldFamily = 		new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldOrganization = 	new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldPosition = 		new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldPhone = 			new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldEMail =			new JTextField( LENGTH_TEXT_FEILD );
	private JTextField tfldFromDate=		new JTextField( 10 );
	private JTextField tfldToDate=			new JTextField( 10 );
	private JTextPane tpnAddInfo = 			new JTextPane();
	
	private JLabel lblLogin =				new JLabel();
	private ResourceBundle uiTextResources;
	private JRadioButton rBtnSMS;
	private JRadioButton rBtnVisa;
	
	private JButton btnIncrease;
	private JButton btnSaveChng;
	private JButton btnFromDate;
	private JButton btnToDate;
	private JButton btnShowStat;
	private JButton btnDetailStats;
	
	private JTabbedPane tabPane;
	private ButtonGroup btnGrChoice;
	
	private DefaultTableModel tblModel;
	private JTable tblStat;  
	private String[] colNames;
	
	public PrivateRoomDialog( JFrame owner ){
	    super(owner, true);
	    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	    setSize(HEIGHT, WIDTH);
	    setResizable(false);
	    setTitle(uiTextResources.getString("privateRoomDialog.windowTitle"));
	    setIconImage(ZnclApplication.getApplication().getIcon("User.png").getImage());
	    setLocationRelativeTo(owner);
	    initComponents();
	    layoutComponents();
	}
	
	
	private void initComponents() {
		tfldFromDate.setEditable(false);
		tfldToDate.setEditable(false);
		rBtnSMS = new JRadioButton( uiTextResources.getString( "privateRoomDialog.rBtnSMS" ) );
		rBtnSMS.setMnemonic( KeyEvent.VK_C );
		rBtnVisa = new JRadioButton( uiTextResources.getString( "privateRoomDialog.rBtnVisa" ) );
		rBtnVisa.setMnemonic( KeyEvent.VK_V );
		
		btnIncrease = new JButton( uiTextResources.getString( "privateRoomDialog.btnIncrease" ));
		btnSaveChng = new JButton( uiTextResources.getString( "privateRoomDialog.btnSaveChng" ));
		btnFromDate = new JButton( ZnclApplication.getApplication().getIcon("calendar.png") );
		btnToDate = new JButton( ZnclApplication.getApplication().getIcon("calendar.png") );
		btnDetailStats = new JButton( "<html><a href='#'>" + uiTextResources.getString("privateRoomDialog.btnDetailStats") + "</a></html>");
		btnDetailStats.setMaximumSize( new Dimension(100, 22) );
		btnDetailStats.setFocusPainted(false);
		btnDetailStats.setMargin( new Insets(0, 0, 0, 0) );
		btnDetailStats.setContentAreaFilled(false);
		btnDetailStats.setBorderPainted(false);
		btnDetailStats.setOpaque(false);
		
		btnShowStat = new JButton(uiTextResources.getString("privateRoomDialog.btnShow"));

		tabPane = new JTabbedPane();
		btnGrChoice = new ButtonGroup();
		btnGrChoice.add( rBtnSMS );
		btnGrChoice.add( rBtnVisa );
		
		colNames = new String[]{ 
				uiTextResources.getString("privateRoomDialog.tblStats.lblBalance"),
				uiTextResources.getString("privateRoomDialog.tblStats.lblGRN")};
		
		tblModel = new DefaultTableModel();
		tblModel.addColumn(colNames[0]);
		tblModel.addColumn(colNames[1]);
		tblStat = new JTable( tblModel );
		tblStat.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		tblStat.setEnabled(false);
		tblStat.setVisible(false);
	}

	private void layoutComponents() {
		tabPane.add( getStatPanel(), uiTextResources.getString( "privateRoomDialog.tbPnlStat" ) );
		tabPane.add( getIncAccPanel(), uiTextResources.getString( "privateRoomDialog.tbPnlIncAcc" ) );
		tabPane.add( getPrivateDataPanel(), uiTextResources.getString( "privateRoomDialog.tbPnlPrivateData" ) );
//		tabPane.setSelectedIndex(1);
		
		add( tabPane );
		pack();
	}
	
	private JPanel getStatPanel(){
		JPanel pnlComponent = new JPanel();				
		pnlComponent.setLayout( new GridBagLayout() );
		GridBagConstraints grdBgConstr = new GridBagConstraints();
		Border border = BorderFactory.createEtchedBorder();
		pnlComponent.setBorder( BorderFactory.createTitledBorder(border, uiTextResources.getString("privateRoomDialog.lblShowBalance")) );
		
		grdBgConstr.insets = new Insets(5, 5, 5, 5);
		grdBgConstr.anchor = GridBagConstraints.CENTER;
		
		grdBgConstr.weightx = 0;
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 0;
		pnlComponent.add( new JLabel(uiTextResources.getString("privateRoomDialog.lblWith")), grdBgConstr);

		grdBgConstr.weightx = 100.0;
		grdBgConstr.anchor = GridBagConstraints.EAST;
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 0;
		pnlComponent.add( tfldFromDate, grdBgConstr);
		
		grdBgConstr.weightx = 0;
		grdBgConstr.gridx = 2; grdBgConstr.gridy = 0;
		pnlComponent.add( btnFromDate, grdBgConstr);
		
		grdBgConstr.anchor = GridBagConstraints.CENTER;
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 1;
		pnlComponent.add( new JLabel(uiTextResources.getString("privateRoomDialog.lblOn")), grdBgConstr);
		
		grdBgConstr.weightx = 50;
		grdBgConstr.anchor = GridBagConstraints.EAST;
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 1;
		pnlComponent.add( tfldToDate, grdBgConstr);

		grdBgConstr.weightx = 0;
		grdBgConstr.gridx = 2; grdBgConstr.gridy = 1;
		pnlComponent.add( btnToDate, grdBgConstr);
		
		
		grdBgConstr.anchor = GridBagConstraints.CENTER;
		grdBgConstr.insets = new Insets(25, 5, 0, 5);
		grdBgConstr.weightx = 0;
		grdBgConstr.gridx = 2; grdBgConstr.gridy = 2;
		grdBgConstr.gridwidth = 1;
		pnlComponent.add( btnShowStat, grdBgConstr );
		
		grdBgConstr.anchor = GridBagConstraints.EAST;
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 3;
		grdBgConstr.gridwidth = 3;
		GuiUtil.setColumnWidths(tblStat, 230, 80);
		GuiUtil.setColumnMaximumWidths(tblStat, 230, 80);
		JScrollPane scrollPane = new JScrollPane( tblStat );
		scrollPane.setPreferredSize(new Dimension(300, 91));
		pnlComponent.add( scrollPane, grdBgConstr );
		
		grdBgConstr.gridwidth = 1;
		grdBgConstr.gridx = 2; grdBgConstr.gridy = 4;
		pnlComponent.add( btnDetailStats, grdBgConstr );
		
		JPanel pnlFinal = new JPanel();
		pnlFinal.setBorder( BorderFactory.createEmptyBorder(50, 50, 50, 50));
		pnlFinal.add(pnlComponent);
		return pnlFinal;
	}
	
	private JPanel getIncAccPanel(){
		JPanel pnlComponent = new JPanel();
		pnlComponent.setPreferredSize(new Dimension(320, 150) );
		Border border = BorderFactory.createEtchedBorder();
		pnlComponent.setBorder( BorderFactory.createTitledBorder(border, uiTextResources.getString("privateRoomDialog.msgInc")) );
		pnlComponent.setLayout( new GridBagLayout() );
		GridBagConstraints grdBgConstr = new GridBagConstraints();
				
		grdBgConstr.insets = new Insets(5, 5, 5, 5);
		grdBgConstr.anchor = GridBagConstraints.WEST;
//		grdBgConstr.gridx = 0; grdBgConstr.gridy = 0;
//		pnlComponent.add( new JLabel( uiTextResources.getString( "privateRoomDialog.msgInc" ) ), grdBgConstr );

		grdBgConstr.gridx = 0; grdBgConstr.gridy = 0;
		grdBgConstr.weightx = 0.2;
		pnlComponent.add( rBtnSMS, grdBgConstr );
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 1;
		pnlComponent.add( rBtnVisa, grdBgConstr );
		
		grdBgConstr.anchor = GridBagConstraints.EAST;
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 2;
		pnlComponent.add( btnIncrease, grdBgConstr );
		
		JPanel pnlFinal = new JPanel();
		pnlFinal.setBorder( BorderFactory.createEmptyBorder(50, 50, 150, 50));
		pnlFinal.add(pnlComponent);
		return pnlFinal;
	}
	
	private JPanel getPrivateDataPanel(){
		JPanel pnlRes = new JPanel();
		pnlRes.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		pnlRes.setLayout( new GridBagLayout() );
		GridBagConstraints grdBgConstr = new GridBagConstraints();		
		grdBgConstr.insets = new Insets(5, 5, 5, 5);
		grdBgConstr.anchor = GridBagConstraints.WEST;
		grdBgConstr.fill = GridBagConstraints.HORIZONTAL;
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 0;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblLogin") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 0;
		pnlRes.add( lblLogin, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 1;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblOldPass") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 1;
		pnlRes.add( pfldOldPass, grdBgConstr);		

		grdBgConstr.gridx = 0; grdBgConstr.gridy = 2;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblNewPass") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 2;
		pnlRes.add( pfldNewPass, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 3;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblRepPass") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 3;
		pnlRes.add( pfldRepPass, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 4;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblName") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 4;
		pnlRes.add( tfldName, grdBgConstr);

		grdBgConstr.gridx = 0; grdBgConstr.gridy = 5;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblFamily") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 5;
		pnlRes.add( tfldFamily, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 6;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblOrganization") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 6;
		pnlRes.add( tfldOrganization, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 7;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblPosition") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 7;
		pnlRes.add( tfldPosition, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 8;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblEMail") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 8;
		pnlRes.add( tfldEMail, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 9;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblPhone") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 9;
		pnlRes.add( tfldPhone, grdBgConstr);
		
		grdBgConstr.gridx = 0; grdBgConstr.gridy = 10;
		pnlRes.add( new JLabel( uiTextResources.getString("privateRoomDialog.lblAddInfo") ), grdBgConstr);
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 10;
		JScrollPane scrlPane = new JScrollPane( tpnAddInfo );
		scrlPane.setPreferredSize( new Dimension(100, 100) );
		pnlRes.add( scrlPane, grdBgConstr);
		
		grdBgConstr.anchor = GridBagConstraints.EAST;
		grdBgConstr.gridx = 1; grdBgConstr.gridy = 11;
		pnlRes.add( btnSaveChng, grdBgConstr);		
		
		return pnlRes;
	}
	
	public void setDataInTable(BriefStatistics briefStat){
		Object[] row1 = new Object[]{
				uiTextResources.getString("privateRoomDialog.tblStats.lblbalanceOn") + " " + tfldFromDate.getText(),
				String.format( "%3.2f", briefStat.getFromBalance() )
		};
		Object[] row2 = new Object[]{
				uiTextResources.getString("privateRoomDialog.tblStats.lblSpend"),
				String.format( "%3.2f", briefStat.getSpentBalance() )
		};
		Object[] row3 = new Object[]{
				uiTextResources.getString("privateRoomDialog.tblStats.lblRemain"),
				String.format( "%3.2f", briefStat.getRefillBalance() )
		};
		Object[] row4 = new Object[]{
				uiTextResources.getString("privateRoomDialog.tblStats.lblbalanceOn") + " " + tfldToDate.getText(),
				String.format( "%3.2f", briefStat.getToBalance() )
		};
		tblModel.setDataVector(new Object[][]{row1, row2, row3, row4}, colNames);
	}
	
	public JTextField getTfldEMail() {
		return tfldEMail;
	}

	public JButton getBtnIncrease(){
		return btnIncrease;
	}
	
	public JButton getBtnSaveChng() {
		return btnSaveChng;
	}


	public JRadioButton getRBtnSMS(){
		return rBtnSMS;
	}

	public JRadioButton getRBtnVisa(){
		return rBtnVisa;
	}
	
//	public static void main(String[] args) {
//		GuiUtil.setSysLAF();
//		GuiUtil.testUiComponent( new PrivateRoomDialog(null) );
//	}

	public JPasswordField getPfldOldPass() {
		return pfldOldPass;
	}


	public JPasswordField getPfldRepPass() {
		return pfldRepPass;
	}


	public JTextField getTfldName() {
		return tfldName;
	}


	public JTextField getTfldFamily() {
		return tfldFamily;
	}


	public JTextField getTfldOrganization() {
		return tfldOrganization;
	}


	public JTextField getTfldPosition() {
		return tfldPosition;
	}


	public JTextField getTfldPhone() {
		return tfldPhone;
	}


	public JTextPane getExtraInfo() {
		return tpnAddInfo;
	}
	
	public JLabel getLblLogin() {
		return lblLogin;
	}


	public JPasswordField getPfldNewPass() {
		return pfldNewPass;
	}


	public JButton getBtnFromDate() {
		return btnFromDate;
	}


	public JButton getBtnToDate() {
		return btnToDate;
	}


	public JButton getBtnShowStat() {
		return btnShowStat;
	}


	public JTextField getTfldFromDate() {
		return tfldFromDate;
	}


	public JTextField getTfldToDate() {
		return tfldToDate;
	}


	public JTable getTblStat() {
		return tblStat;
	}


	public JButton getBtnDetailStats() {
		return btnDetailStats;
	}
}
