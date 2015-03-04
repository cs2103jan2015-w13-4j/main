import java.util.StringTokenizer;


public class EditTask {
	public String edit(StringTokenizer st){
		Task current=new Task();
		return String.format(Message.MSG_SUCCESS, current.getTitle());
	}
}
