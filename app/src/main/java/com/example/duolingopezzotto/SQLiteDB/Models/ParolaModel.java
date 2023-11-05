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

    public ParolaModel() {
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

    public void setItaliano(String italiano) {
        this.italiano = italiano;
    }

    public String getInglese() {
        return inglese;
    }

    public void setInglese(String inglese) {
        this.inglese = inglese;
    }

    public String getSpagnolo() {
        return spagnolo;
    }

    public void setSpagnolo(String spagnolo) {
        this.spagnolo = spagnolo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCategoriaId() {
        return categoria_id;
    }

    public void setCategoriaId(int categoria_id) {
        this.categoria_id = categoria_id;
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
