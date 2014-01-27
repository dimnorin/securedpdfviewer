package org.jivesoftware.smack.znannya.pdf;

import java.io.InputStream;

import org.jivesoftware.smack.packet.XMPPError;

public interface IFileDownloadStatusListener {
	/**
	 * Notify listener about getting new file pat
	 */
	void updateProgress(double newProgress);
	/**
	 * Finish file download
	 * @param isMd5Matches - indicates whether file correctly downloaded
	 */
	void finishDownload(InputStream is, boolean isMd5Matches);
	/**
	 * Notify listener that error occurs while file downloading
	 * @param e - error
	 */
	void failedDownload(XMPPError e);
	
	
}
