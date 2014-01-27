package ua.com.znannya.client.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.dao.Publication;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.DissertationService;
import ua.com.znannya.client.service.PublicationService;
import ua.com.znannya.client.ui.widgets.MultiLineCellRenderer;
import ua.com.znannya.client.ui.widgets.MultiLineHeaderRenderer;
import ua.com.znannya.client.ui.widgets.ObjectListTableModel;
import ua.com.znannya.client.util.GuiUtil;

public class ResultsPane extends JPanel{
	  public final static String[] COL_NAMES = DocumentsPane.COL_NAMES;
	  public final static int TOTAL_COLUMN_COUNT = COL_NAMES.length + 1;
	  private final static int DISS_COLUMN_COUNT = DissertationService.COL_NAMES.length + 1;
	  private final static int PUBL_COLUMN_COUNT = PublicationService.COL_NAMES.length + 1;
	  private int TABLE_WIDTH = 1600;
	  
	  private ResourceBundle uiTextResources;
	  private JPanel pnlResultsTables = new JPanel();
	  private JPanel pnlFiles = new JPanel();
	  private JTabbedPane tpnFiles = new JTabbedPane();
	  private JLabel lblFilesCaption, lblResultsCount;
	  private JButton btnFirstPage = new JButton("<<");
	  private JButton btnPrevPage = new JButton("<");
	  private JButton btnNextPage = new JButton(">");
	  private JButton btnLastPage = new JButton(">>");
	  private JButton btnAddInFavorits;
	  private JButton btnDellFromFavorits;
	  private JComboBox cbxPages = new JComboBox();
	  private JTable tblSearchResults = new JTable();
	  private JTable tblRusFiles = new JTable();
	  private JTable tblUkrFiles = new JTable();
	  private JTable tblEnFiles = new JTable();
	  private JTable tblOtherFiles = new JTable();
	  private SearchResultsTableModel searchResultsTableModel;
	  private FilesTableModel rusFilesTableModel, ukrFilesTableModel, enFilesTableModel, otherFilesTableModel;

	  public ResultsPane(){
		  uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		  btnAddInFavorits = new JButton("<html><a href='#'>" + uiTextResources.getString("mainFrame.btnAddInFavorits") + "</a></html>");
		  btnAddInFavorits.setMaximumSize( new Dimension(100, 22) );
		  btnAddInFavorits.setFocusPainted(false);
		  btnAddInFavorits.setMargin( new Insets(0, 0, 0, 0) );
		  btnAddInFavorits.setContentAreaFilled(false);
		  btnAddInFavorits.setBorderPainted(false);
		  btnAddInFavorits.setOpaque(false);
		  
		  btnDellFromFavorits = new JButton("<html><a href='#'>" + uiTextResources.getString("mainFrame.btnDellFromFavorits") + "</a></html>");
		  btnDellFromFavorits.setMaximumSize( new Dimension(100, 22) );
		  btnDellFromFavorits.setFocusPainted(false);
		  btnDellFromFavorits.setMargin( new Insets(0, 0, 0, 0) );
		  btnDellFromFavorits.setContentAreaFilled(false);
		  btnDellFromFavorits.setBorderPainted(false);
		  btnDellFromFavorits.setOpaque(false);
		  btnDellFromFavorits.setVisible(false);
		  
		  initComponents();
		  layoutComponents();
		  
		  final JFrame mainFraim = ZnclApplication.getApplication().getMainFrame(); 
		  mainFraim.addComponentListener(new ComponentAdapter() {
			  public void componentResized(ComponentEvent e){
				  if ( tblSearchResults.getSize().getWidth() < mainFraim.getWidth() )
					  tblSearchResults.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
				  if ( mainFraim.getWidth() < TABLE_WIDTH )
					  tblSearchResults.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			  }
		  });
	  }

