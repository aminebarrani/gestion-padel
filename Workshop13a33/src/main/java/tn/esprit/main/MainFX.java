package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainFX extends Application {  // Ensure this class is public!
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutercategorie.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Ajouter Catégorie");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(" Erreur lors du chargement de l'interface : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
