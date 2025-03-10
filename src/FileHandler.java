// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// FileHandler Class:
// This class handles file operations and conditions for adding students.

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandler {

    public static boolean addStudentsByFile(String filePath, List<Student> students) {
        // Lists to store invalid entries per category
        List<String> invalidEntries = new ArrayList<>();
        List<String> invalidIDs = new ArrayList<>();
        List<String> invalidFirstNames = new ArrayList<>();
        List<String> invalidLastNames = new ArrayList<>();
        List<String> invalidPhoneNumbers = new ArrayList<>();
        List<String> invalidEmails = new ArrayList<>();
        List<String> invalidGPA = new ArrayList<>();
        List<String> invalidContacted = new ArrayList<>();

        // Maps to count occurrences in the file (for duplicate detection within file)
        Map<Integer, Integer> idCountMap = new HashMap<>();
        Map<String, Integer> emailCountMap = new HashMap<>();

        // List for all valid students from the file (before duplicate removal)
        List<Student> fileValidStudents = new ArrayList<>();

        try (BufferedReader readFile = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = readFile.readLine()) != null) {
                String[] details = row.split(" ");
                if (details.length != 7) {
                    invalidEntries.add(row);
                    continue;
                }

                try {
                    int id = Integer.parseInt(details[0].trim());
                    if (id < 10000000 || id > 99999999) {
                        invalidIDs.add(row);
                        continue;
                    }
                    // Count the ID occurrence in the file
                    idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);

                    String firstName = details[1].trim();
                    if (firstName.isEmpty() || firstName.length() > 15) {
                        invalidFirstNames.add(row);
                        continue;
                    }

                    String lastName = details[2].trim();
                    if (lastName.isEmpty() || lastName.length() > 25) {
                        invalidLastNames.add(row);
                        continue;
                    }

                    String phoneNumber = details[3].trim();
                    if (!phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
                        invalidPhoneNumbers.add(row);
                        continue;
                    }

                    String email = details[4].trim();
                    if (!email.matches("^[A-Za-z][A-Za-z0-9._-]*(?<![._-])@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")) {
                        invalidEmails.add(row);
                        continue;
                    }
                    // Count the email occurrence in the file
                    emailCountMap.put(email, emailCountMap.getOrDefault(email, 0) + 1);

                    double gpa;
                    try {
                        gpa = Double.parseDouble(details[5].trim());
                    } catch (NumberFormatException e) {
                        invalidGPA.add(row);
                        continue;
                    }
                    if (gpa < 0 || gpa > 1.9) {
                        invalidGPA.add(row);
                        continue;
                    }

                    String contactedValue = details[6].trim();
                    if (!contactedValue.equalsIgnoreCase("true") && !contactedValue.equalsIgnoreCase("false")) {
                        invalidContacted.add(row);
                        continue;
                    }
                    boolean isContacted = Boolean.parseBoolean(contactedValue);

                    // Student passes field validation; add to valid list
                    Student student = new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted);
                    fileValidStudents.add(student);
                } catch (NumberFormatException e) {
                    invalidEntries.add(row);
                }
            }
        } catch (IOException e) {
            showAlert("File Error", "Error reading the file. Please check the file path and try again.");
            return false;
        } catch (Exception e) {
            showAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            return false;
        }

        // Always display invalid entries (if any) using a scrollable window with a Continue button.
        showAlertsForInvalidEntries(invalidEntries, invalidIDs, invalidFirstNames, invalidLastNames,
                invalidPhoneNumbers, invalidEmails, invalidGPA, invalidContacted);

        // Duplicate check within the file
        Set<Integer> duplicateIDsInFile = idCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Set<String> duplicateEmailsInFile = emailCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // Duplicate check against already added students (global list)
        Set<Integer> existingIds = students.stream()
                .map(Student::getId)
                .collect(Collectors.toSet());
        Set<String> existingEmails = students.stream()
                .map(Student::getEmail)
                .collect(Collectors.toSet());

        // Separate duplicates by ID and by Email from the file valid students
        List<Student> duplicateStudentsByID = fileValidStudents.stream()
                .filter(student -> duplicateIDsInFile.contains(student.getId()) || existingIds.contains(student.getId()))
                .collect(Collectors.toList());

        List<Student> duplicateStudentsByEmail = fileValidStudents.stream()
                .filter(student -> duplicateEmailsInFile.contains(student.getEmail()) || existingEmails.contains(student.getEmail()))
                .collect(Collectors.toList());

        // Display duplicate entries regardless of confirmation.
        if (!duplicateStudentsByID.isEmpty()) {
            showDuplicateIDs(duplicateStudentsByID);
        }
        if (!duplicateStudentsByEmail.isEmpty()) {
            showDuplicateEmails(duplicateStudentsByEmail);
        }

        // Remove duplicates from the list of valid students.
        // (If a student appears as a duplicate by ID or Email, exclude them from addition.)
        List<Student> uniqueValidStudents = fileValidStudents.stream()
                .filter(student -> !(duplicateIDsInFile.contains(student.getId())
                        || duplicateEmailsInFile.contains(student.getEmail())
                        || existingIds.contains(student.getId())
                        || existingEmails.contains(student.getEmail())))
                .collect(Collectors.toList());

        // Confirmation logic for unique valid students using the new valid students window
        if (!uniqueValidStudents.isEmpty()) {
            StringBuilder message = new StringBuilder("Valid students to be added:\n");
            uniqueValidStudents.forEach(student -> message.append(student.toString()).append("\n"));
            // This flag will be set by the confirmation dialog
            boolean[] confirmed = new boolean[1];
            showValidStudentsWindowWithConfirmation(message.toString(), confirmed);
            if (confirmed[0]) {
                students.addAll(uniqueValidStudents);
                showAlert("Success", "Students Added Successfully!");
                return true; // Students were added
            } else {
                showAlert("Cancellation", "Student upload has been cancelled. Students were not added.");
                return false; // Operation cancelled by the user
            }
        } else {
            showAlert("No Valid Students", "No valid students found to add.");
        }

        System.out.println("\nFile processing complete.");
        return false;
    }

    /**
     * Displays invalid (or incorrectly formatted) entries in a scrollable window.
     * A "Continue" button is added so that the user can click it (or exit) to continue.
     */
    private static void showAlertsForInvalidEntries(List<String> invalidEntries, List<String> invalidIDs,
                                                    List<String> invalidFirstNames, List<String> invalidLastNames,
                                                    List<String> invalidPhoneNumbers, List<String> invalidEmails,
                                                    List<String> invalidGPA, List<String> invalidContacted) {

        if (!invalidIDs.isEmpty() || !invalidFirstNames.isEmpty() || !invalidLastNames.isEmpty() || !invalidPhoneNumbers.isEmpty() ||
                !invalidEmails.isEmpty() || !invalidGPA.isEmpty() || !invalidContacted.isEmpty() || !invalidEntries.isEmpty()) {

            StringBuilder message = new StringBuilder("Invalid entries detected and skipped:\n");

            if (!invalidIDs.isEmpty()) {
                message.append("\nInvalid IDs (must be exactly 8 digits):\n")
                        .append(String.join("\n", invalidIDs));
            }
            if (!invalidFirstNames.isEmpty()) {
                message.append("\nInvalid first names:\n")
                        .append(String.join("\n", invalidFirstNames));
            }
            if (!invalidLastNames.isEmpty()) {
                message.append("\nInvalid last names:\n")
                        .append(String.join("\n", invalidLastNames));
            }
            if (!invalidPhoneNumbers.isEmpty()) {
                message.append("\nInvalid phone numbers (must follow xxx-xxx-xxxx format):\n")
                        .append(String.join("\n", invalidPhoneNumbers));
            }
            if (!invalidEmails.isEmpty()) {
                message.append("\nInvalid emails (must be unique and follow example@example.com format):\n")
                        .append(String.join("\n", invalidEmails));
            }
            if (!invalidGPA.isEmpty()) {
                message.append("\nInvalid GPAs (must be between 0 and 1.9):\n")
                        .append(String.join("\n", invalidGPA));
            }
            if (!invalidContacted.isEmpty()) {
                message.append("\nInvalid contacted status:\n")
                        .append(String.join("\n", invalidContacted));
            }
            if (!invalidEntries.isEmpty()) {
                message.append("\nIncorrect formatting:\n")
                        .append(String.join("\n", invalidEntries));
            }

            showScrollableWindow("Invalid Entries", message.toString());
        }
    }

    // Displays duplicate entries detected by ID in a scrollable window with a "Continue" button.
    private static void showDuplicateIDs(List<Student> duplicateStudents) {
        StringBuilder message = new StringBuilder("Duplicate entries detected by ID:\n");
        duplicateStudents.forEach(student -> message.append(student.toString()).append("\n"));
        showScrollableWindow("Duplicate IDs", message.toString());
    }

    // Displays duplicate entries detected by Email in a scrollable window with a "Continue" button.
    private static void showDuplicateEmails(List<Student> duplicateStudents) {
        StringBuilder message = new StringBuilder("Duplicate entries detected by Email:\n");
        duplicateStudents.forEach(student -> message.append(student.toString()).append("\n"));
        showScrollableWindow("Duplicate Emails", message.toString());
    }


    private static void showValidStudentsWindowWithConfirmation(String content, boolean[] confirmedHolder) {
        Stage validStage = new Stage();
        validStage.setTitle("Valid Students");
        validStage.initModality(Modality.APPLICATION_MODAL);

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            // Open a non-modal confirmation dialog.
            Stage confirmStage = new Stage();
            confirmStage.setTitle("Confirm Upload");
            // Set modality to NONE so it does not block the valid students window.
            confirmStage.initModality(Modality.NONE);
            Label confirmLabel = new Label("Are you sure you want to add these students?");

            Button yesButton = new Button("Yes");
            yesButton.setOnAction(ev -> {
                confirmedHolder[0] = true;
                confirmStage.close();
                validStage.close();
            });
            Button noButton = new Button("No");
            noButton.setOnAction(ev -> {
                confirmedHolder[0] = false;
                confirmStage.close();
                validStage.close();
            });

            VBox confirmLayout = new VBox(10, confirmLabel, yesButton, noButton);
            confirmLayout.setAlignment(Pos.CENTER);
            Scene confirmScene = new Scene(confirmLayout, 300, 150);
            confirmStage.setScene(confirmScene);
            confirmStage.show();
        });

        VBox layout = new VBox(10, textArea, continueButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        validStage.setScene(scene);
        validStage.showAndWait();
    }

    private static void showScrollableWindow(String title, String content) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(textArea, continueButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }



    // Utility method to show a simple alert with a title and message.
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
