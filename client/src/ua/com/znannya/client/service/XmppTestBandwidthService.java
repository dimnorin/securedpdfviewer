package ua.com.znannya.client.service;


import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.io.InputStream;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smack.znannya.pdf.DecryptionByteArrayInputStream;

import ua.com.znannya.client.app.XmppConnector;


public class XmppTestBandwidthService {

	
	public XmppTestBandwidthService() {
		// TODO Auto-generated constructor stub
	}
	
	public float testUploadRate(){
		
		int size = 5000;
		float time = 0.0f;
		try {
			time = sendTestPacket(size);
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (time!=0.0f)
			return size/time;
		else
		return 0;
	}
	
	public float testDownloadRate(){
		float rate = 0.0f;
		
		return rate;
	}
	
	public float testBandwith(){
		
        float rate = 0.0f;
		
		return rate;
		
	}
	
	/**
	 * This method return time to send a test packet in sec.
	 * @param size - the size of sending packet (in KBytes)
	 * @return - time to sending packet(in sec.)
	 * @throws XMPPException 
	 */
	private float sendTestPacket(int size) throws XMPPException, IOException{
				
		// create a content
		int nbytes = size*1024; // in Bytes		
		byte[] buffer = new byte[nbytes];
		for (int i = 0;i<nbytes;i++)
			buffer[i] = (byte)(Math.random()*(byte)127);
		
		// create a test packet
		Message test = new Message();
		XMPPConnection connection =  XmppConnector.getInstance().getConnection();
		test.setTo(connection.getServiceName());
		test.setBody(buffer.toString());
		test.setType(Message.Type.normal);
		test.setSubject("this is a test packet");
		
		PacketFilter filter = new MessageTypeFilter(test.getType());
		PacketCollector collector = connection.createPacketCollector(filter);
		
		// send
		long t1 = System.currentTimeMillis();
		connection.sendPacket(test);
		
				
		Message result = (Message) collector.nextResult(10000);
		// Stop queuing results
		collector.cancel();
		if (result == null) 
			throw new XMPPException("No response from server.");
		//} else if (result.getType()== IQ.Type.ERROR) {
		//	throw new XMPPException(result.getError());
		//}
		/*
		InputStream is = new DecryptionByteArrayInputStream(Base64.decode(result.getBody()), connection.getKey());
		int cnt = 0;
		byte[] tmp = new byte[1024];
		
		while(true){
			cnt = is.read(tmp);
			System.out.println(tmp.toString());
			if (cnt==-1) break;
		}
		*/
		long t2 = System.currentTimeMillis();
		System.out.println("t2-t1 = " + (t2-t1) + "millisec.");
		System.out.println(result.toString());
		System.out.println(result.getType());
		System.out.println(result.getBody());
		
		// compute the time
		float sec = (float)((t2-t1)/1000.0f);
		
	    return sec;		
	}
	
 }
	
