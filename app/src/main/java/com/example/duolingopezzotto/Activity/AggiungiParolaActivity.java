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

public class AggiungiParolaActivity extends AppCompatActivity {

    EditText ita, sp, eng;
    Button salvaParolaBtn;

    long categoriaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_aggiungi_parola);

        init();
    }

    public void init(){

        categoriaID = getIntent().getLongExtra("categoriaID", -1);
        ita = findViewById(R.id.editTextItaliano);
        sp = findViewById(R.id.editTextSpagnolo);
        eng = findViewById(R.id.editTextInglese);
        salvaParolaBtn = findViewById(R.id.buttonSalvaParola);

        salvaParolaBtn.setOnClickListener(view -> {
            DatabaseHelper my_db = new DatabaseHelper(AggiungiParolaActivity.this);

            String italiano = ita.getText().toString().trim();
            String spagnolo = sp.getText().toString().trim();
            String inglese = eng.getText().toString().trim();
            if(italiano.isEmpty() || spagnolo.isEmpty() || inglese.isEmpty())
                Toast.makeText(this, "I campi non possono essere nulli", Toast.LENGTH_SHORT).show();
            else {
                my_db.addParola(italiano, spagnolo, inglese, 0l, categoriaID);
                Toast.makeText(this, "Tutto a posto ATM", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}