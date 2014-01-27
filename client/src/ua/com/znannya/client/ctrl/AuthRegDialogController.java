package ua.com.znannya.client.ctrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ZnConfiguration;
import org.jivesoftware.smack.znannya.track.TimeTrackManager;

import ua.com.znannya.client.app.EurekaServer;
import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.service.AuthRegService;
import ua.com.znannya.client.service.UserData;
import ua.com.znannya.client.service.XmppTimeTrackService;
import ua.com.znannya.client.ui.AgreementDialog;
import ua.com.znannya.client.ui.AuthorizationDialog;
import ua.com.znannya.client.ui.LoadDialog;
import ua.com.znannya.client.ui.MainFrame;
import ua.com.znannya.client.ui.NewVersionavailableDialog;
import ua.com.znannya.client.ui.RegistrationDialog;
import ua.com.znannya.client.ui.widgets.PdfViewerComponent;
import ua.com.znannya.client.util.GuiUtil;

/**
 * Controller of authentification and registration dialog forms.
 */
public class AuthRegDialogController implements ActionListener, ChangeListener {
	private AuthorizationDialog _authorizationDialog;
	private AgreementDialog _agreementDialog;
	private RegistrationDialog _registrationDialog;
	private MainFrame mainFrame;
	private ResourceBundle uiTextResources;
	private AuthRegService authRegService;
	private WindowHandler windowHandler = new WindowHandler();
	private String login;

	public AuthRegDialogController() {
		mainFrame = ZnclApplication.getApplication().getMainFrame();
		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		authRegService = ZnclApplication.getApplication().getServiceManager()
				.getAuthRegService();

	}

	/** Stop authorization process and exit application. */
	private void cancelAuthorization() {
		getAuthorizationDialog().setVisible(false);
		ZnclApplication.getApplication().askToClose();
	}

	/** Shows authorization dialog and begins authorization process. */
	public void beginAuthorization() {
		LoadDialog.hideLoadImg();
		getAuthorizationDialog().setVisible(true);
	}

