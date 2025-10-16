package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.activities.CreateRequestActivity;
import com.example.loopv7.adapters.ServiceAdapter;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.Category;
import com.example.loopv7.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ServicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private FloatingActionButton fabCreateRequest;
    private Spinner spinnerCategories;
    private TextView tvCategoryTitle;
    private List<Category> categories;
    private List<Service> allServices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        
        databaseHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());
        
        recyclerView = view.findViewById(R.id.recyclerViewServices);
        fabCreateRequest = view.findViewById(R.id.fabCreateRequest);
        
        // Por ahora, no usar spinner de categorías hasta que se agregue al layout
        spinnerCategories = null;
        tvCategoryTitle = null;
        
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
        
        loadCategories();
        loadServices();
        setupCategorySpinner();
        
        return view;
    }
    
    private void loadServices() {
        allServices = databaseHelper.getAllServices();
        serviceAdapter = new ServiceAdapter(allServices, new ServiceAdapter.OnServiceClickListener() {
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
    
    private void loadCategories() {
        categories = databaseHelper.getAllCategories();
    }
    
    private void setupCategorySpinner() {
        if (spinnerCategories == null) {
            // Si no hay spinner en el layout, no hacer nada
            return;
        }
        
        if (categories == null || categories.isEmpty()) {
            // Si no hay categorías, ocultar el spinner
            spinnerCategories.setVisibility(View.GONE);
            if (tvCategoryTitle != null) {
                tvCategoryTitle.setVisibility(View.GONE);
            }
            return;
        }
        
        // Crear lista de nombres de categorías
        String[] categoryNames = new String[categories.size() + 1];
        categoryNames[0] = "Todas las categorías";
        
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i + 1] = categories.get(i).getName();
        }
        
        // Configurar adapter para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
        
        // Configurar listener para el spinner
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Mostrar todos los servicios
                    filterServicesByCategory(null);
                } else {
                    // Filtrar por categoría seleccionada
                    Category selectedCategory = categories.get(position - 1);
                    filterServicesByCategory(selectedCategory.getName());
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }
    
    private void filterServicesByCategory(String categoryName) {
        if (allServices == null) {
            return;
        }
        
        List<Service> filteredServices;
        
        if (categoryName == null) {
            // Mostrar todos los servicios
            filteredServices = allServices;
        } else {
            // Filtrar servicios por categoría
            filteredServices = new java.util.ArrayList<>();
            for (Service service : allServices) {
                if (categoryName.equals(service.getCategory())) {
                    filteredServices.add(service);
                }
            }
        }
        
        // Actualizar el adapter con los servicios filtrados
        serviceAdapter = new ServiceAdapter(filteredServices, new ServiceAdapter.OnServiceClickListener() {
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
