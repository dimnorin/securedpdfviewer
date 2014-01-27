package org.jivesoftware.smack.znannya.pdf;

import java.io.ByteArrayInputStream;

public class DecryptionByteArrayInputStream extends ByteArrayInputStream {
	private byte[] encSeq;
	private int currentIndex = 0;
	
	public DecryptionByteArrayInputStream(byte[] buf, long key) {
		super(buf);
		encSeq = DHClass.writeLong(key);
	}
	
	/*{$inheritdoc}*/
	@Override
	public synchronized int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	/*{$inheritdoc}*/
	@Override
	public synchronized int read(byte b[], int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (pos >= count) {
			return -1;
		}
		if (pos + len > count) {
			len = count - pos;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		decode(b);
		pos += len;
		return len;
	}
	
	private byte decode(byte i){
		if(encSeq != null && encSeq.length >0){
			i ^= encSeq[getNextIndex()];
		}
		return i;
	}
	
	private void decode(byte[] in){
    	for (int i = 0; i < in.length; i++) {
    		in[i] = decode(in[i]);
		}
	}
	
	private int getNextIndex(){
		currentIndex  = (currentIndex + 1) % encSeq.length;
		return currentIndex;
	}
}
