package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.activities.CreateRequestActivity;
import com.example.loopv7.adapters.ServiceAdapter;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Service;
import com.example.loopv7.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ServicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private SimpleDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private FloatingActionButton fabCreateRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        
        databaseHelper = new SimpleDatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());
        
        recyclerView = view.findViewById(R.id.recyclerViewServices);
        fabCreateRequest = view.findViewById(R.id.fabCreateRequest);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Mostrar FAB solo para clientes
        if (sessionManager.isCliente()) {
            fabCreateRequest.setVisibility(View.VISIBLE);
            fabCreateRequest.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), CreateRequestActivity.class);
                startActivity(intent);
            });
        } else {
            fabCreateRequest.setVisibility(View.GONE);
        }
        
        loadServices();
        
        return view;
    }
    
    private void loadServices() {
        List<Service> services = databaseHelper.getAllServices();
        serviceAdapter = new ServiceAdapter(services, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Service service) {
                if (sessionManager.isCliente()) {
                    // Para clientes, navegar a crear solicitud
                    Intent intent = new Intent(getContext(), CreateRequestActivity.class);
                    intent.putExtra("service_id", service.getId());
                    startActivity(intent);
                } else {
                    // Para socias, mostrar información del servicio
                    Toast.makeText(getContext(), "Servicio: " + service.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(serviceAdapter);
    }
}
