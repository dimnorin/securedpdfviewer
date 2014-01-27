package org.jivesoftware.smack.znannya.pdf;

import java.util.Random;

public class DHClass {
	private long p 	= 180799207l;
	private long g	= 10787047l;
	private int a = getRandom(Integer.MAX_VALUE/2, Integer.MAX_VALUE); 
	private long K;
	
	public static final byte[] encArray = new byte[]{11,123,41,31,79,23,117};
	
	private static final int   NUMBER_OF_BITS_IN_A_BYTE = 8;
    private static final short MASK_TO_BYTE             = 0xFF;
    private static final int   SIZE_OF_AN_LONG_IN_BYTES  = 8;
		
    public static void main(String[] args){
    	DHClass dhClass = new DHClass();
    	long start = System.currentTimeMillis();
    	long res = dhClass.power(dhClass.g, Integer.MAX_VALUE, dhClass.p);
    	System.out.println("Time="+(System.currentTimeMillis()-start)+", val="+res);
//    	DHClass.writeLong(99194853094755497L);
    	DHClass.writeLong(1125899839733759L);
    }
    
    public static int getRandom(int min, int max){
    	// Example assumes these variables have been initialized
    	// above, e.g. as method parameters or otherwise
    	Random rand = new Random();

    	// nextInt is normally exclusive of the top value,
    	// so add 1 to make it inclusive
    	return rand.nextInt(max - min + 1) + min;
    }
    
	public long getA(){
		return power(g,a,p); 
	}
	
	public long getK(long B){
		K = power(B,a,p);
		return getK();
	}
	
	public long getK(){
		return K;
	}
	
	/**
     * Return x^n (mod p)
     * Assumes x, n >= 0, p > 0, x < p, 0^0 = 1
     * Overflow may occur if p > 31 bits.
     */
    private long power( long x, long n, long p )
    {
        if( n == 0 )
            return 1;

        long tmp = power( ( x * x ) % p, n / 2, p );

        if( n % 2 != 0 )
            tmp = ( tmp * x ) % p;

        return tmp;
    }

    public static byte[] writeLong(long p_toWrite )
    {
    	byte[] p_dest = new byte[8];
        // unrolled loop of 4 iterations
        p_dest[0] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;

        p_dest[1] = (byte)(p_toWrite & MASK_TO_BYTE) ;
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;

        p_dest[2] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;

        p_dest[3] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;
        
        p_dest[4] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;
        
        p_dest[5] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;
        
        p_dest[6] = (byte)(p_toWrite & MASK_TO_BYTE );
        p_toWrite >>= NUMBER_OF_BITS_IN_A_BYTE;
        
        p_dest[7] = (byte)(p_toWrite & MASK_TO_BYTE );
        for(int i = 0; i < 8 ; i++){
        	if (p_dest[i] == 0) p_dest[i] = p_dest[7-i];
        	if (p_dest[i] < 0) p_dest[i] *= -1;
        }
        return p_dest;
    }

       
    public int readLong( byte[] p_src ) // must be of size 8
    {
        // unrolled loop of 4 iterations
        int result = (   p_src[ 0 ] & MASK_TO_BYTE );
        result    |= ( ( p_src[ 1 ] & MASK_TO_BYTE ) << 8 );
        result    |= ( ( p_src[ 2 ] & MASK_TO_BYTE ) << 16 );
        result    |= ( ( p_src[ 3 ] & MASK_TO_BYTE ) << 24 );
        result    |= ( ( p_src[ 4 ] & MASK_TO_BYTE ) << 32 );
        result    |= ( ( p_src[ 5 ] & MASK_TO_BYTE ) << 40 );
        result    |= ( ( p_src[ 6 ] & MASK_TO_BYTE ) << 48 );
        result    |= ( ( p_src[ 7 ] & MASK_TO_BYTE ) << 56 );

        return result;
    }
	
}
