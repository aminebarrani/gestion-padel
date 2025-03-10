package tn.esprit.entities;

public class Produit {
    private int idP;
    private String descriptionP;
    private float prix;
    private int stock;
    private tn.esprit.entities.categorie categorie;
    private String image; // New field for storing image path or URL

    // Constructor when you only have the category ID
    public Produit(int idP, String descriptionP, float prix, int stock, int categorie_id, String image) {
        this.idP = idP;
        this.descriptionP = descriptionP;
        this.prix = prix;
        this.stock = stock;
        this.categorie = new tn.esprit.entities.categorie(categorie_id, ""); // You can adjust category name if needed
        this.image = image;
    }

    // Constructor with a complete categorie object
    public Produit(int idP, String descriptionP, float prix, int stock, tn.esprit.entities.categorie categorie, String image) {
        this.idP = idP;
        this.descriptionP = descriptionP;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
        this.image = image;
    }

    // Constructor without category ID, assuming a new category will be passed later
    public Produit(String descriptionP, float prix, int stock, tn.esprit.entities.categorie selectedCategory, String image) {
        this.descriptionP = descriptionP;
        this.prix = prix;
        this.stock = stock;
        this.categorie = selectedCategory;
        this.image = image;
    }

    // Getters and Setters
    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getDescriptionP() {
        return descriptionP;
    }

    public void setDescriptionP(String descriptionP) {
        this.descriptionP = descriptionP;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public tn.esprit.entities.categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(tn.esprit.entities.categorie categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "idP=" + idP +
                ", descriptionP='" + descriptionP + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", categorie=" + (categorie != null ? categorie.getNomCategorie() : "Aucune") +
                ", image='" + image + '\'' +
                '}';
    }
}
