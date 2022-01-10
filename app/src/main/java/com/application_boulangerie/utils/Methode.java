package com.application_boulangerie.utils;

public enum Methode {
    GET {
        public String toString() {
            return  "GET" ;
        }
    },
    POST {
        public String toString() {
            return  "POST" ;
        }
    },
    DELETE {
        public String toString() {
            return  "DELETE" ;
        }
    };

    @Override
    public abstract String toString();
}
