package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GunlukkaydiActivity extends AppCompatActivity {

    private EditText editTextKosuMesafesi, editTextEgzersizSure, editTextAdimSayisi;
    private Button buttonKaydet;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gunlukkaydi);

        editTextKosuMesafesi = findViewById(R.id.editTextKosuMesafesi);
        editTextEgzersizSure = findViewById(R.id.editTextEgzersizSure);
        editTextAdimSayisi = findViewById(R.id.editTextAdimSayisi);
        buttonKaydet = findViewById(R.id.buttonKaydet);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kaydet();
            }
        });
    }

    private void kaydet() {
        String kosuMesafesi = editTextKosuMesafesi.getText().toString().trim();
        String egzersizSure = editTextEgzersizSure.getText().toString().trim();
        String adimSayisi = editTextAdimSayisi.getText().toString().trim();

        if (!kosuMesafesi.isEmpty() && !egzersizSure.isEmpty() && !adimSayisi.isEmpty()) {
            String userId = currentUser.getUid();
            String currentDate = getCurrentDate();

            DocumentReference aktivitelerRef = db.collection("users")
                    .document(userId)
                    .collection("DailyProgress")
                    .document(currentDate);

            Map<String, Object> aktivitelerData = new HashMap<>();
            aktivitelerData.put("kosuMesafesi", kosuMesafesi);
            aktivitelerData.put("egzersizSure", egzersizSure);
            aktivitelerData.put("adimSayisi", adimSayisi);

            aktivitelerRef.set(aktivitelerData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GunlukkaydiActivity.this, "Aktiviteler başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                                // Gerekirse input alanlarını temizle
                                editTextKosuMesafesi.setText("");
                                editTextEgzersizSure.setText("");
                                editTextAdimSayisi.setText("");
                            } else {
                                Toast.makeText(GunlukkaydiActivity.this, "Aktiviteler kaydedilirken bir hata oluştu. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
        }
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
            intent = new Intent(GunlukkaydiActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(GunlukkaydiActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}

