// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 04/03/2025

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Optional;

/**
 * StudentManagement Class:
 * Manages all CRUD functions and the custom action.
 * <p>
 * This class handles adding, updating, deleting, and viewing students,
 * both manually and via file upload, and also displays students that have not been contacted.
 * </p>
 */
public class StudentManagement {

    // Attribute to store all Students in an ArrayList (for display purposes).
    static final List<Student> students = new ArrayList<>();

    /**
     * Method is called to add a student manually.
     */
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

                // Validates GPA.
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

                // Adds student to the students array list to display to the user.
                Student newStudent = UserInput.getStudentInfo(id, firstName, lastName, phoneNumber, email, gpa, isContacted);

                // Ask for confirmation before adding student.
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Add Student?");
                confirmationAlert.setContentText("Are you sure you want to add this student?");
                assert newStudent != null;
                confirmationAlert.setContentText(newStudent.toString());

                // Show the alert and wait for user response.
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // If user clicked 'Yes' (OK), add the student.
                    // Insert student into the database.
                    String insertSQL = "INSERT INTO students (id, firstName, lastName, phoneNumber, email, gpa, isContacted) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                        stmt.setInt(1, id);
                        stmt.setString(2, firstName);
                        stmt.setString(3, lastName);
                        stmt.setString(4, phoneNumber);
                        stmt.setString(5, email);
                        stmt.setDouble(6, gpa);
                        stmt.setBoolean(7, isContacted);
                        stmt.executeUpdate();

