package com.GlitchyDev.IO;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public class FileOutputBitUtility {
    private ObjectOutputStream objectOutputStream;
    private int position;
    private byte currentByte;


    public FileOutputBitUtility(File file) throws IOException {
        this.objectOutputStream = new ObjectOutputStream(new ObjectOutputStream(new FileOutputStream(file)));
        position = 0;
        currentByte = 0;
    }


    public void writeNextBit(boolean bit) throws IOException {
        if(bit) {
            currentByte = setByteValue(currentByte,position);
        }
        position++;

        if(position >= 8) {
            objectOutputStream.writeByte(currentByte);
            position = 0;
            currentByte = 0;
        }
    }

    public void writeNextBits(boolean[] bits) throws IOException {
        for(int i = 0; i < bits.length; i++) {
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
        System.out.println("A" + s.length());
        System.out.println("B" + getCorrectByteValue(s.length()));

        writeNextByte(getCorrectByteValue(s.length()));
        for(char c: s.toCharArray()) {
            writeNextChar(c);
        }
    }

    public void writeNextUUID(UUID uuid) throws IOException {

        writeNextLong(uuid.getMostSignificantBits());
        writeNextLong(uuid.getLeastSignificantBits());
    }

    /*
    Write the remaining byte info to memory, unused section and all
     */
    public void close() throws IOException {
        if(position != 0) {
            writeNextByte(currentByte);
            System.out.println("FileOutputBitUtility: Wasted " + (8-position));

        }
        objectOutputStream.close();
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
