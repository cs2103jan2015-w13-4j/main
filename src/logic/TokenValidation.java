import java.util.*;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class TokenValidation {
	private static int ARRAY_INDEX_TITLE=0;
	private static int ARRAY_INDEX_START_DATE=1;
	private static int ARRAY_INDEX_START_TIME=2;
	private static int ARRAY_INDEX_END_DATE=3;
	private static int ARRAY_INDEX_END_TIME=4;
	private static int ARRAY_INDEX_REPEAT=5;
	private static int ARRAY_INDEX_DELAYTYPE=6;
	private static int ARRAY_INDEX_RECUR=7;
	private static String[] inputArray=new String[8];
	//Calendar date=Calendar.getInstance();
	private static DateTimeFormatter dtf=DateTimeFormat.forPattern("dd/MM/yyyy");
	private static LocalDate inputLocalDate;
	private static LocalDate localDate=new LocalDate();
	private static LocalTime localTime=new LocalTime();
	
	public static boolean isTokenValid(StringTokenizer st){
		Task a= new Task();


		return false;
	}

	public static boolean isTitleValid(String title){
		if(!title.isEmpty()){
			inputArray[ARRAY_INDEX_TITLE]=title;
			return true;
		}
		
		return false;
	}
	
	public static boolean isStartDateValid(String startDate){
		inputLocalDate=dtf.parseLocalDate(startDate);
		if(startDate.isEmpty() || startDate.equalsIgnoreCase("d")){
			inputArray[ARRAY_INDEX_START_DATE]=localDate.toString();
			return true;
		}
		else if(!startDate.isEmpty()){
			inputArray[ARRAY_INDEX_START_DATE]=inputLocalDate.toString();	/////////////////////////// need to break
			return true;
		}
		return false;
	}
	
	public static boolean isStartTimeValid(String startTime){
		if(startTime.isEmpty() || startTime.equalsIgnoreCase("d")){
			inputArray[ARRAY_INDEX_START_TIME]=localTime.toString();
			return true;
		}
		else if(!startTime.isEmpty()){
			inputArray[ARRAY_INDEX_START_TIME]=startTime;	/////////////////////////// need to break
			return true;
		}
		return false;
	}
	
	public static boolean isEndDateValid(String endDate){
		inputLocalDate=dtf.parseLocalDate(endDate);
		if(endDate.isEmpty() || endDate.equalsIgnoreCase("d")){
			inputArray[ARRAY_INDEX_END_DATE]=localDate.toString();
			return true;
		}
		else if(!endDate.isEmpty()){
			inputArray[ARRAY_INDEX_END_DATE]=inputLocalDate.toString();
			return true;
		}
		return false;
	}
	public static boolean isEndTimeValidString(String endTime){
		if(endTime.isEmpty() || endTime.equalsIgnoreCase("d")){
			inputArray[ARRAY_INDEX_END_DATE]="2359";
			return true;
		}
		else if(!endTime.isEmpty()){
			inputArray[ARRAY_INDEX_END_TIME]=endTime;
			return true;
		}
		return false;
	}
	public static boolean isRepeatValid(String repeat){
		if (repeat.equalsIgnoreCase("n") || repeat.equalsIgnoreCase("r")) {
			inputArray[ARRAY_INDEX_REPEAT]=repeat;
			return true;
		}
		return false;
	}
	public static boolean isDelayTypeValid(String delayType){
		if (delayType.equalsIgnoreCase("d") || delayType.equalsIgnoreCase("w") || delayType.equalsIgnoreCase("m") || delayType.equalsIgnoreCase("y")){
			inputArray[ARRAY_INDEX_DELAYTYPE]=delayType;
			return true;
		}
		return false;
	}
	public static boolean isRecurValid(String recur){
		if (!recur.isEmpty()) {
			inputArray[ARRAY_INDEX_RECUR]=recur;
			return true;
		}
		return false;
	}

}
