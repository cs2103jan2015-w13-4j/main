package logic.view;

import logic.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class TaskListCell extends ListCell<Task> {

	private static final String TASK_LIST_FOUND_CLASS = "task-list-found";
    private static final String TASK_LIST_NOT_FOUND_CLASS = "task-list-not-found";
    private static final String TASK_LIST_TITLE_CLASS = "task-list-title";
    private static final String CACHE_LIST_DATETIME_CLASS = "task-list-datetime";
    private static final String TASK_LIST_ICON_CLASS = "task-list-icon";
    private static final String FONT_AWESOME = "FontAwesome";
    
	private GridPane grid = new GridPane();
    //private Label icon = new Label();
    private Label lblTitle = new Label();
    private Label lblDateTime = new Label();

    public TaskListCell() {
        configureGrid();        
        configureIcon();
        configureTitle();
        configureDateTime();
        addControlsToGrid();            
    }
    
    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 10, 0, 10));
    }
    
    private void configureIcon() {
        //icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
        //icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
    }
    
    private void configureTitle() {
    	lblTitle.getStyleClass().add(TASK_LIST_TITLE_CLASS);
    }
    
    private void configureDateTime() {
    	lblDateTime.getStyleClass().add(CACHE_LIST_DATETIME_CLASS);
    }
    
    private void addControlsToGrid() {
        //grid.add(icon, 0, 0, 1, 2);                    
        grid.add(lblTitle, 0, 0);        
        grid.add(lblDateTime, 0, 1);
    }
    
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
 
    private void addContent(Task mTask) {
        setText(null);
        //icon.setText(GeocachingIcons.getIcon(cache).toString());
        lblTitle.setText(mTask.getTitle()+ "id:"+mTask.getID());
        String category=mTask.getCategory();
        if(category.equals("timed")){
        	lblDateTime.setText("Start "+" "+mTask.getStartDate()+ " "+ mTask.getStartTime()+" End by "+mTask.getEndDate()+mTask.getEndTime());
        }
        else if(category.equals("deadline")){
        	lblDateTime.setText("Complete by "+" "+mTask.getEndDate()+ " "+ mTask.getEndTime());
        }
        else if(category.equals("floating")){
        	lblDateTime.setText("Floating task");
        }
        //setStyleClassDependingOnFoundState(mTask);        
        setGraphic(grid);
    }
    
   
    /*
     * for switching classes
    private void setStyleClassDependingOnFoundState(Cache cache) {
    	
        if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
            addClasses(this, CACHE_LIST_FOUND_CLASS);
            removeClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
        } else {
            addClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
            removeClasses(this, CACHE_LIST_FOUND_CLASS);
        }
    }
    */
    
	@Override
	public void updateItem(Task mTask, boolean empty){
		super.updateItem(mTask,  empty);
		if(empty){
			clearContent();
		}else {		
			addContent(mTask);	
			
		}//end if 
	}//end updateItem
	
	
}
