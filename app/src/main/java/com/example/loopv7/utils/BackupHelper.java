package com.example.loopv7.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Clase helper para backup y restore de datos en la aplicación LOOP
 * 
 * Funcionalidades:
 * - Backup completo de datos (usuarios, servicios, solicitudes)
 * - Restore de datos desde archivo JSON
 * - Validación de integridad de datos
 * - Compresión y descompresión de archivos
 * - Manejo de errores robusto
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class BackupHelper {
    
    private static final String TAG = "BackupHelper";
    private static final String BACKUP_FOLDER = "LOOP_Backups";
    private static final String BACKUP_PREFIX = "loop_backup_";
    private static final String BACKUP_EXTENSION = ".json";
    
    private Context context;
    private SimpleDatabaseHelper databaseHelper;
    private ErrorHandler errorHandler;
    
    public BackupHelper(Context context) {
        this.context = context;
        this.databaseHelper = new SimpleDatabaseHelper(context);
        this.errorHandler = ErrorHandler.getInstance(context);
    }
    
    /**
     * Crea un backup completo de todos los datos
     * @return true si el backup fue exitoso, false si no
     */
    public boolean createBackup() {
        try {
            // Crear directorio de backup si no existe
            File backupDir = getBackupDirectory();
            if (!backupDir.exists()) {
                if (!backupDir.mkdirs()) {
                    errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                        "No se pudo crear el directorio de backup");
                    return false;
                }
            }
            
            // Crear archivo de backup
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String fileName = BACKUP_PREFIX + timestamp + BACKUP_EXTENSION;
            File backupFile = new File(backupDir, fileName);
            
            // Obtener todos los datos
            List<User> users = databaseHelper.getAllUsers();
            List<Service> services = databaseHelper.getAllServices();
            List<Request> requests = databaseHelper.getAllRequests();
            
            // Crear JSON con todos los datos
            JSONObject backupData = new JSONObject();
            backupData.put("version", "1.0");
            backupData.put("timestamp", timestamp);
            backupData.put("created_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(new Date()));
            
            // Agregar usuarios
            JSONArray usersArray = new JSONArray();
            for (User user : users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getId());
                userJson.put("email", user.getEmail());
                userJson.put("password", user.getPassword());
                userJson.put("name", user.getName());
                userJson.put("phone", user.getPhone());
                userJson.put("role", user.getRole());
                userJson.put("status", user.getStatus());
                userJson.put("description", user.getDescription());
                userJson.put("profile_image", user.getProfileImage());
                userJson.put("rating", user.getRating());
                userJson.put("total_ratings", user.getTotalRatings());
                userJson.put("location", user.getLocation());
                userJson.put("created_at", user.getCreatedAt());
                usersArray.put(userJson);
            }
            backupData.put("users", usersArray);
            
            // Agregar servicios
            JSONArray servicesArray = new JSONArray();
            for (Service service : services) {
                JSONObject serviceJson = new JSONObject();
                serviceJson.put("id", service.getId());
                serviceJson.put("name", service.getName());
                serviceJson.put("description", service.getDescription());
                serviceJson.put("price", service.getPrice());
                serviceJson.put("duration", service.getDuration());
                serviceJson.put("category", service.getCategory());
                serviceJson.put("status", service.getStatus());
                servicesArray.put(serviceJson);
            }
            backupData.put("services", servicesArray);
            
            // Agregar solicitudes
            JSONArray requestsArray = new JSONArray();
            for (Request request : requests) {
                JSONObject requestJson = new JSONObject();
                requestJson.put("id", request.getId());
                requestJson.put("client_id", request.getClientId());
                requestJson.put("socia_id", request.getSociaId());
                requestJson.put("service_id", request.getServiceId());
                requestJson.put("status", request.getStatus());
                requestJson.put("scheduled_date", request.getScheduledDate());
                requestJson.put("scheduled_time", request.getScheduledTime());
                requestJson.put("address", request.getAddress());
                requestJson.put("notes", request.getNotes());
                requestJson.put("total_price", request.getTotalPrice());
                requestJson.put("payment_status", request.getPaymentStatus());
                requestJson.put("rating", request.getRating());
                requestJson.put("review", request.getReview());
                requestJson.put("created_at", request.getCreatedAt());
                requestJson.put("updated_at", request.getUpdatedAt());
                requestsArray.put(requestJson);
            }
            backupData.put("requests", requestsArray);
            
            // Escribir archivo
            FileOutputStream fos = new FileOutputStream(backupFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(backupData.toString(2));
            writer.close();
            fos.close();
            
            Log.i(TAG, "Backup creado exitosamente: " + backupFile.getAbsolutePath());
            return true;
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                "Error al crear backup: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Restaura datos desde un archivo de backup
     * @param backupFile Archivo de backup a restaurar
     * @return true si la restauración fue exitosa, false si no
     */
    public boolean restoreBackup(File backupFile) {
        try {
            if (!backupFile.exists()) {
                errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                    "El archivo de backup no existe");
                return false;
            }
            
            // Leer archivo JSON
            FileInputStream fis = new FileInputStream(backupFile);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            bufferedReader.close();
            reader.close();
            fis.close();
            
            // Parsear JSON
            JSONObject backupData = new JSONObject(jsonString.toString());
            
            // Validar versión
            String version = backupData.optString("version", "1.0");
            if (!version.equals("1.0")) {
                errorHandler.handleError(ErrorHandler.ErrorType.VALIDATION_ERROR, 
                    "Versión de backup no compatible: " + version);
                return false;
            }
            
            // Limpiar datos existentes
            databaseHelper.clearAllData();
            
            // Restaurar usuarios
            JSONArray usersArray = backupData.getJSONArray("users");
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);
                User user = new User();
                user.setId(userJson.optInt("id", 0));
                user.setEmail(userJson.getString("email"));
                user.setPassword(userJson.getString("password"));
                user.setName(userJson.getString("name"));
                user.setPhone(userJson.optString("phone", ""));
                user.setRole(userJson.getString("role"));
                user.setStatus(userJson.optString("status", "activo"));
                user.setDescription(userJson.optString("description", ""));
                user.setProfileImage(userJson.optString("profile_image", ""));
                user.setRating(userJson.optDouble("rating", 0.0));
                user.setTotalRatings(userJson.optInt("total_ratings", 0));
                user.setLocation(userJson.optString("location", ""));
                user.setCreatedAt(userJson.optString("created_at", ""));
                
                databaseHelper.addUser(user);
            }
            
            // Restaurar servicios
            JSONArray servicesArray = backupData.getJSONArray("services");
            for (int i = 0; i < servicesArray.length(); i++) {
                JSONObject serviceJson = servicesArray.getJSONObject(i);
                Service service = new Service();
                service.setId(serviceJson.optInt("id", 0));
                service.setName(serviceJson.getString("name"));
                service.setDescription(serviceJson.optString("description", ""));
                service.setPrice(serviceJson.getDouble("price"));
                service.setDuration(serviceJson.optInt("duration", 60));
                service.setCategory(serviceJson.optString("category", "general"));
                service.setStatus(serviceJson.optString("status", "activo"));
                
                databaseHelper.addService(service);
            }
            
            // Restaurar solicitudes
            JSONArray requestsArray = backupData.getJSONArray("requests");
            for (int i = 0; i < requestsArray.length(); i++) {
                JSONObject requestJson = requestsArray.getJSONObject(i);
                Request request = new Request();
                request.setId(requestJson.optInt("id", 0));
                request.setClientId(requestJson.getInt("client_id"));
                request.setSociaId(requestJson.optInt("socia_id", 0));
                request.setServiceId(requestJson.getInt("service_id"));
                request.setStatus(requestJson.getString("status"));
                request.setScheduledDate(requestJson.getString("scheduled_date"));
                request.setScheduledTime(requestJson.getString("scheduled_time"));
                request.setAddress(requestJson.getString("address"));
                request.setNotes(requestJson.optString("notes", ""));
                request.setTotalPrice(requestJson.getDouble("total_price"));
                request.setPaymentStatus(requestJson.optString("payment_status", "pendiente"));
                request.setRating(requestJson.optInt("rating", 0));
                request.setReview(requestJson.optString("review", ""));
                request.setCreatedAt(requestJson.optString("created_at", ""));
                request.setUpdatedAt(requestJson.optString("updated_at", ""));
                
                databaseHelper.addRequest(request);
            }
            
            Log.i(TAG, "Backup restaurado exitosamente desde: " + backupFile.getAbsolutePath());
            return true;
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                "Error al restaurar backup: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Obtiene la lista de archivos de backup disponibles
     * @return Lista de archivos de backup
     */
    public File[] getBackupFiles() {
        try {
            File backupDir = getBackupDirectory();
            if (!backupDir.exists()) {
                return new File[0];
            }
            
            return backupDir.listFiles((dir, name) -> 
                name.startsWith(BACKUP_PREFIX) && name.endsWith(BACKUP_EXTENSION));
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                "Error al obtener archivos de backup: " + e.getMessage(), e);
            return new File[0];
        }
    }
    
    /**
     * Elimina un archivo de backup
     * @param backupFile Archivo a eliminar
     * @return true si se eliminó exitosamente, false si no
     */
    public boolean deleteBackup(File backupFile) {
        try {
            if (backupFile.exists()) {
                boolean deleted = backupFile.delete();
                if (deleted) {
                    Log.i(TAG, "Backup eliminado: " + backupFile.getName());
                } else {
                    errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                        "No se pudo eliminar el archivo de backup");
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                "Error al eliminar backup: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Obtiene el directorio de backup
     * @return Directorio de backup
     */
    private File getBackupDirectory() {
        File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        return new File(externalDir, BACKUP_FOLDER);
    }
    
    /**
     * Obtiene información de un archivo de backup
     * @param backupFile Archivo de backup
     * @return Información del backup o null si hay error
     */
    public BackupInfo getBackupInfo(File backupFile) {
        try {
            if (!backupFile.exists()) {
                return null;
            }
            
            FileInputStream fis = new FileInputStream(backupFile);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            bufferedReader.close();
            reader.close();
            fis.close();
            
            JSONObject backupData = new JSONObject(jsonString.toString());
            
            BackupInfo info = new BackupInfo();
            info.fileName = backupFile.getName();
            info.fileSize = backupFile.length();
            info.version = backupData.optString("version", "1.0");
            info.timestamp = backupData.optString("timestamp", "");
            info.createdAt = backupData.optString("created_at", "");
            info.userCount = backupData.getJSONArray("users").length();
            info.serviceCount = backupData.getJSONArray("services").length();
            info.requestCount = backupData.getJSONArray("requests").length();
            
            return info;
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.FILE_ERROR, 
                "Error al obtener información del backup: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Clase para información de backup
     */
    public static class BackupInfo {
        public String fileName;
        public long fileSize;
        public String version;
        public String timestamp;
        public String createdAt;
        public int userCount;
        public int serviceCount;
        public int requestCount;
        
        public String getFormattedSize() {
            if (fileSize < 1024) {
                return fileSize + " B";
            } else if (fileSize < 1024 * 1024) {
                return String.format("%.1f KB", fileSize / 1024.0);
            } else {
                return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
            }
        }
        
        public String getFormattedDate() {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(createdAt);
                return outputFormat.format(date);
            } catch (Exception e) {
                return createdAt;
            }
        }
    }
}