	private void completeAuthorization() {

		String login = getAuthorizationDialog().getTfldLogin().getText();
		String password = getAuthorizationDialog().getTfldPassword().getText();
		getAuthorizationDialog().setVisible(false);

		LoadDialog.showLoadImg();
		ZnclApplication.getApplication().getMainFrame().getStatusBar().
					setMainStatusText( uiTextResources.getString("mainFrame.statusBar.gettingData") );
		AuthThread authThread = new AuthThread();
		authThread.setLoginAndPassword(login,password);
		authThread.execute();

		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	private void beginRegistration() {
		getRegistrationDialog().setVisible(true);
	}

	private boolean completeRegistration() {
		// check mandatory fields
		if (GuiUtil.checkEmpty(getRegistrationDialog().getTfldEmail(),
				getRegistrationDialog().getTfldLogin(), getRegistrationDialog()
						.getTfldPassword())) {
			JOptionPane.showMessageDialog(mainFrame, uiTextResources
					.getString("registrationDialog.msgMandatoryFieldEmpty"));
			return false;
		}
		// check password match
		String pw = getRegistrationDialog().getTfldPassword().getText();
		String pwAgain = getRegistrationDialog().getTfldPasswordAgain()
				.getText();
		if (!pw.equals(pwAgain)) {
			JOptionPane.showMessageDialog(mainFrame, uiTextResources
					.getString("registrationDialog.msgPasswordMismatch"));
			return false;
		}
		String email = getRegistrationDialog().getTfldEmail().getText();
		String pattern = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
		if (!email.matches(pattern)) {
			JOptionPane.showMessageDialog(mainFrame, uiTextResources
					.getString("registrationDialog.msgEmailIncorrect"));
			return false;
		}
		getRegistrationDialog().setVisible(false);
		UserData ud = new UserData();
		ud.setFirstName(getRegistrationDialog().getTfldFirstName().getText());
		ud.setEmail(getRegistrationDialog().getTfldEmail().getText());
		ud.setLastName(getRegistrationDialog().getTfldLastName().getText());
		ud.setLogin(getRegistrationDialog().getTfldLogin().getText());
		ud.setOrganization(getRegistrationDialog().getTfldOrganization()
				.getText());
		ud.setJobTitle(getRegistrationDialog().getTfldJobTitle().getText());
		ud.setPassword(pw);
		ud.setPhone(getRegistrationDialog().getTfldPhone().getText());
		authRegService.register(ud);
		_registrationDialog = null; //clear all filled fields, so next user will not see them
		return true;
	}

	/** Listener method for components' action events. */
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		// Authorizaton dialog's buttons events.
		if (src == getAuthorizationDialog().getBtnCancel()) {
			cancelAuthorization();
		}
		if (src == getAuthorizationDialog().getBtnRegister()) {
			getAuthorizationDialog().setVisible(false);
			getAgreementDialog().setVisible(true);
		}
		if (src == getAuthorizationDialog().getBtnNext()
				|| src == getAuthorizationDialog().getTfldPassword()
				|| src == getAuthorizationDialog().getTfldLogin()) {
			if ( !getAuthorizationDialog().getTfldPassword().getText().isEmpty() && 
					!getAuthorizationDialog().getTfldLogin().getText().isEmpty() )
				completeAuthorization(); // close dialog and let proceed to verify
										// entered credentials
		}
		if ( src == getAuthorizationDialog().getCmbxChooseLanguage() ){
			if ( getAuthorizationDialog().getCmbxChooseLanguage().getSelectedIndex() == 0 ){
				ZnclApplication.getApplication().setUiTextResources(new Locale("uk") );
				Locale.setDefault( new Locale("uk") );
			}
			else if ( getAuthorizationDialog().getCmbxChooseLanguage().getSelectedIndex() == 1 ){
				ZnclApplication.getApplication().setUiTextResources(new Locale("ru") );
				Locale.setDefault( new Locale("ru") );
			}else if ( getAuthorizationDialog().getCmbxChooseLanguage().getSelectedIndex() == 2 ){
				ZnclApplication.getApplication().setUiTextResources(new Locale("en") );
				Locale.setDefault( new Locale("en") );
			}
			uiTextResources = ZnclApplication.getApplication().getUiTextResources();
			ZnclApplication.getApplication().getMainFrame().updateComponent();
			getAgreementDialog().updateComponent();
		}

		// Agreement dialog's buttons events.
		if (src == getAgreementDialog().getBtnCancel()) {
			getAgreementDialog().setVisible(false);
			getAuthorizationDialog().setVisible(true);
		}
		if (src == getAgreementDialog().getBtnNext()) {
			getAgreementDialog().setVisible(false);
			beginRegistration();
		}

		// Registration dialog's buttons events.
		if (src == getRegistrationDialog().getBtnCancel()) {
			getRegistrationDialog().setVisible(false);
			getAuthorizationDialog().setVisible(true);
		}
		if (src == getRegistrationDialog().getBtnComplete()) {
			if (completeRegistration()) {
				beginAuthorization();
			}
		}
	}

	/** Listener method for components' change events. */
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();
		if (src == getAgreementDialog().getRbtnAccept()) {
			JRadioButton rbtnAccept = getAgreementDialog().getRbtnAccept();
			if (rbtnAccept.isSelected()) {
				getAgreementDialog().getBtnNext().setEnabled(true);
			}
		}

