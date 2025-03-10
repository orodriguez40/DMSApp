// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// DMSApp Class (Main application):
// This class handles the GUI for the user to interact with the student management system.
// It displays options for CRUD operations and a custom action.
// The user interacts with the UI to perform actions, and methods from other classes handle processing and validation.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class DMSApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load university logo from file
        File logoFile = new File("Logo/logo.png");
        ImageView logoView = new ImageView();
        if (logoFile.exists()) {
            logoView.setImage(new Image(logoFile.toURI().toString()));
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
        }

        // Username input field
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        // Password input field
        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0077cc; -fx-text-fill: white;");
        Label messageLabel = new Label(); // Label to show error messages if login fails

        // Handle login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Authenticate user using the Authenticator class
            if (Authenticator.authenticate(username, password)) {
                showMainMenu(primaryStage); // Show main menu on successful login
            } else {
                messageLabel.setText("Invalid username or password"); // Display error message
            }
        });

        // Allow pressing Enter in password field to trigger login
        passwordField.setOnAction(e -> loginButton.fire());

        // Layout for login screen
        VBox inputBox = new VBox(10, userLabel, usernameField, passLabel, passwordField, loginButton, messageLabel);
        inputBox.setAlignment(Pos.CENTER);

        // Main layout with login fields and logo
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
        // Create layout for main menu
        VBox buttonLayout = new VBox(15);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(20));

        // Main menu title
        Label mainMenuLabel = new Label("Main Menu\n Please choose from the following options:");
        mainMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center");

        // Buttons for CRUD operations and custom actions
        Button addStudentManualButton = new Button("Add Student Manually");
        addStudentManualButton.setOnAction(e -> StudentManagement.addStudentManual());

        Button addStudentFileButton = new Button("Add Student by File");
        addStudentFileButton.setOnAction(e -> StudentManagement.addStudentFile());

        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setOnAction(e -> StudentManagement.removeStudent());

        Button updateStudentButton = new Button("Update Student");
        updateStudentButton.setOnAction(e -> StudentManagement.updateStudent());

        Button viewOneStudentButton = new Button("Student Search");
        viewOneStudentButton.setOnAction(e -> StudentManagement.viewOneStudent());

        Button viewAllStudentsButton = new Button("View All Students");
        viewAllStudentsButton.setOnAction(e -> StudentManagement.viewAllStudents());

        Button notContactedButton = new Button("View Not Contacted Students");
        notContactedButton.setOnAction(e -> StudentManagement.notContacted());

        // Apply styling to buttons for consistency
        for (Button button : new Button[]{
                addStudentManualButton, addStudentFileButton, removeStudentButton,
                updateStudentButton, viewOneStudentButton, viewAllStudentsButton, notContactedButton
        }) {
            button.setMinWidth(200);
            button.setStyle("-fx-font-size: 14px; " +
                    "-fx-background-color: #4C9ED9; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-color: white; " +   // White border outline
                    "-fx-border-width: 2px; " +    // Border thickness
                    "-fx-border-radius: 5px; " +   // Rounded corners
                    "-fx-padding: 10px 20px;");    // Better button padding
        }

        // Add buttons to the layout
        buttonLayout.getChildren().addAll(
                mainMenuLabel, addStudentManualButton, addStudentFileButton,
                removeStudentButton, updateStudentButton, viewOneStudentButton,
                viewAllStudentsButton, notContactedButton
        );

        // Align buttons to the center for better UI appearance
        HBox mainContent = new HBox(30, buttonLayout);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(50); // Added spacing for better appearance

        // Apply styling to the main menu layout
        mainContent.setPadding(new Insets(20));
        mainContent.setSpacing(15);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setStyle("-fx-background-color: linear-gradient(to right, #4A90E2, #50C9C3);"); // Gradient background

        // Scene and Stage setup with improved styling
        Scene mainMenuScene = new Scene(mainContent, 550, 550); // Slightly larger for better spacing
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Success University DMS");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
