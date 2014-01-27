package ua.com.znannya.client.ctrl;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;
import org.jivesoftware.smack.znannya.dao.Publication;
import org.jivesoftware.smack.znannya.favorite.FavoriteManager;
import org.jivesoftware.smack.znannya.pdf.DHClass;

import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.ComplexSearchCriteria;
import ua.com.znannya.client.service.DissComparator;
import ua.com.znannya.client.service.DissertationService;
import ua.com.znannya.client.service.PublicationService;
import static ua.com.znannya.client.service.DissertationService.PAGE_SIZE;
import ua.com.znannya.client.ui.ButtonTabComponent;
import ua.com.znannya.client.ui.DocumentsPane;
import ua.com.znannya.client.ui.LoadDialog;
import ua.com.znannya.client.ui.ResultsPane;
import ua.com.znannya.client.ui.widgets.MultiLineHeaderRenderer;
import ua.com.znannya.client.ui.widgets.PdfViewerComponent;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.StringUtil;

/**
 * Controller for pane with Dissertations search table and document display
 * tabs.
 */
public class DocumentsPaneController implements ActionListener {
	
	private static final int TOOLTIP_WIDTH						= 600;

	private DocumentsPane _documentsPane;
	private ResultsPane searchResultPanel;
	private ResultsPane favoritePanel;
	private DissertationService dissertationService;
	private PublicationService publicationService;
	private EntriesCollection entryCol;
	private int pages = 1, currentPage = 1, type;
	private String sortBy = null;
	private boolean ascending;
	private boolean ignoreCbxPageEvent; // a flag for not fetching when combo's
										// value was changed internally

	private SearchType lastSearchType;
	private String lastSimpleCriteria;
	private ComplexSearchCriteria lastComplexCriteria;
	private ResourceBundle uiTextResources;
	private Logger logger = Logger.getLogger(DocumentsPaneController.class
			.getName());

	public DocumentsPaneController() {
		dissertationService = ZnclApplication.getApplication()
				.getServiceManager().getDissertationService();
		publicationService = ZnclApplication.getApplication().getServiceManager().getPublicatiopnService();
		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		setupListeners();
		
		//show tooltip untill user do not move mouse from it
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	}

