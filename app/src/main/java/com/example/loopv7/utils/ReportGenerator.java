package com.example.loopv7.utils;

import android.content.Context;
import android.util.Log;

import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Rating;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.SociaReport;
import com.example.loopv7.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generador de reportes detallados para socias
 * 
 * Funcionalidades:
 * - Generar reportes por período (diario, semanal, mensual, anual)
 * - Calcular estadísticas de rendimiento
 * - Analizar tendencias y crecimiento
 * - Generar métricas de satisfacción del cliente
 * - Exportar reportes en diferentes formatos
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ReportGenerator {
    
    private Context context;
    private DatabaseHelper databaseHelper;
    private ErrorHandler errorHandler;
    
    public ReportGenerator(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.errorHandler = ErrorHandler.getInstance(context);
    }
    
    /**
     * Genera un reporte completo para una socia en un período específico
     * @param sociaId ID de la socia
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Reporte completo de la socia
     */
    public SociaReport generateSociaReport(int sociaId, Date startDate, Date endDate) {
        try {
            // Verificar que el usuario es una socia
            User user = databaseHelper.getUserById(sociaId);
            if (user == null || !user.isSocia()) {
                errorHandler.handleError(ErrorHandler.ErrorType.AUTHENTICATION_ERROR, 
                    "Solo las socias pueden generar reportes");
                return null;
            }
            SociaReport report = new SociaReport();
            
            // Configurar período
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setPeriod(getPeriodString(startDate, endDate));
            
            // Obtener todas las solicitudes de la socia en el período
            List<Request> requests = getSociaRequestsInPeriod(sociaId, startDate, endDate);
            
            // Calcular estadísticas básicas
            calculateBasicStats(report, requests);
            
            // Calcular estadísticas de ganancias
            calculateEarningsStats(report, requests);
            
            // Calcular estadísticas de calificaciones
            calculateRatingStats(report, sociaId);
            
            // Calcular estadísticas de servicios
            calculateServiceStats(report, requests);
            
            // Calcular estadísticas de clientes
            calculateClientStats(report, requests);
            
            // Calcular métricas de rendimiento
            calculatePerformanceMetrics(report);
            
            // Calcular tendencias (comparar con período anterior)
            calculateTrends(report, sociaId, startDate, endDate);
            
            return report;
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error al generar reporte de socia", e);
            return null;
        }
    }
    
    /**
     * Genera reporte del día actual
     */
    public SociaReport generateDailyReport(int sociaId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        
        return generateSociaReport(sociaId, startDate, endDate);
    }
    
    /**
     * Genera reporte de la semana actual
     */
    public SociaReport generateWeeklyReport(int sociaId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        
        return generateSociaReport(sociaId, startDate, endDate);
    }
    
    /**
     * Genera reporte del mes actual
     */
    public SociaReport generateMonthlyReport(int sociaId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        
        return generateSociaReport(sociaId, startDate, endDate);
    }
    
    /**
     * Genera reporte del año actual
     */
    public SociaReport generateYearlyReport(int sociaId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        
        return generateSociaReport(sociaId, startDate, endDate);
    }
    
    /**
     * Obtiene las solicitudes de una socia en un período específico
     */
    private List<Request> getSociaRequestsInPeriod(int sociaId, Date startDate, Date endDate) {
        List<Request> allRequests = databaseHelper.getRequestsBySociaId(sociaId);
        List<Request> periodRequests = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        for (Request request : allRequests) {
            try {
                Date requestDate = sdf.parse(request.getCreatedAt());
                if (requestDate != null && !requestDate.before(startDate) && !requestDate.after(endDate)) {
                    periodRequests.add(request);
                }
            } catch (Exception e) {
                // Ignorar fechas inválidas
            }
        }
        
        return periodRequests;
    }
    
    /**
     * Calcula estadísticas básicas
     */
    private void calculateBasicStats(SociaReport report, List<Request> requests) {
        report.setTotalRequests(requests.size());
        
        int completed = 0, pending = 0, cancelled = 0;
        
        for (Request request : requests) {
            switch (request.getStatus()) {
                case "completada":
                    completed++;
                    break;
                case "pendiente":
                case "aceptada":
                case "en_progreso":
                    pending++;
                    break;
                case "rechazada":
                case "cancelada":
                    cancelled++;
                    break;
            }
        }
        
        report.setCompletedRequests(completed);
        report.setPendingRequests(pending);
        report.setCancelledRequests(cancelled);
    }
    
    /**
     * Calcula estadísticas de ganancias
     */
    private void calculateEarningsStats(SociaReport report, List<Request> requests) {
        double totalEarnings = 0;
        double highestEarning = 0;
        double lowestEarning = Double.MAX_VALUE;
        int paidServices = 0;
        
        for (Request request : requests) {
            if ("pagado".equals(request.getPaymentStatus()) && "completada".equals(request.getStatus())) {
                double earning = request.getTotalPrice();
                totalEarnings += earning;
                paidServices++;
                
                if (earning > highestEarning) {
                    highestEarning = earning;
                }
                if (earning < lowestEarning) {
                    lowestEarning = earning;
                }
            }
        }
        
        report.setTotalEarnings(totalEarnings);
        report.setHighestEarning(highestEarning);
        report.setLowestEarning(lowestEarning == Double.MAX_VALUE ? 0 : lowestEarning);
        report.setAverageEarningPerService(paidServices > 0 ? totalEarnings / paidServices : 0);
    }
    
    /**
     * Calcula estadísticas de calificaciones desde la tabla ratings
     */
    private void calculateRatingStats(SociaReport report, int sociaId) {
        try {
            // Obtener calificaciones desde la tabla ratings
            List<Rating> ratings = databaseHelper.getRatingsByRatedId(sociaId);
            
            if (ratings == null || ratings.isEmpty()) {
                report.setTotalRatings(0);
                report.setAverageRating(0);
                report.setFiveStarRatings(0);
                report.setFourStarRatings(0);
                report.setThreeStarRatings(0);
                report.setTwoStarRatings(0);
                report.setOneStarRatings(0);
                return;
            }
            
            int totalRatings = ratings.size();
            double totalRatingSum = 0;
            int[] ratingCounts = new int[5]; // 1-5 estrellas
            
            for (Rating rating : ratings) {
                int overallRating = rating.getOverallRating();
                if (overallRating > 0 && overallRating <= 5) {
                    totalRatingSum += overallRating;
                    ratingCounts[overallRating - 1]++;
                }
            }
            
            report.setTotalRatings(totalRatings);
            report.setAverageRating(totalRatings > 0 ? totalRatingSum / totalRatings : 0);
            report.setFiveStarRatings(ratingCounts[4]);
            report.setFourStarRatings(ratingCounts[3]);
            report.setThreeStarRatings(ratingCounts[2]);
            report.setTwoStarRatings(ratingCounts[1]);
            report.setOneStarRatings(ratingCounts[0]);
            
        } catch (Exception e) {
            Log.e("ReportGenerator", "Error calculando estadísticas de calificaciones", e);
            // Valores por defecto en caso de error
            report.setTotalRatings(0);
            report.setAverageRating(0);
            report.setFiveStarRatings(0);
            report.setFourStarRatings(0);
            report.setThreeStarRatings(0);
            report.setTwoStarRatings(0);
            report.setOneStarRatings(0);
        }
    }
    
    /**
     * Calcula estadísticas de servicios
     */
    private void calculateServiceStats(SociaReport report, List<Request> requests) {
        Map<Integer, Integer> serviceCounts = new HashMap<>();
        Map<Integer, Double> serviceDurations = new HashMap<>();
        
        for (Request request : requests) {
            int serviceId = request.getServiceId();
            serviceCounts.put(serviceId, serviceCounts.getOrDefault(serviceId, 0) + 1);
            
            // Obtener duración del servicio
            Service service = databaseHelper.getServiceById(serviceId);
            if (service != null) {
                serviceDurations.put(serviceId, (double) service.getDuration());
            }
        }
        
        // Encontrar servicio más y menos solicitado
        int mostRequested = 0, leastRequested = 0;
        int maxCount = 0, minCount = Integer.MAX_VALUE;
        
        for (Map.Entry<Integer, Integer> entry : serviceCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostRequested = entry.getKey();
            }
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                leastRequested = entry.getKey();
            }
        }
        
        report.setMostRequestedServiceId(mostRequested);
        report.setLeastRequestedServiceId(leastRequested);
        
        // Obtener nombres de servicios
        if (mostRequested > 0) {
            Service service = databaseHelper.getServiceById(mostRequested);
            report.setMostRequestedServiceName(service != null ? service.getName() : "N/A");
        }
        
        if (leastRequested > 0) {
            Service service = databaseHelper.getServiceById(leastRequested);
            report.setLeastRequestedServiceName(service != null ? service.getName() : "N/A");
        }
        
        // Calcular duración promedio
        double totalDuration = 0;
        for (Double duration : serviceDurations.values()) {
            totalDuration += duration;
        }
        report.setAverageServiceDuration(serviceDurations.size() > 0 ? totalDuration / serviceDurations.size() : 0);
    }
    
    /**
     * Calcula estadísticas de clientes
     */
    private void calculateClientStats(SociaReport report, List<Request> requests) {
        Map<Integer, Integer> clientCounts = new HashMap<>();
        
        for (Request request : requests) {
            int clientId = request.getClientId();
            clientCounts.put(clientId, clientCounts.getOrDefault(clientId, 0) + 1);
        }
        
        report.setUniqueClients(clientCounts.size());
        
        int returningClients = 0;
        for (int count : clientCounts.values()) {
            if (count > 1) {
                returningClients++;
            }
        }
        
        report.setReturningClients(returningClients);
        report.setNewClients(clientCounts.size() - returningClients);
    }
    
    /**
     * Calcula métricas de rendimiento
     */
    private void calculatePerformanceMetrics(SociaReport report) {
        // Tasa de finalización
        if (report.getTotalRequests() > 0) {
            report.setCompletionRate((double) report.getCompletedRequests() / report.getTotalRequests() * 100);
        }
        
        // Satisfacción del cliente (basada en calificaciones)
        if (report.getTotalRatings() > 0) {
            report.setCustomerSatisfaction((report.getAverageRating() / 5.0) * 100);
        }
        
        // Puntuación de eficiencia (combinación de métricas)
        double efficiency = 0;
        if (report.getCompletionRate() > 0) efficiency += report.getCompletionRate() * 0.4;
        if (report.getCustomerSatisfaction() > 0) efficiency += report.getCustomerSatisfaction() * 0.4;
        if (report.getAverageRating() > 0) efficiency += (report.getAverageRating() / 5.0) * 20; // 20% basado en rating
        
        report.setEfficiencyScore(Math.min(efficiency / 10, 10)); // Normalizar a 10
    }
    
    /**
     * Calcula tendencias comparando con período anterior
     */
    private void calculateTrends(SociaReport report, int sociaId, Date startDate, Date endDate) {
        try {
            // Calcular período anterior
            long periodDuration = endDate.getTime() - startDate.getTime();
            Date previousStartDate = new Date(startDate.getTime() - periodDuration);
            Date previousEndDate = new Date(startDate.getTime() - 1);
            
            // Generar reporte del período anterior
            SociaReport previousReport = generateSociaReport(sociaId, previousStartDate, previousEndDate);
            
            if (previousReport != null) {
                // Calcular crecimiento de ganancias
                if (previousReport.getTotalEarnings() > 0) {
                    double earningsGrowth = ((report.getTotalEarnings() - previousReport.getTotalEarnings()) 
                            / previousReport.getTotalEarnings()) * 100;
                    report.setEarningsGrowth(earningsGrowth);
                }
                
                // Calcular tendencia de calificaciones
                report.setRatingTrend(report.getAverageRating() - previousReport.getAverageRating());
                
                // Calcular crecimiento de solicitudes
                report.setRequestsGrowth(report.getTotalRequests() - previousReport.getTotalRequests());
            }
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error al calcular tendencias", e);
        }
    }
    
    /**
     * Genera string descriptivo del período
     */
    private String getPeriodString(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(startDate) + " - " + sdf.format(endDate);
    }
}
