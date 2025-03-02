// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// Admin Class (Main Application):
// This is where the DMS application will run.
// The user will open the JAR file through the CLI.
// They will have options to add a student manually or by file,
// remove a student based on their ID, view students,
// update student information, perform the custom action,
// or close the application.

// Imported Library
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Admin {

    // Main Method
    public static void main(String[] args) {

        // Attributes are instances of the Authenticator, StudentManagement, and UserInput classes.
        Authenticator authenticator = new Authenticator();
        StudentManagement studentManagement = new StudentManagement();


        // Create an instance of Admin to access non-static methods.
        Admin admin = new Admin();

        // Scanner is used to accept all user input.
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;

        // Welcome message for the user.
        System.out.println("\nWelcome to the Success University DMS student outreach application!");

        // Authenticate user before accessing the menu.
        while (!authenticator.authenticate(scanner)) {
            System.out.println("Invalid username or password. Please try again.");
        }

        // Main menu will iterate until the user chooses to close the application.
        do {
            // Display the main menu options.
            admin.viewMenu();
            // Calls method to verify user input.
            try {
                userChoice = UserInput.usersChoice(scanner);

                // Switch statement to handle user choices.
                switch (userChoice) {
                    case 1:
                        Student newStudent = UserInput.getStudentInfo(scanner); // Collects student information.
                        boolean studentAdded = studentManagement.addStudentManual(newStudent); // Checks if a student was added correctly.
                        break;
                    case 2:
                        String filepath = UserInput.getFileInfo(scanner); // Collects the file path for upload.
                        boolean fileUploaded = studentManagement.addStudentFile(filepath, scanner); // Checks if file was uploaded successfully.
                        break;
                    case 3:
                        Student removeStudent = UserInput.searchStudentByID(scanner); // Collects student ID for removal.
                        boolean studentRemoved = studentManagement.removeStudent(removeStudent); // Checks if student was removed sucessfully.
                        break;
                    case 4:
                        Student updateStudent = UserInput.searchStudentByID(scanner); // Collects student ID for update.
                        boolean updatedStudent = studentManagement.updateStudent(updateStudent); // Checks if student information was updated sucessfully.
                        break;
                    case 5:
                        Student viewStudent = UserInput.searchStudentByID(scanner); // Collects studetn ID to view student details.
                        boolean viewedStudent = studentManagement.viewStudent(viewStudent); // Checks if student was found and displayed.
                        break;
                    case 6:
                        boolean viewedStudents = studentManagement.viewAllStudents(); // Checks if student(s) were found and displayed.
                        break;
                    case 7:
                        boolean notContactedList = studentManagement.notContacted(); // Cheks if all students who are not contacted are displayed and their GPA calculated.
                        break;
                    case 8:
                        System.out.println("Thank you for using the Success University's DMS application. Goodbye!\n"); // Message when user chooses to close application.
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number from 1 to 8."); // Checks for invalid user input.
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input.
            } catch (IllegalStateException | NoSuchElementException e) {
                System.out.println("Input error detected. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to prevent closure issues.
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (userChoice != 8); // Continue looping until the user chooses to exit.

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
        System.out.println("Enter 5 to search for student");
        System.out.println("Enter 6 to view all students");
        System.out.println("Enter 7 to view students who have not been contacted and calculate their GPA");
        System.out.println("Enter 8 to exit the application");
    }
}