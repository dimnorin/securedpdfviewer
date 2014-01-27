package ua.com.znannya.client.ctrl;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.znannya.StatsCollection;
import org.jivesoftware.smack.packet.znannya.UserInfo;
import org.jivesoftware.smack.znannya.UserInfoManager;
import org.jivesoftware.smack.znannya.dao.BriefStatistics;
import org.jivesoftware.smack.znannya.stats.StatisticsManager;

import ua.com.znannya.client.app.ControllerManager;
import ua.com.znannya.client.app.XmppConnector;
import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.CalendarDialog;
import ua.com.znannya.client.ui.DetailStatisticDialog;
import ua.com.znannya.client.ui.LoadDialog;
import ua.com.znannya.client.ui.PrivateRoomDialog;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.StringUtil;

public class PrivateRoomDialogController implements ActionListener{
	private static final int TOOLTIP_WIDTH	= 600;
	
	private PrivateRoomDialog privateRoomDialog;
	private CalendarDialog calendarWithDialog;
	private CalendarDialog calendarOnDialog;
	private DetailStatisticDialog detailStatisticDialog;
	
	private ControllerManager controllerManager;
	
	private UserInfo userInfo;
	private ResourceBundle uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	private Logger logger = Logger.getLogger(DocumentsPaneController.class
			.getName());
	private MouseMotionAdapter mouseAdapter;
	
	public PrivateRoomDialogController( ControllerManager cm ){
		controllerManager = cm;
	}
	
