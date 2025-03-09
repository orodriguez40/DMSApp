// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// UserInput Class:
// This class handles all user inputs and confirmations.

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UserInput {

    public static Student getStudentInfo(int id, String firstName, String lastName, String phoneNumber, String email, double gpa, boolean isContacted) {
        try {
            if (!(manualIDInput(id))) return null;
            if (!(firstNameInput(firstName))) return null;
            if (!(lastNameInput(lastName))) return null;
            if (!(phoneNumberInput(phoneNumber))) return null;
            if ((emailInput(email))) return null;
            if (!(gpaInput(gpa))) return null;
            if (isContactedConfirmation(isContacted) == null) return null;

            return new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    private static void handleException(Exception e) {
        if (e instanceof InputMismatchException) {
            System.out.println("Invalid input. Please enter the correct data type.");
        } else if (e instanceof NoSuchElementException || e instanceof IllegalStateException) {
            System.out.println("Input interrupted. Please try again.");
        } else {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static boolean manualIDInput(int id) {
        return handleValidation(() ->
                id >= 10000000 && id <= 99999999 &&
                        StudentManagement.students.stream().noneMatch(student -> student.getId() == id)
        );

    }
    public static boolean firstNameInput(String firstName) {
        return handleValidation(() -> firstName != null && !firstName.isEmpty() && firstName.length() <= 15);
    }

    public static boolean lastNameInput(String lastName) {
        return handleValidation(() -> lastName != null && !lastName.isEmpty() && lastName.length() <= 25);
    }

    public static boolean phoneNumberInput(String phoneNumber) {
        return handleValidation(() -> phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}"));
    }

    public static boolean emailInput(String email) {
        return handleValidation(() -> email.matches("^[A-Za-z][A-Za-z0-9._-]*(?<![._-])@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$") &&
                StudentManagement.students.stream().noneMatch(student -> Objects.equals(student.getEmail(), email)));
    }

    public static boolean gpaInput(double gpa) {
        return handleValidation(() -> gpa >= 0 && gpa <= 1.9);
    }

    public static Boolean isContactedConfirmation(Boolean isContacted) {
        return handleValidation(() -> isContacted != null) ? isContacted : null;
    }

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
}
