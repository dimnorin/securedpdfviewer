package org.jivesoftware.smack.znannya.search;

import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.FileIQ;
import org.jivesoftware.smack.packet.znannya.FilesCollection;
import org.jivesoftware.smack.znannya.dao.EntryType;
import org.jivesoftware.smack.znannya.dao.File;

public class FilesManager {
	private XMPPConnection connection;

    /**
     * Creates a new DissertationManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public FilesManager(XMPPConnection connection) {
        this.connection = connection;
    }
    
    /**
     * Ask server to get files
     *
     * @throws XMPPException if an error occurs creating the account.
     * @see #getAccountAttributes()
     */
    public List<File> getFiles(long pubID, EntryType entryType)throws XMPPException{
    	FilesCollection reg = new FilesCollection();
    	reg.setPubID(pubID);
    	reg.setEntryType(entryType);
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        FilesCollection result = (FilesCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
        // Stop queuing results
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        return result.getFiles();
    }
    /**
     * Ask server to get file
     *
     * @throws XMPPException if an error occurs creating the account.
     * @see #getAccountAttributes()
     */
    public File getFile(long fileID, EntryType entryType)throws XMPPException{
    	FileIQ reg = new FileIQ();
    	reg.setFileID(fileID);
    	reg.setEntryType(entryType);
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        FilesCollection result = (FilesCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
        // Stop queuing results
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        return result.getFiles().get(0);
    }
}
