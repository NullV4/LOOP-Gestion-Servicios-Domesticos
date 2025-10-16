package com.example.loopv7.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.database.SimpleDatabaseHelper;
import com.example.loopv7.models.Request;
import com.example.loopv7.models.Service;
import com.example.loopv7.models.User;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requests;
    private OnRequestClickListener listener;

    public interface OnRequestClickListener {
        void onRequestClick(Request request);
    }

    public RequestAdapter(List<Request> requests, OnRequestClickListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRequestId, tvServiceId, tvDate, tvTime, tvAddress, tvStatus, tvPrice;
        private SimpleDatabaseHelper databaseHelper;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestId = itemView.findViewById(R.id.tvRequestId);
            tvServiceId = itemView.findViewById(R.id.tvServiceId);
            tvDate = itemView.findViewById(R.id.tvRequestDate);
            tvTime = itemView.findViewById(R.id.tvRequestTime);
            tvAddress = itemView.findViewById(R.id.tvRequestAddress);
            tvStatus = itemView.findViewById(R.id.tvRequestStatus);
            tvPrice = itemView.findViewById(R.id.tvRequestPrice);
            
            databaseHelper = new SimpleDatabaseHelper(itemView.getContext());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRequestClick(requests.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Request request) {
            try {
                tvRequestId.setText("Solicitud #" + request.getId());
                
                // Obtener información del servicio y cliente
                Service service = databaseHelper.getServiceById(request.getServiceId());
                User client = databaseHelper.getUserById(request.getClientId());
                
                if (service != null) {
                    tvServiceId.setText(service.getName());
                } else {
                    tvServiceId.setText("Servicio ID: " + request.getServiceId());
                }
                
                tvDate.setText(request.getScheduledDate());
                tvTime.setText(request.getScheduledTime());
                tvAddress.setText(request.getAddress());
                tvStatus.setText(getStatusText(request.getStatus()));
                tvPrice.setText(request.getFormattedPrice());
                
                // Cambiar color según el estado
                int statusColor = getStatusColor(request.getStatus());
                tvStatus.setTextColor(statusColor);
                
            } catch (Exception e) {
                // En caso de error, mostrar información básica
                tvRequestId.setText("Solicitud #" + request.getId());
                tvServiceId.setText("Servicio ID: " + request.getServiceId());
                tvDate.setText(request.getScheduledDate());
                tvTime.setText(request.getScheduledTime());
                tvAddress.setText(request.getAddress());
                tvStatus.setText(getStatusText(request.getStatus()));
                tvPrice.setText(request.getFormattedPrice());
                e.printStackTrace();
            }
        }

        private String getStatusText(String status) {
            switch (status) {
                case "pendiente": return "⏳ Pendiente";
                case "aceptada": return "✅ Aceptada";
                case "rechazada": return "❌ Rechazada";
                case "en_progreso": return "🔄 En Progreso";
                case "completada": return "🎉 Completada";
                case "cancelada": return "🚫 Cancelada";
                default: return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status) {
                case "pendiente": return itemView.getContext().getColor(R.color.warning);
                case "aceptada": return itemView.getContext().getColor(R.color.info);
                case "rechazada": return itemView.getContext().getColor(R.color.error);
                case "en_progreso": return itemView.getContext().getColor(R.color.warning);
                case "completada": return itemView.getContext().getColor(R.color.success);
                case "cancelada": return itemView.getContext().getColor(R.color.secondary);
                default: return itemView.getContext().getColor(R.color.text_primary);
            }
        }
    }
}
