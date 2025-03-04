package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mainvi extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✅ Ensure correct path to FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Gestion des Produits et Catégories");
            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("✅ Application started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
