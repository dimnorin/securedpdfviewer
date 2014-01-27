package ua.com.znannya.client.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.sun.awt.AWTUtilities;

import ua.com.znannya.client.app.ZnclApplication;

public class LoadDialog {
	private static JDialog loadDialog;
	private static boolean isShow = false;
	private static Thread worker = null;
	private static Image loadImg;
	
	public synchronized static void showLoadImg() {
		if(loadImg == null)
			loadImg = ZnclApplication.getApplication().getIcon("splash.gif").getImage();
		int imgWidth = loadImg.getWidth(null);
		int imgHeight = loadImg.getHeight(null);
		
		if (loadDialog == null) {
			loadDialog = new JDialog(ZnclApplication.getApplication()
					.getMainFrame());
			loadDialog.setUndecorated(true);
			loadDialog.setResizable(false);
			loadDialog.setModal(true);
			
			JPanel imgPanel = new JPanel() {
				@Override
				public void paint(Graphics g) {
					// draw the image
					if (loadImg != null)
						g.drawImage(loadImg, 0, 0, this);			
				}
			};
			
			loadDialog.setSize(imgWidth, imgHeight);
			
			/*try{
				//support from this operation since 6u10
				Shape shape = new Ellipse2D.Float(1, 1, loadDialog.getWidth()-2, loadDialog.getHeight()-2);
				AWTUtilities.setWindowShape(loadDialog, shape);
			}catch (Exception e) {}*/

			loadDialog.add(imgPanel);
		}
		Dimension parentSize = ZnclApplication.getApplication().getMainFrame()
				.getSize();
		Point parentLocation = ZnclApplication.getApplication().getMainFrame()
				.getLocation();
		loadDialog.setLocation(parentLocation.x + (parentSize.width - imgWidth)/ 2,
				parentLocation.y + (parentSize.height - imgHeight)/ 2);

		if(!isShow){
			worker = new Thread(){
				public void run(){
					loadDialog.setVisible(true);					
				}
			};
			worker.start();
			isShow = true;
		}
	}

	public synchronized static void hideLoadImg() {
		if (loadDialog != null)
			if(isShow){
				worker.interrupt();
				loadDialog.setVisible(false);
				isShow = false;
			}
	}
}
