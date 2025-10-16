package com.example.loopv7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.adapters.RequestAdapter;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ArchivedRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView tvEmptyState;
    private View emptyStateLayout;
    private static final String TAG = "ArchivedRequestsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_requests);
        
        try {
            databaseHelper = new DatabaseHelper(this);
            sessionManager = new SessionManager(this);
            
            // Configurar toolbar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Solicitudes Archivadas");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            recyclerView = findViewById(R.id.recyclerViewArchivedRequests);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            
            // Initialize empty state views
            emptyStateLayout = findViewById(R.id.emptyStateLayout);
            tvEmptyState = findViewById(R.id.tvEmptyState);
            
            loadArchivedRequests();
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar solicitudes archivadas", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void loadArchivedRequests() {
        try {
            List<Request> archivedRequests = new ArrayList<>();
            int userId = sessionManager.getCurrentUserId();
            String userRole = sessionManager.getCurrentUserRole();
            
            Log.d(TAG, "Loading archived requests for user ID: " + userId + ", role: " + userRole);
            
            if (sessionManager.isCliente()) {
                // Cargar solicitudes archivadas del cliente
                List<Request> allRequests = databaseHelper.getRequestsByClientId(userId);
                for (Request request : allRequests) {
                    if (request.isArchived()) {
                        archivedRequests.add(request);
                    }
                }
                Log.d(TAG, "Found " + archivedRequests.size() + " archived requests for client");
            } else if (sessionManager.isSocia()) {
                // Para socias: cargar solicitudes archivadas donde son la socia asignada
                List<Request> allAcceptedRequests = databaseHelper.getRequestsByStatus("aceptada");
                List<Request> allCompletedRequests = databaseHelper.getRequestsByStatus("completada");
                
                for (Request request : allAcceptedRequests) {
                    if (request.getSociaId() == userId && request.isArchived()) {
                        archivedRequests.add(request);
                    }
                }
                for (Request request : allCompletedRequests) {
                    if (request.getSociaId() == userId && request.isArchived()) {
                        archivedRequests.add(request);
                    }
                }
                
                Log.d(TAG, "Found " + archivedRequests.size() + " archived requests for this socia");
            }
            
            if (archivedRequests.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
                requestAdapter = new RequestAdapter(archivedRequests, new RequestAdapter.OnRequestClickListener() {
                    @Override
                    public void onRequestClick(Request request) {
                        Intent intent = new Intent(ArchivedRequestsActivity.this, RequestDetailsActivity.class);
                        intent.putExtra("request_id", request.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(requestAdapter);
                Log.d(TAG, "Adapter set with " + archivedRequests.size() + " archived requests");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error loading archived requests: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar solicitudes archivadas: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void showEmptyState() {
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
        if (tvEmptyState != null) {
            String message = "No tienes solicitudes archivadas\n\nLas solicitudes se archivan automáticamente después de ser completadas y calificadas.";
            tvEmptyState.setText(message);
        }
        Log.d(TAG, "Showing empty state - no archived requests found");
    }
    
    private void hideEmptyState() {
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar solicitudes archivadas en caso de que haya cambios
        loadArchivedRequests();
    }
}
