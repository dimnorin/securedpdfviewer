package org.jivesoftware.smack.packet.znannya;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.Base64;

public class Feedback extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "urn:xmpp:feedback";
	
	private String description;
	private String failurePoint;
	private String log;
	
	public Feedback(String description, String failurePoint, String log) {
		super();
		this.description = description;
		this.failurePoint = failurePoint;
		this.log = Base64.encodeBytes(log.getBytes());
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
//        buf.append("<").append(ELEMENT_NAME).append(">");
        buf.append("<description>").append(description).append("</description>");
        buf.append("<failurePoint>").append(failurePoint).append("</failurePoint>");
        buf.append("<log>").append(log).append("</log>");
//        buf.append("</").append(ELEMENT_NAME).append(">");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

}
