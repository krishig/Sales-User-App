package com.krishig.android.model;

public class Market {
    String category;

    public Market(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String seedName;
    String seedPrice;

    public Market(String seedName, String seedPrice) {
        this.seedName = seedName;
        this.seedPrice = seedPrice;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public String getSeedPrice() {
        return seedPrice;
    }

    public void setSeedPrice(String seedPrice) {
        this.seedPrice = seedPrice;
    }
}
