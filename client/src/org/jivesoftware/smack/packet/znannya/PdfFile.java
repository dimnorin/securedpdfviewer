package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.znannya.dao.EntryType;

public class PdfFile extends IQ {
	public static final String ELEMENT_NAME = "pdffile";
	public static final String NAMESPACE = "urn:xmpp:pdffile";
	
	private long fileID;
	private String pages;
	private EntryType entryType;
	
	public PdfFile() {}
	
	public PdfFile(long fileID, String pages, EntryType entryType) {
		super();
		this.fileID = fileID;
		this.pages = pages;
		this.entryType = entryType;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append(ELEMENT_NAME).append(">");
        buf.append("<fileID>").append(fileID).append("</fileID>");
        buf.append("<pages>").append(pages).append("</pages>");
        buf.append("<entryType>").append(entryType.toString()).append("</entryType>");
        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}
	
}
