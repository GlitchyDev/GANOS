package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.World;

import java.util.ArrayList;

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

            currentNode.addDirectlyNavigatedNodes(navigatingEntity,availableNodes,endingLocation);
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
        ConnectionNode finalNode = currentNode;
        currentNode = startingNode;
        while(currentNode != finalNode) {
            blockPathList.add(currentNode.getNavigableBlock());
            currentNode = currentNode.getNextNode();
        }

        return blockPathList;


    }


    private static ArrayList<ConnectionNode> availableNodes = new ArrayList<>();
    private static ArrayList<ConnectionNode> closedNodes = new ArrayList<>();
    private static ConnectionNode startingNode = null;
    private static ConnectionNode currentNode = null;
    public static void debugDirectPath(WorldGameState worldGameState, World world, Location startingLocation, Location endingLocation, NavigatingEntity navigatingEntity) {
        if(currentNode == null) {
            startingNode = ((NavigableBlock) worldGameState.getBlockAtLocation(startingLocation)).getConnectionNode();
            currentNode = startingNode;
        }

        availableNodes.remove(currentNode);
        closedNodes.add(currentNode);
        currentNode.addDirectlyNavigatedNodes(navigatingEntity,availableNodes,endingLocation);

        int lowestDirectedMovementCost = Integer.MAX_VALUE;
        for(ConnectionNode connectionNode: availableNodes) {
            if(connectionNode.getDirectedMovementCost() < lowestDirectedMovementCost) {
                currentNode = connectionNode;
            }
        }

        if(currentNode.getNavigableBlock().getLocation().equals(endingLocation)) {
            System.out.println("Navigation Manager has completed a path from " + startingLocation + " to " + endingLocation);
            currentNode = null;
            startingNode = null;
        }
    }


    // Add a method, so that the Heuristic value used for A* can be calculated locally on this side, so that the ConnectionNode can be used universally for its methods
}