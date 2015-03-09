package logic;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.joda.time.*;
import org.joda.time.format.*;



public class LogicAddTask {



	private static DateTimeFormatter dtf=DateTimeFormat.forPattern("dd/MM/yyyy");
	private static DateTimeFormatter dtf2=DateTimeFormat.forPattern("dd/MM/yyyy HH:MM");
	//private static SimpleDateFormat stf=new SimpleDateFormat();

	/*
	public static void dtFormat(String delayType, String[] inputArray){
	
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
		
//		String combine=startDate+" "+inputArray[ARRAY_INDEX_START_TIME];
//		String combine2=endDate+" "+inputArray[ARRAY_INDEX_END_TIME];
//		DateTime startDate2=dtf2.parseDateTime(combine);
//		DateTime endDate2=dtf2.parseDateTime(combine2);
		
	}
	*/
	
	public static String[] formatArray(String[] inputArray){
		String delayType = inputArray[Constants.ARRAY_INDEX_REPEAT];
		
		switch(delayType.toLowerCase()){
			//do delay etc etc
		}

		inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]=String.valueOf(Parser.convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_START_DATE], 
																			inputArray[Constants.ARRAY_INDEX_START_TIME]));
		inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]=String.valueOf(Parser.convertDateToMillisecond(inputArray[Constants.ARRAY_INDEX_END_DATE], 
																			inputArray[Constants.ARRAY_INDEX_END_TIME]));
		
		return inputArray;
	}
	
	/*
	public static String add(String[] inputArray){
		Task newTask = null;
		//DateTime startDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_START_DATE]);
		//DateTime endDate=dtf.parseDateTime(inputArray[ARRAY_INDEX_END_DATE]);
		
		System.out.println("the length is " + inputArray.length);
		for (int i = 0; i < inputArray.length; i++) {
			System.out.println("what is inside " + inputArray[i]);
		}
		
		String[] newArray = new String[Constants.ARRAY_SIZE];
		
		System.arraycopy(inputArray, 0, newArray, 0, inputArray.length);
		
		dtFormat(newArray[Constants.ARRAY_INDEX_REPEAT], newArray);
		
		newTask = new Task(newArray); //id will auto generate inside Task class
		Logic.taskList.add(newTask);

		
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
	*/
	
	public static Task constructNewTask(String[] inputArray){
		Task newTask = null;

		System.out.println("the length is " + inputArray.length);
		for (int i = 0; i < inputArray.length; i++) {
			System.out.println("what is inside " + inputArray[i]);
		}
		
		String[] newArray = new String[Constants.ARRAY_SIZE];
		System.arraycopy(inputArray, 0, newArray, 0, inputArray.length); //copy inputArray into newArray
		
		newArray = formatArray(newArray); //add in more data inside array
		newTask = new Task(newArray); //id will auto generate inside Task class
		
		return newTask;
		
	}//end constructNewTask
	
	
	
	
	
	
}
