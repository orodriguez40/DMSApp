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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;

public class DMSApp extends Application {

    @Override
    public void start(Stage primaryStage) {
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
                showMainMenu(primaryStage);
            } else {
                messageLabel.setText("Invalid username or password");
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

        Label mainMenuLabel = new Label("Main Menu\n Please choose from the following options:");
        mainMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center");

        // Buttons for different actions
        Button addStudentManualButton = new Button("Add Student Manually");
        addStudentManualButton.setOnAction(e -> StudentManagement.addStudentManual());

        Button addStudentFileButton = new Button("Add Student by File");
        addStudentFileButton.setOnAction(e -> StudentManagement.addStudentFile());

        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setOnAction(e -> StudentManagement.removeStudent());

        Button updateStudentButton = new Button("Update Student");
        updateStudentButton.setOnAction(e -> StudentManagement.updateStudent());

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
            button.setStyle("-fx-font-size: 14px; " +
                    "-fx-background-color: #4C9ED9; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-color: white; " +   // White border outline
                    "-fx-border-width: 2px; " +    // Border thickness
                    "-fx-border-radius: 5px; " +   // Rounded corners
                    "-fx-padding: 10px 20px;");    // Better button padding

        }

        buttonLayout.getChildren().addAll(
                mainMenuLabel, addStudentManualButton, addStudentFileButton,
                removeStudentButton, updateStudentButton, viewOneStudentButton,
                viewAllStudentsButton, notContactedButton
        );


        // Align buttons to the left and logo to the right
        HBox mainContent = new HBox(30, buttonLayout);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(50); // Added spacing for better appearance

        // Apply styling to the main content layout
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
              //  outputArea.appendText("Searching for student with ID: " + studentId + "\n");
            } else {
               // outputArea.appendText("Please enter a student ID to search.\n");
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
               // outputArea.appendText("Searching for student with ID: " + studentId + "\n");
            } else {
               // outputArea.appendText("Please enter a student ID to search.\n");
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
