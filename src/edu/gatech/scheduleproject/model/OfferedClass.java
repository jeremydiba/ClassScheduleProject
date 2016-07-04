package edu.gatech.scheduleproject.model;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;

public class OfferedClass extends GridPane {
    private String name;
    private int capacity;
    private int currentlyEnrolled;
    private boolean hasMultSections;
    private int sections;
    private ArrayList<Student> enrolled;
    private HBox classBox;
    private int defaultnumclasses = 6;
    private int placedCounter;
    private int copies;
    private boolean placed;
    /**
     * returns a boolean reflecting the placed value
     * @return Boolean for the placed value
     */
    public boolean isPlaced() {
		return placed;
	}
    /**
     * Changes the value of placed
     * @param Boolean to become the placed value
     */
	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
	@SuppressWarnings("restriction")
    /**
     * Constructor that creates a new OfferedClass
     * @param A String containing the name of the class
     * @param an int containing the capacity of the class
     * @param an int containing the number of sections of this class
     */
	public OfferedClass(String name, int capacity, int sections) {
        this.name = name;
        this.capacity = capacity;
        this.sections = sections;
        copies = 1;
        placed = false;
        if (sections <= 0 || sections == 1) {
            hasMultSections = false;
            placedCounter = 1;
            currentlyEnrolled = 0;
            enrolled = new ArrayList<>(capacity);
        } else {
            hasMultSections = true;
            placedCounter = sections;
            currentlyEnrolled = 0;
            enrolled = new ArrayList<>(capacity);
        }
        classBox = new HBox();
        Text classViewable = new Text(name + ": Capacity " + capacity + " Sections " + sections);
        classBox.getChildren().add(classViewable);
        this.add(classBox, 1, 1);
    }
    /**
     * Returns the value for hasMultSections
     * @return Boolean hasMultSections
     */
    public boolean hasMultSections() {
		return hasMultSections;
	}
    /**
     * Sets the value for hasMultSections
     * @param Boolean hasMultSections
     */
	public void setHasMultSections(boolean hasMultSections) {
		this.hasMultSections = hasMultSections;
	}
    /**
     * default constructor that only needs a name
     * @param A String containing the name of the class
     */
	public OfferedClass(String name) {
        this(name, 100, 0);
    }
    /**
     * Returns the number of copies
     * @return int number of copies of this class
     */
    public int getCopies() {
		return copies;
	}
    /**
     * Sets the number of copies
     * @param int new number of copies
     */
	public void setCopies(int copies) {
		this.copies = copies;
	}
    /**
     * Adds a copy and returns the new value
     * @return int number of copies of this class
     */
	public int addCopy() {
        copies = copies + 1;
        return copies;
    }
    /**
     * ToString method that returns DNE if null and the name of the OfferedClass otherwise
     * @return String name of the class
     */
    public String toString() {
        if(this == null) {
            return "DNE";
        } else {
            return name;
        }
    }    
    /**
     * Equals method that returns true if two classes equal each other
     * @param Object to be compared
     * @return boolean representing if they are equal
     */
    public boolean equals(Object o) {
        if(o instanceof OfferedClass && o != null) {
            if(((OfferedClass)o).toString().equals(name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Getter for the name
     * @return String for the name of the class
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for the number of sections
     * @return int for the number of sections
     */
    public int getSections() {
        return sections;
    }
    /**
     * Reduces the placedCounter by one
     * @return int for the new placedCounter
     */
    public int reducePlaceCounter() {
        placedCounter = placedCounter - 1;
        return placedCounter;
    }
    /**
     * Getter for placedCounter
     * @return int for placedCounter
     */
    public int getPlaceCounter() {
        return placedCounter;
    }
    /**
     * Enrolls a student in a class of enrolled students
     * @param Student to be enrolled
     */
    public void enroll(Student s) throws Exception {
        if(currentlyEnrolled > capacity) {
            currentlyEnrolled++;
            enrolled.add(s);
        } else {
            Exception e = new Exception();
            throw e;
        }
    }
    /**
     * Getter for Capacity
     * @return int for Capacity
     */
    public int getCapacity() {
		return capacity;
	}
    /**
     * Setter for Capacity
     * @param int for new capacity
     */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
    /**
     * Creates a hard copy of the current class
     * @param String representing the name of the copied class
     * @return OfferedClass copied
     */
	public OfferedClass copy(String n) {
        OfferedClass copy = new OfferedClass(n, capacity, sections);
        return copy;
    }
    /**
     * resets the placedCounter, copies, and placed value
     */
    public void reset() {
    	placedCounter = sections;
    	copies = 1;
    	placed = false;
    }
}
