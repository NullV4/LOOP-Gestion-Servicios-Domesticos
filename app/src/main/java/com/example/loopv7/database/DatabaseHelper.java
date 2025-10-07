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

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "loop_database.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_ROLE = "role";
    private static final String COLUMN_USER_STATUS = "status";
    private static final String COLUMN_USER_DESCRIPTION = "description";
    private static final String COLUMN_USER_PROFILE_IMAGE = "profile_image";
    private static final String COLUMN_USER_RATING = "rating";
    private static final String COLUMN_USER_TOTAL_RATINGS = "total_ratings";
    private static final String COLUMN_USER_CREATED_AT = "created_at";
    private static final String COLUMN_USER_UPDATED_AT = "updated_at";

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_USER_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_ROLE + " TEXT NOT NULL, " +
                COLUMN_USER_STATUS + " TEXT DEFAULT 'activo', " +
                COLUMN_USER_DESCRIPTION + " TEXT, " +
                COLUMN_USER_PROFILE_IMAGE + " TEXT, " +
                COLUMN_USER_RATING + " REAL DEFAULT 0, " +
                COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0, " +
                COLUMN_USER_CREATED_AT + " TEXT, " +
                COLUMN_USER_UPDATED_AT + " TEXT" +
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
                COLUMN_REQUEST_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_REQUEST_CLIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REQUEST_SOCIA_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REQUEST_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + ")" +
                ")";

        db.execSQL(createUsersTable);
        db.execSQL(createServicesTable);
        db.execSQL(createRequestsTable);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Agregar nuevas columnas a la tabla users
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_DESCRIPTION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PROFILE_IMAGE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_RATING + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0");
        }
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
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(1).equals(columnName)) {
                    cursor.close();
                    return true;
                }
            }
            cursor.close();
        }
        return false;
    }

    // Métodos para Users
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_STATUS, user.getStatus());
        values.put(COLUMN_USER_DESCRIPTION, user.getDescription() != null ? user.getDescription() : "");
        values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage() != null ? user.getProfileImage() : "");
        values.put(COLUMN_USER_RATING, user.getRating());
        values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
        values.put(COLUMN_USER_CREATED_AT, getCurrentDateTime());
        
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public User getUserByEmail(String email) {
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
            // Manejar columnas que podrían no existir en versiones anteriores
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                user.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_DESCRIPTION)));
            } else {
                user.setDescription("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                user.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PROFILE_IMAGE)));
            } else {
                user.setProfileImage("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                user.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_RATING)));
            } else {
                user.setRating(0.0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                user.setTotalRatings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_TOTAL_RATINGS)));
            } else {
                user.setTotalRatings(0);
            }
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
            user.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_UPDATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public User getUserById(int id) {
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
            // Manejar columnas que podrían no existir en versiones anteriores
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                user.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_DESCRIPTION)));
            } else {
                user.setDescription("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                user.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PROFILE_IMAGE)));
            } else {
                user.setProfileImage("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                user.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_RATING)));
            } else {
                user.setRating(0.0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                user.setTotalRatings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_TOTAL_RATINGS)));
            } else {
                user.setTotalRatings(0);
            }
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
            user.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_UPDATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_STATUS, user.getStatus());
        values.put(COLUMN_USER_DESCRIPTION, user.getDescription());
        values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage());
        values.put(COLUMN_USER_RATING, user.getRating());
        values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
        values.put(COLUMN_USER_UPDATED_AT, getCurrentDateTime());
        
        int result = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", 
                new String[]{String.valueOf(user.getId())});
        db.close();
        return result > 0;
    }

    public List<User> getSocias() {
        List<User> socias = new ArrayList<>();
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
                socias.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return socias;
    }

    // Métodos para Services
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
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
        return services;
    }

    public Service getServiceById(int id) {
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
    }

    // Métodos para Requests
    public long insertRequest(Request request) {
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
    }

    public boolean updateRequest(Request request) {
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
    }

    public List<Request> getRequestsByClientId(int clientId) {
        List<Request> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
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
                request.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_RATING)));
                request.setReview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_REVIEW)));
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                request.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_UPDATED_AT)));
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return requests;
    }

    public List<Request> getRequestsBySociaId(int sociaId) {
        List<Request> requests = new ArrayList<>();
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
                request.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_RATING)));
                request.setReview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_REVIEW)));
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                request.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_UPDATED_AT)));
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return requests;
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
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
                request.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_RATING)));
                request.setReview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_REVIEW)));
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                request.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_UPDATED_AT)));
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return requests;
    }

    public Request getRequestById(int id) {
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
    }
}
