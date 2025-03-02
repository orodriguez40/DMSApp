// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// Authenticator Class:
// This class handles the authentication process to be able to use the DMS.

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Authenticator {

    // Method to authenticate user credentials
    public boolean authenticate(Scanner scanner) {
        // Default credentials.
        String validUsername = "root";
        String validPassword = "password";

        // Ask user for username and password.
        String username = "";
        String password = "";
        int attempts = 0; // Counter for attempts

        while (attempts < 3) {
            try {
                System.out.print("Enter username: ");
                username = scanner.nextLine().trim();
                if (username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty.");

                System.out.print("Enter password: ");
                password = scanner.nextLine().trim();
                if (password.isEmpty()) throw new IllegalArgumentException("Password cannot be empty.");

                // Check if the entered credentials match the valid ones.
                if (username.equals(validUsername) && password.equals(validPassword)) {
                    System.out.println("\nLogin successful! Welcome to the DMS.");
                    return true; // Authentication successful.
                } else {
                    System.out.println("Invalid username or password.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue detected. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner in case of input issues.
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner = new Scanner(System.in); // Reinitialize scanner for retry.
            }
            attempts++; // Increment attempts
        }

        // Notify the user of maximum attempts failure
        System.out.println("\nMaximum attempts reached.");
        exitProgram(); // Call to exit the program.
        return false; // Authentication failed.
    }

    // Method to exit the program
    public void exitProgram() {
        System.out.println("Terminating DMS application.\n");
        System.exit(0); // Terminates the program.
    }
}
