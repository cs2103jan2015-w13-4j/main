package pista.logic;

import pista.Constants;
import pista.storage.Storage;

public class Task {
	private Storage mStorage;
	private String title="";
	//private String description;
	private int id;
	private String startDate = "";
	private String endDate = "";
	private String startTime = "";
	private String endTime = "";
	private boolean isDone = false;
	private boolean isReminded = false;
	private long startMilliseconds;
	private long endMilliseconds;
	private String category = "";
	private String priority = "";  
	private long reminderTime = 0L;
	
	/*3 catergory
	 * event task has start date/time and end date/time
	 * deadline	task has end date/time only
	 * free time task does not have start and end date/time*/
	public Task(String[] inputArray){
		initStorage();
		this.title=inputArray[Constants.ARRAY_INDEX_TITLE];
		this.id= mStorage.getNextAvailableID();
		this.startDate=inputArray[Constants.ARRAY_INDEX_START_DATE];
		this.startTime=inputArray[Constants.ARRAY_INDEX_START_TIME];
		this.endDate=inputArray[Constants.ARRAY_INDEX_END_DATE];
		this.endTime=inputArray[Constants.ARRAY_INDEX_END_TIME];
		this.isDone=false;
		this.isReminded = false;
		this.priority ="0";
		
		setCategoryInConstructor(inputArray);
		
	}

	private void setCategoryInConstructor(String[] inputArray) {
		if(inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]==null && inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]==null){
			this.category="floating";
			
		}else if(inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]!=null && inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]==null){
			this.endMilliseconds=Long.parseLong(inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]);
			this.category="deadline";
			
		}else{
			this.startMilliseconds=Long.parseLong(inputArray[Constants.ARRAY_INDEX_START_MILLISECONDS]);
			this.endMilliseconds=Long.parseLong(inputArray[Constants.ARRAY_INDEX_END_MILLISECONDS]);
			this.category="timed";
		}
	}
	
	public boolean initStorage(){
		try{
			mStorage = Storage.getInstance();
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	/*Used for importing all tasks from XML*/
	public Task(int id, String title, long startMs, long endMs, String startTime, String startDate, String endTime, String endDate, 
				String category, String priority, boolean isDone, long reminderTime){
		this.id = id;
		this.title = title;
		this.startMilliseconds = startMs;
		this.endMilliseconds = endMs;
		this.startTime = startTime;
		this.startDate = startDate;
		this.endTime = endTime;
		this.endDate = endDate;
		this.isDone = isDone;
		this.category = category;
		this.priority = priority;
		this.reminderTime = reminderTime;
		this.isReminded = false;
	}
	
	public String getTitle(){
		return title;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getStartTime(){
		return this.startTime;
	}
	
	public String getEndTime(){
		return this.endTime;
	}
	
	public String getStartDate(){
		return this.startDate;
	}
	
	public String getEndDate(){
		return this.endDate;
	}
	
	public long getStartMilliseconds(){
		return this.startMilliseconds;
	}
	
	public long getEndMilliseconds(){
		return this.endMilliseconds;
	}
	
	public boolean getIsDone(){
		return this.isDone;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getPriority(){
		return this.priority;
	}
	
	public long getReminder(){
		return this.reminderTime;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setStartTime(String startTime){
		this.startTime=startTime;
	}
	
	public void setEndTime(String endTime){
		this.endTime=endTime;
	}
	
	public void setStartDate(String startDate){
		this.startDate=startDate;
	}
	
	public void setEndDate(String endDate){
		this.endDate=endDate;
	}
	
	
	public void setStartMilliseconds(long start){
		this.startMilliseconds = start; 
	}
	
	public void setEndMilliseconds(long start){
		this.endMilliseconds = start; 
	}
	
	
	public void setIsDone(boolean status){
		this.isDone=status;
	}

	public void setCategory(String cat){
		this.category = cat;
	}
	
	public void setPriority(String p){
		this.priority = p;
	}
	
	public long setReminder(long reminderTime){
		return this.reminderTime = reminderTime;
	}
}
