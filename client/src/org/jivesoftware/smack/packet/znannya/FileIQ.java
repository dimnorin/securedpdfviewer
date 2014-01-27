package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;

public class FileIQ extends IQ{
	public static final String ELEMENT_NAME = FilesCollection.ELEMENT_NAME;
	public static final String NAMESPACE = FilesCollection.NAMESPACE;
	
	private File fileObject;
	private long fileID;
	private EntryType entryType;
	
	public File getFileObject() {
		return fileObject;
	}

	public void setFileObject(File fileObject) {
		this.fileObject = fileObject;
	}

	public long getFileID() {
		return fileID;
	}

	public void setFileID(long fileID) {
		this.fileID = fileID;
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
        buf.append("<fileID>").append(fileID).append("</fileID>");
        buf.append("</").append("dissfile").append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}
}
