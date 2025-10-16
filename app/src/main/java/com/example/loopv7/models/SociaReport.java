package com.example.loopv7.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Modelo para reportes detallados de socias
 * 
 * Contiene estadísticas completas de rendimiento, ganancias y servicios
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class SociaReport {
    
    // Período del reporte
    private String period;
    private Date startDate;
    private Date endDate;
    
    // Estadísticas generales
    private int totalRequests;
    private int completedRequests;
    private int pendingRequests;
    private int cancelledRequests;
    
    // Estadísticas de ganancias
    private double totalEarnings;
    private double averageEarningPerService;
    private double highestEarning;
    private double lowestEarning;
    
    // Estadísticas de calificaciones
    private double averageRating;
    private int totalRatings;
    private int fiveStarRatings;
    private int fourStarRatings;
    private int threeStarRatings;
    private int twoStarRatings;
    private int oneStarRatings;
    
    // Estadísticas de servicios
    private int mostRequestedServiceId;
    private String mostRequestedServiceName;
    private int leastRequestedServiceId;
    private String leastRequestedServiceName;
    
    // Estadísticas de tiempo
    private double averageServiceDuration;
    private int totalWorkingHours;
    private int totalWorkingDays;
    
    // Estadísticas de clientes
    private int uniqueClients;
    private int returningClients;
    private int newClients;
    
    // Tendencias
    private double earningsGrowth;
    private double ratingTrend;
    private int requestsGrowth;
    
    // Métricas de rendimiento
    private double completionRate;
    private double customerSatisfaction;
    private double efficiencyScore;
    
    public SociaReport() {
        // Constructor por defecto
    }
    
    // Getters y Setters
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
    
    public int getCompletedRequests() { return completedRequests; }
    public void setCompletedRequests(int completedRequests) { this.completedRequests = completedRequests; }
    
    public int getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }
    
    public int getCancelledRequests() { return cancelledRequests; }
    public void setCancelledRequests(int cancelledRequests) { this.cancelledRequests = cancelledRequests; }
    
    public double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }
    
    public double getAverageEarningPerService() { return averageEarningPerService; }
    public void setAverageEarningPerService(double averageEarningPerService) { this.averageEarningPerService = averageEarningPerService; }
    
    public double getHighestEarning() { return highestEarning; }
    public void setHighestEarning(double highestEarning) { this.highestEarning = highestEarning; }
    
    public double getLowestEarning() { return lowestEarning; }
    public void setLowestEarning(double lowestEarning) { this.lowestEarning = lowestEarning; }
    
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    
    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
    
    public int getFiveStarRatings() { return fiveStarRatings; }
    public void setFiveStarRatings(int fiveStarRatings) { this.fiveStarRatings = fiveStarRatings; }
    
    public int getFourStarRatings() { return fourStarRatings; }
    public void setFourStarRatings(int fourStarRatings) { this.fourStarRatings = fourStarRatings; }
    
    public int getThreeStarRatings() { return threeStarRatings; }
    public void setThreeStarRatings(int threeStarRatings) { this.threeStarRatings = threeStarRatings; }
    
    public int getTwoStarRatings() { return twoStarRatings; }
    public void setTwoStarRatings(int twoStarRatings) { this.twoStarRatings = twoStarRatings; }
    
    public int getOneStarRatings() { return oneStarRatings; }
    public void setOneStarRatings(int oneStarRatings) { this.oneStarRatings = oneStarRatings; }
    
    public int getMostRequestedServiceId() { return mostRequestedServiceId; }
    public void setMostRequestedServiceId(int mostRequestedServiceId) { this.mostRequestedServiceId = mostRequestedServiceId; }
    
    public String getMostRequestedServiceName() { return mostRequestedServiceName; }
    public void setMostRequestedServiceName(String mostRequestedServiceName) { this.mostRequestedServiceName = mostRequestedServiceName; }
    
    public int getLeastRequestedServiceId() { return leastRequestedServiceId; }
    public void setLeastRequestedServiceId(int leastRequestedServiceId) { this.leastRequestedServiceId = leastRequestedServiceId; }
    
    public String getLeastRequestedServiceName() { return leastRequestedServiceName; }
    public void setLeastRequestedServiceName(String leastRequestedServiceName) { this.leastRequestedServiceName = leastRequestedServiceName; }
    
    public double getAverageServiceDuration() { return averageServiceDuration; }
    public void setAverageServiceDuration(double averageServiceDuration) { this.averageServiceDuration = averageServiceDuration; }
    
    public int getTotalWorkingHours() { return totalWorkingHours; }
    public void setTotalWorkingHours(int totalWorkingHours) { this.totalWorkingHours = totalWorkingHours; }
    
    public int getTotalWorkingDays() { return totalWorkingDays; }
    public void setTotalWorkingDays(int totalWorkingDays) { this.totalWorkingDays = totalWorkingDays; }
    
    public int getUniqueClients() { return uniqueClients; }
    public void setUniqueClients(int uniqueClients) { this.uniqueClients = uniqueClients; }
    
    public int getReturningClients() { return returningClients; }
    public void setReturningClients(int returningClients) { this.returningClients = returningClients; }
    
    public int getNewClients() { return newClients; }
    public void setNewClients(int newClients) { this.newClients = newClients; }
    
    public double getEarningsGrowth() { return earningsGrowth; }
    public void setEarningsGrowth(double earningsGrowth) { this.earningsGrowth = earningsGrowth; }
    
    public double getRatingTrend() { return ratingTrend; }
    public void setRatingTrend(double ratingTrend) { this.ratingTrend = ratingTrend; }
    
    public int getRequestsGrowth() { return requestsGrowth; }
    public void setRequestsGrowth(int requestsGrowth) { this.requestsGrowth = requestsGrowth; }
    
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    
    public double getCustomerSatisfaction() { return customerSatisfaction; }
    public void setCustomerSatisfaction(double customerSatisfaction) { this.customerSatisfaction = customerSatisfaction; }
    
    public double getEfficiencyScore() { return efficiencyScore; }
    public void setEfficiencyScore(double efficiencyScore) { this.efficiencyScore = efficiencyScore; }
    
    // Métodos de utilidad
    public String getFormattedTotalEarnings() {
        return String.format("S/ %.2f", totalEarnings);
    }
    
    public String getFormattedAverageEarning() {
        return String.format("S/ %.2f", averageEarningPerService);
    }
    
    public String getFormattedAverageRating() {
        return String.format("%.1f ⭐", averageRating);
    }
    
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate);
    }
    
    public String getFormattedCustomerSatisfaction() {
        return String.format("%.1f%%", customerSatisfaction);
    }
    
    public String getFormattedEfficiencyScore() {
        return String.format("%.1f/10", efficiencyScore);
    }
    
    public String getFormattedPeriod() {
        if (startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(startDate) + " - " + sdf.format(endDate);
        }
        return period;
    }
    
    public String getEarningsGrowthText() {
        if (earningsGrowth > 0) {
            return String.format("+%.1f%%", earningsGrowth);
        } else if (earningsGrowth < 0) {
            return String.format("%.1f%%", earningsGrowth);
        } else {
            return "0%";
        }
    }
    
    public String getRatingTrendText() {
        if (ratingTrend > 0) {
            return String.format("+%.1f", ratingTrend);
        } else if (ratingTrend < 0) {
            return String.format("%.1f", ratingTrend);
        } else {
            return "0.0";
        }
    }
    
    public String getRequestsGrowthText() {
        if (requestsGrowth > 0) {
            return String.format("+%d", requestsGrowth);
        } else if (requestsGrowth < 0) {
            return String.format("%d", requestsGrowth);
        } else {
            return "0";
        }
    }
}
