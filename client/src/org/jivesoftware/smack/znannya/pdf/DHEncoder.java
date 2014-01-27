package org.jivesoftware.smack.znannya.pdf;

import org.jivesoftware.smack.util.Base64;

public class DHEncoder {
	private byte[] encSeq;
	private int currentIndex = 0;
	
    public DHEncoder(long key) {
    	encSeq = DHClass.writeLong(key);
	}

	public String encode(String str){
		currentIndex = 0;
		return Base64.encodeBytes(encode(str.getBytes()));
    }
	
	public String decode(String str){
		currentIndex = 0;
		return new String(encode(Base64.decode(str)));
    }
    
	private byte encode(byte i){
		if(encSeq != null && encSeq.length >0){
			i ^= encSeq[getNextIndex()];
		}
		return i;
	}
	
	private byte[] encode(byte[] in){
    	for (int i = 0; i < in.length; i++) {
    		in[i] = encode(in[i]);
		}
    	return in;
	}
	
	private int getNextIndex(){
		currentIndex  = (currentIndex + 1) % encSeq.length;
		return currentIndex;
	}
}
