package com.example.sproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onLoginButtonClick(View view) {
        // Giriş ekranına yönlendiren Intent
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);


        startActivity(intent);

    }

}