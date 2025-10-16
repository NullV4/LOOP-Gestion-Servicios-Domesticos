package com.example.loopv7.models;

/**
 * Modelo para notificaciones
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class Notification {
    private int id;
    private int userId;
    private String title;
    private String message;
    private String type;
    private String category;
    private String referenceType;
    private int referenceId;
    private boolean isRead;
    private boolean isSent;
    private String sentAt;
    private String readAt;
    private String expiresAt;
    private String createdAt;

    public Notification() {
        // Constructor por defecto
        this.type = "info";
        this.category = "system";
        this.isRead = false;
        this.isSent = false;
    }

    public Notification(int userId, String title, String message, String type, String category) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.category = category;
        this.isRead = false;
        this.isSent = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setIsSent(boolean isSent) {
        this.isSent = isSent;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Verifica si la notificación está expirada
     * @return true si está expirada, false si no
     */
    public boolean isExpired() {
        if (expiresAt == null || expiresAt.isEmpty()) {
            return false;
        }
        
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            java.util.Date expireDate = sdf.parse(expiresAt);
            java.util.Date now = new java.util.Date();
            return now.after(expireDate);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", referenceType='" + referenceType + '\'' +
                ", referenceId=" + referenceId +
                ", isRead=" + isRead +
                ", isSent=" + isSent +
                ", sentAt='" + sentAt + '\'' +
                ", readAt='" + readAt + '\'' +
                ", expiresAt='" + expiresAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
