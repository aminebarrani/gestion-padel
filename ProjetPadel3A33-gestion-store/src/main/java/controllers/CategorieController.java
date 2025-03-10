package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.entities.Produit;
import tn.esprit.entities.categorie;
import tn.esprit.services.Categorieservices;
import tn.esprit.services.produitservices;
import tn.esprit.services.StripePayment;
import tn.esprit.services.ExchangeRateService;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CategorieController {

    @FXML private TableView<categorie> tableViewCategories;
    @FXML private TableColumn<categorie, String> colName;
    @FXML private TableColumn<categorie, String> colDescription;
    @FXML private Label categorieLabel;
    @FXML private GridPane produitGrid;
    @FXML private Button payButton;
    @FXML private TextField amountField;
    @FXML private ChoiceBox<String> fromCurrency;
    @FXML private ChoiceBox<String> toCurrency;
    @FXML private Label resultLabel;
    @FXML private TextField searchField;
    @FXML private ChoiceBox<String> priceFilter;
    private List<Produit> allProduits; // Store all products for filtering
    @FXML private Button homeButton;
    @FXML private Button exchangeButton;
    @FXML private Button categoriesButton;
    @FXML private Button productsButton;

    private Categorieservices categorieservices;
    private produitservices produitservices;
    private Produit selectedProduct;

    // ✅ Fix: Ensure a public no-argument constructor exists
    public CategorieController() {
        this.categorieservices = new Categorieservices();
        this.produitservices = new produitservices();
    }

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadCategories();

        tableViewCategories.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadProduits(newSelection);
            }
        });

        payButton.setDisable(true);
        fromCurrency.getItems().addAll("USD", "EUR", "GBP", "TND");
        toCurrency.getItems().addAll("USD", "EUR", "GBP", "TND");
        fromCurrency.setValue("USD");
        toCurrency.setValue("EUR");

        // Set up navigation buttons
        homeButton.setOnAction(e -> navigateTo("home.fxml"));
        exchangeButton.setOnAction(e -> navigateTo("exchange.fxml"));
        categoriesButton.setOnAction(e -> navigateTo("categories.fxml"));
        productsButton.setOnAction(e -> navigateTo("products.fxml"));

        // Disable search and filter until a category is selected
        searchField.setDisable(false);
        priceFilter.setDisable(false);
    }

    @FXML
    public void handleSearch() {
        if (allProduits == null) {
            showAlert("❌ No products loaded. Please select a category first.");
            return;
        }

        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            showAlert("❌ Please enter a search term.");
            return;
        }

        List<Produit> filteredProduits = allProduits.stream()
                .filter(produit -> produit.getDescriptionP().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displayProduits(filteredProduits);
    }

    @FXML
    public void handleClearSearch() {
        searchField.clear();
        displayProduits(allProduits); // Reset to show all products
    }

    @FXML
    public void handleFilter() {
        if (allProduits == null) {
            showAlert("❌ No products loaded. Please select a category first.");
            return;
        }

        String selectedFilter = priceFilter.getValue();
        if (selectedFilter == null || selectedFilter.equals("All")) {
            displayProduits(allProduits); // Show all products if no filter is selected
            return;
        }

        List<Produit> filteredProduits = allProduits.stream()
                .filter(produit -> {
                    switch (selectedFilter) {
                        case "< 50 €":
                            return produit.getPrix() < 50;
                        case "50 - 100 €":
                            return produit.getPrix() >= 50 && produit.getPrix() <= 100;
                        case "> 100 €":
                            return produit.getPrix() > 100;
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

        displayProduits(filteredProduits);
    }

    private void displayProduits(List<Produit> produits) {
        produitGrid.getChildren().clear();
        int row = 0, col = 0;
        for (Produit produit : produits) {
            VBox productBox = createProductBox(produit);
            produitGrid.add(productBox, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    private void loadCategories() {
        try {
            List<categorie> categories = categorieservices.readList();
            tableViewCategories.getItems().setAll(categories);
        } catch (SQLException e) {
            System.err.println("❌ Error loading categories: " + e.getMessage());
        }
    }

    private void loadProduits(categorie selectedCategory) {
        categorieLabel.setText("Produits de: " + selectedCategory.getNomCategorie());
        produitGrid.getChildren().clear();
        selectedProduct = null;
        payButton.setDisable(true);

        try {
            // Fetch products for the selected category
            allProduits = produitservices.getProduitsByCategorie(selectedCategory.getId()); // Initialize allProduits

            // Enable search and filter after products are loaded
            searchField.setDisable(false);
            priceFilter.setDisable(false);

            // Display products in a 2-column grid
            int row = 0, col = 0;
            for (Produit produit : allProduits) {
                VBox productBox = createProductBox(produit);
                produitGrid.add(productBox, col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error loading products: " + e.getMessage());
        }
    }

    private VBox createProductBox(Produit produit) {
        // Product Name
        Label nameLabel = new Label(produit.getDescriptionP());

        // Product Price
        Label priceLabel = new Label(produit.getPrix() + " €");

        // Image Handling
        ImageView productImageView = new ImageView();
        productImageView.setFitWidth(100); // Adjust width
        productImageView.setFitHeight(100); // Adjust height
        productImageView.setPreserveRatio(true);

        try {
            if (produit.getImage() != null && !produit.getImage().isEmpty()) {
                Image productImage = new Image(produit.getImage(), true);
                productImageView.setImage(productImage);
            } else {
                System.out.println("DEBUG: No image found for " + produit.getDescriptionP());
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error loading image: " + e.getMessage());
        }

        // VBox for Product Display
        VBox productBox = new VBox(5, productImageView, nameLabel, priceLabel);
        productBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 5px;");

        productBox.setOnMouseClicked(event -> {
            selectedProduct = produit;
            payButton.setDisable(false);
            showAlert("✅ Selected: " + produit.getDescriptionP());
        });

        return productBox;
    }

    @FXML
    public void handlePayment() {
        if (selectedProduct == null) {
            showAlert("❌ Please select a product to purchase.");
            return;
        }

        try {
            String checkoutUrl = StripePayment.createCheckoutSession(selectedProduct);
            Desktop.getDesktop().browse(new URI(checkoutUrl));
            showAlert("✅ Redirecting to payment...");
        } catch (Exception e) {
            showAlert("❌ Payment failed: " + e.getMessage());
        }
    }

    @FXML
    public void handleCurrencyConversion() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();

            double convertedAmount = ExchangeRateService.convertCurrency(amount, from, to);
            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, from, convertedAmount, to));
        } catch (Exception e) {
            resultLabel.setText("❌ Conversion failed: " + e.getMessage());
        }
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            homeButton.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("❌ Error loading page: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}