package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Blocks.DebugNavigationBlock;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.Collections;

public class NavigationManager {
    private static final int TIMEOUT_LIMIT = 1000;

    public static ArrayList<NavigableBlock> getDirectPath(WorldGameState worldGameState, Location startingLocation, Location endingLocation, NavigatingEntity navigatingEntity) {
        // Might be un-necessary
        ArrayList<ConnectionNode> availableNodes = new ArrayList<>();
        ArrayList<ConnectionNode> closedNodes = new ArrayList<>();

        final ConnectionNode startingNode = ((NavigableBlock) worldGameState.getBlockAtLocation(startingLocation)).getConnectionNode();
        ConnectionNode currentNode = startingNode;
        int movementCount = 0;
        while(!currentNode.getNavigableBlock().getLocation().equals(endingLocation)) {
            availableNodes.remove(currentNode);
            closedNodes.add(currentNode);

            currentNode.addDirectlyNavigatedNodes(navigatingEntity,availableNodes,closedNodes,endingLocation);
            int lowestDirectedMovementCost = Integer.MAX_VALUE;
            for(ConnectionNode connectionNode: availableNodes) {
                if(connectionNode.getDirectedMovementCost() < lowestDirectedMovementCost) {
                    currentNode = connectionNode;
                }
            }
            movementCount++;
            if(movementCount > TIMEOUT_LIMIT) {
                System.out.println("Navigation Timeout, block may be unreachable or too far");
                return null;
            }
        }

        ArrayList<NavigableBlock> blockPathList = new ArrayList<>();
        while(currentNode.getPreviousNode() != null) {
            blockPathList.add(currentNode.getNavigableBlock());
            currentNode = currentNode.getPreviousNode();
        }
        Collections.reverse(blockPathList);
        return blockPathList;


    }


    private static ArrayList<ConnectionNode> availableNodes = new ArrayList<>();
    private static ArrayList<ConnectionNode> closedNodes = new ArrayList<>();
    private static ConnectionNode startingNode = null;
    private static ConnectionNode currentNode = null;
    public static void debugDirectPath(WorldGameState worldGameState, Location startingLocation, Location endingLocation, NavigatingEntity navigatingEntity) {
        System.out.println();
        if(currentNode == null) {
            System.out.println("Starting navigation for block at location " + startingLocation);

            startingNode = ((NavigableBlock) worldGameState.getBlockAtLocation(startingLocation)).getConnectionNode();
            currentNode = startingNode;
        }
        System.out.println("Current Node " + currentNode);
        System.out.println("Available list and closed list Start");
        System.out.println(availableNodes);
        System.out.println(closedNodes);


        availableNodes.remove(currentNode);
        closedNodes.add(currentNode);


        currentNode.addDirectlyNavigatedNodes(navigatingEntity,availableNodes,closedNodes,endingLocation);

        System.out.println("Available list and closed list After");
        System.out.println(availableNodes);
        System.out.println(closedNodes);

        double lowestDirectedMovementCost = Double.MAX_VALUE;
        for(ConnectionNode connectionNode: availableNodes) {
            if(connectionNode.getDirectedMovementCost() < lowestDirectedMovementCost) {
                currentNode = connectionNode;
                lowestDirectedMovementCost = currentNode.getDirectedMovementCost();
            }
        }

        System.out.println("New Selected Node " + currentNode);

        System.out.println(currentNode.getNavigableBlock().getLocation() + "     " + endingLocation);
        if(currentNode.getNavigableBlock().getLocation().equals(endingLocation)) {
            System.out.println("Navigation Manager has completed a path from " + startingLocation + " to " + endingLocation);

            while(currentNode.getPreviousNode() != null) {
                if(currentNode.getNavigableBlock() instanceof DebugNavigationBlock) {
                    ((DebugNavigationBlock) currentNode.getNavigableBlock()).setInPath(true);
                }
                currentNode = currentNode.getPreviousNode();
            }


            for(ConnectionNode connectionNode: closedNodes) {
                connectionNode.resetNavigation();
            }
            for(ConnectionNode connectionNode: availableNodes) {
                connectionNode.resetNavigation();
            }

            currentNode = null;
            startingNode = null;

        }
    }


    // Add a method, so that the Heuristic value used for A* can be calculated locally on this side, so that the ConnectionNode can be used universally for its methods
}
