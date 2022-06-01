package com.example.bibliotecaapp.AdaptadorRV;

public class Registro {

    private String NombreUser;
    private String Pseudonimo;
    private String Contrasenap;

    public String getNombreUser() {
        return NombreUser;
    }

    public void setNombreUser(String nombreUser) {
        NombreUser = nombreUser;
    }

    public String getPseudonimo() {
        return Pseudonimo;
    }

    public void setPseudonimo(String pseudonimo) {
        Pseudonimo = pseudonimo;
    }

    public String getContrasenap() {
        return Contrasenap;
    }

    public void setContrasenap(String contrasenap) {
        Contrasenap = contrasenap;
    }
}
