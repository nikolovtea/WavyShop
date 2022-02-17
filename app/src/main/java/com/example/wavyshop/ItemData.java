package com.example.wavyshop;

public class ItemData {
    private String itemName;
    private String itemColor;
    private Integer itemImage;
    private String id;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public Integer getItemImage() {
        return itemImage;
    }

    public void setItemImage(Integer itemImage) {
        this.itemImage = itemImage;
    }

    public ItemData(String itemName, String itemColor, Integer itemImage) {
        this.itemName= itemName;
        this.itemColor = itemColor;
        this.itemImage = itemImage;

    }


}
