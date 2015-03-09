package logic;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ListTask {
	

	
	public static StringBuilder listTask(ArrayList<Task> taskList){
		StringBuilder finalString = new StringBuilder("");
		
		for (Task t : taskList){
			
			String title = t.getTitle();
			String startDate = t.getStartDate();
			String endDate = t.getEndDate();
			String startTime = t.getStartTime();
			String endTime = t.getEndTime();
			
			String tmp = title+"/r/nStart :"+startDate+" @ "+startTime+"/r/nEnd :"+endDate+" @ "+endTime+"/r/n";
			finalString.append(tmp);
		}
		
		return finalString;
	}
	
	private String dateFormatter(long ms){
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
		Calendar calendar  = Calendar.getInstance();
		calendar.setTimeInMillis(ms);

		return sdf.format(calendar.getTime());
	}
	
	
}