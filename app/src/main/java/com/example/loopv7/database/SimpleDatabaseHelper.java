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
import com.example.loopv7.models.Category;
import com.example.loopv7.models.Payment;
import com.example.loopv7.models.Rating;
import com.example.loopv7.models.Notification;
import com.example.loopv7.utils.ErrorHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SimpleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "loop_database_simple.db";
    private static final int DATABASE_VERSION = 7;
    
    private ErrorHandler errorHandler;

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
    private static final String COLUMN_USER_LOCATION = "location";
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
    private static final String COLUMN_REQUEST_IS_ARCHIVED = "is_archived";
    private static final String COLUMN_REQUEST_CREATED_AT = "created_at";
    private static final String COLUMN_REQUEST_UPDATED_AT = "updated_at";

    // Tabla Categories
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY_PARENT_ID = "parent_id";
    private static final String COLUMN_CATEGORY_ICON = "icon";
    private static final String COLUMN_CATEGORY_COLOR = "color";
    private static final String COLUMN_CATEGORY_SORT_ORDER = "sort_order";
    private static final String COLUMN_CATEGORY_STATUS = "status";
    private static final String COLUMN_CATEGORY_CREATED_AT = "created_at";

    // Tabla Payments
    private static final String TABLE_PAYMENTS = "payments";
    private static final String COLUMN_PAYMENT_ID = "id";
    private static final String COLUMN_PAYMENT_REQUEST_ID = "request_id";
    private static final String COLUMN_PAYMENT_AMOUNT = "amount";
    private static final String COLUMN_PAYMENT_CURRENCY = "currency";
    private static final String COLUMN_PAYMENT_METHOD = "payment_method";
    private static final String COLUMN_PAYMENT_STATUS = "payment_status";
    private static final String COLUMN_PAYMENT_TRANSACTION_ID = "transaction_id";
    private static final String COLUMN_PAYMENT_GATEWAY_RESPONSE = "gateway_response";
    private static final String COLUMN_PAYMENT_GATEWAY_NAME = "gateway_name";
    private static final String COLUMN_PAYMENT_DATE = "payment_date";
    private static final String COLUMN_PAYMENT_PROCESSED_AT = "processed_at";
    private static final String COLUMN_PAYMENT_REFUNDED_AT = "refunded_at";
    private static final String COLUMN_PAYMENT_NOTES = "notes";
    private static final String COLUMN_PAYMENT_CREATED_AT = "created_at";

    // Tabla Ratings
    private static final String TABLE_RATINGS = "ratings";
    private static final String COLUMN_RATING_ID = "id";
    private static final String COLUMN_RATING_REQUEST_ID = "request_id";
    private static final String COLUMN_RATING_RATER_ID = "rater_id";
    private static final String COLUMN_RATING_RATED_ID = "rated_id";
    private static final String COLUMN_RATING_OVERALL = "overall_rating";
    private static final String COLUMN_RATING_QUALITY = "quality_rating";
    private static final String COLUMN_RATING_PUNCTUALITY = "punctuality_rating";
    private static final String COLUMN_RATING_COMMUNICATION = "communication_rating";
    private static final String COLUMN_RATING_CLEANLINESS = "cleanliness_rating";
    private static final String COLUMN_RATING_REVIEW = "review";
    private static final String COLUMN_RATING_IS_ANONYMOUS = "is_anonymous";
    private static final String COLUMN_RATING_STATUS = "status";
    private static final String COLUMN_RATING_CREATED_AT = "created_at";

    // Tabla Notifications
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIFICATION_ID = "id";
    private static final String COLUMN_NOTIFICATION_USER_ID = "user_id";
    private static final String COLUMN_NOTIFICATION_TITLE = "title";
    private static final String COLUMN_NOTIFICATION_MESSAGE = "message";
    private static final String COLUMN_NOTIFICATION_TYPE = "type";
    private static final String COLUMN_NOTIFICATION_CATEGORY = "category";
    private static final String COLUMN_NOTIFICATION_REFERENCE_TYPE = "reference_type";
    private static final String COLUMN_NOTIFICATION_REFERENCE_ID = "reference_id";
    private static final String COLUMN_NOTIFICATION_IS_READ = "is_read";
    private static final String COLUMN_NOTIFICATION_IS_SENT = "is_sent";
    private static final String COLUMN_NOTIFICATION_SENT_AT = "sent_at";
    private static final String COLUMN_NOTIFICATION_READ_AT = "read_at";
    private static final String COLUMN_NOTIFICATION_EXPIRES_AT = "expires_at";
    private static final String COLUMN_NOTIFICATION_CREATED_AT = "created_at";

    public SimpleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.errorHandler = ErrorHandler.getInstance(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Users (versión enriquecida)
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
                COLUMN_USER_RATING + " REAL DEFAULT 0.0, " +
                COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0, " +
                COLUMN_USER_LOCATION + " TEXT, " +
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
                COLUMN_REQUEST_IS_ARCHIVED + " INTEGER DEFAULT 0, " +
                COLUMN_REQUEST_CREATED_AT + " TEXT, " +
                COLUMN_REQUEST_UPDATED_AT + " TEXT" +
                ")";

        // Crear tabla Categories
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
                COLUMN_CATEGORY_PARENT_ID + " INTEGER, " +
                COLUMN_CATEGORY_ICON + " TEXT, " +
                COLUMN_CATEGORY_COLOR + " TEXT, " +
                COLUMN_CATEGORY_SORT_ORDER + " INTEGER DEFAULT 0, " +
                COLUMN_CATEGORY_STATUS + " TEXT DEFAULT 'activo', " +
                COLUMN_CATEGORY_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_PARENT_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
                ")";

        // Crear tabla Payments
        String createPaymentsTable = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PAYMENT_REQUEST_ID + " INTEGER NOT NULL, " +
                COLUMN_PAYMENT_AMOUNT + " REAL NOT NULL, " +
                COLUMN_PAYMENT_CURRENCY + " TEXT DEFAULT 'MXN', " +
                COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, " +
                COLUMN_PAYMENT_STATUS + " TEXT DEFAULT 'pendiente', " +
                COLUMN_PAYMENT_TRANSACTION_ID + " TEXT, " +
                COLUMN_PAYMENT_GATEWAY_RESPONSE + " TEXT, " +
                COLUMN_PAYMENT_GATEWAY_NAME + " TEXT, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_PAYMENT_PROCESSED_AT + " TEXT, " +
                COLUMN_PAYMENT_REFUNDED_AT + " TEXT, " +
                COLUMN_PAYMENT_NOTES + " TEXT, " +
                COLUMN_PAYMENT_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PAYMENT_REQUEST_ID + ") REFERENCES " + TABLE_REQUESTS + "(" + COLUMN_REQUEST_ID + ") ON DELETE CASCADE" +
                ")";

        // Crear tabla Ratings
        String createRatingsTable = "CREATE TABLE " + TABLE_RATINGS + " (" +
                COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RATING_REQUEST_ID + " INTEGER NOT NULL, " +
                COLUMN_RATING_RATER_ID + " INTEGER NOT NULL, " +
                COLUMN_RATING_RATED_ID + " INTEGER NOT NULL, " +
                COLUMN_RATING_OVERALL + " INTEGER CHECK (" + COLUMN_RATING_OVERALL + " >= 1 AND " + COLUMN_RATING_OVERALL + " <= 5), " +
                COLUMN_RATING_QUALITY + " INTEGER CHECK (" + COLUMN_RATING_QUALITY + " >= 1 AND " + COLUMN_RATING_QUALITY + " <= 5), " +
                COLUMN_RATING_PUNCTUALITY + " INTEGER CHECK (" + COLUMN_RATING_PUNCTUALITY + " >= 1 AND " + COLUMN_RATING_PUNCTUALITY + " <= 5), " +
                COLUMN_RATING_COMMUNICATION + " INTEGER CHECK (" + COLUMN_RATING_COMMUNICATION + " >= 1 AND " + COLUMN_RATING_COMMUNICATION + " <= 5), " +
                COLUMN_RATING_CLEANLINESS + " INTEGER CHECK (" + COLUMN_RATING_CLEANLINESS + " >= 1 AND " + COLUMN_RATING_CLEANLINESS + " <= 5), " +
                COLUMN_RATING_REVIEW + " TEXT, " +
                COLUMN_RATING_IS_ANONYMOUS + " INTEGER DEFAULT 0, " +
                COLUMN_RATING_STATUS + " TEXT DEFAULT 'activo', " +
                COLUMN_RATING_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_RATING_REQUEST_ID + ") REFERENCES " + TABLE_REQUESTS + "(" + COLUMN_REQUEST_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_RATING_RATER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_RATING_RATED_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE" +
                ")";

        // Crear tabla Notifications
        String createNotificationsTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTIFICATION_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_NOTIFICATION_TITLE + " TEXT NOT NULL, " +
                COLUMN_NOTIFICATION_MESSAGE + " TEXT NOT NULL, " +
                COLUMN_NOTIFICATION_TYPE + " TEXT DEFAULT 'info', " +
                COLUMN_NOTIFICATION_CATEGORY + " TEXT DEFAULT 'system', " +
                COLUMN_NOTIFICATION_REFERENCE_TYPE + " TEXT, " +
                COLUMN_NOTIFICATION_REFERENCE_ID + " INTEGER, " +
                COLUMN_NOTIFICATION_IS_READ + " INTEGER DEFAULT 0, " +
                COLUMN_NOTIFICATION_IS_SENT + " INTEGER DEFAULT 0, " +
                COLUMN_NOTIFICATION_SENT_AT + " TEXT, " +
                COLUMN_NOTIFICATION_READ_AT + " TEXT, " +
                COLUMN_NOTIFICATION_EXPIRES_AT + " TEXT, " +
                COLUMN_NOTIFICATION_CREATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_NOTIFICATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE" +
                ")";

        db.execSQL(createUsersTable);
        db.execSQL(createServicesTable);
        db.execSQL(createRequestsTable);
        db.execSQL(createCategoriesTable);
        db.execSQL(createPaymentsTable);
        db.execSQL(createRatingsTable);
        db.execSQL(createNotificationsTable);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            // Agregar nuevas columnas a la tabla users
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_DESCRIPTION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PROFILE_IMAGE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_RATING + " REAL DEFAULT 0.0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_LOCATION + " TEXT");
            
            Log.d("SimpleDatabaseHelper", "Base de datos actualizada a versión 5 - Perfiles enriquecidos agregados");
        }
        
        if (oldVersion < 7) {
            // Agregar campo de archivado a la tabla requests
            db.execSQL("ALTER TABLE " + TABLE_REQUESTS + " ADD COLUMN " + COLUMN_REQUEST_IS_ARCHIVED + " INTEGER DEFAULT 0");
            Log.d("SimpleDatabaseHelper", "Base de datos actualizada a versión 7 - Campo is_archived agregado");
        }
        
        if (oldVersion < 6) {
            // Crear nuevas tablas para la versión 6
            String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
                    COLUMN_CATEGORY_PARENT_ID + " INTEGER, " +
                    COLUMN_CATEGORY_ICON + " TEXT, " +
                    COLUMN_CATEGORY_COLOR + " TEXT, " +
                    COLUMN_CATEGORY_SORT_ORDER + " INTEGER DEFAULT 0, " +
                    COLUMN_CATEGORY_STATUS + " TEXT DEFAULT 'activo', " +
                    COLUMN_CATEGORY_CREATED_AT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_CATEGORY_PARENT_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
                    ")";

            String createPaymentsTable = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
                    COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PAYMENT_REQUEST_ID + " INTEGER NOT NULL, " +
                    COLUMN_PAYMENT_AMOUNT + " REAL NOT NULL, " +
                    COLUMN_PAYMENT_CURRENCY + " TEXT DEFAULT 'MXN', " +
                    COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, " +
                    COLUMN_PAYMENT_STATUS + " TEXT DEFAULT 'pendiente', " +
                    COLUMN_PAYMENT_TRANSACTION_ID + " TEXT, " +
                    COLUMN_PAYMENT_GATEWAY_RESPONSE + " TEXT, " +
                    COLUMN_PAYMENT_GATEWAY_NAME + " TEXT, " +
                    COLUMN_PAYMENT_DATE + " TEXT, " +
                    COLUMN_PAYMENT_PROCESSED_AT + " TEXT, " +
                    COLUMN_PAYMENT_REFUNDED_AT + " TEXT, " +
                    COLUMN_PAYMENT_NOTES + " TEXT, " +
                    COLUMN_PAYMENT_CREATED_AT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_PAYMENT_REQUEST_ID + ") REFERENCES " + TABLE_REQUESTS + "(" + COLUMN_REQUEST_ID + ") ON DELETE CASCADE" +
                    ")";

            String createRatingsTable = "CREATE TABLE " + TABLE_RATINGS + " (" +
                    COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING_REQUEST_ID + " INTEGER NOT NULL, " +
                    COLUMN_RATING_RATER_ID + " INTEGER NOT NULL, " +
                    COLUMN_RATING_RATED_ID + " INTEGER NOT NULL, " +
                    COLUMN_RATING_OVERALL + " INTEGER CHECK (" + COLUMN_RATING_OVERALL + " >= 1 AND " + COLUMN_RATING_OVERALL + " <= 5), " +
                    COLUMN_RATING_QUALITY + " INTEGER CHECK (" + COLUMN_RATING_QUALITY + " >= 1 AND " + COLUMN_RATING_QUALITY + " <= 5), " +
                    COLUMN_RATING_PUNCTUALITY + " INTEGER CHECK (" + COLUMN_RATING_PUNCTUALITY + " >= 1 AND " + COLUMN_RATING_PUNCTUALITY + " <= 5), " +
                    COLUMN_RATING_COMMUNICATION + " INTEGER CHECK (" + COLUMN_RATING_COMMUNICATION + " >= 1 AND " + COLUMN_RATING_COMMUNICATION + " <= 5), " +
                    COLUMN_RATING_CLEANLINESS + " INTEGER CHECK (" + COLUMN_RATING_CLEANLINESS + " >= 1 AND " + COLUMN_RATING_CLEANLINESS + " <= 5), " +
                    COLUMN_RATING_REVIEW + " TEXT, " +
                    COLUMN_RATING_IS_ANONYMOUS + " INTEGER DEFAULT 0, " +
                    COLUMN_RATING_STATUS + " TEXT DEFAULT 'activo', " +
                    COLUMN_RATING_CREATED_AT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_RATING_REQUEST_ID + ") REFERENCES " + TABLE_REQUESTS + "(" + COLUMN_REQUEST_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_RATING_RATER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_RATING_RATED_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE" +
                    ")";

            String createNotificationsTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                    COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTIFICATION_USER_ID + " INTEGER NOT NULL, " +
                    COLUMN_NOTIFICATION_TITLE + " TEXT NOT NULL, " +
                    COLUMN_NOTIFICATION_MESSAGE + " TEXT NOT NULL, " +
                    COLUMN_NOTIFICATION_TYPE + " TEXT DEFAULT 'info', " +
                    COLUMN_NOTIFICATION_CATEGORY + " TEXT DEFAULT 'system', " +
                    COLUMN_NOTIFICATION_REFERENCE_TYPE + " TEXT, " +
                    COLUMN_NOTIFICATION_REFERENCE_ID + " INTEGER, " +
                    COLUMN_NOTIFICATION_IS_READ + " INTEGER DEFAULT 0, " +
                    COLUMN_NOTIFICATION_IS_SENT + " INTEGER DEFAULT 0, " +
                    COLUMN_NOTIFICATION_SENT_AT + " TEXT, " +
                    COLUMN_NOTIFICATION_READ_AT + " TEXT, " +
                    COLUMN_NOTIFICATION_EXPIRES_AT + " TEXT, " +
                    COLUMN_NOTIFICATION_CREATED_AT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_NOTIFICATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE" +
                    ")";

            db.execSQL(createCategoriesTable);
            db.execSQL(createPaymentsTable);
            db.execSQL(createRatingsTable);
            db.execSQL(createNotificationsTable);
            
            // Insertar datos iniciales para las nuevas tablas
            insertNewTablesInitialData(db);
            
            Log.d("SimpleDatabaseHelper", "Base de datos actualizada a versión 6 - Nuevas tablas agregadas");
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

    private void insertNewTablesInitialData(SQLiteDatabase db) {
        String currentTime = getCurrentDateTime();

        // Insertar categorías iniciales
        String[] categories = {
                // Categorías principales
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Limpieza', 'Servicios de limpieza del hogar', NULL, 'cleaning', '#4CAF50', 1, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Lavandería', 'Servicios de lavado y planchado', NULL, 'laundry', '#2196F3', 2, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Organización', 'Servicios de organización y orden', NULL, 'organization', '#FF9800', 3, 'activo', '" + currentTime + "')",

                // Subcategorías de Limpieza
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Limpieza General', 'Limpieza básica del hogar', 1, 'general_cleaning', '#4CAF50', 1, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Limpieza Profunda', 'Limpieza exhaustiva y detallada', 1, 'deep_cleaning', '#4CAF50', 2, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Limpieza de Cocina', 'Limpieza especializada de cocina', 1, 'kitchen_cleaning', '#4CAF50', 3, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Limpieza de Baños', 'Limpieza de baños y sanitarios', 1, 'bathroom_cleaning', '#4CAF50', 4, 'activo', '" + currentTime + "')",

                // Subcategorías de Lavandería
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Planchado', 'Planchado y doblado de ropa', 2, 'ironing', '#2196F3', 1, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Lavado', 'Lavado de ropa y textiles', 2, 'washing', '#2196F3', 2, 'activo', '" + currentTime + "')",

                // Subcategorías de Organización
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Organización de Closets', 'Organización de armarios y closets', 3, 'closet_organization', '#FF9800', 1, 'activo', '" + currentTime + "')",
                "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                "('Organización de Oficina', 'Organización de espacios de trabajo', 3, 'office_organization', '#FF9800', 2, 'activo', '" + currentTime + "')"
        };

        for (String category : categories) {
            db.execSQL(category);
        }

        // Insertar pagos de prueba
        String[] payments = {
                "INSERT INTO " + TABLE_PAYMENTS + " (request_id, amount, currency, payment_method, payment_status, transaction_id, payment_date, created_at) VALUES " +
                "(3, 50.00, 'MXN', 'efectivo', 'completado', 'TXN-001', '" + currentTime + "', '" + currentTime + "')",
                "INSERT INTO " + TABLE_PAYMENTS + " (request_id, amount, currency, payment_method, payment_status, created_at) VALUES " +
                "(2, 80.00, 'MXN', 'tarjeta_credito', 'pendiente', '" + currentTime + "')"
        };

        for (String payment : payments) {
            db.execSQL(payment);
        }

        // Insertar calificaciones de prueba
        String[] ratings = {
                "INSERT INTO " + TABLE_RATINGS + " (request_id, rater_id, rated_id, overall_rating, quality_rating, punctuality_rating, communication_rating, cleanliness_rating, review, status, created_at) VALUES " +
                "(3, 1, 2, 5, 5, 5, 4, 5, 'Excelente servicio, muy puntual y limpia', 'activo', '" + currentTime + "')"
        };

        for (String rating : ratings) {
            db.execSQL(rating);
        }

        // Insertar notificaciones de prueba
        String[] notifications = {
                "INSERT INTO " + TABLE_NOTIFICATIONS + " (user_id, title, message, type, category, reference_type, reference_id, is_read, created_at) VALUES " +
                "(1, 'Solicitud Aceptada', 'Tu solicitud de limpieza ha sido aceptada por Ana López', 'success', 'request', 'request', 2, 0, '" + currentTime + "')",
                "INSERT INTO " + TABLE_NOTIFICATIONS + " (user_id, title, message, type, category, reference_type, reference_id, is_read, created_at) VALUES " +
                "(2, 'Nueva Solicitud', 'Tienes una nueva solicitud de limpieza pendiente', 'info', 'request', 'request', 1, 0, '" + currentTime + "')",
                "INSERT INTO " + TABLE_NOTIFICATIONS + " (user_id, title, message, type, category, reference_type, reference_id, is_read, created_at) VALUES " +
                "(1, 'Promoción Disponible', 'Usa el código BIENVENIDA20 para obtener 20% de descuento', 'promotion', 'promotion', 'promotion', 1, 0, '" + currentTime + "')"
        };

        for (String notification : notifications) {
            db.execSQL(notification);
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
            values.put(COLUMN_USER_DESCRIPTION, user.getDescription());
            values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage());
            values.put(COLUMN_USER_RATING, user.getRating());
            values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
            values.put(COLUMN_USER_LOCATION, user.getLocation());
            values.put(COLUMN_USER_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("insertUser", e);
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
            errorHandler.handleDatabaseError("getUserByEmail", e);
            return null;
        }
    }
    
    /**
     * Verifica si un email ya existe en la base de datos
     * @param email Email a verificar
     * @return true si el email existe, false si no
     */
    public boolean emailExists(String email) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID}, 
                    COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
            
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
            
        } catch (Exception e) {
            errorHandler.handleDatabaseError("emailExists", e);
            return false;
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
            values.put(COLUMN_REQUEST_IS_ARCHIVED, request.isArchived() ? 1 : 0);
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
            errorHandler.handleDatabaseError("getRequestById", e);
            return null;
        }
    }
    
    // Métodos para backup/restore
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);
            
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
                user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_STATUS)));
                user.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_DESCRIPTION)));
                user.setProfileImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PROFILE_IMAGE)));
                user.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_RATING)));
                user.setTotalRatings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_TOTAL_RATINGS)));
                user.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LOCATION)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
                users.add(user);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            errorHandler.handleDatabaseError("getAllUsers", e);
        }
        return users;
    }
    
    
    /**
     * Obtiene todas las solicitudes
     * @return Lista de todas las solicitudes
     */
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTS, null, null, null, null, null, null);
            
            while (cursor.moveToNext()) {
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
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            errorHandler.handleDatabaseError("getAllRequests", e);
        }
        return requests;
    }
    
    /**
     * Limpia todos los datos de la base de datos
     */
    public void clearAllData() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NOTIFICATIONS, null, null);
            db.delete(TABLE_RATINGS, null, null);
            db.delete(TABLE_PAYMENTS, null, null);
            db.delete(TABLE_REQUESTS, null, null);
            db.delete(TABLE_SERVICES, null, null);
            db.delete(TABLE_CATEGORIES, null, null);
            db.delete(TABLE_USERS, null, null);
            db.close();
            Log.i("DatabaseHelper", "Todos los datos han sido eliminados");
        } catch (Exception e) {
            errorHandler.handleDatabaseError("clearAllData", e);
        }
    }
    
    /**
     * Agrega un usuario (para restore)
     * @param user Usuario a agregar
     * @return ID del usuario insertado
     */
    public long addUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_USER_ID, user.getId());
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_ROLE, user.getRole());
            values.put(COLUMN_USER_STATUS, user.getStatus());
            values.put(COLUMN_USER_DESCRIPTION, user.getDescription());
            values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage());
            values.put(COLUMN_USER_RATING, user.getRating());
            values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
            values.put(COLUMN_USER_LOCATION, user.getLocation());
            values.put(COLUMN_USER_CREATED_AT, user.getCreatedAt());
            
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("addUser", e);
            return -1;
        }
    }
    
    /**
     * Agrega un servicio (para restore)
     * @param service Servicio a agregar
     * @return ID del servicio insertado
     */
    public long addService(Service service) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_SERVICE_ID, service.getId());
            values.put(COLUMN_SERVICE_NAME, service.getName());
            values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
            values.put(COLUMN_SERVICE_PRICE, service.getPrice());
            values.put(COLUMN_SERVICE_DURATION, service.getDuration());
            values.put(COLUMN_SERVICE_CATEGORY, service.getCategory());
            values.put(COLUMN_SERVICE_STATUS, service.getStatus());
            
            long result = db.insert(TABLE_SERVICES, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("addService", e);
            return -1;
        }
    }
    
    /**
     * Agrega una solicitud (para restore)
     * @param request Solicitud a agregar
     * @return ID de la solicitud insertada
     */
    public long addRequest(Request request) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_REQUEST_ID, request.getId());
            values.put(COLUMN_REQUEST_CLIENT_ID, request.getClientId());
            values.put(COLUMN_REQUEST_SOCIA_ID, request.getSociaId());
            values.put(COLUMN_REQUEST_SERVICE_ID, request.getServiceId());
            values.put(COLUMN_REQUEST_STATUS, request.getStatus());
            values.put(COLUMN_REQUEST_SCHEDULED_DATE, request.getScheduledDate());
            values.put(COLUMN_REQUEST_SCHEDULED_TIME, request.getScheduledTime());
            values.put(COLUMN_REQUEST_ADDRESS, request.getAddress());
            values.put(COLUMN_REQUEST_NOTES, request.getNotes());
            values.put(COLUMN_REQUEST_TOTAL_PRICE, request.getTotalPrice());
            values.put(COLUMN_REQUEST_PAYMENT_STATUS, request.getPaymentStatus());
            values.put(COLUMN_REQUEST_RATING, request.getRating());
            values.put(COLUMN_REQUEST_REVIEW, request.getReview());
            values.put(COLUMN_REQUEST_CREATED_AT, request.getCreatedAt());
            values.put(COLUMN_REQUEST_UPDATED_AT, request.getUpdatedAt());
            
            long result = db.insert(TABLE_REQUESTS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("addRequest", e);
            return -1;
        }
    }
    
    /**
     * Actualiza un usuario existente
     * @param user Usuario con los datos actualizados
     * @return true si se actualizó exitosamente, false si no
     */
    public boolean updateUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_DESCRIPTION, user.getDescription());
            values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage());
            values.put(COLUMN_USER_LOCATION, user.getLocation());
            
            int result = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", 
                    new String[]{String.valueOf(user.getId())});
            db.close();
            return result > 0;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("updateUser", e);
            return false;
        }
    }

    // =====================================================
    // MÉTODOS CRUD FALTANTES
    // =====================================================

    /**
     * Inserta un nuevo servicio
     * @param service Servicio a insertar
     * @return ID del servicio insertado o -1 si falló
     */
    public long insertService(Service service) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_SERVICE_NAME, service.getName());
            values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
            values.put(COLUMN_SERVICE_PRICE, service.getPrice());
            values.put(COLUMN_SERVICE_DURATION, service.getDuration());
            values.put(COLUMN_SERVICE_CATEGORY, service.getCategory());
            values.put(COLUMN_SERVICE_STATUS, service.getStatus());
            values.put(COLUMN_SERVICE_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_SERVICES, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("insertService", e);
            return -1;
        }
    }

    /**
     * Actualiza un servicio existente
     * @param service Servicio con los datos actualizados
     * @return true si se actualizó exitosamente, false si no
     */
    public boolean updateService(Service service) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_SERVICE_NAME, service.getName());
            values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
            values.put(COLUMN_SERVICE_PRICE, service.getPrice());
            values.put(COLUMN_SERVICE_DURATION, service.getDuration());
            values.put(COLUMN_SERVICE_CATEGORY, service.getCategory());
            values.put(COLUMN_SERVICE_STATUS, service.getStatus());
            
            int result = db.update(TABLE_SERVICES, values, COLUMN_SERVICE_ID + "=?", 
                    new String[]{String.valueOf(service.getId())});
            db.close();
            return result > 0;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("updateService", e);
            return false;
        }
    }

    /**
     * Elimina una solicitud y todos sus datos relacionados
     * @param requestId ID de la solicitud a eliminar
     * @return true si se eliminó exitosamente, false si no
     */
    public boolean deleteRequest(int requestId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            
            // Eliminar pagos relacionados
            db.delete(TABLE_PAYMENTS, COLUMN_PAYMENT_REQUEST_ID + "=?", 
                    new String[]{String.valueOf(requestId)});
            
            // Eliminar calificaciones relacionadas
            db.delete(TABLE_RATINGS, COLUMN_RATING_REQUEST_ID + "=?", 
                    new String[]{String.valueOf(requestId)});
            
            // Eliminar notificaciones relacionadas
            db.delete(TABLE_NOTIFICATIONS, COLUMN_NOTIFICATION_REFERENCE_TYPE + "=? AND " + COLUMN_NOTIFICATION_REFERENCE_ID + "=?", 
                    new String[]{"request", String.valueOf(requestId)});
            
            // Eliminar la solicitud
            int result = db.delete(TABLE_REQUESTS, COLUMN_REQUEST_ID + "=?", 
                    new String[]{String.valueOf(requestId)});
            
            db.close();
            return result > 0;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("deleteRequest", e);
            return false;
        }
    }

    /**
     * Obtiene solicitudes filtradas por estado
     * @param status Estado de las solicitudes a buscar
     * @return Lista de solicitudes con el estado especificado
     */
    public List<Request> getRequestsByStatus(String status) {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTS, null, COLUMN_REQUEST_STATUS + "=?", 
                    new String[]{status}, null, null, COLUMN_REQUEST_CREATED_AT + " DESC");
            
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
            errorHandler.handleDatabaseError("getRequestsByStatus", e);
        }
        return requests;
    }

    // =====================================================
    // MÉTODOS PARA NUEVAS TABLAS
    // =====================================================

    /**
     * Obtiene todas las categorías
     * @return Lista de todas las categorías
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_CATEGORIES, null, COLUMN_CATEGORY_STATUS + "=?", 
                    new String[]{"activo"}, null, null, COLUMN_CATEGORY_SORT_ORDER + " ASC");
            
            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
                    category.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)));
                    category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION)));
                    category.setParentId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_PARENT_ID)));
                    category.setIcon(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ICON)));
                    category.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_COLOR)));
                    category.setSortOrder(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_SORT_ORDER)));
                    category.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_STATUS)));
                    category.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_CREATED_AT)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            errorHandler.handleDatabaseError("getAllCategories", e);
        }
        return categories;
    }

    /**
     * Obtiene todas las notificaciones de un usuario
     * @param userId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NOTIFICATIONS, null, COLUMN_NOTIFICATION_USER_ID + "=?", 
                    new String[]{String.valueOf(userId)}, null, null, COLUMN_NOTIFICATION_CREATED_AT + " DESC");
            
            if (cursor.moveToFirst()) {
                do {
                    Notification notification = new Notification();
                    notification.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_ID)));
                    notification.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_USER_ID)));
                    notification.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_TITLE)));
                    notification.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_MESSAGE)));
                    notification.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_TYPE)));
                    notification.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_CATEGORY)));
                    notification.setReferenceType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_REFERENCE_TYPE)));
                    notification.setReferenceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_REFERENCE_ID)));
                    notification.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_IS_READ)) == 1);
                    notification.setIsSent(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_IS_SENT)) == 1);
                    notification.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_CREATED_AT)));
                    notifications.add(notification);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            errorHandler.handleDatabaseError("getUserNotifications", e);
        }
        return notifications;
    }

    /**
     * Marca una notificación como leída
     * @param notificationId ID de la notificación
     * @return true si se marcó como leída exitosamente
     */
    public boolean markNotificationAsRead(int notificationId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTIFICATION_IS_READ, 1);
            values.put(COLUMN_NOTIFICATION_READ_AT, getCurrentDateTime());
            
            int result = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + "=?", 
                    new String[]{String.valueOf(notificationId)});
            db.close();
            return result > 0;
        } catch (Exception e) {
            errorHandler.handleDatabaseError("markNotificationAsRead", e);
            return false;
        }
    }
}
