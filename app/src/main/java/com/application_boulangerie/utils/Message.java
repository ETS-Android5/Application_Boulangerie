package com.application_boulangerie.utils;

public enum Message {
    PROGRESS {
        public String toString() {
            return "Please wait...";
        }
    }
    ,
    DIALOG_CONFIRMATION_SUPPRIMER {
        public String toString() {
            return "Vous voulez supprimer ";
        }
    },
    TOAST_DIALOG_BUTTON_SUPPRIMER {
        public String toString() {
            return "Vous avez supprimée ";
        }
    },
    TOAST_DIALOG_BOUTON_ANNULLER {
        public String toString() {
            return "Vous avez annullé cette demande";
        }
    },
    TOAST_DIALOG_BOUTON_MODIFIER {
        public String toString() {
            return "Vous avez choisi MODIFIER ";
        }
    },
    DIALOG_TITRE_LISTMP {
        public String toString() {
            return  "List des Matières premières" ;
        }
    },
    TOAST_DIALOG_MP {
        public String toString() {
            return  "C'est Matiere Premiere : " ;
        }
    },
    TOAST_DIALOG_BOUTON_FERMER {
        public String toString() {
            return  "Vous avez choisi fermer ce dialog" ;
        }
    },
    DIALOG_TITRE_LISTCATEGORIE {
        public String toString() {
            return  "Liste des Categories" ;
        }
    },
    DIALOG_CONFIRMATION_CATEGORIE {
        public String toString() {
            return  "Vous voulez MODIFIER cette categorie ou AFFICHER sa liste des produits?" ;
        }
    },
    DIALOG_TITRE_LISTUSERS {
        public String toString() {
            return  "Liste des Utilisateurs" ;
        }
    },
    TOAST_DIALOG_BUTTON_AFFICHER_PRODUIT {
        public String toString() {
            return "Vous avez choisi Afficher liste des Produits de cette categorie" ;
        }
    },
    DIALOG_MESSAGE_PAS_EN_STOCK {
        public String toString(){
            return " n'est plus en stock. A remplir ou supprimer, svp!";
        }
    },
    DIALOG_MESSAGE_PHASE_PAS_EN_STOCK {
        public String toString(){
            return  " qui n'est plus en stock. A remplir ou supprimer, svp!";
        }
    },
    DIALOG_MESSAGE_DECONNEXION {
        public String toString(){
            return  " Etes-vous sûr de vouloir vous déconnecter?";
        }
    },
    TOAST_DIALOG_BUTTON_OUI_DECONNEXION {
        public String toString(){
            return  " Vous etes déconnecté de cette application";
        }
    },
    TOAST_DIALOG_BUTON_NON_DECONNEXION {
        public String toString(){
            return  " Vous restez connecté!";
        }
    }

    ;



    @Override
    public abstract String toString();
}
