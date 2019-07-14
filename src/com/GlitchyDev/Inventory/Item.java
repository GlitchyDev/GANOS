package com.GlitchyDev.Inventory;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Inventory.Enums.ItemDropReason;
import com.GlitchyDev.Inventory.Enums.ItemPickupReason;
import com.GlitchyDev.Inventory.Enums.ItemTransferReason;
import com.GlitchyDev.Inventory.Enums.ItemType;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public abstract class Item {
    protected final WorldGameState worldGameState;
    //
    protected final ItemType itemType;

    public Item(ItemType itemType, WorldGameState worldGameState) {
        this.itemType = itemType;
        this.worldGameState = worldGameState;
    }

    public Item(ItemType itemType, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        this.itemType = itemType;
        this.worldGameState = worldGameState;
    }

    public abstract void renderItemIcon(Renderer renderer, int screenX, int screenY, int layer);

    public abstract boolean attemptItemPickup(Inventory inventory, ItemPickupReason itemPickupReason);
    public abstract boolean attemptItemDrop(Inventory inventory, ItemDropReason itemDropReason);
    public abstract boolean attemptItemTransfer(Inventory inventory, Inventory oldInventory, ItemTransferReason itemTransferReason);

    public abstract boolean onItemPickup(Inventory inventory, ItemPickupReason itemPickupReason);
    public abstract boolean onItemDrop(Inventory inventory, ItemDropReason itemDropReason);
    public abstract boolean onItemTransfer(Inventory oldInventory, Inventory newInventory, ItemTransferReason itemTransferReason);

    public abstract double getItemWeight();

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(itemType.ordinal());
    }


    public final ItemType getItemType() {
        return itemType;
    }
}
