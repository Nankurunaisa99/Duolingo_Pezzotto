package com.example.duolingopezzotto.Comparator;

import com.example.duolingopezzotto.Models.CategoriaModel;

import java.util.Comparator;

public class CategoriaModelStringComparator implements Comparator<CategoriaModel> {
    @Override
    public int compare(CategoriaModel o1, CategoriaModel o2) {
        return o1.getNome().compareTo(o2.getNome());
    }
}
