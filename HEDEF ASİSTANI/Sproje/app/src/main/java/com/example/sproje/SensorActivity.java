package com.example.sproje;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private ListView sensorListView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        sensorListView = findViewById(R.id.sensorListView);

        // Sensör yöneticisi alınır
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            // Sensör listesi alınır
            List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

            // Sensör adlarını ve tiplerini içeren bir dizi oluşturulur
            String[] sensorInfoArray = new String[sensorList.size()];
            for (int i = 0; i < sensorList.size(); i++) {
                Sensor sensor = sensorList.get(i);
                sensorInfoArray[i] = "Name: " + sensor.getName() + ", Type: " + sensor.getType();
            }

            // ArrayAdapter kullanarak ListView'e sensör bilgilerini ekler
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    sensorInfoArray
            );

            sensorListView.setAdapter(arrayAdapter);
        }
    }
}

