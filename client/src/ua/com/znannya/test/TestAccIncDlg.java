package ua.com.znannya.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import sun.font.CreatedFontTracker;
import ua.com.znannya.client.ui.AccountIncreaseDialog;

public class TestAccIncDlg {

	public static void main(String[] args) {
		
		MainWindow.createWindow();
	}
}	
	
 class MainWindow extends JFrame{
		
		private AccountIncreaseDialog dlg = null;
		
		public MainWindow() {
			super();
			init();
		}
		
		private void init(){
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(new Dimension(800,600));
			setLayout(new FlowLayout(FlowLayout.LEFT));
			JMenuBar bar = new JMenuBar();
			JMenu menu = new JMenu("File");
			JMenuItem open = new JMenuItem("open AccountIncreaseDialog");
			open.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					getAccIncDlg().setVisible(true);					
				}
			});
			menu.add(open);
			bar.add(menu);
			getContentPane().add(bar);			
		}
		
		private AccountIncreaseDialog getAccIncDlg(){
			if (dlg == null){
				dlg = new AccountIncreaseDialog(this);
			}
			return dlg;
		}
		
		public static void createWindow(){
			MainWindow window = new MainWindow();
			window.setVisible(true);
			window.pack();
		}
		
	}
	