	  private void initComponents()
	  {
	    searchResultsTableModel = new SearchResultsTableModel();
	    rusFilesTableModel = new FilesTableModel();
	    ukrFilesTableModel = new FilesTableModel();
	    enFilesTableModel = new FilesTableModel();
	    otherFilesTableModel = new FilesTableModel();	    
	    tblSearchResults.setModel(searchResultsTableModel);
	    tblRusFiles.setModel(rusFilesTableModel);
	    tblUkrFiles.setModel(ukrFilesTableModel);
	    tblEnFiles.setModel(enFilesTableModel);
	    tblOtherFiles.setModel(otherFilesTableModel);
	    tblSearchResults.getColumnModel().addColumnModelListener(new ResizeListener());
	    tblSearchResults.getTableHeader().setReorderingAllowed(false);
	    tblSearchResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    setRendr();
	    tblSearchResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tblRusFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tblUkrFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tblEnFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tblOtherFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    pnlResultsTables.setLayout(new BoxLayout(pnlResultsTables, BoxLayout.PAGE_AXIS));
	    lblFilesCaption = new JLabel(uiTextResources.getString("documentsPane.lblFilesCaption"));
	    lblFilesCaption.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
	    lblResultsCount = new JLabel();
	    setResultsCount(0);
	    setFilesPanelVisible(false);
	    int pagingControlHeight = 20;
	    Dimension dimPagingButton = new Dimension(20, pagingControlHeight);
	    btnFirstPage.setPreferredSize(dimPagingButton);
	    btnLastPage.setPreferredSize(dimPagingButton);
	    btnNextPage.setPreferredSize(dimPagingButton);
	    btnPrevPage.setPreferredSize(dimPagingButton);
	    Font fntPagBtn = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	    btnFirstPage.setFont(fntPagBtn);
	    btnLastPage.setFont(fntPagBtn);
	    btnNextPage.setFont(fntPagBtn);
	    btnPrevPage.setFont(fntPagBtn);
	    btnFirstPage.setMargin(new Insets(0, 0, 0, 0));
	    btnLastPage.setMargin(new Insets(0, 0, 0, 0));
	    btnNextPage.setMargin(new Insets(0, 2, 0, 2));
	    btnPrevPage.setMargin(new Insets(0, 2, 0, 2));
	    cbxPages.setMaximumSize(new Dimension(50, pagingControlHeight));
	  }

