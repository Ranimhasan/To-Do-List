package com.example.sproje;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StepActivity extends AppCompatActivity {

    private Button btnIncreaseStepCount;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private int stepCount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step);

        btnIncreaseStepCount = findViewById(R.id.btnIncreaseStepCount);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnIncreaseStepCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Butona tıklama ile adım sayısını artırma
                increaseStepCount();

                // Firestore'a veri ekleme
                addStepCountToFirestore();
            }
        });
    }

    private void increaseStepCount() {

        stepCount++;
    }

    private void addStepCountToFirestore() {
        // Kullanıcının UID'sini alma
        String uid = auth.getCurrentUser().getUid();

        // Kullanıcının UID'si ile Firestore'da bir belge (document) oluşturma
        DocumentReference userDocRef = firestore.collection("Users").document(uid);

        // Belgeye adım sayısını ekleme
        Map<String, Object> exerciseData = new HashMap<>();
        exerciseData.put("stepCount", stepCount);

        // Belgeyi Firestore'a ekleme
        userDocRef.set(exerciseData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(StepActivity.this, "Veri Firestore'a eklendi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StepActivity.this, "Veri eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
