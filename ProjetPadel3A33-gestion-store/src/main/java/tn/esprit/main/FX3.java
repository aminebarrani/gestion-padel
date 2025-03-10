package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;  // Add the import for IOException

public class FX3 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherproduit.fxml")); // Ensure the path is correct
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("FX3 Application");
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
