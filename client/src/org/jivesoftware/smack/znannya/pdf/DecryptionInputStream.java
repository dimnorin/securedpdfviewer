package org.jivesoftware.smack.znannya.pdf;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecryptionInputStream extends InputStream{
	private InputStream in;
	
	private byte[] encSeq;
	private int currentIndex = 0;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public DecryptionInputStream(InputStream is, long key){
		encSeq = DHClass.writeLong(key);
		
		BufferedInputStream bis = new BufferedInputStream(is, 1024 * 2);
		
		byte[] arr = new byte[0];
		try{
			arr = new byte[bis.available()];
			bis.read(arr);
//			writeToFile("readed", arr);
			decode(arr);
//			writeToFile("decoded", arr);
		}catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
		in = new ByteArrayInputStream(arr); 
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int read(byte abyte0[], int i, int j) throws IOException {
		return in.read(abyte0, i, j);
	}

	@Override
	public int available() throws IOException{
		return in.available();
	}
	
	private synchronized byte decode(byte i){
		if(encSeq != null && encSeq.length >0){
			i ^= encSeq[getNextIndex()];
		}
		return i;
	}
	
	private synchronized void decode(byte[] in){
    	for (int i = 0; i < in.length; i++) {
    		in[i] = decode(in[i]);
		}
	}
	
	private synchronized int getNextIndex(){
		currentIndex  = (currentIndex + 1) % encSeq.length;
		return currentIndex;
	}
	
	private void writeToFile(String fileName, byte[] arr){
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			
			fos.write(arr);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
