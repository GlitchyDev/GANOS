package com.GlitchyDev.Inventory;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Inventory.Enums.ItemEquipReason;
import com.GlitchyDev.Inventory.Enums.ItemType;
import com.GlitchyDev.Inventory.Enums.ItemUnequipReason;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public abstract class EquipableItem extends Item {
    public EquipableItem(ItemType itemType, WorldGameState worldGameState) {
        super(itemType, worldGameState);
    }

    public EquipableItem(ItemType itemType, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(itemType, worldGameState, inputBitUtility);
    }

    public abstract boolean attemptItemEquip(InventoryHolder inventoryHolder, Entity targetEntity, ItemEquipReason itemEquipReason);
    public abstract boolean attemptItemUnequip(InventoryHolder inventoryHolder, Entity targetEntity, ItemUnequipReason itemUnequipReason);

    public abstract boolean onItemEquip(InventoryHolder inventoryHolder, Entity targetEntity, ItemEquipReason itemEquipReason);
    public abstract boolean onItemUnequip(InventoryHolder inventoryHolder, Entity targetEntity, ItemUnequipReason itemUnequipReason);
}
