package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.DH;
import org.xmlpull.v1.XmlPullParser;

public class IQDHProvider implements IQProvider{
	public static final String ELEMENT_NAME = DH.ELEMENT_NAME;
	public static final String NAMESPACE = DH.NAMESPACE;
	
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		DH dh = new DH();
		
		boolean done = false;
		while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
            	if (parser.getName().equals("b")) {
            		dh.setB(Long.parseLong(parser.nextText()));
				}
            } else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("dh")) {
					done = true;
				}
			}
        }
		return dh;
	}
}
