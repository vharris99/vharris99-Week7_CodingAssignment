package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;
/* 
 *<----- MENU-DRIVEN APPLICATION -----> 
 * --Takes user input from console 
 * --Performs CRUD Operations on project tables 
 */
public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	// @formatter:off
	private List<String> operations = List.of(
		"1) Add a project",
		"2) List projects",
		"3) Select a project"
	);
	// @formatter:on
	
	// Entry point
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}
	// Prints operations, user menu
	// Repeats until user terminates
	private void processUserSelections() {
		boolean done = false;
		
		while(!done) {
		  try {
			int selection = getUserSelection();
			  
			switch(selection) {
			  	case -1:
			  	  done = exitMenu();
			  	  break;
			  	
			  	case 1: 
			  	  createProject();
			  	  break;
			  	
			  	case 2:
			  	  listProjects();
			  	  break;
			  	  
			  	case 3:
			  	  selectProject();
			  	  break;
			  	  
			  	default:  
			  		System.out.println("\n" + selection + " is not a valid selection. Try again.");
			  		break;
			  }
			}
		    catch(Exception e) {
		  	  System.out.println("\nError: " + e + " Try again.");
		  	}
		}
	}
	/* Lists project ID's & names so user can select a Project ID
	 * @param none
	 * @returns nothing
	 * if success -> current project set to returned project
	 */
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		// Unselects current project
		curProject = null;
		// Throws exception if invalid project ID is entered
		curProject = projectService.fetchProjectById(projectId);
	}
	// No parameters, returns nothing
	// Prints the ID and name separated by ": ", for each Project 
	private void listProjects() {
	  List<Project> projects = projectService.fetchAllProjects();
	  
	  System.out.println("\nProjects:");
	  
	  projects.forEach(project -> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
	}
	
	// Collects user input for project row; calls project service to create row
	private void createProject() {
	  String projectName = getStringInput("Enter the project name");
	  BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
	  BigDecimal actualHours = getDecimalInput("Enter the actual hours");
	  Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
	  String notes = getStringInput("Enter the project notes");
	  
	  Project project = new Project();
	  
	  project.setProjectName(projectName);
	  project.setEstimatedHours(estimatedHours);
	  project.setActualHours(actualHours);
	  project.setDifficulty(difficulty);
	  project.setNotes(notes);
	  
	  Project dbProject = projectService.addProject(project);
	  System.out.println("You have successfully created project: " + dbProject);
	}

    /* Converts user input into BigDecimal
     * -if success --> returns BigDecimal value
     * -if error --> DbException thrown
     */
	private BigDecimal getDecimalInput(String prompt) {
	  String input = getStringInput(prompt);
		
	  if(Objects.isNull(input)) { 
	    return null;
	  }
			
	  try {
		return new BigDecimal(input).setScale(2);
	  }
	  catch(NumberFormatException e) {
		throw new DbException(input + " is not a valid decimal number.");
	  }
	}
	// Method called when user wants to exit app. 
	private boolean exitMenu() {
		System.out.println("Exiting the Menu.");
		return true;
	}
	// Prints available menu selections
	// Converts menu selections & returns them as an int or -1
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}
	// Prints prompt --> obtains user input --> converts user input to Integer
	private Integer getIntInput(String prompt) {
	  String input = getStringInput(prompt);
		
	  if(Objects.isNull(input)) { 
		return null;
	  }
		
	  try {
		return Integer.valueOf(input);
	  }
	  catch(NumberFormatException e) {
		throw new DbException(input + " is not a valid number.");
	  }
	}
	// Prints prompt --> obtains user input
	// -returns null if user enters nothing
	// -returns trimmed input, otherwise
	private String getStringInput(String prompt) {
	  System.out.print(prompt + ": ");
	  String input = scanner.nextLine();
		
	  return input.isBlank() ? null : input.trim();
	}
	
	// Print menu selections, 1 per line
	private void printOperations() {
	  System.out.println("\nThese are the availale selections. Press the 'Enter' key to quit:");
	  // Mod. to display currently selected project, if any; Prints all details
	  operations.forEach(line -> System.out.println("  " + line));
	  // Displays whether or not a project is currently selected
	  if(Objects.isNull(curProject)) {
	    System.out.println("\nYou are not working with a project.");
	  } else {
		  System.out.println("\nYou are working with project: " + curProject);
	  }
	}
}