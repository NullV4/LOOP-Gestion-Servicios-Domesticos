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
    private int completedServices; // Total de servicios completados para socias
    private String lastServiceDate; // Fecha del último servicio completado
    private String location; // Ubicación del usuario
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

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCompletedServices() { return completedServices; }
    public void setCompletedServices(int completedServices) { this.completedServices = completedServices; }

    public String getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(String lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    public boolean isCliente() { return "cliente".equals(role); }
    public boolean isSocia() { return "socia".equals(role); }
    
    public String getFormattedRating() {
        if (totalRatings == 0) return "Sin calificaciones";
        return String.format("%.1f ⭐ (%d calificaciones)", rating, totalRatings);
    }
    
    /**
     * Verifica si el usuario tiene una descripción
     * @return true si tiene descripción
     */
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }
    
    /**
     * Verifica si el usuario tiene una imagen de perfil
     * @return true si tiene imagen de perfil
     */
    public boolean hasProfileImage() {
        return profileImage != null && !profileImage.trim().isEmpty();
    }
    
    /**
     * Verifica si el usuario tiene ubicación
     * @return true si tiene ubicación
     */
    public boolean hasLocation() {
        return location != null && !location.trim().isEmpty();
    }
    
    /**
     * Obtiene la descripción o un mensaje por defecto
     * @return descripción o mensaje por defecto
     */
    public String getDescriptionOrDefault() {
        if (hasDescription()) {
            return description;
        }
        return isSocia() ? "Socia trabajadora sin descripción" : "Cliente sin descripción";
    }
    
    /**
     * Obtiene la ubicación o un mensaje por defecto
     * @return ubicación o mensaje por defecto
     */
    public String getLocationOrDefault() {
        if (hasLocation()) {
            return location;
        }
        return "Ubicación no especificada";
    }
}
