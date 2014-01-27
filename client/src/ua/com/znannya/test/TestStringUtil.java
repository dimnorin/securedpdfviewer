package ua.com.znannya.test;

import javax.swing.JToolTip;

import ua.com.znannya.client.util.StringUtil;

public class TestStringUtil {

	
	public static void main(String[] args) {
		
		testSplitLongString();
		
	}
	
	
	public static void testSplitLongString(){
		
		String longString = "wvbjhwbfjrwj wbtfwibfwibwifyb wfeibwiuhfweih wihfiwuhfiwhifwhifw wufhnuwnfuwnwunfwinwinwiuwniwunwi jneogjnehngegheigheihgeihgi";
		System.out.println("longString: " + longString);
		String res = StringUtil.splitLongString(longString, longString.length(),20);
		System.out.println(res);
			
	}
	
	
}
