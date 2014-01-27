package ua.com.znannya.client.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class RadminDetector {

	public static void main(String[] args){
		System.out.println(isRadminDetected());
	}
	
	public static boolean isRadminDetected(){
		return isRadminFilesDetected() || isRadminProcessDetected() || isRadminRegistryDetected();
	}
	
	private static boolean isRadminProcessDetected(){
		try{
			Process proc = Runtime.getRuntime().exec("tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			if (0 == proc.waitFor ()) {
				String line = null;
		        while ((line = input.readLine()) != null) {
		            if(line.toLowerCase().contains("radmin")) return true;
		        }
		        input.close();
//			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean isRadminRegistryDetected(){
		String radmin = readRegistry("HKEY_LOCAL_MACHINE\\system\\radmin", null);
		String r_server = readRegistry("HKEY_LOCAL_MACHINE\\system\\currentcontrolset\\services\\r_server", null);
		String legacy_r_server = readRegistry("HKEY_LOCAL_MACHINE\\system\\currentcontrolset\\enum\\root\\legacy_r_server", null);
		return radmin != null || r_server != null || legacy_r_server != null;
	}
	
	private static boolean isRadminFilesDetected(){
		String windir = System.getenv("WINDIR");
		File raddrv = new File(windir, "raddrv.dll");	
		File r_server = new File(windir, "r_server.exe");
		return raddrv.exists() || r_server.exists();
	}
	
	/**
     * 
     * @param location path in the registry
     * @param key registry key
     * @return registry value or null if not found
     */
    private static final String readRegistry(String location, String key){
        try {
            // Run reg query, then read output with StreamReader (internal class) 
            Process process = Runtime.getRuntime().exec("reg query " + 
                    '"'+ location + "\""+ (key!=null?" /v " + key:""));

            StreamReader reader = new StreamReader(process.getInputStream());
            process.waitFor();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
            if( ! output.contains("\t")){
                    return null;
            }

            // Parse out the value
            String[] parsed = output.split("\t");
            return parsed[parsed.length-1];
        }
        catch (Exception e) {
            return null;
        }

    }
    
    private static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();;

        public StreamReader(InputStream is) {
            this.is = is;
        }

        private void startRead() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) { }
        }

        public String getResult() {
        	startRead();
            return sw.toString();
        }
    }
}