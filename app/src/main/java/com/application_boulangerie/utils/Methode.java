package com.application_boulangerie.utils;

public enum Methode {
    GET, POST, DELETE;

    @Override
    public String toString() {
        if (this == GET) {
            return "GET";
        } else if (this == POST) {
            return "POST";
        } else if (this == DELETE) {
            return "DELETE";
        }
        return super.toString();
    }
}
