package stressTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.XMPPException;

import stressTest.ui.TestFrame;
import ua.com.znannya.client.util.GuiUtil;

public class StressTestMain implements ActionListener{
	public static final int PARALLEL_TYPE = 0;
	public static final int CHAIN_TYPE = 1;
	private int typeTest;
	private int countUser = 10;
	private int countDoneUser = 0;
	private int countConnectError;
	private int countRegisterError;
	private int countLoginError;
	private int countLoadInitialError;
	private int countGetKeyError;
	private int countGetBalanceError;
	private int countSearchAndDownloadError;
	
	private TestFrame testFrame;

	public StressTestMain(){
		GuiUtil.setSysLAF();
		getTestFrame().setVisible(true);
		getTestFrame().getBtnStart().addActionListener(this);
	}
	
	private void startTest(){
		User[] users = new User[countUser];
		for (int i = 0; i < users.length; i++) {
			users[i] = new User();
		}
		//wait while all user set connect
		if ( typeTest == PARALLEL_TYPE)
			for (int i = 0; i < users.length; i++){ 
//				connectUser( users[i] );
				if ( users[i].connect() )
					setStatus("user #: " + i + " \n\t Status: connected\n");
				else
					setStatus("user #: " + i + " \n\t Status: connection error\n");
			}
		
		for (int i = 0; i < users.length; i++){
			new TestThread(users[i], i);
		}
	}
	
//	private void connectUser( User user ){
//		while ( !user.connect() ){
//			countConnectError++;
//		}
//	}

	public class TestThread implements Runnable{
		private User user;
		private int numOfThread;
		
		public TestThread(User unit, int numOfThread){
			this.user = unit;
			this.numOfThread = numOfThread;
			Thread t = new Thread(this);
			t.start();
		}
		
		public void run(){
			if ( typeTest == StressTestMain.CHAIN_TYPE){
				if ( user.connect() )
					setStatus("user #: " + numOfThread + " \n\t Status: connected\n");
				else
					setStatus("user #: " + numOfThread + " \n\t Status: connection error\n");
			}
			String login = "Test" + numOfThread;
			try {
				user.register(login, login, login + "@.ya.ru");
				setStatus("user #: " + numOfThread + " \n\t Status: register completed\n");
			} catch (XMPPException e) {
				e.printStackTrace();
				countRegisterError++;
			}
			
			if ( !user.login(login, login) ){
				countLoginError++;
				return;
			}
			setStatus("user #: " + numOfThread + " \n\t Status: logged\n");
			
			try {
				user.loadInitialProperties();
				setStatus("user #: " + numOfThread + " \n\t Status: loadInitial completed\n");
			} catch (XMPPException e) {
				e.printStackTrace();
				countLoadInitialError++;
			}
			
			try {
				user.getKeyOverDHManager();
				setStatus("user #: " + numOfThread + " \n\t Status: key is to get\n");
			} catch (XMPPException e) {
				e.printStackTrace();
				countGetKeyError++;
			}
			
			try {
				user.getBalance();
				setStatus("user #: " + numOfThread + " \n\t Status: balance is to get\n");
			} catch (XMPPException e) {
				e.printStackTrace();
				countGetBalanceError++;
			}
			
			try {
				user.makeSearchAndGetFiles();
				setStatus("user #: " + numOfThread + " \n\t Status: search and getfiles comleted\n");
			} catch (XMPPException e) {
				e.printStackTrace();
				countSearchAndDownloadError++;
			}
			
			user.disconnect();
			setStatus("user #: " + numOfThread + " \n\t Status: test complete\n");
			done();
		}
	}
	private void done(){
		countDoneUser++;
		if ( countDoneUser == countUser ){
			StringBuffer strBuffer = new StringBuffer( getTestFrame().getTextPaneStatus().getText() );
			strBuffer.append("\n==================================================\nResults:\n");
			strBuffer.append("Count user: " + countUser + "\n");
			strBuffer.append("\nCount connect error: " + countConnectError);
			strBuffer.append("\nCount register error: " + countRegisterError);
			strBuffer.append("\nCount login error: " + countLoginError);
			strBuffer.append("\nCount load initial error: " + countLoadInitialError);
			strBuffer.append("\nCount get key over DHManager error: " + countGetKeyError);
			strBuffer.append("\nCount get Balance error: " + countGetBalanceError);
			strBuffer.append("\nCount search and download error: " + countSearchAndDownloadError);
			getTestFrame().getTextPaneStatus().setText( strBuffer.toString() );
			getTestFrame().getBtnStart().setEnabled(true);
		}
	}
	
	private void setStatus( String status ){
		String text = getTestFrame().getTextPaneStatus().getText();
		StringBuffer s = new StringBuffer( text!=null?text:"null" );
		s.append(status);
		getTestFrame().getTextPaneStatus().setText(s.toString());
	}
	
	private TestFrame getTestFrame(){
		if ( testFrame == null ){
			testFrame = new TestFrame();
		}
		return testFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == getTestFrame().getBtnStart() ){
			if ( getTestFrame().getTextFieldCountUser().getText().isEmpty() ){
				JOptionPane.showMessageDialog(getTestFrame(), "Count of user field is empty");
				return;
			}
			countUser = Integer.parseInt( getTestFrame().getTextFieldCountUser().getText() );
			if ( getTestFrame().getRadioBtnChain().isSelected() ){
				typeTest = CHAIN_TYPE;
			}
			startTest();
			getTestFrame().getBtnStart().setEnabled(false);
		}
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StressTestMain();
	}
}
