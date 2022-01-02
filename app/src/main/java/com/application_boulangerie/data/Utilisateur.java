package com.application_boulangerie.data;

import java.io.Serializable;

public class Utilisateur  implements Serializable{

	private int user_id;
	private String user_nom, user_password;

	// initialisation constructeur
	public Utilisateur() {
		this.user_nom = "inconnue";
		this.user_password = "inconnue";
	}
	
	//Constructor avec 2 parametres
	public Utilisateur(String user_nom, String user_password) {
		this.user_nom = user_nom;
		this.user_password = user_password;
	}
	@Override
	public String toString() {
		return "Users [user_id=" + user_id + ", user_nom=" + user_nom + ", user_password=" + user_password + "]";
	}
	
	
	public String getUser_nom() {
		return user_nom;
	}
	public void setUser_nom(String user_nom) {
		this.user_nom = user_nom;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public int getUser_id() {
		return user_id;
	}
	
	
	
}
