// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// Admin Class (Main Application):
// This is where the DMS application will run.
// The user will open the JAR file through the CLI.
// They will have options to add a student manually or by file,
// remove a student based on their ID, view all students,
// update student information, perform the custom action,
// or close the application.

// Imported Library
import java.util.Scanner;

public class Admin {

    // Attributes are instances of the Authenticator, StudentManagement, and UserInput classes.
    private static final Authenticator authencator = new Authenticator();
    private static final StudentManagement studentManagement = new StudentManagement();
    private static final UserInput userInput = new UserInput();

    // Main Method
    public static void main(String[] args) {

        // Scanner is used to accept all user input.
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        // Welcome message for the user.
        System.out.println("Welcome to the Success University DMS student outreach application!");

        /* Add authenticator here. */
        // Main menu will iterate until the user chooses to close the application.
        do {
            // Display the main menu options.
            viewMenu();
            // Calls method to verify user input.
            System.out.println("\nEnter choice:");
            userChoice = userInput.usersChoice(scanner);

            // Switch statement to handle user choices.
            switch (userChoice) {
                case 1:
                    StudentManagement.addStudentManual(scanner); // Adds a student manually.
                    break;
                case 2:
                    StudentManagement.addStudentFile(scanner); // Add student(s) by file upload.
                    break;
                case 3:
                    StudentManagement.removeStudent(scanner); // Removes a student.
                    break;
                case 4:
                    StudentManagement.updateStudent(scanner); // Updates any student information.
                    break;
                case 5:
                    StudentManagement.viewAllStudents(); // View all students.
                    break;
                case 6:
                    StudentManagement.notContactedStudents(); // View all students who have not been contacted and calculates their GPA.
                    break;
                case 7:
                    System.out.println("Thank you for using the Success University's DMS application. Goodbye!"); // Message when user chooses to close application.
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number from 1 to 7."); // Checks for invalid user input.
                    break;
            }
        } while (userChoice != 7); // Continue looping until the user chooses to exit.

        scanner.close(); // Closes the scanner instance.
    }

    // Method is called display the main menu options.
    public static void viewMenu() {
        System.out.println("\nMain Menu");
        System.out.println("Please select from the following options:\n");
        System.out.println("Enter 1 to add a student manually");
        System.out.println("Enter 2 to add student(s) by file upload");
        System.out.println("Enter 3 to remove a student");
        System.out.println("Enter 4 to update student information");
        System.out.println("Enter 5 to view all students");
        System.out.println("Enter 6 to view students who have not been contacted and calculate their GPA");
        System.out.println("Enter 7 to exit the application");
    }

}


