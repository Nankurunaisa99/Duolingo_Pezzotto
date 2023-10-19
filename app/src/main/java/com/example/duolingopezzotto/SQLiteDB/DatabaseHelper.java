package com.example.duolingopezzotto.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DuolingoPezzotto.db";

    public static final String PAROLA_TABLE = "PAROLA_TABLE";
    public static final String COLUMN_PAROLA_ID = "ID";
    public static final String COLUMN_PAROLA_ITALIANO = "PAROLA_ITALIANO";
    public static final String COLUMN_PAROLA_SPAGNOLO = "PAROLA_SPAGNOLO";
    public static final String COLUMN_PAROLA_INGLESE = "PAROLA_INGLESE";
    public static final String COLUMN_PAROLA_LIVELLO = "PAROLA_LIVELLO";
    public static final String COLUMN_PAROLA_CATEGORIA_ID = "CATEGORIA_ID";

    public static final String CATEGORIA_TABLE = "CATEGORIA_TABLE";
    public static final String COLUMN_CATEGORIA_ID = "ID";
    public static final String COLUMN_CATEGORIA_NOME = "CATEGORIA_NOME";
    public static final String COLUMN_CATEGORIA_NOTA = "CATEGORIA_NOTA";

    public DatabaseHelper(@Nullable Context context) {
        super(context, context.getFilesDir() + File.separator + "databases" + File.separator + DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createParolaTableStatement = "CREATE TABLE " + PAROLA_TABLE + " (" +
                COLUMN_PAROLA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PAROLA_ITALIANO + " TEXT, " +
                COLUMN_PAROLA_SPAGNOLO + " TEXT, " +
                COLUMN_PAROLA_INGLESE + " TEXT, " +
                COLUMN_PAROLA_LIVELLO + " INTEGER, " +
                COLUMN_PAROLA_CATEGORIA_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_PAROLA_CATEGORIA_ID + ") REFERENCES " + CATEGORIA_TABLE + "(" + COLUMN_CATEGORIA_ID + ")" +
                ")";

        // Creazione della tabella "Categoria"
        String createCategoriaTableStatement = "CREATE TABLE " + CATEGORIA_TABLE + " (" +
                COLUMN_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORIA_NOME + " TEXT, " +
                COLUMN_CATEGORIA_NOTA + " TEXT" +
                ")";

        db.execSQL(createCategoriaTableStatement);
        Log.e("MESSAGGIO", "\nPRIMA TABELLA CREATA\n");
        db.execSQL(createParolaTableStatement);
        Log.e("MESSAGGIO", "\nSECONDA TABELLA CREATA\n");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PAROLA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIA_TABLE);
        onCreate(db);
    }

    public void addCategoria(String nome, String nota){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CATEGORIA_NOME, nome);
        cv.put(COLUMN_CATEGORIA_NOTA, nota);

        long result = db.insert(CATEGORIA_TABLE, null, cv);
        if (result == -1) Log.e("ERRORE: ", "OPS, QUALCOSA E' ANDATO STORTO, ATM");
        else Log.e("SUCCESS", "TUTTO A POSTO, AAAAATM");
    }

    public void addParola(String ita, String sp, String eng, Long level, Long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PAROLA_ITALIANO, ita);
        cv.put(COLUMN_PAROLA_SPAGNOLO, sp);
        cv.put(COLUMN_PAROLA_INGLESE, eng);
        cv.put(COLUMN_PAROLA_LIVELLO, level);
        cv.put(COLUMN_PAROLA_CATEGORIA_ID, id);

        long result = db.insert(PAROLA_TABLE, null, cv);
        if (result == -1) Log.e("ERRORE: ", "OPS, QUALCOSA E' ANDATO STORTO, ATM");
        else Log.e("SUCCESS", "TUTTO A POSTO, AAAAATM");
    }

    public void deleteAll(){
        deleteAllCategorie();
        deleteAllParole();
    }

    public void deleteAllCategorie(){
        String query = "DELETE FROM " + CATEGORIA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public void deleteAllParole(){
        String query = "DELETE FROM " + PAROLA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
    }

    public Cursor readAllCategorie(){
        String query = "SELECT * FROM " + CATEGORIA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor readAllCParole(){
        String query = "SELECT * FROM " + PAROLA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) cursor = db.rawQuery(query, null);

        return cursor;
    }

    public long getIDFromCategoria(String categoria) {
        String query = "SELECT " + COLUMN_CATEGORIA_ID + " FROM " + CATEGORIA_TABLE +
                " WHERE " + COLUMN_CATEGORIA_NOME + " = '" + categoria + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        long categoryId = -1; // Valore di default in caso di errore o nessun risultato
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int categoryIdIndex = cursor.getColumnIndex(COLUMN_CATEGORIA_ID);
                categoryId = cursor.getLong(categoryIdIndex);
            }
            cursor.close();
        }

        return categoryId;
    }

    public Cursor getParoleFromCategoria(String categoria){
        long categoryID = getIDFromCategoria(categoria);
        String query = "SELECT * FROM " + PAROLA_TABLE +
                " WHERE " + COLUMN_PAROLA_CATEGORIA_ID + " = '" + categoryID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getParoleFromArrayList(ArrayList<Integer> categoryIds) {
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ");
        queryBuilder.append(PAROLA_TABLE);
        queryBuilder.append(" WHERE ");
        queryBuilder.append(COLUMN_PAROLA_CATEGORIA_ID);
        queryBuilder.append(" IN (");

        // Aggiungi gli ID delle categorie all'elenco IN nella query
        for (int i = 0; i < categoryIds.size(); i++) {
            int categoryId = categoryIds.get(i);
            queryBuilder.append(categoryId);
            if (i < categoryIds.size() - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(")");

        String query = queryBuilder.toString();
        return db.rawQuery(query, null);
    }

    public void updateLevel(long parolaID, long lev){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PAROLA_LIVELLO, lev);
        db.update(PAROLA_TABLE, cv, "ID=?", new String[]{String.valueOf(parolaID)});

    }

    public void modificaParola(long parolaID, String ita, String sp, String eng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PAROLA_ITALIANO, ita);
        cv.put(COLUMN_PAROLA_SPAGNOLO, sp);
        cv.put(COLUMN_PAROLA_INGLESE, eng);

        db.update(PAROLA_TABLE, cv, "ID=?", new String[]{String.valueOf(parolaID)});
    }

    public void eliminaParola(int parolaID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PAROLA_TABLE,"ID=?", new String[]{String.valueOf(parolaID)});
    }
}
