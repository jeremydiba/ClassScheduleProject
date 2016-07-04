package edu.gatech.scheduleproject.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class Schedule {
  private ArrayList<ArrayList<OfferedClass>> classpartition;
  private ArrayList<Student> newstudentlist;
  private static ArrayList<OfferedClass> classList;
  private ArrayList<Edge> edgeList;
  private int classesPerDay;
  private int df;
  private boolean allplaced;
  /**
   * constructor that creates a new schedule
   * @param an int with the number of classes per day
   */
  public Schedule(int classesPerDay) {
      this.classesPerDay = classesPerDay;
      classpartition = new ArrayList<ArrayList<OfferedClass>>(classesPerDay);
      newstudentlist = new ArrayList<>();
      classList = new ArrayList<>();
      edgeList = new ArrayList<>();
      df = 0;
  }
  /**
   * default constructor that sets classes per day to 6
   */
  public Schedule() {
      this(6);
  }
  /**
   * Resets the schedule lists
   */
  public void resetSchedule() {
	  classpartition.clear();
	  newstudentlist.clear();
	  classList.clear();
	  edgeList.clear();
  }
  /**
   * Adds a student to the schedule
   * @param student to be added
   */
  public void addStudent(Student s) {
      newstudentlist.add(s);
  }
  /**
   * removes a student from the schedule
   * @param Student to be removed
   * @return removed Student
   */
  public Student removeStudent(Student s) {
      if(newstudentlist.contains(s)) {
          newstudentlist.remove(s);
      }
      return s;
  }
  /**
   * Adds an OfferedClass to the schedule
   * @param OfferedClass to be added
   */
  public void addClass(OfferedClass o) {
      classList.add(o);
  }
  /**
   * removes an OfferedClass from the schedule
   * @param OfferedClass to be removed
   * @return removed OfferedClass
   */
  public OfferedClass removeClass(OfferedClass o) {
      if(classList.contains(o)) {
          classList.remove(o);
      }
      return o;
  }
  /**
   * Setter for df
   * @param int representing new df
   */
  public void setDf(int i) {
      df = i;
  }
  /**
   * Setter for classes per day
   * @param int representing new number of classes per day
   */
  public void setClassesPerDay(int i) {
      classesPerDay = i;
  }
  /**
   * Takes a string and finds a matching OfferedClass
   * @param String representing the name of the OfferedClass
   * @return found OfferedClass
   */
  public OfferedClass stringToClass(String str) {
      for(OfferedClass o : classList) {
          if(o.toString().equals(str)) {
              return o;
          }
      }
      return null;
  }
  /**
   * Removes an edge from the database
   * @param incident OfferedClass
   * @param adjacent OfferedClass
   */
  public void removeEdge(OfferedClass i, OfferedClass j) {
	  if (i != j) {
		  try {
			  Edge e = new Edge(i, j);
			  edgeList.remove(e);
		  } catch (Exception e) {
			  System.out.println("Could not find a data point to delete");
		  }
	  }
  }
  /**
   * Adds an edge to the database
   * @param incident OfferedClass
   * @param adjacent OfferedClass
   */
  public void addEdge(OfferedClass i, OfferedClass j) {
      if (i != j) {
          Edge e = new Edge(i, j);
          edgeList.add(e);
      }
  }
  private static ArrayList<OfferedClass> cloneClassList() {
	  ArrayList<OfferedClass> temp = new ArrayList<>();
	  for(OfferedClass o : classList) {
		  temp.add(o);
	  }
	  return temp;
  }
  private int edgeMatches(OfferedClass one, OfferedClass two) {
      int num = 0;
      Edge temp = new Edge(one, two);
      for(Edge e : edgeList) {
          if(e.equals(temp)) {
              num++;
          }
      }
      return num;
  }
  /**
   * Generates the Optimized Class List
   * @return boolean representing a successful save
   */
  public boolean generateClassList() {
	  int stopat = classesPerDay;
	  int tempcounter = 0;
	  ArrayList<OfferedClass> tempClassList = cloneClassList();
      ArrayList<OfferedClass> placed = new ArrayList<>();
      int numclasses = tempClassList.size();
      System.out.println(numclasses + " classes");
      int counter = 0;
      try {
    	  while(tempcounter < stopat && tempcounter < numclasses) {
    		  ArrayList<OfferedClass> temp;
    		  if(df == 0 ) {
    			  temp = new ArrayList<>();
    		  } else {	
    			  try {
    				  temp = classpartition.get(tempcounter);
    			  } catch (Exception e) {
    				  System.out.println("Caught index on index " + tempcounter);
    				  temp = classpartition.get((stopat) - 1);
    			  }
    		  }
    		  OfferedClass oc = tempClassList.get(counter);
    		  System.out.println("counter is " + counter);
    		  counter++;
    		  if(oc.getPlaceCounter() > 0 && !placed.contains(oc) && !oc.isPlaced()) {
    			  System.out.println("entered the oc if statement for " + oc.toString());
    			  int placer = oc.reducePlaceCounter();
    			  System.out.println("reduced place counter is " + oc.getPlaceCounter());
    			  if(oc.getPlaceCounter() < 1 && !oc.isPlaced()) {
    				  System.out.println("Placing a final in placer");
    				  temp.add(oc);
    				  placed.add(oc);
    				  oc.setPlaced(true);
    			  } else if(oc.getPlaceCounter() > 0 && !oc.isPlaced()) {
    				  System.out.println("placing a copy in placer");
    				  int copyno = oc.addCopy();
    				  OfferedClass copy = oc.copy(oc.getName() + " sec " + copyno);
    				  temp.add(copy);
    			  }
    			  for(OfferedClass cla : tempClassList) {
    				  System.out.println("entering for loop for " + cla.toString());
    				  int matches = this.edgeMatches(oc, cla);
    				  System.out.println(matches + " matches");
    				  if(matches <= df) {
    					  int test = cla.getPlaceCounter();
    					  if(cla.getPlaceCounter() > 1 && cla != oc && !cla.isPlaced()) {
    						  System.out.println("Placing a copy in placer");
    						  cla.reducePlaceCounter();
    						  int copyno = cla.addCopy();
    						  OfferedClass copy = cla.copy(cla.getName() + " sec " + copyno);
    						  temp.add(copy);
    					  } else if(cla.getPlaceCounter() <= 1 && cla != oc && !cla.isPlaced()) {
    						  System.out.println("Placing a final in placer");
    						  cla.reducePlaceCounter();
    						  temp.add(cla);
    						  placed.add(cla);
    						  cla.setPlaced(true);
    					  }
    				  }
    			  }
    			  System.out.println("Printing temp list");
    			  for(OfferedClass o : temp) {
    				  System.out.println(o.toString());
    			  }
    			  if(df == 0) {
    				  classpartition.add(temp);
    			  }
    			  tempcounter++;
    			  System.out.println("Tempcounter raised and is now " + tempcounter);
    		  }
    		  for (OfferedClass o : classList) {
    			  if (!o.isPlaced()) {
    				  allplaced = false;
    				  break;
    			  } else {
    				  allplaced = true;
    			  }
    		  }
    		  if(oc.equals(tempClassList.get((tempClassList.size() - 1))) && allplaced) {
    			  System.out.println("reached end of tempClassList true");
    			  return true;
    		  }
    	  }
  		} catch(Exception e) {
  			System.out.println("Caught.  No class to work with");
  		}
      if(placed.size() >= numclasses && allplaced) {
          return true;
      } else {
          df = df + 1;
          System.out.println("!! df increased !!");
          ArrayList<OfferedClass> ac = new ArrayList<>();
          for(OfferedClass c : tempClassList) {
        	  if(!c.isPlaced()) {
        		  ac.add(c);
        	  }
          }
          tempClassList = ac;
          generateClassList();
          return true;
      }
  }
  /**
   * Saves the data to an Excel File
   * @param name of Excel file
   */
  public void save(String fileName) {
	  XSSFWorkbook workbook = new XSSFWorkbook();
	  XSSFSheet sheet = workbook.createSheet("ClassSchedule");
	  XSSFSheet backSheet = workbook.createSheet("Background data");
	  Map<String,Object[]> data = new TreeMap<String, Object[]>();
	  data.put("1", new Object[]{"Peroid", "Classes"});
	  int listcounter = 1;
	  for(ArrayList<OfferedClass> list : classpartition) {
		  Object[] objectArray = new Object[list.size() + 1];
		  objectArray[0] = listcounter;
		  listcounter++;
		  for(int i = 1; i <= list.size(); i++) {
			  try {
				  objectArray[i] = list.get(i - 1).toString();
				  System.out.println("Set data point " + i);
			  } catch (Exception e) {
				  System.out.println("Entered Save Catch on item " + i + " In partition " + (listcounter - 1));
			  }
		  }
		  if(objectArray != null) {
			  data.put(((Integer)(listcounter + 1)).toString(), objectArray);
		  }
	  }
	  Set<String> keyset = data.keySet();
	  int rownum = 0;
	  for(String key : keyset) {
		  Row row = sheet.createRow(rownum++);
		  Object[] objArr = data.get(key);
		  int cellnum = 0;
		  for(Object obj : objArr) {
			  Cell cell = row.createCell(cellnum++);
			  if (obj instanceof String) {
				  cell.setCellValue((String)obj);
			  } else if (obj instanceof Integer) {
				  cell.setCellValue((Integer)obj);
			  }
		  }
	  }
	  Map<String, Object[]> classData = new HashMap<String, Object[]>(classList.size() + 1);
	  int classcounter = 2;
	  classData.put("1", new Object[] {"Class Name", "Class Capacity", "Number of Sections"});
	  for(OfferedClass o: classList) {
		  classData.put(((Integer)classcounter).toString(), new Object[] {o.getName(), o.getCapacity(), o.getSections()});
		  classcounter ++;
	  }
	  Set<String> classkeyset = classData.keySet();
	  rownum = 0;
	  for(String key: classkeyset) {
		  Row row = backSheet.createRow(rownum++);
		  Object[] objArr = classData.get(key);
		  int cellnum = 0;
			for (Object obj : objArr) {
				if(obj != null) {
					Cell cell = row.createCell(cellnum++);
					if(obj instanceof Boolean) {
						cell.setCellValue((Boolean)obj);
					} else if(obj instanceof String) {
						cell.setCellValue((String)obj);
					} else if(obj instanceof Double) {
						cell.setCellValue((Double)obj);
					} else if(obj instanceof Integer) {
						cell.setCellValue((Integer)obj);
					}
				}
			}
			
	  }
	  rownum = 10;
	  Map<String, Object[]> studentData = new HashMap<String, Object[]>(newstudentlist.size() + 1);
	  classcounter = 2;
	  studentData.put("1", new Object[] {"Student Name", "Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7", "Class 8"});
	  for(Student s: newstudentlist) {
		  Object[] studentInfo = new Object[9];
		  studentInfo[0] = s.getName();
		  OfferedClass[] ocArray = s.getRequestClasses();
		  for(int i = 1; i < 9; i++) {
			  studentInfo[i] = ocArray[i - 1];
			  if(studentInfo[i] != null) {
				  studentInfo[i] = studentInfo[i].toString();
			  }
		  }
		  studentData.put(((Integer)classcounter).toString(), studentInfo);
		  classcounter ++;
	  }
	  Set<String> studentkeyset = studentData.keySet();
	  for(String key: studentkeyset) {
		  Row row = backSheet.createRow(rownum++);
		  Object[] objArr = studentData.get(key);
		  int cellnum = 0;
			for (Object obj : objArr) {
				if(obj != null) {
					Cell cell = row.createCell(cellnum++);
					if(obj instanceof Boolean) {
						cell.setCellValue((Boolean)obj);
					} else if(obj instanceof String) {
						cell.setCellValue((String)obj);
					} else if(obj instanceof Double) {
						cell.setCellValue((Double)obj);
					} else if(obj instanceof Integer) {
						cell.setCellValue((Integer)obj);
					}
				}
			}
			
	  }
	  try {
		  FileOutputStream out = new FileOutputStream(new File(fileName));
		  workbook.write(out);
		  out.close();
		  workbook.close();
		  System.out.println("Write successful " + df + " df");
		  df = 0;
		  for(OfferedClass c : classList) {
        	  c.reset();
		  }
		  classpartition.clear();
	  } catch (Exception e) {
		  System.out.println("Save failed");
		  e.printStackTrace();
	  }
  }
  public void generateStudentsList() {
	  
  }

}
