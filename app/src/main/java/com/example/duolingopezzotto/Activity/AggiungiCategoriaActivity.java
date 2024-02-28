package com.example.duolingopezzotto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duolingopezzotto.R;
import com.example.duolingopezzotto.SQLiteDB.DatabaseHelper;

public class AggiungiCategoriaActivity extends AppCompatActivity {

    EditText nomeEditText, noteEditText;
    Button creaCategoriaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_categoria);

        init();
    }

    public void init(){

        nomeEditText = findViewById(R.id.nomeEditText);
        noteEditText = findViewById(R.id.noteEditText);
        creaCategoriaButton = findViewById(R.id.createCategoriaButton);

        creaCategoriaButton.setOnClickListener(view -> {
            DatabaseHelper my_db = new DatabaseHelper(AggiungiCategoriaActivity.this);

            String nome = nomeEditText.getText().toString().trim();
            String nota = noteEditText.getText().toString().trim();
            if(nome.isEmpty() || nota.isEmpty())
                Toast.makeText(this, "I campi non possono essere nulli", Toast.LENGTH_SHORT).show();
            else {
                my_db.addCategoria(nome, nota);
                Toast.makeText(this, "Categoria aggiunta con successo!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}