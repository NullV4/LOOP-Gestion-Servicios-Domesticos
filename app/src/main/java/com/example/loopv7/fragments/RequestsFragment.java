package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.activities.RequestDetailsActivity;
import com.example.loopv7.adapters.RequestAdapter;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView tvEmptyState;
    private View emptyStateLayout;
    private TextInputEditText etSearch;
    private List<Request> allRequests;
    private List<Request> filteredRequests;
    private static final String TAG = "RequestsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        
        try {
            databaseHelper = new DatabaseHelper(getContext());
            sessionManager = new SessionManager(getContext());
            
            recyclerView = view.findViewById(R.id.recyclerViewRequests);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            
            // Initialize empty state views
            emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
            tvEmptyState = view.findViewById(R.id.tvEmptyState);
            etSearch = view.findViewById(R.id.etSearch);
            
            // Configurar título según el rol
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            if (sessionManager.isCliente()) {
                tvTitle.setText("Mis Solicitudes");
            } else if (sessionManager.isSocia()) {
                tvTitle.setText("Solicitudes Aceptadas");
            }
            
            Log.d(TAG, "User role: " + sessionManager.getCurrentUserRole());
            Log.d(TAG, "User ID: " + sessionManager.getCurrentUserId());
            
            loadRequests();
            setupSearch();
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar solicitudes: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        return view;
    }
    
    private void loadRequests() {
        try {
            int userId = sessionManager.getCurrentUserId();
            String userRole = sessionManager.getCurrentUserRole();
            
            Log.d(TAG, "Loading requests for user ID: " + userId + ", role: " + userRole);
            
            if (sessionManager.isCliente()) {
                // Cargar solicitudes del cliente (excluyendo archivadas)
                List<Request> clientRequests = databaseHelper.getRequestsByClientId(userId);
                allRequests = new java.util.ArrayList<>();
                for (Request request : clientRequests) {
                    if (!request.isArchived()) {
                        allRequests.add(request);
                    }
                }
                Log.d(TAG, "Found " + allRequests.size() + " non-archived requests for client");
            } else if (sessionManager.isSocia()) {
                // Para socias: mostrar solicitudes aceptadas, en progreso y completadas (excluyendo archivadas)
                // Obtener todas las solicitudes de esta socia
                List<Request> sociaRequests = databaseHelper.getRequestsBySociaId(userId);
                
                // Filtrar solo las no archivadas y en estados relevantes
                allRequests = new java.util.ArrayList<>();
                for (Request request : sociaRequests) {
                    // Excluir archivadas
                    if (request.isArchived()) {
                        continue;
                    }
                    
                    // Solo incluir aceptadas, en progreso o completadas
                    String status = request.getStatus();
                    if ("aceptada".equals(status) || "en_progreso".equals(status) || "completada".equals(status)) {
                        // Si está completada y calificada, archivarla automáticamente
                        if ("completada".equals(status) && request.getRating() > 0) {
                            request.setArchived(true);
                            databaseHelper.updateRequest(request);
                            Log.d(TAG, "Request " + request.getId() + " archivada automáticamente (completada y calificada)");
                            continue; // No mostrar en la lista
                        }
                        allRequests.add(request);
                    }
                }
                
                Log.d(TAG, "Found " + allRequests.size() + " non-archived accepted/in-progress/completed requests for this socia");
            } else {
                // Para usuarios sin rol específico, mostrar solicitudes pendientes (excluyendo archivadas)
                List<Request> allPendingRequests = databaseHelper.getRequestsByStatus("pendiente");
                allRequests = new java.util.ArrayList<>();
                for (Request request : allPendingRequests) {
                    if (!request.isArchived()) {
                        allRequests.add(request);
                    }
                }
                Log.d(TAG, "Found " + allRequests.size() + " non-archived pending requests (default)");
            }
            
            filteredRequests = new java.util.ArrayList<>(allRequests);
            updateRequestAdapter();
            
        } catch (Exception e) {
            Log.e(TAG, "Error loading requests: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar solicitudes: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void updateRequestAdapter() {
        if (filteredRequests.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            requestAdapter = new RequestAdapter(filteredRequests, new RequestAdapter.OnRequestClickListener() {
                @Override
                public void onRequestClick(Request request) {
                    Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                    intent.putExtra("request_id", request.getId());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(requestAdapter);
            Log.d(TAG, "Adapter set with " + filteredRequests.size() + " requests");
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
            String message = sessionManager.isCliente() ? 
                "No tienes solicitudes activas\n(Las solicitudes completadas y calificadas se archivan automáticamente)" : 
                "No hay solicitudes disponibles en este momento\n(Las solicitudes completadas y calificadas se archivan automáticamente)";
            tvEmptyState.setText(message);
        }
        Log.d(TAG, "Showing empty state - no requests found");
    }
    
    private void hideEmptyState() {
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
    
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRequests(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void filterRequests(String query) {
        filteredRequests.clear();
        
        if (query.isEmpty()) {
            filteredRequests.addAll(allRequests);
        } else {
            String searchQuery = query.toLowerCase().trim();
            for (Request request : allRequests) {
                // Buscar por ID de solicitud, dirección, notas, estado, etc.
                if (String.valueOf(request.getId()).contains(searchQuery) ||
                    request.getAddress().toLowerCase().contains(searchQuery) ||
                    (request.getNotes() != null && request.getNotes().toLowerCase().contains(searchQuery)) ||
                    request.getStatus().toLowerCase().contains(searchQuery) ||
                    request.getScheduledDate().toLowerCase().contains(searchQuery) ||
                    request.getScheduledTime().toLowerCase().contains(searchQuery)) {
                    filteredRequests.add(request);
                }
            }
        }
        
        updateRequestAdapter();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh requests when fragment becomes visible
        if (databaseHelper != null && sessionManager != null) {
            loadRequests();
        }
    }
}

