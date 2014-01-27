package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.EntryType;

public class Favorite extends IQ {
	public static final String ELEMENT_NAME = "favquery";
	public static final String NAMESPACE = "urn:xmpp:favorite";
	
	private int pubID;
	private EntryType entryType;
	private Action action;
	
	public Favorite(){}
	
	public Favorite(int pubID, EntryType entryType, Action action) {
		super();
		this.pubID = pubID;
		this.entryType = entryType;
		this.action = action;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append("favorite").append(">");
        if(action != null)
        	buf.append("<action>").append(action.toString()).append("</action>");
        buf.append("<pubID>").append(pubID).append("</pubID>");
        if(entryType != null)
        	buf.append("<entryType>").append(entryType.toString()).append("</entryType>");
        buf.append("</").append("favorite").append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}
	
	public enum Action{
		add,
		remove
	}

}
