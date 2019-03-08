package com.GlitchyDev.Utility;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public class OutputBitUtility {
    private ObjectOutputStream objectOutputStream;
    private int position;
    private byte currentByte;

    private int totalOutputBytes = 0;


    public OutputBitUtility(File file) throws IOException {
        this.objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        position = 0;
        currentByte = 0;
    }

    public OutputBitUtility(OutputStream outputStream) throws IOException {
        this.objectOutputStream = new ObjectOutputStream(outputStream);
        position = 0;
        currentByte = 0;
    }


    public void writeNextBit(boolean bit) throws IOException {
        System.out.print(bit ? "A" : "B");
        if(bit) {
            currentByte = setByteValue(currentByte,position);
        }
        position++;

        if(position >= 8) {
            System.out.print("*");
            objectOutputStream.writeByte(currentByte);
            position = 0;
            currentByte = 0;
            totalOutputBytes++;
        }
    }

    public void writeNextBits(boolean[] bits) throws IOException {
        for(int i = 0; i < bits.length; i++) {
           writeNextBit(bits[i]);
        }
    }

    public void writeNextCorrectedBitsInt(int value, int numBits) throws IOException {
        boolean[] bits = new boolean[numBits];

        int tempValue = value;
        for(int i = (numBits - 1); i >= 0; i--) {
            if (tempValue - Math.pow(2.0, i) >= 0) {
                bits[i] = true;
                tempValue -= Math.pow(2.0, i);
            } else {
                bits[i] = false;
            }

        }

        for(int i = 0; i < numBits; i++) {
            writeNextBit(bits[i]);
        }
    }

    public void writeNextByte(byte nextByte) throws IOException {

        boolean[] bits = new boolean[8];

        for(int i = 0; i < 8; i++) {
            bits[i] = getByteValue(nextByte,i);
        }
        writeNextBits(bits);
    }

    public void writeNextBytes(byte[] nextBytes) throws IOException {
        for(byte b: nextBytes) {
            writeNextByte(b);
        }
    }

    public void writeNextCorrectByteInt(int integer) throws IOException {
        writeNextByte(getCorrectByteValue(integer));
    }

    public void writeNextInteger(int integer) throws IOException {
        writeNextBytes(ByteBuffer.allocate(4).putInt(integer).array());
    }

    public void writeNextShort(short s) throws IOException {
        writeNextBytes(ByteBuffer.allocate(2).putShort(s).array());
    }

    public void writeNextLong(long l) throws IOException {
        writeNextBytes(ByteBuffer.allocate(8).putLong(l).array());
    }

    public void writeNextFloat(float f) throws IOException {
        writeNextBytes(ByteBuffer.allocate(4).putFloat(f).array());
    }

    public void writeNextDouble(double d) throws IOException {
        writeNextBytes(ByteBuffer.allocate(8).putDouble(d).array());
    }

    public void writeNextChar(char c) throws IOException {
        writeNextBytes(ByteBuffer.allocate(8).putChar(c).array());
    }

    public void writeNextChars(String s) throws IOException {

        for(char c: s.toCharArray()) {
            writeNextChar(c);
        }
    }

    public void writeNextString(String s) throws IOException {
        writeNextCorrectByteInt(s.length());
        for(char c: s.toCharArray()) {
            writeNextChar(c);
        }
    }

    public void writeNextUUID(UUID uuid) throws IOException {

        writeNextLong(uuid.getMostSignificantBits());
        writeNextLong(uuid.getLeastSignificantBits());
    }

    public int flush() throws IOException {
        if(position != 0) {
            objectOutputStream.writeByte(currentByte);
            for(int i = position; i < 8; i++) {
                System.out.print("!");
            }
        }
        System.out.println();
        System.out.println("OutputBitUtility: Wasted " + (7-position) + " Bits");
        position = 0;


        objectOutputStream.flush();
        return totalOutputBytes;
    }

    /*
    Write the remaining byte info to memory, unused section and all
     */
    public int close() throws IOException {
        int count = flush();
        objectOutputStream.close();
        return count;
    }



    // Helper Methods

    private static byte setByteValue(byte b, int pos) {
        if(pos == 0) {
            return b |= 1 << 7;
        }
        return b |= 1 << (pos-1);
    }

    private static byte getCorrectByteValue(int i) {
        return (byte) (-128 + (i));
    }


    private static boolean getByteValue(byte b, int pos) {
        return ((b >> (pos - 1)) & 1) == 1;
    }

}
