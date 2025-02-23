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
    public boolean addStudentManual(Scanner scanner) {

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
    public boolean addStudentFile(Scanner scanner) {
        // Prompt user for the file location.
        System.out.print("\nEnter the file path for the student list:\n");
        System.out.print("Example (C:\\Users\\<YourUsername>\\Desktop\\<YourFileName>.txt)\n ");
        String filePath = scanner.nextLine().trim();

        // Create FileHandler class instance to process the file.
        FileHandler fileUpload = new FileHandler();
        return fileUpload.addStudentsByFile(filePath, students, scanner); // Add students from the file and return success/failure
    }

    // Method is called to remove a student.
    public boolean removeStudent(Scanner scanner) {
        // Loop to allow user to remove multiple students.
        while (true) {
            int id = UserInput.studentIdSearch(scanner); // Calls helper method the ID of the student to remove.
            Student student = UserInput.findStudentById(id); // Calls helper method to find the student by ID.

            // Checks if the student exists.
            if (student != null) {

                //student information will be displayed before confirming.
                System.out.print("\nStudent Found:\n");
                System.out.println("\nID: S" + student.getId() +
                        "\nName: " + student.getFirstName() + " " + student.getLastName() +
                        "\nPhone Number: " + student.getPhoneNumber() +
                        "\nEmail: " + student.getEmail() +
                        "\nGPA: " + student.getGpa() +
                        "\nContacted: " + student.getIsContacted());

                // Ask for confirmation before deletion.
                if (UserInput.userConfirmation(scanner, "\nAre you sure you want to delete this student? y or n: ")) {
                    students.remove(student); // Remove the student from the Arraylist.
                    System.out.println("\nStudent removed successfully. Returning to the main menu."); // User is notified student is added.
                    return true; // Return success after removal
                } else {
                    System.out.println("\nStudent deletion canceled. Returning to the main menu."); // User is notified of cancellation.
                }
                return false; // Return false if no student is removed
            } else {
                System.out.println("Student not found in the system.\n"); // Inform user ID is not in the system.

                // Ask if the user wants to try again.
                if (!UserInput.userConfirmation(scanner, "Would you like to try again? y or n\n(Entering n will take you back to the main menu): ")) {
                    return false; // Exit the method if user chooses not to try and return false
                }
            }
        }
    }

    public boolean updateStudent(Scanner scanner) {
        // Ask for the student ID to update
        int studentId = UserInput.studentIdSearch(scanner);

        // Find the student by ID
        Student student = UserInput.findStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return false; // Return false if student is not found
        }

        // Display current student details
        System.out.println("Current student details:");
        System.out.println(student);

        // Ask for confirmation to update
        if (UserInput.userConfirmation(scanner, "Do you want to update this student's information? (y/n): ")) {

            // Option to update each detail
            if (UserInput.userConfirmation(scanner, "Do you want to update the first name? (y/n): ")) {
                String firstName = UserInput.firstNameInput(scanner, "First name: ");
                student.setFirstName(firstName);
            }

            if (UserInput.userConfirmation(scanner, "Do you want to update the last name? (y/n): ")) {
                String lastName = UserInput.lastNameInput(scanner, "Last name: ");
                student.setLastName(lastName);
            }

            if (UserInput.userConfirmation(scanner, "Do you want to update the phone number? (y/n): ")) {
                String phoneNumber = UserInput.phoneNumberInput(scanner, "Phone number: ");
                student.setPhoneNumber(phoneNumber);
            }

            if (UserInput.userConfirmation(scanner, "Do you want to update the email? (y/n): ")) {
                String email = UserInput.emailInput(scanner, "Email: ");
                student.setEmail(email);
            }

            if (UserInput.userConfirmation(scanner, "Do you want to update the GPA? (y/n): ")) {
                double gpa = UserInput.gpaInput(scanner);
                student.setGpa(gpa);
            }

            if (UserInput.userConfirmation(scanner, "Do you want to update the contacted status? (y/n): ")) {
                boolean isContacted = UserInput.userConfirmation(scanner, "Is this student contacted? (y/n): ");
                student.setContacted(isContacted);
            }

            System.out.println("Student information updated successfully.");
            return true; // Return true if updated successfully
        } else {
            System.out.println("Update canceled.");
            return false; // Return false if update is canceled
        }
    }


    // Method is called to view all students.
    public boolean viewAllStudents() {
        // Check if there are any students in the ArrayList.
        if (students.isEmpty()) {
            System.out.println("\nNo students found.\n"); // Inform user if no students are present.
            return false; // Return false if no students are found
        } else {
            //Sorts by ID number.
            students.sort(Comparator.comparing(Student::getId));
            System.out.println("\nList of students:");
            // Iterate through each student and format the output.
            for (Student student : students) {
                System.out.println("\nID: S" + student.getId() +
                        "\nName: " + student.getFirstName() + " " + student.getLastName() +
                        "\nPhone Number: " + student.getPhoneNumber() +
                        "\nEmail: " + student.getEmail() +
                        "\nGPA: " + student.getGpa() +
                        "\nContacted: " + student.getIsContacted() + "\n");
            }
        }
        System.out.println("Returning to the main menu.");
        return true; // Return true after viewing students
    }

    // Method to display students who have not been contacted and calculate GPA improvement
    public boolean notContacted() {
        boolean anyNotContacted = false; // Flag to check if there are students who haven't been contacted
        double targetGPA = 2.0;

        // Iterate through all students to check if they have been contacted
        for (Student student : students) {
            if (!student.getIsContacted()) {
                anyNotContacted = true;
                // Display student information
                System.out.println("\nID: S" + student.getId() +
                        "\nName: " + student.getFirstName() + " " + student.getLastName() +
                        "\nPhone Number: " + student.getPhoneNumber() +
                        "\nEmail: " + student.getEmail() +
                        "\nGPA: " + student.getGpa() +
                        "\nContacted: " + student.getIsContacted());
                // Check GPA improvement opportunity
                if (student.getGpa() < targetGPA) {
                    System.out.println("GPA improvement needed: " + (targetGPA - student.getGpa()));
                }
            }
        }

        if (!anyNotContacted) {
            System.out.println("There are no student available.");
        }
        return anyNotContacted; // Return true if any students were not contacted
    }
}
