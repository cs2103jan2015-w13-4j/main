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
	
	private SettingLayoutController mSettingCtrl = null;
	private UIController mUICtrl = null;
	
	private static String CSS_CLASS_IMAGE_BACKGROUND = "image-background";
	
    @FXML
    private BorderPane borderPaneRoot;
    
    
    @FXML
	public void initialize() {
    	borderPaneRoot.getStyleClass().addAll(CSS_CLASS_IMAGE_BACKGROUND);
    }
    
	@FXML
	void onTaskAddClick(ActionEvent event) {
		mUICtrl.add_outline();
	}
	
	@FXML
	void onTaskEditClick(ActionEvent event) {
		mUICtrl.edit_outline();
	}
	
	@FXML
	void onTaskDeleteClick(ActionEvent event) {
		mUICtrl.delete_outline();
	}
	
	@FXML
	void onActionUndoClick(ActionEvent event) {
		mUICtrl.txtBoxCommand.setText("undo");
		try {
			mUICtrl.enter();
			mUICtrl.txtBoxCommand.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	void onActionRedoClick(ActionEvent event) {
		mUICtrl.txtBoxCommand.setText("redo");
		try {
			mUICtrl.enter();
			mUICtrl.txtBoxCommand.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	void onViewRefreshClick(ActionEvent event) {
		mUICtrl.onRefresh(event);
	}
	
	@FXML
	void onHelpUserGuideClick(ActionEvent event) {
		mUICtrl.onHelp(event);
	}
	
	@FXML
    void onMenuItemSettingClick(ActionEvent event) {
		Stage stage = new Stage();

		stage.setTitle(Constants.SETTING_LAYOUT_TITLE);
		stage.initModality(Modality.NONE);
		stage.initStyle(StageStyle.DECORATED);
		stage.setResizable(false);
		//stage.setMaxHeight(800);
		//stage.setMaxWidth(600);
		//stage.setMinHeight(600);
		//stage.setMinWidth(400);
		try {
			FXMLLoader loader = new FXMLLoader();
			
	        loader.setLocation(getClass().getResource(Constants.SETTING_LAYOUT_PATH));
	        BorderPane rootLayout;
			rootLayout = (BorderPane) loader.load();
			
			mSettingCtrl  = loader.getController(); //setting page will not capture rootcontroller during initialize
	        mSettingCtrl.setRootController(this); //setting will capture rootcontroller after initialize
	        
			Scene scene = new Scene(rootLayout, 500, 300);		
			
			stage.setScene(scene);   
			stage.show();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void setUIController(UIController ctrl){
    	mUICtrl = ctrl;
    }
	
	public boolean refreshUIListView(){
		boolean isUpdated = mUICtrl.initTaskListInListView();
		return isUpdated;
	}

}
