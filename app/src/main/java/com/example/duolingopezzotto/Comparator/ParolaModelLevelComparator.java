package com.example.duolingopezzotto.Comparator;

import com.example.duolingopezzotto.SQLiteDB.Models.ParolaModel;

import java.util.Comparator;

public class ParolaModelLevelComparator implements Comparator<ParolaModel> {
    @Override
    public int compare(ParolaModel p1, ParolaModel p2) {
        return Integer.compare(p1.getLevel(), p2.getLevel());
    }
}
