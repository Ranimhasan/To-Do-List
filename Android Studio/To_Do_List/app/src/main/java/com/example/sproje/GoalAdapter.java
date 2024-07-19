package com.example.sproje;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class GoalAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> goalsList;
    private final ArrayList<Boolean> completionStatusList;

    public GoalAdapter(Context context, ArrayList<String> goalsList, ArrayList<Boolean> completionStatusList, ArrayList<String> list, ArrayList<Boolean> statusList) {
        super(context, R.layout.todolist, goalsList);
        this.context = context;
        this.goalsList = goalsList;
        this.completionStatusList = completionStatusList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.todolist, parent, false);

        CheckBox checkBox = rowView.findViewById(R.id.checkBox);
        TextView goalTextView = rowView.findViewById(R.id.goalTextView);
        TextView completionStatusTextView = rowView.findViewById(R.id.completionStatusTextView);

        goalTextView.setText(goalsList.get(position));

        boolean isCompleted = completionStatusList.get(position);
        String completionStatus = isCompleted ? "Tamamlandı" : "Tamamlanmadı";
        completionStatusTextView.setText(completionStatus);

        // Checkbox'un durumunu ayarlama
        checkBox.setChecked(isCompleted);
        //completionStatusTextView.setVisibility(View.GONE);
        // Checkbox'un durumu değiştiğinde Firestore'a güncelleme yapma
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Checkbox durumu değiştiğinde bu metot çağrılır
                updateCompletionStatusInFirestore(position, isChecked);
            }

            private void updateCompletionStatusInFirestore(int position, boolean isChecked) {
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    String goalName = goalsList.get(position);

                    // Hedefin adına göre sorgu yapma
                    db.collection("users").document(currentUser.getUid()).collection("hedefler")
                            .whereEqualTo("goalName", goalName)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Hedefin Firestore'daki belirli belge id'sini alma
                                            String documentId = document.getId();

                                            // Hedef belgesini güncelleme
                                            db.collection("users").document(currentUser.getUid()).collection("hedefler")
                                                    .document(documentId)
                                                    .update("completed", isChecked)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Başarıyla güncellendi
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Güncelleme başarısız oldu
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Firestore'dan hedefi bulmada bir hata oluştu
                                        Log.d("HedefBulma", "Hedef bulunamadı");
                                    }
                                }
                            });
                }
            }

        });

        return rowView;
    }

}

