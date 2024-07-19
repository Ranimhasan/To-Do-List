package com.example.sproje;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StepCounterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                // Cihaz yeniden başlatıldığında adım sayacını başlatma
                startStepCounterService(context);
            }
        }
    }

    private void startStepCounterService(Context context) {
        Intent serviceIntent = new Intent(context, StepCounterService.class);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
