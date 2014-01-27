package ua.com.znannya.client.ui.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.icepdf.core.SecurityCallback;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.views.DocumentViewController;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.util.PropertiesManager;
import org.jivesoftware.smack.znannya.dao.File;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ctrl.FileDownloadDialogController;
import ua.com.znannya.client.util.StringUtil;

public class PdfViewerComponent implements ActionListener {
	/**
	 * Store pdf viewer properties
	 */
	private PropertiesManager propertiesManager;
	/**
	 * Swing viwer component
	 */
	private JPanel viewerPanel;

	private JButton ordBtn;
	/**
	 * Controller to operate pdf document
	 */
	private SwingController controller;
	private int initWidth = 800;
	private int initHeight = 600;
	private static Logger logger;
	private File file;

	static {
		logger = Logger.getLogger(PdfViewerComponent.class.getName());
	}

	public PdfViewerComponent(File file) {
		this.file = file;
		init();
	}

	public PdfViewerComponent(int initWidth, int initHeight, File file ) {
		this(file);
		this.initWidth = initWidth;
		this.initHeight = initHeight;

	}

	private void init() {
		ordBtn = new JButton(ZnclApplication.getApplication()
				.getUiTextResources().getString("pdf.file.order"));
		ordBtn.addActionListener(this);
	}

	public static void main(String[] args) {
		PdfViewerComponent comp = new PdfViewerComponent(null);
		comp.loadPdf("D:/Disks/Igromania_08_2010/Igromania 08 2010.pdf");
		// comp.loadPdf(new TestDissertationService().getFileContent(null));
		// comp.getController().getDocumentViewController().getViewContainer().add(new
		// Button("Order"))
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(comp.getViewerPanel());
		frame.setSize(800, 600);
		frame.setVisible(true);

	}

	public void loadPdf(String filePath) {
		prepareController();
		controller.openDocument(filePath);
	}

	public void loadPdf(InputStream is) {
		prepareController();
		controller.openDocument(is, null, null);
	}
	
	public void loadPdf(InputStream is, String password) {
		prepareController();
		controller.openDocument(is, null, null, new PdfViewerSecurityCallback(password));
	}

	private void prepareController() {
		// Get a file from the command line to open
		// load message bundle
		ResourceBundle messageBundle = ResourceBundle
				.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);

		// initiate the properties manager.
		Properties sysProps = System.getProperties();
		sysProps.setProperty("swingri.home", "view");
		sysProps.setProperty("org.icepdf.core.paint.disableAlpha", "false");
		propertiesManager = new PropertiesManager(sysProps, null, messageBundle);

		// initiate font Cache manager, reads system font data and stores
		// summary
		// information in a properties file. If new font are added to the OS
		// then the properties file can be deleted to initiate a re-read of the
		// font data.
		// new FontPropertiesManager(propertiesManager, sysProps,
		// messageBundle);

		// input new System properties
		System.setProperties(sysProps);
		setProperties();

		// set look & feel
		setupLookAndFeel(messageBundle);

