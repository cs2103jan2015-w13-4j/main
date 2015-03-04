import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.joda.time.*;
import org.joda.time.format.*;



public class AddTask {
	private static final int ARRAY_INDEX_TITLE=0;
	private static final int ARRAY_INDEX_START_DATE=1;
	private static final int ARRAY_INDEX_START_TIME=2;
	private static final int ARRAY_INDEX_END_DATE=3;
	private static final int ARRAY_INDEX_END_TIME=4;
	private static final int ARRAY_INDEX_REPEAT=5;
	private static final int ARRAY_INDEX_DELAYTYPE=6;
	private static final int ARRAY_INDEX_RECUR=7;
	private static int ARRAY_INDEX_START_MILLISECONDS=8;;
	private static int ARRAY_INDEX_END_MILLISECONDS=9;;
	private static final int ONE_DAY=1;
	private static final int ONE_WEEK=1;
	private static final int ONE_MONTH=1;
	private static final int ONE_YEAR=1;
	private static final int DELAY_DURATION_ONE=1;

	//DateTime today=new DateTime();	//jodatime
	//private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private static DateTimeFormatter dtf=DateTimeFormat.forPattern("dd/MM/yyyy");
	private static DateTimeFormatter dtf2=DateTimeFormat.forPattern("dd/MM/yyyy HH:MM");
	//private  static DateTime laterDate;

	
	public static void dtFormat(String delayType, String[] inputArray, DateTime startDate, DateTime endDate){
		
		if(delayType.equalsIgnoreCase("d")){
			startDate.plusDays(DELAY_DURATION_ONE);
			endDate.plusDays(DELAY_DURATION_ONE);
			
		}
		else if(delayType.equalsIgnoreCase("w")){
			startDate.plusWeeks(DELAY_DURATION_ONE);
			endDate.plusWeeks(DELAY_DURATION_ONE);
		}
		else if(delayType.equalsIgnoreCase("m")){
			startDate.plusMonths(DELAY_DURATION_ONE);
			endDate.plusMonths(DELAY_DURATION_ONE);
		}
		else if(delayType.equalsIgnoreCase("y")){
			startDate.plusYears(DELAY_DURATION_ONE);
			endDate.plusYears(DELAY_DURATION_ONE);
		}
		
		inputArray[ARRAY_INDEX_START_DATE]=startDate.toString();
		inputArray[ARRAY_INDEX_END_DATE]=endDate.toString();
		inputArray[ARRAY_INDEX_START_MILLISECONDS]=String.valueOf(startDate.getMillis());
		inputArray[ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(endDate.getMillis());
	}
	
	public static String add(String[] inputArray){
		Task newTask;
		DateTime startDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_START_DATE]);
		DateTime endDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_END_DATE]);
		
		
		newTask=new Task(inputArray);
		///////////// add to hashmap
		
		if(inputArray[ARRAY_INDEX_REPEAT].equalsIgnoreCase("r")){
			String delayType=inputArray[ARRAY_INDEX_DELAYTYPE];
			int recur=Integer.parseInt(inputArray[ARRAY_INDEX_RECUR]);
			while(recur>0){
				dtFormat(delayType, inputArray, startDate, endDate);
				newTask=new Task(inputArray);
				///////////// add to hashmap
			}
		}
		return String.format(Message.MSG_SUCCESS, newTask.getTitle());
	}
}
