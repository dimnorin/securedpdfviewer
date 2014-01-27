package ua.com.znannya.client.ui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jivesoftware.smack.ZnConfiguration;

import ua.com.znannya.client.app.ZnclApplication;

/**
 * Specialized Panel widget for incapsulating status bar functionality of the application.
 */
public class ZnclStatusBar extends JPanel
{
	private float prevBalance = 0;
	private float usedBalance = 0;
	
  private JLabel lblMainStatus = new JLabel();
  private JLabel lblUsed = new JLabel();
  private JLabel lblAvailable = new JLabel();
  private String usedFunds, availableFunds;
//  private int usedPages;


  public ZnclStatusBar()
  {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
    add(lblMainStatus);
    add(Box.createHorizontalGlue());
    add(lblUsed); //TODO uncomment
    add(Box.createHorizontalStrut(20));
    add(lblAvailable);
    lblAvailable.setForeground(new Color(10, 5 ,72));  // cyan
    lblUsed.setForeground(new Color(175, 130, 88));    // orange
  }

  public void updateUsages()
  {
	  float funds = 0;
	  try {
		funds = Float.parseFloat(availableFunds.replaceAll(",", ""));
	  } catch (Exception e) {}
	  availableFunds = String.format("%,.2f ", funds);
	  calcUsedTime(funds);
	  
	  setBalance(usedFunds, availableFunds);
  }

  private void setBalance(String usedFunds, String availableFunds){
	  ResourceBundle uiTextResources = ZnclApplication.getApplication().getUiTextResources();
	    lblUsed.setText(
	      uiTextResources.getString("mainFrame.statusBar.pnlFundsUsed") +
	      " " + usedFunds + " " + uiTextResources.getString("mainFrame.statusBar.currency") 
	//      + " / " + usedPages + " " + uiTextResources.getString("mainFrame.statusBar.pages")
	    );
	
	    lblAvailable.setText(
	      uiTextResources.getString("mainFrame.statusBar.pnlFundsAvailable") + 
	      " " + availableFunds + " " + uiTextResources.getString("mainFrame.statusBar.currency")
	    );
  }
  
  public void clearBalance(){
	  prevBalance = 0;
	  usedBalance = 0;
	  availableFunds = "0.0";
	  usedFunds = "0.0";
	  setBalance(usedFunds, availableFunds);
  }

  public void setMainStatusText(String text)
  {
    lblMainStatus.setText(text);
  }

  private void setUsedFunds(String f)
  {
    usedFunds = f;
  }

/*  private void setUsedPages(int p)
  {
    usedPages = p;
  }*/

  public void setAvailableFunds(String f)
  {
    availableFunds = f;
//    getTimeFromFunds(availableFunds);
    updateUsages();
  }
  
/*  private String getTimeFromFunds(String availableFunds){
	  float minuteCost = Float.MAX_VALUE;
	    try{
	    	minuteCost = Float.parseFloat(ZnConfiguration.get(ZnConfiguration.TTRACK_MINUTE_COST));
	    }catch (Exception e) {}
	    
	    float funds = 0;
	    try {
			funds = Float.parseFloat(availableFunds.replaceAll(",", ""));
		} catch (Exception e) {}
	    int availableMinutes = Math.round(funds / minuteCost);
	    int	availableHours = availableMinutes / 60;
	    availableMinutes %= 60; 
	    
	    calcUsedTime(funds);
	    
	    return availableHours+":"+availableMinutes;
  }*/
  
  private void calcUsedTime(float availableFunds){
	  	float usedFunds = prevBalance - availableFunds;
	  	prevBalance = availableFunds;
	  	if(usedFunds < 0) return;
	  	usedBalance += usedFunds;
	  
//	  	float minuteCost = Float.MAX_VALUE;
//	  	try{
//		  minuteCost = Float.parseFloat(ZnConfiguration.get(ZnConfiguration.TTRACK_MINUTE_COST));
//	  	}catch (Exception e) {}
//	  
//	  	int usedMinutes = Math.round(usedBalance / minuteCost);
//	    int	usedHours = usedMinutes / 60;
//	    usedMinutes %= 60; 
	    
	    setUsedFunds(String.format("%,.2f ", usedBalance));
//	    setUsedTime(usedHours+":"+usedMinutes);
  }
  
}
