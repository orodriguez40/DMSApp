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

    // Main Method
    public static void main(String[] args) {

        // Attributes are instances of the Authenticator, StudentManagement, and UserInput classes.
         Authenticator authenticator = new Authenticator();
         StudentManagement studentManagement = new StudentManagement();
         UserInput userInput = new UserInput();

        // Create an instance of Admin to access non-static methods.
        Admin admin = new Admin();

        // Scanner is used to accept all user input.
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        // Welcome message for the user.
        System.out.println("Welcome to the Success University DMS student outreach application!");

        // Authenticate user before accessing the menu.
        while (!authenticator.authenticate(scanner)) {
            System.out.println("Invalid username or password. Please try again.");
        }

        // Main menu will iterate until the user chooses to close the application.
        do {
            // Display the main menu options.
            admin.viewMenu();
            // Calls method to verify user input.
            System.out.println("\nEnter choice:");
            userChoice = userInput.usersChoice(scanner);

            // Switch statement to handle user choices.
            switch (userChoice) {
                case 1:
                    studentManagement.addStudentManual(scanner); // Adds a student manually.
                    break;
                case 2:
                    studentManagement.addStudentFile(scanner); // Add student(s) by file upload.
                    break;
                case 3:
                    studentManagement.removeStudent(scanner); // Removes a student.
                    break;
                case 4:
                    studentManagement.updateStudent(scanner); // Updates any student information.
                    break;
                case 5:
                    studentManagement.viewAllStudents(); // View all students.
                    break;
                case 6:
                    studentManagement.notContacted(); // View all students who have not been contacted and calculates their GPA.
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

    // Method is called to display the main menu options.
    public void viewMenu() {
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
