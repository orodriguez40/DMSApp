// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// StudentManagement Class:
// This class manages all functions related to Students.

//Imported Library
import java.util.*;

public class StudentManagement {


// Attribute to store all Students in an Arraylist.
static final List<Student> students = new ArrayList<>();

// Method is called to add a Student manually.
public static boolean addStudentManual(Scanner scanner) {

    //Attribute to store and return boolean.
    boolean result = false;

    // Loop to allow adding multiple Students.
    do {

        System.out.println("Please enter the following Student information:\n");

        // Get Student information through the user's input.
        int id = UserInput.manualIdInput(scanner); // Get a unique ID for the Student.
        String firstName = UserInput.firstNameInput(scanner, "First Name: "); // Get Student's first name.
        String lastName = UserInput.lastNameInput(scanner, "Last Name: "); // Get Student's last name.
        String phoneNumber = UserInput.phoneNumberInput(scanner, "Phone Number (Format (555) 555-5555 ): "); // Get Student's phone number.
        String email = UserInput.emailInput(scanner, "Email (Format example@gmail.com ): "); // Get Student's email address.
        double gpa = UserInput.gpaInput(scanner); // Get the Student's GPA.
        boolean isContacted = UserInput.userConfirmation(scanner,"Has the student been contacted? y or n: "); //

        //Student information will be displayed before confirming.
        System.out.print("\nPlease verify information is correct:\n");
        System.out.println("\nID: S" + id +
                            "\nName: " + firstName + " " + lastName +
                            "\nPhone Number: " + phoneNumber +
                            "\nEmail: " + email +
                            "\nGPA: " + gpa +
                            "\nContacted: " + isContacted);

        //Asks user to confirm if they want to add Student.
        if (UserInput.userConfirmation(scanner, "\nAre you sure you want to add this Student? y or n: ")) {
            // Add the new Student to the Students Arraylist.
            students.add(new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted));
            result = true; // Mark that a student was added
            System.out.println("\nResult: " + result);
            System.out.println("\nStudent added successfully!");
        } else {
            System.out.println("\nResult: " + result);
            System.out.println("\nStudent not added.");
        }

        // Ask if the user wants to add another Student
    } while (UserInput.userConfirmation(scanner, "\nWould you like to add another student? y or n:\n"));

    System.out.println("\nReturning to the main menu.");
    return result;
    }

    // Method is called to add students by file upload.
    public static void addStudentFile(Scanner scanner) {
        // Prompt user for the file location.
        System.out.print("\nEnter the file path for the student list:\n");
        System.out.print("Example (C:\\Users\\<YourUsername>\\Desktop\\<YourFileName>.txt)\n ");
        String filePath = scanner.nextLine().trim();

        // Create FileHandler class instance to process the file.
        FileHandler fileUpload = new FileHandler();
        fileUpload.addStudentsByFile(filePath, students, scanner); // Add students from the file.
    }
}
