package com.application_boulangerie.data;

public class Produit {

	private int produit_id;
	private String produit_nom; 
	private double produit_prix; 
	private int produit_quantite;
	private String produit_description_ingredients; 
	
	
	// initialisation constructeur
	public Produit() {
		this.produit_id = 100;
		this.produit_nom = "inconnue";
		this.produit_prix = 0.0;
		this.produit_quantite = 0;
		this.produit_description_ingredients = "inconnue" ;
		
	}



	//Constructor avec 5 parametres
	public Produit(int produit_id, String produit_nom, double produit_prix, int produit_quantite, String produit_ingredients) {
		this.produit_id = produit_id;
		this.produit_nom = produit_nom;
		this.produit_prix = produit_prix;
		this.produit_quantite = produit_quantite;
		this.produit_description_ingredients = produit_ingredients;

	}

	//Constructor avec 4 parametres
	public Produit(String produit_nom, double produit_prix, int produit_quantite, String produit_ingredients) {
		this.produit_nom = produit_nom;
		this.produit_prix = produit_prix;
		this.produit_quantite = produit_quantite;
		this.produit_description_ingredients = produit_ingredients;

	}

	@Override
	public String toString() {
		return "Produit [produit_id=" + produit_id + ", produit_nom=" + produit_nom + ", produit_prix=" + produit_prix
				+ ", produit_quantite=" + produit_quantite + ", produit_description_ingredients=" + produit_description_ingredients
				+  "]";
	}

	public String 	getProduit_nom() 							{return produit_nom;}

	public void 	setProduit_nom(String produit_nom) 			{this.produit_nom = produit_nom;}
	
	public double 	getProduit_prix() 							{return produit_prix;}

	public void 	setProduit_prix(double produit_prix)		{this.produit_prix = produit_prix;}

	public int 		getProduit_quantite() 						{return produit_quantite;}

	public void 	setProduit_quantite(int produit_quantite) 	{this.produit_quantite = produit_quantite;}

	public String 	getProduit_description_ingredients() 		{return produit_description_ingredients;}

	public void 	setProduit_description_ingredients(String produit_description_ingredients) {this.produit_description_ingredients = produit_description_ingredients;}

	public int 		getProduit_id() 							{return produit_id;}

	public void 	setProduit_id(int produit_id) 				{this.produit_id = produit_id;}
}
