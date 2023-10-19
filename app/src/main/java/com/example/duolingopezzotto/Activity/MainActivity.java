package com.example.duolingopezzotto.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    Button esercitatiButton, aggiungiButton;
    DatabaseHelper my_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        esercitatiButton = findViewById(R.id.esercitatiButton);
        aggiungiButton = findViewById(R.id.aggiungiButton);

        my_db = new DatabaseHelper(MainActivity.this);

        aggiungiButton.setOnClickListener(view -> {
            Intent gotoCategorie = new Intent(MainActivity.this, CategorieActivity.class);
            startActivity(gotoCategorie);
        });

        esercitatiButton.setOnClickListener(view -> {
            //my_db.deleteAll();
            Intent gotoScegliCategorie = new Intent(MainActivity.this, ScegliCategorieActivity.class);
            startActivity(gotoScegliCategorie);
        });
    }
}