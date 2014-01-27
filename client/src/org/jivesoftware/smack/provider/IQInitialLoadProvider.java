package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.PropertiesCollection;
import org.jivesoftware.smack.packet.znannya.StringCollection;
import org.xmlpull.v1.XmlPullParser;

public class IQInitialLoadProvider implements IQProvider{
	public static final String ELEMENT_NAME = StringCollection.ELEMENT_NAME;
	public static final String NAMESPACE = StringCollection.NAMESPACE;

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		StringCollection collection = new StringCollection();
		
		boolean done = false;
		while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
            	if (parser.getName().equals("codes")) {
            		return parseCodes(parser);
				}else if (parser.getName().equals("years")){
					return parseYears(parser);
				}else if (parser.getName().equals("publishers")){
					return parsePublishers(parser);
				}else if (parser.getName().equals("properties")){
					return parseProperties(parser);
				}
            }
        }
		return collection;
	}
	
	private StringCollection parseCodes(XmlPullParser parser) throws Exception {
		StringCollection collection = new StringCollection();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("code")) {
					collection.add(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("codes")) {
					done = true;
				}
			}
		}
		return collection;
	}
	
	private StringCollection parseYears(XmlPullParser parser) throws Exception {
		StringCollection collection = new StringCollection();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("year")) {
					collection.add(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("years")) {
					done = true;
				}
			}
		}
		return collection;
	}
	
	private StringCollection parsePublishers(XmlPullParser parser) throws Exception {
		StringCollection collection = new StringCollection();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("publisher")) {
					collection.add(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("publishers")) {
					done = true;
				}
			}
		}
		return collection;
	}
	
	private PropertiesCollection parseProperties(XmlPullParser parser) throws Exception {
		PropertiesCollection collection = new PropertiesCollection();

		PropertiesCollection.Property property = collection.new Property(); 
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("property")) {
					property = collection.new Property(); 
				}else if (parser.getName().equals("name")) {
					property.setName(parser.nextText());
				}else if (parser.getName().equals("propvalue")) {
					property.setValue(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("property")) {
					collection.add(property); 
				}else if (parser.getName().equals("properties")) {
					done = true;
				}
			}
		}
		return collection;
	}

}
