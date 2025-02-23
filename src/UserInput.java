// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// UserInput Class:
// This class handles all user inputs and confirmations.


//Imported Libraries
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class UserInput {

    // Method checks for user's choice in the main menu.
    public int usersChoice(Scanner scanner) {
        while (true) {
            try {
                // Get user input and validate it.
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clears the buffer.
                if (choice >= 1 && choice <= 7) {
                    return choice; // Return valid input.
                }
                System.out.println("Please try again. Select a number between 1 and 7."); // Error message for invalid numeric input.
            } catch (InputMismatchException e) {
                System.out.println("Please try again. Input must be a number."); // Error message for non-numeric input.
                scanner.nextLine(); // Clears invalid input.
            }
        }
    }

    // Method to get a patron ID from user input.
    public static int manualIdInput(Scanner scanner) {
        int id; // Local variable to hold the student ID.

        // Loops until a valid 8-digit ID is entered.
        while (true) {
            System.out.print("ID: ");
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{8}") && Integer.parseInt(input) >= 10000000) { // Check if the input is a 8-digit number and >= 10000000
                id = Integer.parseInt(input);

                // Checks if the ID is unique.
                int finalId = id;
                if (StudentManagement.students.stream().noneMatch(student -> student.getId() == finalId)) {
                    return finalId; // Returns the valid ID.
                } else {
                    System.out.println("ID is already in use. Please enter a different number.");
                }
            } else {
                System.out.println("Invalid ID. It must be a 8-digit number and must start with a 1. Exclude the 'S'.Please try again.");
            }
        }
    }

    // Method is called to get the first name input from the user.
    public static String firstNameInput(Scanner scanner, String prompt) {
        String input = ""; // Initialize input to an empty string.

        // Loop until valid input is received
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Read and trim input

            if (!input.isEmpty() && input.length() <= 15) {
                return input; // Valid input, return it
            } else {
                System.out.println("Invalid input. Enter 15 characters or less.");
            }
        }
    }

    // Method is called to get the last name input from the user.
    public static String lastNameInput(Scanner scanner, String prompt) {
        String input = ""; // Initialize input to an empty string.

        // Loop until valid input is received
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Read and trim input

            if (!input.isEmpty() && input.length() <= 25) {
                return input; // Valid input, return it
            } else {
                System.out.println("Invalid input. Enter 25 characters or less.");
            }
        }
    }

    // Method is called to get the phone number input from the user.
    public static String phoneNumberInput(Scanner scanner, String prompt) {
        String input = ""; // Initialize input to an empty string.

        // Loop until valid input is received
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Read and trim input

            // Validate phone number format: (XXX) XXX-XXXX
            if (input.matches("\\(\\d{3}\\) \\d{3}-\\d{4}")) {
                return input;
                } else {
                    System.out.println("Invalid input. Phone number must be 10 digits and in the format (555) 555-5555.");
                }
            }
        }

    // Method is called to get the email address input from the user.
    public static String emailInput(Scanner scanner, String prompt) {
        String input = ""; // Initialize input to an empty string.

        // Loop until valid input is received
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Read and trim input

            // Validate email format: example@gmail.com
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                // Checks if the email is unique.
                String finalEmail = input;
                if (StudentManagement.students.stream().noneMatch(student -> Objects.equals(student.getEmail(), finalEmail))) {

                    return finalEmail; // Valid input, return it
                } else {
                    System.out.println("Invalid input. Email must be in the format example@gmail.com.");
                }
            }
        }
    }

    // Method is called to get the GPA input from the user.
    public static double gpaInput(Scanner scanner) {
        double gpa; // Local variable to hold the GPA value.
        // Loops until a valid amount is entered.
        while (true) {
            System.out.print("GPA: ");
            if (scanner.hasNextDouble()) {
                gpa = scanner.nextDouble();
                scanner.nextLine(); // Clears the line
                if (gpa >= 0 && gpa <= 1.9) {
                    return gpa; // Return the valid amount
                } else {
                    System.out.print("Please try again. GPA must be between 0 and 1.9.\n");
                }
            } else {
                System.out.print("Invalid Input. Please enter a valid number: ");
                scanner.nextLine(); // Clears invalid input
            }
        }
    }

    // Method is called to get a valid student ID to delete.
    public static int studentIdSearch(Scanner scanner) {
        while (true) {
            System.out.print("Enter student ID: ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Checks ID to be the specified range.
            try {
                int id = Integer.parseInt(input);
                if (id >= 10000000 && id <= 99999999) {
                    return id; // Returns the valid ID.
                } else {
                    System.out.println("Invalid ID. It must be exactly 8 digits. Exclude the 'S' ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid 8-digit number. Exclude the 'S' " );
            }
        }
    }

    // Helper method is called to find a student by ID.
    public static Student findStudentById(int id) {
        return StudentManagement.students.stream().filter(student -> student.getId() == id).findFirst().orElse(null); // Return the student if found.
    }

    // Method is called to get confirmation from the user.
    public static boolean userConfirmation(Scanner scanner, String message) {
        // Loop until we get a valid response from the user
        while (true) {
            // Prompt the user with the provided message.
            System.out.print(message);

            // Read the user's input and normalizes it.
            String choice = scanner.nextLine().trim().toLowerCase();

            // Check if the user confirmed.
            if (choice.equals("y") || choice.equals("yes")) {
                return true; // User confirmed, return true.
            }
            // Check if the user declined.
            else if (choice.equals("n") || choice.equals("no")) {
                return false; // User declined, return false.
            }
            // Checks for invalid input.
            else {
                // Inform the user about the invalid input.
                System.out.println("\nThat's not a valid response. Please enter y for yes or n for no.");
            }
        }
    }
}

