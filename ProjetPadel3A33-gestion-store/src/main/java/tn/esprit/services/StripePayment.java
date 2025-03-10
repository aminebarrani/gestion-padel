package tn.esprit.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import tn.esprit.entities.Produit;

public class StripePayment {

    private static final String STRIPE_API_KEY = "sk_test_51QxIsEEK3b089kCHcH9m01KLAiBPYYEfuhuD8QPbAM1TV49YOttDHFSGXXHxRu9Z9A5ywRgC9W1tXM4z6mnTm3X200Mexauw53";

    public static String createCheckoutSession(Produit produit) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        long amountInCents = (long) (produit.getPrix() * 100); // Convert price to cents

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://yourwebsite.com/success")
                .setCancelUrl("https://yourwebsite.com/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(produit.getDescriptionP())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