		controller = commonWindowCreation(messageBundle);
	}

	protected SwingController commonWindowCreation(ResourceBundle messageBundle) {
		SwingController ctrl = new SwingController(messageBundle);

		// add interactive mouse link annotation support
		ctrl.getDocumentViewController().setAnnotationCallback(
				new MyAnnotationCallback(ctrl.getDocumentViewController()));

		// guild a new swing viewer with remembered view settings.
		int viewType = DocumentViewControllerImpl.ONE_PAGE_VIEW;
		int pageFit = org.icepdf.core.views.DocumentViewController.PAGE_FIT_WINDOW_WIDTH;
		try {
			viewType = propertiesManager.getInt("document.viewtype",
					DocumentViewControllerImpl.ONE_PAGE_VIEW);
			pageFit = propertiesManager.getInt(
					PropertiesManager.PROPERTY_DEFAULT_PAGEFIT,
					DocumentViewController.PAGE_FIT_WINDOW_WIDTH);
		} catch (NumberFormatException e) {
			// eating error, as we can contue with out alarm
		}

		SwingViewBuilder factory = new SwingViewBuilder(ctrl,
				propertiesManager, null, false,
				SwingViewBuilder.TOOL_BAR_STYLE_FIXED, null, viewType, pageFit);

		viewerPanel = factory.buildViewerPanel();
			
		JPanel orderPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		orderPnl.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		orderPnl.add(ordBtn);
		
		JToolBar toolBar = (JToolBar) viewerPanel.getComponent(0);
		toolBar.setLayout(new BoxLayout(toolBar,BoxLayout.LINE_AXIS));			
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(orderPnl);
		setSizeComponent(toolBar);
//		int width = propertiesManager.getInt("application.width", initWidth);
//		int height = propertiesManager.getInt("application.height", initHeight);
		// setSize(width, height);

//		int x = propertiesManager.getInt("application.x", 1);
//		int y = propertiesManager.getInt("application.y", 1);

		// setLocation((int) (x), (int) (y));
		// setVisible(true);

		return ctrl;
	}

	private void setSizeComponent(Component component){
		if ( component instanceof JToolBar ){
			JToolBar toolBar = (JToolBar) component;
			for (int i = 0; i < toolBar.getComponentCount(); i++) {
				setSizeComponent( toolBar.getComponent(i) );
			}
		}
		if ( component instanceof JTextField ){
			JTextField textField  = (JTextField) component;
			textField.setPreferredSize(new Dimension(60,30));
			textField.setMaximumSize(textField.getPreferredSize());
			return;
		}
		if ( component instanceof JComboBox ){
			JComboBox comboBox = (JComboBox) component;
			comboBox.setPreferredSize(new Dimension(100,30));
			comboBox.setMaximumSize(comboBox.getPreferredSize());
			return;
		}
	}
	
	private void setProperties() {
		propertiesManager.setBoolean(StringUtil
				.decode("1@@<931D9?>YD??<21BYC8?GYED9<9DIYC1F5"), false);
		propertiesManager.setBoolean(StringUtil
				.decode("1@@<931D9?>YD??<21BYC8?GYED9<9DIY@B9>D"), false);
		propertiesManager.setBoolean("application.utilitypane.show.annotation", false);
		propertiesManager.setBoolean("application.utilitypane.show.bookmarks", false);
		propertiesManager.setBoolean("application.toolbar.show.utility.upane", false);
		propertiesManager.setBoolean("application.toolbar.show.utility", true);
		propertiesManager.setBoolean("application.toolbar.show.utility.search", true);
	
		propertiesManager.setBoolean("application.toolbar.show.tool", false);
		propertiesManager.setBoolean("application.toolbar.show.annotation", false);
	}

	/**
	 * If a L&F has been specifically set then try and use it. If not then
	 * resort to the 'native' system L&F.
	 * 
	 * @param messageBundle
	 */
	private void setupLookAndFeel(ResourceBundle messageBundle) {

		// Do Mac related-setup (if running on a Mac)
		/*
		 * if (Defs.sysProperty("mrj.version") != null) { // Running on a mac //
		 * take the menu bar off the jframe
		 * Defs.setSystemProperty("apple.laf.useScreenMenuBar", "true"); // set
		 * the name of the application menu item (must precede the L&F setup)
		 * String appName =
		 * messageBundle.getString("viewer.window.title.default");
		 * Defs.setSystemProperty
		 * ("com.apple.mrj.application.apple.menu.about.name", appName); }
		 */

		String className = propertiesManager.getLookAndFeel(
				"application.lookandfeel", null);

		if (className != null) {
			try {
				UIManager.setLookAndFeel(className);
				return;
			} catch (Exception e) {

				// setup a patterned message
				Object[] messageArguments = { propertiesManager
						.getString("application.lookandfeel") };
				MessageFormat formatter = new MessageFormat(messageBundle
						.getString("viewer.launcher.URLError.dialog.message"));

				// Error - unsupported L&F (probably windows)
				JOptionPane
						.showMessageDialog(
								null,
								formatter.format(messageArguments),
								messageBundle
										.getString("viewer.launcher.lookAndFeel.error.message"),
								JOptionPane.ERROR_MESSAGE);
			}
		}

		try {
			String defaultLF = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(defaultLF);
		} catch (Exception e) {
			logger.log(Level.FINE, "Error setting Swing Look and Feel.", e);
		}
	}

	public JPanel getViewerPanel() {
		return viewerPanel;
	}

	public SwingController getController() {
		return controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		FileDownloadDialogController fDwnCtrl = ZnclApplication
				.getApplication().getControllerManager()
				.getFileDownloadDialogController();
		ZnclApplication.getApplication().getControllerManager().getChoosePagesController().loadPages(file.getPages());
		fDwnCtrl.startOrderPages();

	}
	
	private class PdfViewerSecurityCallback implements SecurityCallback{
		private String password;
		
		PdfViewerSecurityCallback(String password) {
			this.password = password;
		}
		
		@Override
		public String requestPassword(Document arg0) {
			return password;
		}
		
	}

}
