package com.example.loopv7.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loopv7.R;
import com.example.loopv7.models.Rating;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para mostrar calificaciones en un RecyclerView
 * 
 * @author LOOP Team
 * @version 1.0
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private List<Rating> ratings;
    private Context context;

    public RatingAdapter(List<Rating> ratings, Context context) {
        this.ratings = ratings;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.bind(rating);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvClientName, tvServiceName, tvReview, tvDate;
        private RatingBar ratingBarOverall, ratingBarQuality, ratingBarPunctuality, 
                        ratingBarCommunication, ratingBarCleanliness;
        private TextView tvQualityScore, tvPunctualityScore, tvCommunicationScore, tvCleanlinessScore;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvDate = itemView.findViewById(R.id.tvDate);
            
            ratingBarOverall = itemView.findViewById(R.id.ratingBarOverall);
            ratingBarQuality = itemView.findViewById(R.id.ratingBarQuality);
            ratingBarPunctuality = itemView.findViewById(R.id.ratingBarPunctuality);
            ratingBarCommunication = itemView.findViewById(R.id.ratingBarCommunication);
            ratingBarCleanliness = itemView.findViewById(R.id.ratingBarCleanliness);
            
            tvQualityScore = itemView.findViewById(R.id.tvQualityScore);
            tvPunctualityScore = itemView.findViewById(R.id.tvPunctualityScore);
            tvCommunicationScore = itemView.findViewById(R.id.tvCommunicationScore);
            tvCleanlinessScore = itemView.findViewById(R.id.tvCleanlinessScore);
        }

        public void bind(Rating rating) {
            // Configurar calificación general
            ratingBarOverall.setRating(rating.getOverallRating());
            
            // Configurar calificaciones específicas
            ratingBarQuality.setRating(rating.getQualityRating());
            ratingBarPunctuality.setRating(rating.getPunctualityRating());
            ratingBarCommunication.setRating(rating.getCommunicationRating());
            ratingBarCleanliness.setRating(rating.getCleanlinessRating());
            
            // Mostrar puntuaciones numéricas
            tvQualityScore.setText(String.valueOf(rating.getQualityRating()));
            tvPunctualityScore.setText(String.valueOf(rating.getPunctualityRating()));
            tvCommunicationScore.setText(String.valueOf(rating.getCommunicationRating()));
            tvCleanlinessScore.setText(String.valueOf(rating.getCleanlinessRating()));
            
            // Mostrar información del cliente
            if (rating.isAnonymous()) {
                tvClientName.setText("Cliente Anónimo");
            } else {
                tvClientName.setText("Cliente #" + rating.getRaterId());
            }
            
            // Mostrar nombre del servicio (se puede obtener de la base de datos si es necesario)
            tvServiceName.setText("Servicio #" + rating.getRequestId());
            
            // Mostrar reseña
            if (rating.getReview() != null && !rating.getReview().trim().isEmpty()) {
                tvReview.setText(rating.getReview());
                tvReview.setVisibility(View.VISIBLE);
            } else {
                tvReview.setVisibility(View.GONE);
            }
            
            // Mostrar fecha
            tvDate.setText(formatDate(rating.getCreatedAt()));
        }
        
        private String formatDate(String dateString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return outputFormat.format(date);
            } catch (Exception e) {
                return dateString;
            }
        }
    }
}
