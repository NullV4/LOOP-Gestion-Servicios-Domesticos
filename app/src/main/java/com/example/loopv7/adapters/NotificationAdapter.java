package com.example.loopv7.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.models.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para mostrar notificaciones en RecyclerView
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private OnNotificationClickListener clickListener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
        void onMarkAsReadClick(Notification notification);
    }

    public NotificationAdapter(List<Notification> notifications, OnNotificationClickListener clickListener) {
        this.notifications = notifications;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvMessage, tvTime, tvCategory;
        private ImageView ivIcon, ivUnreadIndicator;
        private View layoutNotification;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
            tvCategory = itemView.findViewById(R.id.tvNotificationCategory);
            ivIcon = itemView.findViewById(R.id.ivNotificationIcon);
            ivUnreadIndicator = itemView.findViewById(R.id.ivUnreadIndicator);
            layoutNotification = itemView.findViewById(R.id.layoutNotification);
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getTitle());
            tvMessage.setText(notification.getMessage());
            tvCategory.setText(getCategoryDisplayName(notification.getCategory()));
            tvTime.setText(formatTime(notification.getCreatedAt()));
            
            // Configurar icono según el tipo
            setNotificationIcon(notification.getType());
            
            // Configurar indicador de no leído
            if (notification.isRead()) {
                ivUnreadIndicator.setVisibility(View.GONE);
                layoutNotification.setAlpha(0.7f);
            } else {
                ivUnreadIndicator.setVisibility(View.VISIBLE);
                layoutNotification.setAlpha(1.0f);
            }
            
            // Configurar click listeners
            layoutNotification.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onNotificationClick(notification);
                }
            });
            
            ivUnreadIndicator.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onMarkAsReadClick(notification);
                }
            });
        }
        
        private void setNotificationIcon(String type) {
            int iconRes;
            switch (type) {
                case "success":
                    iconRes = R.drawable.ic_check_circle;
                    break;
                case "warning":
                    iconRes = R.drawable.ic_warning;
                    break;
                case "error":
                    iconRes = R.drawable.ic_error;
                    break;
                case "promotion":
                    iconRes = R.drawable.ic_local_offer;
                    break;
                default:
                    iconRes = R.drawable.ic_info;
                    break;
            }
            ivIcon.setImageResource(iconRes);
        }
        
        private String getCategoryDisplayName(String category) {
            if (category == null) return "Sistema";
            
            switch (category) {
                case "request":
                    return "Solicitud";
                case "payment":
                    return "Pago";
                case "rating":
                    return "Calificación";
                case "promotion":
                    return "Promoción";
                case "system":
                    return "Sistema";
                default:
                    return category;
            }
        }
        
        private String formatTime(String dateTime) {
            if (dateTime == null || dateTime.isEmpty()) {
                return "Ahora";
            }
            
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(dateTime);
                
                if (date == null) return "Ahora";
                
                long diff = System.currentTimeMillis() - date.getTime();
                long minutes = diff / (60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                long days = diff / (24 * 60 * 60 * 1000);
                
                if (minutes < 1) {
                    return "Ahora";
                } else if (minutes < 60) {
                    return minutes + "m";
                } else if (hours < 24) {
                    return hours + "h";
                } else if (days < 7) {
                    return days + "d";
                } else {
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
                    return outputFormat.format(date);
                }
            } catch (ParseException e) {
                return "Ahora";
            }
        }
    }
}
