package com.GlitchyDev.Utility;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public class InputBitUtility {
    private ObjectInputStream objectInputStream;
    private int position;
    private byte currentByte;

    public InputBitUtility(File file) throws IOException {
        this.objectInputStream = new ObjectInputStream(new FileInputStream(file));
        position = 9;
        currentByte = 0;
    }

    public InputBitUtility(InputStream inputStream) throws IOException {
        this.objectInputStream = new ObjectInputStream(inputStream);
        position = 9;
        currentByte = 0;
    }


    // Read Info
    public boolean getNextBit() throws IOException {
        if(position >= 8) {
            //if(position == 8) {
            //    System.out.print("*");
            //}
            currentByte = objectInputStream.readByte();
            position = 0;
        }
        boolean bitValue = getByteValue(currentByte,position++);
        //System.out.print(bitValue ? "1" : "0");
        return bitValue;
    }

    public boolean[] getNextBits(int length) throws IOException {
        boolean[] output = new boolean[length];
        for(int i = 0; i < length; i++) {
            output[i] = getNextBit();
        }
        return output;
    }

    public int getNextCorrectedIntBit(int bits) throws IOException {
        int value = 0;
        for(int i = 0; i < bits; i++) {
            value+= getNextBit() ? Math.pow(2.0, i) : 0;
        }
        return value;
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

    public short getNextShort() throws IOException {
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

        int stringLength = getNextCorrectIntByte();
        for(int i = 0; i < stringLength; i++) {
            output += getNextChar();
        }
        return output;
    }

    public UUID getNextUUID() throws IOException {
        return new UUID(getNextLong(),getNextLong());
    }

    public int getRemainingBytes() throws IOException {
        return objectInputStream.available();
    }

    public void complete() throws IOException {
        //for(int i = position; i < 8; i++) {
        //    System.out.print("#");
        //}
        if(objectInputStream.available() > 0) {
            position = 0;
            currentByte = objectInputStream.readByte();
        } else {
            position = 8;
        }
        //System.out.println();
    }

    public boolean ready() throws IOException {
        return objectInputStream.available() != 0;
    }

    public void close() throws IOException {
        objectInputStream.close();
    }
    // Helper

    private static boolean getByteValue(byte b, int pos) {
        return ((b >> (pos - 1)) & 1) == 1;
    }

    private static int getCorrectedIntValue(byte b) {
        return b + 128;
    }

    private static byte setByteValue(byte b, int pos) {
        if(pos == 0) {
            return b |= 1 << 7;
        }
        return b |= 1 << (pos-1);
    }

}
