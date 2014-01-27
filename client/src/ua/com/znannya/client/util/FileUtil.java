package ua.com.znannya.client.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	public static String loadFile(InputStream is)throws IOException{
		StringBuilder sb = new StringBuilder();
		DataInputStream dis = new DataInputStream(is);
		String line = "";
		while((line = dis.readLine())!=null)
			sb.append(line).append('\n');
		return sb.toString();
	}
	
	public static void copy(InputStream in, File destFile) throws IOException{
		OutputStream out = new FileOutputStream(destFile);
		byte[] buffer = new byte[1024]; 
		int len = in.read(buffer); 
		while (len != -1) { 
		    out.write(buffer, 0, len); 
		    len = in.read(buffer); 
		} 
		out.close();
	}
}
