package com.example.loopv7.models;

public class Request {
    private int id;
    private int clientId;
    private int sociaId;
    private int serviceId;
    private String status; // "pendiente", "aceptada", "rechazada", "en_progreso", "completada", "cancelada"
    private String scheduledDate;
    private String scheduledTime;
    private String address;
    private String notes;
    private double totalPrice;
    private String paymentStatus; // "pendiente", "pagado", "reembolsado"
    private int rating;
    private String review;
    private boolean isArchived; // Indica si la solicitud está archivada
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

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { this.isArchived = archived; }

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
    public boolean isInProgress() { return "en_progreso".equals(status); }
    public boolean isCompleted() { return "completada".equals(status); }
    public boolean isCancelled() { return "cancelada".equals(status); }
    
    /**
     * Verifica si el servicio puede ser iniciado (debe estar aceptado)
     * @return true si puede ser iniciado
     */
    public boolean canBeStarted() {
        return isAccepted() && "pagado".equals(paymentStatus);
    }
    
    /**
     * Verifica si el servicio puede ser completado (debe estar en progreso)
     * @return true si puede ser completado
     */
    public boolean canBeCompleted() {
        return isInProgress();
    }
    
    /**
     * Verifica si el servicio puede ser calificado (debe estar completado y pagado)
     * @return true si puede ser calificado
     */
    public boolean canBeRated() {
        return isCompleted() && "pagado".equals(paymentStatus) && rating == 0;
    }
    
    /**
     * Verifica si la solicitud puede ser archivada (debe estar completada, pagada y calificada)
     * @return true si puede ser archivada
     */
    public boolean canBeArchived() {
        return isCompleted() && "pagado".equals(paymentStatus) && rating > 0 && !isArchived;
    }
    
    /**
     * Obtiene el texto legible del estado
     * @return String con el estado en formato legible
     */
    public String getStatusText() {
        switch (status) {
            case "pendiente": return "Pendiente";
            case "aceptada": return "Aceptada";
            case "rechazada": return "Rechazada";
            case "en_progreso": return "En Progreso";
            case "completada": return "Completada";
            case "cancelada": return "Cancelada";
            default: return status;
        }
    }
    
    /**
     * Obtiene el color del estado para la UI
     * @return String con el nombre del color
     */
    public String getStatusColor() {
        switch (status) {
            case "pendiente": return "warning";
            case "aceptada": return "info";
            case "rechazada": return "error";
            case "en_progreso": return "warning";
            case "completada": return "success";
            case "cancelada": return "secondary";
            default: return "text_primary";
        }
    }
    
    /**
     * Obtiene el ícono del estado para la UI
     * @return String con el emoji del estado
     */
    public String getStatusIcon() {
        switch (status) {
            case "pendiente": return "⏳";
            case "aceptada": return "✅";
            case "rechazada": return "❌";
            case "en_progreso": return "🔄";
            case "completada": return "🎉";
            case "cancelada": return "🚫";
            default: return "❓";
        }
    }
}
