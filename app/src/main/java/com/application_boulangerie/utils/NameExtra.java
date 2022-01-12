package com.application_boulangerie.utils;

public enum NameExtra {
    MP {
        public String toString() {
            return "matière première";
        }
    },
    PRODUIT {
        public String toString() {
            return "produit";
        }
    },
    CATEGORIE {
        public String toString() {
            return "categorie";
        }
    },
    INGREDIENT {
        public String toString() {
            return "ingredient";
        }
    },
    UTILISATEUR {
        public String toString() {
            return "user";
        }
    },
    UTILISATEUR_ID {
        public String toString() {
            return "user_id";
        }
    },
    MP_ID {
        public String toString() {
            return "mp_id";
        }
    },
    PRODUIT_ID {
        public String toString() {
            return "produit_id";
        }
    },
    CATEGORIE_ID {
        public String toString() {
            return "categorie_id";
        }
    },
    INGREDIENT_ID {
        public String toString() {
            return "ingredient_id";
        }
    };

    @Override
    public abstract String toString();
}
