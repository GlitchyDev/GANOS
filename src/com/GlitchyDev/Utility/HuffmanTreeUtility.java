package com.GlitchyDev.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanTreeUtility {

    public static HashMap<String,Object> getHuffmanValues(Object[] objects, int[] frequency) {
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

        processNode("",values,lastConnectedNode);




        return values;
    }


    private static void processNode(String currentPath, HashMap<String,Object> values, HuffmanNode huffmanNode) {
        if(huffmanNode instanceof ValueHuffmanNode) {
            values.put(currentPath,((ValueHuffmanNode) huffmanNode).getObject());
        } else {
            processNode(currentPath + "0" , values, ((ConnectingHuffmanNode)huffmanNode).getNode1());
            processNode( currentPath + "1", values, ((ConnectingHuffmanNode)huffmanNode).getNode2());
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
