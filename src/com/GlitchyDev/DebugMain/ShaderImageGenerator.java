package com.GlitchyDev.DebugMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShaderImageGenerator {

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(500,500,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();

        final int GRID_WIDTH = 40;
        final int MAX_CIRCLES_SIZE = 100;
//
        int[][] grid = generateVerticalSlidingGraph(10,10,false);
        int gridLimit = grid.length*grid.length-1;

        for(int y1 = 0; y1 < grid.length; y1++) {
            for(int x1 = 0; x1 < grid.length; x1++) {
                System.out.print(grid[x1][y1] + "|");
            }
            System.out.println();
        }

        int max = 0;
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                double distance = getDistance(x,y,250,250);
                double angle = getRatio(getAngle(x,y,250,250),360);
                int value = 0;

                double ratio = grid.length/500.0;
                int gridX = (int) (ratio * x);
                int gridY = (int) (ratio * y);
                double r = getRatio(grid[gridX][gridY],10);
                double g = angle;
                double b = angle;
                double a = 1;
                image.setRGB(x,y,new Color(normalize(r),normalize(g),normalize(b),normalize(a)).getRGB());



            }
        }





        String fileName = "Debug1";
        File outputfile = new File("GameAssets/Textures/GeneratedImages/" + fileName + ".png");
        ImageIO.write(image, "png", outputfile);
    }


    private static double getDistance(int sourceX, int sourceY, int pointX, int pointY) {
        return Math.sqrt(Math.pow(sourceX-pointX,2) + Math.pow(sourceY-pointY,2));
    }

    private static double getRatio(double input, double max) {
        if(input < max) {
            return 1.0 / max * input;
        }
        return 1.0;
    }

    private static double invertRatio(double d) {
        return 1.0-d;
    }

    private static double binaryLimiter(double input, double limiter, double limit) {
        return limiter(input,0,limiter,limit);
    }

    private static double limiter(double inputA, double inputB, double limiter, double limit) {
        return betweenLimiter(inputA,inputB,limiter,0,limit);
    }

    private static double betweenLimiter(double inputA, double inputB, double limiter, double lowerLimit, double upperLimit) {
        if(limiter >= lowerLimit && limiter <= upperLimit) {
            return inputA;
        }
        return inputB;
    }

    private static double getAngle(int x1, int y1, int x2, int y2) {
        return  360.0-(180.0/Math.PI * Math.atan2(x1 - x2, y1 - y2)+180);
    }

    public static int[][] generateSectorGraph(int sectorWidth, int sectorHeight, int totalWidth, int totalHeight) {
        int[][] sectorGraph = new int[totalWidth/sectorWidth][totalHeight/sectorWidth];
        int count = 0;
        for(int y = 0; y < totalHeight/sectorHeight; y++) {
            for(int x = 0; x < totalWidth/sectorWidth; x++) {
                sectorGraph[x][y] = count;
                count++;
            }
        }
        return sectorGraph;
    }


    private static int getSectorIDFromGraph(int x, int y, int sectorWidth, int sectorHeight, int totalWidth, int totalHeight, int[][] graph) {
        return graph[x/sectorWidth][y/sectorHeight];
    }

    public static void randomizeSectorGraph(int[][] sectorGraph) {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>(sectorGraph.length * sectorGraph[0].length);
        for(int y = 0; y < sectorGraph.length; y++) {
            for(int x = 0; x < sectorGraph[0].length; x++) {
                list.add(sectorGraph[x][y]);
            }
        }
        Collections.shuffle(list);
        for(int y = 0; y < sectorGraph.length; y++) {
            for(int x = 0; x < sectorGraph[0].length; x++) {
                sectorGraph[x][y] = list.get(0);
                list.remove(0);
            }
        }
    }

    private static int getRawSectorID(int x, int y, int sectorWidth, int sectorHeight, int totalWidth, int totalHeight) {
        int sectorID = (x/sectorWidth + (y/sectorHeight)*(totalWidth/sectorWidth));
        System.out.println("X: " + x + " Y:" + y + " " + sectorID);
        return sectorID;
    }

    public static int getRawSectorSizing(int sectorWidth, int sectorHeight, int totalWidth, int totalHeight) {
        return (totalWidth/sectorWidth)*(totalHeight/sectorHeight)-1;
    }

    private static double getSectorID(int x, int y, int sectorWidth, int sectorHeight, int totalWidth, int totalHeight) {
        return (1.0/getRawSectorSizing(sectorWidth,sectorHeight,totalWidth,totalHeight)) * getRawSectorID(x,y,sectorWidth,sectorHeight,totalWidth,totalHeight);
    }


    private static int normalize(double d) {
        if(d <= 1.0) {
            if(d >= 0.0) {
                return (int) (255.0 * d);
            }
            return 0;
        }
        else {
            return 255;
        }
    }

    private static int[][] generateSpiralPatternGraph(int gridLength) {
        int[][] grid = new int[gridLength][gridLength];
        int requiredLayers = (gridLength - 1)/2;

        int currentCount = 0;
        for(int i = 0; i < requiredLayers; i++) {
            currentCount = generateRightWardStroke(grid,i,i,gridLength-i*2,currentCount);
            currentCount = generateDownwardStroke(grid,gridLength - i - 1,1 + i,gridLength - (i*2 + 2),currentCount);
            currentCount = generateLeftwardStroke(grid,gridLength - i - 1,gridLength - i - 1,gridLength-i*2,currentCount);
            currentCount = generateUpwardStroke(grid,i,gridLength - (i+1) - 1,gridLength - (i*2 + 2),currentCount);
        }
        grid[gridLength/2][gridLength/2] = currentCount;


        return grid;
    }

    public static int[][] generateHorizontalSlidingGraph(int xSectors, int ySectors, boolean flip) {
        int[][] sectorGraph = new int[xSectors][ySectors];
        int count = 0;
        for(int y = 0; y < ySectors; y++) {
            if((y % 2 == 0) != flip) {
                generateRightWardStroke(sectorGraph,0,y,xSectors,0);
            } else {
                generateLeftwardStroke(sectorGraph,xSectors-1,y,xSectors,0);
            }
        }
        return sectorGraph;
    }

    public static int[][] generateVerticalSlidingGraph(int xSectors, int ySectors, boolean flip) {
        int[][] sectorGraph = new int[xSectors][ySectors];
        int count = 0;
        for(int x= 0; x < xSectors; x++) {
            if((x % 2 == 0) != flip) {
                generateDownwardStroke(sectorGraph,x,0,ySectors,0);
            } else {
                generateUpwardStroke(sectorGraph,x,ySectors-1,ySectors,0);
            }
        }
        return sectorGraph;
    }

    private static int generateDownwardStroke(int[][] grid, int relativeX, int relativeY, int strokeLength, int lastCount) {
        for(int i = 0; i < strokeLength; i++)  {
            writeValue(grid,relativeX,relativeY+i,lastCount + i);
        }
        return lastCount + strokeLength;
    }

    private static int generateUpwardStroke(int[][] grid, int relativeX, int relativeY, int strokeLength, int lastCount) {
        for(int i = 0; i < strokeLength; i++)  {
            writeValue(grid,relativeX,relativeY-i,lastCount + i);
        }
        return lastCount + strokeLength;
    }

    private static int generateLeftwardStroke(int[][] grid, int relativeX, int relativeY, int strokeLength, int lastCount) {
        for(int i = 0; i < strokeLength; i++)  {
            writeValue(grid,relativeX-i,relativeY,lastCount + i);
        }
        return lastCount + strokeLength;
    }

    private static int generateRightWardStroke(int[][] grid, int relativeX, int relativeY, int strokeLength, int lastCount) {
        for(int i = 0; i < strokeLength; i++)  {
            writeValue(grid,relativeX+i,relativeY,lastCount + i);
        }
        return lastCount + strokeLength;
    }

    private static void writeValue(int[][] grid, int x, int y, int value) {
        if(grid[x][y] == 0) {
            grid[x][y] = value;
        }
    }
}
