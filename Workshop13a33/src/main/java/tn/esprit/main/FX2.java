package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FX2 extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichercategorie.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Category Management");
            primaryStage.setScene(scene);


            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
