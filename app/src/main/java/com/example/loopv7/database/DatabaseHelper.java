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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "loop_database.db";
    private static final int DATABASE_VERSION = 9;

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
    private static final String COLUMN_USER_COMPLETED_SERVICES = "completed_services";
    private static final String COLUMN_USER_LAST_SERVICE_DATE = "last_service_date";
    private static final String COLUMN_USER_LOCATION = "location";
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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Asegurar que todas las columnas existan cuando se abre la base de datos
        ensureColumnsExist(db);
    }
    
    /**
     * Asegura que todas las columnas necesarias existan en la tabla users
     * Este método se ejecuta cada vez que se abre la base de datos
     */
    private void ensureColumnsExist(SQLiteDatabase db) {
        try {
            // Verificar y agregar columna location si no existe
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_LOCATION + " TEXT");
                    Log.d("DatabaseHelper", "Columna location agregada en onOpen");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna location en onOpen: " + e.getMessage());
                }
            }
            
            // Verificar y agregar otras columnas si no existen
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_DESCRIPTION + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna description: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PROFILE_IMAGE + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna profile_image: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_RATING + " REAL DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna rating: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna total_ratings: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_COMPLETED_SERVICES + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna completed_services: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_LAST_SERVICE_DATE + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna last_service_date: " + e.getMessage());
                }
            }
            
            if (!columnExists(db, TABLE_REQUESTS, COLUMN_REQUEST_IS_ARCHIVED)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_REQUESTS + " ADD COLUMN " + COLUMN_REQUEST_IS_ARCHIVED + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error agregando columna is_archived: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error en ensureColumnsExist: " + e.getMessage(), e);
        }
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
                COLUMN_USER_COMPLETED_SERVICES + " INTEGER DEFAULT 0, " +
                COLUMN_USER_LAST_SERVICE_DATE + " TEXT, " +
                COLUMN_USER_LOCATION + " TEXT, " +
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
                COLUMN_REQUEST_IS_ARCHIVED + " INTEGER DEFAULT 0, " +
                COLUMN_REQUEST_CREATED_AT + " TEXT, " +
                COLUMN_REQUEST_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_REQUEST_CLIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REQUEST_SOCIA_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REQUEST_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE_ID + ")" +
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
        try {
            // Asegurar que todas las columnas existan, agregándolas si no existen
            // Esto maneja el caso donde la base de datos puede no tener todas las columnas
            
            if (oldVersion < 2 || !columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_DESCRIPTION + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna description ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 2 || !columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PROFILE_IMAGE + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna profile_image ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 2 || !columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_RATING + " REAL DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna rating ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 2 || !columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_TOTAL_RATINGS + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna total_ratings ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 2 || !columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_LOCATION + " TEXT");
                    Log.d("DatabaseHelper", "Columna location agregada exitosamente");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna location ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 7 || !columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_COMPLETED_SERVICES + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna completed_services ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 7 || !columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_LAST_SERVICE_DATE + " TEXT");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna last_service_date ya existe o error agregándola: " + e.getMessage());
                }
            }
            
            if (oldVersion < 8 || !columnExists(db, TABLE_REQUESTS, COLUMN_REQUEST_IS_ARCHIVED)) {
                try {
                    db.execSQL("ALTER TABLE " + TABLE_REQUESTS + " ADD COLUMN " + COLUMN_REQUEST_IS_ARCHIVED + " INTEGER DEFAULT 0");
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Columna is_archived ya existe o error agregándola: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error durante onUpgrade: " + e.getMessage(), e);
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
            
            Log.d("DatabaseHelper", "Base de datos actualizada a versión 6 - Nuevas tablas agregadas");
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

        // Insertar solicitudes de prueba
        String[] requests = {
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, socia_id, service_id, scheduled_date, scheduled_time, address, notes, status, total_price, payment_status, rating, review, created_at, updated_at) VALUES " +
                "(1, 2, 1, '" + currentTime + "', '10:00', 'Calle Principal 123, Ciudad', 'Necesito limpieza general de mi casa de 3 habitaciones', 'pendiente', 50.00, 'pendiente', 0, '', '" + currentTime + "', '" + currentTime + "')",
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, socia_id, service_id, scheduled_date, scheduled_time, address, notes, status, total_price, payment_status, rating, review, created_at, updated_at) VALUES " +
                "(1, 2, 2, '" + currentTime + "', '14:00', 'Avenida Central 456, Ciudad', 'Limpieza profunda de oficina pequeña', 'aceptada', 80.00, 'pagado', 0, '', '" + currentTime + "', '" + currentTime + "')",
                "INSERT INTO " + TABLE_REQUESTS + " (client_id, socia_id, service_id, scheduled_date, scheduled_time, address, notes, status, total_price, payment_status, rating, review, created_at, updated_at) VALUES " +
                "(1, 2, 3, '" + currentTime + "', '16:00', 'Calle Secundaria 789, Ciudad', 'Limpieza especializada de cocina', 'completada', 35.00, 'pagado', 5, 'Excelente servicio, muy recomendado', '" + currentTime + "', '" + currentTime + "')"
        };

        for (String request : requests) {
            db.execSQL(request);
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

    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    /**
     * Método para verificar el estado de la base de datos y datos
     */
    public void logDatabaseStatus() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            
            // Verificar usuarios
            Cursor userCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
            int userCount = 0;
            if (userCursor.moveToFirst()) {
                userCount = userCursor.getInt(0);
            }
            userCursor.close();
            
            // Verificar servicios
            Cursor serviceCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_SERVICES, null);
            int serviceCount = 0;
            if (serviceCursor.moveToFirst()) {
                serviceCount = serviceCursor.getInt(0);
            }
            serviceCursor.close();
            
            // Verificar solicitudes
            Cursor requestCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS, null);
            int requestCount = 0;
            if (requestCursor.moveToFirst()) {
                requestCount = requestCursor.getInt(0);
            }
            requestCursor.close();
            
            // Verificar solicitudes completadas
            Cursor completedCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS + " WHERE " + COLUMN_REQUEST_STATUS + " = 'completada'", null);
            int completedCount = 0;
            if (completedCursor.moveToFirst()) {
                completedCount = completedCursor.getInt(0);
            }
            completedCursor.close();
            
            // Verificar calificaciones
            Cursor ratingCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RATINGS, null);
            int ratingCount = 0;
            if (ratingCursor.moveToFirst()) {
                ratingCount = ratingCursor.getInt(0);
            }
            ratingCursor.close();
            
            Log.d("DatabaseHelper", "Estado de la base de datos:");
            Log.d("DatabaseHelper", "- Usuarios: " + userCount);
            Log.d("DatabaseHelper", "- Servicios: " + serviceCount);
            Log.d("DatabaseHelper", "- Solicitudes: " + requestCount);
            Log.d("DatabaseHelper", "- Solicitudes completadas: " + completedCount);
            Log.d("DatabaseHelper", "- Calificaciones: " + ratingCount);
            
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error verificando estado de la base de datos: " + e.getMessage());
        }
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
        
        try {
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_ROLE, user.getRole());
            
            // Asegurarse de que el status siempre sea "activo" si es null o vacío
            String status = user.getStatus();
            if (status == null || status.trim().isEmpty()) {
                status = "activo";
                user.setStatus(status);
            }
            values.put(COLUMN_USER_STATUS, status.trim().toLowerCase());
            
            // Verificar y agregar columnas opcionales solo si existen
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                values.put(COLUMN_USER_DESCRIPTION, user.getDescription() != null ? user.getDescription() : "");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage() != null ? user.getProfileImage() : "");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                values.put(COLUMN_USER_RATING, user.getRating());
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                values.put(COLUMN_USER_COMPLETED_SERVICES, user.getCompletedServices());
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                values.put(COLUMN_USER_LAST_SERVICE_DATE, user.getLastServiceDate() != null ? user.getLastServiceDate() : "");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                values.put(COLUMN_USER_LOCATION, user.getLocation() != null ? user.getLocation() : "");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_CREATED_AT)) {
                values.put(COLUMN_USER_CREATED_AT, getCurrentDateTime());
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_UPDATED_AT)) {
                values.put(COLUMN_USER_UPDATED_AT, getCurrentDateTime());
            }
            
            long result = db.insert(TABLE_USERS, null, values);
            return result;
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error insertando usuario: " + e.getMessage(), e);
            return -1;
        } finally {
            db.close();
        }
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
            
            // Obtener status y asegurarse de que no sea null (por defecto "activo")
            int statusIndex = cursor.getColumnIndex(COLUMN_USER_STATUS);
            String status = statusIndex >= 0 && !cursor.isNull(statusIndex) ? 
                           cursor.getString(statusIndex) : null;
            
            // Si el status es null, vacío o no es "activo", establecer como "activo" y actualizar en BD
            if (status == null || status.trim().isEmpty() || !"activo".equalsIgnoreCase(status.trim())) {
                status = "activo";
                user.setStatus(status);
                // Actualizar en la base de datos
                ContentValues updateValues = new ContentValues();
                updateValues.put(COLUMN_USER_STATUS, "activo");
                db.update(TABLE_USERS, updateValues, COLUMN_USER_ID + "=?", 
                         new String[]{String.valueOf(user.getId())});
                Log.d("DatabaseHelper", "Usuario " + email + " tenía status inválido, actualizado a 'activo'");
            } else {
                user.setStatus(status.trim().toLowerCase());
            }
            // Manejar columnas que podrían no existir en versiones anteriores
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                int descriptionIndex = cursor.getColumnIndex(COLUMN_USER_DESCRIPTION);
                user.setDescription(descriptionIndex >= 0 && !cursor.isNull(descriptionIndex) ? cursor.getString(descriptionIndex) : "");
            } else {
                user.setDescription("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                int profileImageIndex = cursor.getColumnIndex(COLUMN_USER_PROFILE_IMAGE);
                user.setProfileImage(profileImageIndex >= 0 && !cursor.isNull(profileImageIndex) ? cursor.getString(profileImageIndex) : "");
            } else {
                user.setProfileImage("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                int ratingIndex = cursor.getColumnIndex(COLUMN_USER_RATING);
                user.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getDouble(ratingIndex) : 0.0);
            } else {
                user.setRating(0.0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                int totalRatingsIndex = cursor.getColumnIndex(COLUMN_USER_TOTAL_RATINGS);
                user.setTotalRatings(totalRatingsIndex >= 0 && !cursor.isNull(totalRatingsIndex) ? cursor.getInt(totalRatingsIndex) : 0);
            } else {
                user.setTotalRatings(0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                int locationIndex = cursor.getColumnIndex(COLUMN_USER_LOCATION);
                user.setLocation(locationIndex >= 0 && !cursor.isNull(locationIndex) ? cursor.getString(locationIndex) : "");
            } else {
                user.setLocation("");
            }
            
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
            user.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_UPDATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
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
            db.close();
            return exists;
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error verificando si email existe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza las estadísticas de un usuario basándose en las solicitudes completadas y calificaciones
     */
    public void updateUserStatsFromDatabase(int userId) {
        SQLiteDatabase db = null;
        try {
            Log.d("DatabaseHelper", "Actualizando estadísticas del usuario ID: " + userId);
            
            // Obtener el usuario
            User user = getUserById(userId);
            if (user == null) {
                Log.e("DatabaseHelper", "Usuario no encontrado para actualizar estadísticas");
                return;
            }
            
            // Abrir base de datos una sola vez
            db = this.getReadableDatabase();
            
            // Contar TODOS los servicios completados por esta socia (incluyendo archivados)
            // Las estadísticas deben mostrar el historial completo
            Cursor completedCursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_REQUESTS + 
                " WHERE " + COLUMN_REQUEST_SOCIA_ID + " = ? AND " + COLUMN_REQUEST_STATUS + " = 'completada'", 
                new String[]{String.valueOf(userId)}
            );
            
            int completedServices = 0;
            if (completedCursor.moveToFirst()) {
                completedServices = completedCursor.getInt(0);
            }
            completedCursor.close();
            
            // Obtener calificaciones de la socia directamente desde la base de datos
            Cursor ratingCursor = db.rawQuery(
                "SELECT " + COLUMN_RATING_OVERALL + " FROM " + TABLE_RATINGS + 
                " WHERE " + COLUMN_RATING_RATED_ID + " = ?", 
                new String[]{String.valueOf(userId)}
            );
            
            double averageRating = 0.0;
            int totalRatings = 0;
            double totalRatingSum = 0.0;
            
            if (ratingCursor.moveToFirst()) {
                do {
                    int ratingIndex = ratingCursor.getColumnIndex(COLUMN_RATING_OVERALL);
                    if (ratingIndex >= 0 && !ratingCursor.isNull(ratingIndex)) {
                        double rating = ratingCursor.getDouble(ratingIndex);
                        totalRatingSum += rating;
                        totalRatings++;
                    }
                } while (ratingCursor.moveToNext());
                
                if (totalRatings > 0) {
                    averageRating = totalRatingSum / totalRatings;
                }
            }
            ratingCursor.close();
            
            // Obtener fecha del último servicio completado
            Cursor lastServiceCursor = db.rawQuery(
                "SELECT " + COLUMN_REQUEST_UPDATED_AT + " FROM " + TABLE_REQUESTS + 
                " WHERE " + COLUMN_REQUEST_SOCIA_ID + " = ? AND " + COLUMN_REQUEST_STATUS + " = 'completada'" +
                " ORDER BY " + COLUMN_REQUEST_UPDATED_AT + " DESC LIMIT 1", 
                new String[]{String.valueOf(userId)}
            );
            
            String lastServiceDate = "";
            if (lastServiceCursor.moveToFirst()) {
                int dateIndex = lastServiceCursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                if (dateIndex >= 0 && !lastServiceCursor.isNull(dateIndex)) {
                    lastServiceDate = lastServiceCursor.getString(dateIndex);
                }
            }
            lastServiceCursor.close();
            
            // Actualizar el usuario
            user.setCompletedServices(completedServices);
            user.setRating(averageRating);
            user.setTotalRatings(totalRatings);
            user.setLastServiceDate(lastServiceDate);
            
            // Guardar en la base de datos
            boolean updated = updateUser(user);
            
            Log.d("DatabaseHelper", "Estadísticas actualizadas para " + user.getName() + ":");
            Log.d("DatabaseHelper", "- Servicios completados: " + completedServices);
            Log.d("DatabaseHelper", "- Calificación promedio: " + String.format("%.2f", averageRating));
            Log.d("DatabaseHelper", "- Total calificaciones: " + totalRatings);
            Log.d("DatabaseHelper", "- Último servicio: " + lastServiceDate);
            Log.d("DatabaseHelper", "- Actualización exitosa: " + updated);
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error actualizando estadísticas del usuario: " + e.getMessage(), e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public User getUserById(int id) {
        try {
            Log.d("DatabaseHelper", "Buscando usuario con ID: " + id);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ID + "=?", 
                    new String[]{String.valueOf(id)}, null, null, null);
            
            User user = null;
            if (cursor.moveToFirst()) {
                Log.d("DatabaseHelper", "Usuario encontrado en la base de datos");
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
            
            // Obtener status y asegurarse de que no sea null (por defecto "activo")
            int statusIndex = cursor.getColumnIndex(COLUMN_USER_STATUS);
            String status = statusIndex >= 0 && !cursor.isNull(statusIndex) ? 
                           cursor.getString(statusIndex) : null;
            
            // Si el status es null, vacío o no es "activo", establecer como "activo" y actualizar en BD
            if (status == null || status.trim().isEmpty() || !"activo".equalsIgnoreCase(status.trim())) {
                status = "activo";
                user.setStatus(status);
                // Actualizar en la base de datos
                ContentValues updateValues = new ContentValues();
                updateValues.put(COLUMN_USER_STATUS, "activo");
                db.update(TABLE_USERS, updateValues, COLUMN_USER_ID + "=?", 
                         new String[]{String.valueOf(user.getId())});
                Log.d("DatabaseHelper", "Usuario ID " + user.getId() + " tenía status inválido, actualizado a 'activo'");
            } else {
                user.setStatus(status.trim().toLowerCase());
            }
            // Manejar columnas que podrían no existir en versiones anteriores
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                int descriptionIndex = cursor.getColumnIndex(COLUMN_USER_DESCRIPTION);
                user.setDescription(descriptionIndex >= 0 && !cursor.isNull(descriptionIndex) ? cursor.getString(descriptionIndex) : "");
            } else {
                user.setDescription("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                int profileImageIndex = cursor.getColumnIndex(COLUMN_USER_PROFILE_IMAGE);
                user.setProfileImage(profileImageIndex >= 0 && !cursor.isNull(profileImageIndex) ? cursor.getString(profileImageIndex) : "");
            } else {
                user.setProfileImage("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                int ratingIndex = cursor.getColumnIndex(COLUMN_USER_RATING);
                user.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getDouble(ratingIndex) : 0.0);
            } else {
                user.setRating(0.0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                int totalRatingsIndex = cursor.getColumnIndex(COLUMN_USER_TOTAL_RATINGS);
                user.setTotalRatings(totalRatingsIndex >= 0 && !cursor.isNull(totalRatingsIndex) ? cursor.getInt(totalRatingsIndex) : 0);
            } else {
                user.setTotalRatings(0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                int completedServicesIndex = cursor.getColumnIndex(COLUMN_USER_COMPLETED_SERVICES);
                user.setCompletedServices(completedServicesIndex >= 0 && !cursor.isNull(completedServicesIndex) ? cursor.getInt(completedServicesIndex) : 0);
            } else {
                user.setCompletedServices(0);
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                int lastServiceDateIndex = cursor.getColumnIndex(COLUMN_USER_LAST_SERVICE_DATE);
                user.setLastServiceDate(lastServiceDateIndex >= 0 && !cursor.isNull(lastServiceDateIndex) ? cursor.getString(lastServiceDateIndex) : "");
            } else {
                user.setLastServiceDate("");
            }
            
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                int locationIndex = cursor.getColumnIndex(COLUMN_USER_LOCATION);
                user.setLocation(locationIndex >= 0 && !cursor.isNull(locationIndex) ? cursor.getString(locationIndex) : "");
            } else {
                user.setLocation("");
            }
            
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
            
            // Handle nullable updated_at
            int updatedAtIndex = cursor.getColumnIndex(COLUMN_USER_UPDATED_AT);
            user.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
            } else {
                Log.w("DatabaseHelper", "No se encontró usuario con ID: " + id);
            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo usuario por ID: " + e.getMessage(), e);
            return null;
        }
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        try {
            // Campos básicos que siempre existen
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_ROLE, user.getRole());
            values.put(COLUMN_USER_STATUS, user.getStatus());
            
            // Campos que pueden no existir en versiones anteriores
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                values.put(COLUMN_USER_DESCRIPTION, user.getDescription());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                values.put(COLUMN_USER_RATING, user.getRating());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                values.put(COLUMN_USER_COMPLETED_SERVICES, user.getCompletedServices());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                values.put(COLUMN_USER_LAST_SERVICE_DATE, user.getLastServiceDate());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                values.put(COLUMN_USER_LOCATION, user.getLocation());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_UPDATED_AT)) {
                values.put(COLUMN_USER_UPDATED_AT, getCurrentDateTime());
            }
            
            int result = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", 
                    new String[]{String.valueOf(user.getId())});
            return result > 0;
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error actualizando usuario: " + e.getMessage(), e);
            return false;
        } finally {
            db.close();
        }
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
    /**
     * Obtiene todos los servicios activos
     * @return Lista de servicios activos
     */
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
    
    /**
     * Obtiene todos los servicios (sin filtrar por estado) - para backup
     * @return Lista de todos los servicios
     */
    public List<Service> getAllServicesForBackup() {
        List<Service> services = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_SERVICES, null, null, null, null, null, null);
            
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
            Log.e("DatabaseHelper", "Error obteniendo todos los servicios: " + e.getMessage(), e);
        }
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
        values.put(COLUMN_REQUEST_IS_ARCHIVED, request.isArchived() ? 1 : 0);
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
                
                // Handle nullable socia_id
                int sociaIdIndex = cursor.getColumnIndex(COLUMN_REQUEST_SOCIA_ID);
                request.setSociaId(sociaIdIndex >= 0 && !cursor.isNull(sociaIdIndex) ? cursor.getInt(sociaIdIndex) : 0);
                
                request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                
                // Handle nullable notes
                int notesIndex = cursor.getColumnIndex(COLUMN_REQUEST_NOTES);
                request.setNotes(notesIndex >= 0 && !cursor.isNull(notesIndex) ? cursor.getString(notesIndex) : "");
                
                request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                
                // Handle nullable rating
                int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                
                // Handle nullable review
                int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                
                // Handle nullable isArchived
                int isArchivedIndex = cursor.getColumnIndex(COLUMN_REQUEST_IS_ARCHIVED);
                request.setArchived(isArchivedIndex >= 0 && cursor.getInt(isArchivedIndex) == 1);
                
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                
                // Handle nullable updated_at
                int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
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
                
                // Handle nullable socia_id
                int sociaIdIndex = cursor.getColumnIndex(COLUMN_REQUEST_SOCIA_ID);
                request.setSociaId(sociaIdIndex >= 0 && !cursor.isNull(sociaIdIndex) ? cursor.getInt(sociaIdIndex) : 0);
                
                request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                
                // Handle nullable notes
                int notesIndex = cursor.getColumnIndex(COLUMN_REQUEST_NOTES);
                request.setNotes(notesIndex >= 0 && !cursor.isNull(notesIndex) ? cursor.getString(notesIndex) : "");
                
                request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                
                // Handle nullable rating
                int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                
                // Handle nullable review
                int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                
                // Handle nullable isArchived
                int isArchivedIndex = cursor.getColumnIndex(COLUMN_REQUEST_IS_ARCHIVED);
                request.setArchived(isArchivedIndex >= 0 && cursor.getInt(isArchivedIndex) == 1);
                
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                
                // Handle nullable updated_at
                int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
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
                
                // Handle nullable socia_id
                int sociaIdIndex = cursor.getColumnIndex(COLUMN_REQUEST_SOCIA_ID);
                request.setSociaId(sociaIdIndex >= 0 && !cursor.isNull(sociaIdIndex) ? cursor.getInt(sociaIdIndex) : 0);
                
                request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                
                // Handle nullable notes
                int notesIndex = cursor.getColumnIndex(COLUMN_REQUEST_NOTES);
                request.setNotes(notesIndex >= 0 && !cursor.isNull(notesIndex) ? cursor.getString(notesIndex) : "");
                
                request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                
                // Handle nullable rating
                int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                
                // Handle nullable review
                int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                
                // Handle nullable isArchived
                int isArchivedIndex = cursor.getColumnIndex(COLUMN_REQUEST_IS_ARCHIVED);
                request.setArchived(isArchivedIndex >= 0 && cursor.getInt(isArchivedIndex) == 1);
                
                request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                
                // Handle nullable updated_at
                int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
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

    // ========== NUEVOS MÉTODOS CRUD PARA SERVICIOS ==========
    
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
            Log.e("DatabaseHelper", "Error insertando servicio: " + e.getMessage());
            return -1;
        }
    }

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
            Log.e("DatabaseHelper", "Error actualizando servicio: " + e.getMessage());
            return false;
        }
    }

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
            Log.e("DatabaseHelper", "Error eliminando solicitud: " + e.getMessage());
            return false;
        }
    }

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
            Log.e("DatabaseHelper", "Error obteniendo solicitudes por estado: " + e.getMessage());
        }
        return requests;
    }

    // ========== MÉTODOS PARA CATEGORÍAS ==========
    
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
            Log.e("DatabaseHelper", "Error obteniendo categorías: " + e.getMessage());
        }
        return categories;
    }

    // ========== MÉTODOS PARA PAGOS ==========
    
    public long insertPayment(Payment payment) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_PAYMENT_REQUEST_ID, payment.getRequestId());
            values.put(COLUMN_PAYMENT_AMOUNT, payment.getAmount());
            values.put(COLUMN_PAYMENT_CURRENCY, payment.getCurrency());
            values.put(COLUMN_PAYMENT_METHOD, payment.getPaymentMethod());
            values.put(COLUMN_PAYMENT_STATUS, payment.getPaymentStatus());
            values.put(COLUMN_PAYMENT_TRANSACTION_ID, payment.getTransactionId());
            values.put(COLUMN_PAYMENT_GATEWAY_RESPONSE, payment.getGatewayResponse());
            values.put(COLUMN_PAYMENT_GATEWAY_NAME, payment.getGatewayName());
            values.put(COLUMN_PAYMENT_DATE, payment.getPaymentDate());
            values.put(COLUMN_PAYMENT_PROCESSED_AT, payment.getProcessedAt());
            values.put(COLUMN_PAYMENT_REFUNDED_AT, payment.getRefundedAt());
            values.put(COLUMN_PAYMENT_NOTES, payment.getNotes());
            values.put(COLUMN_PAYMENT_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_PAYMENTS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error insertando pago: " + e.getMessage());
            return -1;
        }
    }

    // ========== MÉTODOS PARA CALIFICACIONES ==========
    
    public long insertRating(Rating rating) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_RATING_REQUEST_ID, rating.getRequestId());
            values.put(COLUMN_RATING_RATER_ID, rating.getRaterId());
            values.put(COLUMN_RATING_RATED_ID, rating.getRatedId());
            values.put(COLUMN_RATING_OVERALL, rating.getOverallRating());
            values.put(COLUMN_RATING_QUALITY, rating.getQualityRating());
            values.put(COLUMN_RATING_PUNCTUALITY, rating.getPunctualityRating());
            values.put(COLUMN_RATING_COMMUNICATION, rating.getCommunicationRating());
            values.put(COLUMN_RATING_CLEANLINESS, rating.getCleanlinessRating());
            values.put(COLUMN_RATING_REVIEW, rating.getReview());
            values.put(COLUMN_RATING_IS_ANONYMOUS, rating.isAnonymous() ? 1 : 0);
            values.put(COLUMN_RATING_STATUS, rating.getStatus());
            values.put(COLUMN_RATING_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_RATINGS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error insertando calificación: " + e.getMessage());
            return -1;
        }
    }

    // ========== MÉTODOS PARA NOTIFICACIONES ==========
    
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
            Log.e("DatabaseHelper", "Error obteniendo notificaciones: " + e.getMessage());
        }
        return notifications;
    }

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
            Log.e("DatabaseHelper", "Error marcando notificación como leída: " + e.getMessage());
            return false;
        }
    }

    public long createNotification(int userId, String title, String message, String type, String category, String referenceType, Integer referenceId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            values.put(COLUMN_NOTIFICATION_USER_ID, userId);
            values.put(COLUMN_NOTIFICATION_TITLE, title);
            values.put(COLUMN_NOTIFICATION_MESSAGE, message);
            values.put(COLUMN_NOTIFICATION_TYPE, type);
            values.put(COLUMN_NOTIFICATION_CATEGORY, category);
            values.put(COLUMN_NOTIFICATION_REFERENCE_TYPE, referenceType);
            if (referenceId != null) {
                values.put(COLUMN_NOTIFICATION_REFERENCE_ID, referenceId);
            }
            values.put(COLUMN_NOTIFICATION_IS_READ, 0);
            values.put(COLUMN_NOTIFICATION_IS_SENT, 0);
            values.put(COLUMN_NOTIFICATION_CREATED_AT, getCurrentDateTime());
            
            long result = db.insert(TABLE_NOTIFICATIONS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creando notificación: " + e.getMessage());
            return -1;
        }
    }

    public List<Rating> getRatingsByRatedId(int ratedId) {
        List<Rating> ratings = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_RATINGS + 
                               " WHERE " + COLUMN_RATING_RATED_ID + " = ? AND " + 
                               COLUMN_RATING_STATUS + " = 'activo' ORDER BY " + 
                               COLUMN_RATING_CREATED_AT + " DESC";
            
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(ratedId)});
            
            if (cursor.moveToFirst()) {
                do {
                    Rating rating = new Rating();
                    rating.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_ID)));
                    rating.setRequestId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_REQUEST_ID)));
                    rating.setRaterId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_RATER_ID)));
                    rating.setRatedId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_RATED_ID)));
                    rating.setOverallRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_OVERALL)));
                    rating.setQualityRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_QUALITY)));
                    rating.setPunctualityRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_PUNCTUALITY)));
                    rating.setCommunicationRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_COMMUNICATION)));
                    rating.setCleanlinessRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_CLEANLINESS)));
                    rating.setReview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING_REVIEW)));
                    rating.setAnonymous(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_IS_ANONYMOUS)) == 1);
                    rating.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING_STATUS)));
                    rating.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING_CREATED_AT)));
                    
                    ratings.add(rating);
                } while (cursor.moveToNext());
            }
            
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo calificaciones: " + e.getMessage());
        }
        return ratings;
    }

    // ========== MÉTODO ACTUALIZADO PARA LIMPIAR DATOS ==========
    
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
            Log.e("DatabaseHelper", "Error limpiando datos: " + e.getMessage());
        }
    }

    /**
     * Reinicia la base de datos: limpia todos los datos y restaura los datos iniciales
     * Esto es útil para pruebas o cuando se necesita empezar desde cero
     * @return true si el reinicio fue exitoso, false si hubo error
     */
    public boolean resetDatabase() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            
            Log.i("DatabaseHelper", "Iniciando reinicio de base de datos...");
            
            // Eliminar todos los datos (en orden correcto para respetar foreign keys)
            db.delete(TABLE_NOTIFICATIONS, null, null);
            db.delete(TABLE_RATINGS, null, null);
            db.delete(TABLE_PAYMENTS, null, null);
            db.delete(TABLE_REQUESTS, null, null);
            db.delete(TABLE_SERVICES, null, null);
            db.delete(TABLE_CATEGORIES, null, null);
            db.delete(TABLE_USERS, null, null);
            
            Log.d("DatabaseHelper", "Datos eliminados, insertando datos iniciales...");
            
            // Reinsertar datos iniciales básicos (usuarios y servicios)
            insertInitialData(db);
            
            // Reinsertar categorías (sin dependencias de IDs)
            insertCategoriesData(db);
            
            db.close();
            
            Log.i("DatabaseHelper", "Base de datos reiniciada exitosamente");
            return true;
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reiniciando base de datos: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Inserta solo las categorías (sin solicitudes, pagos, calificaciones que dependen de IDs)
     */
    private void insertCategoriesData(SQLiteDatabase db) {
        try {
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

                    // Subcategorías de Limpieza (parent_id = 1)
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Limpieza General', 'Limpieza básica del hogar', 1, 'general_cleaning', '#4CAF50', 1, 'activo', '" + currentTime + "')",
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Limpieza Profunda', 'Limpieza exhaustiva y detallada', 1, 'deep_cleaning', '#4CAF50', 2, 'activo', '" + currentTime + "')",
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Limpieza de Cocina', 'Limpieza especializada de cocina', 1, 'kitchen_cleaning', '#4CAF50', 3, 'activo', '" + currentTime + "')",
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Limpieza de Baños', 'Limpieza de baños y sanitarios', 1, 'bathroom_cleaning', '#4CAF50', 4, 'activo', '" + currentTime + "')",

                    // Subcategorías de Lavandería (parent_id = 2)
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Planchado', 'Planchado y doblado de ropa', 2, 'ironing', '#2196F3', 1, 'activo', '" + currentTime + "')",
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Lavado', 'Lavado de ropa y textiles', 2, 'washing', '#2196F3', 2, 'activo', '" + currentTime + "')",

                    // Subcategorías de Organización (parent_id = 3)
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Organización de Closets', 'Organización de armarios y closets', 3, 'closet_organization', '#FF9800', 1, 'activo', '" + currentTime + "')",
                    "INSERT INTO " + TABLE_CATEGORIES + " (name, description, parent_id, icon, color, sort_order, status, created_at) VALUES " +
                    "('Organización de Oficina', 'Organización de espacios de trabajo', 3, 'office_organization', '#FF9800', 2, 'activo', '" + currentTime + "')"
            };

            for (String category : categories) {
                try {
                    db.execSQL(category);
                } catch (Exception e) {
                    Log.d("DatabaseHelper", "Error insertando categoría (puede que ya exista): " + e.getMessage());
                }
            }
            
            Log.d("DatabaseHelper", "Categorías insertadas exitosamente");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error insertando categorías: " + e.getMessage(), e);
        }
    }

    // ========== MÉTODOS PARA BACKUP/RESTORE ==========
    
    /**
     * Obtiene todos los usuarios (sin filtrar por estado)
     * @return Lista de todos los usuarios
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);
            
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                    user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                    user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                    user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                    user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
                    user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
                    user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_STATUS)));
                    
                    // Campos opcionales
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                        int descriptionIndex = cursor.getColumnIndex(COLUMN_USER_DESCRIPTION);
                        user.setDescription(descriptionIndex >= 0 && !cursor.isNull(descriptionIndex) ? cursor.getString(descriptionIndex) : "");
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                        int profileImageIndex = cursor.getColumnIndex(COLUMN_USER_PROFILE_IMAGE);
                        user.setProfileImage(profileImageIndex >= 0 && !cursor.isNull(profileImageIndex) ? cursor.getString(profileImageIndex) : "");
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                        int ratingIndex = cursor.getColumnIndex(COLUMN_USER_RATING);
                        user.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getDouble(ratingIndex) : 0.0);
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                        int totalRatingsIndex = cursor.getColumnIndex(COLUMN_USER_TOTAL_RATINGS);
                        user.setTotalRatings(totalRatingsIndex >= 0 && !cursor.isNull(totalRatingsIndex) ? cursor.getInt(totalRatingsIndex) : 0);
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                        int completedServicesIndex = cursor.getColumnIndex(COLUMN_USER_COMPLETED_SERVICES);
                        user.setCompletedServices(completedServicesIndex >= 0 && !cursor.isNull(completedServicesIndex) ? cursor.getInt(completedServicesIndex) : 0);
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                        int lastServiceDateIndex = cursor.getColumnIndex(COLUMN_USER_LAST_SERVICE_DATE);
                        user.setLastServiceDate(lastServiceDateIndex >= 0 && !cursor.isNull(lastServiceDateIndex) ? cursor.getString(lastServiceDateIndex) : "");
                    }
                    
                    if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                        int locationIndex = cursor.getColumnIndex(COLUMN_USER_LOCATION);
                        user.setLocation(locationIndex >= 0 && !cursor.isNull(locationIndex) ? cursor.getString(locationIndex) : "");
                    }
                    
                    user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
                    
                    int updatedAtIndex = cursor.getColumnIndex(COLUMN_USER_UPDATED_AT);
                    user.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
                    
                    users.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo todos los usuarios: " + e.getMessage(), e);
        }
        return users;
    }
    
    /**
     * Obtiene todas las solicitudes (sin filtrar por estado)
     * @return Lista de todas las solicitudes
     */
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTS, null, null, null, null, null, COLUMN_REQUEST_CREATED_AT + " DESC");
            
            if (cursor.moveToFirst()) {
                do {
                    Request request = new Request();
                    request.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)));
                    request.setClientId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CLIENT_ID)));
                    
                    int sociaIdIndex = cursor.getColumnIndex(COLUMN_REQUEST_SOCIA_ID);
                    request.setSociaId(sociaIdIndex >= 0 && !cursor.isNull(sociaIdIndex) ? cursor.getInt(sociaIdIndex) : 0);
                    
                    request.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SERVICE_ID)));
                    request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_STATUS)));
                    request.setScheduledDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_DATE)));
                    request.setScheduledTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_SCHEDULED_TIME)));
                    request.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ADDRESS)));
                    
                    int notesIndex = cursor.getColumnIndex(COLUMN_REQUEST_NOTES);
                    request.setNotes(notesIndex >= 0 && !cursor.isNull(notesIndex) ? cursor.getString(notesIndex) : "");
                    
                    request.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_TOTAL_PRICE)));
                    request.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_PAYMENT_STATUS)));
                    
                    int ratingIndex = cursor.getColumnIndex(COLUMN_REQUEST_RATING);
                    request.setRating(ratingIndex >= 0 && !cursor.isNull(ratingIndex) ? cursor.getInt(ratingIndex) : 0);
                    
                    int reviewIndex = cursor.getColumnIndex(COLUMN_REQUEST_REVIEW);
                    request.setReview(reviewIndex >= 0 && !cursor.isNull(reviewIndex) ? cursor.getString(reviewIndex) : "");
                    
                    int isArchivedIndex = cursor.getColumnIndex(COLUMN_REQUEST_IS_ARCHIVED);
                    request.setArchived(isArchivedIndex >= 0 && cursor.getInt(isArchivedIndex) == 1);
                    
                    request.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_CREATED_AT)));
                    
                    int updatedAtIndex = cursor.getColumnIndex(COLUMN_REQUEST_UPDATED_AT);
                    request.setUpdatedAt(updatedAtIndex >= 0 && !cursor.isNull(updatedAtIndex) ? cursor.getString(updatedAtIndex) : "");
                    
                    requests.add(request);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error obteniendo todas las solicitudes: " + e.getMessage(), e);
        }
        return requests;
    }
    
    /**
     * Agrega un usuario (para restore)
     * NOTA: Los IDs se generan automáticamente, no se pueden restaurar IDs específicos
     * @param user Usuario a agregar
     * @return ID del usuario insertado
     */
    public long addUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            // No intentar insertar el ID - SQLite lo generará automáticamente
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_PHONE, user.getPhone());
            values.put(COLUMN_USER_ROLE, user.getRole());
            values.put(COLUMN_USER_STATUS, user.getStatus());
            
            // Campos opcionales
            if (columnExists(db, TABLE_USERS, COLUMN_USER_DESCRIPTION)) {
                values.put(COLUMN_USER_DESCRIPTION, user.getDescription() != null ? user.getDescription() : "");
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_PROFILE_IMAGE)) {
                values.put(COLUMN_USER_PROFILE_IMAGE, user.getProfileImage() != null ? user.getProfileImage() : "");
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_RATING)) {
                values.put(COLUMN_USER_RATING, user.getRating());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_TOTAL_RATINGS)) {
                values.put(COLUMN_USER_TOTAL_RATINGS, user.getTotalRatings());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_COMPLETED_SERVICES)) {
                values.put(COLUMN_USER_COMPLETED_SERVICES, user.getCompletedServices());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LAST_SERVICE_DATE)) {
                values.put(COLUMN_USER_LAST_SERVICE_DATE, user.getLastServiceDate() != null ? user.getLastServiceDate() : "");
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_LOCATION)) {
                values.put(COLUMN_USER_LOCATION, user.getLocation() != null ? user.getLocation() : "");
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_CREATED_AT)) {
                values.put(COLUMN_USER_CREATED_AT, user.getCreatedAt() != null ? user.getCreatedAt() : getCurrentDateTime());
            }
            if (columnExists(db, TABLE_USERS, COLUMN_USER_UPDATED_AT)) {
                values.put(COLUMN_USER_UPDATED_AT, getCurrentDateTime());
            }
            
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error agregando usuario: " + e.getMessage(), e);
            return -1;
        }
    }
    
    /**
     * Agrega un servicio (para restore)
     * NOTA: Los IDs se generan automáticamente, no se pueden restaurar IDs específicos
     * @param service Servicio a agregar
     * @return ID del servicio insertado
     */
    public long addService(Service service) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            // No intentar insertar el ID - SQLite lo generará automáticamente
            values.put(COLUMN_SERVICE_NAME, service.getName());
            values.put(COLUMN_SERVICE_DESCRIPTION, service.getDescription());
            values.put(COLUMN_SERVICE_PRICE, service.getPrice());
            values.put(COLUMN_SERVICE_DURATION, service.getDuration());
            values.put(COLUMN_SERVICE_CATEGORY, service.getCategory());
            values.put(COLUMN_SERVICE_STATUS, service.getStatus());
            
            if (columnExists(db, TABLE_SERVICES, COLUMN_SERVICE_CREATED_AT)) {
                values.put(COLUMN_SERVICE_CREATED_AT, getCurrentDateTime());
            }
            
            long result = db.insert(TABLE_SERVICES, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error agregando servicio: " + e.getMessage(), e);
            return -1;
        }
    }
    
    /**
     * Agrega una solicitud (para restore)
     * NOTA: Los IDs se generan automáticamente, no se pueden restaurar IDs específicos
     * Las relaciones (client_id, socia_id, service_id) se restauran correctamente
     * @param request Solicitud a agregar
     * @return ID de la solicitud insertada
     */
    public long addRequest(Request request) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            // No intentar insertar el ID - SQLite lo generará automáticamente
            // Pero sí restaurar las relaciones (estas deben referenciar IDs válidos)
            values.put(COLUMN_REQUEST_CLIENT_ID, request.getClientId());
            
            if (request.getSociaId() > 0) {
                values.put(COLUMN_REQUEST_SOCIA_ID, request.getSociaId());
            }
            
            values.put(COLUMN_REQUEST_SERVICE_ID, request.getServiceId());
            values.put(COLUMN_REQUEST_STATUS, request.getStatus());
            values.put(COLUMN_REQUEST_SCHEDULED_DATE, request.getScheduledDate());
            values.put(COLUMN_REQUEST_SCHEDULED_TIME, request.getScheduledTime());
            values.put(COLUMN_REQUEST_ADDRESS, request.getAddress());
            values.put(COLUMN_REQUEST_NOTES, request.getNotes() != null ? request.getNotes() : "");
            values.put(COLUMN_REQUEST_TOTAL_PRICE, request.getTotalPrice());
            values.put(COLUMN_REQUEST_PAYMENT_STATUS, request.getPaymentStatus());
            values.put(COLUMN_REQUEST_RATING, request.getRating());
            values.put(COLUMN_REQUEST_REVIEW, request.getReview() != null ? request.getReview() : "");
            
            if (columnExists(db, TABLE_REQUESTS, COLUMN_REQUEST_IS_ARCHIVED)) {
                values.put(COLUMN_REQUEST_IS_ARCHIVED, request.isArchived() ? 1 : 0);
            }
            
            if (columnExists(db, TABLE_REQUESTS, COLUMN_REQUEST_CREATED_AT)) {
                values.put(COLUMN_REQUEST_CREATED_AT, request.getCreatedAt() != null ? request.getCreatedAt() : getCurrentDateTime());
            }
            
            if (columnExists(db, TABLE_REQUESTS, COLUMN_REQUEST_UPDATED_AT)) {
                values.put(COLUMN_REQUEST_UPDATED_AT, request.getUpdatedAt() != null ? request.getUpdatedAt() : getCurrentDateTime());
            }
            
            long result = db.insert(TABLE_REQUESTS, null, values);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error agregando solicitud: " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Migra usuarios desde la base de datos antigua (loop_database_simple.db) a la nueva (loop_database.db)
     * Este método debe ser llamado una sola vez después de cambiar a DatabaseHelper
     * NOTA: Este método se mantiene por compatibilidad, pero SimpleDatabaseHelper ya no se usa
     */
    public void migrateUsersFromSimpleDatabase(Context context) {
        try {
            Log.d("DatabaseHelper", "Iniciando migración de usuarios desde base de datos antigua");
            
            // Abrir la base de datos simple (solo lectura)
            SQLiteDatabase simpleDb = context.openOrCreateDatabase("loop_database_simple.db", Context.MODE_PRIVATE, null);
            
            // Leer todos los usuarios de la base de datos simple
            Cursor cursor = simpleDb.query("users", null, null, null, null, null, null);
            
            int migratedCount = 0;
            int skippedCount = 0;
            
            if (cursor.moveToFirst()) {
                do {
                    try {
                        // Leer datos del usuario
                        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                        
                        // Verificar si el usuario ya existe en la nueva base de datos
                        if (emailExists(email)) {
                            Log.d("DatabaseHelper", "Usuario " + email + " ya existe, saltando...");
                            skippedCount++;
                            continue;
                        }
                        
                        // Crear objeto User
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
                        user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                        
                        // Campos opcionales
                        int descriptionIndex = cursor.getColumnIndex("description");
                        if (descriptionIndex >= 0 && !cursor.isNull(descriptionIndex)) {
                            user.setDescription(cursor.getString(descriptionIndex));
                        }
                        
                        int profileImageIndex = cursor.getColumnIndex("profile_image");
                        if (profileImageIndex >= 0 && !cursor.isNull(profileImageIndex)) {
                            user.setProfileImage(cursor.getString(profileImageIndex));
                        }
                        
                        int ratingIndex = cursor.getColumnIndex("rating");
                        if (ratingIndex >= 0 && !cursor.isNull(ratingIndex)) {
                            user.setRating(cursor.getDouble(ratingIndex));
                        }
                        
                        int totalRatingsIndex = cursor.getColumnIndex("total_ratings");
                        if (totalRatingsIndex >= 0 && !cursor.isNull(totalRatingsIndex)) {
                            user.setTotalRatings(cursor.getInt(totalRatingsIndex));
                        }
                        
                        int locationIndex = cursor.getColumnIndex("location");
                        if (locationIndex >= 0 && !cursor.isNull(locationIndex)) {
                            user.setLocation(cursor.getString(locationIndex));
                        }
                        
                        // Insertar en la nueva base de datos
                        long result = insertUser(user);
                        if (result != -1) {
                            migratedCount++;
                            Log.d("DatabaseHelper", "Usuario " + email + " migrado exitosamente");
                        } else {
                            Log.e("DatabaseHelper", "Error migrando usuario " + email);
                        }
                        
                    } catch (Exception e) {
                        Log.e("DatabaseHelper", "Error procesando usuario durante migración: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            }
            
            cursor.close();
            simpleDb.close();
            
            Log.i("DatabaseHelper", "Migración completada: " + migratedCount + " usuarios migrados, " + skippedCount + " saltados");
            
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error durante migración de usuarios: " + e.getMessage(), e);
        }
    }
}
