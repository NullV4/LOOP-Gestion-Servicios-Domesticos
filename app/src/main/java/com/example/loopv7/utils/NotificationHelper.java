package com.example.loopv7.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.loopv7.R;
import com.example.loopv7.MainActivity;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.User;

/**
 * Clase helper para manejar notificaciones de la aplicación LOOP
 * 
 * Funcionalidades:
 * - Crear canales de notificación
 * - Enviar notificaciones de solicitudes
 * - Enviar notificaciones de pagos
 * - Enviar notificaciones de calificaciones
 * - Enviar notificaciones de cambios de estado
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    
    // Canales de notificación
    public static final String CHANNEL_REQUESTS = "channel_requests";
    public static final String CHANNEL_PAYMENTS = "channel_payments";
    public static final String CHANNEL_RATINGS = "channel_ratings";
    public static final String CHANNEL_STATUS = "channel_status";
    
    // IDs de notificación
    private static final int NOTIFICATION_ID_REQUESTS = 1000;
    private static final int NOTIFICATION_ID_PAYMENTS = 2000;
    private static final int NOTIFICATION_ID_RATINGS = 3000;
    private static final int NOTIFICATION_ID_STATUS = 4000;
    
    private Context context;
    private NotificationManagerCompat notificationManager;
    
    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannels();
    }
    
    /**
     * Crea los canales de notificación necesarios
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            // Canal para solicitudes
            NotificationChannel requestsChannel = new NotificationChannel(
                CHANNEL_REQUESTS,
                "Solicitudes",
                NotificationManager.IMPORTANCE_HIGH
            );
            requestsChannel.setDescription("Notificaciones sobre nuevas solicitudes y cambios en solicitudes");
            requestsChannel.enableVibration(true);
            requestsChannel.setShowBadge(true);
            
            // Canal para pagos
            NotificationChannel paymentsChannel = new NotificationChannel(
                CHANNEL_PAYMENTS,
                "Pagos",
                NotificationManager.IMPORTANCE_HIGH
            );
            paymentsChannel.setDescription("Notificaciones sobre pagos recibidos y procesados");
            paymentsChannel.enableVibration(true);
            paymentsChannel.setShowBadge(true);
            
            // Canal para calificaciones
            NotificationChannel ratingsChannel = new NotificationChannel(
                CHANNEL_RATINGS,
                "Calificaciones",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            ratingsChannel.setDescription("Notificaciones sobre calificaciones recibidas");
            ratingsChannel.enableVibration(true);
            ratingsChannel.setShowBadge(true);
            
            // Canal para cambios de estado
            NotificationChannel statusChannel = new NotificationChannel(
                CHANNEL_STATUS,
                "Estados",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            statusChannel.setDescription("Notificaciones sobre cambios de estado de servicios");
            statusChannel.enableVibration(true);
            statusChannel.setShowBadge(true);
            
            // Crear canales
            manager.createNotificationChannel(requestsChannel);
            manager.createNotificationChannel(paymentsChannel);
            manager.createNotificationChannel(ratingsChannel);
            manager.createNotificationChannel(statusChannel);
            
            Log.d(TAG, "Canales de notificación creados");
        }
    }
    
    /**
     * Envía notificación de nueva solicitud
     * 
     * @param request Solicitud creada
     * @param clientName Nombre del cliente
     */
    public void notifyNewRequest(Request request, String clientName) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "requests");
            PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                NOTIFICATION_ID_REQUESTS + request.getId(),
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REQUESTS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Nueva Solicitud")
                .setContentText("Tienes una nueva solicitud de " + clientName)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Solicitud #" + request.getId() + " de " + clientName + 
                            "\nServicio: " + request.getServiceId() + 
                            "\nFecha: " + request.getScheduledDate() + " " + request.getScheduledTime()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
            
            notificationManager.notify(NOTIFICATION_ID_REQUESTS + request.getId(), builder.build());
            Log.d(TAG, "Notificación de nueva solicitud enviada");
            
        } catch (Exception e) {
            Log.e(TAG, "Error enviando notificación de nueva solicitud", e);
        }
    }
    
    /**
     * Envía notificación de pago recibido
     * 
     * @param request Solicitud pagada
     */
    public void notifyPaymentReceived(Request request) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "requests");
            PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                NOTIFICATION_ID_PAYMENTS + request.getId(),
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_PAYMENTS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Pago Recibido")
                .setContentText("Has recibido un pago de S/ " + request.getTotalPrice())
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Pago confirmado para la solicitud #" + request.getId() + 
                            "\nMonto: S/ " + request.getTotalPrice() + 
                            "\nFecha: " + request.getScheduledDate()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
            
            notificationManager.notify(NOTIFICATION_ID_PAYMENTS + request.getId(), builder.build());
            Log.d(TAG, "Notificación de pago recibido enviada");
            
        } catch (Exception e) {
            Log.e(TAG, "Error enviando notificación de pago", e);
        }
    }
    
    /**
     * Envía notificación de calificación recibida
     * 
     * @param request Solicitud calificada
     * @param rating Calificación recibida
     */
    public void notifyRatingReceived(Request request, int rating) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "profile");
            PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                NOTIFICATION_ID_RATINGS + request.getId(),
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            String ratingText = "⭐".repeat(rating) + " (" + rating + "/5)";
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_RATINGS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Nueva Calificación")
                .setContentText("Has recibido una calificación de " + ratingText)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Calificación recibida para la solicitud #" + request.getId() + 
                            "\nCalificación: " + ratingText + 
                            "\n¡Gracias por tu excelente servicio!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
            
            notificationManager.notify(NOTIFICATION_ID_RATINGS + request.getId(), builder.build());
            Log.d(TAG, "Notificación de calificación recibida enviada");
            
        } catch (Exception e) {
            Log.e(TAG, "Error enviando notificación de calificación", e);
        }
    }
    
    /**
     * Envía notificación de cambio de estado
     * 
     * @param request Solicitud con estado cambiado
     * @param oldStatus Estado anterior
     * @param newStatus Estado nuevo
     */
    public void notifyRequestStatusChange(Request request, String oldStatus, String newStatus) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "requests");
            PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                NOTIFICATION_ID_STATUS + request.getId(),
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            String title = getStatusChangeTitle(newStatus);
            String message = getStatusChangeMessage(oldStatus, newStatus);
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_STATUS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Solicitud #" + request.getId() + 
                            "\nEstado anterior: " + getStatusText(oldStatus) + 
                            "\nEstado actual: " + getStatusText(newStatus) + 
                            "\nFecha: " + request.getScheduledDate()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
            
            notificationManager.notify(NOTIFICATION_ID_STATUS + request.getId(), builder.build());
            Log.d(TAG, "Notificación de cambio de estado enviada");
            
        } catch (Exception e) {
            Log.e(TAG, "Error enviando notificación de cambio de estado", e);
        }
    }
    
    /**
     * Obtiene el título para el cambio de estado
     */
    private String getStatusChangeTitle(String newStatus) {
        switch (newStatus) {
            case "aceptada": return "Solicitud Aceptada";
            case "rechazada": return "Solicitud Rechazada";
            case "en_progreso": return "🚀 Servicio Iniciado";
            case "completada": return "🎉 Servicio Completado";
            case "cancelada": return "Solicitud Cancelada";
            default: return "Estado Actualizado";
        }
    }
    
    /**
     * Obtiene el mensaje para el cambio de estado
     */
    private String getStatusChangeMessage(String oldStatus, String newStatus) {
        switch (newStatus) {
            case "aceptada": return "Tu solicitud ha sido aceptada";
            case "rechazada": return "Tu solicitud ha sido rechazada";
            case "en_progreso": return "¡El servicio ha comenzado! 🚀";
            case "completada": return "¡El servicio ha sido completado! 🎉";
            case "cancelada": return "La solicitud ha sido cancelada";
            default: return "El estado de tu solicitud ha cambiado";
        }
    }
    
    /**
     * Obtiene el texto legible del estado
     */
    private String getStatusText(String status) {
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
     * Verifica si las notificaciones están habilitadas
     * 
     * @return true si las notificaciones están habilitadas
     */
    public boolean areNotificationsEnabled() {
        return notificationManager.areNotificationsEnabled();
    }
    
    /**
     * Cancela una notificación específica
     * 
     * @param notificationId ID de la notificación a cancelar
     */
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
        Log.d(TAG, "Notificación " + notificationId + " cancelada");
    }
    
    /**
     * Cancela todas las notificaciones
     */
    public void cancelAllNotifications() {
        notificationManager.cancelAll();
        Log.d(TAG, "Todas las notificaciones canceladas");
    }
}
