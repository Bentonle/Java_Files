package jac444.wk6;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Student class is the base class
 * - Where id, name, course and grade are stored.
 * - Basic constructor to set values is initialized.
 * - Getter and Setter's are used for this class.
 * @author Benton Le
 *
 */
public class Student {
	
	/*private int id;
	private String name;
	private String course;
	private int grade;
	*/
	
	// Properties to store user info.
	private IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
	private StringProperty name = new SimpleStringProperty(this, "name", "");
	private StringProperty course = new SimpleStringProperty(this, "course", "");
	private IntegerProperty grade = new SimpleIntegerProperty(this, "grade", 0);
	
	// Basic constructor to set values.
	public Student(Integer id, String name, String course, Integer grade) {
		this.id.set(id);
		this.name.set(name);
		this.course.set(course);
		this.grade.set(grade);
	}
	
	// - - - Class getter's - - -
	public final int getId() { return id.get(); }
	public final String getName() { return name.get(); }
	public final String getCourse() { return course.get(); }
	public final int getGrade() { return grade.get(); }
	
	// - - - Class setter's - - -
	public void setId(int id) { this.id.set(id); }
	public void setName(String name) { this.name.set(name); }
	public void setCourse(String course) { this.course.set(course); }
	public void setGrade(int grade) {this.grade.set(grade); }
}
