package com.example.loopv7.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loopv7.MainActivity;
import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            // Inicializar componentes
            databaseHelper = new SimpleDatabaseHelper(this);
            sessionManager = new SessionManager(this);

            // Verificar si ya está logueado
            if (sessionManager.isLoggedIn()) {
                redirectToMainActivity();
                return;
            }

            // Inicializar vistas
            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);
            tvRegister = findViewById(R.id.tvRegister);

            // Configurar listeners
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogin();
                }
            });

            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "Error al inicializar la aplicación: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void performLogin() {
        try {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validaciones
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("El email es requerido");
                etEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("La contraseña es requerida");
                etPassword.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Ingrese un email válido");
                etEmail.requestFocus();
                return;
            }

            // Buscar usuario en la base de datos
            User user = databaseHelper.getUserByEmail(email);
            
            if (user == null) {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!user.getPassword().equals(password)) {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!"activo".equals(user.getStatus())) {
                Toast.makeText(this, "Usuario inactivo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login exitoso
            sessionManager.createLoginSession(user);
            Toast.makeText(this, "Bienvenido " + user.getName(), Toast.LENGTH_SHORT).show();
            redirectToMainActivity();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar sesión: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
