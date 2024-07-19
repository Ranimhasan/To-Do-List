package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetayliActivity extends AppCompatActivity {

    private TextView textViewGoalName;
    private TextView textViewGoalDescription;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private TextView textViewSubGoalsTitle;
    private ListView listViewSubGoals;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String hedefId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detayli);


        textViewGoalName = findViewById(R.id.textViewGoalName);
        textViewGoalDescription = findViewById(R.id.textViewGoalDescription);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        textViewSubGoalsTitle = findViewById(R.id.textViewSubGoalsTitle);
        listViewSubGoals = findViewById(R.id.listViewSubGoals);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Intent'ten hedefId'yi al
        hedefId = getIntent().getStringExtra("hedefId");


        displayGoalDetails();
        displaySubGoals();
    }

    private void displayGoalDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Firestore'dan temel bilgileri çekme
            db.collection("users").document(userId).collection("hedefler").document(hedefId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                textViewGoalName.setText("Hedef Adı: " + document.getString("goalName"));
                                textViewGoalDescription.setText("Hedef Açıklaması: " + document.getString("goalDescription"));
                                textViewStartDate.setText("Başlangıç Tarihi: " + document.getString("baslangicTarihi"));
                                textViewEndDate.setText("Bitiş Tarihi: " + document.getString("bitisTarihi"));
                            } else {
                                textViewGoalName.setText("Belirtilen hedef bulunamadı.");
                            }
                        } else {
                            textViewGoalName.setText("Hedef bilgileri alınamadı.");
                        }
                    });
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
            intent = new Intent(DetayliActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(DetayliActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void displaySubGoals() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Firestore'dan alt hedefleri çekme
            db.collection("users").document(userId).collection("hedefler").document(hedefId).collection("subGoals")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<String> subGoalsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subGoalsList.add(document.getString("altHedefAdi"));
                            }

                            SubGoalsListAdapter adapter = new SubGoalsListAdapter(this, subGoalsList);
                            listViewSubGoals.setAdapter(adapter);
                        } else {
                            textViewSubGoalsTitle.setText("Alt hedefler alınamadı.");
                        }
                    });
        }
    }
}
