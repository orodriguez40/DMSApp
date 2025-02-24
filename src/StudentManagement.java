// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// StudentManagement Class:
// This class manages all CRUD functions related to Students.

// Imported Library
import java.util.*;

public class StudentManagement {

    // Attribute to store all Students in an ArrayList.
    static final List<Student> students = new ArrayList<>();

    // Method is called to add a Student manually.
    public boolean addStudentManual(Scanner scanner) {
        boolean result = false; // Attribute to store and return boolean.

        // Loop to allow adding multiple Students.
        do {
            try {
                System.out.println("Please enter the following Student information:\n");

                // Get Student information through the user's input.
                int id = UserInput.manualIdInput(scanner); // Get a unique ID for the Student.
                String firstName = UserInput.firstNameInput(scanner, "First Name: "); // Get Student's first name.
                String lastName = UserInput.lastNameInput(scanner, "Last Name: "); // Get Student's last name.
                String phoneNumber = UserInput.phoneNumberInput(scanner, "Phone Number (Format (555) 555-5555): "); // Get Student's phone number.
                String email = UserInput.emailInput(scanner, "Email (Format example@gmail.com): "); // Get Student's email address.
                double gpa = UserInput.gpaInput(scanner); // Get the Student's GPA.
                boolean isContacted = UserInput.userConfirmation(scanner, "Has the student been contacted? y or n: "); //

                // Student information will be displayed before confirming.
                System.out.print("\nPlease verify information is correct:\n");
                System.out.println("\nID: S" + id +
                        "\nName: " + firstName + " " + lastName +
                        "\nPhone Number: " + phoneNumber +
                        "\nEmail: " + email +
                        "\nGPA: " + gpa +
                        "\nContacted: " + isContacted);

                // Asks user to confirm if they want to add Student.
                if (UserInput.userConfirmation(scanner, "\nAre you sure you want to add this Student? y or n: ")) {
                    // Add the new Student to the Students ArrayList.
                    students.add(new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted));
                    result = true; // Mark that a student was added
                    System.out.println("\nResult: " + result);
                    System.out.println("Student added successfully!");
                } else {
                    result = false;
                    System.out.println("\nResult: " + result);
                    System.out.println("Student not added.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter the correct data type.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the buffer
            }

            // Ask if the user wants to add another Student
        } while (UserInput.userConfirmation(scanner, "\nWould you like to add another student? y or n:\n"));

        System.out.println("\nReturning to the main menu.");
        return result;
    }

    // Method is called to add students by file upload.
    public boolean addStudentFile(Scanner scanner) {
        System.out.print("\nEnter the file path for the student list:\n");
        System.out.print("Example (C:\\Users\\<YourUsername>\\Desktop\\<YourFileName>.txt)\n ");
        String filePath = scanner.nextLine().trim();

        FileHandler fileUpload = new FileHandler();
        return fileUpload.addStudentsByFile(filePath, students, scanner); // Add students from the file and return success/failure
    }

    // Method is called to remove a student.
    public boolean removeStudent(Scanner scanner) {
        while (true) {
            try {
                int id = UserInput.studentIdSearch(scanner); // Calls helper method for the ID of the student to remove.
                Student student = UserInput.findStudentById(id); // Calls helper method to find the student by ID.

                // Checks if the student exists.
                if (student != null) {
                    // Student information will be displayed before confirming.
                    System.out.print("\nStudent Found:\n");
                    System.out.println("\nID: S" + student.getId() +
                            "\nName: " + student.getFirstName() + " " + student.getLastName() +
                            "\nPhone Number: " + student.getPhoneNumber() +
                            "\nEmail: " + student.getEmail() +
                            "\nGPA: " + student.getGpa() +
                            "\nContacted: " + student.getIsContacted());

                    // Ask for confirmation before deletion.
                    if (UserInput.userConfirmation(scanner, "\nAre you sure you want to delete this student? y or n: ")) {
                        students.remove(student); // Remove the student from the ArrayList.
                        System.out.println("\nStudent removed successfully. Returning to the main menu.");
                        return true; // Return success after removal
                    } else {
                        System.out.println("\nStudent deletion canceled. Returning to the main menu.");
                    }
                    return false; // Return false if no student is removed
                } else {
                    System.out.println("Student not found in the system.\n"); // Inform user ID is not in the system.

                    // Ask if the user wants to try again.
                    if (!UserInput.userConfirmation(scanner, "Would you like to try again? y or n\n(Entering n will take you back to the main menu): ")) {
                        return false; // Exit the method if user chooses not to try and return false
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid student ID.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the buffer
            }
        }
    }

    public boolean updateStudent(Scanner scanner) {
        UserInput userInput = new UserInput();
        int userChoice;
        boolean result = false;

        try {
            int studentId = UserInput.studentIdSearch(scanner);
            Student student = UserInput.findStudentById(studentId);
            if (student == null) {
                System.out.println("\nStudent not found.");
                return false; // Return false if student is not found
            }

            // Display current student details
            System.out.println("\nCurrent student details:");
            System.out.println(student);

            // Ask for confirmation to update
            if (UserInput.userConfirmation(scanner, "Do you want to update this student's information? (y/n):\n")) {
                do {
                    // Display the update options.
                    System.out.println("\nUpdate Menu");
                    System.out.println("Please select which value to update from the following options:\n");
                    System.out.println("1: ID");
                    System.out.println("2: First Name");
                    System.out.println("3: Last Name");
                    System.out.println("4: Phone Number");
                    System.out.println("5: Email");
                    System.out.println("6: GPA");
                    System.out.println("7: Contacted Status");
                    System.out.println("8: Return to the Main Menu");

                    // Calls method to verify user input.
                    System.out.println("\nEnter choice:");
                    userChoice = userInput.usersChoice(scanner);

                    // Switch statement to handle user choices.
                    switch (userChoice) {
                        case 1:
                            int id = UserInput.manualIdInput(scanner); // Updates student's ID.
                            student.setId(id);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 2:
                            String firstName = UserInput.firstNameInput(scanner, "First name: "); // Updates first name.
                            student.setFirstName(firstName);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 3:
                            String lastName = UserInput.lastNameInput(scanner, "Last name: "); // Updates last name.
                            student.setLastName(lastName);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 4:
                            String phoneNumber = UserInput.phoneNumberInput(scanner, "Phone number: "); // Updates phone number.
                            student.setPhoneNumber(phoneNumber);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 5:
                            String email = UserInput.emailInput(scanner, "Email: "); // Updates email.
                            student.setEmail(email);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 6:
                            double gpa = UserInput.gpaInput(scanner); // Updates GPA.
                            student.setGpa(gpa);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 7:
                            boolean isContacted = UserInput.userConfirmation(scanner, "Has this student been contacted? (y/n): "); // Updates contacted status.
                            student.setContacted(isContacted);
                            System.out.println("\nUpdated student details:");
                            System.out.println(student);
                            System.out.println("Returning to the update menu.");
                            result = true;
                            break;
                        case 8:
                            System.out.println("Returning to the main menu."); // User chooses to go back to the main menu.
                            break;
                        default:
                            System.out.println("Invalid option. Please enter a number from 1 to 8.");// Checks for invalid user input.
                            break;
                    }
                } while (userChoice != 8);
            } else {
                System.out.println("Returning to the main menu.");
                result = false;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid student ID.");
            scanner.nextLine(); // Clear the invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear the buffer
        }
        return result;
    }

    // Method to display specified student.
    public boolean viewStudent(Scanner scanner) {
        try {
            int studentId = UserInput.studentIdSearch(scanner);
            Student student = UserInput.findStudentById(studentId);
            if (student == null) {
                System.out.println("\nStudent not found.");
                return false; // Return false if student is not found.
            } else {
                System.out.println("\nStudent details:");
                System.out.println(student);
                System.out.println("Returning to the main menu.");
                return true;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid student ID.");
            scanner.nextLine(); // Clear the invalid input
            return false;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear the buffer
            return false;
        }
    }

    // Method is called to view all students.
    public boolean viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.\n"); // Inform user if no students are present.
            return false; // Return false if no students are found
        } else {
            students.sort(Comparator.comparing(Student::getId));
            System.out.println("\nList of students:");
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
        return true; // Return true after viewing students.
    }

    // Method to display students who have not been contacted and calculate GPA improvement
    public boolean notContacted() {
        boolean anyNotContacted = false; // Flag to check if there are students who haven't been contacted
        double targetGPA = 2.0;

        // Iterate through all students to check if they have been contacted.
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
                // Check GPA improvement opportunity.
                double gpaImprovement = targetGPA - student.getGpa();
                if (student.getGpa() < targetGPA) {
                    System.out.println("GPA improvement needed: " + String.format("%.2f", gpaImprovement));
                }
            }
        }

        if (!anyNotContacted) {
            System.out.println("There are no students available.");
        }
        return anyNotContacted; // Return true if any students were not contacted
    }
}