	public PrivateRoomDialog getDialog(){
		if ( privateRoomDialog == null ){
			privateRoomDialog = new PrivateRoomDialog( ZnclApplication.getApplication().getMainFrame() );
			setListners();
			UserInfoManager userInfoManager = new UserInfoManager( XmppConnector.getInstance().getConnection() );
			try {
				userInfo = userInfoManager.getUserInfo();
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
			setUserInfoInDialog( userInfo );
		}
		return privateRoomDialog;
	}

	public CalendarDialog getCalendarWithDialog() {
		if ( calendarWithDialog == null ){
			calendarWithDialog = new CalendarDialog(getDialog(), uiTextResources.getLocale());
		}
		return calendarWithDialog;
	}

	public CalendarDialog getCalendarOnDialog() {
		if ( calendarOnDialog == null ){
			calendarOnDialog = new CalendarDialog(getDialog(), uiTextResources.getLocale());
		}		
		return calendarOnDialog;
	}
	
	public DetailStatisticDialog getDetailStatisticDialog(){
		if ( detailStatisticDialog == null )
			detailStatisticDialog = new DetailStatisticDialog( getDialog() );
		return detailStatisticDialog;
	}

	public void showDialog(){
		getDialog().setVisible(true);
	}
	
	public void setListners(){
		getDialog().getBtnIncrease().addActionListener(this);
		getDialog().getBtnSaveChng().addActionListener(this);
		getDialog().getBtnFromDate().addActionListener(this);
		getDialog().getBtnToDate().addActionListener(this);
		getCalendarOnDialog().getBtnOk().addActionListener(this);
		getCalendarWithDialog().getBtnOk().addActionListener(this);
		getDialog().getBtnShowStat().addActionListener(this);
		getDialog().getBtnDetailStats().addActionListener(this);
		getDetailStatisticDialog().getTblDetailStats().addMouseMotionListener(getMouseMotionAdapter());
	}
	
	private boolean dataFieldIsEmpty(){
		if ( getDialog().getTfldFromDate().getText().isEmpty() || 
				getDialog().getTfldToDate().getText().isEmpty() )
			return true;
		return false;
			
	}
	
	private MouseMotionAdapter getMouseMotionAdapter(){
		if ( mouseAdapter == null ){
			mouseAdapter = new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					JTable table = getDetailStatisticDialog().getTblDetailStats();
					if ( e.getSource() == table ){
	                    // calculate a table cell, is over which the cursor
						int row = table.rowAtPoint(e.getPoint());
						int column = table.columnAtPoint(e.getPoint());
						Object value = table.getModel().getValueAt(row, column);
	                    // calculate a bounds of cell
						Rectangle cellRect = table.getCellRect(row, column, true);

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
		}
		return mouseAdapter;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		//////////////////////////////////////////////////////////////
		//Increase balance
		if ( source == privateRoomDialog.getBtnIncrease() ){
			if ( privateRoomDialog.getRBtnSMS().isSelected() )
				 controllerManager.getRefundAccountDialogController().showDialog();
			else
				if ( privateRoomDialog.getRBtnVisa().isSelected() ){
			    	String url = String.format(uiTextResources.getString("mainFrame.menu.privateRoom.accountRefund.card.url"), 
			    				XmppConnector.getInstance().getServerHost(), controllerManager.getAuthRegDialogController().getLogin(), uiTextResources.getLocale().getLanguage());
			    	try{
			    		Desktop.getDesktop().browse(new URI(url));
			    	}catch (Exception e1) {
			    		JOptionPane.showMessageDialog( privateRoomDialog, String.format(ZnclApplication.getApplication().getUiTextResources()
			    				.getString("accountRefund.card.cantopen.url"), url));
			    	}
				}
			return;
		}

		//////////////////////////////////////////////////////////////
		//Change user info
		if ( source == privateRoomDialog.getBtnSaveChng() ){
			String login = controllerManager.getAuthRegDialogController().getLogin();
			String name = privateRoomDialog.getTfldName().getText().trim() + " "
								+ privateRoomDialog.getTfldFamily().getText().trim();
			String eMail = privateRoomDialog.getTfldEMail().getText();
			String organization = privateRoomDialog.getTfldOrganization().getText();
			String phone = privateRoomDialog.getTfldPhone().getText();
			String position = privateRoomDialog.getTfldPosition().getText();
			String extraInfo = privateRoomDialog.getExtraInfo().getText();
			String oldPass = new String( privateRoomDialog.getPfldOldPass().getPassword() );
			String newPass = new String( privateRoomDialog.getPfldNewPass().getPassword() );
			String repPass = new String( privateRoomDialog.getPfldRepPass().getPassword() );

			if ( oldPass.isEmpty() && newPass.isEmpty() && repPass.isEmpty() )
				newPass = repPass = oldPass = null;
			
			changeUserInfo(login, oldPass, newPass, repPass, name, organization, position, phone, eMail, extraInfo);
			return;
		}
		
		//////////////////////////////////////////////////////////////
		//Show calendar
		if ( source == privateRoomDialog.getBtnFromDate() ){
			getCalendarWithDialog().getCalendar().setMaxSelectableDate( new Date() );
			getCalendarWithDialog().setVisible(true);
			return;
		}
		if ( source == privateRoomDialog.getBtnToDate() ){
			getCalendarOnDialog().getCalendar().setMaxSelectableDate( new Date() );
			getCalendarOnDialog().setVisible(true);
			return;
		}
		
		//////////////////////////////////////////////////////////////
		//Set date in text field
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		if ( source == getCalendarWithDialog().getBtnOk() ){
			Date date = getCalendarWithDialog().getCalendar().getDate();
			getCalendarOnDialog().getCalendar().setMinSelectableDate(date);
			Date toDay = new Date();
			if ( date.getTime() > toDay.getTime() )
				date = toDay;
			if ( !getDialog().getTfldToDate().getText().isEmpty() )
				if ( date.getTime() > getCalendarOnDialog().getCalendar().getDate().getTime() )
					date = getCalendarOnDialog().getCalendar().getDate();
			getDialog().getTfldFromDate().setText( formatter.format( date ) );
			getCalendarWithDialog().setVisible(false);
			setDateInToField(date, formatter);
		}
		if ( source == getCalendarOnDialog().getBtnOk() ){
			Date setDate = getCalendarOnDialog().getCalendar().getDate();
			getCalendarWithDialog().getCalendar().setMaxSelectableDate(setDate);
			getDialog().getTfldToDate().setText( formatter.format( setDate ) );
			getCalendarOnDialog().setVisible(false);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		long from = 0, to = 0;
		try{
			from = sdf.parse( getDialog().getTfldFromDate().getText() ).getTime();
			to = sdf.parse( getDialog().getTfldToDate().getText() ).getTime();
		} catch (Exception e1){
			
		}
		
		//////////////////////////////////////////////////////////////
		//show statTable
		if ( source == getDialog().getBtnShowStat() ){
			if ( dataFieldIsEmpty() ){
				JOptionPane.showMessageDialog(getDialog(), uiTextResources.getString("privateRoomDialog.msgEmtyDate"));
				return;
			}
			try {
				StatisticsManager statsManager = new StatisticsManager(XmppConnector.getInstance().getConnection());
				BriefStatistics statistics = statsManager.getBriefStatistics(from, to);
				getDialog().getTblStat().setVisible(true);
				getDialog().setDataInTable(statistics);
			} catch (XMPPException e1) {
				ErrorUtil.showError(e1);
			}
		}
		
		//////////////////////////////////////////////////////////////
		//show detail stats
		if ( source == getDialog().getBtnDetailStats() ){
			if ( dataFieldIsEmpty() ){
				JOptionPane.showMessageDialog(getDialog(), uiTextResources.getString("privateRoomDialog.msgEmtyDate"));
				return;
			}
			try {
				StatisticsManager statsManager = new StatisticsManager(XmppConnector.getInstance().getConnection());
				StatsCollection	statsCollection = statsManager.getFullStatistics(from, to);
				getDetailStatisticDialog().setDataInTable(statsCollection);
				getDetailStatisticDialog().setVisible(true);
				getDetailStatisticDialog().setTitle(uiTextResources.getString("privateRoomDialog.tblDetailStat.Header") + " " +
						getDialog().getTfldFromDate().getText() + " - " + getDialog().getTfldToDate().getText() );
			} catch (XMPPException e1) {
				ErrorUtil.showError(e1);
			}
		}
	}
	
	private void setDateInToField(Date from, Format formatter){
		Calendar cal = new GregorianCalendar();
		cal.setTime(from);
		cal.add(Calendar.MONTH, 1);
		from = cal.getTime();
		Date toDay = new Date();
		if ( from.getTime() > toDay.getTime() )
			from = toDay;
		if ( getDialog().getTfldToDate().getText().isEmpty() )
			getDialog().getTfldToDate().setText( formatter.format( from ));
	}

	private void changeUserInfo(final String login, final String oldPass, final String newPass, String repPass,  final String name, final String organization, final String position, final String phone, final String eMail, final String extraInfo){
		if ( newPass != null && repPass != null )
			if ( !newPass.equals(repPass) ){
				ErrorUtil.showError(uiTextResources.getString("privateRoomDialog.msgWrongRepPass"));
				return;
			}
		
		LoadDialog.showLoadImg();
		ZnclApplication.getApplication().getMainFrame().getStatusBar().
				setMainStatusText( uiTextResources.getString("mainFrame.statusBar.changeUserData") );
		SwingWorker<UserInfo, Void> worker = new SwingWorker<UserInfo, Void>(){
			@Override
			protected UserInfo doInBackground()  {
				try {
					UserInfoManager userInfoManager = new UserInfoManager(XmppConnector.getInstance().getConnection());
					return userInfoManager.setUserInfo(login, oldPass, newPass, name, organization, position, phone, eMail, extraInfo);
				} catch (XMPPException e) {
					ErrorUtil.showError(e);
					return null;
				}
			}
			@Override
			protected void done(){
				try {
					UserInfo userInfo = get();
					if ( userInfo != null )
						JOptionPane.showMessageDialog(privateRoomDialog, uiTextResources.getString("privateRoomDialog.msgSuccessChng"), 
							uiTextResources.getString("privateRoomDialog.formTitle"), JOptionPane.INFORMATION_MESSAGE);
					LoadDialog.hideLoadImg();
					ZnclApplication.getApplication().getMainFrame().getStatusBar().
							setMainStatusText( uiTextResources.getString("mainFrame.statusBar.waitingForUser") );					
				} catch (Exception e) {
					logger.log(Level.SEVERE, null, e);
				}
			}
		};
		worker.execute();
		privateRoomDialog.setVisible( false );
	}
	
	/**
	 * @param userInfo object which contains information for filling of the fields 
	 */
	private void setUserInfoInDialog(UserInfo userInfo){
		String name = "", family = "";
		String[] temp = userInfo.getName().split(" ");
		if ( temp != null && temp.length > 0 ){
			name = temp[0];
			if ( temp.length > 1 )
				family = temp[1];
		}
		
		privateRoomDialog.getLblLogin().setText(  "<html><b>" + 
				controllerManager.getAuthRegDialogController().getLogin() + "</b></html>" );
		privateRoomDialog.getTfldName().setText( name );
		privateRoomDialog.getTfldFamily().setText( family );
		privateRoomDialog.getTfldOrganization().setText( userInfo.getOrganization() );
		privateRoomDialog.getTfldEMail().setText( userInfo.getEmail() );
		privateRoomDialog.getTfldPhone().setText( userInfo.getPhone() );
		privateRoomDialog.getTfldPosition().setText( userInfo.getPosition() );
		privateRoomDialog.getExtraInfo().setText( userInfo.getExtraInfo() );
	}
}
