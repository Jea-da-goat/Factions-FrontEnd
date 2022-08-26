package com.itndev.factions.Utils.FactionList;

import java.util.HashMap;

public class SortedList {

    private HashMap<Integer, String> map;
    private int length;

    public SortedList(HashMap<Integer, String> map, int length) {
        this.length = length;
        this.map = map;
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    public int getLength() {
        return length;
    }
}
