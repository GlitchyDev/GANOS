package com.GlitchyDev.A;

import com.GlitchyDev.Utility.HuffmanTreeUtility;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugEntity;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import org.lwjgl.system.CallbackI;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main {



    public static void main(String[] args) throws IOException {



        while(true) {
            File file = new File(System.getProperty("user.home") + "/Desktop/Test.crp");

            OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);


            int size = 256;
            int width = size;
            int length = size;
            int height = size;
            System.out.println("Main: Generating Region of " + size + " size");


            int totalByteCount = 0;
            BlockBase[][][] blocks = new BlockBase[height][width][length];
            for (int y = 0; y < blocks.length; y++) {
                for (int x = 0; x < blocks[0].length; x++) {
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        if (Math.random() > 0.5) {
                            blocks[y][x][z] = new AirBlock(new Location());
                            totalByteCount += 1;
                        } else {
                            blocks[y][x][z] = new DebugBlock(new Location(), 1);
                            totalByteCount += 2;
                        }
                    }
                }
            }

            ArrayList<EntityBase> entities = new ArrayList<>();
            entities.add(new DebugEntity(UUID.randomUUID(), new Location(0, 0, 0), Direction.NORTH));
            entities.add(new DebugEntity(UUID.randomUUID(), new Location(2, 2, 2), Direction.EAST));

            RegionBase testRegion = new RegionBase(13, blocks, entities);

            long timeStart = System.currentTimeMillis();
            testRegion.writeData(fileOutputBitUtility);
            int totalBytes = fileOutputBitUtility.close();
            long timeEnd = System.currentTimeMillis();
            System.out.println("Time: " + (timeEnd - timeStart) / 1000.0 + "s");


            System.out.println("Region took " + totalBytes + "/" + totalByteCount + " bytes " + (100.0 / totalByteCount * totalBytes) + "%");

        /*
        for(int z = 0; z < length; z++) {
            for(int x = 0; x < width; x++) {
                System.out.print(testRegion.getBlockRelative(x,0,z) instanceof AirBlock ? 1 : 0);
            }
            System.out.println();
        }
        for(EntityBase entityBase: testRegion.getEntities()) {
            System.out.println(entityBase);
        }
        */

            System.out.println("------");


            InputBitUtility inputBitUtility = new InputBitUtility(file);

            timeStart = System.currentTimeMillis();
            RegionBase loadedRegion = new RegionBase(inputBitUtility);
            timeEnd = System.currentTimeMillis();
            System.out.println("Time: " + (timeEnd - timeStart) / 1000.0 + "s");


        }
        /*
        for(int z = 0; z < length; z++) {
            for(int x = 0; x < width; x++) {
                System.out.print(loadedRegion.getBlockRelative(x,0,z) instanceof AirBlock ? 1 : 0);
            }
            System.out.println();
        }
        for(EntityBase entityBase: loadedRegion.getEntities()) {
            System.out.println(entityBase);
        }
        */




        /*
        String[] items = new String[]{"A","B","C","D","E"};
        int[] frequency = new int[]{5,4,3,2,1};
        HashMap<String,Object> values = HuffmanTreeUtility.generateDecodeHuffmanValues(items,frequency);
        for(String s: values.keySet()) {
            System.out.println(values.get(s) + ": " + s);
        }
        System.out.println("-------");


        OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);
        HuffmanTreeUtility.saveHuffmanTreeValues(fileOutputBitUtility,items,frequency);
        int totalBytes = fileOutputBitUtility.close();

        InputBitUtility inputBitUtility = new InputBitUtility(file);
        HashMap<String,Object> values2 = HuffmanTreeUtility.loadHuffmanTreeValues(inputBitUtility,items);
        for(String s: values2.keySet()) {
            System.out.println(values.get(s) + ": " + s);
        }
        System.out.println("------- " + totalBytes);


        ArrayList<BlockBase> m = new ArrayList<>();
        AirBlock a = new AirBlock(new Location(0,0,0,null));
        m.add(a);
        m.add(new AirBlock(new Location(0,0,0,null)));
        m.add(new AirBlock(new Location(0,0,0,null)));
        m.remove(a);
        m.remove(a);
        System.out.println(m.size());
        */


        /*

        try {
            ServerSocket socket = new ServerSocket(5000);
            Socket clientSocket = new Socket(InetAddress.getLocalHost(),5000);
            Socket serverInteractSocket = socket.accept();

            OutputBitUtility outputBitUtility = new OutputBitUtility(serverInteractSocket.getOutputStream());
            InputBitUtility inputBitUtility = new InputBitUtility(clientSocket.getInputStream());




            int rand = (int) (Math.random() * 5.0);
            System.out.println("Rand " + rand);
            for(int i = 0; i < rand; i++) {
                outputBitUtility.writeNextDouble(Math.random());
            }
            outputBitUtility.flush();
            while(inputBitUtility.getRemainingBytes() != 0) {
                System.out.println(inputBitUtility.getNextDouble());
            }





        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        /*

        System.out.println("---------------------");

        try {
            OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);
            boolean bit = false;
            System.out.println(bit);
            fileOutputBitUtility.writeNextBit(bit);
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
            fileOutputBitUtility.writeNextUUID(uuid);
            long l = 12L;
            System.out.println(l);
            fileOutputBitUtility.writeNextLong(l);
            boolean bit2 = true;
            System.out.println(bit2);
            fileOutputBitUtility.writeNextBit(bit2);
            short s = 13;
            System.out.println(s);
            fileOutputBitUtility.writeNextShort(s);
            String string = "13";
            System.out.println(string);
            fileOutputBitUtility.writeNextChars(string);
            char c = 'a';
            System.out.println(c);
            fileOutputBitUtility.writeNextChar(c);
            double d = 0.56763;
            System.out.println(d);
            fileOutputBitUtility.writeNextDouble(d);
            float f = 0.00054f;
            System.out.println(f);
            fileOutputBitUtility.writeNextFloat(f);
            int i = 155;
            System.out.println(i);
            fileOutputBitUtility.writeNextInteger(i);
            String str = "Thomas";
            System.out.println(str);
            fileOutputBitUtility.writeNextString(str);
            int bitLength = 10;
            int value = 1020;
            fileOutputBitUtility.writeNextCorrectedBitsInt(value, bitLength);
            System.out.println(value);
            int correctedByte = 255;
            fileOutputBitUtility.writeNextCorrectByteInt(correctedByte);
            System.out.println(correctedByte);

            fileOutputBitUtility.close();


            System.out.println("----------");

            InputBitUtility fileInputBitUtility = new InputBitUtility(file);
            System.out.println(fileInputBitUtility.getNextBit());
            System.out.println(fileInputBitUtility.getNextUUID());
            System.out.println(fileInputBitUtility.getNextLong());
            System.out.println(fileInputBitUtility.getNextBit());
            System.out.println(fileInputBitUtility.getNextShort());
            System.out.println(fileInputBitUtility.getNextChars(2));
            System.out.println(fileInputBitUtility.getNextChar());
            System.out.println(fileInputBitUtility.getNextDouble());
            System.out.println(fileInputBitUtility.getNextFloat());
            System.out.println(fileInputBitUtility.getNextInteger());
            System.out.println(fileInputBitUtility.getNextString());
            System.out.println(fileInputBitUtility.getNextCorrectedIntBit(10));
            System.out.println(fileInputBitUtility.getNextCorrectIntByte());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

}
