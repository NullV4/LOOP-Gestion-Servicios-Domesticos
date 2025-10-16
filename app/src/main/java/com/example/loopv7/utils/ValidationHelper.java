package com.example.loopv7.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Clase helper para validaciones de datos en la aplicación LOOP
 * 
 * Funcionalidades:
 * - Validación de emails (formato y unicidad)
 * - Validación de teléfonos peruanos
 * - Validación de fechas y horarios
 * - Validación de precios y montos
 * - Validación de textos y direcciones
 * - Validación de contraseñas
 * - Validación de datos de tarjetas de crédito
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class ValidationHelper {
    
    private static final String TAG = "ValidationHelper";
    
    // Patrones de validación
    private static final Pattern PHONE_PATTERN = Pattern.compile("^9[0-9]{8}$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{16}$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3,4}$");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/([0-9]{2})$");
    
    // Formatos de fecha
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    
    private Context context;
    private SimpleDatabaseHelper databaseHelper;
    
    public ValidationHelper(Context context) {
        this.context = context;
        this.databaseHelper = new SimpleDatabaseHelper(context);
    }
    
    /**
     * Valida un email
     * @param email Email a validar
     * @param checkUniqueness Si debe verificar unicidad en la base de datos
     * @return Resultado de la validación
     */
    public ValidationResult validateEmail(String email, boolean checkUniqueness) {
        if (TextUtils.isEmpty(email)) {
            return new ValidationResult(false, "El email es requerido");
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ValidationResult(false, "El formato del email no es válido");
        }
        
        if (email.length() > 100) {
            return new ValidationResult(false, "El email no puede tener más de 100 caracteres");
        }
        
        if (checkUniqueness && databaseHelper.emailExists(email)) {
            return new ValidationResult(false, "Este email ya está registrado");
        }
        
        return new ValidationResult(true, "Email válido");
    }
    
    /**
     * Valida un teléfono peruano
     * @param phone Teléfono a validar
     * @return Resultado de la validación
     */
    public ValidationResult validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return new ValidationResult(false, "El teléfono es requerido");
        }
        
        // Remover espacios y caracteres especiales
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            return new ValidationResult(false, "El teléfono debe tener 9 dígitos y comenzar con 9");
        }
        
        return new ValidationResult(true, "Teléfono válido");
    }
    
    /**
     * Valida una contraseña
     * @param password Contraseña a validar
     * @return Resultado de la validación
     */
    public ValidationResult validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return new ValidationResult(false, "La contraseña es requerida");
        }
        
        if (password.length() < 6) {
            return new ValidationResult(false, "La contraseña debe tener al menos 6 caracteres");
        }
        
        if (password.length() > 50) {
            return new ValidationResult(false, "La contraseña no puede tener más de 50 caracteres");
        }
        
        // Verificar que tenga al menos una letra y un número
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        
        if (!hasLetter || !hasNumber) {
            return new ValidationResult(false, "La contraseña debe contener al menos una letra y un número");
        }
        
        return new ValidationResult(true, "Contraseña válida");
    }
    
    /**
     * Valida la confirmación de contraseña
     * @param password Contraseña original
     * @param confirmPassword Confirmación de contraseña
     * @return Resultado de la validación
     */
    public ValidationResult validatePasswordConfirmation(String password, String confirmPassword) {
        if (TextUtils.isEmpty(confirmPassword)) {
            return new ValidationResult(false, "La confirmación de contraseña es requerida");
        }
        
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "Las contraseñas no coinciden");
        }
        
        return new ValidationResult(true, "Confirmación válida");
    }
    
    /**
     * Valida un nombre
     * @param name Nombre a validar
     * @return Resultado de la validación
     */
    public ValidationResult validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            return new ValidationResult(false, "El nombre es requerido");
        }
        
        String trimmedName = name.trim();
        
        if (trimmedName.length() < 2) {
            return new ValidationResult(false, "El nombre debe tener al menos 2 caracteres");
        }
        
        if (trimmedName.length() > 50) {
            return new ValidationResult(false, "El nombre no puede tener más de 50 caracteres");
        }
        
        // Verificar que solo contenga letras, espacios y algunos caracteres especiales
        if (!trimmedName.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-\\.]+$")) {
            return new ValidationResult(false, "El nombre solo puede contener letras, espacios, guiones y puntos");
        }
        
        return new ValidationResult(true, "Nombre válido");
    }
    
    /**
     * Valida una fecha
     * @param dateString Fecha en formato yyyy-MM-dd
     * @param mustBeFuture Si la fecha debe ser futura
     * @return Resultado de la validación
     */
    public ValidationResult validateDate(String dateString, boolean mustBeFuture) {
        if (TextUtils.isEmpty(dateString)) {
            return new ValidationResult(false, "La fecha es requerida");
        }
        
        try {
            Date date = DATE_FORMAT.parse(dateString);
            Date today = new Date();
            
            if (mustBeFuture && date.before(today)) {
                return new ValidationResult(false, "La fecha debe ser futura");
            }
            
            // Verificar que no sea más de 1 año en el futuro
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.YEAR, 1);
            Date maxDate = calendar.getTime();
            
            if (date.after(maxDate)) {
                return new ValidationResult(false, "La fecha no puede ser más de 1 año en el futuro");
            }
            
            return new ValidationResult(true, "Fecha válida");
            
        } catch (ParseException e) {
            return new ValidationResult(false, "El formato de fecha no es válido (yyyy-MM-dd)");
        }
    }
    
    /**
     * Valida un horario
     * @param timeString Horario en formato HH:mm
     * @return Resultado de la validación
     */
    public ValidationResult validateTime(String timeString) {
        if (TextUtils.isEmpty(timeString)) {
            return new ValidationResult(false, "El horario es requerido");
        }
        
        try {
            TIME_FORMAT.parse(timeString);
            
            // Verificar que esté en horario laboral (6:00 - 22:00)
            String[] timeParts = timeString.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            
            if (hour < 6 || hour > 22) {
                return new ValidationResult(false, "El horario debe estar entre 6:00 y 22:00");
            }
            
            return new ValidationResult(true, "Horario válido");
            
        } catch (ParseException | NumberFormatException e) {
            return new ValidationResult(false, "El formato de horario no es válido (HH:mm)");
        }
    }
    
    /**
     * Valida un precio
     * @param priceString Precio como string
     * @return Resultado de la validación
     */
    public ValidationResult validatePrice(String priceString) {
        if (TextUtils.isEmpty(priceString)) {
            return new ValidationResult(false, "El precio es requerido");
        }
        
        try {
            double price = Double.parseDouble(priceString);
            
            if (price <= 0) {
                return new ValidationResult(false, "El precio debe ser mayor a 0");
            }
            
            if (price > 10000) {
                return new ValidationResult(false, "El precio no puede ser mayor a S/ 10,000");
            }
            
            // Verificar que no tenga más de 2 decimales
            if (priceString.contains(".")) {
                String[] parts = priceString.split("\\.");
                if (parts.length > 1 && parts[1].length() > 2) {
                    return new ValidationResult(false, "El precio no puede tener más de 2 decimales");
                }
            }
            
            return new ValidationResult(true, "Precio válido");
            
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "El precio debe ser un número válido");
        }
    }
    
    /**
     * Valida una dirección
     * @param address Dirección a validar
     * @return Resultado de la validación
     */
    public ValidationResult validateAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return new ValidationResult(false, "La dirección es requerida");
        }
        
        String trimmedAddress = address.trim();
        
        if (trimmedAddress.length() < 10) {
            return new ValidationResult(false, "La dirección debe tener al menos 10 caracteres");
        }
        
        if (trimmedAddress.length() > 200) {
            return new ValidationResult(false, "La dirección no puede tener más de 200 caracteres");
        }
        
        return new ValidationResult(true, "Dirección válida");
    }
    
    /**
     * Valida las notas de una solicitud
     * @param notes Notas a validar
     * @return Resultado de la validación
     */
    public ValidationResult validateNotes(String notes) {
        if (TextUtils.isEmpty(notes)) {
            return new ValidationResult(true, "Notas válidas"); // Las notas son opcionales
        }
        
        if (notes.length() > 500) {
            return new ValidationResult(false, "Las notas no pueden tener más de 500 caracteres");
        }
        
        return new ValidationResult(true, "Notas válidas");
    }
    
    /**
     * Valida datos de tarjeta de crédito
     * @param cardNumber Número de tarjeta
     * @param expiryDate Fecha de vencimiento
     * @param cvv CVV
     * @param cardHolder Nombre del titular
     * @return Resultado de la validación
     */
    public ValidationResult validateCardData(String cardNumber, String expiryDate, String cvv, String cardHolder) {
        // Validar número de tarjeta
        if (TextUtils.isEmpty(cardNumber)) {
            return new ValidationResult(false, "El número de tarjeta es requerido");
        }
        
        String cleanCardNumber = cardNumber.replaceAll("\\s", "");
        if (!CARD_NUMBER_PATTERN.matcher(cleanCardNumber).matches()) {
            return new ValidationResult(false, "El número de tarjeta debe tener 16 dígitos");
        }
        
        // Validar fecha de vencimiento
        if (TextUtils.isEmpty(expiryDate)) {
            return new ValidationResult(false, "La fecha de vencimiento es requerida");
        }
        
        if (!EXPIRY_PATTERN.matcher(expiryDate).matches()) {
            return new ValidationResult(false, "La fecha de vencimiento debe estar en formato MM/YY");
        }
        
        // Validar que la tarjeta no esté vencida
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt("20" + parts[1]);
            
            Calendar expiry = Calendar.getInstance();
            expiry.set(year, month - 1, 1);
            expiry.set(Calendar.DAY_OF_MONTH, expiry.getActualMaximum(Calendar.DAY_OF_MONTH));
            
            if (expiry.before(Calendar.getInstance())) {
                return new ValidationResult(false, "La tarjeta está vencida");
            }
        } catch (Exception e) {
            return new ValidationResult(false, "Fecha de vencimiento inválida");
        }
        
        // Validar CVV
        if (TextUtils.isEmpty(cvv)) {
            return new ValidationResult(false, "El CVV es requerido");
        }
        
        if (!CVV_PATTERN.matcher(cvv).matches()) {
            return new ValidationResult(false, "El CVV debe tener 3 o 4 dígitos");
        }
        
        // Validar nombre del titular
        if (TextUtils.isEmpty(cardHolder)) {
            return new ValidationResult(false, "El nombre del titular es requerido");
        }
        
        String trimmedHolder = cardHolder.trim();
        if (trimmedHolder.length() < 2) {
            return new ValidationResult(false, "El nombre del titular debe tener al menos 2 caracteres");
        }
        
        if (!trimmedHolder.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            return new ValidationResult(false, "El nombre del titular solo puede contener letras y espacios");
        }
        
        return new ValidationResult(true, "Datos de tarjeta válidos");
    }
    
    /**
     * Valida una calificación
     * @param rating Calificación (1-5)
     * @return Resultado de la validación
     */
    public ValidationResult validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            return new ValidationResult(false, "La calificación debe estar entre 1 y 5");
        }
        
        return new ValidationResult(true, "Calificación válida");
    }
    
    /**
     * Valida una reseña
     * @param review Reseña a validar
     * @return Resultado de la validación
     */
    public ValidationResult validateReview(String review) {
        if (TextUtils.isEmpty(review)) {
            return new ValidationResult(false, "La reseña es requerida");
        }
        
        String trimmedReview = review.trim();
        
        if (trimmedReview.length() < 10) {
            return new ValidationResult(false, "La reseña debe tener al menos 10 caracteres");
        }
        
        if (trimmedReview.length() > 500) {
            return new ValidationResult(false, "La reseña no puede tener más de 500 caracteres");
        }
        
        return new ValidationResult(true, "Reseña válida");
    }
    
    /**
     * Valida un EditText y muestra error si es inválido
     * @param editText EditText a validar
     * @param validator Función de validación
     * @return true si es válido, false si no
     */
    public boolean validateEditText(EditText editText, TextValidator validator) {
        String text = editText.getText().toString().trim();
        ValidationResult result = validator.validate(text);
        
        if (!result.isValid()) {
            editText.setError(result.getMessage());
            editText.requestFocus();
            return false;
        }
        
        editText.setError(null);
        return true;
    }
    
    /**
     * Interfaz para validadores de texto
     */
    public interface TextValidator {
        ValidationResult validate(String text);
    }
    
    /**
     * Clase para resultados de validación
     */
    public static class ValidationResult {
        private boolean valid;
        private String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
