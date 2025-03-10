package tn.esprit.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Produit;
import tn.esprit.services.StripePayment;

import java.awt.*;
import java.net.URI;

public class StripePaymentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label statusLabel = new Label("Click to pay with Stripe");
        Button payButton = new Button("Pay Now");

        Produit sampleProduct = new Produit("Sample Product", 20.00f, 10, null, ""); // Fixed constructor

        payButton.setOnAction(event -> {
            try {
                String checkoutUrl = StripePayment.createCheckoutSession(sampleProduct);
                Desktop.getDesktop().browse(new URI(checkoutUrl)); // Open browser for payment
                statusLabel.setText("Redirecting to payment...");
            } catch (Exception e) {
                statusLabel.setText("Payment failed: " + e.getMessage());
            }
        });

        VBox root = new VBox(10, statusLabel, payButton);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Stripe Payment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
