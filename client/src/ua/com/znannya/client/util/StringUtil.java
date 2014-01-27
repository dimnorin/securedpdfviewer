package ua.com.znannya.client.util;

public class StringUtil {
	public static String decode(String in){
		char mod 	= 91;
		char upOut	= 122;
		char shift	= 43;
		
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			int x1 = in.charAt(i) + mod - shift;
			
			if(x1 > upOut) x1 = in.charAt(i) - shift;
			
			out.append((char)x1);
		}
		return out.toString();
	}
	
	public static String splitLongString(String longString, int parts, int partLength) {
		StringBuffer sb = new StringBuffer();
		int start = 0;
		for (int i = 1; i <= parts; i++) {
			int end = findClosestWordIndex(longString, i * partLength);
			sb.append(longString.substring(start, end).trim()).append('\n');
			start = end;
		}
		return sb.toString();
	}
	
	public static int findClosestWordIndex(String s, int startIndex)
    {
		if(s.length() <= startIndex) return startIndex;
		
        int wordAfterIndex = s.length();
        int wordBeforeIndex = -1;
        for (int i = startIndex; i < s.length(); i++)
        {
            if (s.charAt(i) == ' '  )
            {
                wordAfterIndex = i;
                break;
            }
        }
        for (int i = startIndex; i >= 0; i--)
        {
            if (s.charAt(i) == ' ')
            {
                wordBeforeIndex = i;
                break;
            }
        }

        if (wordAfterIndex - startIndex <= startIndex - wordBeforeIndex)
            return wordAfterIndex;
        else
            return wordBeforeIndex;
    }

	
	public static String convertTextToHTML(String text){
		
		String startTag = "<html>";
		String endTag = "</html>";
		String newLine = "<br>";
		String[] strs = text.split("\n");
		StringBuilder sb = new StringBuilder(startTag);
		
		for (int i = 0;i<strs.length;i++){
			sb.append(strs[i]);
			if(i != strs.length-1)
				sb.append(newLine);
		}
		sb.append(endTag);	
		return sb.toString();					
	}
	
    public static String toString(Object[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
            	return b.append(']').toString();
            b.append("\n");
        }
    }
}
