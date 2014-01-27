package org.jivesoftware.smack.packet.znannya;

import java.util.Map;

import org.jivesoftware.smack.packet.IQ;

public class EntrySearch extends IQ {
	public static final String ELEMENT_NAME = "dissquery";
	public static final String NAMESPACE = "urn:xmpp:dissertation";
	
    private Map<String, String> attributes = null;
    private boolean isSort = false;
	
    /**
     * Returns the map of String key/value pairs of account attributes.
     *
     * @return the account attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Sets the account attributes. The map must only contain String key/value pairs.
     *
     * @param attributes the account attributes.
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<publication>");
        buf.append("<isSort>").append(isSort).append("</isSort>");
        if (attributes != null && attributes.size() > 0) {
            for (String name : attributes.keySet()) {
                String value = attributes.get(name);
                buf.append("<").append(name).append(">");
                buf.append(value);
                buf.append("</").append(name).append(">");
            }
        }
        buf.append("</publication>");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }

}
