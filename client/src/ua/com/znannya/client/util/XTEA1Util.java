package ua.com.znannya.client.util;

public class XTEA1Util {
	private static final int[] key = {15485863, 32452843, 49979687, 67867967}; 
	
	// Encryption is commented now, as it not used in client
	/*public static String encrypt(int id){
		int[] v = new int[] {0, id};
		xtea1_encipher(v, key);
		return a2s(v);
	}*/
	
	public static int decrypt(String s){
		int[] v = s2a(s);
		xtea1_decipher(v, key);
		return v[1];
	}
	
	
	private static int rol(int base, int shift){
		int res;
	    /* only 5 bits of shift are significant*/
        shift &= 0x1F;
        res = (base << shift) | (base >> (32 - shift));
        return res;
	}
	 
	/**
	 * XTEA-1 Encryptor
	 * @param v — исходный текст состоящий из двух слов по 32 бита
	 * @param k — ключ состоящий из четырех 32-битных слов
	 */
	private static void xtea1_encipher(int[] v, int[] k){
		int num_rounds = 32; 
		int i;
		int y, z, sum=0, delta=0x9E3779B9;
		/* load and pre-white the registers */
		y = v[0] + k[0];
		z = v[1] + k[1];
		/* Round functions */
		for (i = 0; i < num_rounds; i++) 
		{
			y += ((z << 4) ^ (z >> 5)) + (z ^ sum) + rol(k[sum & 3], z);
			sum += delta;
			z += ((y << 4) ^ (y >> 5)) + (y ^ sum) + rol(k[(sum >> 11) & 3], y);
		}
		/* post-white and store registers */
		v[0] = y ^ k[2];
		v[1] = z ^ k[3];
	}
	 
	private static void xtea1_decipher(int[] v, int[] k)
	{
		int num_rounds = 32;
		int i;
		int y, z,delta=0x9E3779B9,sum=delta*num_rounds;
		z = v[1] ^ k[3];
		y = v[0] ^ k[2];
		for (i = 0; i < num_rounds; i++) 
		{
			z -= ((y << 4) ^ (y >> 5)) + (y ^ sum) + rol(k[(sum >> 11) & 3], y);
			sum -= delta;
			y -= ((z << 4) ^ (z >> 5)) + (z ^ sum) + rol(k[sum & 3], z);
	 
		}
		v[1] = z - k[1];
		v[0] = y - k[0];
	}

	
/*	private static String a2s(int[] v){
		StringBuilder sb = new StringBuilder();
		for(int v1 : v){
			if(v1 < 0){
				sb.append("M");
				v1 = - v1;
			}
			sb.append(v1).append("R");
		}
		sb.deleteCharAt(sb.length()-1); // delete last R
		return sb.toString();
	}*/
	
	private static int[] s2a(String s) {
		s = s.toUpperCase();
		String[] parts = s.split("R");
		int[] v = new int[parts.length];
		for (int i = 0; i < v.length; i++) {
			int mIdx = parts[i].indexOf("M");
			if (mIdx > -1) {
				parts[i] = parts[i].substring(mIdx + 1);
				v[i] = -Integer.parseInt(parts[i]);
			} else {
				v[i] = Integer.parseInt(parts[i]);
			}
		}
		return v;
	}
	
	public static void main(String[] args){
		xtea1_encipher(new int[]{0, 1}, key);
	}
}
