package com.example.loopv7.models;

/**
 * Modelo para calificaciones detalladas
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class Rating {
    private int id;
    private int requestId;
    private int raterId;
    private int ratedId;
    private int overallRating;
    private int qualityRating;
    private int punctualityRating;
    private int communicationRating;
    private int cleanlinessRating;
    private String review;
    private boolean isAnonymous;
    private String status;
    private String createdAt;

    public Rating() {
        // Constructor por defecto
        this.status = "activo";
        this.isAnonymous = false;
    }

    public Rating(int requestId, int raterId, int ratedId, int overallRating, String review) {
        this.requestId = requestId;
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.overallRating = overallRating;
        this.review = review;
        this.status = "activo";
        this.isAnonymous = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getRaterId() {
        return raterId;
    }

    public void setRaterId(int raterId) {
        this.raterId = raterId;
    }

    public int getRatedId() {
        return ratedId;
    }

    public void setRatedId(int ratedId) {
        this.ratedId = ratedId;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(int qualityRating) {
        this.qualityRating = qualityRating;
    }

    public int getPunctualityRating() {
        return punctualityRating;
    }

    public void setPunctualityRating(int punctualityRating) {
        this.punctualityRating = punctualityRating;
    }

    public int getCommunicationRating() {
        return communicationRating;
    }

    public void setCommunicationRating(int communicationRating) {
        this.communicationRating = communicationRating;
    }

    public int getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(int cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
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

    /**
     * Calcula el rating promedio de todos los criterios
     * @return Rating promedio
     */
    public double getAverageRating() {
        int totalRatings = 0;
        int count = 0;
        
        if (overallRating > 0) {
            totalRatings += overallRating;
            count++;
        }
        if (qualityRating > 0) {
            totalRatings += qualityRating;
            count++;
        }
        if (punctualityRating > 0) {
            totalRatings += punctualityRating;
            count++;
        }
        if (communicationRating > 0) {
            totalRatings += communicationRating;
            count++;
        }
        if (cleanlinessRating > 0) {
            totalRatings += cleanlinessRating;
            count++;
        }
        
        return count > 0 ? (double) totalRatings / count : 0.0;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", raterId=" + raterId +
                ", ratedId=" + ratedId +
                ", overallRating=" + overallRating +
                ", qualityRating=" + qualityRating +
                ", punctualityRating=" + punctualityRating +
                ", communicationRating=" + communicationRating +
                ", cleanlinessRating=" + cleanlinessRating +
                ", review='" + review + '\'' +
                ", isAnonymous=" + isAnonymous +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
