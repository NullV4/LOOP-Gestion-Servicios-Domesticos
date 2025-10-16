package com.example.loopv7.models;

/**
 * Modelo para categorías de servicios
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class Category {
    private int id;
    private String name;
    private String description;
    private int parentId;
    private String icon;
    private String color;
    private int sortOrder;
    private String status;
    private String createdAt;

    public Category() {
        // Constructor por defecto
    }

    public Category(String name, String description, int parentId, String icon, String color, int sortOrder, String status) {
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.icon = icon;
        this.color = color;
        this.sortOrder = sortOrder;
        this.status = status;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                ", sortOrder=" + sortOrder +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
