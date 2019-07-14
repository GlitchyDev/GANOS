package com.GlitchyDev.Inventory;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Inventory.Enums.ItemDropReason;
import com.GlitchyDev.Inventory.Enums.ItemPickupReason;
import com.GlitchyDev.Inventory.Enums.ItemTransferReason;
import com.GlitchyDev.Inventory.Enums.ItemType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.util.ArrayList;

public class Inventory {
    private final WorldGameState worldGameState;
    private InventoryHolder inventoryHolder;
    private final ArrayList<Item> heldItems;
    private int inventoryWeightSize;

    public Inventory(WorldGameState worldGameState, InventoryHolder inventoryHolder, int inventoryWeightSize) {
        this.worldGameState = worldGameState;
        this.inventoryHolder = inventoryHolder;
        this.heldItems = new ArrayList<>();
        this.inventoryWeightSize = inventoryWeightSize;
    }

    public Inventory(InputBitUtility inputBitUtility, WorldGameState worldGameState, InventoryHolder inventoryHolder) throws IOException {
        this.worldGameState = worldGameState;
        this.inventoryHolder = inventoryHolder;
        this.inventoryWeightSize = inputBitUtility.getNextCorrectIntByte();
        int inventorySize = inputBitUtility.getNextCorrectIntByte();
        heldItems = new ArrayList<>(inventorySize);
        for(int i = 0; i < inventorySize; i++) {
            ItemType itemType = ItemType.values()[inputBitUtility.getNextCorrectIntByte()];
            Item item = itemType.getItemFromInput(inputBitUtility,worldGameState);
            heldItems.add(item);
        }
    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(inventoryWeightSize);
        outputBitUtility.writeNextCorrectByteInt(heldItems.size());
        for(int i = 0; i < heldItems.size(); i++) {
            heldItems.get(i).writeData(outputBitUtility);
        }
    }



    public void addItem(Item item, ItemPickupReason itemPickupReason) {
        item.onItemPickup(this,itemPickupReason);
        heldItems.add(item);
    }

    public void removeItem(Item item, ItemDropReason itemDropReason) {
        item.onItemDrop(this,itemDropReason);
        heldItems.add(item);
    }

    public void transferItem(Item item, Inventory newInventory, ItemTransferReason itemTransferReason) {
        item.onItemTransfer(this,newInventory,itemTransferReason);
        heldItems.remove(item);
        newInventory.getHeldItems().add(item);
    }

    public int getInventoryWeightSize() {
        return inventoryWeightSize;
    }

    public InventoryHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    public void setInventoryWeightSize(int inventoryWeightSize) {
        this.inventoryWeightSize = inventoryWeightSize;
    }

    public ArrayList<Item> getHeldItems() {
        return heldItems;
    }
}
