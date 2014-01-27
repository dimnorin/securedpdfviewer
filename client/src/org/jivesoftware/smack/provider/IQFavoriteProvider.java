package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.packet.znannya.Favorite;
import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Publication;
import org.xmlpull.v1.XmlPullParser;

public class IQFavoriteProvider implements IQProvider {
	public static final String ELEMENT_NAME = Favorite.ELEMENT_NAME;
	public static final String NAMESPACE = Favorite.NAMESPACE;
	
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		EntriesCollection collection = new EntriesCollection();
		
		boolean done = false;
		while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
            	if (parser.getName().equals("entries")) {
            		return parseEntries(parser);
				}
            }
        }
		return collection;
	}

	private EntriesCollection parseEntries(XmlPullParser parser) throws Exception {
		EntriesCollection collection = new EntriesCollection();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("dissertation")) {
					parseDissertations(collection, parser);
				}else if(parser.getName().equals("publication"))
					parsePublications(collection, parser);
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("entries")) {
					done = true;
				}
			}
		}
		return collection;
	}
	
	private EntriesCollection parseDissertations(EntriesCollection collection, XmlPullParser parser) throws Exception{
		Dissertation diss = new Dissertation();		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("dissID")) {
					diss.setID(Integer.parseInt(parser.nextText()));
				} else if (parser.getName().equals("name")) {
					diss.setName(parser.nextText());
				} else if (parser.getName().equals("author")) {
					diss.setAuthor(parser.nextText());
				} else if (parser.getName().equals("kind")) {
					diss.setKind(parser.nextText());
				} else if (parser.getName().equals("code")) {
					diss.setCode(parser.nextText());
				} else if (parser.getName().equals("organization")) {
					diss.setOrganization(parser.nextText());
				} else if (parser.getName().equals("city")) {
					diss.setCity(parser.nextText());
				} else if (parser.getName().equals("year")) {
					diss.setYear(parser.nextText());
				} else if (parser.getName().equals("pages")) {
					diss.setPages(Integer.parseInt(parser.nextText()));
				} else if (parser.getName().equals("DACNTI_code")) {
					diss.setDACNTI_code(parser.nextText());
				} else if (parser.getName().equals("UDK_idx")) {
					diss.setUDK_idx(parser.nextText());
				} else if (parser.getName().equals("description")) {
					diss.setDescription(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("dissertation")) {
					collection.add(diss);
					done = true;
				}
			}
		}
		return collection;
	}
	
	private EntriesCollection parsePublications(EntriesCollection collection, XmlPullParser parser) throws Exception{
		Publication pub = new Publication();		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("ID")) {
					pub.setID(Integer.parseInt(parser.nextText()));
				} else if (parser.getName().equals("name")) {
					pub.setName(parser.nextText());
				} else if (parser.getName().equals("author")) {
					pub.setAuthor(parser.nextText());
				} else if (parser.getName().equals("city")) {
					pub.setCity(parser.nextText());
				} else if (parser.getName().equals("year")) {
					pub.setYear(parser.nextText());
				} else if (parser.getName().equals("description")) {
					pub.setDescription(parser.nextText());
				} else if (parser.getName().equals("isbn")) {
					pub.setIsbn(parser.nextText());
				} else if (parser.getName().equals("UDK_idx")) {
					pub.setUDK_idx(parser.nextText());
				} else if (parser.getName().equals("BBK_idx")) {
					pub.setBBK_idx(parser.nextText());
				} else if (parser.getName().equals("publisherName")) {
					pub.setPublisherName(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("publication")) {
					collection.add(pub);
					done = true;
				}
			}
		}
		return collection;
	}
}
