package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionNode {
    private final NavigableBlock navigableBlock;
    private final HashMap<ConnectionNode,PathType> pathConnections;
    private ConnectionNode previousNode;
    private ConnectionNode nextNode;

    private int heuristicNavigationValue;
    private int accumulatedMovementCost;

    public ConnectionNode(NavigableBlock navigableBlock) {
        this.navigableBlock = navigableBlock;
        pathConnections = new HashMap<>();
        previousNode = null;
        nextNode = null;
        heuristicNavigationValue = 0;
        accumulatedMovementCost = 0;
    }


    // A* Functionality
    public void addDirectlyNavigatedNodes(NavigatingEntity navigatingEntity, ArrayList<ConnectionNode> availableNodes, Location navigationTarget) {
        for(ConnectionNode connectionNode: pathConnections.keySet()) {
            int movementCost = navigatingEntity.getMovementCost(pathConnections.get(connectionNode));
            if(!availableNodes.contains(connectionNode)) {
                directlyNavigatedNode(this,movementCost,navigationTarget);
                availableNodes.add(this);
            } else {
                if(connectionNode.getAccumulatedMovementCost() >= getAccumulatedMovementCost() + movementCost) {
                    directlyNavigatedNode(previousNode,movementCost,navigationTarget);
                }
            }
        }
    }

    public void directlyNavigatedNode(ConnectionNode previousNode, int movementCost, Location navigationTarget) {
        this.previousNode = previousNode;
        Location currentLocation = navigableBlock.getLocation();
        accumulatedMovementCost = previousNode.getAccumulatedMovementCost() + movementCost;
        heuristicNavigationValue = (Math.abs(currentLocation.getX()-navigationTarget.getX()) + Math.abs(currentLocation.getY()-navigationTarget.getY()) + Math.abs(currentLocation.getZ()-navigationTarget.getZ()));
    }


    public void resetNavigation() {
        previousNode = null;
        nextNode = null;
        heuristicNavigationValue = 0;
        accumulatedMovementCost = 0;
    }

    public void resetPathConnections() {
        pathConnections.clear();
    }


    public int getAccumulatedMovementCost() {
        return accumulatedMovementCost;
    }

    public int getHeuristicNavigationValue() {
        return heuristicNavigationValue;
    }

    public int getDirectedMovementCost() {
        return accumulatedMovementCost + heuristicNavigationValue;
    }

    public ConnectionNode getPreviousNode() {
        return previousNode;
    }

    public ConnectionNode getNextNode() {
        return nextNode;
    }

    public HashMap<ConnectionNode, PathType> getPathConnections() {
        return pathConnections;
    }

    public NavigableBlock getNavigableBlock() {
        return navigableBlock;
    }
}
