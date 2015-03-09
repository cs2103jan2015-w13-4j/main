package logic;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class TokenValidation {

	private static String[] inputArray=new String[8];
	//Calendar date=Calendar.getInstance();
	private static DateTimeFormatter dtf=DateTimeFormat.forPattern("dd/MM/yyyy");
	private static LocalDate inputLocalDate;
	private static LocalDate localDate=new LocalDate();
	private static LocalTime localTime=new LocalTime();
	


	public static boolean isTitleValid(String title){
		if(!title.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	public static boolean isDateValid(String myDate){
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Date testDate;
		try {
			testDate = sdf.parse(myDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static boolean isTimeValid(String myTime){
		Pattern pattern;
		Matcher matcher;
		String TIME24HOURS_PATTERN ="([01]?[0-9]|2[0-3]):[0-5][0-9]";
		pattern = Pattern.compile(TIME24HOURS_PATTERN);
		matcher=pattern.matcher(myTime);
		if(matcher.matches()){
			return true;
		}
		return false;
	}
	
	public static boolean isRepeatValid(String repeat){
		if (repeat.equalsIgnoreCase("n") || repeat.equalsIgnoreCase("r")) {
			inputArray[Constants.ARRAY_INDEX_REPEAT]=repeat;
			return true;
		}
		return false;
	}
	public static boolean isDelayTypeValid(String delayType){
		if (delayType.equalsIgnoreCase("d") || delayType.equalsIgnoreCase("w") || delayType.equalsIgnoreCase("m") || delayType.equalsIgnoreCase("y")){
			inputArray[Constants.ARRAY_INDEX_DELAYTYPE]=delayType;
			return true;
		}
		return false;
	}
	public static boolean isRecurValid(String recur){
		if (!recur.isEmpty()) {
			inputArray[Constants.ARRAY_INDEX_RECUR]=recur;
			return true;
		}
		return false;
	}

}
