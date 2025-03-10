package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.categorie;
import tn.esprit.services.Categorieservices;

import java.io.IOException;
import java.sql.SQLException;

public class ajoutercategorie {

    @FXML
    private TextField nomCategorie;

    @FXML
    private TextField description;

    private Categorieservices cs = new Categorieservices();

    @FXML
    void save(ActionEvent event) {
        try {
            String nom = nomCategorie.getText().trim();
            String desc = description.getText().trim();

            if (nom.isEmpty() || desc.isEmpty()) {
                System.out.println("⚠ Veuillez remplir tous les champs !");
                return;
            }

            categorie newCategorie = new categorie(0, nom, desc);
            cs.add(newCategorie);
            System.out.println(" Catégorie ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de l'ajout : " + e.getMessage());
        }
    }
    private void switchScene(String fxmlFile) throws IOException {
        Stage stage = (Stage) nomCategorie.getScene().getWindow(); // Get current window
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile)); // Load new scene
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Go to Category List
    @FXML
    private void goToCategoryList() throws IOException {
        switchScene("/affichercategorie.fxml");
    }

    // Go to Add Product
    @FXML
    private void goToAddProduct() throws IOException {
        switchScene("/ajouterProduit.fxml");
    }

    // Go to Product List
    @FXML
    private void goToProductList() throws IOException {
        switchScene("/afficherproduit.fxml");
    }

}
