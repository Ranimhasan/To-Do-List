package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TodolistActivity extends AppCompatActivity {

    private ListView goalsListView;
    private GoalAdapter arrayAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ArrayList<String> goalsList;
    private Spinner spinnerCategory;
    private String selectedCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listto);

        goalsListView = findViewById(R.id.listTodo);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        setupCategorySpinner();

        // Hedefleri çek ve listele
        getGoalsFromDatabase();
    }

    private void setupCategorySpinner() {
        String[] categories = getResources().getStringArray(R.array.category_array);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = (String) parentView.getSelectedItem();
                filterGoalsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    public void onHedefEkleClick(View view) {

        Intent intent = new Intent(this, TodoActivity.class);
        startActivity(intent);
    }

    public void onHedefGuncelleClick(View view) {

        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }


    private void filterGoalsByCategory(String selectedCategory) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                    .whereEqualTo("category", selectedCategory)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                goalsList.clear(); // Clear the existing goals
                                ArrayList<Boolean> completionStatusList = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String goalName = document.getString("goalName");
                                    Boolean isCompleted = document.getBoolean("completed");

                                    goalsList.add(goalName);

                                    if (isCompleted != null) {
                                        completionStatusList.add(isCompleted);
                                    } else {
                                        completionStatusList.add(false);
                                    }
                                }

                                arrayAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("TodolistActivity", "filterGoalsByCategory: Hata", task.getException());
                                Toast.makeText(TodolistActivity.this, "Hedefleri filtrelerken bir hata oluştu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void getGoalsFromDatabase() {
        Log.d("TodolistActivity", "getGoalsFromDatabase: Başlatılıyor");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("TodolistActivity", "getGoalsFromDatabase: Başarılı");

                                goalsList = new ArrayList<>(); // goalsList sıfırlama
                                ArrayList<Boolean> completionStatusList = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String goalName = document.getString("goalName");
                                    Boolean isCompleted = document.getBoolean("completed");

                                    goalsList.add(goalName);

                                    // Null kontrolü ekleyerek hata önleme
                                    if (isCompleted != null) {
                                        completionStatusList.add(isCompleted.booleanValue());
                                    } else {
                                        completionStatusList.add(false);
                                    }
                                }

                                // Goal adapter kullanarak hedef adı ve tamamlanma durumu gösterme
                                arrayAdapter = new GoalAdapter(TodolistActivity.this, goalsList, completionStatusList, goalsList, completionStatusList);
                                goalsListView.setAdapter(arrayAdapter);
                            } else {
                                Log.d("TodolistActivity", "getGoalsFromDatabase: Başlatılıyor");
                                Log.d("TodolistActivity", "updateCompletionStatusInFirestore: Güncelleme başarısız oldu");

                                Log.e("TodolistActivity", "getGoalsFromDatabase: Hata", task.getException());
                                Toast.makeText(TodolistActivity.this, "Hedefleri çekerken bir hata oluştu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.d("TodolistActivity", "getGoalsFromDatabase: Kullanıcı bulunamadı");
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
            intent = new Intent(TodolistActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(TodolistActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
