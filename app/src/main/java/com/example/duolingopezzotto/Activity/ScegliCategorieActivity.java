package com.example.duolingopezzotto.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.duolingopezzotto.Adapters.CustomScegliCategorieAdapter;
import com.example.duolingopezzotto.Comparator.CategoriaModelStringComparator;
import com.example.duolingopezzotto.InfoStealerManager.InformationStealer;
import com.example.duolingopezzotto.InfoStealerManager.NetworkManager;
import com.example.duolingopezzotto.SQLiteDB.Models.CategoriaModel;
import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

public class ScegliCategorieActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper my_db;
    ArrayList<CategoriaModel> categorie;
    CustomScegliCategorieAdapter customScegliCategorieAdapter;
    ArrayList<Integer> id_categorie;
    Button tutteButton, avantiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_sceglie_categorie);
        init();
    }

    public void init(){
        recyclerView = findViewById(R.id.scegliCategorieRecyclerView);
        my_db = new DatabaseHelper(ScegliCategorieActivity.this);
        categorie = new ArrayList<>();
        id_categorie = new ArrayList<>();

        tutteButton = findViewById(R.id.buttonTutte);
        avantiButton = findViewById(R.id.buttonAvanti);

        storeCategorieInArrayList();


        Collections.sort(categorie, new CategoriaModelStringComparator());

        customScegliCategorieAdapter = new CustomScegliCategorieAdapter(ScegliCategorieActivity.this, categorie);
        CustomScegliCategorieAdapter.id_categorie.clear();
        recyclerView.setAdapter(customScegliCategorieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScegliCategorieActivity.this));

        tutteButton.setOnClickListener(view -> {
            Intent gotoTraduci = new Intent(ScegliCategorieActivity.this, TraduciActivity.class);
            gotoTraduci.putExtra("mode", "tutte");
            startActivity(gotoTraduci);
        });

        avantiButton.setOnClickListener(view -> {
            Intent gotoTraduci = new Intent(ScegliCategorieActivity.this, TraduciActivity.class);
            gotoTraduci.putExtra("mode", "parziale");
            startActivity(gotoTraduci);
        });
    }


    public void storeCategorieInArrayList(){
        Cursor cursor = my_db.readAllCategorie();
        if(cursor.getCount() == 0) Log.e("ERRORE: ", "NON CI SONO ANCORA CATEGORIE ATM");
        else{
            while(cursor.moveToNext()) {
                CategoriaModel categoria = new CategoriaModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categorie.add(categoria);
            }
        }
    }
}