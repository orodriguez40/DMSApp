// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/30/2025

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * UserInput Class:
 * <p>
 * This class handles all user inputs and confirmations.
 * It validates student information, ensures data uniqueness,
 * and provides methods to search for students in the database.
 * </p>
 */
public class UserInput {

    /**
     * Validates manual user input and returns a Student object if all validations pass.
     *
     * @param id          the student's ID.
     * @param firstName   the student's first name.
     * @param lastName    the student's last name.
     * @param phoneNumber the student's phone number.
     * @param email       the student's email address.
     * @param gpa         the student's GPA.
     * @param isContacted indicates whether the student has been contacted.
     * @return a Student object with the provided information, or null if validation fails.
     */
    public static Student getStudentInfo(int id, String firstName, String lastName, String phoneNumber, String email, double gpa, boolean isContacted) {
        try {
            if (!(manualIDInput(id))) {
                System.out.println("Invalid ID");
                return null;
            }
            if (!(firstNameInput(firstName))) {
                System.out.println("Invalid First");
                return null;
            }
            if (!(lastNameInput(lastName))) {
                System.out.println("Invalid Last");
                return null;
            }
            if (!(phoneNumberInput(phoneNumber))) {
                System.out.println("Invalid phone");
                return null;
            }
            if (!(emailInput(email))) {
                System.out.println("Invalid email");
                return null;
            }
            if (!(gpaInput(gpa))) {
                System.out.println("Invalid gpa");
                return null;
            }
            if (isContactedConfirmation(isContacted) == null) {
                System.out.println("Invalid contacted");
                return null;
            }

            return new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    /**
     * Displays exceptions encountered during input validation.
     *
     * @param e the exception to handle.
     */
    private static void handleException(Exception e) {
        if (e instanceof InputMismatchException) {
            System.out.println("Invalid input. Please enter the correct data type.");
        } else if (e instanceof NoSuchElementException || e instanceof IllegalStateException) {
            System.out.println("Input interrupted. Please try again.");
        } else {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Validates the ID format and uniqueness.
     *
     * @param id the student's ID.
     * @return true if the ID is valid and unique, false otherwise.
     */
    public static boolean manualIDInput(int id) {
        if (id < 10000000 || id > 99999999) {
            return false;
        }
        // Checks if ID is unique in the database.
        String query = "SELECT COUNT(*) FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return false;
    }

    /**
     * Validates the first name (at most 15 characters and only letters).
     *
     * @param firstName the student's first name.
     * @return true if valid, false otherwise.
     */
    public static boolean firstNameInput(String firstName) {
        return handleValidation(() -> firstName != null && !firstName.isEmpty() && firstName.length() <= 15 && !firstName.matches(".*\\d.*") && firstName.matches("[a-zA-Z]+"));
    }

    /**
     * Validates the last name (at most 25 characters and only letters).
     *
     * @param lastName the student's last name.
     * @return true if valid, false otherwise.
     */
    public static boolean lastNameInput(String lastName) {
        return handleValidation(() -> lastName != null && !lastName.isEmpty() && lastName.length() <= 25 && !lastName.matches(".*\\d.*") && lastName.matches("[a-zA-Z]+"));
    }

    /**
     * Validates the phone number (must follow xxx-xxx-xxxx format).
     *
     * @param phoneNumber the student's phone number.
     * @return true if valid, false otherwise.
     */
    public static boolean phoneNumberInput(String phoneNumber) {
        return handleValidation(() -> phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}"));
    }

    /**
     * Validates the email format and uniqueness.
     *
     * @param email the student's email address.
     * @return true if valid and unique, false otherwise.
     */
    public static boolean emailInput(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (!email.matches("^[A-Za-z][A-Za-z0-9._-]*(?<![._-])@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")) {
            return false;
        }
        // Checks if email is unique within the database.
        String query = "SELECT COUNT(*) FROM students WHERE email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return false;
    }

    /**
     * Validates the GPA (must be between 0 and 1.9).
     *
     * @param gpa the student's GPA.
     * @return true if valid, false otherwise.
     */
    public static boolean gpaInput(double gpa) {
        return handleValidation(() -> gpa >= 0 && gpa <= 1.9);
    }

    /**
     * Checks for a valid contacted status.
     *
     * @param isContacted the contacted status.
     * @return the contacted status if valid, or null otherwise.
     */
    public static Boolean isContactedConfirmation(Boolean isContacted) {
        return handleValidation(() -> isContacted != null) ? isContacted : null;
    }

    /**
     * Helper method for input validations using a lambda.
     *
     * @param validationFunction the validation function to execute.
     * @return true if validation passes, false otherwise.
     */
    private static boolean handleValidation(ValidationFunction validationFunction) {
        try {
            return validationFunction.validate();
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate();
    }

    /**
     * Searches for a student based on their ID.
     * <p>
     * Displays the information and returns the corresponding Student object if found.
     * </p>
     *
     * @param input the student's ID as a string.
     * @return the Student object if found, or null if not found.
     */
    public static Student searchStudentByID(String input) {
        try {
            int id = Integer.parseInt(input);
            if (id < 10000000 || id > 99999999) {
                showAlert("Invalid ID", "ID must be exactly 8 digits.");
                return null;
            }
            String query = "SELECT * FROM students WHERE id = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("phoneNumber"),
                            rs.getString("email"),
                            rs.getDouble("gpa"),
                            rs.getBoolean("isContacted")
                    );
                } else {
                    showAlert("Error", "No student found with the given ID.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid 8-digit number (exclude the 'S' in the ID).");
        } catch (Exception e) {
            showAlert("Unexpected Error", "Please try again. " + e.getMessage());
        }
        return null;
    }

    /**
     * Utility method to show a simple alert with a title and message.
     *
     * @param title   the title of the alert.
     * @param message the message to display.
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
