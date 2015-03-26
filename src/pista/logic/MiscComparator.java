package pista.logic;

import java.util.Comparator;

public class MiscComparator {

	public static Comparator<Task> titleComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			String titleOne=one.getTitle();
			String titleTwo=two.getTitle();
			return titleOne.compareTo(titleTwo);
		}
	};	//added ; to resolve ClassBodyDeclarations	

	public static Comparator<Task> isDoneComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			boolean taskOneIsDone = one.getIsDone();
			boolean taskTwoIsDone = two.getIsDone();
			if(taskOneIsDone == true && taskTwoIsDone == false){
				return 1;
			}else if (taskOneIsDone == false && taskTwoIsDone == true) {
				return -1;
			}else{	// this is when taskOneStatus == statusTwoStatus  T==T or F==F
				return 0;
			}
		}
	};

	public static Comparator<Task> endDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getEndMilliseconds();
			long taskTwoDate = two.getEndMilliseconds();
			if(taskOneDate != 0L && taskTwoDate != 0L){
				if(taskOneDate < taskTwoDate){
					return -1;
				}else if(taskOneDate == taskTwoDate){
					return 0;
				}else if (taskOneDate > taskTwoDate){
					return 1;
				}
			}else if (taskOneDate == 0L && taskTwoDate != 0L){
				return 1;
			}else if (taskOneDate != 0L && taskTwoDate == 0L) {
				return -1;
			}
			return 0;
		}
	};
	
	public static Comparator<Task> ascendingStartDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getStartMilliseconds();
			long taskTwoDate = two.getStartMilliseconds();
			if(taskOneDate != 0L && taskTwoDate != 0L){
				if(taskOneDate < taskTwoDate){
					return -1;
				}else if(taskOneDate == taskTwoDate){
					return 0;
				}else if (taskOneDate > taskTwoDate){
					return 1;
				}
			}else if (taskOneDate == 0L && taskTwoDate != 0L){
				return 1;
			}else if (taskOneDate != 0L && taskTwoDate == 0L) {
				return -1;
			}
			return 0;
		}
	};
	
	public static Comparator<Task> descendingStartDateComparator = new Comparator<Task>(){
		@Override
		public int compare (Task one, Task two){
			long taskOneDate = one.getStartMilliseconds();
			long taskTwoDate = two.getStartMilliseconds();
			if(taskOneDate != 0L && taskTwoDate != 0L){
				if(taskOneDate < taskTwoDate){
					return -1;
				}else if(taskOneDate == taskTwoDate){
					return 0;
				}else if (taskOneDate > taskTwoDate){
					return 1;
				}
			}else if (taskOneDate == 0L && taskTwoDate != 0L){
				return -1;
			}else if (taskOneDate != 0L && taskTwoDate == 0L) {
				return 1;
			}
			return 0;
		}
	};



}



