package jac444.wk6;

import java.io.BufferedWriter;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.UUID;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * This Controller class is the operator of this program.
 * - This is where all functions are implemented for when:
 *   - Buttons are pressed or values are entered
 * @author Benton Le
 *
 */
public class Controller implements Initializable {

	// - - - TextField JavaFXML controls - - -
	// - Used to obtain user input into fields.
	//@FXML private TextField fieldFile;
	@FXML private TextField fieldName;
	@FXML private TextField fieldCourse;
	@FXML private TextField fieldGrade;
	@FXML private TextField fieldDelete;
	@FXML private TextField fieldEdit;
	
	@FXML private TextField fieldUser;
	@FXML private TextField fieldServer;
	@FXML private TextField fieldPass;
	
	// - - - Label JavaFXML control - - -
	// - Used to alert user of changes or failures.
	@FXML private Label lblAlert;
	@FXML private Label lblFile;
	@FXML private Label lblHidden;
	
	// - - - Button JavaFXML controls - - -
	// - Used to disable/enable buttons.
	@FXML private Button btnAdd;
	@FXML private Button btnDelete;
	@FXML private Button btnSave;
	@FXML private Button btnEdit;
	@FXML private Button btnLoad;
	
	@FXML private Button btnLink;
	
	// - - - Table View/Column JavaFXML controls - - -
	// - Used to display data onto TableView.
	@FXML private TableView<Student> tableView;
	@FXML private TableColumn<Student, Integer> tableId;
	@FXML private TableColumn<Student, String> tableName;
	@FXML private TableColumn<Student, String> tableCourse;
	@FXML private TableColumn<Student, Integer> tableGrade;
	
	// Collection where Student objects are stored.
	ObservableList<Student> collection_ = FXCollections.observableArrayList();
	
	private DbConnection dc;
	
	/**
	 * This function intializes any data in it.
	 * - In this case it activates the table 'cells' to show data from
	 *   objects.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Fetching data...");
				
		tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
		tableGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
	}
	
	/**
	 * THis function links the data the user enters into database.
	 */
	public void linkButton() {
		
		// These lines are used to get text from fields.
		String tmpServer = fieldServer.getText();
		String tmpUser = fieldUser.getText();
		String tmpPass = fieldPass.getText();
		
		btnLoad.setDisable(true);
		
		try {
			// Sets up connection.
			dc = new DbConnection(tmpServer, tmpUser, tmpPass);
			
			// Gets setup connection.
			Connection conn = dc.getConnection();
			
			// Selects data from database.
			ResultSet rs = conn.createStatement().executeQuery("select * from Students");
			
			// Adds each column into tableView.
			while(rs.next()) {
				collection_.add(new Student(Integer.parseInt(rs.getString("ID")), rs.getString("NAME"), rs.getString("COURSE"), Integer.parseInt(rs.getString("GRADE"))));
			}
			colorPass();
			lblAlert.setText("Link: Table successfully linked.");
			
			// Shows data on table view.
			tableView.setItems(collection_);
		} catch (SQLException e) {
			colorFail();
			lblAlert.setText("Link: Table failed to link with data.");
			System.err.println("Error: " + e);
		}
	}
	
	/**
	 * This function generates a unique id.
	 * @return Integer.parseInt(str);
	 */
	public static int generateUniqueId() {      
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;        
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
	
	/**
	 * This function loads all data from file once pressed.
	 * - If file valid.
	 *   - Create Student object and store data from file into it.
	 *   - Store that Student into collection.
	 */
	public void loadButton() {
	
		btnLink.setDisable(true);
		
		// Removes any previous collection_. (Used for when load is clicked twice)
		collection_.removeAll(collection_);
		
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);
		fc.getExtensionFilters().addAll(new ExtensionFilter("Jar Files", "*.jar"));
		
		lblFile.setText(selectedFile.getName());
		lblHidden.setText(selectedFile.getAbsolutePath());
		
		// Gets file from field.
		String file = selectedFile.getAbsolutePath();
		int fileLength = file.length();
		if(file.compareTo("") != 0) {
			if(file.substring(fileLength - 4, fileLength).compareTo(".txt") == 0) {
				Scanner scan = null;
				
				try {
					scan = new Scanner(new File(file));
			
					// Adds student into collection once initialized.
					while(scan.hasNext())
						collection_.add(new Student(scan.nextInt(), scan.next(), scan.next(), scan.nextInt()));
				
					// Views collection onto table.
					tableView.setItems(collection_);
				} catch (FileNotFoundException e) {
					colorFail();
					lblAlert.setText("Load: File does not exist.");
					e.printStackTrace();
				} finally {
						if(scan != null)
							scan.close();
				}
				colorPass();
				lblAlert.setText("Load: File loaded successfully.");
				enableButtons();
			}
			else {
				colorFail();
				lblAlert.setText("Load: Please select a .txt text file.");
				lblFile.setText("No File Chosen");
			}
		}
		else {
			colorFail();
			lblAlert.setText("Load: Please enter a file, before pressing the button.");
		}
	}
	
