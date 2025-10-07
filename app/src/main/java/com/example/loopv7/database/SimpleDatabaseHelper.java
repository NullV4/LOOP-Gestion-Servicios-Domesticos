package com.example.loopv7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.loopv7.models.User;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SimpleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "loop_database_simple.db";
    private static final int DATABASE_VERSION = 4;

    // Tabla Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_ROLE = "role";
    private static final String COLUMN_USER_STATUS = "status";
    private static final String COLUMN_USER_CREATED_AT = "created_at";

    // Tabla Services
    private static final String TABLE_SERVICES = "services";
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_SERVICE_NAME = "name";
    private static final String COLUMN_SERVICE_DESCRIPTION = "description";
    private static final String COLUMN_SERVICE_PRICE = "price";
    private static final String COLUMN_SERVICE_DURATION = "duration";
    private static final String COLUMN_SERVICE_CATEGORY = "category";
    private static final String COLUMN_SERVICE_STATUS = "status";
    private static final String COLUMN_SERVICE_CREATED_AT = "created_at";

    // Tabla Requests
    private static final String TABLE_REQUESTS = "requests";
    private static final String COLUMN_REQUEST_ID = "id";
    private static final String COLUMN_REQUEST_CLIENT_ID = "client_id";
    private static final String COLUMN_REQUEST_SOCIA_ID = "socia_id";
    private static final String COLUMN_REQUEST_SERVICE_ID = "service_id";
    private static final String COLUMN_REQUEST_STATUS = "status";
    private static final String COLUMN_REQUEST_SCHEDULED_DATE = "scheduled_date";
    private static final String COLUMN_REQUEST_SCHEDULED_TIME = "scheduled_time";
    private static final String COLUMN_REQUEST_ADDRESS = "address";
    private static final String COLUMN_REQUEST_NOTES = "notes";
    private static final String COLUMN_REQUEST_TOTAL_PRICE = "total_price";
    private static final String COLUMN_REQUEST_PAYMENT_STATUS = "payment_status";
    private static final String COLUMN_REQUEST_RATING = "rating";
    private static final String COLUMN_REQUEST_REVIEW = "review";
    private static final String COLUMN_REQUEST_CREATED_AT = "created_at";
    private static final String COLUMN_REQUEST_UPDATED_AT = "updated_at";

    public SimpleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Users (versión simplificada)
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_USER_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_ROLE + " TEXT NOT NULL, " +
                COLUMN_USER_STATUS + " TEXT DEFAULT 'activo', " +
                COLUMN_USER_CREATED_AT + " TEXT" +
                ")";

        // Crear tabla Services
        String createServicesTable = "CREATE TABLE " + TABLE_SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SERVICE_NAME + " TEXT NOT NULL, " +
                COLUMN_SERVICE_DESCRIPTION + " TEXT, " +
                COLUMN_SERVICE_PRICE + " REAL NOT NULL, " +
                COLUMN_SERVICE_DURATION + " INTEGER NOT NULL, " +
                COLUMN_SERVICE_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_SERVICE_STATUS + " TEXT DEFAULT 'activo', " +
                COLUMN_SERVICE_CREATED_AT + " TEXT" +
                ")";

        // Crear tabla Requests
        String createRequestsTable = "CREATE TABLE " + TABLE_REQUESTS + " (" +
                COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST_CLIENT_ID + " INTEGER NOT NULL, " +
                COLUMN_REQUEST_SOCIA_ID + " INTEGER, " +
                COLUMN_REQUEST_SERVICE_ID + " INTEGER NOT NULL, " +
                COLUMN_REQUEST_STATUS + " TEXT DEFAULT 'pendiente', " +
                COLUMN_REQUEST_SCHEDULED_DATE + " TEXT NOT NULL, " +
                COLUMN_REQUEST_SCHEDULED_TIME + " TEXT NOT NULL, " +
                COLUMN_REQUEST_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_REQUEST_NOTES + " TEXT, " +
                COLUMN_REQUEST_TOTAL_PRICE + " REAL NOT NULL, " +
                COLUMN_REQUEST_PAYMENT_STATUS + " TEXT DEFAULT 'pendiente', " +
                COLUMN_REQUEST_RATING + " INTEGER, " +
                COLUMN_REQUEST_REVIEW + " TEXT, " +
                COLUMN_REQUEST_CREATED_AT + " TEXT, " +
                COLUMN_REQUEST_UPDATED_AT + " TEXT" +
                ")";

        db.execSQL(createUsersTable);
        db.execSQL(createServicesTable);
        db.execSQL(createRequestsTable);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        String currentTime = getCurrentDateTime();

        // Insertar servicios iniciales
        String[] services = {
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Limpieza General', 'Limpieza completa del hogar incluyendo todas las habitaciones', 50.00, 180, 'Limpieza', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Limpieza Profunda', 'Limpieza exhaustiva con productos especializados', 80.00, 240, 'Limpieza', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Limpieza de Cocina', 'Limpieza especializada de cocina y electrodomésticos', 35.00, 120, 'Limpieza', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Limpieza de Baños', 'Limpieza profunda de baños y sanitarios', 30.00, 90, 'Limpieza', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Planchado de Ropa', 'Planchado y doblado de ropa', 25.00, 120, 'Lavandería', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_SERVICES + " (name, description, price, duration, category, status, created_at) VALUES " +
                "('Organización de Closets', 'Organización y limpieza de armarios', 40.00, 150, 'Organización', 'activo', '" + currentTime + "')"
        };

        for (String service : services) {
            db.execSQL(service);
        }

        // Insertar usuarios de prueba
        String[] users = {
                "INSERT INTO " + TABLE_USERS + " (email, password, name, phone, role, status, created_at) VALUES " +
                "('cliente@test.com', '123456', 'María García', '555-0001', 'cliente', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_USERS + " (email, password, name, phone, role, status, created_at) VALUES " +
                "('socia@test.com', '123456', 'Ana López', '555-0002', 'socia', 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_USERS + " (email, password, name, phone, role, status, created_at) VALUES " +
                "('socia2@test.com', '123456', 'Carmen Ruiz', '555-0003', 'socia', 'activo', '" + currentTime + "')"
        };

        for (String user : users) {
            db.execSQL(user);
        }

        // Insertar solicitudes de prueba
        String[] requests = {
                // Solicitud pendiente para que las socias puedan aceptar
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, service_id, status, scheduled_date, scheduled_time, address, notes, total_price, payment_status, created_at) VALUES " +
                "(1, 1, 'pendiente', '2024-01-20', '10:00', 'Calle Principal 123, Ciudad', 'Limpieza general de la casa', 50.00, 'pendiente', '" + currentTime + "')",
                
                // Solicitud aceptada por socia, lista para pago
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, socia_id, service_id, status, scheduled_date, scheduled_time, address, notes, total_price, payment_status, created_at) VALUES " +
                "(1, 2, 2, 'aceptada', '2024-01-22', '14:00', 'Avenida Central 456, Ciudad', 'Limpieza profunda del apartamento', 80.00, 'pendiente', '" + currentTime + "')",
                
                // Solicitud completada y pagada, lista para calificar
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, socia_id, service_id, status, scheduled_date, scheduled_time, address, notes, total_price, payment_status, created_at) VALUES " +
                "(1, 2, 1, 'completada', '2024-01-18', '16:00', 'Calle de Prueba 999, Ciudad', 'Servicio completado y pagado', 50.00, 'pagado', '" + currentTime + "')",
                
                // Otra solicitud pendiente
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, service_id, status, scheduled_date, scheduled_time, address, notes, total_price, payment_status, created_at) VALUES " +
                "(1, 3, 'pendiente', '2024-01-25', '09:00', 'Calle Secundaria 789, Ciudad', 'Solo limpieza de cocina', 35.00, 'pendiente', '" + currentTime + "')"
        };

        for (String request : requests) {
            db.execSQL(request);
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Métodos para Users
    public long insertUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_ROLE, user.getRole());
            values.put(COLUMN_USER_STATUS, user.getStatus());
            values.put(COLUMN_USER_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting user: " + e.getMessage());
            return -1;
        }
    }

    public User getUserByEmail(String email) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", 
                    new String[]{email}, null, null, null);
            
            User user = null;
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
                user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_STATUS)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
                
                // Inicializar campos nuevos con valores por defecto
                user.setDescription("");
                user.setProfileImage("");
                user.setRating(0.0);
                user.setTotalRatings(0);
            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting user by email: " + e.getMessage());
            return null;
        }
    }

    public User getUserById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ID + "=?", 
                    new String[]{String.valueOf(id)}, null, null, null);
            
            User user = null;
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
                user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_STATUS)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
                
                // Inicializar campos nuevos con valores por defecto
                user.setDescription("");
                user.setProfileImage("");
                user.setRating(0.0);
                user.setTotalRatings(0);
            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting user by id: " + e.getMessage());
            return null;
        }
    }

    public List<User> getSocias() {
        List<User> socias = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ROLE + "=? AND " + COLUMN_USER_STATUS + "=?", 
                    new String[]{"socia", "activo"}, null, null, null);
            
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                    user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                    user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                    user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
                    user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
                    user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_STATUS)));
                    user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
                    
                    // Inicializar campos nuevos con valores por defecto
                    user.setDescription("");
                    user.setProfileImage("");
                    user.setRating(0.0);
                    user.setTotalRatings(0);
                    
                    socias.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting socias: " + e.getMessage());
        }
        return socias;
    }

    // Métodos para Services
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_SERVICES, null, COLUMN_SERVICE_STATUS + "=?", 
                    new String[]{"activo"}, null, null, null);
            
            if (cursor.moveToFirst()) {
                do {
                    Service service = new Service();
                    service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)));
                    service.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)));
                    service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION)));
                    service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)));
                    service.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DURATION)));
                    service.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_CATEGORY)));
                    service.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_STATUS)));
                    service.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_CREATED_AT)));
                    services.add(service);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting services: " + e.getMessage());
        }
        return services;
    }

    public Service getServiceById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_SERVICES, null, COLUMN_SERVICE_ID + "=?", 
                    new String[]{String.valueOf(id)}, null, null, null);
            
            Service service = null;
            if (cursor.moveToFirst()) {
                service = new Service();
                service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)));
                service.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DURATION)));
                service.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_CATEGORY)));
                service.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_STATUS)));
                service.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_CREATED_AT)));
            }
            cursor.close();
            db.close();
            return service;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting service by id: " + e.getMessage());
            return null;
        }
    }

    // Métodos para Requests
    public long insertRequest(Request request) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_REQUEST_CLIENT_ID, request.getClientId());
            values.put(COLUMN_REQUEST_SERVICE_ID, request.getServiceId());
            values.put(COLUMN_REQUEST_STATUS, request.getStatus());
            values.put(COLUMN_REQUEST_SCHEDULED_DATE, request.getScheduledDate());
            values.put(COLUMN_REQUEST_SCHEDULED_TIME, request.getScheduledTime());
            values.put(COLUMN_REQUEST_ADDRESS, request.getAddress());
            values.put(COLUMN_REQUEST_NOTES, request.getNotes());
            values.put(COLUMN_REQUEST_TOTAL_PRICE, request.getTotalPrice());
            values.put(COLUMN_REQUEST_PAYMENT_STATUS, request.getPaymentStatus());
            values.put(COLUMN_REQUEST_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_REQUESTS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting request: " + e.getMessage());
            return -1;
        }
    }

    public boolean updateRequest(Request request) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_REQUEST_SOCIA_ID, request.getSociaId());
            values.put(COLUMN_REQUEST_STATUS, request.getStatus());
            values.put(COLUMN_REQUEST_SCHEDULED_DATE, request.getScheduledDate());
            values.put(COLUMN_REQUEST_SCHEDULED_TIME, request.getScheduledTime());
            values.put(COLUMN_REQUEST_ADDRESS, request.getAddress());
            values.put(COLUMN_REQUEST_NOTES, request.getNotes());
            values.put(COLUMN_REQUEST_TOTAL_PRICE, request.getTotalPrice());
            values.put(COLUMN_REQUEST_PAYMENT_STATUS, request.getPaymentStatus());
            values.put(COLUMN_REQUEST_RATING, request.getRating());
            values.put(COLUMN_REQUEST_REVIEW, request.getReview());
            values.put(COLUMN_REQUEST_UPDATED_AT, getCurrentDateTime());
            
            int result = db.update(TABLE_REQUESTS, values, COLUMN_REQUEST_ID + "=?", 
                    new String[]{String.valueOf(request.getId())});
            db.close();
            return result > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating request: " + e.getMessage());
            return false;
        }
    }

    public List<Request> getRequestsByClientId(int clientId) {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("DatabaseHelper", "Querying requests for client ID: " + clientId);
            Cursor cursor = db.query(TABLE_REQUESTS, null, COLUMN_REQUEST_CLIENT_ID + "=?", 
                    new String[]{String.valueOf(clientId)}, null, null, COLUMN_REQUEST_CREATED_AT + " DESC");
            
            if (cursor.moveToFirst()) {
                do {
                    Request request = new Request();
                    request.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)));
                    request.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CLIENT_ID)));
                    request.setSociaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SOCIA_ID)));
                    request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                    request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                    request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                    request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                    request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                    request.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_NOTES)));
                    request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                    request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                    
                    // Handle nullable fields
                    int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                    int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                    int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                    
                    request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                    request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                    request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                    request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
                    requests.add(request);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("DatabaseHelper", "Found " + requests.size() + " requests for client ID: " + clientId);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting requests by client id: " + e.getMessage());
        }
        return requests;
    }

    public List<Request> getRequestsBySociaId(int sociaId) {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTS, null, COLUMN_REQUEST_SOCIA_ID + "=?", 
                    new String[]{String.valueOf(sociaId)}, null, null, COLUMN_REQUEST_CREATED_AT + " DESC");
            
            if (cursor.moveToFirst()) {
                do {
                    Request request = new Request();
                    request.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)));
                    request.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CLIENT_ID)));
                    request.setSociaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SOCIA_ID)));
                    request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                    request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                    request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                    request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                    request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                    request.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_NOTES)));
                    request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                    request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                    
                    // Handle nullable fields
                    int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                    int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                    int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                    
                    request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                    request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                    request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                    request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
                    requests.add(request);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting requests by socia id: " + e.getMessage());
        }
        return requests;
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d("DatabaseHelper", "Querying pending requests");
            Cursor cursor = db.query(TABLE_REQUESTS, null, COLUMN_REQUEST_STATUS + "=?", 
                    new String[]{"pendiente"}, null, null, COLUMN_REQUEST_CREATED_AT + " DESC");
            
            if (cursor.moveToFirst()) {
                do {
                    Request request = new Request();
                    request.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)));
                    request.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CLIENT_ID)));
                    request.setSociaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SOCIA_ID)));
                    request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                    request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                    request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                    request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                    request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                    request.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_NOTES)));
                    request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                    request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                    
                    // Handle nullable fields
                    int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                    int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                    int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                    
                    request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                    request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                    request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                    request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
                    requests.add(request);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("DatabaseHelper", "Found " + requests.size() + " pending requests");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting pending requests: " + e.getMessage());
        }
        return requests;
    }

    public Request getRequestById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTS, null, COLUMN_REQUEST_ID + "=?", 
                    new String[]{String.valueOf(id)}, null, null, null);
            
            Request request = null;
            if (cursor.moveToFirst()) {
                request = new Request();
                request.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)));
                request.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CLIENT_ID)));
                request.setSociaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SOCIA_ID)));
                request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                request.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_NOTES)));
                request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                request.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_RATING)));
                request.setReview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_REVIEW)));
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                request.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_UPDATED_AT)));
            }
            cursor.close();
            db.close();
            return request;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting request by id: " + e.getMessage());
            return null;
        }
    }
}
