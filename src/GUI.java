// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// GUI class (Main Application):
// This class handles the setup for the Graphical User Interface the user will interact with.
// This is where the DMS application will run.
// The user will open the JAR file and access the user interface.
// They will have options to add a student manually or by file,
// remove a student based on their ID, view students,
// update student information, perform the custom action,
// or close the application.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class GUI extends Application {
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the Admin instance with the output area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("Output will be displayed here");

        // Load university logo
        File logoFile = new File("Logo/logo.png");
        if (logoFile.exists()) {
            ImageView logoView = new ImageView(new Image(logoFile.toURI().toString()));
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);

            // Username field
            Label userLabel = new Label("Username:");
            TextField usernameField = new TextField();

            // Password field
            Label passLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            // Login button
            Button loginButton = new Button("Login");
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

            // Layout
            VBox layout = new VBox(10, logoView, userLabel, usernameField, passLabel, passwordField, loginButton, messageLabel);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-padding: 20px;");

            // Scene and Stage setup
            Scene scene = new Scene(layout, 300, 400);
            primaryStage.setTitle("University DMS Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            outputArea.appendText("Logo file not found.\n");
        }
    }

    private void showMainMenu(Stage primaryStage) {
        // Create the main menu layout
        VBox mainMenuLayout = new VBox(20);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.setPadding(new Insets(20));

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

        Button viewOneStudentButton = new Button("View One Student");
        viewOneStudentButton.setOnAction(e -> viewOneStudentGUI());

        Button viewAllStudentsButton = new Button("View All Students");
        viewAllStudentsButton.setOnAction(e -> viewAllStudentsGUI());

        Button notContactedButton = new Button("View Not Contacted Students");
        notContactedButton.setOnAction(e -> notContactedGUI());

        // Add the output area to the layout
        mainMenuLayout.getChildren().addAll(
                mainMenuLabel,
                addStudentManualButton,
                addStudentFileButton,
                removeStudentButton,
                updateStudentButton,
                viewOneStudentButton,
                outputArea
        );

        // Scene and Stage setup
        Scene mainMenuScene = new Scene(mainMenuLayout, 600, 600);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("DMS Application");
        primaryStage.show();
    }

    private void addStudentManualGUI() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Student Manually");

        // Create labels and text fields for student information
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
        TextField contactedField = new TextField(); // Changed to TextField

        // Buttons for adding and clearing details
        Button addButton = new Button("Add");
        Button clearButton = new Button("Clear");

        // Action for the Add button
        addButton.setOnAction(e -> {
            // Logic to add the student will be implemented later
            outputArea.appendText("Adding student: " + firstNameField.getText() + " " + lastNameField.getText() + "\n");
            popupStage.close(); // Close the popup after adding
        });

        // Action for the Clear button
        clearButton.setOnAction(e -> {
            idField.clear();
            firstNameField.clear();
            lastNameField.clear();
            phoneField.clear();
            emailField.clear();
            gpaField.clear();
            contactedField.clear(); // Clear the contacted text field
        });

        // Layout for the popup
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
        grid.add(contactedField, 1, 6); // Add the contacted text field
        grid.add(addButton, 0, 7);
        grid.add(clearButton, 1, 7);

        // Scene for the popup
        Scene scene = new Scene(grid, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
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
            if (!filePath.isEmpty()) {
                // Logic to process the file will be implemented later
                outputArea.appendText("Uploading student data from file: " + filePath + "\n");
                // Call your method to handle file upload here
                // admin.addStudentFile(filePath);
                popupStage.close(); // Close the popup after uploading
            } else {
                outputArea.appendText("Please select a file before uploading.\n");
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
        Scene scene = new Scene(grid, 400, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to close
    }

    private void removeStudentGUI() {
    }

    private void updateStudentGUI() {
    }

    private void viewOneStudentGUI() {
    }

    private void viewAllStudentsGUI() {
    }

    private void notContactedGUI() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
