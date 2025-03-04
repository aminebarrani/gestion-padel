package tn.esprit.services;

import tn.esprit.entities.Produit;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    // Ajouter une entrée dans la base de données
    void add(T t) throws SQLException;

    List<T> readList() throws SQLException;
    void update(T t) throws SQLException;
    public void delete(int id) throws SQLException;
    public void add(Produit produit) throws SQLException;
    public List<Produit> getProduitsByCategorie(int categorieId) throws SQLException;
    }
