package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.utils.SessionManager;

import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView tvEmptyState;
    private View emptyStateLayout;
    private static final String TAG = "RequestsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        
        try {
            databaseHelper = new SimpleDatabaseHelper(getContext());
            sessionManager = new SessionManager(getContext());
            
            recyclerView = view.findViewById(R.id.recyclerViewRequests);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            
            // Initialize empty state views
            emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
            tvEmptyState = view.findViewById(R.id.tvEmptyState);
            
            // Configurar título según el rol
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            if (sessionManager.isCliente()) {
                tvTitle.setText("Mis Solicitudes");
            } else if (sessionManager.isSocia()) {
                tvTitle.setText("Solicitudes Disponibles");
            }
            
            Log.d(TAG, "User role: " + sessionManager.getCurrentUserRole());
            Log.d(TAG, "User ID: " + sessionManager.getCurrentUserId());
            
            loadRequests();
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar solicitudes: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        return view;
    }
    
    private void loadRequests() {
        try {
            List<Request> requests;
            int userId = sessionManager.getCurrentUserId();
            String userRole = sessionManager.getCurrentUserRole();
            
            Log.d(TAG, "Loading requests for user ID: " + userId + ", role: " + userRole);
            
            if (sessionManager.isCliente()) {
                // Cargar solicitudes del cliente
                requests = databaseHelper.getRequestsByClientId(userId);
                Log.d(TAG, "Found " + requests.size() + " requests for client");
            } else if (sessionManager.isSocia()) {
                // Para socias: mostrar solo sus solicitudes aceptadas
                List<Request> myRequests = databaseHelper.getRequestsBySociaId(userId);
                
                Log.d(TAG, "Found " + myRequests.size() + " my requests");
                
                // Filtrar solo las solicitudes aceptadas
                requests = new java.util.ArrayList<>();
                for (Request request : myRequests) {
                    if ("aceptada".equals(request.getStatus()) || "completada".equals(request.getStatus())) {
                        requests.add(request);
                    }
                }
                
                Log.d(TAG, "Found " + requests.size() + " accepted/completed requests");
            } else {
                requests = databaseHelper.getPendingRequests();
                Log.d(TAG, "Found " + requests.size() + " pending requests (default)");
            }
            
            if (requests.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
                requestAdapter = new RequestAdapter(requests, new RequestAdapter.OnRequestClickListener() {
                    @Override
                    public void onRequestClick(Request request) {
                        Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                        intent.putExtra("request_id", request.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(requestAdapter);
                Log.d(TAG, "Adapter set with " + requests.size() + " requests");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error loading requests: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar solicitudes: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                "No tienes solicitudes creadas" : 
                "No hay solicitudes disponibles en este momento";
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
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh requests when fragment becomes visible
        if (databaseHelper != null && sessionManager != null) {
            loadRequests();
        }
    }
}
