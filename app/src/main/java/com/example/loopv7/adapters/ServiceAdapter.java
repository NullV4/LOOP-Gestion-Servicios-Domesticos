package com.example.loopv7.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.models.Service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> services;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }

    public ServiceAdapter(List<Service> services, OnServiceClickListener listener) {
        this.services = services;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvPrice, tvDuration, tvCategory;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvPrice = itemView.findViewById(R.id.tvServicePrice);
            tvDuration = itemView.findViewById(R.id.tvServiceDuration);
            tvCategory = itemView.findViewById(R.id.tvServiceCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onServiceClick(services.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Service service) {
            tvName.setText(service.getName());
            tvDescription.setText(service.getDescription());
            tvPrice.setText(service.getFormattedPrice());
            tvDuration.setText(service.getFormattedDuration());
            tvCategory.setText(service.getCategory());
        }
    }
}
