package org.jivesoftware.smack.packet.znannya;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

public class StringCollection extends IQ {
	public static final String ELEMENT_NAME = "initialload";
	public static final String NAMESPACE = "urn:xmpp:initload";
	
	private List<String> strList = new ArrayList<String>();
	
	public void add(String str){
		strList.add(str);
	}
	
	public List<String> getStrings(){
		return strList;
	}
	
	@Override
	public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\""+NAMESPACE+"\">");
        buf.append("<").append(ELEMENT_NAME).append("/>");
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
	}

}
