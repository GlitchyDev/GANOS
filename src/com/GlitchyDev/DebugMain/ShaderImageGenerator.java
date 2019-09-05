package com.GlitchyDev.DebugMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShaderImageGenerator {

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(500,500,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();

        final int GRID_WIDTH = 40;
        final int MAX_CIRCLES_SIZE = 100;


        int max = 0;
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                double distance = getDistance(x,y,250,250);
                double angle = getAngle(x,y,250,250);
                double r = getSectorID(x,y, 100,100,500,500);
                double g = getRatio(y % 100,100);
                double b = getRatio(x % 100,100);
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

    private static int getRawSectorID(int x, int y, int sectorWidth, int sectorHeight, int totalWidth, int totalHeight) {
        int sizing = getRawSectorSizing(sectorWidth,sectorHeight,totalWidth,totalHeight);
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
}
