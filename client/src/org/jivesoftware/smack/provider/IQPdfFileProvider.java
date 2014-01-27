package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.PdfFile;
import org.xmlpull.v1.XmlPullParser;

public class IQPdfFileProvider implements IQProvider {
	public static final String ELEMENT_NAME = PdfFile.ELEMENT_NAME;
	public static final String NAMESPACE = PdfFile.NAMESPACE;

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		PdfFile pdfFile = new PdfFile();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {

			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("pdffile")) {
					done = true;
				}
			}
		}
		return pdfFile;
	}
}
