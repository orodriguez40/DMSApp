// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// StudentManagement Class:
// This class manages all CRUD functions related to Students.

// Imported Library
import java.util.*;

public class StudentManagement {

    // Attribute to store all Students in an ArrayList.
    static final List<Student> students = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    // Method is called to add a Student manually.
    public boolean addStudentManual(Student newStudent) {
            if (newStudent != null) {
                students.add(newStudent);
                System.out.println("\nStudent added successfully!");
                return true; // Successfully added student.
            } else {
                System.out.println("Student not added. Returning to the main menu");
                return false; // Student not added.
            }
    }

    // Method is called to add students by file upload.
    public boolean addStudentFile(String filePath, Scanner scanner) {
        if (filePath != null && !filePath.isEmpty()) {
            FileHandler fileHandler = new FileHandler();
            boolean result = fileHandler.addStudentsByFile(filePath, students, scanner); // Checks if file was processed successfully.
            if (result) {
                System.out.println("\nReturning to the main menu.");
                return true; // File processed successfully.
            }
        } else {
            System.out.println("Returning to the main menu.");
            return false; // File path was invalid or user chooses not to upload file.
        }
        return false;
    }

    // Method is called to remove a student.
    public boolean removeStudent(Student removeStudent) {
        try {
            // Check if the student exists before proceeding with deletion.
            if (removeStudent != null) {
                // Ask for confirmation before deletion using the new method.
                if (UserInput.confirmDeletion(scanner)) {
                    students.remove(removeStudent); // Remove the student from the ArrayList.
                    System.out.println("\nStudent removed successfully. Returning to the main menu.");
                    return true; // Return success after removal
                } else {
                    System.out.println("\nStudent deletion canceled. Returning to the main menu.");
                    return false; // Return false if deletion is canceled
                }
            } else {
                System.out.println("Returning to the main menu."); // Inform user ID is not in the system.
                return false; // Return false if student is not found
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return false; // Exit on error.
        }
    }


    // Method is called to update student information.
    public boolean updateStudent(Student updateStudent) {
        // Attribute to store user's choice for the update menu.
        int userChoice;

        // A variable to track if any updates were made.
        boolean isUpdated = false;

        if (updateStudent != null) {

            try {
                // Ask for confirmation to update.
                if (UserInput.confirmUpdate(scanner)) {
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
                        userChoice = UserInput.usersChoice(scanner);

                        // Switch statement to handle user choices.
                        switch (userChoice) {
                            case 1:
                                int id = UserInput.manualIdInput(scanner); // Updates student's ID.
                                if (id != -1) { // Check for valid ID input.
                                    updateStudent.setId(id);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. ID was not updated.");
                                }
                                break;
                            case 2:
                                String firstName = UserInput.firstNameInput(scanner, "First name: "); // Updates first name.
                                if (firstName != null) { // Check for valid first name input.
                                    updateStudent.setFirstName(firstName);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. First name was not updated.");
                                }
                                break;
                            case 3:
                                String lastName = UserInput.lastNameInput(scanner, "Last name: "); // Updates last name.
                                if (lastName != null) { // Check for valid last name input.
                                    updateStudent.setLastName(lastName);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. Last name was not updated.");
                                }
                                break;
                            case 4:
                                String phoneNumber = UserInput.phoneNumberInput(scanner, "Phone number: "); // Updates phone number.
                                if (phoneNumber != null) { // Check for valid phone number input.
                                    updateStudent.setPhoneNumber(phoneNumber);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. Phone number was not updated.");
                                }
                                break;
                            case 5:
                                String email = UserInput.emailInput(scanner, "Email: "); // Updates email.
                                if (email != null) { // Check for valid email input.
                                    updateStudent.setEmail(email);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. Email was not updated.");
                                }
                                break;
                            case 6:
                                double gpa = UserInput.gpaInput(scanner); // Updates GPA.
                                if (gpa != -1) { // Check for valid GPA input.
                                    updateStudent.setGpa(gpa);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. GPA was not updated.");
                                }
                                break;
                            case 7:
                                Boolean isContacted = UserInput.userConfirmation(scanner, "Has this student been contacted? (y/n): "); // Updates contacted status.
                                if (isContacted != null) { // Check for valid contacted status input.
                                    updateStudent.setContacted(isContacted);
                                    System.out.println("\nUpdated student details:");
                                    System.out.println(updateStudent);
                                    System.out.println("Returning to the update menu.");
                                    isUpdated = true; // Mark as updated.
                                } else {
                                    System.out.println("Invalid input. Contacted status was not updated.");
                                }
                                break;
                            case 8:
                                System.out.println("\nReturning to the main menu.");
                                break;
                            default:
                                System.out.println("Invalid input. Please select a valid option.");
                                break;
                        }


                    } while (userChoice != 8);
                } else {
                    System.out.println("\nReturning to the main menu.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter the correct data type.");
                scanner.nextLine(); // Clear the buffer
            } catch (Exception e) {
                System.out.println("Invalid input. Press enter to exit the update menu.");
                scanner.nextLine(); // Clear the buffer
            }

        }
        return isUpdated; // Return true if any updates were made, false otherwise.
    }

    // Method to display specified student.
    public boolean viewStudent(Student viewStudent) {
            if (viewStudent == null) {
                return false; // Return false if student is not found.
            } else {
                return true; // Return true if student is found.
            }
        }

    // Method is called to view all students.
    public boolean viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.\n"); // Inform user if no students are present.
            return false; // Return false if no students are found.
        } else {
            students.sort(Comparator.comparing(Student::getLastName));
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