	  private void layoutComponents()
	  {
//	    this.addTab(uiTextResources.getString("documentsPane.resultsTab"), pnlResultsTables);
	    tpnFiles.addTab(uiTextResources.getString("documentsPane.tpnFiles.ukrTab"), new JScrollPane(tblUkrFiles));
	    tpnFiles.addTab(uiTextResources.getString("documentsPane.tpnFiles.rusTab"), new JScrollPane(tblRusFiles));
	    tpnFiles.addTab(uiTextResources.getString("documentsPane.tpnFiles.enTab"), new JScrollPane(tblEnFiles));
	    tpnFiles.addTab(uiTextResources.getString("documentsPane.tpnFiles.otTab"), new JScrollPane(tblOtherFiles));
	    
	    Box boxPagination = new Box(BoxLayout.LINE_AXIS);
	    boxPagination.add(btnFirstPage);
	    boxPagination.add(btnPrevPage);
	    boxPagination.add(Box.createHorizontalStrut(3));
	    boxPagination.add(cbxPages);
	    boxPagination.add(Box.createHorizontalStrut(3));
	    boxPagination.add(btnNextPage);
	    boxPagination.add(btnLastPage);
	    Box boxResultsCount = new Box(BoxLayout.LINE_AXIS);
	    boxResultsCount.add(lblResultsCount);
	    Box boxSearchControls = new Box(BoxLayout.LINE_AXIS);
	    boxSearchControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    boxSearchControls.add(boxResultsCount);
	    boxSearchControls.add( Box.createHorizontalStrut(10) );
	    boxSearchControls.add(btnAddInFavorits);
	    boxSearchControls.add(btnDellFromFavorits);
	    boxSearchControls.add(Box.createHorizontalGlue());
	    boxSearchControls.add(boxPagination);

	    JPanel pnlSearchResults = new JPanel();
	    pnlSearchResults.setLayout(new BoxLayout(pnlSearchResults, BoxLayout.PAGE_AXIS));
	    pnlSearchResults.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1000));
	    pnlSearchResults.add(new JScrollPane(tblSearchResults));
	    pnlSearchResults.add(boxSearchControls);

	    pnlFiles.setLayout(new BoxLayout(pnlFiles, BoxLayout.PAGE_AXIS));
	    pnlFiles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
	    Box labelBox = Box.createHorizontalBox();
	    labelBox.add(lblFilesCaption);
	    labelBox.add(Box.createHorizontalGlue());
	    pnlFiles.add(labelBox);
	    pnlFiles.add(tpnFiles);

	    pnlResultsTables.add(pnlSearchResults);
	    pnlResultsTables.add(Box.createVerticalStrut(4));
	    pnlResultsTables.add(new JSeparator());
	    pnlResultsTables.add(Box.createVerticalStrut(4));
	    pnlResultsTables.add(pnlFiles);
	    
	  }
	  
	  public JPanel getPnlResultsTables(){
		  return pnlResultsTables;
	  }
	  /** Create a text for label showing how much Dissertations was found. */
	  public void setResultsCount(int rc)
	  {
	    lblResultsCount.setText(
	      uiTextResources.getString("documentsPane.lblResultsCountPre") +
	      " " + rc + " " +
	      uiTextResources.getString("documentsPane.lblResultsCountPost")
	    );
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

	  public void setFilesPanelVisible(boolean visible)
	  {
	    pnlFiles.setVisible(visible);
	  }
	  
	  public void addCountEntrys(){
		  tpnFiles.setTitleAt(0, uiTextResources.getString("documentsPane.tpnFiles.ukrTab")
				  + " (" + ukrFilesTableModel.getRowCount() + ")"); 

		  tpnFiles.setTitleAt(1, uiTextResources.getString("documentsPane.tpnFiles.rusTab")
				  + " (" + rusFilesTableModel.getRowCount() + ")");
		  
		  tpnFiles.setTitleAt(2, uiTextResources.getString("documentsPane.tpnFiles.enTab")
				  + " (" + enFilesTableModel.getRowCount() + ")");
		  
		  tpnFiles.setTitleAt(3, uiTextResources.getString("documentsPane.tpnFiles.otTab")
				  + " (" + otherFilesTableModel.getRowCount() + ")");
	  }

	  public SearchResultsTableModel getSearchResultsTableModel()
	  {
	    return searchResultsTableModel;
	  }

	  public FilesTableModel getRusFilesTableModel()
	  {
	    return rusFilesTableModel;
	  }

	  public FilesTableModel getUkrFilesTableModel()
	  {
	    return ukrFilesTableModel;
	  }
	  
	  public FilesTableModel getEnFilesTableModel()
	  {
	    return enFilesTableModel;
	  }

	  public FilesTableModel getOtherFilesTableModel()
	  {
	    return otherFilesTableModel;
	  }
	  
	  public JTable getTblSearchResults()
	  {
	    return tblSearchResults;
	  }

	  public JTable getTblRusFiles()
	  {
	    return tblRusFiles;
	  }

	  public JTable getTblUkrFiles()
	  {
	    return tblUkrFiles;
	  }
	  
	  public JTable getTblEnFiles()
	  {
	    return tblEnFiles;
	  }

	  public JTable getTblOtherFiles()
	  {
	    return tblOtherFiles;
	  }
	  
	  public JButton getBtnFirstPage()
	  {
	    return btnFirstPage;
	  }

	  public JButton getBtnPrevPage()
	  {
	    return btnPrevPage;
	  }

	  public JButton getBtnNextPage()
	  {
	    return btnNextPage;
	  }

	  public JButton getBtnLastPage()
	  {
	    return btnLastPage;
	  }

	  public JComboBox getCbxPages()
	  {
	    return cbxPages;
	  }

	  public JButton getBtnAddInFavorits() {
		return btnAddInFavorits;
	}

	public JButton getBtnDellFromFavorits() {
		return btnDellFromFavorits;
	}

	/**
	   * hides columns which are not needed for the search of dissertations
	   */
	  public void removePublicationsFields(){
		  // if headers did not change
//		  if ( searchResultsTableModel.getColumnCount() == TOTAL_COLUMN_COUNT || 
//				  searchResultsTableModel.getColumnCount() == DISS_COLUMN_COUNT){
			  restoreFields();
			  searchResultsTableModel.turnOffColumn(3);
			  searchResultsTableModel.turnOffColumn(4);
			  searchResultsTableModel.turnOffColumn(5);
			  searchResultsTableModel.turnOffColumn(6);
			  setRendr();
			  GuiUtil.setColumnWidths(tblSearchResults, 	   20, 120, 100, 70, 60, 120, 80, 30, 30, 90, 90, 400);
			  GuiUtil.setColumnMaximumWidths(tblSearchResults, 20, 250, 200, 140, 120, 250, 160, 60, 60, 180, 180, 1000);
			  TABLE_WIDTH = 1320;
//		  }
	  }
	  
	  /**
	   * hides columns which are not needed for the search of publications
	   */
	  public void removeDissertationsFields(){
//		  if ( searchResultsTableModel.getColumnCount() == TOTAL_COLUMN_COUNT || 
//				  searchResultsTableModel.getColumnCount() == PUBL_COLUMN_COUNT){
			  restoreFields();
			  searchResultsTableModel.turnOffColumn(7);
			  searchResultsTableModel.turnOffColumn(8);
			  searchResultsTableModel.turnOffColumn(9);
			  searchResultsTableModel.turnOffColumn(12);
			  searchResultsTableModel.turnOffColumn(13);
			  searchResultsTableModel.turnOffColumn(14);
			  setRendr();
			  GuiUtil.setColumnWidths(tblSearchResults, 	   20, 200, 120, 60, 120, 50, 50, 90, 40, 200);
			  GuiUtil.setColumnMaximumWidths(tblSearchResults, 40, 600, 300, 60, 120, 50, 50, 90, 40, 1000);
			  TABLE_WIDTH = 960;
//		  }
	  }
	  
	  public void restoreFields(){
		searchResultsTableModel.resetColumns();
		setRendr();
		GuiUtil.setColumnWidths(tblSearchResults, 		 20, 200, 120, 60, 120, 50, 50, 100, 70, 180, 90, 40, 90, 100, 100, 200);
		GuiUtil.setColumnMaximumWidths(tblSearchResults, 30, 600, 300, 60, 120, 50, 50, 300, 70, 600, 90, 40, 90, 100, 150, 1000);
		TABLE_WIDTH = 1600;
	  }
	  
	  private void setRendr(){
		  Enumeration<TableColumn> e = tblSearchResults.getColumnModel().getColumns();   // set custom header renderer to all columns of search results table
		  while (e.hasMoreElements()) {
			  TableColumn tc = e.nextElement();
			  tc.setHeaderRenderer( new MultiLineHeaderRenderer() );
			  tc.setCellRenderer( new MultiLineCellRenderer() );
		  }
	  }

	  public class SearchResultsTableModel extends ObjectListTableModel<Entry>
	  {
	    public SearchResultsTableModel()
	    {
	      super(new String[] {
	        uiTextResources.getString("documentsPane.tblSearchResults.number"),
	        uiTextResources.getString("documentsPane.tblSearchResults.name"),
	        uiTextResources.getString("documentsPane.tblSearchResults.author"),
	        
	        //need remove for dissertation search
	        uiTextResources.getString("documentsPane.tblSearchResults.ISBN"),
	        uiTextResources.getString("documentsPane.tblSearchResults.Publisher"),
	        uiTextResources.getString("documentsPane.tblSearchResults.UDK"),
	        uiTextResources.getString("documentsPane.tblSearchResults.BBK"),
	        
	        //need remove for publication search
	        uiTextResources.getString("documentsPane.tblSearchResults.kind"),
	        uiTextResources.getString("documentsPane.tblSearchResults.code"),
	        uiTextResources.getString("documentsPane.tblSearchResults.organization"),
	        
	        uiTextResources.getString("documentsPane.tblSearchResults.city"),
	        uiTextResources.getString("documentsPane.tblSearchResults.year"),
	        
	        //need remove for publication search
	        uiTextResources.getString("documentsPane.tblSearchResults.pages"),
	        uiTextResources.getString("documentsPane.tblSearchResults.gasnti"),
	        uiTextResources.getString("documentsPane.tblSearchResults.udk"),
	        
	        uiTextResources.getString("documentsPane.tblSearchResults.description")
	      });
	    }

	    @Override public Object getObjectField(int fidx, Entry entry)
	    {
	    	if ( entry instanceof Dissertation ){
	    		Dissertation dissertation = (Dissertation) entry;
	    		switch (fidx)
	    		{
	    			case 0: return getNumerationStart() + getCurrentRow();
	    			case 1: return dissertation.getName();
			        case 2: return dissertation.getAuthor();
			        case 7: return dissertation.getKind();
			        case 8: return dissertation.getCode();
			        case 9: return dissertation.getOrganization();
			        case 10: return dissertation.getCity();
			        case 11: return dissertation.getYear();
			        case 12: return dissertation.getPages();
			        case 13: return dissertation.getDACNTI_code();
			        case 14: return dissertation.getUDK_idx();
			        case 15: return dissertation.getDescription();
	    		}
	    	}
	    	if ( entry instanceof Publication ){
	    		Publication publication = (Publication) entry;
	    		switch (fidx)
	    		{
	    			case 0: return getNumerationStart() + getCurrentRow();
	    			case 1: return publication.getName();
			        case 2: return publication.getAuthor();
			        case 3: return publication.getIsbn();
			        case 4: return publication.getPublisherName();
			        case 5: return publication.getUDK_idx();
			        case 6: return publication.getBBK_idx();
			        case 10: return publication.getCity();
			        case 11: return publication.getYear();
			        case 15: return publication.getDescription();
	    		}
	    	}
	    	return null;
	    }
	  }

	  public class FilesTableModel extends ObjectListTableModel<File>
	  {
	    public FilesTableModel()
	    {
	      super(new String[] {
	        uiTextResources.getString("documentsPane.tblFiles.fileName"),
	        uiTextResources.getString("documentsPane.tblFiles.author"),
	        uiTextResources.getString("documentsPane.tblFiles.pages"),
	      });
	    }

	    @Override public Object getObjectField(int fidx, File file)
	    {
	      switch (fidx)
	      {
	        case 0: return file.getName();
	        case 1: return file.getAuthor();
	        case 2: return file.getPages();
	      }
	      return null;
	    }
	  }
}
