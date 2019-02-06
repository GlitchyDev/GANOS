package com.GlitchyDev.IO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class FileOutputBitUtility {
    private ObjectInputStream objectInputStream;
    private int position;
    private byte currentByte;

    public FileOutputBitUtility(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
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

    public int getCorrectIntByte() throws IOException {
        return getCorrectedIntValue(getNextByte());
    }



    // Helper

    private static boolean getByteValue(byte b, int pos) {
        return ((b >> (pos - 1)) & 1) == 1;
    }

    private static int getCorrectedIntValue(byte b) {
        return (b>=0) ? b : (127 + (129 + b));
    }

    private static byte setByteValue(byte b, int pos) {
        if(pos == 0) {
            return b |= 1 << 7;
        }
        return b |= 1 << (pos-1);
    }

}