		if (src == getAgreementDialog().getRbtnReject()) {
			JRadioButton rbtnReject = getAgreementDialog().getRbtnReject();
			if (rbtnReject.isSelected()) {
				getAgreementDialog().getBtnNext().setEnabled(false);
			}
		}
	}

	/** Class for handling dialogs' window events. */
	class WindowHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			cancelAuthorization();
		}
	}

	public AuthorizationDialog getAuthorizationDialog() {
		ZnclApplication.getApplication().getMainFrame().getStatusBar().
				setMainStatusText( uiTextResources.getString("mainFrame.statusBar.waitingForUser") );
		if (_authorizationDialog == null) {
			_authorizationDialog = new AuthorizationDialog(mainFrame);
			_authorizationDialog.getBtnRegister().addActionListener(this);
			_authorizationDialog.getBtnCancel().addActionListener(this);
			_authorizationDialog.getBtnNext().addActionListener(this);
			_authorizationDialog.getTfldLogin().addActionListener(this);
			_authorizationDialog.getTfldPassword().addActionListener(this);
			_authorizationDialog.getCmbxChooseLanguage().addActionListener(this);
			_authorizationDialog.addWindowListener(windowHandler);
		}
		return _authorizationDialog;
	}

	public AgreementDialog getAgreementDialog() {
		if (_agreementDialog == null) {
			_agreementDialog = new AgreementDialog(mainFrame);
			_agreementDialog.getBtnCancel().addActionListener(this);
			_agreementDialog.getBtnNext().addActionListener(this);
			_agreementDialog.getRbtnAccept().addChangeListener(this);
			_agreementDialog.getRbtnReject().addChangeListener(this);
		}
		return _agreementDialog;
	}

	public RegistrationDialog getRegistrationDialog() {
		if (_registrationDialog == null) {
			_registrationDialog = new RegistrationDialog(mainFrame);
			_registrationDialog.getBtnCancel().addActionListener(this);
			_registrationDialog.getBtnComplete().addActionListener(this);
		}
		return _registrationDialog;
	}

	public String getLogin(){
		return login;
	}
	
	private void setLogin(String login){
		this.login = login;
	}
	
	private class AuthThread extends SwingWorker<XMPPException, Void> {

		private String login = null;
		private String password = null;
		private boolean authSuccess = false;

		public AuthThread() {
		}

		public void setLoginAndPassword(String login,String password) {
			this.login = login.toLowerCase();
			this.password = password;
		}
		

		@Override
		protected XMPPException doInBackground() throws Exception {
			return authRegService.authenticate(login, password);
		}

		@Override
		protected void done() {

			authSuccess = false;
			XMPPException error = null;
			try {
				error = get();
				authSuccess = error == null;
				ZnclApplication.getApplication().getMainFrame().getStatusBar().
									setMainStatusText( uiTextResources.getString("mainFrame.statusBar.waitingForUser") );
				LoadDialog.hideLoadImg();
				String msgTitle = uiTextResources
						.getString("authorizationDialog.formTitle");
				if (!authSuccess) {
					String errorMsg = null;
					try {
						errorMsg = uiTextResources.getString("login.error."+error.getXMPPError().getCondition());
					} catch (Exception e) {
						errorMsg = uiTextResources.getString("authorizationDialog.msgIncorrectLogin");
					}
					JOptionPane
							.showMessageDialog(
									mainFrame,
									errorMsg,
									msgTitle, JOptionPane.INFORMATION_MESSAGE);
					beginAuthorization();
				} else {
					_authorizationDialog = null; //clear all filled fields, so next user will not see them
					if (ZnclApplication.getApplication().getServiceManager()
							.getDictionaryService().getYears().isEmpty())
						ZnclApplication.getApplication().getControllerManager()
								.getComplexSearchDialogController().fillLists();

					// Doing this after znConfigs retrieval
					String availableBalance = ZnclApplication.getApplication()
							.getServiceManager().getBalanceService()
							.getAvailableBalance();
					ZnclApplication.getApplication().getControllerManager()
							.getMainFrameController().updateBalance(
									availableBalance);
					
					String minClientVersion = ZnConfiguration.get( ZnConfiguration.MIN_CLIENT_VERSION );
					String curClientVersion = ZnConfiguration.CURRENT_CLIENT_VERSION;
					if ( !minClientVersion.equals(curClientVersion) )
						new NewVersionavailableDialog(ZnclApplication.getApplication().getMainFrame())
											.setVisible(true);
					
					String title = uiTextResources.getString("mainFrame.windowTitle") + " - " + login;
					String user = uiTextResources
							.getString("authorizationDialog.msgUser");
					String autStr = uiTextResources
							.getString("authorizationDialog.msgSuccessReg");
					String msg = user + " " + login + " " + autStr;
					JOptionPane.showMessageDialog(mainFrame, msg, msgTitle,
							JOptionPane.INFORMATION_MESSAGE);
					ZnclApplication.getApplication().getControllerManager()
							.getAdviceDayDialogController().startShowDialog();
					ZnclApplication.getApplication().getControllerManager()
							.getMainFrameController().getMainFrame().setTitle(title);

					setLogin(this.login);
					
					// If any arg are passed to app main method, then show dialog to download filesfor view 
				    EurekaServer.handleClient(ZnclApplication.getApplication().getArgs());
				    // Register track listeners 
					TimeTrackManager timeTrackManager = new TimeTrackManager(XmppConnector.getInstance().getConnection());
					timeTrackManager.registerUpdatePacketListener();
					TimeTrackManager.removeAllListeners();
					TimeTrackManager.addListener((new XmppTimeTrackService()).new TrackUpdatesImpl());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
