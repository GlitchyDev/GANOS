package com.GlitchyDev.Utility;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;

import java.util.*;

public class SortUtility {


    public static HashMap<BlockBase, Integer> sortByValue(HashMap<BlockBase, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<BlockBase, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        // put data from sorted list to hashmap
        HashMap<BlockBase, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<BlockBase, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
