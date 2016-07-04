package edu.gatech.scheduleproject.model;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;

public class Student extends GridPane {
    private OfferedClass[] requestClasses;
    private String name;
    private static int numstudents = 0;
    private HBox studentBox;
    /**
     * constructor that creates a new Student
     * @param a String representing the name of the student
     * @param An array of the classes the student is planning to take
     */
    public Student(String name, OfferedClass requestClasses[]) {
        this.name = name;
        this.requestClasses = requestClasses;
        numstudents++;
        studentBox = new HBox();
        Text studentName = new Text(name + ": ");
        studentBox.getChildren().add(studentName);
        for (OfferedClass o : requestClasses) {
            try {
                String classtaken = o.toString();
                Text textClass = new Text(classtaken + " ");
                studentBox.getChildren().add(textClass);
            } catch (Exception e) {
                String classtaken = "DNE";
                Text textClass = new Text(classtaken + " ");
                studentBox.getChildren().add(textClass);
            }
        }
        this.add(studentBox, 1 , 1);
    }
    /**
     * getter for the student's requested classes
     * @return OfferedClass[] of the students requested classes
     */
    public OfferedClass[] getRequestClasses() {
		return requestClasses;
	}
    /**
     * setter for the student's requested classes
     * @param OfferedClass[] of the students requested classes
     */
	public void setRequestClasses(OfferedClass[] requestClasses) {
		this.requestClasses = requestClasses;
	}
    /**
     * equals method that compares names of students
     * @param Object to be compared
     * @return true of they have the same name, false otherwise
     */
	public boolean equals(Object o) {
        if (o instanceof Student) {
            if((((Student)o).getName()).equals(name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * getter for the student's name
     * @return String for the student's name
     */
    public String getName() {
        return name;
    }
}