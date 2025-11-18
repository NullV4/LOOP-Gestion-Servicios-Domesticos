package com.example.loopv7.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loopv7.R;

public class OnboardingFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_RES = "image_res";
    private static final String ARG_COLOR = "color";

    private String title;
    private String description;
    private int imageRes;
    private String color;

    public static OnboardingFragment newInstance(String title, String description, int imageRes, String color) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_RES, imageRes);
        args.putString(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            description = getArguments().getString(ARG_DESCRIPTION);
            imageRes = getArguments().getInt(ARG_IMAGE_RES);
            color = getArguments().getString(ARG_COLOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        ImageView ivImage = view.findViewById(R.id.ivImage);
        
        tvTitle.setText(title);
        tvDescription.setText(description);
        
        // Usar emojis como imágenes por simplicidad
        if (imageRes == R.drawable.ic_welcome) {
            tvTitle.setText("👋 " + title);
        } else if (imageRes == R.drawable.ic_services) {
            tvTitle.setText("🏠 " + title);
        } else if (imageRes == R.drawable.ic_verified) {
            tvTitle.setText("✅ " + title);
        } else if (imageRes == R.drawable.ic_start) {
            tvTitle.setText("🚀 " + title);
        }
        
        // Configurar color de fondo
        try {
            int backgroundColor = Color.parseColor(color);
            view.setBackgroundColor(backgroundColor);
        } catch (Exception e) {
            view.setBackgroundColor(Color.parseColor("#2E7D32"));
        }
        
        return view;
    }
}
