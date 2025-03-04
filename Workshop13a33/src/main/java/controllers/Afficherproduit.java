package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Produit;
import tn.esprit.services.produitservices;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Afficherproduit {

    @FXML
    private ListView<Produit> listViewProducts;

    @FXML
    private TextField categoryIdField, idPField, descriptionPField, prixField, stockField;

    @FXML
    private Button updateProductButton, deleteProductButton, payButton;

    private final produitservices produitServices = new produitservices();

    // Set your Stripe API key here
    private static final String STRIPE_API_KEY = "sk_test_YourSecretKey";

    @FXML
    public void initialize() {
        listViewProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillUpdateFields(newSelection);
            }
        });
        updateProductButton.setDisable(true);
    }

    @FXML
    public void loadProductsByCategory(ActionEvent event) {
        try {
            int categoryId = Integer.parseInt(categoryIdField.getText().trim());
            loadProductsForCategory(categoryId);
        } catch (NumberFormatException e) {
            showAlert("Invalid category ID format. Please enter a valid number.");
        }
    }

    private void fillUpdateFields(Produit product) {
        if (product != null) {
            idPField.setText(String.valueOf(product.getIdP()));
            descriptionPField.setText(product.getDescriptionP());
            prixField.setText(String.valueOf(product.getPrix()));
            stockField.setText(String.valueOf(product.getStock()));
            updateProductButton.setDisable(false);
        }
    }

    private void loadProductsForCategory(int categoryId) {
        try {
            List<Produit> produits = produitServices.getProduitsByCategorie(categoryId);
            ObservableList<Produit> produitsObservable = FXCollections.observableList(produits);
            listViewProducts.setItems(produitsObservable);
        } catch (SQLException e) {
            showAlert("Error fetching products: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        Produit selectedProduct = listViewProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                if (descriptionPField.getText().trim().isEmpty() || prixField.getText().trim().isEmpty() || stockField.getText().trim().isEmpty()) {
                    showAlert("All fields must be filled out.");
                    return;
                }

                selectedProduct.setDescriptionP(descriptionPField.getText());
                selectedProduct.setPrix(Float.parseFloat(prixField.getText()));
                selectedProduct.setStock(Integer.parseInt(stockField.getText()));

                produitServices.update(selectedProduct);
                loadProductsForCategory(Integer.parseInt(categoryIdField.getText().trim()));
                showAlert("Product updated successfully!");
                clearFields();

            } catch (NumberFormatException e) {
                showAlert("Invalid input format. Please enter valid numbers for price and stock.");
            } catch (SQLException e) {
                showAlert("Error updating product: " + e.getMessage());
            }
        } else {
            showAlert("Please select a product to update.");
        }
    }

    @FXML
    public void handleDelete() {
        Produit selectedProduct = listViewProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                produitServices.delete(selectedProduct.getIdP());
                loadProductsForCategory(Integer.parseInt(categoryIdField.getText().trim()));
                showAlert("Product deleted successfully!");
                clearFields();

            } catch (SQLException e) {
                showAlert("Error deleting product: " + e.getMessage());
            }
        } else {
            showAlert("Please select a product to delete.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        idPField.clear();
        descriptionPField.clear();
        prixField.clear();
        stockField.clear();
        updateProductButton.setDisable(true);
    }

    @FXML
    private void goToAddCategory() throws IOException {
        switchScene("/ajoutercategorie.fxml");
    }

    @FXML
    private void goToAddProduct() throws IOException {
        switchScene("/ajouterproduit.fxml");
    }

    @FXML
    private void goToCategoryList() throws IOException {
        switchScene("/affichercategorie.fxml");
    }

    private void switchScene(String fxmlFile) throws IOException {
        Stage stage = (Stage) listViewProducts.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Handle Stripe Payment

}
