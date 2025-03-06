// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// Authenticator Class:
// This class handles the authentication process to be able to use the DMS.

public class Authenticator {
    private static final String validUsername = "root";
    private static final String validPassword = "password";

    // Method to authenticate user credentials
    public static boolean authenticate(String username, String password) {
        return username.equals(validUsername) && password.equals(validPassword);
    }
}

