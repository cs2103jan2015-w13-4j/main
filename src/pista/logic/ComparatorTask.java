package pista.logic;

import java.util.Comparator;

public class ComparatorTask implements Comparator <Task> {
	@Override
	public int compare(Task taskOne, Task taskTwo){
		if(compareIsDone(taskOne.getIsDone(), taskTwo.getIsDone()) == 0){
			if(compareDate(taskOne.getEndMilliseconds(), taskTwo.getEndMilliseconds()) == 0){
				if(comparePriority(Integer.parseInt(taskOne.getPriority()), Integer.parseInt(taskTwo.getPriority())) == 0){
					return compareTitle(taskOne.getTitle(), taskTwo.getTitle());
				}else{
					return comparePriority(Integer.parseInt(taskOne.getPriority()), Integer.parseInt(taskTwo.getPriority()));
				}
			}else{
				return compareDate(taskOne.getEndMilliseconds(), taskTwo.getEndMilliseconds());
			}
		}else{
			return compareIsDone(taskOne.getIsDone(), taskTwo.getIsDone());
		}
	} 

	private int compareDate(long taskOneDate, long taskTwoDate){
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

	private int compareTitle(String taskOneTitle, String taskTwoTitle){
		return taskOneTitle.compareTo(taskTwoTitle);
	}

	private int compareIsDone(boolean taskOneIsDone, boolean taskTwoIsDone){
		if(taskOneIsDone == true && taskTwoIsDone == false){
			return 1;
		}else if (taskOneIsDone == false && taskTwoIsDone == true) {
			return -1;
		}else{	// this is when taskOneStatus == statusTwoStatus  T==T or F==F
			return 0;
		}
	}

	private int comparePriority(int taskOnePriority, int taskTwoPriority){
		if(taskOnePriority < taskTwoPriority ){
			return 1;
		}else if (taskOnePriority > taskTwoPriority) {
			return -1;
		}else{
			return 0;
		}
	}


}
