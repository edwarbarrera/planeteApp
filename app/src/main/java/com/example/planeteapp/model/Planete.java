package com.example.planeteapp.model;

import com.google.gson.annotations.SerializedName;

public class Planete {
    @SerializedName("name")// fait correspondre avec le name dans gson il mappe les 2 noms
    private String nom;
    private long id;
    private int distance;
    private String imageBase64;

    public Planete() {
    }

    public Planete(String nom, long id, int distance, String imageBase64) {
        this.nom = nom;
        this.id = id;
        this.distance = distance;
        this.imageBase64 = imageBase64;
    }

    public Planete(String nom, int distance, String imageBase64) {

    }

    @Override
    public String toString() {
        return "Planete{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
                ", distance=" + distance +
                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}

