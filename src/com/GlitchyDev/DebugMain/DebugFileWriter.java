package com.GlitchyDev.DebugMain;

import com.GlitchyDev.Utility.HuffmanTreeUtility;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class DebugFileWriter {


    public static void main(String[] args) throws IOException {

        File file = new File(System.getProperty("user.home") + "/Desktop/Test.crp");


        int itemCount = (int) (Math.random() * 100 + 1);
        Integer[] items = new Integer[itemCount];
        int[] frequency = new int[itemCount];
        for(int i = 0; i < items.length; i++) {
            items[i] = (int)(Math.random() * 10 + 1);
            frequency[i] = (int)(100 * Math.random());
        }

        HuffmanTreeUtility.ConnectingHuffmanNode headTreeNode = HuffmanTreeUtility.createHuffmanTree(items,frequency);
        HashMap<String,Object> values = HuffmanTreeUtility.generateDecodeHuffmanValues(headTreeNode);
        for(String s: values.keySet()) {
            System.out.println(values.get(s) + ": " + s);
        }
        System.out.println("-------");
        OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);
        fileOutputBitUtility.writeNextCorrectByteInt(items.length);

        ArrayList<Object> reorderedObjects = HuffmanTreeUtility.encodeObjectList(headTreeNode);
        for(int i = 0; i < reorderedObjects.size(); i++) {
            fileOutputBitUtility.writeNextString("" + reorderedObjects.get(i));
        }
        HuffmanTreeUtility.saveHuffmanTreeValues(fileOutputBitUtility,headTreeNode);
        int totalBytes = fileOutputBitUtility.close();



        InputBitUtility inputBitUtility = new InputBitUtility(file);
        int size = inputBitUtility.getNextCorrectIntByte();
        String[] loadedItems = new String[size];
        for(int i = 0; i < size; i++) {
            loadedItems[i] = inputBitUtility.getNextString();
        }
        HashMap<String,Object> values2 = HuffmanTreeUtility.loadHuffmanTreeValues(inputBitUtility,loadedItems);
        for(String s: values2.keySet()) {
            System.out.println(values2.get(s) + ": " + s);
        }
        System.out.println("------- " + totalBytes);





    }

    public static double[] slightVariaion(ArrayList<Integer> list) {
        double[] varied = new double[list.size()];
        int index = 0;
        for(int i: list) {
            varied[index] = (i + Math.random());
            index++;
        }
        return varied;
    }

    public static double[] slightVariaion(int[] list) {
        double[] varied = new double[list.length];
        int index = 0;
        for(int i: list) {
            varied[index] = (i + Math.random());
            index++;
        }
        return varied;
    }
        /*
        while(true) {
            File file = new File(System.getProperty("user.home") + "/Desktop/Test.crp");

            OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);
            int totalByteCount = 0;


            int width = 9;
            int length = 30;
            int height = 9;




            RegionBase testRegion = new RegionBase(width,length,height);
            for (int y = 0; y < testRegion.getHeight(); y++) {
                for (int x = 0; x < testRegion.getWidth(); x++) {
                    for (int z = 0; z < testRegion.getLength(); z++) {
                        if (Math.random() > 0.5) {
                            testRegion.setBlockRelative(x,y,z,new AirBlock(new Location(x,y,z)));
                            totalByteCount += 1;
                        } else {
                            testRegion.setBlockRelative(x,y,z,new DebugBlock(new Location(x,y,z),1));
                            totalByteCount += 2;
                        }
                    }
                }
            }
            //testRegion.getEntities().add(new DebugEntity(UUID.randomUUID(), new Location(0, 0, 0), Direction.NORTH));
            //testRegion.getEntities().add(new DebugEntity(UUID.randomUUID(), new Location(2, 2, 2), Direction.EAST));

            long timeStart = System.currentTimeMillis();
            testRegion.writeData(fileOutputBitUtility);
            int totalBytes = fileOutputBitUtility.close();
            long timeEnd = System.currentTimeMillis();
            System.out.println("Time: " + (timeEnd - timeStart) / 1000.0 + "s");


            System.out.println("Region took " + totalBytes + "/" + totalByteCount + " bytes " + (100.0 / totalByteCount * totalBytes) + "%");


        for(int z = 0; z < length; z++) {
            for(int x = 0; x < width; x++) {
                System.out.print(testRegion.getBlockRelative(x,0,z) instanceof AirBlock ? 1 : 0);
            }
            System.out.println();
        }
        for(EntityBase entityBase: testRegion.getEntities()) {
            System.out.println(entityBase);
        }


            System.out.println("------");


            InputBitUtility inputBitUtility = new InputBitUtility(file);

            timeStart = System.currentTimeMillis();
            // Needs whole system to run
            RegionBase loadedRegion = new RegionBase(inputBitUtility);
            timeEnd = System.currentTimeMillis();
            System.out.println("Time: " + (timeEnd - timeStart) / 1000.0 + "s");

            */



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
