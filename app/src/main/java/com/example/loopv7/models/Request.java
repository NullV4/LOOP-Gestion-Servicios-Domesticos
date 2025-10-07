package com.example.loopv7.models;

public class Request {
    private int id;
    private int clientId;
    private int sociaId;
    private int serviceId;
    private String status; // "pendiente", "aceptada", "rechazada", "completada", "cancelada"
    private String scheduledDate;
    private String scheduledTime;
    private String address;
    private String notes;
    private double totalPrice;
    private String paymentStatus; // "pendiente", "pagado", "reembolsado"
    private int rating;
    private String review;
    private String createdAt;
    private String updatedAt;

    // Constructores
    public Request() {}

    public Request(int clientId, int serviceId, String scheduledDate, String scheduledTime, 
                   String address, String notes, double totalPrice) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.address = address;
        this.notes = notes;
        this.totalPrice = totalPrice;
        this.status = "pendiente";
        this.paymentStatus = "pendiente";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getSociaId() { return sociaId; }
    public void setSociaId(int sociaId) { this.sociaId = sociaId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getFormattedPrice() {
        return String.format("$%.2f", totalPrice);
    }

    public boolean isPending() { return "pendiente".equals(status); }
    public boolean isAccepted() { return "aceptada".equals(status); }
    public boolean isRejected() { return "rechazada".equals(status); }
    public boolean isCompleted() { return "completada".equals(status); }
    public boolean isCancelled() { return "cancelada".equals(status); }
}
