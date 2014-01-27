package org.jivesoftware.smack.znannya.track;

import org.jivesoftware.smack.packet.XMPPError;

public interface ITrackUpdatesListener {
	
	/**
	 * If track updater got error document showing should be stoped
	 * @param error may be null
	 */
	void gotError(XMPPError error);
	
	/**
	 * New balance value
	 * @param balance
	 */
	void gotBalanceUpdate(String balance);
}
