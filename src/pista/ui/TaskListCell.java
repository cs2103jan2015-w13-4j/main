package pista.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sun.javafx.geom.Rectangle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import pista.Constants;
import pista.logic.Logic;
import pista.logic.Task;
import pista.parser.MainParser;
import pista.parser.TokenValidation;

public class TaskListCell extends ListCell<Task> {

	private final String TASK_LIST_FOUND_CLASS = "task-list-found";
    private final String TASK_LIST_NOT_FOUND_CLASS = "task-list-not-found";
    
    private final String TASK_LIST_CELL = "task-list-cell";
    private final String TASK_LIST_CELL_ID_CLASS = "task-list-cell-id";
    private final String TASK_LIST_CELL_ID_BLACK_CLASS = "task-list-cell-id-black";
    private final String TASK_LIST_CELL_ID_RED_CLASS = "task-list-cell-id-red";
    private final String TASK_LIST_CELL_ID_GREEN_CLASS = "task-list-cell-id-green";
    private final String TASK_LIST_CELL_ID_YELLOW_CLASS = "task-list-cell-id-yellow";
    private final String TASK_LIST_CELL_ID_PURPLE_CLASS = "task-list-cell-box-purple";
    
    
    private final String TASK_LIST_CELL_BOX_RED_CLASS = "task-list-cell-box-red";
    private final String TASK_LIST_CELL_BOX_YELLOW_CLASS = "task-list-cell-box-yellow";
    private final String TASK_LIST_CELL_BOX_GREEN_CLASS = "task-list-cell-box-green";
    
    private final String TASK_LIST_CELL_TITLE_CLASS = "task-list-cell-title";
    private final String TASK_LIST_CELL_DATETIME_CLASS = "task-list-cell-datetime";
    
    private final String TASK_LIST_CELL_TEXT_IS_DONE_CLASS = "task-list-cell-text-is-done";
    private final String TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS = "task-list-cell-background-is-done";
    
    private final String TASK_LIST_CELL_STRIKE_THROUGH_CLASS = "task-list-cell-label-strike-through";
    
    private final int MAX_CHARACTER_IN_TITLE = 40;
    
    private String DISPLAY_START_DATE_TIME = "From [datetime]"; 
    private String DISPLAY_END_DATE_TIME = "To [datetime]"; 
    
    private final String DONE_COMMAND = "-" + Constants.MARK_DONE; //-done
    private final String NOT_DONE_COMMAND = "-" + Constants.MARK_UNDONE; //-undone
    
	private GridPane grid = new GridPane();
	private VBox vBoxDateTime = new VBox(0);
	private VBox vBoxCheckBox = new VBox(0);
	
    //private Label icon = new Label();
	private Label lblID = new Label();
    private Label lblTitle = new Label();
    private Label lblStartDateTime = new Label();
    private Label lblEndDateTime = new Label();
    private CheckBox checkBox = new CheckBox();
    private UIController mUIParent = null;
    private VBox vBoxColor = new VBox(0);
    private Image mImg = new Image("images/alarm.png");
    private ImageView mImgView = new ImageView();
    
    private String getID = "";
    private String getTitle = "";
    private String getCategory = "";
    private String getStartDate = "";
    private String getEndDate = "";
    private  String getStartTime = "";
    private String getEndTime = "";
    private boolean getIsDone = false;
    
    
    public TaskListCell() {   	
        configureGrid(); 
        configureCheckBoxIsDone();
        configureImageView();
        configureLabelID();
        configureLabelTitle();
        configureLabelDateTime();
        configureVBoxCheckBox();
        addControlsToVBox();
        addControlsToGrid();            
    }
    
