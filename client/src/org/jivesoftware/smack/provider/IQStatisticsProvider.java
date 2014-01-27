package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.Statistic;
import org.jivesoftware.smack.packet.znannya.StatsCollection;
import org.jivesoftware.smack.znannya.dao.BriefStatistics;
import org.jivesoftware.smack.znannya.dao.FullStatistics;
import org.xmlpull.v1.XmlPullParser;

public class IQStatisticsProvider implements IQProvider{
	public static final String ELEMENT_NAME = Statistic.ELEMENT_NAME;
	public static final String NAMESPACE = Statistic.NAMESPACE;
	
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		StatsCollection collection = new StatsCollection();
		
		boolean done = false;
		while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
            	if (parser.getName().equals("brief")) {
            		return parseBrief(parser);
				}else if (parser.getName().equals("full")) {
            		return parseFull(parser);
				}
            }
        }
		return collection;
	}

	private IQ parseBrief(XmlPullParser parser) throws Exception {
		BriefStatistics stat = new BriefStatistics();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("from")) {
					stat.setFromBalance(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("to")) {
					stat.setToBalance(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("spent")) {
					stat.setSpentBalance(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("refill")) {
					stat.setRefillBalance(Float.parseFloat(parser.nextText()));
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("brief")) {
					done = true;
				}
			}
		}
		return stat;
	}
	
	private IQ parseFull(XmlPullParser parser) throws Exception {
		StatsCollection collection = new StatsCollection();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("statentry")) {
					parseFullStatistics(collection, parser);
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("full")) {
					done = true;
				}
			}
		}
		return collection;
	}
	
	private StatsCollection parseFullStatistics(StatsCollection collection, XmlPullParser parser) throws Exception{
		FullStatistics stat = new FullStatistics();		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("time")) {
					stat.setTime(Long.parseLong(parser.nextText()));
				} else if (parser.getName().equals("ip")) {
					stat.setIp(parser.nextText());
				} else if (parser.getName().equals("action")) {
					stat.setAction(parser.nextText());
				} else if (parser.getName().equals("quantity")) {
					stat.setQuantity(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("unit")) {
					stat.setUnit(parser.nextText());
				} else if (parser.getName().equals("costPerUnit")) {
					stat.setCostPerUnit(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("amount")) {
					stat.setAmount(Float.parseFloat(parser.nextText()));
				} else if (parser.getName().equals("description")) {
					stat.setDescription(parser.nextText());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("statentry")) {
					collection.add(stat);
					done = true;
				}
			}
		}
		return collection;
	}
	

}
