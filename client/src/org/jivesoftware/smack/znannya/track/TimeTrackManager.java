package org.jivesoftware.smack.znannya.track;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ZnConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.znannya.Balance;
import org.jivesoftware.smack.znannya.dao.EntryType;

import ua.com.znannya.client.ctrl.DocumentsPaneController;

public class TimeTrackManager {

	private XMPPConnection connection;
	/**
	 * Listen for error messages while tracking updates
	 */
	private PacketListener errorPacketListener;
	/**
	 * Listen for update balance packets
	 */
	private PacketListener updatePacketListener;
	
	/**
	 * Listeners which should be noticed when document showing must be stoped
	 */
	private static List<ITrackUpdatesListener> trackListeners = new ArrayList<ITrackUpdatesListener>();
	
	private static PdfFileViewTrackTask trackTask;
	
	private static Timer timer = new Timer("timer-client", true);
	
	private Logger logger = Logger.getLogger(TimeTrackManager.class.getName());
	
	 /**
    * Creates a new TimeTrackManager instance.
    *
    * @param connection a connection to a XMPP server.
    */
   public TimeTrackManager(XMPPConnection connection) {
       this.connection = connection;
   }
   
   public String getBalance() throws XMPPException {
	   Balance req = new Balance(TimeTrack.Type.get);
	   req.setType(IQ.Type.GET);
	   req.setTo(connection.getServiceName());
       PacketFilter filter = new AndFilter(new PacketIDFilter(req.getPacketID()),
               new PacketTypeFilter(IQ.class));
       PacketCollector collector = connection.createPacketCollector(filter);
       connection.sendPacket(req);
       Balance result = (Balance)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
       // Stop queuing results
       collector.cancel();
       if (result == null) {
           throw new XMPPException("No response from server.");
       }
       else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
       }
       
       logger.log(Level.INFO, "Get balance saccessfull, balance="+result.getBalance());
       return result.getBalance();
   }
   
   /**
    * Ask server to start time tracking. If this method completes without exception, then client can start showing documents.
    * @throws XMPPException - exception if document showing is not allowed
    * @return balance
    */
   public String startTrack(long fileID, EntryType entryType) throws XMPPException {
	   Balance req = new Balance(TimeTrack.Type.start, fileID, entryType);
	   req.setType(IQ.Type.GET);
	   req.setTo(connection.getServiceName());
       PacketFilter filter = new AndFilter(new PacketIDFilter(req.getPacketID()),
               new PacketTypeFilter(IQ.class));
       PacketCollector collector = connection.createPacketCollector(filter);
       connection.sendPacket(req);
       Balance result = (Balance)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
       // Stop queuing results
       collector.cancel();
       if (result == null) {
           throw new XMPPException("No response from server.");
       }
       else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
       }
       
       int delay = Integer.parseInt(ZnConfiguration.get(ZnConfiguration.TTRACK_DELAY));
       int minute = 1000 * 60;
       int period = delay * minute;
       // stop last task and start new
       if(trackTask != null){
    	   trackTask.cancel();
       }
       trackTask = new PdfFileViewTrackTask();
       timer.scheduleAtFixedRate(trackTask, period, period);
       
//       registerErrorPacketListener(filter); // not needed as we register error listener for all error, not only ttrack related 
       
       logger.log(Level.INFO, "Start was saccessfull, balance="+result.getBalance());
       return result.getBalance();
   }
   
   /**
    * Stop time tracking 
    * @throws XMPPException
    * @return balance
    */
   public String stopTrack() throws XMPPException {
	   if(trackTask != null){
		   trackTask.cancel();
		   trackTask = null;
	   }
	   
	   Balance req = new Balance(TimeTrack.Type.stop);
	   req.setType(IQ.Type.GET);
	   req.setTo(connection.getServiceName());
       PacketFilter filter = new AndFilter(new PacketIDFilter(req.getPacketID()),
               new PacketTypeFilter(IQ.class));
       PacketCollector collector = connection.createPacketCollector(filter);
       connection.sendPacket(req);
       Balance result = (Balance)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
       // Stop queuing results
       collector.cancel();
       if (result == null) {
           throw new XMPPException("No response from server.");
       }
       else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
       }
       
