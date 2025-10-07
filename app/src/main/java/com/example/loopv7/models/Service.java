package com.example.loopv7.models;

public class Service {
    private int id;
    private String name;
    private String description;
    private double price;
    private int duration; // en minutos
    private String category;
    private String status; // "activo" o "inactivo"
    private String createdAt;

    // Constructores
    public Service() {}

    public Service(String name, String description, double price, int duration, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.category = category;
        this.status = "activo";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public String getFormattedDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;
        if (hours > 0) {
            return minutes > 0 ? hours + "h " + minutes + "min" : hours + "h";
        }
        return minutes + "min";
    }
}
