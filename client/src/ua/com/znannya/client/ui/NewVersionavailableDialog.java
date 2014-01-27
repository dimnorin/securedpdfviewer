package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.security.SecurityController;

public class NewVersionavailableDialog extends JDialog{
	private ResourceBundle uiTextResources;
	private JButton btnOK;
	private JEditorPane editPaneMessage;
	
	public NewVersionavailableDialog(JFrame owner){
		super(owner, true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		setIconImage(ZnclApplication.getApplication().getIcon("icon.jpg").getImage());

		setTitle( uiTextResources.getString("NewVersionavailableDialog.title") );
		setSize(250, 200);
		
		initComponent();
		layoutComponent();
		
		setLocationRelativeTo(ZnclApplication.getApplication().getMainFrame());
	}

	private void initComponent() {
		btnOK = new JButton("OK");
		btnOK.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
		    	ZnclApplication.getApplication().askToClose();
			}
		});
		
		editPaneMessage = new JEditorPane();
		editPaneMessage.setEditable(false);
		editPaneMessage.setContentType("text/html");
		
		editPaneMessage.setText(
				"<html>" +
					"<center>" +
						uiTextResources.getString("NewVersionavailableDialog.msg") + "<br>" +
						"<a href=\"http://eureka-i.com\">" + "http://eureka-i.com" + "</a>" +
					"</center>" +
				"</html>");
		editPaneMessage.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ){
			    	try{
			    		Desktop.getDesktop().browse( e.getURL().toURI() );
			    	}catch (Exception e1) {
			    		JOptionPane.showMessageDialog( ZnclApplication.getApplication().getMainFrame(), String.format(ZnclApplication.getApplication().getUiTextResources()
			    				.getString("accountRefund.card.cantopen.url"), e.getURL()) );
			    	}
				}
			}
		});
	}
	
	private void layoutComponent() {
		JPanel pnlButton = new JPanel();
		pnlButton.setLayout( new BoxLayout(pnlButton, BoxLayout.X_AXIS) );
		pnlButton.add( Box.createHorizontalGlue() );
		pnlButton.add( btnOK );
		pnlButton.add( Box.createHorizontalGlue() );
		setLayout( new BorderLayout() );
		add( editPaneMessage, BorderLayout.CENTER );
		add( pnlButton, BorderLayout.SOUTH );
	}
	
}
