package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.List;

public class PendingRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView tvEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_requests, container, false);
        
        databaseHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());
        
        recyclerView = view.findViewById(R.id.recyclerViewPendingRequests);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadPendingRequests();
        
        return view;
    }
    
    private void loadPendingRequests() {
        List<Request> pendingRequests = databaseHelper.getRequestsByStatus("pendiente");
        
        if (pendingRequests.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("No hay solicitudes pendientes en este momento.\n\n¡Revisa más tarde!");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            
            requestAdapter = new RequestAdapter(pendingRequests, new RequestAdapter.OnRequestClickListener() {
                @Override
                public void onRequestClick(Request request) {
                    Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                    intent.putExtra("request_id", request.getId());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(requestAdapter);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar solicitudes cuando se regrese a este fragment
        loadPendingRequests();
    }
}
