package edu.gatech.scheduleproject.model;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
/**
 * Main Class for JavaFX application
 *
 * @author Jeremy DiBattista
 * @version 1
 */
@SuppressWarnings("restriction")
public class ClassGenerator extends Application {
    private ObservableList<Student> studentsOL = FXCollections.observableArrayList();
    private ListView<Student> studentLV = new ListView<Student>(studentsOL);
    private ObservableList<OfferedClass> classesOL = FXCollections.observableArrayList();
    private ListView<OfferedClass> classLV = new ListView<OfferedClass>(classesOL);
    private Schedule schedule = new Schedule();
    private ClassGenerator classGenerator;
    /**
     * Main Method
     * @param String[] arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Start method for JavaFX that runs and implements the GUI
     * @param Stage
     */
    public void start(Stage primaryStage) {
    	classGenerator = new ClassGenerator();
        //Creates the load, save, and number classes
        BorderPane root = new BorderPane();
        VBox loadsavebox = new VBox(10);
        HBox loadBox = new HBox();
        TextField loadField = new TextField("Input file to be loaded.  Do not add .xlsx");
        Button loadButton = new Button("Load from textfile");
        loadButton.setOnAction(e -> {
        	try {
        		String loadName = loadField.getCharacters().toString() + ".xlsx";
        		ArrayList<OfferedClass> classToAdd = classGenerator.loadClasses(loadName);
        		classesOL.clear();
            	studentsOL.clear();
            	schedule.resetSchedule();
        		for(OfferedClass o: classToAdd) {
        			schedule.addClass(o);
                    classesOL.add(o);
                    Button deleteClass = new Button("Delete");
                    deleteClass.setOnAction(e2 -> {
                        OfferedClass cla = (OfferedClass)((Button)e2.getSource()).getParent();
                        classesOL.remove(cla);
                        schedule.removeClass(cla);
                    });
                    o.getChildren().add(deleteClass);
        		}
        		ArrayList<Student> studentToAdd = classGenerator.loadStudents(loadName);
        			for(Student s : studentToAdd) {
        				schedule.addStudent(s);
                        studentsOL.add(s);
                        OfferedClass[] requestClass = s.getRequestClasses();
                        for(int i = 0; i < 8; i++) {
                       		if(requestClass[i] != null) {
                       			for(OfferedClass c : requestClass) {
                       				if(c != requestClass[i] && c != null) {
                       					schedule.addEdge(requestClass[i], c);
                       				}
                        		}
                        	}
                        }
                        Button deleteStu = new Button("Delete");
                        deleteStu.setOnAction(e2 -> {
                            Student stu = (Student)((Button)e2.getSource()).getParent();
                            studentsOL.remove(stu);
                            schedule.removeStudent(stu);
                            OfferedClass[] oc = stu.getRequestClasses();
                      	  		for(int i = 0; i < 8; i++) {
                      	  			if(oc[i] != null) {
                      	  				for(OfferedClass c : oc) {
                      	  					if(c != oc[i] && c != null) {
                      	  						schedule.removeEdge(oc[i], c);
                      	  					}
                      	  				}
                      	  			}
                      	  		}
                        });
                        s.getChildren().add(deleteStu);
        			}
        	} catch(Exception ex) {
        		System.out.println("Try again! File was not found, or database was corrupted.");
        	}
        });
        loadBox.getChildren().addAll(loadField, loadButton);
        HBox saveBox = new HBox();
        TextField saveField = new TextField("Input name of file to save");
        Button saveButton = new Button("Save and Generate");
        saveButton.setOnAction(e -> {
        	if(classesOL.size() > 0 && studentsOL.size() > 0){
        		if(!saveField.getCharacters().toString().equals("Input name of file to save")) {
        			schedule.generateClassList();
        			String saveName = saveField.getCharacters().toString() + ".xlsx";
        			schedule.save(saveName);
        		} else {
        			schedule.generateClassList();
        			schedule.save("ClassList.xlsx");
        		}
        	}
        });
        saveBox.getChildren().addAll(saveField, saveButton);
        HBox numClassesBox = new HBox();
        TextField numClassesField = new TextField("Enter number of classes per sttudent (defaut 6)");
        Button changeNumClasses = new Button("Change");
        changeNumClasses.setOnAction(e -> {
            try {
                String newnum = numClassesField.getCharacters().toString();
                int intnumclass = Integer.parseInt(newnum);
                if (intnumclass > 0 && intnumclass < 9) {
                    schedule.setClassesPerDay(intnumclass);
                }
            } catch (Exception j) {
                System.out.println("only values 1-8");
            }
        });
        numClassesBox.getChildren().addAll(numClassesField, changeNumClasses);
        loadsavebox.getChildren().addAll(loadBox, saveBox, numClassesBox);
        root.setTop(loadsavebox);
        //creates the create student section
        HBox centerBox = new HBox(10);
        Text createStudentBoxTitle = new Text("Add Student");
        VBox createStudentBox = new VBox(15);
        HBox studentNameBox = new HBox(15);
        TextField studentLName = new TextField("Last Name");
        TextField studentFName = new TextField("First Name");
        studentNameBox.getChildren().addAll(studentLName, studentFName);
        TextField[] classes = new TextField[8];
        classes[0] = new TextField("Input class 1");
        classes[1] = new TextField("Input class 2");
        classes[2] = new TextField("Input class 3");
        classes[3] = new TextField("Input class 4");
        classes[4] = new TextField("Input class 5");
        classes[5] = new TextField("Input class 6");
        classes[6] = new TextField("Input class 7");
        classes[7] = new TextField("Input class 8");
        Button addStudent = new Button("Add Student");
        addStudent.setOnAction(e -> {
            OfferedClass[] requestClasses = new OfferedClass[8];
            String lname = studentLName.getCharacters().toString();
            String fname = studentFName.getCharacters().toString();
            String name = fname + " " + lname;
            for(int i = 0; i < 8; i++) {
                String takenClass = classes[i].getCharacters().toString();
                requestClasses[i] = schedule.stringToClass(takenClass);
                if(requestClasses[i] != null) {
                    for(OfferedClass c : requestClasses) {
                        if(c != requestClasses[i] && c != null) {
                            schedule.addEdge(requestClasses[i], c);
                        }
                    }
                }
            }
            Student s = new Student(name, requestClasses);
            boolean isDuplicate = false;
            for(Student stu : studentsOL) {
                if(stu.equals(s)) {
                    isDuplicate = true;
                }
            }
            if(!isDuplicate) {
                schedule.addStudent(s);
                studentsOL.add(s);
                Button deleteStu = new Button("Delete");
                deleteStu.setOnAction(e2 -> {
                    Student stu = (Student)((Button)e2.getSource()).getParent();
                    studentsOL.remove(stu);
                    schedule.removeStudent(stu);
                    OfferedClass[] oc = stu.getRequestClasses();
              	  		for(int i = 0; i < 8; i++) {
              	  			if(oc[i] != null) {
              	  				for(OfferedClass c : oc) {
              	  					if(c != oc[i] && c != null) {
              	  						schedule.removeEdge(oc[i], c);
              	  					}
              	  				}
              	  			}
              	  		}
                });
                s.getChildren().add(deleteStu);
            }
        });
        createStudentBox.getChildren().addAll(createStudentBoxTitle, studentNameBox, classes[0], classes[1],
            classes[2], classes[3], classes[4], classes[5], classes[6], classes[7], addStudent);
        //Creates the add class section
        VBox createClassBox = new VBox(15);
        Text createClassTitle = new Text("Create new Class");
        TextField className = new TextField("Input class name");
        TextField capacity = new TextField("Input class capacity");
        TextField sections = new TextField("Input number of sections");
        Button addClass = new Button("Add Class");
        addClass.setOnAction(e -> {
            String classtitle = className.getCharacters().toString();
            try {
                int capac = Integer.parseInt(capacity.getCharacters().toString());
                int sect = Integer.parseInt(sections.getCharacters().toString());
                if(capac < 1) {
                    capac = 100;
                }
                if(sect < 0) {
                    sect = 1;
                }
                OfferedClass o = new OfferedClass(classtitle, capac, sect);
                boolean isDuplicate = false;
                for(OfferedClass oc : classesOL) {
                    if(oc.equals(o)) {
                        isDuplicate = true;
                    }
                }
                if(!isDuplicate) {
                    schedule.addClass(o);
                    classesOL.add(o);
                    Button deleteClass = new Button("Delete");
                    deleteClass.setOnAction(e2 -> {
                        OfferedClass cla = (OfferedClass)((Button)e2.getSource()).getParent();
                        classesOL.remove(cla);
                        schedule.removeClass(cla);
                    });
                    o.getChildren().add(deleteClass);
                }

            } catch(Exception j) {
                System.out.println("Either capacity or section isn't a number");
            }
        });
        createClassBox.getChildren().addAll(createClassTitle, className,
            capacity, sections, addClass);
        //Creates a viewable list of students
        VBox studentListBox = new VBox(15);
        Text studentListTitle = new Text("List of Students");
        studentListBox.getChildren().addAll(studentListTitle, studentLV);
        //Creates a viewable list of classes
        VBox classListBox = new VBox(15);
        Text classListTitle = new Text("list of classes");
        classListBox.getChildren().addAll(classListTitle, classLV);
        //General adding and putting together
        centerBox.getChildren().addAll(createStudentBox, createClassBox, studentListBox, classListBox);
        root.setCenter(centerBox);
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Class Schedule Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private static Object getCellValue(Cell cell) {
	    switch (cell.getCellType()) {
	    case Cell.CELL_TYPE_STRING:
	        return cell.getStringCellValue();
	 
	    case Cell.CELL_TYPE_BOOLEAN:
	        return cell.getBooleanCellValue();
	 
	    case Cell.CELL_TYPE_NUMERIC:
	        Double d = cell.getNumericCellValue();
	        int i = d.intValue();
	        return i;
	    }
	 
	    return null;
	}
    /**
     * Loads the Offered Classes from an Excel File
     * @param A String containing the filename
     * @return An ArrayList of OfferedClass that contains the classes to be added
     */
    public ArrayList<OfferedClass> loadClasses(String filename) throws Exception {
    	try {
    		FileInputStream file = new FileInputStream(new File(filename));
    		XSSFWorkbook workbook = new XSSFWorkbook (file);
    		XSSFSheet sheet = workbook.getSheetAt(1);
    		Iterator<Row> rowIterator = sheet.iterator();
    		ArrayList<OfferedClass> toAddList = new ArrayList<>();
    		//adding all of the classes
    		int skipFirstRow = 0;
    		int loopCounter = 0;
    		String firstVal = "nothing yet";
			while(rowIterator.hasNext() && !(firstVal.equals("Student Name"))) {
				loopCounter++;
				Row row = rowIterator.next();
				if(skipFirstRow == 0) {
					row = rowIterator.next();
					skipFirstRow = -1;
				}
				
				//For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				ArrayList<Object> dataCollector = new ArrayList<>(3);
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					Object cellval = getCellValue(cell);
					dataCollector.add(cellval);
				}
				 try {
					System.out.println(((dataCollector.get(0).toString()) + " " + (dataCollector.get(1).toString()) + " " + (dataCollector.get(2).toString())));
					firstVal = dataCollector.get(0).toString();
					System.out.println("firstVal is now " + firstVal);
					if(!firstVal.equals("Student Name") && firstVal != null) {
						String classCapS = dataCollector.get(1).toString();
						int classCap = Integer.parseInt(classCapS);
						String numSecS = dataCollector.get(2).toString();
						int numSec = Integer.parseInt(numSecS);
						OfferedClass newClass = new OfferedClass(firstVal, classCap, numSec);
						System.out.println(newClass.toString());
						toAddList.add(newClass);
					}
				} catch(Exception e) {
					System.out.println("Corrupt offeredclass");
					e.printStackTrace();
				}
			}
    	return toAddList;
    	} catch(Exception e) {
    		System.out.println("likely issue is file not found, or file is open.  Please try again.");
    		e.printStackTrace();
    		Exception exc = new Exception();
    		throw exc;
    	}
    }
    /**
     * Loads the Students from an Excel File
     * @param A String containing the filename
     * @return An ArrayList of Students that contains the students to be added
     */
    public ArrayList<Student> loadStudents(String filename) throws Exception {
    	try {
    		FileInputStream file = new FileInputStream(new File(filename));
    		XSSFWorkbook workbook = new XSSFWorkbook (file);
    		XSSFSheet sheet = workbook.getSheetAt(1);
    		Iterator<Row> rowIterator = sheet.iterator();
    		ArrayList<Student> toAddList = new ArrayList<>();
    		//adding all of the classes
    		int loopCounter = 0;
    		String firstVal = "nothing yet";
			while(rowIterator.hasNext() && !(firstVal.equals("Student Name"))) {
				loopCounter++;
				System.out.println("firstVal is now " + firstVal);
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell firstCell = cellIterator.next();
				Object firstCellval = getCellValue(firstCell);
				firstVal = firstCellval.toString(); 
			}
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				//For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				ArrayList<Object> dataCollector = new ArrayList<>();
				Cell firstCell = cellIterator.next();
				if(cellIterator.hasNext()) {
					Object firstCellval = getCellValue(firstCell);
					firstVal = firstCellval.toString();
				} else {
					firstVal = null;
				}
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					Object cellval = getCellValue(cell);
					dataCollector.add(cellval);
				}
				 try {
					
					System.out.println("firstVal is now " + firstVal);
					if(!firstVal.equals("Student Name") && firstVal != null) {
						OfferedClass[] studentClasses = new OfferedClass[8];
						int dataSize = dataCollector.size();
						for(int i = 0; i < dataSize; i++) {
							studentClasses[i] = schedule.stringToClass((String)dataCollector.get(i));
							if(studentClasses[i] != null) {
								for(OfferedClass c : studentClasses) {
									if(c != studentClasses[i] && c != null) {
										schedule.addEdge(studentClasses[i], c);
									}
		                        }
		                    }
		                }
						Student newStudent = new Student(firstVal, studentClasses);
						toAddList.add(newStudent);
					}
				} catch(Exception e) {
					System.out.println("Corrupt student");
					e.printStackTrace();
				}
			}
    	return toAddList;
    	} catch(Exception e) {
    		System.out.println("likely issue is file not found, or file is open.  Please try again.");
    		e.printStackTrace();
    		Exception exc = new Exception();
    		throw exc;
    	}
    }
}