    private void configureGrid() {
        //grid.setHgap(10);
        //grid.setVgap(4);
        grid.setPadding(new Insets(0, 2, 0, 2));
        grid.setMaxHeight(70.0);
        grid.setMinHeight(70.0);
        grid.setPrefHeight(70.0);
        
        ColumnConstraints col1 = new ColumnConstraints(); //color box
        col1.setPercentWidth(2);
        col1.setHalignment(HPos.LEFT);
        
        ColumnConstraints col2 = new ColumnConstraints(); //check box
        col2.setPercentWidth(3);
        col2.setHalignment(HPos.CENTER);
        
        ColumnConstraints col3 = new ColumnConstraints(); //ID
        col3.setPercentWidth(10);
        col3.setHalignment(HPos.CENTER);
        
        ColumnConstraints col4 = new ColumnConstraints(); //title
        col4.setPercentWidth(55);
        col4.setHalignment(HPos.LEFT);
        
        ColumnConstraints col5 = new ColumnConstraints(); // alarm
        col5.setPercentWidth(10);
        col5.setHalignment(HPos.RIGHT);
        
        ColumnConstraints col6 = new ColumnConstraints(); //date time
        col6.setPercentWidth(20);
        col6.setHalignment(HPos.RIGHT);
        
        
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPrefHeight(70.0);
        grid.getRowConstraints().addAll(row1);
        
        grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6);
        
    }
    
    //private void configureIcon() {
        //icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
        //icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
    //}
    
    private void setCellBackground(boolean isDone){
    	if(isDone){
    		getStyleClass().add(TASK_LIST_CELL_BACKGROUND_IS_DONE_CLASS);
    	}else{
    		getStyleClass().add(TASK_LIST_CELL);
    	}
    }
    
    private void configureImageView(){
    	mImgView.setImage(mImg);
    	mImgView.setFitWidth(20.0);
    	mImgView.setPreserveRatio(true);
    	mImgView.setSmooth(true);
    	mImgView.setCache(true);
    }
    
    
    private void configureBoxColor(int type){
    	vBoxColor.setMaxWidth(7.0);
    	vBoxColor.setMinWidth(7.0);
    	vBoxColor.setPrefWidth(7.0);
    	vBoxColor.setAlignment(Pos.CENTER_LEFT);  	
    	
    	if(type == 1){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_RED_CLASS);
    	}
    	
    	if (type == -1 || type == 0){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_YELLOW_CLASS);
    	}
    	
    	if (type == -2 || type == 4){
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_ID_PURPLE_CLASS);
    	}
    	
    	if (type == 3){ //is done
    		vBoxColor.getStyleClass().addAll(TASK_LIST_CELL_BOX_GREEN_CLASS);
    	}
    	
    }
    
    
    private void configureCheckBoxIsDone(){
    	checkBox.setOnAction(eh);
    }
    
    private void configureVBoxCheckBox(){
    	vBoxCheckBox.setPadding(new Insets(0, 0, 0, 0));
    }
    
    private void configureLabelID(){
    	//System.out.println("type - " + type);
    	lblID.getStyleClass().addAll(TASK_LIST_CELL_ID_CLASS);
    	lblID.setAlignment(Pos.CENTER);
    }
    
    private void configureLabelTitle() {
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_TITLE_CLASS);
    	lblTitle.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void configureLabelDateTime() {
    	lblStartDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	lblEndDateTime.getStyleClass().add(TASK_LIST_CELL_DATETIME_CLASS);
    	configureLabels(lblStartDateTime);
    	configureLabels(lblEndDateTime);  	
    }
    
    private void configureLabels(Label lbl){
    	lbl.setMaxWidth(180.0);
    	lbl.setMaxHeight(20.0);
    }
    
    private void strikeThroughLabels(){
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    	lblID.getStyleClass().add(TASK_LIST_CELL_STRIKE_THROUGH_CLASS);
    }
    
    private void isDoneLabel(){
    	lblTitle.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    	lblID.getStyleClass().add(TASK_LIST_CELL_TEXT_IS_DONE_CLASS);
    }
    
    private void addControlsToVBox(){
    	vBoxDateTime.getChildren().add(lblEndDateTime);
    	vBoxDateTime.getChildren().add(lblStartDateTime);
    	vBoxDateTime.setAlignment(Pos.CENTER_RIGHT);
    	
    	vBoxCheckBox.getChildren().add(checkBox);
    	vBoxCheckBox.setAlignment(Pos.CENTER);
    }
    
    private void addControlsToGrid() {
    	//add(node, column index, row index)
    	grid.add(vBoxColor, 0, 0);
    	vBoxColor.setPadding(new Insets(0, 0, 0, 5));
    	
    	grid.add(vBoxCheckBox, 1, 0);
        //GridPane.setRowSpan(vBoxCheckBox, 2);
    	grid.add(lblID, 2, 0); 
    	
    	//GridPane.setRowSpan(lblID, 2);
        grid.add(lblTitle, 3, 0);
        
        grid.add(mImgView, 4, 0);
        grid.setMargin(mImgView, new Insets(5));
       // GridPane.setRowSpan(lblTitle, 2);
        grid.add(vBoxDateTime, 5, 0);
        
        //GridPane.setRowSpan(vBoxDateTime, 2);
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
        	
        } else if(getCategory.equals("deadline")){
        	lblEndDateTime.setText(DISPLAY_END_DATE_TIME.replace("[datetime]", getEndDate + " " + getEndTime));
        	
        } else if(getCategory.equals("floating")){
        	lblStartDateTime.setText("");
        	lblEndDateTime.setText("");
        }
        //setStyleClassDependingOnFoundState(mTask);  
        
        if(getIsDone){ //task is done or floating
        	strikeThroughLabels();
        	isDoneLabel();
        	configureBoxColor(3); //green
        	
        }else if (getCategory.equals("floating")){
        	configureBoxColor(4);
        	
        }else{
        	configureBoxColor(TokenValidation.compareWithCurrentDate(getEndDate, getEndTime));
        }
        
        
        setCellBackground(getIsDone);
        
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
	    	boolean isUpdated = false;
	    	String markCommandFormatString = "mark [id] [type]";
	    	String markCommandEditedString = "";
	    	
	        if (event.getSource() instanceof CheckBox) {
	            CheckBox chk = (CheckBox) event.getSource();
	            
	            String getTaskID = chk.getId().replace("checkBox_", "").trim();
	            
	            if(chk.isSelected()){
	            	getIsDone = true; 
	            	markCommandEditedString = markCommandFormatString.replace("[id]", getTaskID).replace("[type]", DONE_COMMAND);
	            	
	            }else{
	            	getIsDone = false;
	            	markCommandEditedString = markCommandFormatString.replace("[id]", getTaskID).replace("[type]", NOT_DONE_COMMAND);
	            }
	            
	            executeCommand(markCommandEditedString);
	            /*
	            isUpdated = updateTaskDoneStatus(Integer.parseInt(getTaskID), getIsDone);
	            if(isUpdated){
	            	setCellBackground(getIsDone);
		            refreshUIControllerParentListView();
	            }
	            */
	            
	            
	        }//end if
	    }//end handle
	};
	
	
	public void setUIParent(UIController ui){
		mUIParent = ui;
	}
	
	private boolean executeCommand(String input){
		String[] tokens = null;
		String parserOutput = "";
		String logicOutput = "";
		String command = "";
		
		MainParser mp = MainParser.validateInput(input);
		parserOutput = mp.getMessage();
		if(!parserOutput.equals(Constants.MESSAGE_VALID_INPUT)){
			setUIControllerParentTextStatus(parserOutput);
			return false;
		}
		
		command = mp.getCommand();
		tokens = mp.getTokens();
		
		logicOutput = Logic.runCommand(command, tokens);
		setUIControllerParentTextStatus(logicOutput);
		
		refreshUIControllerParentListView();
		
		return true;
		
	}
	
	private void setUIControllerParentTextStatus(String msg){
		if(mUIParent != null){
			mUIParent.setTextStatus(msg);
		}
	}
	
	private void refreshUIControllerParentListView(){
		if(mUIParent != null){
			mUIParent.showTaskListInListView();
		}
	}
	
	/*
	private boolean updateTaskDoneStatus(int id, boolean newDone){
		boolean isUpdated = Logic.updateTaskIsDone(id, newDone);
		return isUpdated;
	}
	*/
	
}
