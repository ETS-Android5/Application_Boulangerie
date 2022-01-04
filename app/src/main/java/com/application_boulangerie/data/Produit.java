package com.application_boulangerie.data;

import java.io.Serializable;
import java.util.List;

public class Produit implements Serializable{


	private int produit_id;
	private String produit_nom; 
	private double produit_prix; 
	private int produit_quantite;

		private List<MatierePremiere> listeMPs;
		private Categorie produitCategorie;

	//Constructor avec 3 parametres
	public Produit(String produit_nom, double produit_prix, int produit_quantite) {
		this.produit_nom = produit_nom;
		this.produit_prix = produit_prix;
		this.produit_quantite = produit_quantite;
	}


	//Constructor avec 4 parametres
	public Produit(int produit_id, String produit_nom, double produit_prix, int produit_quantite) {
		this.produit_id = produit_id;
		this.produit_nom = produit_nom;
		this.produit_prix = produit_prix;
		this.produit_quantite = produit_quantite;
	}

	@Override
	public String toString() {
		return "Produit [produit_id=" + produit_id + ", produit_nom=" + produit_nom + ", produit_prix=" + produit_prix
				+ ", produit_quantite=" + produit_quantite +"]";
	}

	public String 	getProduit_nom() 							{return produit_nom;}

	public void 	setProduit_nom(String produit_nom) 			{this.produit_nom = produit_nom;}
	
	public double 	getProduit_prix() 							{return produit_prix;}

	public void 	setProduit_prix(double produit_prix)		{this.produit_prix = produit_prix;}

	public int 		getProduit_quantite() 						{return produit_quantite;}

	public void 	setProduit_quantite(int produit_quantite) 	{this.produit_quantite = produit_quantite;}

	public int 		getProduit_id() 							{return produit_id;}

	public List<MatierePremiere> 	getListeMPs() 								{return listeMPs;}

	public void 					setListeMPs(List<MatierePremiere> listeMPs) {this.listeMPs = listeMPs;}

	public Categorie 	getProduitCategorie() 							{return produitCategorie;}

	public void 		setProduitCategorie(Categorie produitCategorie) {this.produitCategorie = produitCategorie;}
	
	

}
