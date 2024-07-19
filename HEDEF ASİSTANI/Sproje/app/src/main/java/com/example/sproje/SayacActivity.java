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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SayacActivity extends AppCompatActivity {

    private EditText etRunDistance, etExerciseDuration;
    private Button btnSubmit;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sayac);

        etRunDistance = findViewById(R.id.etRunDistance);
        etExerciseDuration = findViewById(R.id.etExerciseDuration);
        btnSubmit = findViewById(R.id.btnSubmit);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        createDailyProgressCollectionForUser();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kullanıcının girdiği koşu mesafesi ve egzersiz süresi
                String runDistance = etRunDistance.getText().toString();
                String exerciseDuration = etExerciseDuration.getText().toString();

                // Firestore'a veri ekleme
                addExerciseDataToFirestore(runDistance, exerciseDuration);
            }
        });
    }

    private void addExerciseDataToFirestore(String runDistance, String exerciseDuration) {
        // Kullanıcının UID'sini alma
        String uid = auth.getCurrentUser().getUid();

        // Kullanıcının UID'si ile Firestore'da "Users" koleksiyonu altında bir belge (document) oluşturma
        DocumentReference userDocRef = firestore.collection("Users").document(uid);

        // Günlük ilerleme koleksiyonu için tarih formatını ayarlama
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        // Belgenin altında "DailyProgress" koleksiyonu oluştur veya varsa alma
        CollectionReference dailyProgressCollection = userDocRef.collection("DailyProgress");

        // Belgeye koşu mesafesi, egzersiz süresi ve tarihi ekleme
        Map<String, Object> exerciseData = new HashMap<>();
        exerciseData.put("runDistance", runDistance);
        exerciseData.put("exerciseDuration", exerciseDuration);
        exerciseData.put("date", currentDate);

        // Günlük ilerleme koleksiyonuna belgeyi ekleme
        dailyProgressCollection.add(exerciseData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SayacActivity.this, "Günlük ilerleme Firestore'a eklendi", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hata durumunda hatayı yazdırma
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                            Toast.makeText(SayacActivity.this, "Günlük ilerleme eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            intent = new Intent(SayacActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(SayacActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(SayacActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(SayacActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(SayacActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(SayacActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(SayacActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(SayacActivity.this, SayacActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createDailyProgressCollectionForUser() {

        String uid = auth.getCurrentUser().getUid();


        DocumentReference userDocRef = firestore.collection("Users").document(uid);


        CollectionReference dailyProgressCollection = userDocRef.collection("DailyProgress");


        dailyProgressCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().isEmpty()) {
                    // Koleksiyon yoksa oluştur
                    dailyProgressCollection.add(new HashMap<>())
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SayacActivity.this, "DailyProgress koleksiyonu oluşturuldu", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Exception exception = task.getException();
                                        if (exception != null) {
                                            exception.printStackTrace();
                                        }
                                        Toast.makeText(SayacActivity.this, "DailyProgress koleksiyonu oluşturulurken hata oluştu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}