package com.GlitchyDev.A;

import com.GlitchyDev.IO.InputBitUtility;
import com.GlitchyDev.IO.OutputBitUtility;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Main {



    public static void main(String[] args) {

        /*
        int[] arrangement = new int[]{0,15,25,35,45,55};
        String[] items = new String[]{"0","15","25","35","45","55"};
        HashMap<String,Object> values = HuffmanTreeUtility.getHuffmanValues(items,arrangement);
        for(String s: values.keySet()) {
            System.out.println(values.get(s) + ": " + s);
        }
        */
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



        System.out.println("---------------------");
        File file = new File(System.getProperty("user.home") + "/Desktop/Test.crp");

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
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
