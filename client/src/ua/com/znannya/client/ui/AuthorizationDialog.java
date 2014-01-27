package ua.com.znannya.client.ui;

import java.awt.Dimension;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Authorization form dialog.
 */
public class AuthorizationDialog extends JDialog
{
  private ResourceBundle uiTextResources;
  private JPanel pnlFormFields = new JPanel();
  private JPanel pnlButtons = new JPanel();
  private JPanel pnlMain = new JPanel();
  private JLabel lblLogin, lblPassword, lblLanguage;
  private JTextField tfldLogin = new JTextField();
  private JTextField tfldPassword = new JPasswordField();
  private JButton btnRegister, btnCancel, btnNext;
  private JComboBox cmbxChooseLanguage = new JComboBox(new String[] {
		  ZnclApplication.getApplication().getUiTextResources().getString("authorizationDialog.LanguageUk"),
		  ZnclApplication.getApplication().getUiTextResources().getString("authorizationDialog.LanguageRu"),
		  ZnclApplication.getApplication().getUiTextResources().getString("authorizationDialog.LanguageEn")
		  });
  
  public AuthorizationDialog(JFrame owner)
  {
    super(owner, true);
    
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();    
    setTitle(uiTextResources.getString("authorizationDialog.windowTitle"));
    setIconImage(ZnclApplication.getApplication().getIcon("User.png").getImage());
    setSize(320, 200);
    initComponents();
    layoutComponents();
    add(pnlMain);
    setLocationRelativeTo(owner);
  }

  private void initComponents()
  {	  
    Dimension prefLabelSize = new Dimension(80, 22);
    lblLogin = new JLabel(uiTextResources.getString("authorizationDialog.lblLogin"));
    lblLogin.setPreferredSize(prefLabelSize);
    lblPassword = new JLabel(uiTextResources.getString("authorizationDialog.lblPassword"));
    lblPassword.setPreferredSize(prefLabelSize);
    lblLanguage = new JLabel( uiTextResources.getString("authorizationDialog.lblLanguage") );
    lblLanguage.setPreferredSize(prefLabelSize);
    Dimension maxTextFieldSize = new Dimension(Integer.MAX_VALUE, 22);
    tfldLogin.setMaximumSize(maxTextFieldSize);
    tfldPassword.setMaximumSize(maxTextFieldSize);
    btnRegister = new JButton(uiTextResources.getString("authorizationDialog.btnRegister"));
    btnCancel = new JButton(uiTextResources.getString("common.btnCancel"));
    btnNext = new JButton(uiTextResources.getString("common.btnNext"));
    if ( uiTextResources.getLocale().toString().equals("ru") )
    	cmbxChooseLanguage.setSelectedIndex(1);
    else if ( uiTextResources.getLocale().toString().equals("uk"))
    	cmbxChooseLanguage.setSelectedIndex(0);
    else 
    	cmbxChooseLanguage.setSelectedIndex(2);
  }

  private void layoutComponents()
  {
    Border boxBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    Box b1 = Box.createHorizontalBox();
    b1.setBorder(boxBorder);
    b1.add(lblLogin); b1.add(tfldLogin);
    Box b2 = Box.createHorizontalBox();
    b2.setBorder(boxBorder);
    b2.add(lblPassword); b2.add(tfldPassword);
    Box b3 = Box.createHorizontalBox();
    b3.setBorder(boxBorder);
    b3.add( lblLanguage ); b3.add(cmbxChooseLanguage);    

    pnlFormFields.setLayout(new BoxLayout(pnlFormFields, BoxLayout.PAGE_AXIS));
    pnlFormFields.setBorder(BorderFactory.createTitledBorder(uiTextResources.getString("authorizationDialog.formTitle")));
    pnlFormFields.add(b1);
    pnlFormFields.add(b2);
    pnlFormFields.add(b3);

    pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
    pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    pnlButtons.add(btnRegister);
    pnlButtons.add(Box.createHorizontalStrut(5));
    pnlButtons.add(btnCancel);
    pnlButtons.add(Box.createHorizontalStrut(5));
    pnlButtons.add(btnNext);
    
    pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
    pnlMain.add(pnlFormFields);
    pnlMain.add(Box.createVerticalGlue());
    pnlMain.add(pnlButtons);
    
  }

//  public static void main(String[] a)
//  {
//    GuiUtil.testUiComponent(new AuthorizationDialog(null));
//  }

  public JTextField getTfldLogin()
  {
    return tfldLogin;
  }

  public JTextField getTfldPassword()
  {
    return tfldPassword;
  }

  public JButton getBtnRegister()
  {
    return btnRegister;
  }

  public JButton getBtnCancel()
  {
    return btnCancel;
  }

  public JButton getBtnNext()
  {
    return btnNext;
  }

public JComboBox getCmbxChooseLanguage() {
	return cmbxChooseLanguage;
}
}