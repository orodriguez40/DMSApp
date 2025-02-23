// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// Authenticator Class:
// This class handles the authentication process to be able to use the DMS.


import java.util.Scanner;

public class Authenticator {

    // Method to authenticate user credentials
    public boolean authenticate(Scanner scanner) {
        // Default credentials
        String validUsername = "root";
        String validPassword = "password";

        // Ask user for username and password
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Check if the entered credentials match the valid ones.
        if (username.equals(validUsername) && password.equals(validPassword)) {
            System.out.println("\nLogin successful! Welcome to the DMS.");
            return true; // Authentication successful.
        } else {
            return false; // Authentication failed.
        }
    }
}
