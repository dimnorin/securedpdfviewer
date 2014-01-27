package org.jivesoftware.smack.znannya.feedback;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.znannya.Feedback;

public class FeedbackManager {
	private XMPPConnection connection;

    /**
     * Creates a new FeedbackManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public FeedbackManager(XMPPConnection connection) {
        this.connection = connection;
    }
    
    public void sendFeedback(String description, String failurePoint, String log){
    	Feedback reg = new Feedback(description, failurePoint, log);
        reg.setType(IQ.Type.SET);
        reg.setTo(connection.getServiceName());
        connection.sendPacket(reg);
    }
}
