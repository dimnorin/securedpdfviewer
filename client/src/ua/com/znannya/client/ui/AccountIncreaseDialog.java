package ua.com.znannya.client.ui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;

import ua.com.znannya.client.app.ZnclApplication;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import javax.swing.JLabel;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class AccountIncreaseDialog extends JDialog {

			
	private ResourceBundle uiTextResources;
	private JPanel jContentPane = null;
	private JPanel bottomPanel = null;
	private JPanel centrePanel = null;
	private JLabel increaseLabel = null;
	private JComboBox numbersBox = null;
	private JLabel urlLabel = null;
	private JTextPane textPane = null;
	private JTextArea notesArea = null;
	private JButton closeButton = null;
	
	private Font defaultFont; 
	
	/**
	 * @param owner
	 */
	public AccountIncreaseDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		this.setSize(380, 280);
		this.setTitle(uiTextResources.getString("accountIncreaseDialog.windowTitle"));
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setResizable(false);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(jContentPane,BoxLayout.X_AXIS));
			Box box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(15));
			box.add(getCentrePanel());
			box.add(getBottomPanel());
			jContentPane.add(Box.createHorizontalStrut(15));
			jContentPane.add(box);
			jContentPane.add(Box.createHorizontalStrut(15));			
		}
		return jContentPane;
	}

	

	

	/**
	 * This method initializes bottomPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.setPreferredSize(new Dimension(0, 40));
			bottomPanel.add(getCloseButton(), null);
		}
		return bottomPanel;
	}

	/**
	 * This method initializes centrePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCentrePanel() {
		if (centrePanel == null) {
			increaseLabel = new JLabel();
			increaseLabel.setText(uiTextResources.getString("accountIncreaseDialog.increasingTo"));
			defaultFont = increaseLabel.getFont();
//			increaseLabel.setFont(new Font("Default",Font.PLAIN,10));
			centrePanel = new JPanel();
			centrePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 1), uiTextResources.getString("accountIncreaseDialog.borderName"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, new Font("Default", Font.BOLD, 12), new Color(51, 51, 51)));
			centrePanel.setLayout(new BoxLayout(centrePanel,BoxLayout.X_AXIS));
			centrePanel.add(Box.createHorizontalStrut(15));
			
			
			Box centreBox = new Box(BoxLayout.Y_AXIS);
			centreBox.add(Box.createVerticalStrut(10));
			
			Box box = new Box(BoxLayout.X_AXIS);
			//box.add(Box.createHorizontalStrut(30));
			box.add(increaseLabel);
			box.add(Box.createHorizontalStrut(10));
			box.add(getNumbersBox());
			box.add(Box.createHorizontalStrut(50));
			box.add(getUrlLabel());
			//box.add(Box.createHorizontalStrut(30));
			
			centreBox.add(box);
			centreBox.add(Box.createVerticalStrut(15));
			centreBox.add(getTextPane());
			//centreBox.add(Box.createVerticalStrut(5));
			centreBox.add(getNotesArea());
			//centreBox.add(Box.createVerticalStrut(10));
			
			centrePanel.add(centreBox);
			centrePanel.add(Box.createHorizontalStrut(15));
			
		}
		return centrePanel;
	}

	/**
	 * This method initializes numbersBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getNumbersBox() {
		if (numbersBox == null) {
			numbersBox = new JComboBox();
			//numbersBox.setBounds(new Rectangle(130, 30, 86, 23));
		}
		return numbersBox;
	}

	/**
	 * This method initializes urlLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	public JLabel getUrlLabel(){
		if (urlLabel == null){
			urlLabel = new JLabel("<html><u>"+uiTextResources.getString("accountIncreaseDialog.urlOtherCountries")+"</u></html>");
			Font fnt = urlLabel.getFont();
			urlLabel.setFont(new Font(fnt.getFontName(),Font.BOLD,fnt.getSize()));
			urlLabel.setForeground(Color.BLUE);			
		}
		return urlLabel;	
	}

	/**
	 * This method initializes text	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextPane getTextPane() {
		if (textPane == null) {
			textPane = new JTextPane();
			textPane.setBackground(this.getBackground());
			textPane.setEditable(false);
			textPane.setContentType("text/html");
//			textPane.setWrapStyleWord(true);
//		    textPane.setLineWrap(true);
		    Font fnt = defaultFont;
			textPane.setFont(new Font(fnt.getFontName(),Font.PLAIN,fnt.getSize()-2));
		}
		return textPane;
	}

	/**
	 * This method initializes notesArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getNotesArea() {
		if (notesArea == null) {
			notesArea = new JTextArea();
			notesArea.setBackground(this.getBackground());
			notesArea.setWrapStyleWord(true);
			notesArea.setLineWrap(true);
			notesArea.setText(uiTextResources.getString("accountIncreaseDialog.note"));
			Font fnt = defaultFont;
			notesArea.setFont(new Font(fnt.getFontName(), Font.PLAIN, fnt.getSize()-2));
		}
		return notesArea;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(" "+uiTextResources.getString("accountIncreaseDialog.btnClose"));
		}
		return closeButton;
	}
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"  
