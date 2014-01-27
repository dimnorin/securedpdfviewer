package stressTest.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import ua.com.znannya.client.util.GuiUtil;

public class TestFrame extends JFrame {
	private ButtonGroup buttonGroup;
	private JRadioButton radioBtnParallel;
	private JRadioButton radioBtnChain;
	private JButton buttonStart;
	private JTextPane textPaneStatus;
	private JTextField textFieldCountUser;
	
	public TestFrame(){
		setTitle("Stress Test");
		setSize(300, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponent();
		layoutComponent();
	}

	private void initComponent() {
		buttonGroup = new ButtonGroup();
		buttonGroup.getSelection();
		radioBtnParallel = new JRadioButton("Parallel test");
		radioBtnParallel.setSelected(true);
		radioBtnChain = new JRadioButton("Chain test");
		buttonGroup.add( radioBtnParallel );
		buttonGroup.add( radioBtnChain );
		buttonStart = new JButton("Start Test");
		textFieldCountUser = new JTextField(10);
		textPaneStatus = new JTextPane();
		textPaneStatus.setMinimumSize(new Dimension(300, 300));
		textPaneStatus.setMaximumSize(new Dimension(700, 500));
		textPaneStatus.setPreferredSize(new Dimension(700, 500));
	}
	
	private void layoutComponent(){
		JPanel panelWithSwitchers = new JPanel();
		panelWithSwitchers.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0; c.gridy = 0;
		panelWithSwitchers.add(radioBtnParallel, c);
		
		c.gridx = 1; c.gridy = 0;
		panelWithSwitchers.add(radioBtnChain, c);
		
		c.gridx = 0; c.gridy = 1;
		panelWithSwitchers.add( new JLabel("Count of User: "), c );
		
		c.gridx = 1; c.gridy = 1;
		panelWithSwitchers.add(textFieldCountUser, c);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(buttonStart);
		
		setLayout( new BorderLayout() );
		add( panelWithSwitchers, BorderLayout.NORTH );
		add( new JScrollPane(textPaneStatus), BorderLayout.CENTER );
		add( buttonPanel, BorderLayout.SOUTH );
	}
	
	public JButton getBtnStart() {
		return buttonStart;
	}

	public JTextPane getTextPaneStatus() {
		return textPaneStatus;
	}

	public JTextField getTextFieldCountUser() {
		return textFieldCountUser;
	}

	public JRadioButton getRadioBtnParallel() {
		return radioBtnParallel;
	}

	public JRadioButton getRadioBtnChain() {
		return radioBtnChain;
	}

	public static void main( String[] args ){
		GuiUtil.setSysLAF();
		new TestFrame().setVisible(true);
	}
}
