package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.services.Categorieservices;
import tn.esprit.entities.categorie;

import java.io.IOException;
import java.sql.SQLException;

public class affichercategorie {

    @FXML
    private TableView<categorie> tableViewCategories;

    @FXML
    private TableColumn<categorie, String> colName;

    @FXML
    private TableColumn<categorie, String> colDescription;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDescription;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    private Categorieservices categorieService;
    private ObservableList<categorie> categories;

    public void initialize() {
        categorieService = new Categorieservices();

        colName.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadCategories();
    }

    private void loadCategories() {
        try {
            categories = FXCollections.observableArrayList(categorieService.readList());
            tableViewCategories.setItems(categories);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        categorie selected = tableViewCategories.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String newName = txtName.getText();
            String newDescription = txtDescription.getText();

            if (newName != null && !newName.trim().isEmpty() && newDescription != null && !newDescription.trim().isEmpty()) {
                try {
                    selected.setNomCategorie(newName);
                    selected.setDescription(newDescription);
                    categorieService.update(selected);
                    loadCategories();
                    clearFields();
                } catch (SQLException e) {
                    showAlert("Error", "Failed to update category: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Please provide both name and description.");
            }
        } else {
            showAlert("Error", "Please select a category to update.");
        }
    }

    @FXML
    private void handleDelete() {
        categorie selected = tableViewCategories.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                categorieService.delete(selected.getId());
                loadCategories();
                clearFields();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete category: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a category to delete.");
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
        txtName.clear();
        txtDescription.clear();
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
    private void goToProductList() throws IOException {
        switchScene("/afficherproduit.fxml");
    }

    private void switchScene(String fxmlFile) throws IOException {
        Stage stage = (Stage) tableViewCategories.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }
}

