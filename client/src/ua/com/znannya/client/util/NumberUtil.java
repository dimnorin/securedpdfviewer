package ua.com.znannya.client.util;

import java.text.NumberFormat;

public class NumberUtil {

	public static String formatFloat(float number,int fractionDigits) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(fractionDigits);
        nf.setMaximumFractionDigits(fractionDigits);           
        return nf.format(number);
    }
	
	public static String formatLong(long number,int fractionDigits) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(fractionDigits);
        nf.setMaximumFractionDigits(fractionDigits);           
        return nf.format(number);
    }
	
	
	
}
