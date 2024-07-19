package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sproje.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTextUpdateGoalName;
    private EditText editTextUpdateCategory;
    private EditText editTextUpdateGoalDescription;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String userId;
    private String goalDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        editTextUpdateGoalName = findViewById(R.id.editTextUpdateGoalName);
        editTextUpdateCategory = findViewById(R.id.editTextUpdateCategory);
        editTextUpdateGoalDescription = findViewById(R.id.editTextUpdateGoalDescription);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            goalDocumentId = extras.getString("GOAL_DOCUMENT_ID");
            userId = extras.getString("USER_ID");

            GoalDetailsRetriever goalDetailsRetriever = new GoalDetailsRetriever(userId, goalDocumentId);
            goalDetailsRetriever.getGoalDetails();
        } else {
            Toast.makeText(this, "Hedef detayları alınamadı. Uygulamada bir sorun olabilir.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateGoalClick(View view) {
        String updatedGoalName = editTextUpdateGoalName.getText().toString().trim();
        String updatedCategory = editTextUpdateCategory.getText().toString().trim();
        String updatedGoalDescription = editTextUpdateGoalDescription.getText().toString().trim();

        if (!updatedGoalName.isEmpty() && !updatedCategory.isEmpty() && !updatedGoalDescription.isEmpty()) {
            updateGoalInFirestore(updatedGoalName, updatedCategory, updatedGoalDescription);
        } else {
            Toast.makeText(this, "Lütfen güncellenecek hedef bilgilerini girin", Toast.LENGTH_SHORT).show();
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
            intent = new Intent(UpdateActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(UpdateActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateGoalInFirestore(String updatedGoalName, String updatedCategory, String updatedGoalDescription) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DocumentReference goalRef = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("hedefler")
                    .document(goalDocumentId);

            goalRef.update("goalName", updatedGoalName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                goalRef.update("category", updatedCategory)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    goalRef.update("goalDescription", updatedGoalDescription)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(UpdateActivity.this, "Hedef başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(UpdateActivity.this, "Hedef güncellenirken bir hata oluştu. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(UpdateActivity.this, "Hedef güncellenirken bir hata oluştu. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(UpdateActivity.this, "Hedef güncellenirken bir hata oluştu. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Kullanıcı bilgileri alınamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private class GoalDetailsRetriever {

        private String userId;
        private String goalDocumentId;

        public GoalDetailsRetriever(String userId, String goalDocumentId) {
            this.userId = userId;
            this.goalDocumentId = goalDocumentId;
        }

        public void getGoalDetails() {
            if (userId != null && goalDocumentId != null) {
                DocumentReference goalRef = db.collection("users")
                        .document(userId)
                        .collection("hedefler")
                        .document(goalDocumentId);

                goalRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String currentGoalName = document.getString("goalName");
                                String currentCategory = document.getString("category");
                                String currentGoalDescription = document.getString("goalDescription");

                                editTextUpdateGoalName.setText(currentGoalName);
                                editTextUpdateCategory.setText(currentCategory);
                                editTextUpdateGoalDescription.setText(currentGoalDescription);
                            } else {
                                Toast.makeText(UpdateActivity.this, "Belge bulunamadı. Hedef Document Id: " + goalDocumentId, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateActivity.this, "Hedef detayları alınamadı. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(UpdateActivity.this, "Kullanıcı ID veya Hedef Document ID null.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
