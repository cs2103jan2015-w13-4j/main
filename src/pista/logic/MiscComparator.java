package pista.logic;

import java.util.Comparator;

import pista.Constants;

public class MiscComparator {

	public static Comparator<Task> titleComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			String titleOne=one.getTitle();
			String titleTwo=two.getTitle();
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			if(compareIsDone(taskOneStatus, taskTwoStatus) == 0){
				return titleOne.compareTo(titleTwo);
			}
			return 1;
		}
	};	//added ; to resolve ClassBodyDeclarations	

	public static Comparator<Task> isDoneComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			return compareIsDone(taskOneStatus, taskTwoStatus);
		}
	};

	public static Comparator<Task> endDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getEndMilliseconds();
			long taskTwoDate = two.getEndMilliseconds();
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			if(compareIsDone(taskOneStatus, taskTwoStatus) == 0){
				if (taskOneDate == 0L && taskTwoDate != 0L){
					return -1;
				}
				if(taskOneDate != 0L && taskTwoDate != 0L){
					if(taskOneDate < taskTwoDate){
						return -1;
					}else if(taskOneDate == taskTwoDate){
						return 0;
					}else if (taskOneDate > taskTwoDate){
						return 1;
					}
				}
			}
			
			
			return 1;
		}
	};

	public static Comparator<Task> startDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getStartMilliseconds();
			long taskTwoDate = two.getStartMilliseconds();
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			if(compareIsDone(taskOneStatus, taskTwoStatus) == 0){
				if (taskOneDate == 0L && taskTwoDate != 0L){
					return 1;
				}
				if(taskOneDate != 0L && taskTwoDate != 0L){
					if(taskOneDate < taskTwoDate){
						return -1;
					}else if(taskOneDate == taskTwoDate){
						return 0;
					}else if (taskOneDate > taskTwoDate){
						return 1;
					}
				}
			}
			
			
			return -1;
		}
	};

	public static Comparator<Task> descendingStartDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getStartMilliseconds();
			long taskTwoDate = two.getStartMilliseconds();
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			if (taskOneDate == 0L && taskTwoDate != 0L){
				return -1;
			}
			if(compareIsDone(taskOneStatus, taskTwoStatus) == 0){
				if(taskOneDate != 0L && taskTwoDate != 0L){
					if(taskOneDate < taskTwoDate){
						return -1;
					}else if(taskOneDate == taskTwoDate){
						return 0;
					}else if (taskOneDate > taskTwoDate){
						return 1;
					}
				}
			}		
			return 1;
		}
	};

	public static Comparator<Task> taskCategoryComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			String catOne = one.getCategory();
			String catTwo = two.getCategory();
			Integer orderOne = taskOrder(catOne);
			Integer orderTwo = taskOrder(catTwo);
			return orderOne.compareTo(orderTwo);
		}

		private int taskOrder(String taskCategory){
			if(taskCategory.equalsIgnoreCase(Constants.TASK_FLOATED)){
				return -1;
			}else if(taskCategory.equalsIgnoreCase(Constants.TASK_DEADLINE)){
				return 0;
			}

			return 1;	
		}
	};
	public static Comparator<Task> priorityComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			int taskOnePriority = Integer.parseInt(one.getPriority());
			int taskTwoPriority = Integer.parseInt(two.getPriority());
			boolean taskOneStatus = one.getIsDone();
			boolean taskTwoStatus = two.getIsDone();
			if(compareIsDone(taskOneStatus, taskTwoStatus) == 0){
				if(taskOnePriority < taskTwoPriority ){
					return 1;
				}else if (taskOnePriority > taskTwoPriority) {
					return -1;
				}else{
					return 0;
				}
			}
			return 1;
		}
	};

	private static int compareIsDone(boolean taskOneIsDone, boolean taskTwoIsDone){
		if(taskOneIsDone == true && taskTwoIsDone == false){
			return 1;
		}else if (taskOneIsDone == false && taskTwoIsDone == true) {
			return -1;
		}else{	// this is when taskOneStatus == statusTwoStatus  T==T or F==F
			return 0;
		}
	}
}



