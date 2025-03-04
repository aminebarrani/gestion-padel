package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Produit;
import tn.esprit.entities.categorie;
import tn.esprit.services.produitservices;
import tn.esprit.services.Categorieservices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterProduitController {

    @FXML
    private TextField descriptionPField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField stockField;

    @FXML
    private ComboBox<categorie> categorieComboBox;

    @FXML
    private ImageView productImageView;

    private String imagePath = ""; // Stocke le chemin de l'image sélectionnée

    private final produitservices produitService = new produitservices();
    private final Categorieservices categorieService = new Categorieservices();
    @FXML
    public void initialize() {
        loadCategories(); // Load categories when the form opens
    }

    private void loadCategories() {
        try {
            // Fetch categories from the database
            List<categorie> categories = categorieService.readList();

            // Populate the ComboBox
            categorieComboBox.getItems().setAll(categories);
        } catch (SQLException e) {
            showAlert("❌ Erreur", "Erreur lors du chargement des catégories: " + e.getMessage());
        }
    }

    @FXML
    void handleAddProduct(ActionEvent event) {
        try {
            // Récupérer les valeurs saisies
            String descriptionP = descriptionPField.getText().trim();
            String prixText = prixField.getText().trim();
            String stockText = stockField.getText().trim();
            categorie selectedCategory = categorieComboBox.getValue();

            // Validation des champs
            if (descriptionP.isEmpty() || prixText.isEmpty() || stockText.isEmpty() || selectedCategory == null) {
                showAlert("⚠ Validation Error", "Veuillez remplir tous les champs !");
                return;
            }

            // Conversion sécurisée des entrées
            float prix;
            int stock;
            try {
                prix = Float.parseFloat(prixText);
                stock = Integer.parseInt(stockText);
            } catch (NumberFormatException e) {
                showAlert("⚠ Erreur de saisie", "Veuillez entrer des valeurs valides pour le prix et le stock !");
                return;
            }

            if (imagePath.isEmpty()) {
                showAlert("⚠ Erreur de validation", "Veuillez sélectionner une image pour le produit !");
                return;
            }

            // Création et ajout du produit
            Produit produit = new Produit(0, descriptionP, prix, stock, selectedCategory, imagePath);
            produitService.add(produit);

            showAlert("✅ Succès", "Produit ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            showAlert("❌ Erreur base de données", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    void handleImageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString(); // Convertir en format URI pour l'ImageView
            productImageView.setImage(new Image(imagePath));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        descriptionPField.clear();
        prixField.clear();
        stockField.clear();
        categorieComboBox.getSelectionModel().clearSelection();
        productImageView.setImage(null);
        imagePath = "";
    }
    // Navigation Methods
    @FXML
    private void goToCategoryList() throws IOException {
        switchScene("/affichercategorie.fxml");
    }

    @FXML
    private void goToAddCategory() throws IOException {
        switchScene("/ajoutercategorie.fxml");
    }

    @FXML
    private void goToProductList() throws IOException {
        switchScene("/afficherproduit.fxml");
    }

    private void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if (root == null) {
            throw new IOException("FXML file not found: " + fxmlFile);
        }

        Stage stage = (Stage) descriptionPField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
