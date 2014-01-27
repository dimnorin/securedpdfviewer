package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.track.TimeTrack;
import org.jivesoftware.smack.znannya.track.TimeTrack.Type;

public class Balance extends IQ {
	public static final String ELEMENT_NAME = "ttrack";
	public static final String NAMESPACE = "urn:xmpp:ttrack";
	
	private TimeTrack.Type action;
	private String balance;
	private long fileID;
	private EntryType entryType;
	
	public Balance(){}
	
	public Balance(TimeTrack.Type action) {
		super();
		this.action = action;
	}
	
	public Balance(TimeTrack.Type action, long fileID, EntryType entryType) {
		this(action);
		this.fileID = fileID;
		this.entryType = entryType;
	}

	public TimeTrack.Type getAction() {
		return action;
	}

	public void setAction(TimeTrack.Type action) {
		this.action = action;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append(ELEMENT_NAME).append(">");
        buf.append("<action>").append(action).append("</action>");
        buf.append("<fileID>").append(fileID).append("</fileID>");
        buf.append("<entryType>").append(entryType).append("</entryType>");
        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

}
