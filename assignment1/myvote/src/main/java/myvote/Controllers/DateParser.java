package myvote.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
	public String getDate(){
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss.S'Z'");
	    String returnDate = ft.format(dNow);
	    return returnDate;
	}
}