//       unregisterErrorPacketListener(); // do not unregister, as we can't see errors on gui
       
       logger.log(Level.INFO, "Stop was seccessfull, balance="+result.getBalance());
       return result.getBalance();
   }
   
   /**
    * Update server with track status
    * @throws XMPPException
    * @return balance
    */
   private String updateTrack() throws XMPPException {
	   if(trackTask == null) throw new XMPPException("Timer task is null");
	   
	   Balance req = new Balance(TimeTrack.Type.update);
	   req.setType(IQ.Type.GET);
	   req.setTo(connection.getServiceName());
       PacketFilter filter = new AndFilter(new PacketIDFilter(req.getPacketID()),
               new PacketTypeFilter(IQ.class));
       PacketCollector collector = connection.createPacketCollector(filter);
       connection.sendPacket(req);
       Balance result = (Balance)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
       // Stop queuing results
       collector.cancel();
       if (result == null) {
           throw new XMPPException("No response from server.");
       }
       else if (result.getType() == IQ.Type.ERROR) {
           throw new XMPPException(result.getError());
       }
       logger.log(Level.INFO, "Update was seccessfull, balance="+result.getBalance());
       return result.getBalance();
   }
   
   /*public void registerErrorPacketListener(){
	   registerErrorPacketListener(null);
   }*/
   
   public void registerErrorPacketListener(/*PacketFilter filter*/){
	   unregisterErrorPacketListener();
	   
	  
	   PacketFilter errFilter = new PacketFilter() {
		
		   public boolean accept(Packet packet) {
				return packet.getError() != null;
			}
	   };
	   
	   PacketFilter andFilter = null;
	   /*if(filter != null)
		   andFilter = new AndFilter(filter, errFilter);
	   else*/
		   andFilter = errFilter;
	   
	   errorPacketListener = new PacketListener() {
	        public void processPacket(Packet packet) {
	        	System.out.println("Packet error listener got packet:"+packet);
            	gotError(packet.getError());
	        }
	    };
	    // Register the listener.
	    connection.addPacketListener(errorPacketListener, andFilter);
   }
   
   private void unregisterErrorPacketListener(){
	   if(errorPacketListener != null)
		   connection.removePacketListener(errorPacketListener);
   }
   
   public void registerUpdatePacketListener(){
	   unregisterUpdatePacketListener();
	   
	   PacketFilter updateFilter = new AndFilter(new PacketTypeFilter(Balance.class), new PacketFilter() {
			
		   public boolean accept(Packet packet) {
				if(packet instanceof Balance){
					Balance balance = (Balance)packet;
					if(balance.getAction() == TimeTrack.Type.update)
						return true;
				}
				return false;	
			}
	   });
	   
	   updatePacketListener = new PacketListener() {
	        public void processPacket(Packet packet) {
	        	System.out.println("Packet update listener got packet:"+packet);
	        	gotBalanceUpdate(((Balance)packet).getBalance());
	        }
	    };
	    // Register the listener.
	    connection.addPacketListener(updatePacketListener, updateFilter);
   }
   
   private void unregisterUpdatePacketListener(){
	   if(updatePacketListener != null)
		   connection.removePacketListener(updatePacketListener);
   }
   
   public static void addListener(ITrackUpdatesListener listener){
	   trackListeners.add(listener);
   }
   
   public static void removeListener(ITrackUpdatesListener listener){
	   trackListeners.remove(listener);
   }
   
   public static void removeAllListeners(){
	   trackListeners.clear();
   }
   
   private static void gotError(XMPPError error){
	   for(ITrackUpdatesListener listener : trackListeners){
		   listener.gotError(error);
	   }
   }
   
   private static void gotBalanceUpdate(String balance){
	   for(ITrackUpdatesListener listener : trackListeners){
		   listener.gotBalanceUpdate(balance);
	   }
   }

	private class PdfFileViewTrackTask extends TimerTask {
		@Override
		public void run() {
			try {
				String balance = updateTrack();
				gotBalanceUpdate(balance);
			} catch (XMPPException e) {
				gotError(e.getXMPPError());
				cancel();
			} catch (Exception e) {
				gotError(null);
				cancel();
			}
		}
	}
}
