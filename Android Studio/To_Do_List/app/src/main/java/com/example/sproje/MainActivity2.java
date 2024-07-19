package com.example.sproje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MainActivity2 extends AppCompatActivity {

    private ListView goalsListView;
    private ArrayAdapter<String> arrayAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        goalsListView = findViewById(R.id.listTodo);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Hedefleri çek ve listele
        getGoalsFromDatabase();
    }
    public void addTask(View view) {


        Intent intent = new Intent(MainActivity2.this, TodoActivity.class);
        startActivity(intent);
    }

    private void getGoalsFromDatabase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> goalsList = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String goalName = document.getString("goalName");
                                    goalsList.add(goalName);
                                }

                                arrayAdapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, goalsList);
                                goalsListView.setAdapter(arrayAdapter);
                            } else {
                                Toast.makeText(MainActivity2.this, "Hedefleri çekerken bir hata oluştu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