                        // Student is added to the students list.
                        students.add(newStudent);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Student Added!");
                        alert.setHeaderText(null);
                        assert newStudent != null;
                        alert.setContentText(newStudent.toString());
                        alert.showAndWait();
                    } catch (SQLException ex) {
                        showAlert("Database Error", "Failed to add student: " + ex.getMessage());
                    }

                    // Clear fields after successful insertion.
                    idField.clear();
                    firstNameField.clear();
                    lastNameField.clear();
                    phoneField.clear();
                    emailField.clear();
                    gpaField.clear();
                    contactedField.clear();
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

    /**
     * Method is called to add students by file upload.
     */
    public static void addStudentFile() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Student(s) by File");

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
                FileHandler.addStudentsByFile(filePath);
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

    /**
     * Method is called to delete a student from the DMS database.
     */
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

        // If student is found, it is passed as an array for deletion.
        final Student[] foundStudent = {null};

        // Action for the Search button.
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                foundStudent[0] = UserInput.searchStudentByID(studentId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (foundStudent[0] != null) {
                    alert.setTitle("Student found");
                    alert.setHeaderText(null);
                    alert.setContentText(foundStudent[0].toString());
                    alert.showAndWait();
                }
            } else {
                showAlert("Error", "Please enter a student ID to search.");
            }
        });

        // Action for the Delete button.
        deleteButton.setOnAction(e -> {
            if (foundStudent[0] != null) {
                // Confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Deletion");
                confirmationAlert.setHeaderText("Are you sure you want to delete this student?");
                confirmationAlert.setContentText(foundStudent[0].toString());

                // Add Yes and No options
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                // Show the confirmation window
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    String deleteSQL = "DELETE FROM students WHERE id = ?";
                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
                        stmt.setInt(1, foundStudent[0].getId());
                        stmt.executeUpdate();
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Deletion Successful");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Student successfully deleted:\n" + foundStudent[0].toString());
                        successAlert.showAndWait();
                        idField.clear();
                    } catch (SQLException ex) {
                        showAlert("Database Error", "Failed to delete student: " + ex.getMessage());
                    }
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

    /**
     * Method is called to update student information.
     */
    public static void updateStudent() {
        // Create a new Stage for the ID search popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Update Student");

        // Create label and text field for student ID input
        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        // Buttons for search, update, and clear (in the ID search popup)
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");

        // If student is found, it is passed via an array for modification.
        final Student[] updateStudentHolder = {null};

        // Action for the Search button: search for the student by ID.
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                Student found = UserInput.searchStudentByID(studentId);
                updateStudentHolder[0] = found;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (found != null) {
                    alert.setTitle("Student Found");
                    alert.setHeaderText(null);
                    alert.setContentText(found.toString());
                    alert.showAndWait();
                }
            } else {
                showAlert("Error", "Please enter a student ID to search.");
            }
        });

        // Action for the Update button in the ID search popup.
        // When clicked, if a student is found, open a new popup with pre-populated fields.
        updateButton.setOnAction(e -> {
            if (updateStudentHolder[0] != null) {
                Stage updatePopupStage = new Stage();
                updatePopupStage.initModality(Modality.APPLICATION_MODAL);
                updatePopupStage.setTitle("Update Student Details");

                // Instruction label
                Label instructionLabel = new Label("Enter new values (leave blank to ignore):");
                instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-padding: 5px;");

                // Pre-populated fields for update
                Label idLabelUpdate = new Label("ID:");
                TextField idFieldUpdate = new TextField(String.valueOf(updateStudentHolder[0].getId()));
                Label firstNameLabelUpdate = new Label("First Name:");
                TextField firstNameFieldUpdate = new TextField(updateStudentHolder[0].getFirstName());
                Label lastNameLabelUpdate = new Label("Last Name:");
                TextField lastNameFieldUpdate = new TextField(updateStudentHolder[0].getLastName());
                Label phoneLabelUpdate = new Label("Phone Number:");
                TextField phoneFieldUpdate = new TextField(updateStudentHolder[0].getPhoneNumber());
                Label emailLabelUpdate = new Label("Email:");
                TextField emailFieldUpdate = new TextField(updateStudentHolder[0].getEmail());
                Label gpaLabelUpdate = new Label("GPA:");
                TextField gpaFieldUpdate = new TextField(String.valueOf(updateStudentHolder[0].getGpa()));
                Label contactedLabelUpdate = new Label("Contacted Status:");
                TextField contactedFieldUpdate = new TextField(String.valueOf(updateStudentHolder[0].getIsContacted()));

                Button updateButtonConfirm = new Button("Confirm Update");
                Button clearButtonUpdate = new Button("Clear");

                // Action for the Confirm Update button.
                updateButtonConfirm.setOnAction(ev -> {
                    // Alerts to display updates
                    Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    Alert failedAlert = new Alert(Alert.AlertType.ERROR);

                    // Start with the current values
                    int newId = updateStudentHolder[0].getId();
                    String newFirstName = updateStudentHolder[0].getFirstName();
                    String newLastName = updateStudentHolder[0].getLastName();
                    String newPhoneNumber = updateStudentHolder[0].getPhoneNumber();
                    String newEmail = updateStudentHolder[0].getEmail();
                    double newGpa = updateStudentHolder[0].getGpa();
                    boolean newIsContacted = updateStudentHolder[0].getIsContacted();

                    // Retrieve input values; if blank, original values remain.
                    String idInput = idFieldUpdate.getText().trim();
                    String firstNameInput = firstNameFieldUpdate.getText().trim();
                    String lastNameInput = lastNameFieldUpdate.getText().trim();
                    String phoneInput = phoneFieldUpdate.getText().trim();
                    String emailInput = emailFieldUpdate.getText().trim();
                    String gpaInput = gpaFieldUpdate.getText().trim();
                    String contactedInput = contactedFieldUpdate.getText().trim();

                    // Validate and update ID if provided.
                    if (!idInput.isEmpty()) {
                        try {
                            int parsedId = Integer.parseInt(idInput);
                            boolean validId = UserInput.manualIDInput(parsedId);
                            // Allow same ID if unchanged
                            if (validId || parsedId == updateStudentHolder[0].getId()) {
                                newId = parsedId;
                            } else {
                                failedAlert.setTitle("ID not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please ensure the ID is a valid 8-digit number and not a duplicate.");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            failedAlert.setTitle("ID not updated");
                            failedAlert.setHeaderText(null);
                            failedAlert.setContentText("Invalid ID format.");
                            failedAlert.showAndWait();
                            return;
                        }
                    }

                    // Validate and update First Name if provided.
                    if (!firstNameInput.isEmpty()) {
                        try {
                            String modifiedFirstName = firstNameInput.substring(0, 1).toUpperCase() +
                                    firstNameInput.substring(1).toLowerCase();
                            boolean validFirstName = UserInput.firstNameInput(modifiedFirstName);
                            if (validFirstName) {
                                newFirstName = modifiedFirstName;
                            } else {
                                failedAlert.setTitle("First name not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("First name must contain only letters and be at most 15 characters.");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (Exception ex) {
                            // Ignores empty field.
                        }
                    }

                    // Validate and update Last Name if provided.
                    if (!lastNameInput.isEmpty()) {
                        try {
                            String modifiedLastName = lastNameInput.substring(0, 1).toUpperCase() +
                                    lastNameInput.substring(1).toLowerCase();
                            boolean validLastName = UserInput.lastNameInput(modifiedLastName);
                            if (validLastName) {
                                newLastName = modifiedLastName;
                            } else {
                                failedAlert.setTitle("Last name not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Last name must contain only letters and be at most 25 characters.");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (Exception ex) {
                            // Ignores empty field.
                        }
                    }

                    // Validate and update Phone Number if provided.
                    if (!phoneInput.isEmpty()) {
                        try {
                            if (UserInput.phoneNumberInput(phoneInput)) {
                                newPhoneNumber = phoneInput;
                            } else {
                                failedAlert.setTitle("Phone number not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Please use a valid format: xxx-xxx-xxxx.");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (Exception ex) {
                            // Ignores empty field.
                        }
                    }

                    // Validate and update Email if provided.
                    if (!emailInput.isEmpty()) {
                        try {
                            if (UserInput.emailInput(emailInput)) {
                                newEmail = emailInput.toLowerCase();
                            } else {
                                failedAlert.setTitle("Email not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("Ensure the email is unique and in a valid format (example@example.com).");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (Exception ex) {
                            // Ignores empty field.
                        }
                    }

                    // Validate and update GPA if provided.
                    if (!gpaInput.isEmpty()) {
                        try {
                            double parsedGpa = Double.parseDouble(gpaInput);
                            if (UserInput.gpaInput(parsedGpa)) {
                                newGpa = parsedGpa;
                            } else {
                                failedAlert.setTitle("GPA not updated");
                                failedAlert.setHeaderText(null);
                                failedAlert.setContentText("GPA must be a number between 0 and 1.9.");
                                failedAlert.showAndWait();
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            failedAlert.setTitle("GPA not updated");
                            failedAlert.setHeaderText(null);
                            failedAlert.setContentText("Invalid GPA format.");
                            failedAlert.showAndWait();
                            return;
                        }
                    }

                    // Validate and update Contacted Status if provided.
                    if (!contactedInput.isEmpty()) {
                        if (contactedInput.equalsIgnoreCase("true")) {
                            newIsContacted = true;
                        } else if (contactedInput.equalsIgnoreCase("false")) {
                            newIsContacted = false;
                        } else {
                            failedAlert.setTitle("Contacted status not updated");
                            failedAlert.setHeaderText(null);
                            failedAlert.setContentText("Please enter 'true' or 'false' for contacted status.");
                            failedAlert.showAndWait();
                            return;
                        }
                    }

                    // Build the SQL update statement and execute it once.
                    String updateSQL = "UPDATE students SET id = ?, firstName = ?, lastName = ?, phoneNumber = ?, email = ?, gpa = ?, isContacted = ? WHERE id = ?";
                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

                        stmt.setInt(1, newId);
                        stmt.setString(2, newFirstName);
                        stmt.setString(3, newLastName);
                        stmt.setString(4, newPhoneNumber);
                        stmt.setString(5, newEmail);
                        stmt.setDouble(6, newGpa);
                        stmt.setBoolean(7, newIsContacted);
                        // Use the original ID for the WHERE clause.
                        stmt.setInt(8, updateStudentHolder[0].getId());

                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            // Update the student object with new values.
                            updateStudentHolder[0].setId(newId);
                            updateStudentHolder[0].setFirstName(newFirstName);
                            updateStudentHolder[0].setLastName(newLastName);
                            updateStudentHolder[0].setPhoneNumber(newPhoneNumber);
                            updateStudentHolder[0].setEmail(newEmail);
                            updateStudentHolder[0].setGpa(newGpa);
                            updateStudentHolder[0].setIsContacted(newIsContacted);

                            successAlert.setTitle("Current Student Details");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText(updateStudentHolder[0].toString());
                            successAlert.showAndWait();
                        } else {
                            failedAlert.setTitle("Update Failed");
                            failedAlert.setHeaderText(null);
                            failedAlert.setContentText("No changes were made.");
                            failedAlert.showAndWait();
                        }
                    } catch (SQLException ex) {
                        showAlert("Update Error", ex.getMessage());
                    }
                });

                // Clears all field values.
                clearButtonUpdate.setOnAction(c -> {
                    idFieldUpdate.clear();
                    firstNameFieldUpdate.clear();
                    lastNameFieldUpdate.clear();
                    phoneFieldUpdate.clear();
                    emailFieldUpdate.clear();
                    gpaFieldUpdate.clear();
                    contactedFieldUpdate.clear();
                });

                // Layout setup for the update popup.
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

                // Scene setup for the update popup.
                Scene updateScene = new Scene(grid, 350, 350);
                updatePopupStage.setScene(updateScene);
                updatePopupStage.showAndWait();
            } else {
                showAlert("Invalid Input", "Please enter a valid ID to update the student.");
            }
        });

        // Action for the Clear button in the ID search popup.
        clearButton.setOnAction(e -> idField.clear());

        // Main layout for the ID search popup.
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(searchButton, 0, 1);
        grid.add(updateButton, 1, 1);
        grid.add(clearButton, 2, 1);

        // Scene setup for the ID search popup.
        Scene scene = new Scene(grid, 300, 100);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    /**
     * Method is called to search and view one student.
     */
    public static void viewOneStudent() {
        // Create a new Stage for the popup.
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Student Search");

        // Create label and text field for student ID input.
        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        // Buttons for search and clear.
        Button searchButton = new Button("Search");
        Button clearButton = new Button("Clear");

        // Action for the Search button.
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {

                Student findStudent = UserInput.searchStudentByID(studentId);

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

        // Action for the Clear button.
        clearButton.setOnAction(e -> idField.clear());

        // Layout for the popup.
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(searchButton, 0, 1);
        grid.add(clearButton, 2, 1);

        // Scene for the popup.
        Scene scene = new Scene(grid, 300, 100);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }

    /**
     * Method is called to view all students.
     */
    public static void viewAllStudents() {
        StringBuilder studentsStr = new StringBuilder();
        String selectSQL = "SELECT * FROM students ORDER BY lastName";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"),
                        rs.getDouble("gpa"),
                        rs.getBoolean("isContacted")
                );
                studentsStr.append(s.toString());
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Error retrieving students: " + ex.getMessage());
            return;
        }

        if (studentsStr.length() == 0) {
            showAlert("No Students", "No students found in the DMS.");
            return;
        }

        Stage studentStage = new Stage();
        studentStage.setTitle("DMS Students");

        TextArea studentTextArea = new TextArea(studentsStr.toString());
        studentTextArea.setEditable(false);
        studentTextArea.setWrapText(true);
        studentTextArea.setPrefSize(400, 300);

        ScrollPane scrollPane = new ScrollPane(studentTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> studentStage.close());

        VBox layout = new VBox(10, scrollPane, continueButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(400, 300);

        Scene scene = new Scene(layout);
        studentStage.setScene(scene);
        studentStage.show();
    }

    /**
     * Method to view students who have not been contacted and display the GPA improvement needed.
     */
    public static void notContacted() {
        double targetGPA = 2.0;
        String selectSQL = "SELECT * FROM students WHERE isContacted = false ORDER BY lastName";
        StringBuilder displayText = new StringBuilder();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"),
                        rs.getDouble("gpa"),
                        rs.getBoolean("isContacted")
                );
                double gpaImprovement = targetGPA - s.getGpa();
                displayText.append(s.toString())
                        .append("GPA improvement needed: ")
                        .append(String.format("%.2f", gpaImprovement))
                        .append("\n----------------------\n");
            }
            if (!found) {
                showAlert("Error", "There are no available students to display.");
                return;
            }
        } catch (SQLException ex) {
            showAlert("Database Error", "Error retrieving students: " + ex.getMessage());
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Not Contacted Students");

        TextArea textArea = new TextArea(displayText.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 350);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> stage.close());

        VBox layout = new VBox(10, textArea, continueButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 350);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Helper method for input validation that displays an alert with the provided title and message.
     *
     * @param title   the title of the alert.
     * @param message the message to display in the alert.
     */
    static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
