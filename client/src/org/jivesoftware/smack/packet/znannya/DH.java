package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;

public class DH extends IQ {
	private long A;
	private long B;
	
	public static final String ELEMENT_NAME = "dh";
	public static final String NAMESPACE = "urn:xmpp:dh";
	
	public DH(){}
	
	public DH(long a) {
		super();
		A = a;
	}

	public long getB() {
		return B;
	}

	public void setB(long b) {
		B = b;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append(ELEMENT_NAME).append(">");
        buf.append("<a>").append(A).append("</a>");
        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

}
