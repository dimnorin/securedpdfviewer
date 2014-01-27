package ua.com.znannya.client.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import ua.com.znannya.client.app.ZnclApplication;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class ChoosePagesDialog extends JDialog {

	private JPanel jContentPane = null;
    private ResourceBundle uiTextResources;
	private JList pagesList = null;
	private JPanel buttonPanel = null;
	private JList selectPagesList = null;
	private JButton addBtn = null;
	private JButton delBtn = null;
	private JScrollPane scrollPaneLeft = null;
	private JScrollPane scrollPaneRight = null;
	private JButton okBtn = null;
	
	/**
	 * @param owner
	 */
	public ChoosePagesDialog(Frame owner) {
		super(owner);
		this.setLocationRelativeTo(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//this.setSize(200, 200);
		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		this.setTitle(uiTextResources.getString("fileDownloadDialog.choicePagesDlg.title"));
		this.setModal(true);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.pack();
		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			
			Box box1 = new Box(BoxLayout.LINE_AXIS);
			box1.add(getScrollPaneLeft());
			box1.add(getButtonPanel());
			box1.add(getScrollPaneRight());
			
			jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.PAGE_AXIS));
			box1.setAlignmentX(0.99f);
			jContentPane.add(box1);
			jContentPane.add(Box.createVerticalStrut(5));
			
			getOkBtn().setAlignmentX(RIGHT_ALIGNMENT);
			jContentPane.add(getOkBtn());
		}
		return jContentPane;
	}

	/**
	 * This method initializes pagesList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getPagesList() {
		if (pagesList == null) {
			pagesList = new JList();
			pagesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			pagesList.setBorder(new LineBorder(Color.GRAY));
		}
		return pagesList;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));
			buttonPanel.setPreferredSize(new Dimension(50, 200));			
			//buttonPanel.setMinimumSize(new Dimension(50, 30));
			
			Box buttonBox = new Box(BoxLayout.Y_AXIS);
			buttonBox.add(Box.createVerticalStrut(20));
			buttonBox.add(getAddBtn());
			buttonBox.add(Box.createVerticalStrut(20));
			buttonBox.add(getDelBtn());
			buttonBox.add(Box.createVerticalStrut(20));
			
            buttonPanel.add(Box.createHorizontalStrut(10));
            buttonPanel.add(buttonBox);
            buttonPanel.add(Box.createHorizontalStrut(10));
		}
		return buttonPanel;
	}

	/**
	 * This method initializes selPgsList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getSelectPagesList() {
		if (selectPagesList == null) {
			selectPagesList = new JList();
			selectPagesList.setBorder(new LineBorder(Color.GRAY));
		}
		return selectPagesList;
	}

	/**
	 * This method initializes addBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getAddBtn() {
		if (addBtn == null) {
			addBtn = new JButton();
			addBtn.setText(">");
		}
		return addBtn;
	}

	/**
	 * This method initializes delBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getDelBtn() {
		if (delBtn == null) {
			delBtn = new JButton();
			delBtn.setText("<");
		}
		return delBtn;
	}
	
	/**
	 * This method initializes okBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getOkBtn() {
		if (okBtn == null) {
			okBtn = new JButton();
			okBtn.setText("Ok");
		}
		return okBtn;
	}

	private JScrollPane getScrollPaneLeft() {
		if (scrollPaneLeft == null){
			scrollPaneLeft = new JScrollPane();
			scrollPaneLeft.setViewportView(getPagesList());
			scrollPaneLeft.setPreferredSize(new Dimension(80, 200));
		}
		return scrollPaneLeft;
	}	
	
	private JScrollPane getScrollPaneRight() {
		if (scrollPaneRight == null){
			scrollPaneRight = new JScrollPane();
			scrollPaneRight.setViewportView(getSelectPagesList());
			scrollPaneRight.setPreferredSize(new Dimension(80, 200));
		}
		return scrollPaneRight;
	}
	
}  
