package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jivesoftware.smack.ZnConfiguration;

import ua.com.znannya.client.app.ZnclApplication;

public class AboutDialog extends JDialog{

	private JEditorPane editorPaneAbout;
	private JLabel icon;
	private static JButton btnOk;
	
	public AboutDialog(){
		setSize(450, 207);
		setResizable(false);
		setLocationRelativeTo( ZnclApplication.getApplication().getMainFrame() );
		setTitle( ZnclApplication.getApplication().getUiTextResources().getString("mainFrame.help.miAbout"));
		initComponent();
		layotComponent();
	}
	
	private void initComponent() {
		icon = new JLabel( ZnclApplication.getApplication().getIcon("about_logo.gif") );
		btnOk = new JButton("Ok");
		btnOk.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		ResourceBundle uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		
		editorPaneAbout = new JEditorPane();
		editorPaneAbout.setEditable(false);
		editorPaneAbout.setContentType("text/html");
		String text = String.format(uiTextResources.getString("mainFrame.about.version"),  ZnConfiguration.CURRENT_CLIENT_VERSION );
		
		editorPaneAbout.setText(
				"<html>" +
					"<center>" +
						uiTextResources.getString("mainFrame.about.company") + "<br>" +
						text + "<br>" +
						"(c) Copyright Eureka 2010 Inc.  All rights reserved." + "<br>" +
						uiTextResources.getString("mainFrame.about.vebSite") +
						"<a href=\"http://eureka-i.com\">" + "http://eureka-i.com" + "</a>" +
					"</center>" +
				"</html>");
		
		editorPaneAbout.addHyperlinkListener(new HyperlinkListener() {
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

	private void layotComponent() {
		JPanel panelOfButton = new JPanel();
		panelOfButton.setLayout( new BoxLayout(panelOfButton, BoxLayout.X_AXIS) );
		panelOfButton.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		panelOfButton.add( Box.createHorizontalGlue() );
		panelOfButton.add( btnOk );
		
		setLayout( new BorderLayout() );
		add( icon, BorderLayout.WEST );
		add( editorPaneAbout, BorderLayout.CENTER );
		add( panelOfButton, BorderLayout.SOUTH );
	}
	
	public static void main(String args[]){
		new AboutDialog().setVisible(true);
	}
}
