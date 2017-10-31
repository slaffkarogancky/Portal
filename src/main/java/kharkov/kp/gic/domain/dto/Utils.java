package kharkov.kp.gic.domain.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String nullifyIfNeed(String source) {
		if (source == null)
			return null;
		return source.trim().length() == 0 ? null : source.trim();
	}
	
	private static SimpleDateFormat _dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		try
		{
			return  _dateFormat.format(date);
		}
		catch(Exception e) {
			return "";
		}
	}
}
