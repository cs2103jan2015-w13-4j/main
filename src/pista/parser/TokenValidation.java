package pista.parser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pista.Constants;


public class TokenValidation {

	private static String[] inputArray=new String[8];

	public static boolean isTitleValid(String title){
		if(!title.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	public static boolean isDateValid(String myDate){
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		Date testDate;
		
		sdf.setLenient(false);
		if(Constants.DEFAULT_VALUE.equalsIgnoreCase(myDate)){
			return true;
		}
		
		try {
			testDate = sdf.parse(myDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isTimeValid(String myTime){
		Pattern pattern;
		Matcher matcher;
		String TIME24HOURS_PATTERN ="([01]?[0-9]|2[0-3]):[0-5][0-9]";
		pattern = Pattern.compile(TIME24HOURS_PATTERN);
		matcher=pattern.matcher(myTime);
		if(matcher.matches() || Constants.DEFAULT_VALUE.equalsIgnoreCase(myTime)){
			return true;
		}
		return false;
	}
	
	public static boolean isStartDateBeforeThanEndDate(String start, String end, String startTime, String endTime){
		String sdst=start+" "+startTime;
		String edet=end +" "+endTime;
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
		sdf.setLenient(false);
		try {
			Date startDate=sdf.parse(sdst);
			Date endDate=sdf.parse(edet);
			if(startDate.before(endDate)){
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	

}
