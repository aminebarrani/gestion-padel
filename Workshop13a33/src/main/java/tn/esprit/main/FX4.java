package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FX4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ensure the correct path to your FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterproduit.fxml"));

            // Ensure the FXML file is loaded correctly
            AnchorPane root = loader.load();

            // Set up the scene
            Scene scene = new Scene(root);

            // Optional: Add a stylesheet (if you have one)
            // scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            primaryStage.setTitle("Add Product");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            // Improved error message to help in debugging
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
