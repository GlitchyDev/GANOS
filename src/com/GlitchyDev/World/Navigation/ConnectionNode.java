package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionNode {
    private final NavigableBlock navigableBlock;
    private final HashMap<ConnectionNode,PathType> pathConnections;
    private ConnectionNode previousNode;

    private double heuristicNavigationValue;
    private double accumulatedMovementCost;

    public ConnectionNode(NavigableBlock navigableBlock) {
        this.navigableBlock = navigableBlock;
        pathConnections = new HashMap<>();
        previousNode = null;
        heuristicNavigationValue = 0;
        accumulatedMovementCost = 0;
    }


    // A* Functionality
    public void addDirectlyNavigatedNodes(NavigatingEntity navigatingEntity, ArrayList<ConnectionNode> availableNodes, ArrayList<ConnectionNode> closedList, Location navigationTarget) {
        for(ConnectionNode connectionNode: pathConnections.keySet()) {
            if (!closedList.contains(connectionNode)) {
                int movementCost = navigatingEntity.getMovementCost(pathConnections.get(connectionNode));
                if (!availableNodes.contains(connectionNode)) {
                    connectionNode.directlyNavigatedNode(this, navigatingEntity.getMovementCost(pathConnections.get(connectionNode)), navigationTarget);
                    availableNodes.add(connectionNode);
                } else {
                    if (connectionNode.getAccumulatedMovementCost() >= getAccumulatedMovementCost() + movementCost) {
                        connectionNode.directlyNavigatedNode(this, navigatingEntity.getMovementCost(pathConnections.get(connectionNode)), navigationTarget);
                    }
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
        heuristicNavigationValue = 0;
        accumulatedMovementCost = 0;
    }

    public void resetPathConnections() {
        pathConnections.clear();
    }


    public double getAccumulatedMovementCost() {
        return accumulatedMovementCost;
    }

    public double getHeuristicNavigationValue() {
        return heuristicNavigationValue;
    }

    public double getDirectedMovementCost() {
        return accumulatedMovementCost + heuristicNavigationValue;
    }

    public ConnectionNode getPreviousNode() {
        return previousNode;
    }

    public HashMap<ConnectionNode, PathType> getPathConnections() {
        return pathConnections;
    }

    public NavigableBlock getNavigableBlock() {
        return navigableBlock;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getDirectedMovementCost();
    }
}
