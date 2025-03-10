package tn.esprit.services;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeRateService {
    private static final String API_KEY = "bec1acf52b03b01c29b01cef"; // Replace with your API key
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        String urlString = BASE_URL + API_KEY + "/latest/" + fromCurrency;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONObject("conversion_rates").getDouble(toCurrency);
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) throws Exception {
        double rate = getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }
}
