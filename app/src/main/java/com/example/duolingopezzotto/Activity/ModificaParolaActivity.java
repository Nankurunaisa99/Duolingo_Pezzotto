package com.example.duolingopezzotto.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;

public class ModificaParolaActivity extends AppCompatActivity {

    EditText ita, sp, eng;
    Button modificaBtn, eliminaBtn;
    int parola_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_modifica_parola);

        init();
    }

    public void init(){

        ita = findViewById(R.id.editTextItaliano2);
        sp = findViewById(R.id.editTextSpagnolo2);
        eng = findViewById(R.id.editTextInglese2);

        modificaBtn = findViewById(R.id.buttonModificaParola);
        eliminaBtn = findViewById(R.id.buttonEliminaParola);

        ita.setText(getIntent().getStringExtra("italiano"));
        sp.setText(getIntent().getStringExtra("spagnolo"));
        eng.setText(getIntent().getStringExtra("inglese"));
        parola_id = getIntent().getIntExtra("id", -1);

        modificaBtn.setOnClickListener(view -> {
            DatabaseHelper my_db = new DatabaseHelper(ModificaParolaActivity.this);
            String italiano = ita.getText().toString().trim();
            String spagnolo = sp.getText().toString().trim();
            String inglese = eng.getText().toString().trim();

            if(italiano.isEmpty() || spagnolo.isEmpty() || inglese.isEmpty())
                Toast.makeText(this, "I campi non possono essere nulli", Toast.LENGTH_SHORT).show();
            else {
                if(parola_id == -1){
                    Toast.makeText(this, "C'è stato un errore durante la modifica della parola", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    my_db.modificaParola(parola_id, italiano, spagnolo, inglese);
                    Toast.makeText(this, "Parola modificata con successo!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        eliminaBtn.setOnClickListener(view -> {
            DatabaseHelper my_db = new DatabaseHelper(ModificaParolaActivity.this);
            my_db.eliminaParola(parola_id);
            finish();
        });

    }
}