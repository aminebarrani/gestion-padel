package tn.esprit.services;

import tn.esprit.entities.Produit;
import tn.esprit.entities.categorie;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categorieservices implements IService<categorie> {

    private final Connection con;

    public Categorieservices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(categorie categorie) throws SQLException {
        String query = "INSERT INTO categorie (nomCategorie, description) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, categorie.getNomCategorie());
            ps.setString(2, categorie.getDescription());
            ps.executeUpdate();
            System.out.println("Catégorie ajoutée avec succès !");
        }
    }
    public static categorie getCategorieById(int categoryId) {
        categorie category = null;
        String query = "SELECT * FROM categorie WHERE id = ?";
        try (Connection con = MyDatabase.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new categorie(rs.getInt("id"), rs.getString("nomCategorie"));
                } else {
                    System.out.println("Category not found with ID: " + categoryId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching category: " + e.getMessage());
        }
        return category;
    }

    @Override
    public List<categorie> readList() throws SQLException {
        String query = "SELECT * FROM categorie ORDER BY nomCategorie ASC"; // Sorting alphabetically
        List<categorie> categories = new ArrayList<>();

        try (Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                categorie categorie = new categorie(
                        rs.getInt("id"),
                        rs.getString("nomCategorie"),
                        rs.getString("description")
                );
                categories.add(categorie);
            }
        }
        return categories;
    }


    @Override
    public void update(categorie c) throws SQLException {
        // Ensure your connection is properly established
        String req = "UPDATE categorie SET nomCategorie = ?, description = ? WHERE id = ?";

        // Using try-with-resources to ensure the PreparedStatement is closed after execution
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, c.getNomCategorie()); // Setting the name for update
            ps.setString(2, c.getDescription());  // Setting the description for update
            ps.setInt(3, c.getId());              // Setting the id of the category to update

            // Execute the update statement
            ps.executeUpdate();
        } catch (SQLException e) {
            // You can log the error or rethrow it as needed
            throw new SQLException("Error updating category: " + e.getMessage(), e);
        }
    }


    public void delete(int id) throws SQLException {
        String query = "DELETE FROM categorie WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Catégorie supprimée avec succès !");
            } else {
                System.out.println("Aucune catégorie trouvée avec cet ID !");
            }
        }
    }

    @Override
    public void add(Produit produit) throws SQLException {

    }

    @Override
    public List<Produit> getProduitsByCategorie(int categorieId) throws SQLException {
        return null;
    }
}
