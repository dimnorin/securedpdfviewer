package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;

public class Statistic extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "urn:xmpp:statistics";
	
	private Type type;
	private long from;
	private long to;
	
	public Statistic() {}
	
	public Statistic(Type type, long from, long to) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
//        buf.append("<").append(ELEMENT_NAME).append(">");
        buf.append("<type>").append(type).append("</type>");
        buf.append("<from>").append(from).append("</from>");
        buf.append("<to>").append(to).append("</to>");
//        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

	@Override
	public String toString() {
		return "Statistic [from=" + from + ", to=" + to + ", type=" + type
				+ "]";
	}

	public enum Type{
		/**
		 * For brief statistics
		 */
		brief,
		/**
		 * For full statistics
		 */
		full
	}
}
