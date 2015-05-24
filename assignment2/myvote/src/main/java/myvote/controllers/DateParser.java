package myvote.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
	private static String format = "yyyy-MM-dd'T'hh:mm:ss.S'Z'";
	public String getDate(){
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat (format);
	    String returnDate = ft.format(dNow);
	    return returnDate;
	}
	public Date getDateTime(String dateString) throws Exception{
		SimpleDateFormat ft = new SimpleDateFormat (format);
		Date date = ft.parse(dateString);
		return date;
	}
}

