// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// DMSApp Class (Main application):
// This class handles the GUI for the user to see and work with.
// It will display a list of CRUD operations and the custom action.
// The user will interact directly with the UI to perform the CRUD operations and the custom action.
// The class will call other methods from other classes as needed to perform input validation.

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
import java.util.Optional;

import java.io.File;

public class DMSApp extends Application {
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("Output will be displayed here");

        // Load university logo
        File logoFile = new File("Logo/logo.png");
        ImageView logoView = new ImageView();
        if (logoFile.exists()) {
            logoView.setImage(new Image(logoFile.toURI().toString()));
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
        }

        // Username field
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        // Password field
        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0077cc; -fx-text-fill: white;");
        Label messageLabel = new Label();

        // Handle login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (Authenticator.authenticate(username, password)) {
                messageLabel.setText("Login Successful! Welcome to the DMS.");
                showMainMenu(primaryStage);
            } else {
                messageLabel.setText("Invalid username or password. Try again.");
            }
        });

        // Handle pressing Enter on password field for login
        passwordField.setOnAction(e -> loginButton.fire());

        // Layout for login screen
        VBox inputBox = new VBox(10, userLabel, usernameField, passLabel, passwordField, loginButton, messageLabel);
        inputBox.setAlignment(Pos.CENTER);

        HBox mainLayout = new HBox(20, inputBox, logoView);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        // Scene and Stage setup
        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setTitle("University DMS Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainMenu(Stage primaryStage) {
        // Create the main menu layout
        VBox buttonLayout = new VBox(15);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(20));

        Label mainMenuLabel = new Label("DMS Main Menu:");
        mainMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Buttons for different actions
        Button addStudentManualButton = new Button("Add Student Manually");
        addStudentManualButton.setOnAction(e -> addStudentManualGUI());

        Button addStudentFileButton = new Button("Add Student by File");
        addStudentFileButton.setOnAction(e -> addStudentFileGUI());

        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setOnAction(e -> removeStudentGUI());

        Button updateStudentButton = new Button("Update Student");
        updateStudentButton.setOnAction(e -> updateStudentGUI());

        Button viewOneStudentButton = new Button("Student Search");
        viewOneStudentButton.setOnAction(e -> viewOneStudentGUI());

        Button viewAllStudentsButton = new Button("View All Students");
        viewAllStudentsButton.setOnAction(e -> viewAllStudentsGUI());

        Button notContactedButton = new Button("View Not Contacted Students");
        notContactedButton.setOnAction(e -> notContactedGUI());

        // Apply styling to buttons
        for (Button button : new Button[]{
                addStudentManualButton, addStudentFileButton, removeStudentButton,
                updateStudentButton, viewOneStudentButton, viewAllStudentsButton, notContactedButton
        }) {
            button.setMinWidth(200);
            button.setStyle("-fx-font-size: 14px; -fx-background-color: #4C9ED9; -fx-text-fill: white;");
        }

        buttonLayout.getChildren().addAll(
                mainMenuLabel, addStudentManualButton, addStudentFileButton,
                removeStudentButton, updateStudentButton, viewOneStudentButton,
                viewAllStudentsButton, notContactedButton, outputArea
        );


        // Align buttons to the left and logo to the right
        HBox mainContent = new HBox(30, buttonLayout);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(50); // Added spacing for better appearance

        // Scene and Stage setup
        Scene mainMenuScene = new Scene(mainContent, 540, 550); // Increased width to 800
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("DMS Application");
        primaryStage.show();
    }

    private void addStudentManualGUI() {
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

                // Check if the ID is already in the system
                if (!UserInput.manualIDInput(id)) {
                    throw new IllegalArgumentException("Invalid ID: ID already exists in the system.");
                }

                if(firstNameField.getText().length() > 15) {
                    throw new IllegalArgumentException("Invalid First Name: Must be less than 15 characters.");
                }

                String firstName = firstNameField.getText();

                if(lastNameField.getText().length() > 25) {
                    throw new IllegalArgumentException("Invalid Last Name: Must be less than 25 characters.");
                }

                String lastName = lastNameField.getText();


                if(!phoneField.getText().matches("\\d{3}-\\d{3}-\\d{4}")){
                    throw new IllegalArgumentException("Invalid Phone Number: Valid format (555-555-5555).");

                }

                String phoneNumber = phoneField.getText();

                if(!emailField.getText().matches("^[A-Za-z][A-Za-z0-9._-]*(?<![._-])@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")){
                    throw new IllegalArgumentException("Invalid Email: Valid format (example@example.com).");
                }

                String email = emailField.getText();

                if(!UserInput.emailInput(email)) {
                    throw new IllegalArgumentException("Invalid Email: Email must be unique.");
                }



                if (!gpaField.getText().matches("-?\\d+(\\.\\d+)?")) {
                    throw new IllegalArgumentException("Invalid input: Please enter a double number.");
                }

                double gpa = Double.parseDouble(gpaField.getText());

                if (gpa < 0 || gpa >= 1.9) {
                    throw new IllegalArgumentException("Invalid GPA: Must be between 0 and 1.9.");
                }


                boolean isContacted = Boolean.parseBoolean(contactedField.getText());

                if (!contactedField.getText().equalsIgnoreCase("true") && !contactedField.getText().equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid input: Please enter 'true' for yes or 'false' no.");
                }

                Student newStudent = UserInput.getStudentInfo(id, firstName, lastName, phoneNumber, email, gpa, isContacted);
                boolean studentAdded = StudentManagement.addStudentManual(newStudent);

                if (studentAdded) {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void addStudentFileGUI() {
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
                // Show error if the file path is blank
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a file before uploading.");
                errorAlert.showAndWait();
            } else {
                // Ask for confirmation before proceeding with the upload
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Upload");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to upload the student data from the file: " + filePath + "?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    outputArea.appendText("Uploading student data from file: " + filePath + "\n");
                    popupStage.close(); // Close the popup after uploading
                }
            }
        });

        // Action for the Clear button
        clearButton.setOnAction(e -> {
            filePathField.clear(); // Clear the file path field
        });

        // Layout for the popup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(filePathLabel, 0, 0);
        grid.add(filePathField, 1, 0);
        grid.add(chooseFileButton, 2, 0);
        grid.add(uploadButton, 0, 1);
        grid.add(clearButton, 1, 1);

        // Scene for the popup
        Scene scene = new Scene(grid, 350, 100);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }

    private void removeStudentGUI() {
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

        // Action for the Search button
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                outputArea.appendText("Searching for student with ID: " + studentId + "\n");
            } else {
                outputArea.appendText("Please enter a student ID to search.\n");
            }
        });

        // Action for the Delete button
        deleteButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                outputArea.appendText("Deleting student with ID: " + studentId + "\n");
                popupStage.close();
            } else {
                outputArea.appendText("Please enter a student ID to delete.\n");
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

    private void updateStudentGUI() {
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

        // Action for the Search button
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                outputArea.appendText("Searching for student with ID: " + studentId + "\n");
            } else {
                outputArea.appendText("Please enter a student ID to search.\n");
            }
        });

        // Action for the Update button
        updateButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                outputArea.appendText("Updating student with ID: " + studentId + "\n");
                popupStage.close();
            } else {
                outputArea.appendText("Please enter a student ID to update.\n");
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

    private void viewOneStudentGUI() {
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

        // Action for the Search button
        searchButton.setOnAction(e -> {
            String studentId = idField.getText().trim();
            if (!studentId.isEmpty()) {
                outputArea.appendText("Searching for student with ID: " + studentId + "\n");
            } else {
                outputArea.appendText("Please enter a student ID to search.\n");
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
        Scene scene = new Scene(grid, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }

    private void viewAllStudentsGUI() {
        // Empty method to be filled with your logic
    }

    private void notContactedGUI() {
        // Empty method to be filled with your logic
    }

    public static void main(String[] args) {
        launch(args);
    }
}
