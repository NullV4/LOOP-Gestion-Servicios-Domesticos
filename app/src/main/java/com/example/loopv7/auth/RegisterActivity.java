package com.example.loopv7.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.ValidationHelper;
import com.example.loopv7.utils.ErrorHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Spinner spinnerRole;
    private Button btnRegister;
    private TextView tvLogin;
    private SimpleDatabaseHelper databaseHelper;
    private ValidationHelper validationHelper;
    private ErrorHandler errorHandler;
    private String selectedRole = "cliente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar componentes
        databaseHelper = new SimpleDatabaseHelper(this);
        validationHelper = new ValidationHelper(this);
        errorHandler = ErrorHandler.getInstance(this);

        // Inicializar vistas
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Configurar spinner de roles
        setupRoleSpinner();

        // Configurar listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegister();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupRoleSpinner() {
        String[] roles = {"Cliente", "Socia Trabajadora"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = position == 0 ? "cliente" : "socia";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = "cliente";
            }
        });
    }

    private void performRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones robustas usando ValidationHelper
        boolean isValid = true;
        
        // Validar nombre
        ValidationHelper.ValidationResult nameResult = validationHelper.validateName(name);
        if (!nameResult.isValid()) {
            etName.setError(nameResult.getMessage());
            etName.requestFocus();
            isValid = false;
        } else {
            etName.setError(null);
        }
        
        // Validar email
        ValidationHelper.ValidationResult emailResult = validationHelper.validateEmail(email, true);
        if (!emailResult.isValid()) {
            etEmail.setError(emailResult.getMessage());
            etEmail.requestFocus();
            isValid = false;
        } else {
            etEmail.setError(null);
        }
        
        // Validar teléfono
        ValidationHelper.ValidationResult phoneResult = validationHelper.validatePhone(phone);
        if (!phoneResult.isValid()) {
            etPhone.setError(phoneResult.getMessage());
            etPhone.requestFocus();
            isValid = false;
        } else {
            etPhone.setError(null);
        }
        
        // Validar contraseña
        ValidationHelper.ValidationResult passwordResult = validationHelper.validatePassword(password);
        if (!passwordResult.isValid()) {
            etPassword.setError(passwordResult.getMessage());
            etPassword.requestFocus();
            isValid = false;
        } else {
            etPassword.setError(null);
        }
        
        // Validar confirmación de contraseña
        ValidationHelper.ValidationResult confirmResult = validationHelper.validatePasswordConfirmation(password, confirmPassword);
        if (!confirmResult.isValid()) {
            etConfirmPassword.setError(confirmResult.getMessage());
            etConfirmPassword.requestFocus();
            isValid = false;
        } else {
            etConfirmPassword.setError(null);
        }
        
        if (!isValid) {
            return;
        }

        // Crear nuevo usuario
        try {
            User newUser = new User(email, password, name, phone, selectedRole);
            long result = databaseHelper.insertUser(newUser);

            if (result != -1) {
                Toast.makeText(this, "Registro exitoso. Ahora puede iniciar sesión.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                errorHandler.handleError(ErrorHandler.ErrorType.DATABASE_ERROR, 
                    "No se pudo crear el usuario en la base de datos");
            }
        } catch (Exception e) {
            errorHandler.handleCriticalError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error inesperado durante el registro", e);
        }
    }
}