	/**
	 * This function saves any modified data from collection_
	 * into file.
	 * - This functions delete's file then recreates it.
	 *   - And stores data from collection_ into file.
	 * @throws IOException
	 */
	public void saveButton() throws IOException {
		String file = lblHidden.getText();
		
		if(file.compareTo("") != 0) {
			File f = new File(file);
			FileWriter fw = new FileWriter(f);
			BufferedWriter buff = new BufferedWriter(fw);
			
			// Deletes file then creates a new one.
			if(f.exists())
				f.delete();
			f.createNewFile();
			
			// Stores each Student from collection_ into file.
			for(int x = 0; x < collection_.size(); x++)
				buff.write(collection_.get(x).getId() + " " + collection_.get(x).getName() + " " +
						   collection_.get(x).getCourse() + " " + collection_.get(x).getGrade() + " ");
			
			buff.close();
			clearFields();
			
			colorPass();
			lblAlert.setText("Save: Successfully Saved.");
		}
		else {
			colorFail();
			lblAlert.setText("Save: Please enter valid text file.");
		}
	}
	
	/**
	 * This function adds a new student into collection_.
	 */
	public void addButton() {
		
		String tempName = fieldName.getText();
		String tempCourse = fieldCourse.getText();
		int tempGrade = Integer.parseInt(fieldGrade.getText());
		
		if(!(tempName.contentEquals("") || tempCourse.contentEquals("") || tempGrade < 0)) {
			if(tempGrade >= 0 && tempGrade <= 100) {
				collection_.add(new Student(generateUniqueId(), tempName, tempCourse, tempGrade));
				colorPass();
				lblAlert.setText("Add: Student successfully added into collection. ");		
			}
			else {
				colorFail();
				lblAlert.setText("Add: Please enter a grade between 0 and 100.");
			}
		}
		else {
			colorFail();
			lblAlert.setText("Add: Please Enter data for every field.");
		}
		clearFields();
	}
	/**
	 * This function deletes a selected record.
	 */
	public void deleteButton() {
		
		int tempID = Integer.parseInt(fieldDelete.getText());
		Boolean found = false;
		if(tempID != 0) {
			for(int x = 0; x < collection_.size() - 1; x++) {
				if(collection_.get(x).getId() == tempID) {
					collection_.remove(x);
					found = true;
				}
			}
			if(found) {
				colorPass();
				lblAlert.setText("Delete: Student found and successfully deleted.");
			}
			else {
				colorFail();
				lblAlert.setText("Delete: Student ID not found.");
			}
			clearFields();
		}
		else {
			colorFail();
			lblAlert.setText("Delete: Please enter a value beofre pressing the button.");
		}
	}
	/**
	 * This function edits a record specified.
	 */
	public void editButton() {
		
		int tempID = Integer.parseInt(fieldEdit.getText());
		String tempName = fieldName.getText();
		String tempCourse = fieldCourse.getText();
		int tempGrade = Integer.parseInt(fieldGrade.getText());
		
		Boolean found = false;
		if(tempID != 0) {
			for(int x = 0; x < collection_.size() - 1; x++) {
				if(collection_.get(x).getId() == tempID) {
					found = true;
					if(!(tempName.contentEquals("") || tempCourse.contentEquals("") || tempGrade < 0)) {
						if(tempGrade >= 0 && tempGrade <= 100) {
							collection_.set(x, new Student(collection_.get(x).getId(), tempName, tempCourse, tempGrade));
							
							colorPass();
							lblAlert.setText("Edit: Student successfully modified into collection. ");		
						}
						else {
							colorFail();
							lblAlert.setText("Edit: Please enter a grade between 0 and 100.");
						}
					}
					else {
						colorFail();
						lblAlert.setText("Edit: Please Enter data for every field.");
					}
					clearFields();
				}
			}
			if(found) {
				colorPass();
				lblAlert.setText("Edit: Student found and successfully modified.");
			}
		}
		else {
			colorFail();
			lblAlert.setText("Edit: Please enter a value before presing the button.");
		}
	}
	
	
	
	/**
	 * Changes Label text color.
	 */
	public void colorPass() { lblAlert.setTextFill(Color.web("#00FF00")); }
	public void colorFail() { lblAlert.setTextFill(Color.web("#FF0000")); }
	
	/**
	 *  This function disable the buttons.
	 */
	public void disableButtons() { btnDelete.setDisable(true); btnAdd.setDisable(true); btnSave.setDisable(true); btnEdit.setDisable(true); }
	/**
	 * This function enables the buttons.
	 */
	public void enableButtons() { btnLoad.setDisable(false); btnDelete.setDisable(false); btnAdd.setDisable(false); btnSave.setDisable(false); btnEdit.setDisable(false); }
	/**
	 * This function clears data from all fields.
	 */
	public void clearFields() { 
		fieldName.clear(); 
		fieldCourse.clear(); 
		fieldGrade.clear(); 
		fieldDelete.clear(); 
	
		fieldServer.clear();
		fieldUser.clear();
		fieldPass.clear();
	}
	/**
	 * This function clears entire screen.
	 */
	public void newButton() { collection_.clear(); clearFields(); colorPass(); lblAlert.setText("New: Table successfully cleared."); lblFile.setText("No File Chosen"); disableButtons(); btnLink.setDisable(false);}
}
