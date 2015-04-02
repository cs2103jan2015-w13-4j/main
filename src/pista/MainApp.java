package pista;

import java.io.IOException;

import pista.log.CustomLogging;
import pista.logic.Logic;
import pista.parser.MainParser;
import pista.storage.Storage;
import pista.ui.RootLayoutController;
import pista.ui.UIController;
import javafx.application.Application;
import javafx.event.EventHandler;
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

public class MainApp extends Application {
	
	private Stage primaryStage;
    private BorderPane rootLayout;
      
    
    private UIController mUICtrl = null;
    private RootLayoutController mRootCtrl = null;
    
    
    //initialises the stage
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.PRODUCT_NAME);
        //this.primaryStage.initModality(Modality.NONE);
        this.primaryStage.initStyle(StageStyle.DECORATED);
        this.primaryStage.setResizable(false);
        
        initRootLayout();
        initMainUI();
        
        mRootCtrl.setUIController(mUICtrl);

    }
    
    
    //sets up the base root layout inclusive of the menu bar
    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.ROOT_LAYOUT_PATH));
            rootLayout = (BorderPane) loader.load();

            mRootCtrl = loader.getController();
            
            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("images/pista_icon.png"));
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	//sets up inside the base root layout the main UI
    public void initMainUI() {
        try {
            // Load MainUI
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Constants.MAIN_UI_LAYOUT_PATH));
            AnchorPane personOverview = (AnchorPane) loader.load();

            mUICtrl = loader.getController();
            
            // Set MainUI into the center of RootLayout
            rootLayout.setCenter(personOverview);      
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns primary stage
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}
