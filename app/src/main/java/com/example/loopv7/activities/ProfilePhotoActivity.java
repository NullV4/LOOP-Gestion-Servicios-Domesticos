package com.example.loopv7.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.loopv7.R;
import com.example.loopv7.database.DatabaseHelper;
import com.example.loopv7.models.User;
import com.example.loopv7.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfilePhotoActivity extends AppCompatActivity {

    private static final String TAG = "ProfilePhotoActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    private ImageView ivProfilePhoto;
    private Button btnTakePhoto, btnSelectFromGallery, btnSavePhoto, btnResetFilters;
    private SeekBar sbBrightness, sbContrast, sbSaturation;
    private TextView tvBrightness, tvContrast, tvSaturation;
    
    private Bitmap originalBitmap;
    private Bitmap currentBitmap;
    private Uri imageUri;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);
        
        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(this);
        
        initViews();
        setupListeners();
        setupActivityResultLaunchers();
        loadCurrentProfilePhoto();
    }
    
    private void initViews() {
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery);
        btnSavePhoto = findViewById(R.id.btnSavePhoto);
        btnResetFilters = findViewById(R.id.btnResetFilters);
        
        sbBrightness = findViewById(R.id.sbBrightness);
        sbContrast = findViewById(R.id.sbContrast);
        sbSaturation = findViewById(R.id.sbSaturation);
        
        tvBrightness = findViewById(R.id.tvBrightness);
        tvContrast = findViewById(R.id.tvContrast);
        tvSaturation = findViewById(R.id.tvSaturation);
        
        // Configurar SeekBars
        sbBrightness.setMax(200);
        sbBrightness.setProgress(100);
        sbContrast.setMax(200);
        sbContrast.setProgress(100);
        sbSaturation.setMax(200);
        sbSaturation.setProgress(100);
        
        updateFilterLabels();
    }
    
    private void setupListeners() {
        btnTakePhoto.setOnClickListener(v -> takePhoto());
        btnSelectFromGallery.setOnClickListener(v -> selectFromGallery());
        btnSavePhoto.setOnClickListener(v -> savePhoto());
        btnResetFilters.setOnClickListener(v -> resetFilters());
        
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && currentBitmap != null) {
                    applyFilters();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && currentBitmap != null) {
                    applyFilters();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        sbSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && currentBitmap != null) {
                    applyFilters();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    
    private void setupActivityResultLaunchers() {
        // Launcher para cámara
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (imageUri != null) {
                        loadImageFromUri(imageUri);
                    }
                }
            }
        );
        
        // Launcher para galería
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (selectedImage != null) {
                        loadImageFromUri(selectedImage);
                    }
                }
            }
        );
        
        // Launcher para permisos
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Permiso concedido, continuar con la acción
                } else {
                    Toast.makeText(this, "Permiso necesario para acceder a la cámara", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageUri = createImageUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(intent);
        }
    }
    
    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
    
    private Uri createImageUri() {
        String fileName = "profile_photo_" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(getExternalFilesDir(null), fileName);
        return Uri.fromFile(photoFile);
    }
    
    private void loadImageFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            originalBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            
            // Redimensionar la imagen para optimizar memoria
            originalBitmap = resizeBitmap(originalBitmap, 800, 800);
            currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            
            ivProfilePhoto.setImageBitmap(currentBitmap);
            resetFilters();
            
            // Mostrar controles de filtros
            showFilterControls(true);
            
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        
        if (ratio < 1) {
            width = Math.round(width * ratio);
            height = Math.round(height * ratio);
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        
        return bitmap;
    }
    
    private void applyFilters() {
        if (originalBitmap == null) return;
        
        float brightness = (sbBrightness.getProgress() - 100) / 100f;
        float contrast = sbContrast.getProgress() / 100f;
        float saturation = sbSaturation.getProgress() / 100f;
        
        ColorMatrix colorMatrix = new ColorMatrix();
        
        // Aplicar brillo
        colorMatrix.set(new float[]{
            contrast, 0, 0, 0, brightness * 255,
            0, contrast, 0, 0, brightness * 255,
            0, 0, contrast, 0, brightness * 255,
            0, 0, 0, 1, 0
        });
        
        // Aplicar saturación
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);
        colorMatrix.postConcat(saturationMatrix);
        
        // Crear bitmap con filtros aplicados
        currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        android.graphics.Canvas canvas = new android.graphics.Canvas(currentBitmap);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(originalBitmap, 0, 0, paint);
        
        ivProfilePhoto.setImageBitmap(currentBitmap);
        updateFilterLabels();
    }
    
    private void resetFilters() {
        sbBrightness.setProgress(100);
        sbContrast.setProgress(100);
        sbSaturation.setProgress(100);
        
        if (originalBitmap != null) {
            currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            ivProfilePhoto.setImageBitmap(currentBitmap);
        }
        
        updateFilterLabels();
    }
    
    private void updateFilterLabels() {
        tvBrightness.setText("Brillo: " + (sbBrightness.getProgress() - 100));
        tvContrast.setText("Contraste: " + sbContrast.getProgress() + "%");
        tvSaturation.setText("Saturación: " + sbSaturation.getProgress() + "%");
    }
    
    private void showFilterControls(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        sbBrightness.setVisibility(visibility);
        sbContrast.setVisibility(visibility);
        sbSaturation.setVisibility(visibility);
        tvBrightness.setVisibility(visibility);
        tvContrast.setVisibility(visibility);
        tvSaturation.setVisibility(visibility);
        btnResetFilters.setVisibility(visibility);
        btnSavePhoto.setVisibility(visibility);
    }
    
    private void savePhoto() {
        if (currentBitmap == null) {
            Toast.makeText(this, "No hay imagen para guardar", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Guardar imagen en almacenamiento interno
            String fileName = "profile_photo_" + sessionManager.getCurrentUserId() + ".jpg";
            File photoFile = new File(getFilesDir(), fileName);
            
            FileOutputStream fos = new FileOutputStream(photoFile);
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            
            // Actualizar en la base de datos
            User currentUser = sessionManager.getCurrentUser();
            if (currentUser != null) {
                currentUser.setProfileImage(fileName);
                databaseHelper.updateUser(currentUser);
                // Actualizar en SessionManager
                sessionManager.updateCurrentUser(currentUser);
            }
            
            Toast.makeText(this, "Foto de perfil guardada exitosamente", Toast.LENGTH_SHORT).show();
            
            // Devolver resultado a la actividad anterior
            Intent resultIntent = new Intent();
            resultIntent.putExtra("photo_saved", true);
            setResult(RESULT_OK, resultIntent);
            finish();
            
        } catch (IOException e) {
            Log.e(TAG, "Error saving photo: " + e.getMessage());
            Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void loadCurrentProfilePhoto() {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser != null && currentUser.getProfileImage() != null) {
            try {
                File photoFile = new File(getFilesDir(), currentUser.getProfileImage());
                if (photoFile.exists()) {
                    originalBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    ivProfilePhoto.setImageBitmap(currentBitmap);
                    showFilterControls(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading current profile photo: " + e.getMessage());
            }
        }
    }
}
