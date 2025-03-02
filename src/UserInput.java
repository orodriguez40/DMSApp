// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// UserInput Class:
// This class handles all user inputs and confirmations.

import java.io.File;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class UserInput {

    // Method checks for user's choice in the main menu and the update menu.
    public static int usersChoice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("\nEnter your choice (1-8): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clears the buffer.
                if (choice >= 1 && choice <= 8) {
                    return choice; // Return valid input.
                }
                System.out.println("Invalid choice. Please select a number between 1 and 8.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clears invalid input.
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Error: Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to allow retry.
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to allow retry.
            }
        }
    }

    //Method is called to collect student information and calls the appropiate methods.
    public static Student getStudentInfo(Scanner scanner) {

        // Loop to allow adding multiple Students.
        do {
            try {
                System.out.println("Please enter the following Student information:\n");

                // Get Student information through the user's input.
                int id = manualIdInput(scanner); // Get a unique ID for the Student.
                String firstName = firstNameInput(scanner, "First Name: "); // Get Student's first name.
                String lastName = lastNameInput(scanner, "Last Name: "); // Get Student's last name.
                String phoneNumber = phoneNumberInput(scanner, "Phone Number (Format (555) 555-5555): "); // Get Student's phone number.
                String email = emailInput(scanner, "Email (Format example@gmail.com): "); // Get Student's email address.
                double gpa = gpaInput(scanner); // Get the Student's GPA.
                boolean isContacted = userConfirmation(scanner, "Has the student been contacted? y or n: "); //

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
                    return new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted);
                } else {
                    return null;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter the correct data type.");
                scanner.nextLine(); // Clear the invalid input
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Input interrupted. Please try again.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the buffer
            }

            // Ask if the user wants to add another Student.
        } while (UserInput.userConfirmation(scanner, "\nWould you like to add another student? y or n:\n"));
        System.out.println("\nReturning to the main menu.");
        return null;
    }

    // Method to get a patron ID from user input.
    public static int manualIdInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("ID: ");
                String input = scanner.nextLine().trim();
                if (input.matches("\\d{8}") && Integer.parseInt(input) >= 10000000) {
                    int id = Integer.parseInt(input);

                    // Checks if the ID is unique.
                    if (StudentManagement.students.stream().noneMatch(student -> student.getId() == id)) {
                        return id; // Valid ID.
                    } else {
                        System.out.println("ID is already in use. Please enter a different number.");
                    }
                } else {
                    System.out.println("Invalid ID. It must be an 8-digit number starting with '1'.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Input issue detected. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    // Method is called to verify first and last name inputs from the user.
    public static String validateString(Scanner scanner, String prompt, int maxLength) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty() && input.length() <= maxLength) {
                    return input;
                } else {
                    System.out.printf("Invalid input. Enter up to %d characters.%n", maxLength);
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    // Methods are called to verify string length for first and last names.
    public static String firstNameInput(Scanner scanner, String prompt) {
        return validateString(scanner, prompt, 15);
    }

    public static String lastNameInput(Scanner scanner, String prompt) {
        return validateString(scanner, prompt, 25);
    }

    // Method is called to enter and verify phone number format entered by the user.
    public static String phoneNumberInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.matches("\\(\\d{3}\\)\\s?\\d{3}-\\d{4}") || input.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) {
                    return input;
                } else {
                    System.out.println("Invalid input. Phone number must be in format (555) 555-5555.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    // Method is called to check for unique and correctly formated email entered by the user.
    public static String emailInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")) {
                    if (StudentManagement.students.stream().noneMatch(student -> Objects.equals(student.getEmail(), input))) {
                        return input; // Valid email.
                    } else {
                        System.out.println("Email is already in use. Please enter a different one.");
                    }
                } else {
                    System.out.println("Invalid email. Format: example@gmail.com");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    // Method is called to check for a valid GPA entered by the user.
    public static double gpaInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("GPA: ");
                if (scanner.hasNextDouble()) {
                    double gpa = scanner.nextDouble();
                    scanner.nextLine(); // Clears buffer.
                    if (gpa >= 0 && gpa <= 1.9) {
                        return gpa;
                    } else {
                        System.out.println("Invalid GPA. Must be between 0 and 1.9.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a numeric value.");
                    scanner.nextLine(); // Clears invalid input.
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }
    public static String getFileInfo(Scanner scanner) {
        String filePath;
        while (true) {
            System.out.print("\nEnter the file path for the student list:\n");
            System.out.print("Example (C:\\Users\\<YourUsername>\\Desktop\\<YourFileName>.txt)\n ");
            filePath = scanner.nextLine().trim(); // Return file path as a string.

            // Validate the file path
            if (isValidFilePath(filePath)) {
                return filePath; // Return valid file path
            } else {
                System.out.println("Invalid file path. Please check and try again.");
            }
        }
    }

    // Method to validate the file path
    private static boolean isValidFilePath(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            return file.exists() && file.isFile(); // Check if the file exists and is a file.
        }
        return false; // Invalid file path.
    }

    // Method searches for a student based on their ID, displays the information, and returns the student.
    public static Student searchStudentByID(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter student ID: ");
                String input = scanner.nextLine().trim();
                int id = Integer.parseInt(input);

                // Check if ID is exactly 8 digits.
                if (id >= 10000000 && id <= 99999999) {
                    // Find the Student by ID.
                    Student foundStudent = StudentManagement.students.stream()
                            .filter(student -> student.getId() == id)
                            .findFirst()
                            .orElse(null); // Return null if no student is found

                    // If student is found, display their information.
                    if (foundStudent != null) {
                        // Student information will be displayed.
                        System.out.print("\nStudent Details:\n");
                        System.out.println("\nID: S" + foundStudent.getId() +
                                "\nName: " + foundStudent.getFirstName() + " " + foundStudent.getLastName() +
                                "\nPhone Number: " + foundStudent.getPhoneNumber() +
                                "\nEmail: " + foundStudent.getEmail() +
                                "\nGPA: " + foundStudent.getGpa() +
                                "\nContacted: " + foundStudent.getIsContacted());

                        return foundStudent; // Return the found student.
                    } else {
                        // No student found with the given ID.
                        System.out.println("No student found with the given ID.");
                    }
                } else {
                    // Invalid ID. It must be exactly 8 digits.
                    System.out.println("Invalid ID. It must be exactly 8 digits.");
                }
            } catch (NumberFormatException e) {
                // Invalid input. Please enter a valid 8-digit number.
                System.out.println("Invalid input. Please enter a valid 8-digit number.");
            } catch (NoSuchElementException | IllegalStateException e) {
                // Unexpected input issue. Please try again.
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to avoid issues
            } catch (Exception e) {
                // Unexpected error occurred. Please try again.
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to avoid issues
            }
        }
    }

    // Method is called to confirm user's choice when performing CRUD operations or the Custom Action.
    public static boolean userConfirmation(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("y") || choice.equals("yes")) {
                    return true;
                } else if (choice.equals("n") || choice.equals("no")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Enter 'y' for yes or 'n' for no.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    // Method is called to confirm deletion of a student.
    public static boolean confirmDeletion(Scanner scanner) {
        return userConfirmation(scanner, "\nAre you sure you want to delete this student? y or n: ");
    }

    //Method is called to confirm if user wants to update student information.
    public static boolean confirmUpdate(Scanner scanner) {
        return userConfirmation(scanner, " \nAre you sure you want to update this student? y or n: " );
    }

    // Method is called to confirm is the user wants to enter a new input(try again or enter new information).
    public static boolean newInput(Scanner scanner) {
        return userConfirmation(scanner, "Would you like to enter a new input? y or n\n(Entering n will take you back to the main menu): ");
    }

}
