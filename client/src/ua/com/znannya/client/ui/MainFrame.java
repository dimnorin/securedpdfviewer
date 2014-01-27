package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.security.SecurityController;
import ua.com.znannya.client.ui.widgets.ZnclStatusBar;

/**
 * Main app's window frame. Contains menu, toolbar, status bar, main panel.
 */
public class MainFrame extends JFrame
{
  private ResourceBundle uiTextResources;

//  private JToolBar toolBar;					//need for simple search
//  private JTextField tfldSimpleSearch = new JTextField();
//  private JButton btnStartSimpleSearch, btnOpenComplexSearch;
  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;
  
  private ZnclStatusBar statusBar = new ZnclStatusBar();
  private JPanel pnlMain = new JPanel();
  private JToolBar toolBar;
  private JPopupMenu popupHelpMenu;
  private JPopupMenu popupExitMenu;
  private JButton btnFavorit;
  private JButton btnSearch;
  private JButton btnPrivateRoom;
  private JButton btnHelp;
  private JButton btnExit;
  private JMenuItem miContent;
  private JMenuItem miAdvices;
  private JMenuItem miAbout;
  
  private JMenuItem miSwitchUser;
  private JMenuItem miExit;
  
  public MainFrame()
  {
    uiTextResources = ZnclApplication.getApplication().getUiTextResources();
    setSize(WIDTH, HEIGHT);
    setLayout(new BorderLayout());
    setLocationByPlatform(true);
    setTitle(uiTextResources.getString("mainFrame.windowTitle"));
    setIconImage(ZnclApplication.getApplication().getIcon("icon.gif").getImage());

    initComponents();
    createMenuBar();
//    createToolBar();							//need for simple search

//    setJMenuBar(menuBar);
    add(toolBar, BorderLayout.NORTH);
    add(pnlMain, BorderLayout.CENTER);
    add(statusBar, BorderLayout.SOUTH);
//    setupFocusHandling();						//need for simple search
  }
  
  private void initComponents()
  {
	  miContent = new JMenuItem(uiTextResources.getString("mainFrame.help.miContent"));
	  miAdvices = new JMenuItem(uiTextResources.getString("mainFrame.help.miAdvices"));
	  miAbout = new JMenuItem(uiTextResources.getString("mainFrame.help.miAbout"));
	  
	  miSwitchUser = new JMenuItem(uiTextResources.getString("mainFrame.exit.switchUser"));
	  miExit = new JMenuItem(uiTextResources.getString("mainFrame.exit.exit"));
	  
	  popupHelpMenu = new JPopupMenu();
	  popupHelpMenu.add(miContent);
	  popupHelpMenu.add(miAdvices);
	  popupHelpMenu.add(miAbout);
	  
	  popupExitMenu = new JPopupMenu();
	  popupExitMenu.add(miSwitchUser);
	  popupExitMenu.add(miExit);
	
	  pnlMain.setBorder(BorderFactory.createLoweredBevelBorder());
	  pnlMain.setBackground(Color.GRAY);
	  pnlMain.setLayout(new GridLayout(1, 1));
//////////////////////////////////////////////////////////////////////////////////////////
//need for simple search   
//    tfldSimpleSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
//    tfldSimpleSearch.setText(uiTextResources.getString("mainFrame.toolbar.simpleSearchTip"));
//    btnStartSimpleSearch = new JButton(ZnclApplication.getApplication().getIcon("Search.png"));
//    btnOpenComplexSearch = new JButton("<html><a href='#'>" + uiTextResources.getString("mainFrame.toolbar.btnOpenComplexSearch") + "</a></html>");
//    btnOpenComplexSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
//    btnOpenComplexSearch.setBorderPainted(false);
//    btnOpenComplexSearch.setMaximumSize(new Dimension(100, 22));
//////////////////////////////////////////////////////////////////////////////////////////
	  statusBar.setMainStatusText(uiTextResources.getString("mainFrame.statusBar.waitingForUser"));   // TODO: provisional data for status bar, remove later
	  statusBar.setAvailableFunds("0");
//    statusBar.setAvailableTime("0:0");
//    statusBar.setUsedFunds("0");
//    statusBar.setUsedTime("0:0");
//    statusBar.setUsedPages(0);
	  statusBar.updateUsages();
  }

