package com.GlitchyDev.Utility;

import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.World;
import org.joml.Vector2i;

import java.util.ArrayList;

public class BresenhamLineUtility {


    public static ArrayList<Location> findLineNew(Location origin, Location target) {
        ArrayList<Location> line = new ArrayList<>();
        Location difference = origin.getLocationDifference(target);

        double largest = Math.max(difference.getX(), Math.max(difference.getY(), difference.getZ()));
        double xMod = difference.getX()/largest;
        double yMod = difference.getY()/largest;
        double zMod = difference.getZ()/largest;


        for(int i = 0; i <= largest; i++) {
            int x = (int) (xMod * i);
            System.out.println(i + " : " + x + " = " + (xMod * i));
            int y = (int) (yMod * i);
            int z = (int) (zMod * i);

            line.add(origin.getOffsetLocation(x,y,z));
        }

        return line;
    }


   public static void main(String[] args) {
       boolean[][] area = new boolean[20][20];
       Location origin = new Location(0,0,0);
       Location target = new Location(6,19,0);
       ArrayList<Location> line = findLineNew(origin, target);

       for(Location location: line) {
           area[location.getY()][location.getX()] = true;
       }


       int count = 0;
       for(boolean[] row: area) {
           System.out.print(count % 10+ " :");
           for(boolean b: row) {
               if(b) {
                   System.out.print("1");
               } else {
                   System.out.print("0");
               }
           }
           System.out.println();
           count++;
       }




   }
}
