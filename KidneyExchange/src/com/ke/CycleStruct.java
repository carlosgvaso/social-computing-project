package com.ke;

import java.util.ArrayList;

public class CycleStruct
{
    public int weight;
    public ArrayList<ArrayList<Integer>> IncludedCycles;
    
    public CycleStruct () {
        weight = 0;
        IncludedCycles = new ArrayList<ArrayList<Integer>>();
    }
}