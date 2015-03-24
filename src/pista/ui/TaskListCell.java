package pista.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.parser.TokenValidation;

public class TaskListCell extends ListCell<Task> {

	private final String TASK_LIST_FOUND_CLASS = "task-list-found";
    private final String TASK_LIST_NOT_FOUND_CLASS = "task-list-not-found";
    
    private final String TASK_LIST_CELL = "task-list-cell";
    private final String TASK_LIST_CELL_ID_CLASS = "task-list-cell-id";
    private final String TASK_LIST_CELL_TITLE_CLASS = "task-list-cell-title";
    private final String TASK_LIST_CELL_DATETIME_CLASS = "task-list-cell-datetime";
    private final String TASK_LIST_ICON_CLASS = "task-list-icon";
    private final String FONT_AWESOME = "FontAwesome";
    
    private final int MAX_CHARACTER_IN_TITLE = 40;
    
    private String DISPLAY_START_DATE_TIME = "from [datetime]"; 
    private String DISPLAY_END_DATE_TIME = "to [datetime]"; 
    
	private GridPane grid = new GridPane();
	private VBox vBoxLeft = new VBox(0);
	private VBox vBoxRight = new VBox(0);
	
    //private Label icon = new Label();
	private Label lblID = new Label();
    private Label lblTitle = new Label();
    private Label lblStartDateTime = new Label();
    private Label lblEndDateTime = new Label();
    private CheckBox checkBox = new CheckBox();
    
    
    private String getID = "";
    private String getTitle = "";
    private String getCategory = "";
    private String getStartDate = "";
    private String getEndDate = "";
    private  String getStartTime = "";
    private String getEndTime = "";
    private boolean getIsDone = false;
    
    
    public TaskListCell() {
    	getStyleClass().add(TASK_LIST_CELL);
        configureGrid(); 
        configureIsDone();
        configureIcon();
        configureTitle();
        configureDateTime();
        configureVBoxRight();
        addControlsToVBox();
        addControlsToGrid();            
    }
    
    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(10);
        col3.setHalignment(HPos.RIGHT);
        grid.getColumnConstraints().addAll(col1, col2, col3);
        
    }
    
    private void configureIcon() {
        //icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
        //icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
    }
    
    private void configureIsDone(){
    	configureCheckBox(checkBox);
    }
    
    private void configureCheckBox(CheckBox cb){
    	cb.setOnAction(eh);
    }
    
    private void configureVBoxRight(){
    	vBoxRight.setPadding(new Insets(10, 0, 0, 0));
    }
    
    private void configureID(int type){
    	if(type != -2){
    		if(type == 1){
    			
    		}else if (type == -1){
    			
    		}else{
    			
    		}
    		lblID.getStyleClass().addAll(TASK_LIST_CELL_ID_CLASS, "task-list-cell-id-green");
    	}else{
    		lblID.getStyleClass().addAll(TASK_LIST_CELL_ID_CLASS);
    	}
    	
    }
    
    private void configureTitle() {
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_TITLE_CLASS);
    }
    
    private void configureDateTime() {
    	lblStartDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	lblEndDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	configureLabel(lblStartDateTime);
    	configureLabel(lblEndDateTime);  	
    }
    
    private void configureLabel(Label lbl){
    	lbl.setMaxWidth(Double.POSITIVE_INFINITY);
    	lbl.setMaxHeight(Double.POSITIVE_INFINITY);
    }
    
    private void addControlsToVBox(){
    	vBoxLeft.getChildren().add(lblStartDateTime);
    	vBoxLeft.getChildren().add(lblEndDateTime);
    	
    	vBoxRight.getChildren().add(checkBox);
    	vBoxRight.setAlignment(Pos.TOP_RIGHT);
    }
    
    private void addControlsToGrid() {
    	//add(node, column index, row index)
    	grid.add(lblID, 0, 0); 
    	GridPane.setRowSpan(lblID, 2);
        grid.add(lblTitle, 1, 0);        
        grid.add(vBoxLeft, 1, 1);
        grid.add(vBoxRight, 2, 0);
        GridPane.setRowSpan(vBoxRight, 2);
    }
    
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
 
    private void addContent(Task mTask) {
        setText(null);
        //icon.setText(GeocachingIcons.getIcon(cache).toString());
        
        getID = String.valueOf(mTask.getID());
        getTitle = mTask.getTitle();
        getCategory = mTask.getCategory();
        getStartDate = mTask.getStartDate();
        getEndDate = mTask.getEndDate();
        getStartTime = mTask.getStartTime();
        getEndTime = mTask.getEndTime();
        getIsDone = mTask.getIsDone();
        
        if(mTask.getTitle().length() > MAX_CHARACTER_IN_TITLE){
        	getTitle = getTitle.substring(0, MAX_CHARACTER_IN_TITLE) + "...";
        }
        
        lblTitle.setText(getTitle);
        lblID.setText(getID);
        checkBox.setId("checkBox_" + getID);
        checkBox.setSelected(getIsDone);
        
        if(getCategory.equals("timed")){
        	lblStartDateTime.setText(DISPLAY_START_DATE_TIME.replace("[datetime]", getStartDate + " " + getStartTime));
        	lblEndDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", getEndDate + " " + getEndTime));					
        }
        
        else if(getCategory.equals("deadline")){
        	lblEndDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", getEndDate + " " + getEndTime));
        }
        
        else if(getCategory.equals("floating")){
        	lblStartDateTime.setText("");
        	lblEndDateTime.setText("");
        }
        //setStyleClassDependingOnFoundState(mTask);  
        
        configureID(TokenValidation.compareWithCurrentDate(getEndDate, getEndTime));
        
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
	
	
	EventHandler eh = new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
	        if (event.getSource() instanceof CheckBox) {
	            CheckBox chk = (CheckBox) event.getSource();
	            System.out.println(chk.getId());
	            
	            String getTaskID = chk.getId().replace("checkBox_", "").trim();
	            
/*
	            if ("chk 1".equals(chk.getText())) {
	                chk2.setSelected(!chk1.isSelected());
	            } else if ("chk 2".equals(chk.getText())) {
	                chk1.setSelected(!chk2.isSelected());
	            }
*/
	        }//end if
	    }//end handle
	};
	
}
