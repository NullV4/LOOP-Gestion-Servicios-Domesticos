package com.example.loopv7.models;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String role; // "cliente" o "socia"
    private String status; // "activo" o "inactivo"
    private String description; // Descripción para socias
    private String profileImage; // URL o path de la imagen de perfil
    private double rating; // Calificación promedio para socias
    private int totalRatings; // Total de calificaciones recibidas
    private String createdAt;
    private String updatedAt;

    // Constructores
    public User() {}

    public User(String email, String password, String name, String phone, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.status = "activo";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public boolean isCliente() { return "cliente".equals(role); }
    public boolean isSocia() { return "socia".equals(role); }
    
    public String getFormattedRating() {
        if (totalRatings == 0) return "Sin calificaciones";
        return String.format("%.1f ⭐ (%d calificaciones)", rating, totalRatings);
    }
}
