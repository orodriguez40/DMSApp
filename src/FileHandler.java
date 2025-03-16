// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/30/2025

// FileHandler Class:
// This class handles file operations and conditions for adding students.
// In this Phase 4 update, it reads a text file and inserts valid student records directly into the database.
// It now also checks for duplicate IDs and emails within the file and against the database.

import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandler {

    // Method is called to validate file contents and upload student information from a text file.
    public static boolean addStudentsByFile(String filePath) {
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
        showInvalidEntries(invalidEntries, invalidIDs, invalidFirstNames, invalidLastNames,
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

        // Retrieve existing student IDs and emails from the database
        Set<Integer> existingIds = new HashSet<>();
        Set<String> existingEmails = new HashSet<>();
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, email FROM students")) {
            while (rs.next()) {
                existingIds.add(rs.getInt("id"));
                existingEmails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error retrieving existing student records: " + e.getMessage());
            return false;
        }

        // Display duplicates found in the database
        List<Student> duplicateStudentsByDBId = fileValidStudents.stream()
                .filter(student -> existingIds.contains(student.getId()))
                .collect(Collectors.toList());
        if (!duplicateStudentsByDBId.isEmpty()) {
            showDuplicateIDs(duplicateStudentsByDBId);
        }
        List<Student> duplicateStudentsByDBEmail = fileValidStudents.stream()
                .filter(student -> existingEmails.contains(student.getEmail()))
                .collect(Collectors.toList());
        if (!duplicateStudentsByDBEmail.isEmpty()) {
            showDuplicateEmails(duplicateStudentsByDBEmail);
        }

        // Remove duplicates from the list of valid students (both within file and with database)
        List<Student> uniqueValidStudents = fileValidStudents.stream()
                .filter(student -> !(duplicateIDsInFile.contains(student.getId())
                        || duplicateEmailsInFile.contains(student.getEmail())
                        || existingIds.contains(student.getId())
                        || existingEmails.contains(student.getEmail())))
                .collect(Collectors.toList());

        // Confirmation logic for unique valid students
        if (!uniqueValidStudents.isEmpty()) {
            StringBuilder message = new StringBuilder("Valid students to be added:\n");
            uniqueValidStudents.forEach(student -> message.append(student.toString()).append("\n"));
            boolean[] confirmed = new boolean[1];
            showValidStudents(message.toString(), confirmed);
            if (confirmed[0]) {
                // Insert each student into the database
                try (Connection conn = DatabaseConnector.getConnection()) {
                    String insertSQL = "INSERT INTO students (id, firstName, lastName, phoneNumber, email, gpa, isContacted) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    for (Student s : uniqueValidStudents) {
                        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                            stmt.setInt(1, s.getId());
                            stmt.setString(2, s.getFirstName());
                            stmt.setString(3, s.getLastName());
                            stmt.setString(4, s.getPhoneNumber());
                            stmt.setString(5, s.getEmail());
                            stmt.setDouble(6, s.getGpa());
                            stmt.setBoolean(7, s.getIsContacted());
                            stmt.executeUpdate();
                        }
                    }
                    showAlert("Success", "Students Added Successfully!");
                    return true;
                } catch (SQLException e) {
                    showAlert("Database Error", "Failed to insert students: " + e.getMessage());
                    return false;
                }
            } else {
                showAlert("Cancellation", "Student upload has been cancelled. Students were not added.");
                return false;
            }
        } else {
            showAlert("No Valid Students", "No valid students found to add.");
        }

        System.out.println("\nFile processing complete.");
        return false;
    }

    // Displays invalid (or incorrectly formatted) entries in a scrollable window.
    private static void showInvalidEntries(List<String> invalidEntries, List<String> invalidIDs,
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

            scrollableWindow("Invalid Entries", message.toString());
        }
    }

    // Displays duplicate entries detected by ID.
    private static void showDuplicateIDs(List<Student> duplicateStudents) {
        StringBuilder message = new StringBuilder("Duplicate entries detected by ID:\n");
        duplicateStudents.forEach(student -> message.append(student.toString()).append("\n"));
        scrollableWindow("Duplicate IDs", message.toString());
    }

    // Displays duplicate entries detected by Email.
    private static void showDuplicateEmails(List<Student> duplicateStudents) {
        StringBuilder message = new StringBuilder("Duplicate entries detected by Email:\n");
        duplicateStudents.forEach(student -> message.append(student.toString()).append("\n"));
        scrollableWindow("Duplicate Emails", message.toString());
    }

    private static void showValidStudents(String content, boolean[] confirmedHolder) {
        Stage validStage = new Stage();
        validStage.setTitle("Valid Students");
        validStage.initModality(Modality.APPLICATION_MODAL);

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            // Open a confirmation dialog.
            Stage confirmStage = new Stage();
            confirmStage.setTitle("Confirm Upload");
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

    // Helper method to display a scrollable window.
    private static void scrollableWindow(String title, String content) {
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
