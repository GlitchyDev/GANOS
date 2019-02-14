package com.GlitchyDev.Utility;

import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.World;
import org.joml.Vector2i;

import java.util.ArrayList;

public class BresenhamLineUtility {



    public static  ArrayList<Location> findLine(Location origin, Location target) {
        ArrayList<Location> line = new ArrayList<>();
        Location difference = origin.getLocationDifference(target);

        int incrementLength = Math.abs(Math.max(Math.max(difference.getX(),difference.getY()),difference.getZ()));

        boolean usesX = difference.getX() != 0;
        boolean usesY = difference.getY() != 0;
        boolean usesZ = difference.getZ() != 0;

        int dx = usesX ? incrementLength/difference.getX() : 0;
        int dy = usesY ? incrementLength/difference.getY() : 0;
        int dz = usesZ ? incrementLength/difference.getZ() : 0;

        for(int i = 0; i < incrementLength; i++) {
            int xCount = usesX ? i/dx : 0;
            int yCount = usesY ? i/dy : 0;
            int zCount = usesZ ? i/dz : 0;
            Location nextLocation = origin.getOffsetLocation(xCount,yCount,zCount);
            line.add(nextLocation);
        }


        return line;
    }

   public static void main(String[] args) {
       boolean[][] area = new boolean[20][20];
       Location origin = new Location(0,0,0,null);
       Location target = new Location(11,4,0,null);
       ArrayList<Location> line = findLine(origin, target);

       for(Location location: line) {
           area[location.getY()][location.getX()] = true;
       }


       for(boolean[] row: area) {
           for(boolean b: row) {
               if(b) {
                   System.out.print("1");
               } else {
                   System.out.print("0");
               }
           }
           System.out.println();
       }




   }
}
