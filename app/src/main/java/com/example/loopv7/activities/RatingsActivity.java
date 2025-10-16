package com.example.loopv7.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.adapters.RatingAdapter;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Rating;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.ErrorHandler;
import com.example.loopv7.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad para mostrar las calificaciones de una socia
 * 
 * Funcionalidades:
 * - Mostrar todas las calificaciones recibidas por la socia
 * - Mostrar estadísticas de calificaciones
 * - Permitir ver detalles de cada calificación
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class RatingsActivity extends AppCompatActivity {

    private TextView tvTitle, tvAverageRating, tvTotalRatings, tvNoRatings;
    private RecyclerView recyclerViewRatings;
    private LinearLayout layoutStats, layoutNoRatings;
    
    private DatabaseHelper databaseHelper;
    private ErrorHandler errorHandler;
    private SessionManager sessionManager;
    private RatingAdapter ratingAdapter;
    private List<Rating> ratings;
    private int sociaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        
        // Inicializar componentes
        databaseHelper = new DatabaseHelper(this);
        errorHandler = ErrorHandler.getInstance(this);
        sessionManager = new SessionManager(this);
        
        // Obtener ID de la socia
        sociaId = getIntent().getIntExtra("socia_id", -1);
        if (sociaId == -1) {
            errorHandler.handleError(ErrorHandler.ErrorType.VALIDATION_ERROR, "ID de socia no válido");
            finish();
            return;
        }
        
        initializeViews();
        loadRatings();
    }
    
    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        tvTotalRatings = findViewById(R.id.tvTotalRatings);
        tvNoRatings = findViewById(R.id.tvNoRatings);
        recyclerViewRatings = findViewById(R.id.recyclerViewRatings);
        layoutStats = findViewById(R.id.layoutStats);
        layoutNoRatings = findViewById(R.id.layoutNoRatings);
        
        // Configurar RecyclerView
        recyclerViewRatings.setLayoutManager(new LinearLayoutManager(this));
        
        // Configurar título
        User socia = databaseHelper.getUserById(sociaId);
        if (socia != null) {
            tvTitle.setText("⭐ Calificaciones de " + socia.getName());
        } else {
            tvTitle.setText("⭐ Mis Calificaciones");
        }
    }
    
    private void loadRatings() {
        try {
            // Obtener calificaciones de la socia
            ratings = databaseHelper.getRatingsByRatedId(sociaId);
            
            if (ratings != null && !ratings.isEmpty()) {
                // Mostrar estadísticas
                showStats();
                
                // Configurar adapter
                ratingAdapter = new RatingAdapter(ratings, this);
                recyclerViewRatings.setAdapter(ratingAdapter);
                
                // Mostrar contenido
                layoutStats.setVisibility(View.VISIBLE);
                layoutNoRatings.setVisibility(View.GONE);
                recyclerViewRatings.setVisibility(View.VISIBLE);
                
            } else {
                // No hay calificaciones
                showNoRatings();
            }
            
        } catch (Exception e) {
            errorHandler.handleError(ErrorHandler.ErrorType.UNKNOWN_ERROR, 
                "Error al cargar calificaciones", e);
            showNoRatings();
        }
    }
    
    private void showStats() {
        if (ratings == null || ratings.isEmpty()) {
            return;
        }
        
        // Calcular promedio
        double totalRating = 0;
        for (Rating rating : ratings) {
            totalRating += rating.getOverallRating();
        }
        double averageRating = totalRating / ratings.size();
        
        // Mostrar estadísticas
        tvAverageRating.setText(String.format("%.1f ⭐", averageRating));
        tvTotalRatings.setText(ratings.size() + " calificaciones");
    }
    
    private void showNoRatings() {
        layoutStats.setVisibility(View.GONE);
        layoutNoRatings.setVisibility(View.VISIBLE);
        recyclerViewRatings.setVisibility(View.GONE);
        
        tvNoRatings.setText("Aún no tienes calificaciones.\n¡Completa servicios para recibir calificaciones!");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar calificaciones cuando se vuelve a la actividad
        loadRatings();
    }
}
