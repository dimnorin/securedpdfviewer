package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ua.com.znannya.client.app.ZnclApplication;

public class AdviceOfDayDialog extends JDialog {
	private JEditorPane editrPaneAdvice = new JEditorPane();
	private ResourceBundle uiTextResource;
	private JButton btnClose;
	private JButton btnNextAdvice;
	private JButton btnPrevAdvice;
	private JCheckBox chkBoxShowStart;
	
	public AdviceOfDayDialog(JFrame parent){
		super(parent, true);
		
		uiTextResource  = ZnclApplication.getApplication().getUiTextResources();
		setTitle( uiTextResource.getString("adviceOfDayDialog.Title") );
		setSize(480, 320);
		setLocationByPlatform(true);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setIconImage(ZnclApplication.getApplication().getIcon("Text.png").getImage());
		initComponent();
		loyoutComponent();
	}
	
	private void loyoutComponent() {
		JPanel buttonPanel = new JPanel();		
		buttonPanel.setBorder( BorderFactory.createEmptyBorder(7, 0, 5, 0) );
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
		buttonPanel.add( chkBoxShowStart );
		buttonPanel.add( Box.createHorizontalGlue() );
		buttonPanel.add(btnPrevAdvice);		
		buttonPanel.add( Box.createHorizontalStrut(5));
		buttonPanel.add(btnNextAdvice);
		buttonPanel.add( Box.createHorizontalStrut(5));
		buttonPanel.add( btnClose );

		Container container = getContentPane();		
		container.setLayout( new BorderLayout() );
		container.add( new JScrollPane(editrPaneAdvice), BorderLayout.CENTER );
		container.add( buttonPanel, BorderLayout.SOUTH );
		getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	private void initComponent(){
		btnClose = new JButton(uiTextResource.getString("adviceOfDayDialog.Close"));
		btnNextAdvice = new JButton( uiTextResource.getString("adviceOfDayDialog.btnNextAdvice") );
		btnPrevAdvice = new JButton( uiTextResource.getString("adviceOfDayDialog.btnPrevAdvice") );
		chkBoxShowStart = new JCheckBox( uiTextResource.getString("adviceOfDayDialog.chkBoxShowStart") );
		chkBoxShowStart.setSelected(true);
		editrPaneAdvice.setEditable(false);
		editrPaneAdvice.setContentType("text/html");
	}
	public void setAdvice(String text, String imgPath){
		editrPaneAdvice.setText("" +
				"<html>" +
					"<img src='" + ZnclApplication.class.getResource(imgPath) + "' hspace=10 wspace=10 align=left>" + 
					"<br>" +
					text +
				"</html>");
	}

	public JButton getBtnClose() {
		return btnClose;
	}
	public JButton getBtnNextAdvice() {
		return btnNextAdvice;
	}
	public JButton getBtnPrevAdvice() {
		return btnPrevAdvice;
	}
	public JCheckBox getChkBoxShowStart() {
		return chkBoxShowStart;
	}

}