	private void setupListeners() {
		MouseListener tableClickListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) // double click on table row
				{
					Entry selectEntry = null;
					boolean isFavorite = false;
					if ( e.getSource() == getSearchResultPanel().getTblSearchResults() ||
							getDocumentsPane().getSelectedComponent() == getSearchResultPanel().getPnlResultsTables()) //needed to handle clicks in file panel
						selectEntry = getSearchResultPanel().getSearchResultsTableModel().
									getObject( getSearchResultPanel().getTblSearchResults().getSelectedRow() );
					else if(e.getSource() == getFavoritePanel().getTblSearchResults() ||
							getDocumentsPane().getSelectedComponent() == getFavoritePanel().getPnlResultsTables()){ //needed to handle clicks in file panel
						selectEntry = getFavoritePanel().getSearchResultsTableModel().
									getObject( getFavoritePanel().getTblSearchResults().getSelectedRow() );
						isFavorite = true;
					}

					if (e.getSource() == getSearchResultPanel().getTblSearchResults() ||
							e.getSource() == getFavoritePanel().getTblSearchResults() ) { // fill and show files
																						// pane
						getFileList(selectEntry, isFavorite);
						if ( e.getSource() == getSearchResultPanel().getTblSearchResults() )
							getSearchResultPanel().setFilesPanelVisible(true);
						else
							getFavoritePanel().setFilesPanelVisible(true);
					}
					
					EntryType entryType = null;
					if ( selectEntry != null )
						if ( selectEntry instanceof Dissertation )
							entryType = EntryType.diss;
						else
							entryType = EntryType.pub;
					downloadFileFromFilePanel(e.getSource(), getSearchResultPanel(), entryType);
					downloadFileFromFilePanel(e.getSource(), getFavoritePanel(), entryType);
				}
				/////////////////////////////////////////////////////////////////
				// click on table header for sorting
				if (e.getSource() == getSearchResultPanel().getTblSearchResults().getTableHeader() ||
						e.getSource() == getFavoritePanel().getTblSearchResults().getTableHeader() )
				{
					TableColumnModel colModel;
					if ( e.getSource() == getSearchResultPanel().getTblSearchResults().getTableHeader() )
						colModel = getSearchResultPanel().getTblSearchResults().getColumnModel();
					else
						colModel = getFavoritePanel().getTblSearchResults().getColumnModel();
					
					//index of the column whose header was clicked
					int vColIndex = colModel.getColumnIndexAtX(e.getX()); 
					if ( vColIndex < 1 )
						return; // ignore click on numeration field
					ascending = !ascending;
					
					int columnCount;
					if ( e.getSource() == getSearchResultPanel().getTblSearchResults().getTableHeader() )
						columnCount = getSearchResultPanel().getSearchResultsTableModel().getColumnCount();
					else
						columnCount = getFavoritePanel().getSearchResultsTableModel().getColumnCount();
					if ( columnCount == DocumentsPane.TOTAL_COLUMN_COUNT ){
						if ( !DocumentsPane.COL_NAMES[vColIndex - 1].equals(sortBy) )
							ascending = true;
						sortBy = DocumentsPane.COL_NAMES[vColIndex - 1];
					} else						
						if ( type == ComplexSearchCriteria.DISSERTATIONS ){
							// reset direction for new field
							if (!DissertationService.COL_NAMES[vColIndex - 1].equals(sortBy))
								ascending = true;
							//index corrected to correspond to sorting fields array 
							sortBy = DissertationService.COL_NAMES[vColIndex - 1]; 
						}														
						else {
							if (!PublicationService.COL_NAMES[vColIndex - 1].equals(sortBy))
								ascending = true;
							sortBy = PublicationService.COL_NAMES[vColIndex - 1]; 						
						}
					// iterate through column headers and show sorting arrow on
					// clicked header
					for (int colIdx = 0; colIdx < colModel.getColumnCount(); colIdx++) {
						MultiLineHeaderRenderer hr = (MultiLineHeaderRenderer) colModel
								.getColumn(colIdx).getHeaderRenderer();
						if (vColIndex == colIdx) {
							hr.showArrow(ascending);
						} else {
							hr.hideArrow();
						}
					}
					if ( e.getSource() == getSearchResultPanel().getTblSearchResults().getTableHeader() )
						getSearchResultPanel().getTblSearchResults().getTableHeader().repaint();
					else
						getFavoritePanel().getTblSearchResults().getTableHeader().repaint();
					currentPage = 1;
					
					if ( columnCount == DocumentsPane.TOTAL_COLUMN_COUNT ){
						Collections.sort(entryCol.getEntries(), new DissComparator(sortBy, ascending));
						showFavorite();
					} else
						fetchPage();
				}
			}
		};

		// invoke, when mouse cursor is over a time cell table
		MouseMotionAdapter cellToolTipListener = new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				JTable table;
				if ( e.getSource() == getSearchResultPanel().getTblSearchResults() )
					table = getSearchResultPanel().getTblSearchResults();
				else
					table = getFavoritePanel().getTblSearchResults();
				if (e.getSource() == table) {
                    // calculate a table cell, is over which the cursor
					int row = table.rowAtPoint(e.getPoint());
					int column = table.columnAtPoint(e.getPoint());
					
					Object value = null;
					if(row > -1 && column > -1){
						value = table.getModel().getValueAt(row, column);
					}

					if (value != null) {
						String str = value.toString();
						// get a font of cell
						Font font = table.getCellRenderer(row, column)
								.getTableCellRendererComponent(table, value,
										false, true, row, column).getFont();
						
						// calculate a width of cell's string
						FontMetrics metrics = table
								.getCellRenderer(row, column)
								.getTableCellRendererComponent(table, value,
										false, true, row, column)
								.getFontMetrics(font);

						
						// checking if the width of the string was longer than the width 
						// of the cell that shows the string in the hint
//				 		calculate a bounds of cell
						Rectangle cellRect = table.getCellRect(row, column, true);
				
						int stringWidth = metrics.stringWidth(str);
						if (stringWidth > cellRect.width) {
							int parts = stringWidth / TOOLTIP_WIDTH;
							if(stringWidth % TOOLTIP_WIDTH > 0) parts += 1;
							int partLength = str.length() / parts;
							//table.getToolTipLocation(e);							
							String s = StringUtil.splitLongString(str, parts, partLength);

							table.setToolTipText(StringUtil.convertTextToHTML(s));
						} else
							table.setToolTipText(null);
					} else
						table.setToolTipText(null);
				}
			}
		};

		getSearchResultPanel().getTblSearchResults().getTableHeader().addMouseListener( tableClickListener );
		getFavoritePanel().getTblSearchResults().getTableHeader().addMouseListener( tableClickListener );
		
		getSearchResultPanel().getTblSearchResults().addMouseListener( tableClickListener );
		getFavoritePanel().getTblSearchResults().addMouseListener( tableClickListener );
		
		getSearchResultPanel().getTblSearchResults().addMouseMotionListener( cellToolTipListener );
		getFavoritePanel().getTblSearchResults().addMouseMotionListener( cellToolTipListener );
		
		getSearchResultPanel().getTblRusFiles().addMouseListener(tableClickListener);
		getFavoritePanel().getTblRusFiles().addMouseListener(tableClickListener);
		
		getSearchResultPanel().getTblUkrFiles().addMouseListener(tableClickListener);
		getFavoritePanel().getTblUkrFiles().addMouseListener(tableClickListener);
		
		setActionListnerForResultsPane(getSearchResultPanel());
		setActionListnerForResultsPane(getFavoritePanel());

		getDocumentsPane().addAllTabsClosedListener(
				new DocumentsPane.IAllTabsClosedListener() {
					public void tabsClosed() {
						String availableBalance = ZnclApplication
								.getApplication().getServiceManager()
								.getTimeTrackService().stopTrack();
						ZnclApplication.getApplication().getControllerManager()
								.getMainFrameController().updateBalance(
										availableBalance);
					}
				});
	}
	
	private void setActionListnerForResultsPane(ResultsPane pane){
		pane.getBtnFirstPage().addActionListener(this);
		pane.getBtnLastPage().addActionListener(this);
		pane.getBtnPrevPage().addActionListener(this);
		pane.getBtnNextPage().addActionListener(this);
		pane.getBtnAddInFavorits().addActionListener(this);
		pane.getBtnDellFromFavorits().addActionListener(this);
		pane.getCbxPages().addActionListener(this);		
	}
	
	private void downloadFileFromFilePanel( Object src, ResultsPane pane, EntryType entryType){
		//download and show clicked file in Russian
		if (src == pane.getTblRusFiles() ){ 
			downloadFile(pane.getRusFilesTableModel()
					.getObject(pane.getTblRusFiles().getSelectedRow()), entryType);
		}
		//download and show clicked file in Ukrainian
		if (src == pane.getTblUkrFiles()) {
			downloadFile(pane.getUkrFilesTableModel()
					.getObject(pane.getTblUkrFiles().getSelectedRow()), entryType);
		}
		if (src == pane.getTblEnFiles()) {
			downloadFile(pane.getEnFilesTableModel()
					.getObject(pane.getTblEnFiles().getSelectedRow()), entryType);
		}
		if (src == pane.getTblOtherFiles()) {
			downloadFile(pane.getOtherFilesTableModel()
					.getObject(pane.getTblOtherFiles().getSelectedRow()), entryType);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == getSearchResultPanel().getBtnFirstPage() || src == getFavoritePanel().getBtnFirstPage()) {
			currentPage = 1;
			fetchPage();
		}
		if (src == getSearchResultPanel().getBtnPrevPage() || src == getFavoritePanel().getBtnPrevPage()) {
			if (currentPage > 1)
				currentPage--;
			fetchPage();
		}
		if (src == getSearchResultPanel().getBtnNextPage() || src == getFavoritePanel().getBtnNextPage()) {
			if (currentPage < pages)
				currentPage++;
			fetchPage();
		}
		if (src == getSearchResultPanel().getBtnLastPage() || src == getFavoritePanel().getBtnLastPage()) {
			currentPage = pages;
			fetchPage();
		}
		if (src == getSearchResultPanel().getCbxPages() || src == getFavoritePanel().getCbxPages()) {
			if (!ignoreCbxPageEvent	&& getSearchResultPanel().getCbxPages().getSelectedItem() != null) {
				currentPage = (Integer) getSearchResultPanel().getCbxPages().getSelectedItem();
				fetchPage();
			}
			if (!ignoreCbxPageEvent	&& getFavoritePanel().getCbxPages().getSelectedItem() != null) {
				currentPage = (Integer) getFavoritePanel().getCbxPages().getSelectedItem();
				fetchPage();
			}
		}
		
		//Add and dell from favorites
		if ( src == getSearchResultPanel().getBtnAddInFavorits() ){
			Entry selectedEntry = getSearchResultPanel().getSearchResultsTableModel().
							getObject( getSearchResultPanel().getTblSearchResults().getSelectedRow() );
			if ( selectedEntry == null )
				JOptionPane.showMessageDialog( getDocumentsPane(), uiTextResources.getString("documentsPane.msgNotSelectObject"), 
						uiTextResources.getString("documentsPane.addInFavorits"), JOptionPane.INFORMATION_MESSAGE);
			else{
				addInFavorits( selectedEntry );
				JOptionPane.showMessageDialog( getDocumentsPane(), uiTextResources.getString("documentsPane.msgSuccAddInFavorit"), 
						uiTextResources.getString("documentsPane.addInFavorits"), JOptionPane.INFORMATION_MESSAGE);
			}			
		}
		if ( src == getFavoritePanel().getBtnDellFromFavorits() ){
			Entry selectedEntry = getFavoritePanel().getSearchResultsTableModel().
					getObject( getFavoritePanel().getTblSearchResults().getSelectedRow() );
			if ( selectedEntry == null )
				JOptionPane.showMessageDialog( getDocumentsPane(), uiTextResources.getString("documentsPane.msgNotSelectObject"), 
						uiTextResources.getString("documentsPane.addInFavorits"), JOptionPane.INFORMATION_MESSAGE);
			else{
				dellFromFavorits( selectedEntry );
				JOptionPane.showMessageDialog( getDocumentsPane(), uiTextResources.getString("documentsPane.msgSuccDellFormFavorit"), 
						uiTextResources.getString("documentsPane.addInFavorits"), JOptionPane.INFORMATION_MESSAGE);
			}			
		}
	}
	
	private void dellFromFavorits(Entry selectedEntry){
		int id = (int) selectedEntry.getID();
		EntryType type = EntryType.diss;
		if ( selectedEntry instanceof Publication )
			type = EntryType.pub;
		FavoriteManager favoriteManager = new FavoriteManager( XmppConnector.getInstance().getConnection() );
		favoriteManager.removeFavorite(id, type);
		getFavorite();
	}
	
	private void addInFavorits(Entry selectedEntry) {
		int id = (int) selectedEntry.getID();
		EntryType type = EntryType.diss;
		if ( selectedEntry instanceof Publication )
			type = EntryType.pub;
		FavoriteManager favoriteManager = new FavoriteManager( XmppConnector.getInstance().getConnection() );
		favoriteManager.addFavorite(id, type);
	}

	/** Delegates File download process to FileDownloadDialogController. */
	private void downloadFile(File file, EntryType entryType) {	
		FileDownloadDialogController fddc = ZnclApplication.getApplication()
				.getControllerManager().getFileDownloadDialogController();
		fddc.startDownload(file, entryType);
	}

	/** Creates new tab and shows given File's content in it via ICEpdf viewer. */
	public void showFileContent(InputStream fileContent, File file, EntryType entryType) {
		if (fileContent != null) {
			if (!getDocumentsPane().isClosableTabExists()) {
				String availableBalance = ZnclApplication.getApplication()
						.getServiceManager().getTimeTrackService().startTrack(file.getFileID(), entryType);
				ZnclApplication.getApplication().getControllerManager()
						.getMainFrameController().updateBalance(
								availableBalance);
			}

			PdfViewerComponent pvc = new PdfViewerComponent(file);
			pvc.loadPdf(fileContent, Base64.encodeBytes(DHClass.writeLong(XmppConnector.getInstance().getConnection().getKey())));			
			getDocumentsPane().addTab(file.getName(), pvc.getViewerPanel(),	true);
			ZnclApplication.getApplication().getControllerManager().getMainFrameController().showDocumentsPane();
			FileDownloadDialogController.countOfOpenedFiles++;
		}
	}

	public synchronized void doSimpleSearch(String ss) {
		lastSearchType = SearchType.SIMPLE;
		lastSimpleCriteria = ss;
		currentPage = 1; // if doing new search - do it from first page
		findSimple(ss, 0, PAGE_SIZE, "", true, true);
	}
	

	private void findSimple(String ss, int startIndex, int featchSize,
			String sortBy, boolean ascending, boolean isCount) {
		final String criteria = ss;
		final int startIndex1 = startIndex;
		final int featchSize1 = featchSize;
		final String sortBy1 = sortBy;
		final boolean ascending1 = ascending;
		final boolean isCount1 = isCount;

		LoadDialog.showLoadImg();
		SwingWorker<EntriesCollection, Object> worker = new SwingWorker<EntriesCollection, Object>() {
			@Override
			protected EntriesCollection doInBackground() throws Exception {
				return dissertationService.findSimple(criteria, startIndex1,
						featchSize1, sortBy1, ascending1, isCount1);
			}

			@Override
			protected void done() {
				LoadDialog.hideLoadImg();
				try {
					entryCol = get();
					showSearchResults(entryCol);
				} catch (Exception e) {
					logger.log(Level.SEVERE, null, e);
				}
			}
		};
		worker.execute();
	}

	public synchronized void doComplexSearch(ComplexSearchCriteria criteria) {
		lastSearchType = SearchType.COMPLEX;
		lastComplexCriteria = criteria;
		currentPage = 1; // if doing new search - do it from first page
		findComplex(criteria, 0, PAGE_SIZE, "", true, true);
	}

	private void findComplex(ComplexSearchCriteria criteria, int startIndex,
			int featchSize, String sortBy, boolean ascending, boolean isCount) {
		
		final ComplexSearchCriteria criteria1 = criteria;
		final int startIndex1 = startIndex;
		final int featchSize1 = featchSize;
		final String sortBy1 = sortBy;
		final boolean ascending1 = ascending;
		final boolean isCount1 = isCount;
		type = criteria1.getType();

		LoadDialog.showLoadImg();
		ZnclApplication.getApplication().getMainFrame().getStatusBar().
							setMainStatusText( uiTextResources.getString("mainFrame.statusBar.search") );
		SwingWorker<EntriesCollection, Object> worker = new SwingWorker<EntriesCollection, Object>() {
			@Override
			protected EntriesCollection doInBackground() throws Exception {
				if ( criteria1.getType() == ComplexSearchCriteria.DISSERTATIONS )
					return dissertationService.findComplex(criteria1, startIndex1,
							featchSize1, sortBy1, ascending1, isCount1);
				else
					return publicationService.findComplex(criteria1, startIndex1, 
							featchSize1, sortBy1, ascending1, isCount1);
			}

			@Override
			protected void done() {
				LoadDialog.hideLoadImg();
				ZnclApplication.getApplication().getMainFrame().getStatusBar().
							setMainStatusText( uiTextResources.getString("mainFrame.statusBar.waitingForUser") );
				try {
					entryCol = get();
					showSearchResults(entryCol);
				} catch (Exception e) {
					logger.log(Level.SEVERE, null, e);
				}
			}
		};
		worker.execute();
	}

	private void showSearchResults(EntriesCollection diss) {
		int resultsCount = diss.getCount();
		if (resultsCount > -1) {
			getSearchResultPanel().setFilesPanelVisible(false);
			getSearchResultPanel().getBtnAddInFavorits().setVisible(true);
			getSearchResultPanel().getBtnDellFromFavorits().setVisible(false);
			pages = resultsCount / PAGE_SIZE;
			if (resultsCount % PAGE_SIZE > 0)
				pages++; // one more page for last few results that count less
							// then PAGE_SIZE
			getSearchResultPanel().setResultsCount(resultsCount);
			ignoreCbxPageEvent = true;
			getSearchResultPanel().getCbxPages().removeAllItems(); // clear and
																// refill pages
																// combo with
																// pages numbers
			for (int pnum = 1; pnum <= pages; pnum++) {
				getSearchResultPanel().getCbxPages().addItem(pnum);
			}
		}
		showFetchedList(diss.getEntries());
	}

	/**
	 * Fills table with a List of Dissertations fetched by parameters calculated
	 * on current page of table.
	 */
	private void showFetchedList(List<Entry> disList) {
		int startIndex = ((currentPage - 1) * PAGE_SIZE);
		// List<Dissertation> disList =
		// dissertationService.fetchFoundDissertations(startIndex, PAGE_SIZE,
		// sortBy, ascending);
		if ( !disList.isEmpty() ){
			if ( disList.get(0) instanceof Dissertation )
				getSearchResultPanel().removePublicationsFields();
			else
				getSearchResultPanel().removeDissertationsFields();
		}
	
		Component addedComponent = getSearchResultPanel().getPnlResultsTables();
		if ( getDocumentsPane().indexOfComponent(addedComponent) == -1 )
			getDocumentsPane().addTab(uiTextResources.getString("documentsPane.resultsTab"), addedComponent);
		if ( getDocumentsPane().getTabCount() == 1 )
			getDocumentsPane().setWhiteBackGround();
		int indx = getDocumentsPane().indexOfComponent( getSearchResultPanel().getPnlResultsTables() );
		getDocumentsPane().setTabComponentAt(indx, new ButtonTabComponent( getDocumentsPane() ));
		
		getDocumentsPane().setSelectedComponent(getSearchResultPanel().getPnlResultsTables());
		getSearchResultPanel().getSearchResultsTableModel().setObjectList(disList, startIndex);
		
//		setRowHeight(getSearchResultPanel().getTblSearchResults());
		
		ignoreCbxPageEvent = true;
		getSearchResultPanel().getCbxPages().setSelectedItem(currentPage);
		ignoreCbxPageEvent = false;
	}
	
	// Returns the preferred height of a row. // The result is equal to the
	// tallest cell in the row. 
	public int setRowHeight(JTable table) { // Get the current default height for all rows
		int height = table.getRowHeight(); // Determine highest cell in the row
		for(int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++)
		for (int column = 0; column < table.getColumnCount(); column++) {
			Object value = null;
			if(rowIndex > -1 && column > -1){
				value = table.getModel().getValueAt(rowIndex, column);
			}
			if (value != null) {
				String str = value.toString();
				// get a font of cell
				Font font = table.getCellRenderer(rowIndex, column)
						.getTableCellRendererComponent(table, value,
								false, true, rowIndex, column).getFont();
				// calculate a width of cell's string
				FontMetrics metrics = table
						.getCellRenderer(rowIndex, column)
						.getTableCellRendererComponent(table, value,
								false, true, rowIndex, column)
						.getFontMetrics(font);
	
				
				// checking if the width of the string was longer than the width 
				// of the cell that shows the string in the hint
	//	 		calculate a bounds of cell
				Rectangle cellRect = table.getCellRect(rowIndex, column, true);
		
				int stringWidth = metrics.stringWidth(str);
				if (stringWidth > cellRect.width) {
					int parts = stringWidth / cellRect.width;
					if(stringWidth % cellRect.width > 0) parts += 1;
					
					height =  metrics.getHeight() * parts;
					table.setRowHeight(rowIndex, height); 
				}
			}
			/*
			TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
			Component comp = table.prepareRenderer(renderer, rowIndex, c);
			int h = comp.getPreferredSize().height + 2 * margin;
			height = Math.max(height, h);*/
		}
		return height;
	}

	private void fetchPage() {
		int startIndex = ((currentPage - 1) * PAGE_SIZE);

		 if (entryCol.getCount() > -1 && entryCol.getCount()<PAGE_SIZE){
			 showFetchedList(fetchFoundDissertations(startIndex,PAGE_SIZE,sortBy, ascending,false));
		 }
		 else{
			 if (lastSearchType == SearchType.SIMPLE)
					findSimple(lastSimpleCriteria, startIndex, PAGE_SIZE, sortBy,
							ascending, false);
				else if (lastSearchType == SearchType.COMPLEX)
					findComplex(lastComplexCriteria, startIndex, PAGE_SIZE, sortBy,
							ascending, false);
		 }
		
	}

	 private  List<Entry> fetchFoundDissertations(int startIndex, int fetchSize, String orderField, boolean ascending, boolean isCount)
	  {
	    List<Entry> resultList = entryCol.getEntries();

	    if (orderField != null) {
	      Collections.sort(resultList, new DissComparator(orderField, ascending));
	    }

	    if (startIndex < 1) {
	      startIndex = 1;
	    }
	    int endIndex = startIndex + fetchSize - 1;
	    if (endIndex > resultList.size()) {
	      endIndex = resultList.size();
	    }
	    
	    Logger.getLogger(getClass().getName()).info("Fetching starting at " + startIndex);
	    return resultList.subList(startIndex - 1, endIndex);
	  }
	
	
	/** Gets Files list from service, filters on language and fills tables. */
	private void getFileList(Entry selectedEntry, boolean isFavorite) {
//		boolean isFavorite = false;
//		Entry selectedEntry = getSearchResultPanel().getSearchResultsTableModel().getObject(
//						getSearchResultPanel().getTblSearchResults().getSelectedRow());
//		if ( selectedEntry == null ){
//			selectedEntry = getFavoritePanel().getSearchResultsTableModel().getObject(
//					getFavoritePanel().getTblSearchResults().getSelectedRow());
//			isFavorite = true;
//		}
		
		List<File> files;
		if ( selectedEntry instanceof Dissertation )
			files = dissertationService.getFileList(selectedEntry);
		else
			files = publicationService.getFileList(selectedEntry);

		if ( !isFavorite )
			setFileList(getSearchResultPanel(), files);
		else 
			setFileList(getFavoritePanel(), files);
	}
	
	private void setFileList(ResultsPane pane, List<File> files){
		List<File> rusFiles = filterOnLanguage(files, "ru");
		List<File> ukrFiles = filterOnLanguage(files, "uk");
		ukrFiles.addAll(filterOnLanguage(files, "ua"));
		List<File> enFiles = filterOnLanguage(files, "en");
		List<File> otherFiles = filterOnLanguage(files, "zz");
		
		pane.getRusFilesTableModel().setObjectList(rusFiles);
		pane.getUkrFilesTableModel().setObjectList(ukrFiles);
		pane.getEnFilesTableModel().setObjectCollection(enFiles);
		pane.getOtherFilesTableModel().setObjectCollection(otherFiles);
		pane.addCountEntrys();
	}

	/** Creates new List of Files selected on given language. */
	private List<File> filterOnLanguage(List<File> files, String lang) {
		List<File> result = new ArrayList<File>();
		for (File f : files) {
			if (f.getLanguage().equalsIgnoreCase(lang)) {
				result.add(f);
			}
		}
		return result;
	}

	public DocumentsPane getDocumentsPane() {
		if (_documentsPane == null) {
			_documentsPane = new DocumentsPane();
		}
		return _documentsPane;
	}

	public ResultsPane getSearchResultPanel() {
		if ( searchResultPanel == null ){
			searchResultPanel = new ResultsPane();
		}
		return searchResultPanel;
	}

	public ResultsPane getFavoritePanel() {
		if ( favoritePanel == null ){
			favoritePanel = new ResultsPane();
		}
		return favoritePanel;
	}

	private enum SearchType {
		SIMPLE, COMPLEX
	}

	public void getFavorite() {
		ZnclApplication.getApplication().getMainFrame().getStatusBar().
				setMainStatusText( uiTextResources.getString("mainFrame.statusBar.getFavorites") );
		LoadDialog.showLoadImg();
		SwingWorker<EntriesCollection, Void> worker = new SwingWorker<EntriesCollection, Void>(){

			@Override
			protected EntriesCollection doInBackground() throws Exception {
				FavoriteManager favoriteManager = new FavoriteManager( XmppConnector.getInstance().getConnection() );
				return favoriteManager.getFavorites();
			}
			
			@Override
			protected void done(){
				LoadDialog.hideLoadImg();
				ZnclApplication.getApplication().getMainFrame().getStatusBar().
						setMainStatusText( uiTextResources.getString("mainFrame.statusBar.waitingForUser") );
				try {
					entryCol = get();
					getFavoritePanel().restoreFields();
					showFavorite();
//					throw new NullPointerException("AAABBBCCC");
				} catch (Exception e) {
					if(e instanceof XMPPException)
						ErrorUtil.showError((XMPPException)e);
					logger.log(Level.SEVERE, null, e);
					JOptionPane.showMessageDialog(ZnclApplication.getApplication().getMainFrame(), uiTextResources.getString("documentsPane.msgErrGetFavorit"),
							uiTextResources.getString("error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		worker.execute();
	}
	
	private void showFavorite(){
		Component addedComponent = getFavoritePanel().getPnlResultsTables();
		if ( getDocumentsPane().indexOfComponent(addedComponent) == -1 )
			getDocumentsPane().addTab(uiTextResources.getString("mainFrame.menu.favorits"), addedComponent);
		if ( getDocumentsPane().getTabCount() == 1 )
			getDocumentsPane().setWhiteBackGround();
		int indx = getDocumentsPane().indexOfComponent( getFavoritePanel().getPnlResultsTables() );
		getDocumentsPane().setTabComponentAt(indx, new ButtonTabComponent( getDocumentsPane() ));
		getFavoritePanel().getSearchResultsTableModel().setObjectList( entryCol.getEntries() );
		getFavoritePanel().setResultsCount( entryCol.getEntries().size() );		
		getFavoritePanel().setFilesPanelVisible(false);
		getFavoritePanel().getBtnAddInFavorits().setVisible(false);
		getFavoritePanel().getBtnDellFromFavorits().setVisible(true);
		getFavoritePanel().getCbxPages().setVisible(false);
		getFavoritePanel().getBtnFirstPage().setVisible(false);
		getFavoritePanel().getBtnLastPage().setVisible(false);
		getFavoritePanel().getBtnNextPage().setVisible(false);
		getFavoritePanel().getBtnPrevPage().setVisible(false);
		getDocumentsPane().setSelectedComponent(getFavoritePanel().getPnlResultsTables());
	}
}
