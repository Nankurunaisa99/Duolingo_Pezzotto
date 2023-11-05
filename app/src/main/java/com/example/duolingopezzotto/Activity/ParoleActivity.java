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

import com.example.duolingopezzotto.Adapters.CustomParoleAdapter;
import com.example.duolingopezzotto.Comparator.ParolaModelStringComparator;
import com.example.duolingopezzotto.SQLiteDB.Models.ParolaModel;
import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class ParoleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addParolaFloatingActionButton;
    DatabaseHelper my_db;
    ArrayList<ParolaModel> parole;
    CustomParoleAdapter customParoleAdapter;
    long categoriaID;
    String categoria_della_parola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_parole);

        init();
    }

    public void init(){

        if(getIntent().hasExtra("categoria")) categoria_della_parola = getIntent().getStringExtra("categoria");
        else categoria_della_parola = "NONE";

        recyclerView = findViewById(R.id.paroleRecyclerView);
        addParolaFloatingActionButton = findViewById(R.id.addParolaBtn);

        my_db = new DatabaseHelper(ParoleActivity.this);
        parole = new ArrayList<>();

        categoriaID = my_db.getIDFromCategoria(categoria_della_parola);

        addParolaFloatingActionButton.setOnClickListener(view -> {
            Intent gotoAggiungiParola = new Intent(ParoleActivity.this, AggiungiParolaActivity.class);
            gotoAggiungiParola.putExtra("categoriaID", categoriaID);
            startActivity(gotoAggiungiParola);
        });


        storeParoleInArrayList(categoria_della_parola);
        Collections.sort(parole, new ParolaModelStringComparator());

        customParoleAdapter = new CustomParoleAdapter(ParoleActivity.this, parole);
        recyclerView.setAdapter(customParoleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ParoleActivity.this));

    }

    public void storeParoleInArrayList(String categoria_della_parola){
        Cursor cursor = my_db.getParoleFromCategoria(categoria_della_parola);
        if(cursor.getCount() == 0) Log.e("ERRORE: ", "NON CI SONO ANCORA CATEGORIE ATM");
        else{
            while(cursor.moveToNext()) {
                ParolaModel parola = new ParolaModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                parole.add(parola);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        parole.clear(); // Pulisci l'ArrayList delle parole per evitare duplicati
        storeParoleInArrayList(categoria_della_parola);
        Collections.sort(parole, new ParolaModelStringComparator());
        customParoleAdapter.notifyDataSetChanged(); // Notifica all'adapter che i dati sono cambiati
    }

}