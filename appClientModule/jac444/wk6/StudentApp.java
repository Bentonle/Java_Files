package jac444.wk6;

//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.Map;

import javafx.application.Application;
//import javafx.fxml.FXML;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * StudentApp class
 * - Used as starting base for SceneBuilder application.
 * @author Benton Le
 *
 */
public class StudentApp extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("Student App");
			primaryStage.setScene(scene);
			primaryStage.show();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}