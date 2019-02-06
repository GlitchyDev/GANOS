package com.GlitchyDev.IO;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public class FileInputBitUtility {
    private ObjectInputStream objectInputStream;
    private int position;
    private byte currentByte;

    public FileInputBitUtility(File file) throws IOException {
        this.objectInputStream = new ObjectInputStream(new ObjectInputStream(new FileInputStream(file)));
        position = 8;
        currentByte = 0;

    }


    // Read Info
    public boolean getNextBit() throws IOException {
        if(position >= 8) {
            currentByte = objectInputStream.readByte();
            position = 0;
        }
        return getByteValue(currentByte,position++);
    }

    public boolean[] getNextBits(int length) throws IOException {
        boolean[] output = new boolean[length];
        for(int i = 0; i < length; i++) {
            output[i] = getNextBit();
        }
        return output;
    }

    public byte getNextByte() throws IOException {
        byte output = 0;
        for(int i = 0; i < 8; i++) {
            if(getNextBit()) {
                output = setByteValue(output, i);
            }
        }
        return output;
    }

    public byte[] getNextBytes(int length) throws IOException {
        byte[] output = new byte[length];
        for(int i = 0; i < length; i++) {
            output[i] = getNextByte();
        }
        return output;
    }

    public int getNextCorrectIntByte() throws IOException {
        return getCorrectedIntValue(getNextByte());
    }

    public int getNextInteger() throws IOException {
        return ByteBuffer.wrap(getNextBytes(4)).getInt();
    }

    public long getNextShort() throws IOException {
        return ByteBuffer.wrap(getNextBytes(2)).getShort();
    }

    public long getNextLong() throws IOException {
        return ByteBuffer.wrap(getNextBytes(8)).getLong();
    }

    public float getNextFloat() throws IOException {
        return ByteBuffer.wrap(getNextBytes(4)).getFloat();
    }

    public double getNextDouble() throws IOException {
        return ByteBuffer.wrap(getNextBytes(8)).getDouble();
    }

    public char getNextChar() throws IOException {
        return ByteBuffer.wrap(getNextBytes(8)).getChar();
    }

    public String getNextChars(int length) throws IOException {
        String output = "";
        for(int i = 0; i < length; i++) {
            output += getNextChar();
        }
        return output;
    }

    public String getNextString() throws IOException {
        String output = "";

        int stringLength = getCorrectedIntValue(getNextByte());
        for(int i = 0; i < stringLength; i++) {
            output += getNextChar();
        }
        return output;
    }

    public UUID getNextUUID() throws IOException {
        return new UUID(getNextLong(),getNextLong());
    }



    // Helper

    private static boolean getByteValue(byte b, int pos) {
        return ((b >> (pos - 1)) & 1) == 1;
    }

    private static int getCorrectedIntValue(byte b) {
        return (b>=0) ? b : (128 + b);
    }

    private static byte setByteValue(byte b, int pos) {
        if(pos == 0) {
            return b |= 1 << 7;
        }
        return b |= 1 << (pos-1);
    }

}
