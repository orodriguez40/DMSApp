// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/30/2025

// DMSApp Class (Main application):
// This version eliminates the separate login screen.
// Users now only enter database connection details which serve as the authentication.
// If the connection is successful, the main menu (with CRUD options) is displayed.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

public class DMSApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Directly prompt the user for database connection details.
        databaseConnection(primaryStage);
    }

    // Prompts the user to enter MySQL database connection details.
    // These credentials serve as the sole authentication for using the DMS.
    // The university logo is displayed at the top of this window.
    private void databaseConnection(Stage primaryStage) {
        Stage dbStage = new Stage();
        dbStage.setTitle("Success University DMS");
        dbStage.initOwner(primaryStage);
        dbStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        // Load university logo from file and configure its appearance
        Image logoFile = new Image(Objects.requireNonNull(getClass().getResource("/Logo/logo.png")).toExternalForm());
        ImageView logoView = new ImageView(logoFile);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        // Create labels and input fields for database connection
        Label serverLabel = new Label("MySQL Server Address:");
        TextField serverField = new TextField("localhost");  // default value

        Label dbNameLabel = new Label("Database Name:");
        TextField dbNameField = new TextField();

        Label dbUserLabel = new Label("Database Username:");
        TextField dbUserField = new TextField();

        Label dbPassLabel = new Label("Database Password:");
        PasswordField dbPassField = new PasswordField();
        // Pressing Enter in the password field triggers the connect button
        Button connectButton = new Button("Connect");
        Label messageLabel = new Label();

        dbPassField.setOnAction(e -> connectButton.fire());

        connectButton.setOnAction(e -> {
            String server = serverField.getText().trim();
            String dbName = dbNameField.getText().trim();
            String dbUser = dbUserField.getText().trim();
            String dbPass = dbPassField.getText().trim();

            if (server.isEmpty() || dbName.isEmpty() || dbUser.isEmpty()) {
                messageLabel.setText("Please fill in all required fields.");
                return;
            }
            try {
                DatabaseConnector.connect(server, dbName, dbUser, dbPass);
                // Connection successful: close DB dialog and show main menu
                dbStage.close();
                showMainMenu(primaryStage);
            } catch (Exception ex) {
                messageLabel.setText("Connection failed: " + ex.getMessage());
            }
        });

        // Layout for input fields
        VBox fieldsLayout = new VBox(10, serverLabel, serverField, dbNameLabel, dbNameField,
                dbUserLabel, dbUserField, dbPassLabel, dbPassField, connectButton, messageLabel);
        fieldsLayout.setAlignment(Pos.CENTER);

        // Overall layout: logo at the top and connection fields below
        VBox layout = new VBox(20, logoView, fieldsLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 350, 470);
        dbStage.setScene(scene);
        dbStage.showAndWait();
    }

    // Displays the main menu with CRUD options.
    // Enhanced to look more polished and professional, now with a scroll bar in case content overflows.
    private void showMainMenu(Stage primaryStage) {
        // Create a BorderPane to hold a top banner and the main content
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to right, #4A90E2, #50C9C3);");

        // Top banner with the university logo and a title
        HBox topBanner = new HBox();
        topBanner.setAlignment(Pos.CENTER);
        topBanner.setSpacing(15);

        // Load the same university logo for consistency
        Image logoFile = new Image(Objects.requireNonNull(getClass().getResource("/Logo/logo.png")).toExternalForm());
        ImageView logoView = new ImageView(logoFile);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);

        Label mainMenuLabel = new Label("Success University DMS\nMain Menu");
        mainMenuLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        topBanner.getChildren().addAll(logoView, mainMenuLabel);
        mainLayout.setTop(topBanner);

        // VBox for buttons
        VBox buttonLayout = new VBox(15);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(30, 20, 30, 20));

        // Subtitle label (fixed CSS: using bold instead of semi-bold)
        Label subLabel = new Label("Please choose from the following options:");
        subLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

        // Create the CRUD option buttons
        Button addStudentManualButton = new Button("Add Student Manually");
        addStudentManualButton.setOnAction(e -> StudentManagement.addStudentManual());

        Button addStudentFileButton = new Button("Add Students from File");
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

        // Style the buttons for a cohesive look
        for (Button button : new Button[]{
                addStudentManualButton, addStudentFileButton, removeStudentButton,
                updateStudentButton, viewOneStudentButton, viewAllStudentsButton, notContactedButton
        }) {
            button.setMinWidth(200);
            button.setStyle("-fx-font-size: 14px; " +
                    "-fx-background-color: #4C9ED9; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-color: white; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px 20px;");
        }

        // Add all items to the button layout
        buttonLayout.getChildren().add(subLabel);
        buttonLayout.getChildren().addAll(
                addStudentManualButton, addStudentFileButton, removeStudentButton,
                updateStudentButton, viewOneStudentButton, viewAllStudentsButton, notContactedButton
        );

        // Wrap the button layout in a StackPane for the semi-transparent background
        StackPane centerPane = new StackPane(buttonLayout);
        centerPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 20;");

        // Now add a ScrollPane so the user can scroll if needed
        ScrollPane scrollPane = new ScrollPane(centerPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Put the ScrollPane in the center of the main layout
        mainLayout.setCenter(scrollPane);

        // Create and set the scene
        Scene mainMenuScene = new Scene(mainLayout, 600, 500);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Success University DMS");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
