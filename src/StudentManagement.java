// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// StudentManagement Class:
// This class manages all CRUD functions and the custom action.

// Imported Libraries
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    // Method is called to add a student manually.
    public static void addStudentManual() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Student Manually");

        // Labels and Fields to help user enter data.
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

        // Buttons to perform actions.
        Button addButton = new Button("Add");
        Button clearButton = new Button("Clear");

        // When addButton is clicked, user validation and confirmation is performed.
        addButton.setOnAction(e -> {
            try {
                // All fields must contain value in order to add a student.
                if (idField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
                        || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                        || gpaField.getText().isEmpty() || contactedField.getText().isEmpty()) {
                    showAlert("Input Error", "All fields must be filled out before adding a student.");
                    return;
                }

                // Validates ID
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

                // Validates first name and automatically converts the letters for the first to be uppercase and the rest lowercase.
                if (firstNameField.getText().length() > 15) {
                    throw new IllegalArgumentException("Invalid First Name: Must be less than 15 characters.");
                }

                String firstName = firstNameField.getText();
                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();

                if (firstName.matches(".*\\d.*") || !firstName.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Invalid First Name: Must contain only letters.");
                }

                // Validates last name and automatically converts the letters for the first to be uppercase and the rest lowercase.
                if (lastNameField.getText().length() > 25) {
                    throw new IllegalArgumentException("Invalid Last Name: Must be less than 25 characters.");
                }

                String lastName = lastNameField.getText();
                lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

                if (lastName.matches(".*\\d.*") || !lastName.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Invalid Last Name: Must contain only letters.");
                }

                // Validates phone number.
                if (!phoneField.getText().matches("\\d{3}-\\d{3}-\\d{4}")) {
                    throw new IllegalArgumentException("Invalid Phone Number: Valid format (555-555-5555).");
                }

                String phoneNumber = phoneField.getText();

                // Validates email.
                if (!emailField.getText().matches("^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    throw new IllegalArgumentException("Invalid Email: Valid format (example@example.com).");
                }

                String email = emailField.getText();
                email = email.toLowerCase();

                // Checks if the email is already in the system
                if (!UserInput.emailInput(email)) {
                    throw new IllegalArgumentException("Email is already in use: Email must be unique.");
                }


                // Validates GPA
                if (!gpaField.getText().matches("-?\\d+(\\.\\d+)?")) {
                    throw new IllegalArgumentException("Invalid input: Please enter a double number (example 1.0).");
                }

                double gpa = Double.parseDouble(gpaField.getText());

                if (gpa < 0 || gpa > 1.9) {
                    throw new IllegalArgumentException("Invalid GPA: Must be between 0 and 1.9.");
                }

                // Validates contacted status.
                boolean isContacted = Boolean.parseBoolean(contactedField.getText());

                if (!contactedField.getText().equalsIgnoreCase("true") && !contactedField.getText().equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid input: Please enter 'true' for yes or 'false' for no.");
                }

                // Ask for confirmation before adding student.
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Add Student");
                confirmationAlert.setContentText("Are you sure you want to add this student?");

                // Show the alert and wait for user response.
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // If user clicked 'Yes' (OK), add the student.
                    Student newStudent = UserInput.getStudentInfo(id, firstName, lastName, phoneNumber, email, gpa, isContacted);

                    if (newStudent != null) {
                        // Student is added and displayed to the user.
                        students.add(newStudent);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Student Added!");
                        alert.setHeaderText(null);
                        alert.setContentText(newStudent.toString());
                        alert.showAndWait();

                        // Fields are cleared for new entry.
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

        // Clears fields
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

        // when button is clicked, file chooser window will appear for the user to select file.
        // The user can also type or copy/paste the file path.
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Student File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(popupStage);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());

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

        // Clears field
        clearButton.setOnAction(e -> filePathField.clear());

        // Layout for the popup
        popupStage.setTitle("Upload File");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Add all components displayed in order
        layout.getChildren().addAll(filePathLabel, filePathField, chooseFileButton, uploadButton, clearButton);
        Scene scene = new Scene(layout, 350, 250);
        popupStage.setScene(scene);
        popupStage.showAndWait();

    }

    // Method is called to delete student from DMS.
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
                showAlert("Invalid Input", "Please enter a valid ID to delete student.\n");
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


    // Method is called to update student information.
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

        // If student is found, it is passed as an Atomic Reference for modification
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
            if (foundStudentRef.get() != null) {
                // Create a new stage for the update form
                Stage updatePopupStage = new Stage();
                updatePopupStage.initModality(Modality.APPLICATION_MODAL);
                updatePopupStage.setTitle("Update Student Details");

                // Instruction label
                Label instructionLabel = new Label("Enter new values in the fields below:");
                instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-padding: 5px;");

                // Form labels and fields
                Label idLabelUpdate = new Label("ID:");
                TextField idFieldUpdate = new TextField(String.valueOf(foundStudentRef.get().getId()));
                Label firstNameLabelUpdate = new Label("First Name:");
                TextField firstNameFieldUpdate = new TextField(foundStudentRef.get().getFirstName());
                Label lastNameLabelUpdate = new Label("Last Name:");
                TextField lastNameFieldUpdate = new TextField(foundStudentRef.get().getLastName());
                Label phoneLabelUpdate = new Label("Phone Number:");
                TextField phoneFieldUpdate = new TextField(foundStudentRef.get().getPhoneNumber());
                Label emailLabelUpdate = new Label("Email:");
                TextField emailFieldUpdate = new TextField(foundStudentRef.get().getEmail());
                Label gpaLabelUpdate = new Label("GPA:");
                TextField gpaFieldUpdate = new TextField(String.valueOf(foundStudentRef.get().getGpa()));
                Label contactedLabelUpdate = new Label("Contacted Status:");
                TextField contactedFieldUpdate = new TextField(String.valueOf(foundStudentRef.get().getIsContacted()));

                // Buttons
                Button updateButtonConfirm = new Button("Confirm Update");
                Button clearButtonUpdate = new Button("Clear");

                // Holds the student for update
                Student updateStudent = foundStudentRef.get();

                updateButtonConfirm.setOnAction(update -> {

                    // Alerts to display to the user if student information was updated.
                    Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    Alert failedAlert = new Alert(Alert.AlertType.ERROR);

                    String idInput = idFieldUpdate.getText().trim();

                    if (!idInput.isEmpty()) {
                        try {
                            int id = Integer.parseInt(idInput);
                            boolean validateId = UserInput.manualIDInput(id);

                            if (validateId) {
                                updateStudent.setId(id);
                            } else {
                                failedAlert.setTitle("ID not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure it is a valid 8-digit ID and not the same ID or a duplicate.");
                                failedAlert.showAndWait();
                            }
                        } catch (NumberFormatException exception) {
                            // Ignore invalid input
                        }
                    }

                    String firstName = firstNameFieldUpdate.getText().trim();
                    if (!firstName.isEmpty()) {
                        try {
                            String modifiedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                            boolean validateFirstName = UserInput.firstNameInput(modifiedFirstName);

                            if (validateFirstName) {
                                updateStudent.setFirstName(modifiedFirstName);
                            } else {
                                failedAlert.setTitle("First name not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure it contains only letters and is less than 16 characters.");
                                failedAlert.showAndWait();
                            }
                        } catch (Exception exception) {
                            // Ignore invalid input
                        }
                    }

                    String lastName = lastNameFieldUpdate.getText().trim();
                    if (!lastName.isEmpty()) {
                        try {
                            String modifiedLastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                            boolean validateLastName = UserInput.firstNameInput(modifiedLastName);

                            if (validateLastName) {
                                updateStudent.setLastName(modifiedLastName);
                            } else {
                                failedAlert.setTitle("Last name not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure it contains only letters and is less than 26 characters.");
                                failedAlert.showAndWait();
                            }
                        } catch (Exception exception) {
                            // Ignore empty field
                        }
                    }


                        String phoneNumber = phoneFieldUpdate.getText().trim();
                        if (!phoneNumber.isEmpty()) {
                            try {
                            if (UserInput.phoneNumberInput(phoneNumber)) {
                                updateStudent.setPhoneNumber(phoneNumber);
                            } else {
                                failedAlert.setTitle("Phone number was not update");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure it is a valid 10-digit number in the format (###-###-####)");
                                failedAlert.showAndWait();
                            }
                        } catch(Exception exception){
                                // Ignore empty field
                        }
                    }

                    String email = emailFieldUpdate.getText().trim();
                        if(!email.isEmpty()) {
                            try {
                                if (UserInput.emailInput(email)) {
                                    updateStudent.setEmail(email.toLowerCase());
                                } else {
                                    failedAlert.setTitle("Email was not updated");
                                    failedAlert.setHeaderText(null);
                                    failedAlert.setContentText("Please make sure it is a different, unique email and in the format (example@example.com).");
                                    failedAlert.showAndWait();
                                }
                            } catch (Exception exception) {
                                // Ignore empty field
                            }
                        }


                    String gpaInput = gpaFieldUpdate.getText().trim();
                    if (!gpaInput.isEmpty()) {
                        try {
                            double gpa = Double.parseDouble(gpaInput);
                            if (UserInput.gpaInput(gpa)) {
                                updateStudent.setGpa(gpa);
                            } else {
                                failedAlert.setTitle("GPA was not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure it a double number between 0 and 1.9.");
                                failedAlert.showAndWait();
                            }
                        } catch (Exception exception) {
                            // Ignore empty field
                        }
                    }

                    try {
                        String contacted = contactedFieldUpdate.getText().trim().toLowerCase();
                        if (!contacted.isEmpty()) {
                            if (contacted.equalsIgnoreCase("true")) {
                                updateStudent.setIsContacted(true);
                            } else if (contacted.equalsIgnoreCase("false")) {
                                updateStudent.setIsContacted(false);
                            } else {
                                failedAlert.setTitle("Contacted was not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please make sure you enter 'true' for yes or 'false' for no.");
                                failedAlert.showAndWait();
                            }
                        }
                    } catch (Exception exception) {
                        // Ignore empty field
                    }

                    // Show success alert only if some fields were updated
                    successAlert.setTitle("Current Student Details");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText(updateStudent.toString());
                    successAlert.showAndWait();
                });


                clearButtonUpdate.setOnAction(c -> {
                    idFieldUpdate.clear();
                    firstNameFieldUpdate.clear();
                    lastNameFieldUpdate.clear();
                    phoneFieldUpdate.clear();
                    emailFieldUpdate.clear();
                    gpaFieldUpdate.clear();
                    contactedFieldUpdate.clear();
                });

                // Layout setup
                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10));
                grid.setVgap(8);
                grid.setHgap(10);

                grid.add(instructionLabel, 0, 0, 2, 1); // Spanning two columns
                grid.add(idLabelUpdate, 0, 1);
                grid.add(idFieldUpdate, 1, 1);
                grid.add(firstNameLabelUpdate, 0, 2);
                grid.add(firstNameFieldUpdate, 1, 2);
                grid.add(lastNameLabelUpdate, 0, 3);
                grid.add(lastNameFieldUpdate, 1, 3);
                grid.add(phoneLabelUpdate, 0, 4);
                grid.add(phoneFieldUpdate, 1, 4);
                grid.add(emailLabelUpdate, 0, 5);
                grid.add(emailFieldUpdate, 1, 5);
                grid.add(gpaLabelUpdate, 0, 6);
                grid.add(gpaFieldUpdate, 1, 6);
                grid.add(contactedLabelUpdate, 0, 7);
                grid.add(contactedFieldUpdate, 1, 7);
                grid.add(updateButtonConfirm, 0, 8);
                grid.add(clearButtonUpdate, 1, 8);

                // Scene setup
                Scene updateScene = new Scene(grid, 350, 350);
                updatePopupStage.setScene(updateScene);
                updatePopupStage.showAndWait();
            } else {
                showAlert("Invalid Input", "Please enter a valid ID to update the student.");
            }
        });

        // Action for the Clear button
        clearButton.setOnAction(e -> idField.clear());

        // Main layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(searchButton, 0, 1);
        grid.add(updateButton, 1, 1);
        grid.add(clearButton, 2, 1);

        // Scene setup
        Scene scene = new Scene(grid, 300, 100);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    // Method is called to search and view one student.
    public static void viewOneStudent() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Student Search");

        // Create label and text field for student ID input
        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        // Buttons for search and clear
        Button searchButton = new Button("Search");
        Button clearButton = new Button("Clear");

        //If student is found, it is pass as an Atomic Reference to search.
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
        grid.add(clearButton, 2, 1);

        // Scene for the popup
        Scene scene = new Scene(grid, 300, 100);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }

    // Method is called to view all students.
    public static void viewAllStudents() {
        if (students.isEmpty()) {
            showAlert("Error", "No students found in the DMS"); // Inform user if no students are present.
            return;
        }

        students.sort(Comparator.comparing(Student::getLastName));

        // Create a new Stage (window)
        Stage studentStage = new Stage();
        studentStage.setTitle("DMS Students");

        // Create a scrollable TextArea
        TextArea studentTextArea = new TextArea();
        studentTextArea.setText(students.toString());
        studentTextArea.setEditable(false); // Make it read-only
        studentTextArea.setWrapText(true);
        studentTextArea.setPrefSize(400, 300);

        // Wrap TextArea in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(studentTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Create a 'Continue' button that closes the window
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> studentStage.close());

        // Layout
        VBox layout = new VBox(10, scrollPane, continueButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(400, 300); // Set window size

        // Set the scene
        Scene scene = new Scene(layout);
        studentStage.setScene(scene);
        studentStage.show();
    }

    // Method is called to view students who have not been contacted and have their improvement GPA calculated.
    public static void notContacted() {
        // Target GPA
        double targetGPA = 2.0;

        // Filter students who have not been contacted
        List<Student> notContactedStudents = students.stream()
                .filter(student -> !student.getIsContacted())
                .sorted(Comparator.comparing(Student::getLastName)) // Sort while streaming
                .toList(); // Collect into a mutable list


        // Check if no students who have not been contacted.
        if (notContactedStudents.isEmpty()) {
            showAlert("Error", "There are no available students to display");
            return;
        }

        // Create a new window (Stage)
        Stage stage = new Stage();
        stage.setTitle("Not Contacted Students");

        // VBox to hold student details
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10;");

        // Add each student to the VBox with GPA improvement
        for (Student student : notContactedStudents) {
            Label studentLabel = new Label(student.toString());

            // Calculate GPA improvement needed
            double gpaImprovement = targetGPA - student.getGpa();
            Label gpaLabel = new Label("GPA improvement needed: " + String.format("%.2f", gpaImprovement));

            // Add student details and GPA improvement to VBox
            vbox.getChildren().addAll(studentLabel, gpaLabel, new Separator());
        }

        // Create a ScrollPane to contain the VBox
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        // Continue Button
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> stage.close());

        // VBox to hold everything (student list + button)
        VBox mainLayout = new VBox(10, scrollPane, continueButton);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-padding: 10;");

        // Create a Scene and set it in the Stage
        Scene scene = new Scene(mainLayout, 400, 350);
        stage.setScene(scene);

        // Show the new window
        stage.show();
    }



    // Helper method for input validation.
static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
