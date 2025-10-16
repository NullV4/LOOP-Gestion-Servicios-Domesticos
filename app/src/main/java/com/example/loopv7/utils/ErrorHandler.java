package com.example.loopv7.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.loopv7.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase centralizada para el manejo de errores en la aplicación LOOP
 * 
 * Funcionalidades:
 * - Categorización de errores
 * - Logging estructurado
 * - Mensajes amigables para el usuario
 * - Estrategias de recuperación
 * - Reporte de errores críticos
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ErrorHandler {
    
    private static final String TAG = "LOOP_ErrorHandler";
    
    // Tipos de errores
    public enum ErrorType {
        NETWORK_ERROR("Error de Red", "Problema de conectividad"),
        DATABASE_ERROR("Error de Base de Datos", "Problema con el almacenamiento local"),
        VALIDATION_ERROR("Error de Validación", "Datos ingresados no válidos"),
        AUTHENTICATION_ERROR("Error de Autenticación", "Problema con el inicio de sesión"),
        PERMISSION_ERROR("Error de Permisos", "Permisos insuficientes"),
        FILE_ERROR("Error de Archivo", "Problema con archivos"),
        UNKNOWN_ERROR("Error Desconocido", "Error no identificado"),
        BUSINESS_LOGIC_ERROR("Error de Lógica", "Error en la lógica de negocio");
        
        private final String title;
        private final String description;
        
        ErrorType(String title, String description) {
            this.title = title;
            this.description = description;
        }
        
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
    
    // Niveles de severidad
    public enum Severity {
        LOW("Bajo", "Error menor que no afecta la funcionalidad principal"),
        MEDIUM("Medio", "Error que afecta algunas funcionalidades"),
        HIGH("Alto", "Error que afecta funcionalidades importantes"),
        CRITICAL("Crítico", "Error que impide el uso de la aplicación");
        
        private final String level;
        private final String description;
        
        Severity(String level, String description) {
            this.level = level;
            this.description = description;
        }
        
        public String getLevel() { return level; }
        public String getDescription() { return description; }
    }
    
    private Context context;
    private static ErrorHandler instance;
    
    private ErrorHandler(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Obtiene la instancia singleton del ErrorHandler
     */
    public static synchronized ErrorHandler getInstance(Context context) {
        if (instance == null) {
            instance = new ErrorHandler(context);
        }
        return instance;
    }
    
    /**
     * Maneja un error de manera centralizada
     * 
     * @param errorType Tipo de error
     * @param severity Nivel de severidad
     * @param message Mensaje del error
     * @param throwable Excepción (opcional)
     * @param showToast Si debe mostrar toast al usuario
     */
    public void handleError(ErrorType errorType, Severity severity, String message, 
                           Throwable throwable, boolean showToast) {
        
        // Crear información del error
        ErrorInfo errorInfo = new ErrorInfo(errorType, severity, message, throwable);
        
        // Log del error
        logError(errorInfo);
        
        // Mostrar mensaje al usuario si es necesario
        if (showToast) {
            showUserMessage(errorInfo);
        }
        
        // Reportar error crítico
        if (severity == Severity.CRITICAL) {
            reportCriticalError(errorInfo);
        }
    }
    
    /**
     * Maneja un error simple (severidad media, sin toast)
     */
    public void handleError(ErrorType errorType, String message) {
        handleError(errorType, Severity.MEDIUM, message, null, false);
    }
    
    /**
     * Maneja un error con excepción
     */
    public void handleError(ErrorType errorType, String message, Throwable throwable) {
        handleError(errorType, Severity.MEDIUM, message, throwable, false);
    }
    
    /**
     * Maneja un error crítico (con toast)
     */
    public void handleCriticalError(ErrorType errorType, String message, Throwable throwable) {
        handleError(errorType, Severity.CRITICAL, message, throwable, true);
    }
    
    /**
     * Maneja un error de validación
     */
    public void handleValidationError(String message) {
        handleError(ErrorType.VALIDATION_ERROR, Severity.LOW, message, null, true);
    }
    
    /**
     * Maneja un error de base de datos
     */
    public void handleDatabaseError(String operation, Throwable throwable) {
        String message = "Error en operación de base de datos: " + operation;
        handleError(ErrorType.DATABASE_ERROR, Severity.HIGH, message, throwable, true);
    }
    
    /**
     * Maneja un error de autenticación
     */
    public void handleAuthenticationError(String message) {
        handleError(ErrorType.AUTHENTICATION_ERROR, Severity.HIGH, message, null, true);
    }
    
    /**
     * Maneja un error de red
     */
    public void handleNetworkError(String message) {
        handleError(ErrorType.NETWORK_ERROR, Severity.MEDIUM, message, null, true);
    }
    
    /**
     * Registra el error en el log
     */
    private void logError(ErrorInfo errorInfo) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        
        String logMessage = String.format(
            "[%s] %s - %s: %s",
            timestamp,
            errorInfo.getSeverity().getLevel(),
            errorInfo.getErrorType().getTitle(),
            errorInfo.getMessage()
        );
        
        // Log según la severidad
        switch (errorInfo.getSeverity()) {
            case LOW:
                Log.d(TAG, logMessage);
                break;
            case MEDIUM:
                Log.w(TAG, logMessage);
                break;
            case HIGH:
                Log.e(TAG, logMessage);
                break;
            case CRITICAL:
                Log.wtf(TAG, logMessage);
                break;
        }
        
        // Log del stack trace si existe
        if (errorInfo.getThrowable() != null) {
            Log.e(TAG, "Stack trace:", errorInfo.getThrowable());
        }
    }
    
    /**
     * Muestra un mensaje amigable al usuario
     */
    private void showUserMessage(ErrorInfo errorInfo) {
        String userMessage = getUserFriendlyMessage(errorInfo);
        
        // Mostrar toast
        Toast.makeText(context, userMessage, Toast.LENGTH_LONG).show();
        
        // Log del mensaje mostrado al usuario
        Log.i(TAG, "Mensaje mostrado al usuario: " + userMessage);
    }
    
    /**
     * Obtiene un mensaje amigable para el usuario
     */
    private String getUserFriendlyMessage(ErrorInfo errorInfo) {
        switch (errorInfo.getErrorType()) {
            case NETWORK_ERROR:
                return "Problema de conexión. Verifica tu internet e intenta nuevamente.";
                
            case DATABASE_ERROR:
                return "Error al guardar datos. Intenta nuevamente o reinicia la aplicación.";
                
            case VALIDATION_ERROR:
                return errorInfo.getMessage(); // Ya es amigable
                
            case AUTHENTICATION_ERROR:
                return "Error de autenticación. Verifica tus credenciales.";
                
            case PERMISSION_ERROR:
                return "Permisos insuficientes. Verifica la configuración de la aplicación.";
                
            case FILE_ERROR:
                return "Error al acceder a archivos. Verifica el almacenamiento.";
                
            case BUSINESS_LOGIC_ERROR:
                return "Error en la operación. Intenta nuevamente.";
                
            case UNKNOWN_ERROR:
            default:
                if (errorInfo.getSeverity() == Severity.CRITICAL) {
                    return "Error crítico. Por favor reinicia la aplicación.";
                } else {
                    return "Ha ocurrido un error. Intenta nuevamente.";
                }
        }
    }
    
    /**
     * Reporta errores críticos (para futuras implementaciones)
     */
    private void reportCriticalError(ErrorInfo errorInfo) {
        // Aquí se podría implementar el envío de reportes a un servicio externo
        // como Firebase Crashlytics, Bugsnag, etc.
        
        Log.wtf(TAG, "ERROR CRÍTICO REPORTADO: " + errorInfo.toString());
        
        // Por ahora, solo log detallado
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        errorInfo.getThrowable().printStackTrace(pw);
        
        Log.wtf(TAG, "Stack trace completo: " + sw.toString());
    }
    
    /**
     * Obtiene el mensaje de error para logging
     */
    public String getErrorMessage(Throwable throwable) {
        if (throwable == null) {
            return "Error sin mensaje";
        }
        
        String message = throwable.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = throwable.getClass().getSimpleName();
        }
        
        return message;
    }
    
    /**
     * Verifica si un error es recuperable
     */
    public boolean isRecoverable(ErrorType errorType) {
        switch (errorType) {
            case NETWORK_ERROR:
            case VALIDATION_ERROR:
            case BUSINESS_LOGIC_ERROR:
                return true;
            case DATABASE_ERROR:
            case AUTHENTICATION_ERROR:
            case PERMISSION_ERROR:
            case FILE_ERROR:
            case UNKNOWN_ERROR:
            default:
                return false;
        }
    }
    
    /**
     * Obtiene una estrategia de recuperación para el error
     */
    public String getRecoveryStrategy(ErrorType errorType) {
        switch (errorType) {
            case NETWORK_ERROR:
                return "Verificar conexión a internet y reintentar";
            case VALIDATION_ERROR:
                return "Corregir los datos ingresados";
            case DATABASE_ERROR:
                return "Reiniciar la aplicación o limpiar caché";
            case AUTHENTICATION_ERROR:
                return "Verificar credenciales o cerrar sesión";
            case PERMISSION_ERROR:
                return "Otorgar permisos necesarios en configuración";
            case FILE_ERROR:
                return "Verificar espacio de almacenamiento";
            case BUSINESS_LOGIC_ERROR:
                return "Reintentar la operación";
            case UNKNOWN_ERROR:
            default:
                return "Reiniciar la aplicación";
        }
    }
    
    /**
     * Clase para información del error
     */
    public static class ErrorInfo {
        private final ErrorType errorType;
        private final Severity severity;
        private final String message;
        private final Throwable throwable;
        private final long timestamp;
        
        public ErrorInfo(ErrorType errorType, Severity severity, String message, Throwable throwable) {
            this.errorType = errorType;
            this.severity = severity;
            this.message = message;
            this.throwable = throwable;
            this.timestamp = System.currentTimeMillis();
        }
        
        public ErrorType getErrorType() { return errorType; }
        public Severity getSeverity() { return severity; }
        public String getMessage() { return message; }
        public Throwable getThrowable() { return throwable; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("ErrorInfo{type=%s, severity=%s, message='%s', timestamp=%d}",
                    errorType, severity, message, timestamp);
        }
    }
}
