package pista.ui;

import java.io.IOException;

import pista.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootLayoutController {
	
	private UIController mUICtrl = null;
	
	private static String CSS_CLASS_IMAGE_BACKGROUND = "image-background";
	
    @FXML
    private BorderPane borderPaneRoot;
    
    @FXML
	public void initialize() {
    	borderPaneRoot.getStyleClass().addAll(CSS_CLASS_IMAGE_BACKGROUND);
    }
	
	public void setUIController(UIController ctrl){
    	mUICtrl = ctrl;
    }
	
	public boolean refreshUIListView(){
		boolean isUpdated = mUICtrl.initTaskListInListView();
		return isUpdated;
	}

}
