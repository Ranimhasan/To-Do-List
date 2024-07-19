package com.example.sproje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sproje.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GunlkgrafActivity extends AppCompatActivity {

    private LineChart lineChart;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gunlkgraf);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        lineChart = findViewById(R.id.lineChart);

        // Grafiği oluşturma
        createLineChart();
    }

    private void createLineChart() {
        // Grafiği temizleme
        lineChart.clear();

        // Verileri alma
        getChartDataFromFirestore();
    }

    private void getChartDataFromFirestore() {
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = currentUser.getUid();

            db.collection("users").document(userId)
                    .collection("DailyProgress")
                    .document(getCurrentDate())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Belge bulundu, ilerleme kayıtlarını alma
                                    List<Entry> entries = new ArrayList<>();

                                    String adimSayisi = document.getString("adimSayisi");
                                    String egzersizSure = document.getString("egzersizSure");
                                    String kosuMesafesi = document.getString("kosuMesafesi");

                                    // Verileri grafiğe ekleme
                                    entries.add(new Entry(0, Float.parseFloat(adimSayisi)));
                                    entries.add(new Entry(1, Float.parseFloat(egzersizSure)));
                                    entries.add(new Entry(2, Float.parseFloat(kosuMesafesi)));

                                    // Verileri içeren bir veri seti oluşturma
                                    LineDataSet dataSet = new LineDataSet(entries, "Günlük İlerleme");
                                    dataSet.setColor(Color.BLUE);
                                    dataSet.setValueTextColor(Color.BLACK);

                                    // X eksenini konfigure etme
                                    XAxis xAxis = lineChart.getXAxis();
                                    xAxis.setValueFormatter(new DateAxisValueFormatter());
                                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                                    // Y eksenini konfigure etme
                                    YAxis yAxis = lineChart.getAxisLeft();
                                    yAxis.setAxisMinimum(0f); // Minimum değeri 0

                                    // Grafiği oluşturulan veri seti ile güncelleme
                                    LineData lineData = new LineData(dataSet);
                                    lineChart.setData(lineData);

                                    // Grafiği güncelleme
                                    lineChart.invalidate();
                                } else {
                                    Toast.makeText(GunlkgrafActivity.this, "Günlük ilerleme bulunamadı.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(GunlkgrafActivity.this, "Firestore verileri alınırken hata oluştu.", Toast.LENGTH_SHORT).show();
                            }
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
            intent = new Intent(GunlkgrafActivity.this, TodolistActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.hedef_detay_sayfasi) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, DetayliActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.motivasyon_bildirimi_sayfasi) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, BildirimActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunluk_ilerleme_sayfasi) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.grafiksel) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, StatikActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.sen) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, SensorActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.step) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, StepActivity.class);
            startActivity(intent);
            return true;
        }else if (itemId == R.id.gunlukilerleme) {
            Intent intent;
            intent = new Intent(GunlkgrafActivity.this, GunlukkaydiActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private class DateAxisValueFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // X eksenindeki değerleri tarih formatına çevirme
            return dateFormat.format(new Date((long) value));
        }
    }
}
