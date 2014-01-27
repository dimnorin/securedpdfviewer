package org.jivesoftware.smack.znannya.stats;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.znannya.Statistic;
import org.jivesoftware.smack.packet.znannya.StatsCollection;
import org.jivesoftware.smack.packet.znannya.UserInfo;
import org.jivesoftware.smack.packet.znannya.Statistic.Type;
import org.jivesoftware.smack.znannya.dao.BriefStatistics;
import org.jivesoftware.smack.znannya.dao.FullStatistics;
import org.jivesoftware.smack.znannya.pdf.DHEncoder;

public class StatisticsManager {

	private XMPPConnection connection;

    /**
     * Creates a new StatisticsManager instance.
     *
     * @param connection a connection to a XMPP server.
     */
    public StatisticsManager(XMPPConnection connection) {
        this.connection = connection;
    }
    
    private PacketCollector getStats(Type type, long from, long to)throws XMPPException{
    	Statistic reg = new Statistic(type, from, to);
        reg.setType(IQ.Type.GET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        return collector;
    }
    
    public BriefStatistics getBriefStatistics(long from, long to)throws XMPPException{
        PacketCollector collector = getStats(Type.brief, from, to);
        BriefStatistics result = (BriefStatistics)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
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
    
    public StatsCollection getFullStatistics(long from, long to)throws XMPPException{
        PacketCollector collector = getStats(Type.full, from, to);
        StatsCollection result = (StatsCollection)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
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

    /**
     * Set new user info
     * @param username
     * @param password - old password, if it is null, then newPass will not be set
     * @param newPassword - new password, if it is null or less 6 characters, then it will not be set
     * @param name
     * @param organization
     * @param position
     * @param phone
     * @param email
     * @param extraInfo
     * @return
     * @throws XMPPException
     */
    public UserInfo setUserInfo(String username, String password, String newPassword, String name, String organization, 
    		String position, String phone, String email, String extraInfo)throws XMPPException{
    	if(newPassword != null && newPassword.length() < 6)
    		throw new XMPPException(new XMPPError(XMPPError.Condition.not_allowed, "userdata.newpassword.incorrect"));
    	if(password != null && "".equals(password.trim()))
    		throw new XMPPException(new XMPPError(XMPPError.Condition.not_allowed, "userdata.password.incorrect"));
    	
    	DHEncoder encoder = new DHEncoder(connection.getKey());
    	String encodedPassword = password!=null?encoder.encode(password):null;
    	String encodedNewPassword = password!=null?encoder.encode(newPassword):null;
    	UserInfo reg = new UserInfo(encodedPassword, encodedNewPassword, name, organization, position, phone, email, extraInfo);
        reg.setType(IQ.Type.SET);
        reg.setTo(connection.getServiceName());
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(reg);
        UserInfo result = (UserInfo)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); 
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
}
