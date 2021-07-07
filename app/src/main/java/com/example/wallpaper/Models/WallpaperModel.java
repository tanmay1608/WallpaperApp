package com.example.wallpaper.Models;

public class WallpaperModel {
    private int id;
    private String originalUrl,mediumUrl,photographerName;

    public WallpaperModel() {

    }

    public WallpaperModel(int id, String originalUrl, String mediumUrl, String photographerName) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.mediumUrl = mediumUrl;
        this.photographerName = photographerName;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }
}
