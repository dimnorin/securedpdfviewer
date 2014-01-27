package org.jivesoftware.smack.znannya.pdf;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.DH;

public class DHManager {
	private XMPPConnection connection;
	
	 /**
    * Creates a new InitialLoadManager instance.
    *
    * @param connection a connection to a XMPP server.
    */
   public DHManager(XMPPConnection connection) {
       this.connection = connection;
   }
   
   /**
    * Ask server to get encryption key. This key will be used for decryption. Will be used inside input stream. 
    * @throws XMPPException
    */
   public long getKey() throws XMPPException {
	   DHClass dhClass = new DHClass();
       DH reg = new DH(dhClass.getA());
       reg.setType(IQ.Type.GET);
       reg.setTo(connection.getServiceName());
       PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
               new PacketTypeFilter(IQ.class));
       PacketCollector collector = connection.createPacketCollector(filter);
       connection.sendPacket(reg);
       DH result = (DH)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
       // Stop queuing results
       collector.cancel();
       if (result == null) {
           throw new XMPPException("No response from server.");
       }
       else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
       }
       return dhClass.getK(result.getB());
   }
}
