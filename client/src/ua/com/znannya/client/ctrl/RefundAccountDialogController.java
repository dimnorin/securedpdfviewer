package ua.com.znannya.client.ctrl;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JDialog;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.AccountIncreaseDialog;

public class RefundAccountDialogController extends MouseAdapter implements
		ActionListener, ItemListener {

	private ResourceBundle uiTextResources;
	private HashMap<String, SMSPriceEntry> smsPriceMap = null;
	private AccountIncreaseDialog accIncDlg = null;

	public RefundAccountDialogController() {
		smsPriceMap = new HashMap<String, SMSPriceEntry>();
		init();
	}

	private void init() {

		uiTextResources = ZnclApplication.getApplication().getUiTextResources();
		int n = Integer.parseInt(uiTextResources
				.getString("accountIncreaseDialog.countOfPrices"));
		int num;
		for (int i = 1; i <= n; i++) {
			SMSPriceEntry entry = new SMSPriceEntry(uiTextResources.getString("accountIncreaseDialog.price" + i));
			addMapEntity(entry.getUnits(), entry);
		}
	}

	/**
	 * Add entity(cost,number) to hashmap
	 * @param units - sms service price in units
	 * @param entry - sms price entry object
	 */
	public void addMapEntity(String units, SMSPriceEntry entry) {
		smsPriceMap.put(units, entry);
	}

	/**
	 * Get the link on the accountIncreaseDialog. 
	 * If accountIncreaseDialog equal empty then create it.
	 * @return accountIncreaseDialog
	 */
	public AccountIncreaseDialog getDialog() {
		if (accIncDlg == null) {
			accIncDlg = new AccountIncreaseDialog(ZnclApplication
					.getApplication().getMainFrame());
			setupListeners();
			loadMapIntoBox();
		}
		return accIncDlg;
	}

	private void setupListeners() {
		accIncDlg.getNumbersBox().addItemListener(this);
		accIncDlg.getUrlLabel().addMouseListener(this);
		accIncDlg.getCloseButton().addActionListener(this);
	}

	private void loadMapIntoBox() {

		JComboBox box = accIncDlg.getNumbersBox();
		box.removeAllItems();
		Set<String> keySet = (Set<String>) smsPriceMap.keySet();
		String[] keys = new String[keySet.size()];
		
		//  Below is ordering keys in ascending order of their numbers
		keys = keySet.toArray(keys);
		Arrays.sort(keys, new StringComparator());

		for (int i = 0; i < keys.length; i++)
			box.addItem(keys[i]);

	}

	private void setSelectedNumberAndCost(String selValue) {

		if (smsPriceMap.containsKey(selValue)) {

			SMSPriceEntry entry = smsPriceMap.get(selValue);
			String text = uiTextResources
					.getString("accountIncreaseDialog.text");
		
			String login = ZnclApplication.getApplication().getControllerManager().getAuthRegDialogController().getLogin();
			text = String.format(text, entry.getUnits(), login, entry
					.getPhoneNumber(), entry.getCost());	
			getDialog().getTextPane().setText(text);
		}

	}
	
   /**
    * This method shows AccountIncreaseDialog and 
    * locate it by centre relative to Main Window 
    */
	public void showDialog() {

		JDialog dlg = getDialog();
		int w = dlg.getWidth();
		int h = dlg.getHeight();
		Rectangle bounds = dlg.getOwner().getBounds();
		// compute of left-upper point of Dialog location
		bounds.x += (bounds.width / 2) - w / 2;
		bounds.y += (bounds.height / 2) - h / 2;
		bounds.width = w;
		bounds.height = h;
		dlg.setBounds(bounds);
		dlg.setVisible(true);

	}
	
    /**
     * Close AccountIncreaseDialog (invoke setVisible(false))
     */
	public void closeDialog() {
		getDialog().setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == getDialog().getCloseButton())
			closeDialog();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		String selValue = (String) getDialog().getNumbersBox()
				.getSelectedItem();
		if (!selValue.equals("null"))
			setSelectedNumberAndCost(selValue);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == getDialog().getUrlLabel()) {

			// open default internet browser and go to site, which locate on given url  
			URI uri;
			try {
				uri = new URI(uiTextResources
						.getString("accountIncreaseDialog.url"));
				Desktop des = Desktop.getDesktop();
				des.browse(uri);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		// set default mouse cursor
		if (e.getSource() == getDialog().getUrlLabel())
			getDialog().setCursor(
					Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	}

	@Override
	public void mouseExited(MouseEvent e) {

		// set default mouse cursor
		if (e.getSource() == getDialog().getUrlLabel())
			getDialog().setCursor(
					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * In this class compares two strings on the first four numbers
	 */
	private class StringComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {

			String[] parts1 = o1.split(" ");
			String[] parts2 = o2.split(" ");
			
			float num1 = Float.parseFloat(parts1[0]);
			float num2 = Float.parseFloat(parts2[0]);

			if (num1 < num2)
				return -1;
			else if (num1 > num2)
				return 1;
			else
				return 0;
		}

	}
	
	private class SMSPriceEntry{
		private int phoneNumber;
		private String cost;
		private String units;
		
		public SMSPriceEntry(String in){
			String[] parts = in.split(" ");
			phoneNumber = Integer.parseInt(parts[0]);
			cost = parts[1] + " " + parts[2];
			units = parts[3] + " " + parts[4];
		}

		public int getPhoneNumber() {
			return phoneNumber;
		}

		public String getCost() {
			return cost;
		}

		public String getUnits() {
			return units;
		}
	}

}
