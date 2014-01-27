package ua.com.znannya.client.security;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.znannya.pdf.DecryptionInputStream;

import com.sun.jna.Native;
import com.sun.jna.Platform;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.util.ErrorUtil;
import ua.com.znannya.client.util.StringUtil;

public final class SecurityController {
	public static String nopillFileName;
	public static String fsxxFileName;
	public static String cbxxFileName;
	private static final String encNopillFileName = StringUtil.decode("Z41DZ>?@9<<Y41D"); ///dat/nopill.dat
	private static final String encFsxxFileName = StringUtil.decode("Z41DZ6CHHY41D"); ///dat/fsxx.dat
	private static final String encCbxxFileName = StringUtil.decode("Z41DZ32HHY41D"); ///dat/cbxx.dat

	private static NopillLibrary NOPILL_INSTANCE;
	private static FsxxLibrary FSXX_INSTANCE;
	private static CbxxLibrary CBXX_INSTANCE;
	
	private static Logger logger = Logger.getLogger(SecurityController.class.getName());
	
	static{
//		new ClipboardCleaner().start();
		/* Not work as frame has hided and this methosd can't get HWND
		 * Runtime.getRuntime().addShutdownHook(new Thread(){
	    	public void run(){
	    		SecurityController.stopPrevent();
	    	}
	    });*/
	}
	
	public static void ifAppCanBeStarted(boolean flag){
		if(flag){
			loadLibrariesFromJar();
			if(isVirtualEnvironment()){
	        	ErrorUtil.showError(StringUtil.decode("J>Y31>DYC8?GY9>YF9BDE1<")); //zn.cant.show.in.virtual
	//        }else if(RadminDetector.isRadminDetected()){
	//        	ErrorUtil.showError(StringUtil.decode("J>YB14=9>Y9CY45D53D54")); //zn.radmin.is.detected
	        }else if(!startPrevent()){
	        	ErrorUtil.showError(StringUtil.decode("J>Y31>DYCD1BD")); //zn.cant.start
	        }else{
	        	return ;
	        }
			askToExit();
		}else{
			stopPrevent();
		}
	}
	
	private static void askToExit(){
		EventQueueController.pushEvent(201);
	}
	
	private static boolean isVirtualEnvironment(){
		try{
//			System.out.println("nopillFileName: " + nopillFileName);
			int ind = nopillFileName.indexOf('.');
			String filfn = nopillFileName.substring(0, ind);
//			System.out.println("nopillFileName: " + filfn);
			
			if(NOPILL_INSTANCE == null)
				NOPILL_INSTANCE = (NopillLibrary)Native.loadLibrary(filfn, NopillLibrary.class);
			return NOPILL_INSTANCE.isVirtualEnv();

		}catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
			throw new RuntimeException();
		}
	}
	
	private static boolean startPrevent(){
		com.sun.jna.platform.win32.W32API.HWND hwnd = getHWnd(ZnclApplication.getApplication().getMainFrame());
		
		if(FSXX_INSTANCE == null)
			FSXX_INSTANCE = (FsxxLibrary)Native.loadLibrary(fsxxFileName, FsxxLibrary.class);
		if(CBXX_INSTANCE == null)
			CBXX_INSTANCE = (CbxxLibrary)Native.loadLibrary(cbxxFileName, CbxxLibrary.class);
		boolean cbxxStatus = true;
		boolean fsxxStatus = true;
//		cbxxStatus = CBXX_INSTANCE.StartstoP(hwnd, true);
//		fsxxStatus = FSXX_INSTANCE.StartstoP(hwnd, true);
		logger.log(Level.INFO, "Started prevent: cbxx="+cbxxStatus+", fsxx="+fsxxStatus);
		return fsxxStatus && cbxxStatus;
//		return true;
	}
	
	private static boolean stopPrevent(){
		com.sun.jna.platform.win32.W32API.HWND hwnd = getHWnd(ZnclApplication.getApplication().getMainFrame());
		
		boolean cbxxStatus = true;
		boolean fsxxStatus = true;
//		cbxxStatus = CBXX_INSTANCE.StartstoP(hwnd, false);
//		fsxxStatus = FSXX_INSTANCE.StartstoP(hwnd, false);
		logger.log(Level.INFO, "Stoped prevent: cbxx="+cbxxStatus+", fsxx="+fsxxStatus);
		return fsxxStatus && cbxxStatus;
//		return true;
	}
	
	private static com.sun.jna.platform.win32.W32API.HWND getHWnd(Component w)
    {
		com.sun.jna.platform.win32.W32API.HWND hwnd = new com.sun.jna.platform.win32.W32API.HWND();
        hwnd.setPointer(Native.getComponentPointer(w));
        return hwnd;
    }
	
	private static void loadLibrariesFromJar(){
		removePrevLibFiles();
		/*
		String tempFolder = System.getProperty(StringUtil.decode(":1F1Y9?YD=@49B")); //java.io.tmpdir
		long rand = (long)(Long.MAX_VALUE * Math.random());
		nopillFileName = tempFolder + rand + StringUtil.decode("Y4<<");//".dll";
		rand = (long)(Long.MAX_VALUE * Math.random());
		fsxxFileName = tempFolder + rand + StringUtil.decode("Y4<<");//".dll";
*/		
		nopillFileName = writeFile(readFile(encNopillFileName));
		fsxxFileName = writeFile(readFile(encFsxxFileName));
		cbxxFileName = writeFile(readFile(encCbxxFileName));
		
		Native.setProtected(true);
	}
	
	private static void removePrevLibFiles(){
		File folder = new File(System.getProperty(StringUtil.decode(":1F1Y9?YD=@49B"))); //java.io.tmpdir
		File[] filesToDelete = folder.listFiles(new FileFilter() {
			
			private long[] sizesArr = new long[] {6656, 8192, 8704, 349255}; //array must be sorted
			
			public boolean accept(File pathname) {
				if(pathname.isFile() && (Arrays.binarySearch(sizesArr, pathname.length()) > -1))
					return true;
				return false;
			}
		});
		
		for(File file : filesToDelete){
			file.delete();
		}
	}
	
	private static byte[] readFile(String fileName){
		try {
			InputStream is = new DecryptionInputStream(ZnclApplication.class.getResourceAsStream(fileName), 99194853094755497L);
//			InputStream is = new FileInputStream(fileName);
			byte[] res = new byte[is.available()];
			is.read(res);
			is.close();
			return res;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
			throw new RuntimeException();
		}
	}
	
	private static String writeFile(byte[] content){
		try {
			File lib = File.createTempFile(StringUtil.decode(":>1")/*"jna"*/, Platform.isWindows() ? StringUtil.decode("Y4<<") /*".dll"*/ : null);
            //File lib = File.createTempFile("jna",".dll");
			lib.deleteOnExit();
            ClassLoader cl = (com.sun.jna.Native.class).getClassLoader();
            if(Platform.deleteNativeLibraryAfterVMExit() && (cl == null || cl.equals(ClassLoader.getSystemClassLoader())))
                Runtime.getRuntime().addShutdownHook(new Native.DeleteNativeLibrary(lib));
			
			FileOutputStream fos = new FileOutputStream(lib);
			fos.write(content);
			fos.close();
			
			return lib.getAbsolutePath();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
			throw new RuntimeException();
		}
	}
}
