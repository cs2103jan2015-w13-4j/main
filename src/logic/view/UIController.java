package logic.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.Constants;

public class UIController {
	
    @FXML
    private Button deleteButton;

    @FXML
    private TextField cmdTextField;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button listButton;
    
    @FXML
    private Button enterButton;
    
    public void add() {
    	cmdTextField.setText("add para1 para2");
    }
    
    public void edit() {
    	cmdTextField.setText("edit para1 para2");
    }
    
    public void delete() {
    	cmdTextField.setText("delete para1 para2");
    }
}

