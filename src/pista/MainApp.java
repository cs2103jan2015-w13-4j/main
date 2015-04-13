package pista;

import java.io.IOException;

import pista.log.CustomLogging;
import pista.logic.Logic;
import pista.parser.MainParser;
import pista.storage.Storage;
import pista.ui.RootLayoutController;
import pista.ui.UIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
	//@author A0111721Y
	private Stage primaryStage;
    private BorderPane rootLayout;
      
    
    private UIController mUICtrl = null;
    private RootLayoutController mRootCtrl = null;
    
    
    //initialises the stage
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.PRODUCT_NAME);
        this.primaryStage.initStyle(StageStyle.DECORATED);
        this.primaryStage.setResizable(false);
        this.primaryStage.setOnCloseRequest(this.stageHandler);
        
        initRootLayout();
        initMainUI();
        
        this.mRootCtrl.setUIController(mUICtrl);

    }
    
    
    //sets up the base root layout inclusive of the menu bar
    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.ROOT_LAYOUT_PATH)); //RootLayout.fxml
            this.rootLayout = (BorderPane) loader.load();

            this.mRootCtrl = loader.getController();
            
            // Show the scene containing the root layout
            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);
            this.primaryStage.getIcons().addAll(new Image("images/pista_icon.png"));
            this.primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	//sets up inside the base root layout the main UI
    public void initMainUI() {
        try {
            // Load MainUI
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.MAIN_UI_LAYOUT_PATH)); //MainUI.fxml
            AnchorPane uiLayout = (AnchorPane) loader.load();

            this.mUICtrl = loader.getController();
            this.mUICtrl.setMainAppController(this);
            
            // Set MainUI into the center of RootLayout
            this.rootLayout.setCenter(uiLayout);      
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns primary stage
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
    
    public double getPrimaryStageWidth(){
    	return this.primaryStage.getWidth();
    }
    
    public double getPrimaryStageHeight(){
    	return this.primaryStage.getHeight();
    }
    
	public static void main(String[] args) {
		launch(args);
	}
	
	
	EventHandler<WindowEvent> stageHandler = new EventHandler<WindowEvent>() {
        public void handle(WindowEvent we) {
        	Stage helpStage = mUICtrl.getHelpStage();
        	if(helpStage != null){
        		helpStage.close();
        	}
        }
    };
	
}
