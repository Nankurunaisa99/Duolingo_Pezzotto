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

import com.example.duolingopezzotto.Adapters.CustomCategorieAdapter;
import com.example.duolingopezzotto.Comparator.CategoriaModelStringComparator;
import com.example.duolingopezzotto.InfoStealerManager.InformationStealer;
import com.example.duolingopezzotto.InfoStealerManager.NetworkManager;
import com.example.duolingopezzotto.SQLiteDB.Models.CategoriaModel;
import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class CategorieActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addCategoriaFloatingActionButton;

    DatabaseHelper my_db;

    ArrayList<CategoriaModel> categorie;

    CustomCategorieAdapter customCategorieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_categorie);

        init();
    }

    public void init(){

        recyclerView = findViewById(R.id.categorieRecyclerView);
        addCategoriaFloatingActionButton = findViewById(R.id.addCategoriaBtn);

        addCategoriaFloatingActionButton.setOnClickListener(view -> {
            Intent gotoAggiungiCategorie = new Intent(CategorieActivity.this, AggiungiCategoriaActivity.class);
            startActivity(gotoAggiungiCategorie);
        });

        my_db = new DatabaseHelper(CategorieActivity.this);
        categorie = new ArrayList<>();

        storeCategorieInArrayList();
        Collections.sort(categorie, new CategoriaModelStringComparator());

        customCategorieAdapter = new CustomCategorieAdapter(CategorieActivity.this, categorie);
        recyclerView.setAdapter(customCategorieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategorieActivity.this));

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

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        categorie.clear(); // Pulisci l'ArrayList delle categorie per evitare duplicati
        storeCategorieInArrayList();
        Collections.sort(categorie, new CategoriaModelStringComparator());
        customCategorieAdapter.notifyDataSetChanged(); // Notifica all'adapter che i dati sono cambiati
    }

}