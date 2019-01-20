package com.hcode.hFarfadet.Model;

public class Lampe {
    private String nom;
    private int red;
    private int green;
    private int blue;


    public Lampe(String nom, int red, int green, int blue) {
        this.nom = nom;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Lampe() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
