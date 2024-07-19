package com.example.sproje;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepCounterService extends Service implements SensorEventListener {
    private void startStepCounterService(Context context) {
        Intent serviceIntent = new Intent(context, StepCounterService.class);
        context.startService(serviceIntent);
    }


    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;

    private float[] gravityValues;
    private float[] magneticValues;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // İvmeölçer ve manyetometre sensörlerini alma
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometerSensor == null || magnetometerSensor == null) {
            // Cihazın ivmeölçer veya manyetometre sensörü desteklemediğinde hata mesajı gösterme
            Toast.makeText(this, "Cihazınız ivmeölçer veya manyetometre sensörünü desteklemiyor.", Toast.LENGTH_SHORT).show();
            stopSelf(); // Servisi sonlandır
        }

        // Listener'ları kaydetme
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        gravityValues = new float[3];
        magneticValues = new float[3];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Listener'ları kaldırma
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravityValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values;
        }

        float[] rotationMatrix = new float[9];
        boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravityValues, magneticValues);

        if (success) {
            float[] orientationValues = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            float azimuth = orientationValues[0];
            float pitch = orientationValues[1];
            float roll = orientationValues[2];

            Log.d("StepCounterService", "Azimuth: " + azimuth + ", Pitch: " + pitch + ", Roll: " + roll);
        } else {
            // Döndürme matrisi başarısız olduğunda hata mesajı gösterme
            Log.e("StepCounterService", "Döndürme matrisi oluşturulamadı.");
            Toast.makeText(this, "Döndürme matrisi oluşturulamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Hassasiyet değişikliklerini takip etmek için kullanılır
        Log.d("StepCounterService", "Sensor accuracy changed: " + accuracy);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Servis bağlantısı gerekli değil, null döndürüyoruz
        return null;
    }
}
