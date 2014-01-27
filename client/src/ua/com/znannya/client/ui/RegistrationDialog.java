package ua.com.znannya.client.ui;

import java.awt.Dimension;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Registration form dialog.
 */
public class RegistrationDialog extends JDialog
{
  private ResourceBundle uiTextResources;
  private JPanel pnlMain = new JPanel();
  private JLabel lblLogin, lblPassword, lblPasswordAgain, lblEmail, lblFirstName, lblLastName, lblOrganization, lblJobTitle, lblPhone;
  private JTextField tfldLogin = new JTextField();
  private JTextField tfldPassword = new JPasswordField();
  private JTextField tfldPasswordAgain = new JPasswordField();
  private JTextField tfldEmail = new JTextField();
  private JTextField tfldFirstName = new JTextField();
  private JTextField tfldLastName = new JTextField();
  private JTextField tfldOrganization = new JTextField();
  private JTextField tfldJobTitle = new JTextField();
  private JTextField tfldPhone = new JTextField();
  private JButton btnCancel, btnComplete;
  private int labelWidth;

  public RegistrationDialog(JFrame owner)
  {
    super(owner, true);
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
    setTitle(uiTextResources.getString("registrationDialog.windowTitle"));
    setIconImage(ZnclApplication.getApplication().getIcon("User.png").getImage());    
    setSize(380, 385);
    initComponents();
    layoutComponents();
    setLocationRelativeTo(owner);
    add(pnlMain);
  }

  private void initComponents()
  {
    lblLogin = new JLabel(uiTextResources.getString("registrationDialog.lblLogin"));
    lblPassword = new JLabel(uiTextResources.getString("registrationDialog.lblPassword"));
    lblPasswordAgain = new JLabel(uiTextResources.getString("registrationDialog.lblPasswordAgain"));
    lblEmail = new JLabel("Email");
    lblFirstName = new JLabel(uiTextResources.getString("registrationDialog.lblFirstName"));
    lblLastName = new JLabel(uiTextResources.getString("registrationDialog.lblLastName"));
    lblOrganization = new JLabel(uiTextResources.getString("registrationDialog.lblOrganization"));
    lblJobTitle = new JLabel(uiTextResources.getString("registrationDialog.lblJobTitle"));
    lblPhone = new JLabel(uiTextResources.getString("registrationDialog.lblPhone"));
    btnCancel = new JButton(uiTextResources.getString("common.btnCancel"));
    btnComplete = new JButton(uiTextResources.getString("registrationDialog.btnComplete"));
  }

  private void layoutComponents()
  {
    labelWidth = GuiUtil.getMaxLabelWidth(lblLogin, lblPassword, lblPasswordAgain, lblFirstName, lblLastName, lblJobTitle, lblOrganization, lblPhone);
    Box boxFields = new Box(BoxLayout.PAGE_AXIS);
    boxFields.setBorder(BorderFactory.createTitledBorder(uiTextResources.getString("registrationDialog.formTitle")));
    boxFields.add(createFormField(lblLogin, tfldLogin));
    boxFields.add(createFormField(lblPassword, tfldPassword));
    boxFields.add(createFormField(lblPasswordAgain, tfldPasswordAgain));
    boxFields.add(createFormField(lblEmail, tfldEmail));
    boxFields.add(createFormField(lblFirstName, tfldFirstName));
    boxFields.add(createFormField(lblLastName, tfldLastName));
    boxFields.add(createFormField(lblOrganization, tfldOrganization));
    boxFields.add(createFormField(lblJobTitle, tfldJobTitle));
    boxFields.add(createFormField(lblPhone, tfldPhone));
    Box boxButtons = new Box(BoxLayout.LINE_AXIS);
    boxButtons.add(Box.createHorizontalGlue());
    boxButtons.add(btnCancel);
    boxButtons.add(Box.createHorizontalStrut(20));
    boxButtons.add(btnComplete);
    boxButtons.add(Box.createHorizontalStrut(20));

    pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    pnlMain.add(boxFields);
    pnlMain.add(Box.createVerticalGlue());
    pnlMain.add(boxButtons);
  }

  private Box createFormField(JLabel lbl, JComponent comp)
  {
    int colGap = 20, maxHeight = 22, hPadding = 5;
    lbl.setPreferredSize(new Dimension(labelWidth, maxHeight));
    comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight));
    Box box = new Box(BoxLayout.LINE_AXIS);
    box.setBorder(BorderFactory.createEmptyBorder(0, hPadding, 0, hPadding));
    box.add(lbl); box.add(Box.createHorizontalStrut(colGap)); box.add(comp);
    return box;
  }

  public static void main(String[] a)
  {
    GuiUtil.testUiComponent(new RegistrationDialog(null));
  }

  public JTextField getTfldLogin()
  {
    return tfldLogin;
  }

  public JTextField getTfldPassword()
  {
    return tfldPassword;
  }

  public JTextField getTfldPasswordAgain()
  {
    return tfldPasswordAgain;
  }

  public JTextField getTfldEmail()
  {
    return tfldEmail;
  }

  public JTextField getTfldFirstName()
  {
    return tfldFirstName;
  }

  public JTextField getTfldLastName()
  {
    return tfldLastName;
  }

  public JTextField getTfldOrganization()
  {
    return tfldOrganization;
  }

  public JTextField getTfldJobTitle()
  {
    return tfldJobTitle;
  }

  public JTextField getTfldPhone()
  {
    return tfldPhone;
  }

  public JButton getBtnCancel()
  {
    return btnCancel;
  }

  public JButton getBtnComplete()
  {
    return btnComplete;
  }
}
