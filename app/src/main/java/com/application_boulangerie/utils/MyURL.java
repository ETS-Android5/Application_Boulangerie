package com.application_boulangerie.utils;

public enum MyURL {
    TITLE, AJOUTEPRODUIT, MODIFIERPRODUIT, LISTPRODUIT, DELETEPRODUIT, FINDPRODUITID,
    AJOUTECATEGORIE, MODIFIERCATEGORIE, LISTCATEGORIE, DELETECATEGORIE, FINDCATEGORIENAME,
    AJOUTEMP, MODIFIERMP, LISTMP, DELETEMP,
    AJOUTEINGREDIENT, MODIFIERINGREDIENT, LISTINGREDIENT, DELETEINGREDIENT, LISTINGREDIENTBYPRODUIT;

        @Override
        public String toString() {
            if (this == TITLE) {
                return "http://192.168.43.148:8080/gestion_boulangerie/webapi";

            } else if (this == AJOUTEPRODUIT) {
                return "/produit/insertionProduit/";

            }else if (this == MODIFIERPRODUIT) {
                return "/produit/updateProduit/";

            }else if (this == DELETEPRODUIT) {
                return "/produit/deleteProduit/";
            }
            else if (this == LISTPRODUIT) {
                return "/produit/getAllProduits";
            }
            else if (this == FINDPRODUITID) {
                return "/produit/findbyIDProduit/";
            }
            else if (this == AJOUTECATEGORIE) {
                return "/categorie/insertionCategorie";
            }
            else if (this == MODIFIERCATEGORIE) {
                return "/categorie/updateCategorie/";
            }
            else if (this == DELETECATEGORIE) {
                return "/categorie/deleteCategorie/";

            }else if (this == LISTCATEGORIE) {
                return "/categorie/getAllCategories";
            }
            else if (this == FINDCATEGORIENAME) {
                return "/categorie/findbyNameCategorie/";
            }
            else if (this == AJOUTEINGREDIENT) {
                return "/ingredient/insertionIngredient";
            }
            else if (this == MODIFIERINGREDIENT) {
                return "/ingredient/updateIngredient/";
            }
            else if (this == DELETEINGREDIENT) {
                return "/ingredient/deleteIngredient/";
            }
            else if (this == LISTINGREDIENT) {
                return "/ingredient/getAllIngredients";
            }
            else if (this == LISTINGREDIENTBYPRODUIT) {
                return "/ingredient/findbyProduitID/";
            }
            else if (this == AJOUTEMP) {
                return "/matierepremiere/insertionMP";
            }
            else if (this == MODIFIERMP) {
                return "/matierepremiere/updateMP/";
            }
            else if (this == DELETEMP) {
                return "/matierepremiere/deleteMP/";
            }
            else if (this == LISTMP) {
                return "/matierepremiere/getAllMPs";
            }

            return super.toString();
    }

}
