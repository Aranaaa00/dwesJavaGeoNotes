package com.example.geonotesteaching;

import java.util.Comparator;

public class ComparadorPorCreatedAt implements Comparator<Note>{

    @Override
    public int compare(Note o1, Note o2) {
        return o2.createdAt().compareTo(o1.createdAt());
    }
    
}
