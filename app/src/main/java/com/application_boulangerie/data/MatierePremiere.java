package com.application_boulangerie.data;

import java.io.Serializable;
import java.util.List;

public class MatierePremiere  implements Serializable{

	private int mp_id;
	private String mp_nom;
	private Double mp_prix;
	private int mp_quantite;
	private String mp_unite;
	private String mp_marque;

		 private List<Produit> listeProduits;


	@Override
	public String toString() {
		return "MatierePremiere [mp_id=" + mp_id + ", mp_nom=" + mp_nom + ", mp_prix=" + mp_prix + ", mp_quantite="
				+ mp_quantite + ", mp_unite=" + mp_unite + ", mp_marque=" + mp_marque + "]";
	}
	//Constructor avec 6 parametres
	public MatierePremiere(int mp_id, String mp_nom, Double mp_prix, int mp_quantite, String mp_unite, String mp_marque) {
		this.mp_id = mp_id;
		this.mp_nom = mp_nom;
		this.mp_prix = mp_prix;
		this.mp_quantite = mp_quantite;
		this.mp_unite = mp_unite;
		this.mp_marque = mp_marque;
	}

	//Constructor avec 5 parametres
	public MatierePremiere(String mp_nom, Double mp_prix, int mp_quantite, String mp_unite, String mp_marque) {
		this.mp_nom = mp_nom;
		this.mp_prix = mp_prix;
		this.mp_quantite = mp_quantite;
		this.mp_unite = mp_unite;
		this.mp_marque = mp_marque;
	}


	public String getMp_nom() {
		return mp_nom;
	}

	public void setMp_nom(String mp_nom) {
		this.mp_nom = mp_nom;
	}

	public int getMp_quantite() {
		return mp_quantite;
	}

	public void setMp_quantite(int mp_quantite) {
		this.mp_quantite = mp_quantite;
	}

	public String getMp_unite() {
		return mp_unite;
	}

	public void setMp_unite(String mp_unite) {
		this.mp_unite = mp_unite;
	}

	public String getMp_marque() {
		return mp_marque;
	}

	public void setMp_marque(String mp_marque) {
		this.mp_marque = mp_marque;
	}

	
	
	public Double getMp_prix() {
		return mp_prix;
	}


	public void setMp_prix(Double mp_prix) {
		this.mp_prix = mp_prix;
	}


	public int getMp_id() {
		return mp_id;
	}


	public List<Produit> getListeProduits() {
		return listeProduits;
	}


	public void setListeProduits(List<Produit> listeProduits) {
		this.listeProduits = listeProduits;
	}
	
	
	
	
	

}
