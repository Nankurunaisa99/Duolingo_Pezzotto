package com.example.duolingopezzotto.SQLiteDB.Models;

public class ParolaModel {

    private int id;
    private String italiano, inglese, spagnolo;
    private int level;
    private int categoria_id;

    public ParolaModel(int id, String italiano, String spagnolo, String inglese, int level, int categoria_id) {
        this.id = id;
        this.italiano = italiano;
        this.inglese = inglese;
        this.spagnolo = spagnolo;
        this.level = level;
        this.categoria_id = categoria_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItaliano() {
        return italiano;
    }

    public String getInglese() {
        return inglese;
    }

    public String getSpagnolo() {
        return spagnolo;
    }

    public int getLevel() {
        return level;
    }

    public int getCategoriaId() {
        return categoria_id;
    }

    @Override
    public String toString() {
        return "ParolaModel{" +
                "id=" + id +
                ", italiano='" + italiano + '\'' +
                ", inglese='" + inglese + '\'' +
                ", spagnolo='" + spagnolo + '\'' +
                ", level=" + level +
                ", categoriaId=" + categoria_id +
                '}';
    }
}
