package com.GlitchyDev.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HuffmanTreeUtility {

    /**
     * Generates a Huffman Value Table from input, requires at least 2 items to function
     * @param objects
     * @param frequency
     * @return
     */
    public static HashMap<String,Object> generateDecodeHuffmanValues(Object[] objects, int[] frequency) {
        HashMap<String,Object> values = new HashMap<>(objects.length);
        ArrayList<HuffmanNode> unmatchedNodes = new ArrayList<>(objects.length);
        for(int i = 0; i < objects.length; i++) {
            unmatchedNodes.add(new ValueHuffmanNode(frequency[i],objects[i]));
        }

        ConnectingHuffmanNode lastConnectedNode = null;
        while(unmatchedNodes.size() != 1) {
            HuffmanNode lowestNodeOne = null;
            for(HuffmanNode node: unmatchedNodes) {
                if(lowestNodeOne == null || node.getValue() < lowestNodeOne.getValue()) {
                    lowestNodeOne = node;
                }
            }
            unmatchedNodes.remove(lowestNodeOne);

            HuffmanNode lowestNodeTwo = null;
            for(HuffmanNode node: unmatchedNodes) {
                if(lowestNodeTwo == null || node.getValue() < lowestNodeTwo.getValue()) {
                    lowestNodeTwo = node;
                }
            }
            unmatchedNodes.remove(lowestNodeTwo);

            lastConnectedNode = new ConnectingHuffmanNode(lowestNodeOne,lowestNodeTwo);
            unmatchedNodes.add(lastConnectedNode);


        }

        HuffmanTreeUtility.processNodeDecode("",values,lastConnectedNode);


        return values;
    }

    public static HashMap<Object,String> generateEncodeHuffmanValues(Object[] objects, int[] frequency) {
        HashMap<Object,String> values = new HashMap<>(objects.length);
        ArrayList<HuffmanNode> unmatchedNodes = new ArrayList<>(objects.length);
        for(int i = 0; i < objects.length; i++) {
            unmatchedNodes.add(new ValueHuffmanNode(frequency[i],objects[i]));
        }

        ConnectingHuffmanNode lastConnectedNode = null;
        while(unmatchedNodes.size() != 1) {
            HuffmanNode lowestNodeOne = null;
            for(HuffmanNode node: unmatchedNodes) {
                if(lowestNodeOne == null || node.getValue() < lowestNodeOne.getValue()) {
                    lowestNodeOne = node;
                }
            }
            unmatchedNodes.remove(lowestNodeOne);

            HuffmanNode lowestNodeTwo = null;
            for(HuffmanNode node: unmatchedNodes) {
                if(lowestNodeTwo == null || node.getValue() < lowestNodeTwo.getValue()) {
                    lowestNodeTwo = node;
                }
            }
            unmatchedNodes.remove(lowestNodeTwo);

            lastConnectedNode = new ConnectingHuffmanNode(lowestNodeOne,lowestNodeTwo);
            unmatchedNodes.add(lastConnectedNode);


        }

        HuffmanTreeUtility.processNodeEncode("",values,lastConnectedNode);


        return values;
    }



    public static HashMap<String,Object> loadHuffmanTreeValues(InputBitUtility inputBitUtility, Object[] objects) throws IOException {
        // Only supports 8 Layers
        HashMap<String,Object> values = new HashMap<>(objects.length);

        ArrayList<Object> objectList = new ArrayList<>();
        objectList.addAll(Arrays.asList(objects));

        HuffmanNode topNode = decodeNode(inputBitUtility,objectList);

        HuffmanTreeUtility.processNodeDecode("", values, topNode);

        return values;
    }


    public static void saveHuffmanTreeValues(OutputBitUtility outputBitUtility, Object[] objects, int[] frequency) throws IOException {
        // Make Tree
        ConnectingHuffmanNode headNode = createConnectingBranch("", generateDecodeHuffmanValues(objects, frequency));
        encodeNode(outputBitUtility,headNode);
    }





    private static HuffmanNode decodeNode(InputBitUtility inputBitUtility, ArrayList<Object> objectList) throws IOException {
        if(inputBitUtility.getNextBit()) {
            return new ConnectingHuffmanNode(decodeNode(inputBitUtility,objectList), decodeNode(inputBitUtility,objectList));
        } else {
            return new ValueHuffmanNode(0,objectList.get(objectList.size() - 1));
        }

    }


    private static ConnectingHuffmanNode createConnectingBranch(String currentModifier, HashMap<String,Object> values) {
        // Left Node
        HuffmanNode node1;
        if(values.containsKey(currentModifier + "0")) {
            node1 = new ValueHuffmanNode(0,null);
        } else {
            node1 = createConnectingBranch(currentModifier + "0",values);
        }

        HuffmanNode node2;
        if(values.containsKey(currentModifier + "1")) {
            node2 = new ValueHuffmanNode(0,null);
        } else {
            node2 = createConnectingBranch(currentModifier + "1",values);
        }

        return new ConnectingHuffmanNode(node1, node2);

    }

    private static void encodeNode(OutputBitUtility outputBitUtility, HuffmanNode node) throws IOException {
        if(node instanceof ConnectingHuffmanNode) {
            outputBitUtility.writeNextBit(true);
            encodeNode(outputBitUtility,((ConnectingHuffmanNode) node).getNode1());
            encodeNode(outputBitUtility,((ConnectingHuffmanNode) node).getNode2());

        } else {
            outputBitUtility.writeNextBit(false);

        }

    }



    private static void processNodeDecode(String currentPath, HashMap<String,Object> values, HuffmanNode huffmanNode) {
        if(huffmanNode instanceof ValueHuffmanNode) {
            values.put(currentPath,((ValueHuffmanNode) huffmanNode).getObject());
        } else {
            HuffmanTreeUtility.processNodeDecode(currentPath + "0" , values, ((ConnectingHuffmanNode)huffmanNode).getNode1());
            HuffmanTreeUtility.processNodeDecode( currentPath + "1", values, ((ConnectingHuffmanNode)huffmanNode).getNode2());
        }
    }

    private static void processNodeEncode(String currentPath, HashMap<Object,String> values, HuffmanNode huffmanNode) {
        if(huffmanNode instanceof ValueHuffmanNode) {
            values.put(((ValueHuffmanNode) huffmanNode).getObject(),currentPath);
        } else {
            processNodeEncode(currentPath + "0" , values, ((ConnectingHuffmanNode)huffmanNode).getNode1());
            processNodeEncode( currentPath + "1", values, ((ConnectingHuffmanNode)huffmanNode).getNode2());
        }
    }


    private static abstract class HuffmanNode {
        public abstract int getValue();
    }
    private static class ValueHuffmanNode extends HuffmanNode {
        private int frequency;
        private Object object;
        public ValueHuffmanNode(int frequency, Object object) {
            this.frequency = frequency;
            this.object = object;
        }
        @Override
        public int getValue() {
            return frequency;
        }
        public Object getObject() {
            return object;
        }
    }
    private static class ConnectingHuffmanNode extends HuffmanNode {
        private int value;
        private HuffmanNode node1;
        private HuffmanNode node2;
        public ConnectingHuffmanNode(HuffmanNode node1, HuffmanNode node2) {
            this.value = node1.getValue() + node2.getValue();
            this.node1 = node1;
            this.node2 = node2;
        }
        @Override
        public int getValue() {
            return value;
        }
        public HuffmanNode getNode1() {
            return node1;
        }
        public HuffmanNode getNode2() {
            return node2;
        }
    }

}
