package com.GlitchyDev.A;

import com.GlitchyDev.IO.RegionIO;
import com.GlitchyDev.World.Utility.HuffmanTreeUtility;

import java.util.HashMap;

public class Main {


    public static void main(String[] args) {
        int[] arrangement = new int[]{0,15,25,35,45,55};
        String[] items = new String[]{"0","15","25","35","45","55"};

        HashMap<String,Object> values = HuffmanTreeUtility.getHuffmanValues(items,arrangement);
        for(String s: values.keySet()) {
            System.out.println(values.get(s) + ": " + s);
        }



    }
}
