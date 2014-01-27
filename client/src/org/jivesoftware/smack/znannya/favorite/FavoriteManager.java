package org.jivesoftware.smack.znannya.favorite;

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
import org.jivesoftware.smack.packet.znannya.Favorite;
import org.jivesoftware.smack.znannya.dao.EntryType;

public class FavoriteManager {
    private XMPPConnection connection;

    /**
     * Creates a new FavoriteManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public FavoriteManager(XMPPConnection connection) {
        this.connection = connection;
    }

    /**
     * Ask server to get favorites
     *
     * @throws XMPPException if an error occurs creating the account.
     */
    public EntriesCollection getFavorites()throws XMPPException{
        Favorite reg = new Favorite();
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        EntriesCollection result = (EntriesCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
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
    
    public void addFavorite(int pubID, EntryType entryType){
    	Favorite reg = new Favorite(pubID, entryType, Favorite.Action.add);
        reg.setType(IQ.Type.SET);
        reg.setTo(connection.getServiceName());
        connection.sendPacket(reg);
    }
    
    public void removeFavorite(int pubID, EntryType entryType){
    	Favorite reg = new Favorite(pubID, entryType, Favorite.Action.remove);
        reg.setType(IQ.Type.SET);
        reg.setTo(connection.getServiceName());
        connection.sendPacket(reg);
    }
}
