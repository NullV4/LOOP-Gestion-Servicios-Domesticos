package com.example.loopv7.fragments;

import android.os.Bundle;
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
import com.example.loopv7.adapters.NotificationAdapter;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.Notification;
import com.example.loopv7.utils.SessionManager;

import java.util.List;

/**
 * Fragment para mostrar las notificaciones del usuario
 * 
 * Funcionalidades:
 * - Mostrar lista de notificaciones
 * - Marcar notificaciones como leídas
 * - Filtrar por tipo de notificación
 * - Acceso rápido a elementos relacionados
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView tvEmptyState;
    private View emptyStateLayout;
    private static final String TAG = "NotificationsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        try {
            databaseHelper = new DatabaseHelper(getContext());
            sessionManager = new SessionManager(getContext());
            
            recyclerView = view.findViewById(R.id.recyclerViewNotifications);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            
            // Initialize empty state views
            emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
            tvEmptyState = view.findViewById(R.id.tvEmptyState);
            
            // Configurar título
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText("📬 Mis Notificaciones");
            
            loadNotifications();
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar notificaciones: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        return view;
    }
    
    private void loadNotifications() {
        try {
            int userId = sessionManager.getCurrentUserId();
            List<Notification> notifications = databaseHelper.getUserNotifications(userId);
            
            android.util.Log.d(TAG, "Loading notifications for user ID: " + userId);
            android.util.Log.d(TAG, "Found " + notifications.size() + " notifications");
            
            if (notifications.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
                notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.OnNotificationClickListener() {
                    @Override
                    public void onNotificationClick(Notification notification) {
                        // Marcar notificación como leída
                        if (!notification.isRead()) {
                            databaseHelper.markNotificationAsRead(notification.getId());
                            notification.setIsRead(true);
                            notificationAdapter.notifyDataSetChanged();
                        }
                        
                        // Manejar navegación según el tipo de notificación
                        handleNotificationNavigation(notification);
                    }
                    
                    @Override
                    public void onMarkAsReadClick(Notification notification) {
                        if (!notification.isRead()) {
                            if (databaseHelper.markNotificationAsRead(notification.getId())) {
                                notification.setIsRead(true);
                                notificationAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Notificación marcada como leída", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                recyclerView.setAdapter(notificationAdapter);
                android.util.Log.d(TAG, "Adapter set with " + notifications.size() + " notifications");
            }
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading notifications: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error al cargar notificaciones: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void handleNotificationNavigation(Notification notification) {
        // Navegar según el tipo de referencia
        if (notification.getReferenceType() != null && notification.getReferenceId() > 0) {
            switch (notification.getReferenceType()) {
                case "request":
                    // Navegar a detalles de solicitud
                    android.content.Intent intent = new android.content.Intent(getContext(), 
                            com.example.loopv7.activities.RequestDetailsActivity.class);
                    intent.putExtra("request_id", notification.getReferenceId());
                    startActivity(intent);
                    break;
                case "payment":
                    // Navegar a detalles de pago
                    Toast.makeText(getContext(), "Ver detalles de pago", Toast.LENGTH_SHORT).show();
                    break;
                case "rating":
                    // Navegar a detalles de calificación
                    Toast.makeText(getContext(), "Ver detalles de calificación", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "Notificación: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Notificación: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
        if (tvEmptyState != null) {
            tvEmptyState.setText("No tienes notificaciones\n¡Mantente al día con tus servicios!");
        }
    }
    
    private void hideEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar notificaciones cuando el fragment se vuelve visible
        loadNotifications();
    }
}
