package tn.esprit.services;

import tn.esprit.entities.Produit;
import tn.esprit.entities.categorie;
import tn.esprit.utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class produitservices {

    private Connection con;

    public produitservices() {
        // Get the connection from MyDatabase
        con = MyDatabase.getInstance().getCon();
    }
    public boolean doesCategoryExist(int categorie_id) throws SQLException {
        ensureConnectionIsOpen(); // Ensure connection is open
        String query = "SELECT COUNT(*) FROM categorie WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, categorie_id);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }


    private void ensureConnectionIsOpen() throws SQLException {
        if (con == null || con.isClosed()) {
            System.err.println("❌ Connection is closed or NULL. Reconnecting...");
            con = MyDatabase.getInstance().getCon();
        }
    }


    public void add(Produit produit) throws SQLException {
        ensureConnectionIsOpen(); // Ensure the connection is open

        if (produit.getCategorie() == null) {
            System.err.println("❌ Error: produit.getCategorie() is NULL. Cannot insert product.");
            return;
        }

        String query = "INSERT INTO produit (descriptionP, prix, stock, categorie_id, image) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, produit.getDescriptionP());
            ps.setFloat(2, produit.getPrix());
            ps.setInt(3, produit.getStock());
            ps.setInt(4, produit.getCategorie().getId());
            ps.setString(5, produit.getImage());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Produit ajouté avec succès !");
            } else {
                System.out.println("⚠️ Aucune ligne insérée.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding product: " + e.getMessage());
            throw e;
        }
    }



    public List<Produit> getProduitsByCategorie(int categorieId) throws SQLException {
        ensureConnectionIsOpen();

        String query = "SELECT * FROM produit WHERE categorie_id = ?";
        List<Produit> produits = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, categorieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produit produit = new Produit(
                            rs.getInt("idP"),
                            rs.getString("descriptionP"),
                            rs.getFloat("prix"),
                            rs.getInt("stock"),
                            new categorie(categorieId, ""),
                            rs.getString("image") // Retrieve the image from the database
                    );
                    produits.add(produit);
                }
            }
        }
        return produits;
    }

    public void update(Produit produit) throws SQLException {
        ensureConnectionIsOpen();

        if (produit.getCategorie() == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être nulle.");
        }

        String query = "UPDATE produit SET descriptionP = ?, prix = ?, stock = ?, categorie_id = ?, image = ? WHERE idP = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, produit.getDescriptionP());
            ps.setFloat(2, produit.getPrix());
            ps.setInt(3, produit.getStock());
            ps.setInt(4, produit.getCategorie().getId());
            ps.setString(5, produit.getImage());
            ps.setInt(6, produit.getIdP());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Produit mis à jour avec succès !");
            } else {
                System.out.println("Aucun produit trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            throw e;
        }
    }

    public void delete(int productId) throws SQLException {
        ensureConnectionIsOpen();

        String query = "DELETE FROM produit WHERE idP = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
            System.out.println("Product deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            throw e;
        }
    }
}
