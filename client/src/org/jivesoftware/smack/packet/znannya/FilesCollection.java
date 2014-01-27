package org.jivesoftware.smack.packet.znannya;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;

public class FilesCollection extends IQ {
	public static final String ELEMENT_NAME = EntrySearch.ELEMENT_NAME;
	public static final String NAMESPACE = EntrySearch.NAMESPACE;
	
	private List<File> filesList = new ArrayList<File>();
	private long pubID;
	private EntryType entryType;

	public void add(File file){
		filesList.add(file);
	}
	
	public List<File> getFiles(){
		return filesList;
	}
	
	public long getPubID() {
		return pubID;
	}

	public void setPubID(long pubID) {
		this.pubID = pubID;
	}

	public EntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append("dissfile").append(">");
        buf.append("<entryType>").append(entryType.toString()).append("</entryType>");
        buf.append("<pubID>").append(pubID).append("</pubID>");
        buf.append("</").append("dissfile").append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}
}
