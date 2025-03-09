// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// StudentManagement Class:
// This class manages all CRUD functions related to Students.

// Imported Library

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class StudentManagement {

    // Attribute to store all Students in an ArrayList.
    static final List<Student> students = new ArrayList<>();

    public static void addStudentManual() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Student Manually");

        Label idLabel = new Label("ID:");
        TextField idField = new TextField();
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label gpaLabel = new Label("GPA:");
        TextField gpaField = new TextField();
        Label contactedLabel = new Label("Contacted Status:");
        TextField contactedField = new TextField();

        Button addButton = new Button("Add");
        Button clearButton = new Button("Clear");

        addButton.setOnAction(e -> {
            try {
                if (idField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
                        || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                        || gpaField.getText().isEmpty() || contactedField.getText().isEmpty()) {
                    showAlert("Input Error", "All fields must be filled out before adding a student.");
                    return;
                }

                if (!idField.getText().matches("\\d{8}")) {
                    throw new IllegalArgumentException("Invalid ID: Must be exactly 8 digits and start with a 1.");
                }

                int id = Integer.parseInt(idField.getText());

                if (id < 10000000 || id > 99999999) {
                    throw new IllegalArgumentException("Invalid ID: Must be between 10000000 and 99999999.");
                }

                // Check if the ID is already in the system
                if (!UserInput.manualIDInput(id)) {
                    throw new IllegalArgumentException("ID already exists in the system.");
                }

                if (firstNameField.getText().length() > 15) {
                    throw new IllegalArgumentException("Invalid First Name: Must be less than 15 characters.");
                }

                String firstName = firstNameField.getText();
                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();

                if(firstName.matches(".*\\d.*") || !firstName.matches("[a-zA-Z]+")){
                    throw new IllegalArgumentException("Invalid First Name: Must contain only letters.");
                }

                if (lastNameField.getText().length() > 25) {
                    throw new IllegalArgumentException("Invalid Last Name: Must be less than 25 characters.");
                }

                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();

                if(firstName.matches(".*\\d.*") || !firstName.matches("[a-zA-Z]+")){
                    throw new IllegalArgumentException("Invalid First Name: Must contain only letters.");
                }

                String lastName = lastNameField.getText();
                lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

                if(lastName.matches(".*\\d.*") || !lastName.matches("[a-zA-Z]+")){
                    throw new IllegalArgumentException("Invalid Last Name: Must contain only letters.");
                }

                if (!phoneField.getText().matches("\\d{3}-\\d{3}-\\d{4}")) {
                    throw new IllegalArgumentException("Invalid Phone Number: Valid format (555-555-5555).");
                }

                String phoneNumber = phoneField.getText();

                if (!emailField.getText().matches("^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    throw new IllegalArgumentException("Invalid Email: Valid format (example@example.com).");
                }

                String email = emailField.getText();
                email = email.toLowerCase();

                if (!UserInput.emailInput(email)) {
                    throw new IllegalArgumentException("Invalid Email: Email must be unique.");
                }

                if (!gpaField.getText().matches("-?\\d+(\\.\\d+)?")) {
                    throw new IllegalArgumentException("Invalid input: Please enter a double number.");
                }

                double gpa = Double.parseDouble(gpaField.getText());

                if (gpa < 0 || gpa > 1.9) {
                    throw new IllegalArgumentException("Invalid GPA: Must be between 0 and 1.9.");
                }

                boolean isContacted = Boolean.parseBoolean(contactedField.getText());

                if (!contactedField.getText().equalsIgnoreCase("true") && !contactedField.getText().equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid input: Please enter 'true' for yes or 'false' for no.");
                }

                // Ask for confirmation before adding student
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Add Student");
                confirmationAlert.setContentText("Are you sure you want to add this student?");

                // Show the alert and wait for user response
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // If user clicked 'Yes' (OK), add the student
                    Student newStudent = UserInput.getStudentInfo(id, firstName, lastName, phoneNumber, email, gpa, isContacted);

                    if (newStudent != null) {
                        students.add(newStudent);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Student Added!");
                        alert.setHeaderText(null);
                        alert.setContentText(newStudent.toString());
                        alert.showAndWait();

                        idField.clear();
                        firstNameField.clear();
                        lastNameField.clear();
                        phoneField.clear();
                        emailField.clear();
                        gpaField.clear();
                        contactedField.clear();
                    } else {
                        showAlert("Error", "Failed to add student. Please try again.");
                    }
                }
            } catch (IllegalArgumentException ex) {
                showAlert("Invalid Input", ex.getMessage());
            }
        });

        clearButton.setOnAction(e -> {
            idField.clear();
            firstNameField.clear();
            lastNameField.clear();
            phoneField.clear();
            emailField.clear();
            gpaField.clear();
            contactedField.clear();
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(phoneLabel, 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(emailLabel, 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(gpaLabel, 0, 5);
        grid.add(gpaField, 1, 5);
        grid.add(contactedLabel, 0, 6);
        grid.add(contactedField, 1, 6);
        grid.add(addButton, 0, 7);
        grid.add(clearButton, 1, 7);

        Scene scene = new Scene(grid, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }



    // Method is called to add students by file upload.
    public static void addStudentFile() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Student by File");

        // Create labels and text fields for file path
        Label filePathLabel = new Label("File Path:");
        TextField filePathField = new TextField();
        filePathField.setPromptText("Select a text file");

        // File chooser button
        Button chooseFileButton = new Button("Choose File");
        StringBuilder fileContent = new StringBuilder();

        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Student File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(popupStage);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
                fileContent.setLength(0); // Clear previous content

                // Read the file content
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }
                } catch (IOException ex) {
                    showAlert("File Error", "Error reading the file. Please check the file format.");
                }
            }
        });

        // Buttons for upload and clear
        Button uploadButton = new Button("Upload");
        Button clearButton = new Button("Clear");

        // Action for the Upload button
        uploadButton.setOnAction(e -> {
            String filePath = filePathField.getText().trim();

            if (filePath.isEmpty()) {
                showAlert("Error", "Please select a file before uploading.");
            } else {
                // Check for valid students and display them
                FileHandler.addStudentsByFile(filePath, students);
                filePathField.clear();

            }
        });

        clearButton.setOnAction(e -> filePathField.clear());

        // Layout for the popup
        popupStage.setTitle("Upload File");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Add all components in a visually pleasing order
        layout.getChildren().addAll(filePathLabel, filePathField, chooseFileButton, uploadButton, clearButton);
        Scene scene = new Scene(layout, 350, 250);
        popupStage.setScene(scene);
        popupStage.showAndWait();

    }

    public static void removeStudent() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Remove Student");

        // Create label and text field for student ID input
        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        // Buttons for search, delete, and clear
        Button searchButton = new Button("Search");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear");

        //If student is found, it is pass as an Atomic Reference for deletion.
        AtomicReference<Student> foundStudentRef = new AtomicReference<>();

        // Action for the Search button
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {

                Student findStudent = UserInput.searchStudentByID(studentId);
                foundStudentRef.set(findStudent); // Store it in AtomicReference

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (findStudent != null) {
                    alert.setTitle("Student found");
                    alert.setHeaderText(null);
                    alert.setContentText(findStudent.toString());
                    alert.showAndWait();
                }

            } else {
                showAlert("Error", "Please enter a student ID to search.");
            }
        });

        // Action for the Delete button
        deleteButton.setOnAction(e -> {
            if (foundStudentRef.get() != null) {
                // Confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Deletion");
                confirmationAlert.setHeaderText("Are you sure you want to delete this student?");
                confirmationAlert.setContentText(foundStudentRef.get().toString());

                // Add Yes and No options
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                // Show the confirmation window
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == yesButton) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Deletion Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Student successfully deleted:\n" + foundStudentRef.get().toString());
                    successAlert.showAndWait();
                    students.remove(foundStudentRef.get());
                    idField.clear();
                } else {
                    showAlert("Cancellation", "Deletion canceled.\n");
                }
            } else {
                showAlert("Invalid Input","Please enter a valid student ID to delete.\n");
            }
        });

        // Action for the Clear button
        clearButton.setOnAction(e -> idField.clear());

        // Layout for the popup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(searchButton, 0, 1);
        grid.add(deleteButton, 1, 1);
        grid.add(clearButton, 2, 1);

        // Scene for the popup
        Scene scene = new Scene(grid, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }


    public static void updateStudent() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Update Student");

        // Create label and text field for student ID input
        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        // Buttons for search, update, and clear
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");

        //If student is found, it is pass as an Atomic Reference for deletion.
        AtomicReference<Student> foundStudentRef = new AtomicReference<>();

        // Action for the Search button
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {

                Student findStudent = UserInput.searchStudentByID(studentId);
                foundStudentRef.set(findStudent); // Store it in AtomicReference

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (findStudent != null) {
                    alert.setTitle("Student found");
                    alert.setHeaderText(null);
                    alert.setContentText(findStudent.toString());
                    alert.showAndWait();
                }

            } else {
                showAlert("Error", "Please enter a student ID to search.");
            }
        });

        // Action for the Update button
        updateButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                // outputArea.appendText("Updating student with ID: " + studentId + "\n");
                popupStage.close();
            } else {
                // outputArea.appendText("Please enter a student ID to update.\n");
            }
        });

        // Action for the Clear button
        clearButton.setOnAction(e -> idField.clear());

        // Layout for the popup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(searchButton, 0, 1);
        grid.add(updateButton, 1, 1);
        grid.add(clearButton, 2, 1);

        // Scene for the popup
        Scene scene = new Scene(grid, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }


    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }
