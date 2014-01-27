package ua.com.znannya.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import ua.com.znannya.client.ctrl.FileDownloadDialogController;

/**
 * Tabbed pane for displaying search results table and downloaded documents.
 */
public class DocumentsPane extends JTabbedPane
{
  public final static String[] COL_NAMES = {"name", "author", "isbn", "publisherName", "UDK_idx", "BBK_idx", "kind", "code", "organization", "city", "year", "pages", "DACNTI_code", "UDK_idx", "description"};
  public final static int TOTAL_COLUMN_COUNT = COL_NAMES.length + 1;
  
  private IAllTabsClosedListener allTabsClosedListener;

  public DocumentsPane()
  {
	  layoutComponents();
  }

  private void layoutComponents()
  {
    Box boxPagination = new Box(BoxLayout.LINE_AXIS);
    boxPagination.add(Box.createHorizontalStrut(3));
    boxPagination.add(Box.createHorizontalStrut(3));
    Box boxResultsCount = new Box(BoxLayout.LINE_AXIS);
    Box boxSearchControls = new Box(BoxLayout.LINE_AXIS);
    boxSearchControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    boxSearchControls.add(boxResultsCount);
    boxSearchControls.add( Box.createHorizontalStrut(10) );
    boxSearchControls.add(Box.createHorizontalGlue());
    boxSearchControls.add(boxPagination);

    JPanel pnlSearchResults = new JPanel();
    pnlSearchResults.setLayout(new BoxLayout(pnlSearchResults, BoxLayout.PAGE_AXIS));
    pnlSearchResults.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1000));
    pnlSearchResults.add(boxSearchControls);

    Box labelBox = Box.createHorizontalBox();
    labelBox.add(Box.createHorizontalGlue());
  }
  
  public void setGrayBackGround(){
	  if ( getBackground() != Color.GRAY ){
		  UIManager.put("TabbedPaneUI", "javax.swing.plaf.basic.BasicTabbedPaneUI");
		  SwingUtilities.updateComponentTreeUI(this);
		  setBackground(Color.GRAY);	  
	  }
  }
  public void setWhiteBackGround(){
	  if ( getBackground() != Color.LIGHT_GRAY ){
		  UIManager.put("TabbedPaneUI", "javax.swing.plaf.basic.BasicTabbedPaneUI");
		  SwingUtilities.updateComponentTreeUI(this);
		  setBackground(Color.LIGHT_GRAY);	  
	  }
  }
  
  public void addTab(String t, Component c, boolean closeable)
  {
    super.addTab(t, c);
    final int idx = indexOfComponent(c);
    if (closeable) {
      setTabComponentAt(idx, new ClosableTab(t, this));
    }
  }
  
  /**
   * Close tabs when document should stop showing
   */
  public void closeDocumentTabs(){
	  for (int i = 0; i < getTabCount(); i++) {
		  Component tab = getTabComponentAt(i);
		  if(tab instanceof ClosableTab){
//			  remove(i);
			  ((ClosableTab)tab).close();
			  i = 0;
//			  removeTabAt(i);
		  }
	  }
  }
  
  public void closeAllTabs(){
	  while (getTabCount() > 0) {
		  Component tab = getTabComponentAt(0);
		  remove( indexOfTabComponent(tab) );
	  }
  }

  class ClosableTab extends JPanel implements ActionListener
  {
    JButton btnClose = new JButton("x");
    JTabbedPane tabbedPane;

    public ClosableTab(String title, JTabbedPane tp)
    {
      tabbedPane = tp;
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
      btnClose.setMargin(new Insets(0, 0, 0, 0));
      btnClose.setMaximumSize(new Dimension(16, 16));
      btnClose.setPreferredSize(new Dimension(16, 16));
      btnClose.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
      btnClose.addActionListener(this);
      add(new JLabel(title));
      add(Box.createHorizontalStrut(5));
      add(Box.createHorizontalGlue());
      add(btnClose);
      setBackground(Color.LIGHT_GRAY);
    }

    public void actionPerformed(ActionEvent e)
    {
    	close();
    }
    
    public void close(){
    	 tabbedPane.remove( tabbedPane.indexOfTabComponent(this) );
         if(!isClosableTabExists()) notifyAllTabsClosed();
         FileDownloadDialogController.countOfOpenedFiles--;
         if ( tabbedPane.getTabCount() == 0 )
        	 setGrayBackGround();
    }
  }
  
  public synchronized boolean isClosableTabExists(){
	  for (int i = 0; i < getTabCount(); i++) {
		  Component tab = getTabComponentAt(i);
		  if(tab instanceof ClosableTab) return true;
	  }
	  return false;
  }
  
  private void notifyAllTabsClosed(){
	  if(allTabsClosedListener != null) allTabsClosedListener.tabsClosed();
  }
  
  public interface IAllTabsClosedListener{
	  void tabsClosed();
  }
  
  public void addAllTabsClosedListener(IAllTabsClosedListener listener){
	  allTabsClosedListener = listener;
  }

  class ResizeListener implements TableColumnModelListener
  {
    public void columnMarginChanged(ChangeEvent e)
    {
    	
    	//System.out.println("column changed");
 
    }

    public void columnSelectionChanged(ListSelectionEvent e) { }
    public void columnAdded(TableColumnModelEvent e) { }
    public void columnRemoved(TableColumnModelEvent e) { }
    public void columnMoved(TableColumnModelEvent e) { }
       
  }
}
