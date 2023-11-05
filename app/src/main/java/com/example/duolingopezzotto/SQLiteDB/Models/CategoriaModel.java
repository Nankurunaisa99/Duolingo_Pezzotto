package com.example.duolingopezzotto.SQLiteDB.Models;

public class CategoriaModel {

    private int id;
    private String nome;
    private String nota;

    public CategoriaModel(int id, String nome, String nota) {
        this.id = id;
        this.nome = nome;
        this.nota = nota;
    }

    public CategoriaModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nome) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "CategoriaModel{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nota='" + nota + '\'' +
                '}';
    }
}
