package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.UserInfo;
import org.xmlpull.v1.XmlPullParser;

public class IQUserInfoProvider implements IQProvider {
	public static final String ELEMENT_NAME = UserInfo.ELEMENT_NAME;
	public static final String NAMESPACE = UserInfo.NAMESPACE;

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		UserInfo userInfo = new UserInfo();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("name")) {
					userInfo.setName(parser.nextText());
				}else if (parser.getName().equals("organization")) {
					userInfo.setOrganization(parser.nextText());
				}else if (parser.getName().equals("position")) {
					userInfo.setPosition(parser.nextText());
				}else if (parser.getName().equals("phone")) {
					userInfo.setPhone(parser.nextText());
				}else if (parser.getName().equals("email")) {
					userInfo.setEmail(parser.nextText());
				}else if (parser.getName().equals("extraInfo")) {
					userInfo.setExtraInfo(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals(ELEMENT_NAME)) {
					done = true;
				}
			}
		}
		return userInfo;
	}
}
