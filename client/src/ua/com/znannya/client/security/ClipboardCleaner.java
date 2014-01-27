package ua.com.znannya.client.security;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardCleaner extends Thread {
	
	public ClipboardCleaner(){
		setDaemon(true);
	}

	public void run() {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		Transferable transferableText = new StringSelection("");
		while (true) {
			try {
				// set the textual content on the clipboard to our
				// Transferable object
				// we use the
				if (isClear(systemClipboard)) {
					systemClipboard.setContents(transferableText, null);
				}
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}

	private boolean isClear(Clipboard systemClipboard) {
		// get the contents on the clipboard in a Transferable object
		Transferable clipboardContents = systemClipboard.getContents(null);

		// check if contents are empty, if so, return null
		if (clipboardContents == null)
			return true;
		else{
//			try {
				// make sure content on clipboard is
				// falls under a format supported by the
				// imageFlavor Flavor
//				long start = System.currentTimeMillis();
				if (clipboardContents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
					// convert the Transferable object
					// to an Image object
//					Image image = (Image) clipboardContents.getTransferData(DataFlavor.imageFlavor);
//					System.out.println("time="+(System.currentTimeMillis() - start));
//					System.out.println("Detect image");
					return true;
				}
			/*} catch (UnsupportedFlavorException ufe) {
				return true;
			} catch (IOException ioe) {
				return true;
			}*/
		}
		return false;
	}
}
