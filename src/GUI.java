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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class GUI extends Application {
    private Admin admin;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the Admin class instance
        admin = new Admin();

        // Load university logo
        File logoFile = new File("Logo/logo.png");
        if (logoFile.exists()) {
            ImageView logoView = new ImageView(new Image(logoFile.toURI().toString()));
            logoView.setFitWidth(150); // Resize logo
            logoView.setPreserveRatio(true);

            // Username field
            Label userLabel = new Label("Username:");
            TextField usernameField = new TextField();

            // Password field
            Label passLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            // Login button
            Button loginButton = new Button("Login");
            Label messageLabel = new Label(); // Label to display messages

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
            // Handle the case where the logo file is not found
            System.out.println("Logo file not found.");
        }
    }

    private void showMainMenu(Stage primaryStage) {
        // Create the main menu layout
        VBox mainMenuLayout = new VBox(20);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.setPadding(new Insets(20));

        Label mainMenuLabel = new Label("DMS Main Menu:");
        mainMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button addStudentManuallyButton = new Button("Add Student Manually");
        addStudentManuallyButton.setOnAction(e -> admin.handleUserChoice(1));

        Button addStudentByFileButton = new Button("Add Student by File");
        addStudentByFileButton.setOnAction(e -> admin.handleUserChoice(2));

        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setOnAction(e -> admin.handleUserChoice(3));

        Button updateStudentButton = new Button("Update Student");
        updateStudentButton.setOnAction(e -> admin.handleUserChoice(4));

        Button viewOneStudentButton = new Button("View One Student");
        viewOneStudentButton.setOnAction(e -> admin.handleUserChoice(5));

        Button viewAllStudentsButton = new Button("View All Students");
        viewAllStudentsButton.setOnAction(e -> admin.handleUserChoice(6));

        Button viewUncontactedStudentsButton = new Button("View Non-contacted Students");
        viewUncontactedStudentsButton.setOnAction(e -> admin.handleUserChoice(7));

        mainMenuLayout.getChildren().addAll(
                mainMenuLabel,
                addStudentManuallyButton,
                addStudentByFileButton,
                removeStudentButton,
                updateStudentButton,
                viewOneStudentButton,
                viewAllStudentsButton,
                viewUncontactedStudentsButton
        );

        // Scene and Stage setup
        Scene mainMenuScene = new Scene(mainMenuLayout, 600, 400);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("DMS Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
