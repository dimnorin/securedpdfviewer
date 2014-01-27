package org.jivesoftware.smack.znannya.search;

import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ZnConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.PropertiesCollection;
import org.jivesoftware.smack.packet.znannya.StringCollection;
import org.jivesoftware.smack.packet.znannya.PropertiesCollection.Property;

public class InitialLoadManager {
	private XMPPConnection connection;
	
	 /**
     * Creates a new InitialLoadManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public InitialLoadManager(XMPPConnection connection) {
        this.connection = connection;
    }

    /**
     * Ask server to do load of initial params
     * @param codes - to fill codes combo
     * @param years - to fill years combo
     * @throws XMPPException
     */
    public void doLoad(List<String> codes, List<String> years, List<String> publishers) throws XMPPException {
        StringCollection reg = new StringCollection();
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        StringCollection result = (StringCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //1 min
        // Stop queuing results
//        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        codes.addAll(result.getStrings());
        
        result = (StringCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //1 min
        // Stop queuing results
//        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        years.addAll(result.getStrings());
        
        result = (StringCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //1 min
        // Stop queuing results
//        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        publishers.addAll(result.getStrings());
        
        PropertiesCollection result1 = (PropertiesCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //1 min
        // Stop queuing results
        collector.cancel();
        if (result1 == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result1.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result1.getError());
        }
        for (Property prop : result1.getProperties()){
    	    ZnConfiguration.put(prop.getName(), prop.getValue());
        }
    }
}
