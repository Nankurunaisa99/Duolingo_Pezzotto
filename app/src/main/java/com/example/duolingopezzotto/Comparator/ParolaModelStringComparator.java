package com.example.duolingopezzotto.Comparator;

import com.example.duolingopezzotto.Models.ParolaModel;

import java.util.Comparator;

public class ParolaModelStringComparator implements Comparator<ParolaModel> {
    @Override
    public int compare(ParolaModel o1, ParolaModel o2) {
        return o1.getItaliano().compareTo(o2.getItaliano());
    }
}
