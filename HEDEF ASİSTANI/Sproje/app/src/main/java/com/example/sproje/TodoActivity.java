package com.example.sproje;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TodoActivity extends AppCompatActivity {

    private EditText editTextGoalName, editTextGoalDescription, editTextSubGoal;
    private Spinner spinnerCategory;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        mAuth = FirebaseAuth.getInstance();


        editTextGoalName = findViewById(R.id.editTextGoalName);
        editTextGoalDescription = findViewById(R.id.editTextGoalDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextSubGoal = findViewById(R.id.editTextSubGoal);

        Button buttonAddGoal = findViewById(R.id.buttonAddGoal);
        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGoalToFirestore();
            }
        });

        Button buttonAddSubGoal = findViewById(R.id.buttonAddSubGoal);
        buttonAddSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubGoalToFirestore();
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
            intent = new Intent(TodoActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(TodoActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(TodoActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(TodoActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(TodoActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(TodoActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(TodoActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(TodoActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addGoalToFirestore() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            String goalName = editTextGoalName.getText().toString();
            String goalDescription = editTextGoalDescription.getText().toString();
            String selectedCategory = spinnerCategory.getSelectedItem().toString();


            if (TextUtils.isEmpty(goalName) || TextUtils.isEmpty(goalDescription)) {
                Toast.makeText(TodoActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }


            Map<String, Object> goalData = new HashMap<>();
            goalData.put("goalName", goalName);
            goalData.put("goalDescription", goalDescription);
            goalData.put("category", selectedCategory);
            goalData.put("baslangicTarihi", FieldValue.serverTimestamp());


            goalData.put("subGoals", new HashMap<String, Object>());


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                    .add(goalData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(TodoActivity.this, "Goal added to Firestore", Toast.LENGTH_SHORT).show();

                            editTextGoalName.setText("");
                            editTextGoalDescription.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TodoActivity.this, "Error adding goal to Firestore", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addSubGoalToFirestore() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            String subGoalName = editTextSubGoal.getText().toString();


            if (TextUtils.isEmpty(subGoalName)) {
                Toast.makeText(TodoActivity.this, "Please fill in the sub-goal field", Toast.LENGTH_SHORT).show();
                return;
            }


            Map<String, Object> subGoalData = new HashMap<>();
            subGoalData.put("subGoalName", subGoalName);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                    .limit(1) //1 belge
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Belge bulunduğunda, alt hedefi ekleme
                            DocumentReference goalDocumentReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
                            goalDocumentReference.update("subGoals." + subGoalName, subGoalData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(TodoActivity.this, "Sub-goal added to Firestore", Toast.LENGTH_SHORT).show();

                                            editTextSubGoal.setText("");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TodoActivity.this, "Error adding sub-goal to Firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(TodoActivity.this, "No goal found in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TodoActivity.this, "Error getting goal from Firestore", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}

