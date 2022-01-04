package com.application_boulangerie.data;

import java.io.Serializable;


public class Ingredient  implements Serializable {

	private int ingredient_id;
	private int produit_id;
	private int mp_id;
	private int ingredient_quantite;
	private String ingredient_unite;
	
	@Override
	public String toString() {
		return "ingredient [produit: " + produit_id + ", mp:" + mp_id + " - "
				+ ingredient_quantite + ingredient_unite + "]";
	}

	// Contructeur avec 5 paramettres qui compris ingredient_id
	public Ingredient(int ingredient_id, int produit_id, int mp_id, int ingredient_quantite, String ingredient_unite) {
		this.ingredient_id = ingredient_id;
		this.produit_id = produit_id;
		this.mp_id = mp_id;
		this.ingredient_quantite = ingredient_quantite;
		this.ingredient_unite = ingredient_unite;
	}

	// Contructeur avec 4 paramettres qui n'a pas d'ingredient_id
	public Ingredient(int produit_id, int mp_id, int ingredient_quantite, String ingredient_unite) {
		this.produit_id = produit_id;
		this.mp_id = mp_id;
		this.ingredient_quantite = ingredient_quantite;
		this.ingredient_unite = ingredient_unite;
	}

	
	
	public int getIngredient_id() {
		return ingredient_id;
	}

	public int getProduit_id() {
		return produit_id;
	}

	public void setProduit_id(int produit_id) {
		this.produit_id = produit_id;
	}

	public int getMp_id() {
		return mp_id;
	}

	public void setMp_id(int mp_id) {
		this.mp_id = mp_id;
	}

	public int getIngredient_quantite() {
		return ingredient_quantite;
	}

	public void setIngredient_quantite(int ingredient_quantite) {this.ingredient_quantite = ingredient_quantite; }

	public String getIngredient_unite() {
		return ingredient_unite;
	}

	public void setIngredient_unite(String ingredient_unite) { this.ingredient_unite = ingredient_unite; }


}
