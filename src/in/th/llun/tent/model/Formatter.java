package in.th.llun.tent.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

	public static Date dateFromString(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
		    Locale.getDefault());
		return format.parse(time);
	}

	public static Date dateFromStringWithTimezone(String time)
	    throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(
		    "yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
		return format.parse(time);
	}

	public static String stringFromDateWithTimezone(Date date) {
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
		    Locale.getDefault());
		return pattern.format(date);
	}

}
