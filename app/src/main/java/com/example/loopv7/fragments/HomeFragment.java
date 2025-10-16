package com.example.loopv7.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.MainActivity;
import com.example.loopv7.R;
import com.example.loopv7.activities.CreateRequestActivity;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvWelcome, tvUserInfo, tvStatsTitle;
    private TextView tvTotalRequests, tvPendingRequests, tvCompletedRequests, tvTotalEarnings;
    private TextView tvPendingLabel, tvCompletedLabel, tvEarningsLabel;
    private LinearLayout layoutStats, layoutQuickActions;
    private Button btnCreateRequest, btnViewRequests, btnViewServices;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        sessionManager = new SessionManager(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        
        initializeViews(view);
        loadUserData();
        loadStatistics();
        setupListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        tvStatsTitle = view.findViewById(R.id.tvStatsTitle);
        
        tvTotalRequests = view.findViewById(R.id.tvTotalRequests);
        tvPendingRequests = view.findViewById(R.id.tvPendingRequests);
        tvCompletedRequests = view.findViewById(R.id.tvCompletedRequests);
        tvTotalEarnings = view.findViewById(R.id.tvTotalEarnings);
        
        tvPendingLabel = view.findViewById(R.id.tvPendingLabel);
        tvCompletedLabel = view.findViewById(R.id.tvCompletedLabel);
        tvEarningsLabel = view.findViewById(R.id.tvEarningsLabel);
        
        layoutStats = view.findViewById(R.id.layoutStats);
        layoutQuickActions = view.findViewById(R.id.layoutQuickActions);
        
        btnCreateRequest = view.findViewById(R.id.btnCreateRequest);
        btnViewRequests = view.findViewById(R.id.btnViewRequests);
        btnViewServices = view.findViewById(R.id.btnViewServices);
    }
    
    private void loadUserData() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            tvWelcome.setText("¡Hola, " + currentUser.getName() + "!");
            tvUserInfo.setText("Rol: " + (currentUser.isSocia() ? "Socia" : "Cliente"));
            
            // Configurar título y etiquetas según el rol
            if (currentUser.isSocia()) {
                tvStatsTitle.setText("📊 Mis Estadísticas");
                tvPendingLabel.setText("Pendientes");
                tvCompletedLabel.setText("Completadas");
                tvEarningsLabel.setText("Ganancias");
            } else {
                tvStatsTitle.setText("📊 Mis Solicitudes");
                tvPendingLabel.setText("En Proceso");
                tvCompletedLabel.setText("Finalizadas");
                tvEarningsLabel.setText("Gastos");
            }
        }
    }
    
    private void loadStatistics() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) return;
        
        try {
            List<Request> userRequests;
            
            if (currentUser.isSocia()) {
                // Para socias: solicitudes donde son la socia asignada
                userRequests = databaseHelper.getRequestsBySociaId(currentUser.getId());
            } else {
                // Para clientes: sus propias solicitudes
                userRequests = databaseHelper.getRequestsByClientId(currentUser.getId());
            }
            
            // Calcular estadísticas
            int totalRequests = userRequests.size();
            int pendingRequests = 0;
            int completedRequests = 0;
            double totalEarnings = 0.0;
            
            for (Request request : userRequests) {
                if ("pendiente".equals(request.getStatus()) || "aceptada".equals(request.getStatus())) {
                    pendingRequests++;
                } else if ("completada".equals(request.getStatus())) {
                    completedRequests++;
                    if (currentUser.isSocia() && "pagado".equals(request.getPaymentStatus())) {
                        totalEarnings += request.getTotalPrice();
                    }
                }
            }
            
            // Actualizar UI
            tvTotalRequests.setText(String.valueOf(totalRequests));
            tvPendingRequests.setText(String.valueOf(pendingRequests));
            tvCompletedRequests.setText(String.valueOf(completedRequests));
            
            if (currentUser.isSocia()) {
                tvTotalEarnings.setText("S/ " + String.format("%.2f", totalEarnings));
            } else {
                // Para clientes, mostrar gasto total
                double totalSpent = 0.0;
                for (Request request : userRequests) {
                    if ("pagado".equals(request.getPaymentStatus())) {
                        totalSpent += request.getTotalPrice();
                    }
                }
                tvTotalEarnings.setText("S/ " + String.format("%.2f", totalSpent));
            }
            
        } catch (Exception e) {
            // Manejar errores silenciosamente
            tvTotalRequests.setText("0");
            tvPendingRequests.setText("0");
            tvCompletedRequests.setText("0");
            tvTotalEarnings.setText("S/ 0.00");
        }
    }
    
    private void setupListeners() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) return;
        
        if (currentUser.isSocia()) {
            // Para socias: ocultar botón de crear solicitud, mostrar botón de solicitudes
            btnCreateRequest.setVisibility(View.GONE);
            btnViewRequests.setVisibility(View.VISIBLE);
            btnViewRequests.setText("Ver Mis Solicitudes");
            btnViewServices.setText("Ver Servicios Disponibles");
            
            btnViewRequests.setOnClickListener(v -> {
                // Navegar a la pestaña de solicitudes
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.selectFragment(1); // Índice de RequestsFragment
                }
            });
        } else {
            // Para clientes: mostrar botón de crear solicitud, ocultar botón de solicitudes
            btnCreateRequest.setVisibility(View.VISIBLE);
            btnViewRequests.setVisibility(View.GONE);
            btnViewServices.setText("Ver Servicios");
            
            btnCreateRequest.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), CreateRequestActivity.class);
                startActivity(intent);
            });
        }
        
        btnViewServices.setOnClickListener(v -> {
            // Navegar a la pestaña de servicios
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.selectFragment(2); // Índice de ServicesFragment
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar estadísticas cuando el fragment se vuelve visible
        loadStatistics();
    }
}