package com.example.medilink.models;

public class Category {
    private String id;
    private String name;
    private String iconUrl;
    private String color;

    public Category() {}

    public Category(String id, String name, String iconUrl, String color) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.color = color;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}