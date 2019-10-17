package com.nufaza.geotagpaud.ui.data;

public class WebScrapResult {

    int image;
    String name;
    String agregat;

    public WebScrapResult(int image, String name, String agregat) {
        this.image = image;
        this.name = name;
        this.agregat = agregat;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgregat() {
        return agregat;
    }

    public void setAgregat(String agregat) {
        this.agregat = agregat;
    }
}
