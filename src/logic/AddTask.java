package logic;
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


	private static DateTimeFormatter dtf=DateTimeFormat.forPattern("dd/MM/yyyy");
	private static DateTimeFormatter dtf2=DateTimeFormat.forPattern("dd/MM/yyyy HH:MM");
	//private static SimpleDateFormat stf=new SimpleDateFormat();

	
	public static void dtFormat(String delayType, String[] inputArray, String startDate, String endDate){
	
		/*if(delayType.equalsIgnoreCase("d")){
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
		}*/
		
//		String combine=startDate+" "+inputArray[ARRAY_INDEX_START_TIME];
//		String combine2=endDate+" "+inputArray[ARRAY_INDEX_END_TIME];
//		DateTime startDate2=dtf2.parseDateTime(combine);
//		DateTime endDate2=dtf2.parseDateTime(combine2);
		inputArray[ARRAY_INDEX_START_MILLISECONDS]=String.valueOf(convertToMillisecond(startDate, inputArray[ARRAY_INDEX_START_TIME]));
		inputArray[ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(convertToMillisecond(endDate, inputArray[ARRAY_INDEX_END_TIME]));
	}
	
	public static String add(String[] inputArray){
		Task newTask;
		//DateTime startDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_START_DATE]);
		//DateTime endDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_END_DATE]);
		
		System.out.println("the length is"+inputArray.length);
		for (int i = 0; i < inputArray.length; i++) {
			System.out.println("what is inside "+inputArray[i]);
		}
		String[] newArray=new String[10];
		System.arraycopy(inputArray, 0, newArray, 0, inputArray.length);
		dtFormat(newArray[ARRAY_INDEX_REPEAT], newArray, newArray[ARRAY_INDEX_START_DATE], newArray[ARRAY_INDEX_END_DATE]);
		newTask=new Task(newArray);
		Functions.taskList.add(newTask);
		
		
//		if(inputArray[ARRAY_INDEX_REPEAT].equalsIgnoreCase("r")){
//			String delayType=inputArray[ARRAY_INDEX_DELAYTYPE];
//			int recur=Integer.parseInt(inputArray[ARRAY_INDEX_RECUR]);
//			while(recur>0){
//				dtFormat(delayType, inputArray, startDate, endDate);
//				newTask=new Task(inputArray);
//				///////////// add to hashmap
//			}
//		}
		return String.format(Message.MSG_SUCCESS, newTask.getTitle());
	}
	
	private static long convertToMillisecond(String date, String time){
		//date format dd/mm/yyyy
		//time format HH:mm;	
		String[] dateArray = date.split("/");
		String[] timeArray = time.split(":");
		
		Calendar cal = Calendar.getInstance();
		cal.set(stringToInt(dateArray[2]), stringToInt(dateArray[1]), stringToInt(dateArray[0]), 
				stringToInt(timeArray[0]), stringToInt(timeArray[1]));
		
		return cal.getTimeInMillis();
	}
	
	private static int stringToInt(String input){
		return Integer.parseInt(input);
	}
	
}
