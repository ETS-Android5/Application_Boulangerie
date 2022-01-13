package com.application_boulangerie.utils;

public enum MyURL {
    TITLE {
        public String toString() {
            return "http://192.168.43.182:8080/boulangerie/webapi";
        }
    },
    AJOUTEPRODUIT {
        public String toString() {
            return "/produit/insertionProduit/";
        }
    },
    MODIFIERPRODUIT {
        public String toString() {
            return "/produit/updateProduit/";
        }
    },
    LISTPRODUIT {
        public String toString() {
            return "/produit/getAllProduits";
        }
    },
    DELETEPRODUIT {
        public String toString() {
            return "/produit/deleteProduit/";
        }
    },
    FINDPRODUITID {
        public String toString() {
            return "/produit/findbyIDProduit/";
        }
    },
    FINDLISTPRODUITCATEGORIE {
        public String toString() {
            return "/produit/findAllProduitsbyIDCategorie/";
        }
    },
    AJOUTECATEGORIE {
        public String toString() {
            return "/categorie/insertionCategorie";
        }
    },
    MODIFIERCATEGORIE {
        public String toString() {
            return "/categorie/updateCategorie/";
        }
    },
    LISTCATEGORIE {
        public String toString() {
            return "/categorie/getAllCategories";
        }
    },
    DELETECATEGORIE {
        public String toString() {
            return "/categorie/deleteCategorie/";
        }
    },
    FINDCATEGORIEBYID {
        public String toString() {
            return "/categorie/findbyIDCategorie/";
        }
    },
    FINDCATEGORIENAME {
        public String toString() {
            return "/categorie/findbyNameCategorie/";
        }
    },
    AJOUTEMP {
        public String toString() {
            return "/matierepremiere/insertionMP";
        }
    },
    MODIFIERMP {
        public String toString() {
            return "/matierepremiere/updateMP/";
        }
    },
    LISTMP {
        public String toString() {
            return "/matierepremiere/getAllMPs";
        }
    },
    DELETEMP {
        public String toString() {
            return "/matierepremiere/deleteMP/";
        }
    },
    FINDMPBYID {
        public String toString() {
            return "/matierepremiere/findbyIDMP/";
        }
    },
    AJOUTEINGREDIENT {
        public String toString() {
            return "/ingredient/insertionIngredient";
        }
    },
    MODIFIERINGREDIENT {
        public String toString() {
            return "/ingredient/updateIngredient/";
        }
    },
    LISTINGREDIENT {
        public String toString() {
            return "/ingredient/getAllIngredients";
        }
    },
    DELETEINGREDIENT {
        public String toString() {
            return "/ingredient/deleteIngredient/";
        }
    },
    FINDINGREDIENTBYID {
        public String toString() {
            return "/ingredient/findbyID/";
        }
    },
    LISTINGREDIENTBYPRODUIT {
        public String toString() {
            return "/ingredient/findbyProduitID/";
        }
    },
    FINDUSERBYID {
        public String toString() {
            return "/user/findbyIDUtilisateur/";
        }
    },
    LISTUSERS {
        public String toString() {
            return "/user/getAllUtilisateurs";
        }
    },
    DELETEUSERS {
        public String toString() {
            return "/user/deleteUtilisateur/";
        }
    },
    MODIFIERUSERS {
        public String toString() {
            return "/user/updateUtilisateur/";
        }
    },
    AJOUTEUSER {
        public String toString() {
            return "/user/insertionUtilisateur";
        }
    }
    ;

        @Override
        public abstract String toString() ;
    }

