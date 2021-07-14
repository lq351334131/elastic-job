package com.etocrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat nyr = new SimpleDateFormat("yyyy-MM-dd");

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static String getDateTme(String timeformat) {
		Date now=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat(timeformat);
		return dateFormat.format(now);
		
	}
    
    public static String nyr(){
        String res;
        Date date = new Date();
        res = nyr.format(date);
        return res;
    }

}
