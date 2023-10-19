package com.example.duolingopezzotto.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duolingopezzotto.Adapters.CustomScegliCategorieAdapter;
import com.example.duolingopezzotto.Comparator.ParolaModelLevelComparator;
import com.example.duolingopezzotto.Models.ParolaModel;
import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

public class TraduciActivity extends AppCompatActivity {

    private int posizioneCorrente = 0;
    TextView parola_ita;
    EditText parola_sp, parola_eng;
    Button check;

    String flag;

    DatabaseHelper my_db;

    ArrayList<ParolaModel> lista_parole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_traduci);

        init();
    }

    public void init(){

        lista_parole = new ArrayList<>();

        parola_sp = findViewById(R.id.editTextTraduzioneSpagnolo);
        parola_eng = findViewById(R.id.editTextTraduzioneInglese);
        parola_ita = findViewById(R.id.textViewParolaCorrente);
        check = findViewById(R.id.buttonCheck);

        my_db = new DatabaseHelper(TraduciActivity.this);

        flag = getIntent().getStringExtra("mode");

        if(flag.equals("tutte")) storeAllParoleInArrayList();
        else if(flag.equals("parziale")) storeSelectedParoleInArrayList(CustomScegliCategorieAdapter.id_categorie);

        Collections.shuffle(lista_parole);
        Collections.sort(lista_parole, new ParolaModelLevelComparator());

        mostraParolaCorrente();
        stampaArrayListParole(lista_parole);

        check.setOnClickListener(view -> {
            prossimaParola();
        });

    }

    public void storeAllParoleInArrayList(){
        Cursor cursor = my_db.readAllCParole();
        if(cursor.getCount() == 0) Log.e("ERRORE: ", "NON CI SONO PAROLE ATM");
        else{
            while(cursor.moveToNext()) {
                ParolaModel parola = new ParolaModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                lista_parole.add(parola);
            }
        }
    }

    public void storeSelectedParoleInArrayList(ArrayList<Integer> categorie){
        Cursor cursor = my_db.getParoleFromArrayList(categorie);
        if(cursor.getCount() == 0) Log.e("ERRORE: ", "NON CI SONO PAROLE IN QUESTE CATEGORIE ATM");
        else{
            while(cursor.moveToNext()) {
                ParolaModel parola = new ParolaModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                lista_parole.add(parola);
            }
        }
    }

    private void mostraParolaCorrente() {
        if (posizioneCorrente < lista_parole.size()) {
            ParolaModel parolaCorrente = lista_parole.get(posizioneCorrente);
            parola_ita.setText(parolaCorrente.getItaliano());
            parola_ita.setTextColor(Color.BLACK);
            parola_sp.setText("");
            parola_eng.setText("");

        } else {
            Toast.makeText(this, "Parole Finite ATM", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private boolean verificaTraduzioniCorrette() {
        if(lista_parole.get(posizioneCorrente).getInglese().equals(parola_eng.getText().toString().trim()) && lista_parole.get(posizioneCorrente).getSpagnolo().equals(parola_sp.getText().toString().trim())) return true;
        else return false;
    }

    private void mostraDialogTraduzioniCorrette() {
        // Aggiungi qui il codice per mostrare un dialog con le traduzioni corrette
        // Puoi utilizzare AlertDialog o qualsiasi altro dialog personalizzato
        // Esempio:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Traduzioni corrette");
        builder.setMessage("Italiano: " + lista_parole.get(posizioneCorrente).getItaliano() + "\nSpagnolo: " + lista_parole.get(posizioneCorrente).getSpagnolo() + " \nInglese: " + lista_parole.get(posizioneCorrente).getInglese());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Avanti alla prossima parola dopo aver premuto OK nel dialog
                posizioneCorrente++;
                mostraParolaCorrente();
            }
        });
        builder.show();
    }

    public static void stampaArrayListParole(ArrayList<ParolaModel> parole) {
        for (ParolaModel parola : parole) {
            System.out.println("ID: " + parola.getId());
            System.out.println("Italiano: " + parola.getItaliano());
            System.out.println("Inglese: " + parola.getInglese());
            System.out.println("Spagnolo: " + parola.getSpagnolo());
            System.out.println("Level: " + parola.getLevel());
            System.out.println("Categoria ID: " + parola.getCategoriaId());
            System.out.println("--------------------");
        }
    }

    private void prossimaParola() {
        Handler handler = new Handler();
        long ritardoInMillisecondi = 600;
        // Controlla se ci sono traduzioni corrette
        boolean traduzioniCorrette = verificaTraduzioniCorrette();

        // Se le traduzioni sono corrette, colora il testo in verde e passa alla prossima parola
        if (traduzioniCorrette) {
            parola_ita.setTextColor(Color.GREEN);
            if(lista_parole.get(posizioneCorrente).getLevel() != 10) my_db.updateLevel(lista_parole.get(posizioneCorrente).getId(), lista_parole.get(posizioneCorrente).getLevel() + 1);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    posizioneCorrente++;
                    mostraParolaCorrente();
                }
            };
            handler.postDelayed(runnable, ritardoInMillisecondi);
        } else {
            // Le traduzioni non sono corrette, mostra un dialog con le traduzioni corrette
            if(lista_parole.get(posizioneCorrente).getLevel() != -10) my_db.updateLevel(lista_parole.get(posizioneCorrente).getId(), lista_parole.get(posizioneCorrente).getLevel() - 1);
            mostraDialogTraduzioniCorrette();
        }
    }

}