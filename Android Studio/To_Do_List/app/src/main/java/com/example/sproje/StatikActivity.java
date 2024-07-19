package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StatikActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView textViewCompletedGoalsCount;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statik);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewCompletedGoalsCount = findViewById(R.id.textViewCompletedGoalsCount);
        barChart = findViewById(R.id.barChart);

        // Tamamlanan hedef istatistiklerini alma
        getCompletedGoalsStats();
    }

    private void getCompletedGoalsStats() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Tamamlanan hedefleri belirleme
            db.collection("users").document(userId).collection("hedefler")
                    .whereEqualTo("completed", true)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int completedGoalsCount = task.getResult().size();
                            displayStats(completedGoalsCount);
                        } else {
                            Toast.makeText(this, "Tamamlanan hedefler alınamadı.", Toast.LENGTH_SHORT).show();
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
            intent = new Intent(StatikActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(StatikActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(StatikActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(StatikActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(StatikActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(StatikActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(StatikActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(StatikActivity.this, GunlkgrafActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void displayStats(int completedGoalsCount) {
        // İstatistikleri kullanmak için burada toplam tamamlanan hedef sayısını kullandik
        textViewCompletedGoalsCount.setText("Toplam tamamlanan hedef sayısı: " + completedGoalsCount);

        // Hedeflere ulaşma grafiğini oluşturma
        createAchievementChart(completedGoalsCount);
    }

    private void createAchievementChart(int completedGoalsCount) {
        // Toplam hedef sayısını belirleme
        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("hedefler")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalGoalsCount = task.getResult().size();

                        // Hedeflere ulaşma grafiğini oluşturma
                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0, completedGoalsCount));
                        entries.add(new BarEntry(1, totalGoalsCount - completedGoalsCount));

                        BarDataSet dataSet = new BarDataSet(entries, "Hedeflere Ulaşma Durumu");
                        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
                        dataSet.setDrawValues(true);

                        BarData barData = new BarData(dataSet);
                        barChart.setData(barData);

                        // X eksenini konfigure etme
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new XAxisValueFormatter());
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Y eksenini konfigure etme
                        YAxis yAxis = barChart.getAxisLeft();
                        yAxis.setAxisMinimum(0f);

                        barChart.getDescription().setEnabled(false);
                        barChart.getLegend().setEnabled(false);

                        // Grafiği güncelleme
                        barChart.invalidate();
                    } else {
                        Toast.makeText(this, "Toplam hedef sayısı alınamadı.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class XAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // X eksenindeki değerleri formatlama
            return value == 0 ? "Tamamlanan Hedefler" : "Tamamlanmayan Hedefler";
        }
    }
}
