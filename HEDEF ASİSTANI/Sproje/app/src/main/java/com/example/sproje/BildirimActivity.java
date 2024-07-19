package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BildirimActivity extends AppCompatActivity {

    private CheckBox checkBoxMotivationNotifications;
    private Button buttonSavePreferences;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bildirim);

        db = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {

            Toast.makeText(this, "Oturum açmış bir kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
            finish(); // Aktiviteyi kapat
            return;
        }

        checkBoxMotivationNotifications = findViewById(R.id.checkBoxMotivationNotifications);
        buttonSavePreferences = findViewById(R.id.buttonSavePreferences);

        buttonSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotificationPreferences();
            }
        });

        loadNotificationPreferences();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secenek, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.ana_activity) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
                Intent intent;
                intent = new Intent(BildirimActivity.this, BildirimActivity.class);
                startActivity(intent);
                return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(BildirimActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void saveNotificationPreferences() {
        boolean isMotivationNotificationsEnabled = checkBoxMotivationNotifications.isChecked();

        // Kullanıcının bildirim tercihlerini Firestore'a kaydetme
        Map<String, Object> preferencesData = new HashMap<>();
        preferencesData.put("motivationNotificationsEnabled", isMotivationNotificationsEnabled);

        db.collection("userPreferences")
                .document(userId)
                .set(preferencesData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BildirimActivity.this, "Tercihler kaydedildi.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BildirimActivity.this, "Tercihleri kaydetme başarısız oldu. Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadNotificationPreferences() {
        // Kullanıcının daha önce kaydedilen tercihlerini Firestore'dan alma
        db.collection("userPreferences")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            boolean isMotivationNotificationsEnabled = documentSnapshot.getBoolean("motivationNotificationsEnabled");
                            checkBoxMotivationNotifications.setChecked(isMotivationNotificationsEnabled);
                        } else {

                            Toast.makeText(BildirimActivity.this, "Kullanıcı tercihleri bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BildirimActivity.this, "Kullanıcı tercihleri alınamadı. Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