  private void createMenuBar()
  {
    btnFavorit = new JButton( uiTextResources.getString("mainFrame.menu.favorits") );
    btnFavorit.setFocusPainted(false);
    btnFavorit.setFocusable(false);
    btnSearch = new JButton( uiTextResources.getString("mainFrame.menu.search") );
    btnSearch.setFocusPainted(false);
    btnSearch.setFocusable(false);
    btnSearch.setToolTipText("Ctrl + F");
    btnPrivateRoom = new JButton( uiTextResources.getString("mainFrame.menu.privateRoom") );
    btnPrivateRoom.setFocusPainted(false);
    btnPrivateRoom.setFocusable(false);
    btnHelp = new JButton( uiTextResources.getString("mainFrame.menu.help") );
    btnHelp.setFocusPainted(false);
    btnHelp.setFocusable(false);
    btnExit = new JButton(uiTextResources.getString("mainFrame.menu.exit"));
    btnExit.setFocusPainted(false);
    btnExit.setFocusable(false);
    
    toolBar = new JToolBar();
    

    JComboBox cmbxHelp = new JComboBox( new Object[]{new JButton("1"), new JButton("2"), new JButton("3")} );
    cmbxHelp.setFocusable(false);

    toolBar.add(btnFavorit);
    toolBar.add(btnSearch);
    toolBar.add(btnPrivateRoom);
    toolBar.add(btnHelp);
//    toolBar.addSeparator();
    toolBar.add(Box.createHorizontalGlue());
    toolBar.add(btnExit);
    
    toolBar.setFloatable(false);
    toolBar.setFocusable(false);
  }

  @Override
  public void setVisible(boolean flag){
	  super.setVisible(flag);
	  SecurityController.ifAppCanBeStarted(flag);
  }
 
//////////////////////////////////////////////////////////////////////////////////////////
//need for simple search
  
//  private void createToolBar()
//  {
//    toolBar = new JToolBar();
//    toolBar.setFloatable(false);
//    toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//    toolBar.add(tfldSimpleSearch);
//    toolBar.add(Box.createHorizontalStrut(5));
//    toolBar.add(btnStartSimpleSearch);
//    toolBar.add(Box.createHorizontalStrut(15));
//    toolBar.add(btnOpenComplexSearch);
//    toolBar.add(Box.createHorizontalStrut(30));
//
//  }
//
//  private void setupFocusHandling()
//  {
//    addWindowFocusListener(new WindowAdapter() {
//      @Override public void windowGainedFocus(WindowEvent e)
//      {
//        btnStartSimpleSearch.requestFocusInWindow();
//      }
//    });
//    tfldSimpleSearch.addFocusListener(new FocusAdapter() {
//      @Override public void focusGained(FocusEvent e)
//      {
//        tfldSimpleSearch.selectAll();
//        tfldSimpleSearch.requestFocusInWindow();
//      }
//    });
//  }
//
//  public JTextField getTfldSimpleSearch()
//  {
//    return tfldSimpleSearch;
//  }
//
//  public JButton getBtnStartSimpleSearch()
//  {
//    return btnStartSimpleSearch;
//  }

// end of simple Search
///////////////////////////////////////////////////////////////////////////  
  public void updateComponent(){
	  uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	  btnFavorit.setText( uiTextResources.getString("mainFrame.menu.favorits") );
	  btnSearch.setText( uiTextResources.getString("mainFrame.menu.search") );
	  btnPrivateRoom.setText( uiTextResources.getString("mainFrame.menu.privateRoom") );
	  btnHelp.setText( uiTextResources.getString("mainFrame.menu.help") );
	  btnExit.setText(uiTextResources.getString("mainFrame.menu.exit"));
	  
	  statusBar.updateUsages();
	  statusBar.setMainStatusText(uiTextResources.getString("mainFrame.statusBar.waitingForUser"));
	  miContent.setText(uiTextResources.getString("mainFrame.help.miContent"));
	  miAdvices.setText(uiTextResources.getString("mainFrame.help.miAdvices"));
	  miAbout.setText(uiTextResources.getString("mainFrame.help.miAbout"));
	  
	  miSwitchUser.setText(uiTextResources.getString("mainFrame.exit.switchUser"));
	  miExit.setText(uiTextResources.getString("mainFrame.exit.exit"));
  }
  
  public JPanel getPnlMain()
  {
    return pnlMain;
  }
  
  public ZnclStatusBar getStatusBar(){
	  return statusBar;
  }

  public JMenuItem getMiRefundBySms() {
	  return null;//miRefundBySMS;
  } 
  
  public JMenuItem getMiRefundByCard() {
	  return null;//miRefundByCard;
  } 
  
public JButton getBtnFavorit() {
	return btnFavorit;
}

public JButton getBtnSearch() {
	return btnSearch;
}

public JButton getBtnPrivateRoom() {
	return btnPrivateRoom;
}

public JButton getBtnHelp() {
	return btnHelp;
}

public JButton getBtnExit() {
	return btnExit;
}

public JPopupMenu getPopupHelpMenu() {
	return popupHelpMenu;
}

public JPopupMenu getPopupExitMenu() {
	return popupExitMenu;
}

public JMenuItem getMiContent() {
	return miContent;
}

public JMenuItem getMiAdvices() {
	return miAdvices;
}

public JMenuItem getMiAbout() {
	return miAbout;
}

public JMenuItem getMiSwitchUser() {
	return miSwitchUser;
}

public JMenuItem getMiExit() {
	return miExit;
}

public static void main(String[] args){
	  MainFrame mfr = new MainFrame();
	  mfr.setVisible(true);
	  mfr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
  }
}
