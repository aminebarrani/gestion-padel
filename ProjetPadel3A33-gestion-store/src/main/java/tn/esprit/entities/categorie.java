package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;

public class categorie {
    private int id;
    private String nomCategorie;
    private String description;
    private List<Produit> produits;

    public categorie() {
        this.produits = new ArrayList<>();
    }

    public categorie(int id, String nomCategorie, String description) {
        this.id = id;
        this.nomCategorie = nomCategorie;
        this.description = description;
        this.produits = new ArrayList<>();
    }

    public categorie(int id, String nomCategorie) {
        this.id = id;
        this.nomCategorie = nomCategorie;
        this.produits = new ArrayList<>(); // Initialize the produits list
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
        produit.setCategorie(this);
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nomCategorie='" + nomCategorie + '\'' +
                ", description='" + description + '\'' +
                ", produits=" + produits +
                '}';
    }
}
