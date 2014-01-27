package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.Balance;
import org.jivesoftware.smack.znannya.track.TimeTrack;
import org.xmlpull.v1.XmlPullParser;

public class IQTimeTrackProvider implements IQProvider {
	public static final String ELEMENT_NAME = Balance.ELEMENT_NAME;
	public static final String NAMESPACE = Balance.NAMESPACE;

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		Balance balance = new Balance();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("action")) {
					balance.setAction(Enum.valueOf(TimeTrack.Type.class, parser.nextText()));
				}else if (parser.getName().equals("balance")) {
					balance.setBalance(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals(ELEMENT_NAME)) {
					done = true;
				}
			}
		}
		return balance;
	}

}
