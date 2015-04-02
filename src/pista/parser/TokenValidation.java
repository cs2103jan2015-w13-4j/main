package pista.parser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pista.Constants;


public class TokenValidation {

	private static String[] inputArray=new String[8];

	public static boolean isFileNameValid(String name){
		if(!name.isEmpty() && name.contains(Constants.SET_FILE_TYPE)){ //not empty and abc.xml
			return true;
		}
		return false;
	}
	
	public static boolean isTitleValid(String title){
		if(!title.isEmpty() || Constants.DEFAULT_IGNORE_VALUE.equalsIgnoreCase(title)){
			return true;
		}
		
		return false;
	}
	
	public static boolean isDateValid(String myDate){
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		Date testDate;
		
		sdf.setLenient(false);
		if(Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(myDate)){
			return true;
		}
		if(myDate.length()!=sdf.toPattern().length()){
			return false;
		}
		
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
		if(matcher.matches() || Constants.DEFAULT_CLEAR_VALUE.equalsIgnoreCase(myTime)){
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
	

	public static int compareWithCurrentDate(Long endMillisecond, Long alarmMillisecond){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Calendar cal = Calendar.getInstance();

		String currentDateTime = dateFormat.format(cal.getTime()); //2014/08/06 16:00:22
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			Date currentFormatDate = sdf.parse(currentDateTime);
			Long currentMillisecond = currentFormatDate.getTime();
			
			if (currentMillisecond > endMillisecond){ //exceed deadline (overdue)
				return 1; //current larger than input 			
			}
			
			if(alarmMillisecond == 0L){
				return -2;
			}
			
			if(currentMillisecond > alarmMillisecond && currentMillisecond < endMillisecond){
				return -1;
			}
			
			return -2; //task is still early
			
		} catch (ParseException e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
			return -2;
		}

	}
}
