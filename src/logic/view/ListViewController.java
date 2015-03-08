package logic.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import logic.Constants;
import logic.MainApp;
import logic.Task;
import logic.storage.Storage;

public class ListViewController {

    @FXML
    private ListView<Task> listview_id1;
    
    private MainApp mMainApp;
    private ArrayList<Task> taskList = null;
    
    
    private ObservableList<String> observableList =
    		FXCollections.observableArrayList();

    
    public ListViewController(){} //default constructor
    
    @FXML
    public void initialize() {
    	
    	prepareinitList();
    	
    	ObservableList<Task> myObservableList = FXCollections.observableList(this.taskList);
    	listview_id1.setItems(myObservableList);
    	
    	//listview_fx_id_1.setCellFactory(new Callback<ListView<Task>, new TaskListCell());
    	
    	listview_id1.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>(){
    		@Override
    		public ListCell<Task> call(ListView<Task> param) {
    			return new TaskListCell();
    		}
    	});
    	
    	
    	
	}
    
    public void setMainListView(MainApp main){
    	this.mMainApp = main;
    	
    }
    
	private void prepareinitList(){
		//this.taskList = list;
		this.taskList = Storage.XmltoTable(Constants.XML_FILE_PATH);
		
	}
	
}