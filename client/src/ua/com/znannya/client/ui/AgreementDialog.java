package ua.com.znannya.client.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import ua.com.znannya.client.app.ZnclApplication;
//import ua.com.znannya.client.util.FileUtil;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Agreement form dialog.
 */
public class AgreementDialog extends JDialog
{
  private ResourceBundle uiTextResources;
  private JPanel pnlMain = new JPanel();
  private JPanel pnlRadio = new JPanel();
  private JPanel pnlButtons = new JPanel();
  private JEditorPane jepAgreement;
  private JRadioButton rbtnAccept, rbtnReject;
  private JButton btnCancel, btnNext;
  
  private static Logger logger = Logger.getLogger(AgreementDialog.class.getName());

  public AgreementDialog(JFrame owner)
  {
    super(owner, true);
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
    setTitle(uiTextResources.getString("agreementDialog.windowTitle"));
    setIconImage(ZnclApplication.getApplication().getIcon("Text.png").getImage());
    setSize(400, 400);
    setLocationRelativeTo(owner);
    initComponents();
    layoutComponents();
  }


  private void initComponents()
  {
	  URL url = ZnclApplication.class.getResource("/help/agreement_"+Locale.getDefault().getLanguage()+".htm");
//    jepAgreement.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 12));
    try{
//    	tarAgreement.setText(FileUtil.loadFile(url.openStream()));//uiTextResources.getString("agreementDialog.agreementText"));
    	jepAgreement = new JEditorPane(url);
    	jepAgreement.setEditable(false);
    	jepAgreement.setContentType("text/html");
    }catch (Exception e) {
		logger.log(Level.WARNING, "", e);
		jepAgreement = new JEditorPane();
		jepAgreement.setText(uiTextResources.getString("agreementDialog.agreementText"));
	}

    rbtnAccept = new JRadioButton(uiTextResources.getString("agreementDialog.rbtnAccept"));
    rbtnReject = new JRadioButton(uiTextResources.getString("agreementDialog.rbtnReject"));
    ButtonGroup group = new ButtonGroup();
    group.add(rbtnAccept);
    group.add(rbtnReject);

    btnCancel = new JButton(uiTextResources.getString("common.btnCancel"));
    btnNext = new JButton(uiTextResources.getString("common.btnNext"));
    btnNext.setEnabled(false);
  }

  private void layoutComponents()
  {
    Box vbox = new Box(BoxLayout.PAGE_AXIS);
    vbox.add(rbtnAccept);
    vbox.add(rbtnReject);
    pnlRadio.setLayout(new BoxLayout(pnlRadio, BoxLayout.LINE_AXIS));
    pnlRadio.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
    pnlRadio.add(vbox);
    pnlRadio.add(Box.createHorizontalGlue());
    
    pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
    pnlButtons.add(Box.createHorizontalGlue());
    pnlButtons.add(btnCancel);
    pnlButtons.add(Box.createHorizontalStrut(20));
    pnlButtons.add(btnNext);
    pnlButtons.add(Box.createHorizontalStrut(20));

    pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    JScrollPane scp = new JScrollPane(jepAgreement);
    scp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    pnlMain.add(scp);
    pnlMain.add(pnlRadio);
    pnlMain.add(pnlButtons);
    add(pnlMain);
  }

  public void updateComponent(){
	  ResourceBundle uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	  URL url = ZnclApplication.class.getResource("/help/agreement_"+Locale.getDefault().getLanguage()+".htm");
	  try{
		  jepAgreement.setPage(url);
	  }catch (Exception e) {
		  logger.log(Level.WARNING, "", e);
		  jepAgreement.setText(uiTextResources.getString("agreementDialog.agreementText"));
	  }
	  rbtnAccept.setText(uiTextResources.getString("agreementDialog.rbtnAccept"));
	  rbtnReject.setText(uiTextResources.getString("agreementDialog.rbtnReject"));
	  btnCancel.setText(uiTextResources.getString("common.btnCancel"));
	  btnNext.setText(uiTextResources.getString("common.btnNext"));
  }

  public JEditorPane getJepAgreement()
  {
    return jepAgreement;
  }

  public JRadioButton getRbtnAccept()
  {
    return rbtnAccept;
  }

  public JRadioButton getRbtnReject()
  {
    return rbtnReject;
  }

  public JButton getBtnCancel()
  {
    return btnCancel;
  }

  public JButton getBtnNext()
  {
    return btnNext;
  }
}
