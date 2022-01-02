package com.application_boulangerie.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categorie  implements Serializable{

	private int categorie_id;
	private String categorie_nom;

	private List<Produit> produits = new ArrayList<>();


	// initialisation constructeur
	public Categorie() {
		this.categorie_nom = "inconnue";
	}
	//Constructor avec 1 parametres
	public Categorie( String categorie_nom) {
		this.categorie_nom = categorie_nom;
	}
	@Override
	public String toString() {
		return "Categorie id =" + categorie_id + ":  [" + categorie_nom + "]";
	}
	public String getCategorie_nom() {
		return categorie_nom;
	}
	public void setCategorie_nom(String categorie_nom) {
		this.categorie_nom = categorie_nom;
	}
	public int getCategorie_id() {
		return categorie_id;
	}
	public List<Produit> getProduits() {
		return produits;
	}
	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}


}
