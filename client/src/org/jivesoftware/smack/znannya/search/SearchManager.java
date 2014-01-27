package org.jivesoftware.smack.znannya.search;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.EntriesCollection;
import org.jivesoftware.smack.packet.znannya.EntrySearch;
import org.jivesoftware.smack.znannya.dao.EntryType;

public class SearchManager {
    private XMPPConnection connection;

    /**
     * Creates a new SearchManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public SearchManager(XMPPConnection connection) {
        this.connection = connection;
    }

    /**
     * Ask server to do simple search  
     * @param query - what to search
     * @param startIndex - using for paging	
     * @param numResults - using for paging
     * @throws XMPPException
     */
    /* simoe search is not in use now
     * public EntriesCollection doSearch(String query, int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount) throws XMPPException {
    	if(query == null || "".equals(query.trim()) || isOnlyPunctuation(query)) return new EntriesCollection();
    	
        // Create a map for all the required attributes, but give them blank values.
        Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("startIndex", ""+startIndex);
		attributes.put("numResults", ""+numResults);
		attributes.put("orderField", orderField);
		attributes.put("isAsc", ""+isAsc);
		attributes.put("query", query);
		attributes.put("isCount", ""+isCount);
        
        return search(attributes);
    }*/
    
    /**
     * Ask server to do complex search
     * @param description
     * @param author
     * @param DACNTI_code
     * @param year
     * @param code
     * @param UDK_idx
     * @param startIndex
     * @param numResults
     * @param orderField
     * @param isAsc
     * @return results
     * @throws XMPPException
     */
    public EntriesCollection doDissertationsSearch(String name, String city, String description, String author, String DACNTI_code, 
    		String year, String code, String UDK_idx, int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount) throws XMPPException {
        // Create a map for all the required attributes, but give them blank values.
        Map<String, String> attributes = new HashMap<String, String>();

        if(name!=null && !"".equals(name) && !isOnlyPunctuation(name))
			attributes.put("name", name);
        if(city!=null && !"".equals(city) && !isOnlyPunctuation(city))
			attributes.put("city", city);
		if(description!=null && !"".equals(description) && !isOnlyPunctuation(description))
			attributes.put("description", description);
		if(author!=null && !"".equals(author)  && !isOnlyPunctuation(author))
			attributes.put("author", author);
		if(DACNTI_code!=null && !"".equals(DACNTI_code)  && !isOnlyPunctuation(DACNTI_code))
			attributes.put("DACNTI_code", DACNTI_code);
		if(year!=null && !"".equals(year))
			attributes.put("year", year);
		if(code!=null && !"".equals(code) && !isOnlyPunctuation(code))
			attributes.put("code", code);
		if(UDK_idx!=null && !"".equals(UDK_idx) && !isOnlyPunctuation(UDK_idx))
			attributes.put("UDK_idx", UDK_idx);
		
		if(attributes.isEmpty()) return new EntriesCollection();
		
		attributes.put("startIndex", ""+startIndex);
		attributes.put("numResults", ""+numResults);
		attributes.put("orderField", orderField);
		attributes.put("isAsc", ""+isAsc);
		attributes.put("isCount", ""+isCount);
		attributes.put("entryType", EntryType.diss.toString());
        
        return search(attributes);
    }
    
    public EntriesCollection doPublicationsSearch(String type, String name, String description, String author,  
    		String year, String isbn, String UDK_idx, String BBK_idx, String publisherName, 
    		int startIndex, int numResults, String orderField, boolean isAsc, boolean isCount) throws XMPPException {
        // Create a map for all the required attributes, but give them blank values.
        Map<String, String> attributes = new HashMap<String, String>();

        attributes.put("type", type);
        if(name!=null && !"".equals(name) && !isOnlyPunctuation(name))
			attributes.put("name", name);
		if(description!=null && !"".equals(description) && !isOnlyPunctuation(description))
			attributes.put("description", description);
		if(author!=null && !"".equals(author)  && !isOnlyPunctuation(author))
			attributes.put("author", author);
		if(year!=null && !"".equals(year))
			attributes.put("year", year);
		if(isbn!=null && !"".equals(isbn) && !isOnlyPunctuation(isbn))
			attributes.put("isbn", isbn);
		if(UDK_idx!=null && !"".equals(UDK_idx) && !isOnlyPunctuation(UDK_idx))
			attributes.put("UDK_idx", UDK_idx);
		if(BBK_idx!=null && !"".equals(BBK_idx) && !isOnlyPunctuation(BBK_idx))
			attributes.put("BBK_idx", BBK_idx);
		if(publisherName!=null && !"".equals(publisherName) && !isOnlyPunctuation(publisherName))
			attributes.put("publisherName", publisherName);
		
		if(attributes.isEmpty()) return new EntriesCollection();
		
		attributes.put("startIndex", ""+startIndex);
		attributes.put("numResults", ""+numResults);
		attributes.put("orderField", orderField);
		attributes.put("isAsc", ""+isAsc);
		attributes.put("isCount", ""+isCount);
		attributes.put("entryType", EntryType.pub.toString());
        
        return search(attributes);
    }

    /**
     * Ask server to do search
     *
     * @param attributes the search attributes.
     * @throws XMPPException if an error occurs creating the account.
     * @see #getAccountAttributes()
     */
    private EntriesCollection search(Map<String, String> attributes)throws XMPPException{
        EntrySearch reg = new EntrySearch();
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        reg.setAttributes(attributes);
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        EntriesCollection result = (EntriesCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout() * 24); //1 min
        // Stop queuing results
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        return result;
    }
    
    public static void main(String [] args){
    	SearchManager manager = new SearchManager(null);
    	System.out.println(manager.isOnlyPunctuation(" 4 "));
    }
    
    private boolean isOnlyPunctuation(String in){
    	Pattern p = Pattern.compile("[\\p{Punct}\\s]+");
	    Matcher m = p.matcher(in); // get a matcher object
	    return m.matches();
    }

}
