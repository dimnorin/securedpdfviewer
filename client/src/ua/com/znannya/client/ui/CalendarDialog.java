package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ua.com.znannya.client.app.ZnclApplication;

import com.toedter.calendar.JCalendar;

public class CalendarDialog extends JDialog{
	
	JCalendar calendar = new JCalendar();
	JButton btnOk = new JButton("Ok");
	
	public CalendarDialog( JDialog owner, Locale locale ){
		super(owner);
		setSize(220, 250);
		
		calendar.setLocale(locale);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout( new BoxLayout(btnPanel, BoxLayout.X_AXIS) );
		btnOk = new JButton("Ok");
		btnPanel.add( Box.createHorizontalGlue() );
		btnPanel.add( btnOk );
		
		setLayout( new BorderLayout() );
		add(calendar, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(ZnclApplication.getApplication().getMainFrame());
	}

	public JCalendar getCalendar() {
		return calendar;
	}

	public JButton getBtnOk() {
		return btnOk;
	}
}
