package com.GlitchyDev.Inventory.Enums;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Inventory.Item;
import com.GlitchyDev.Utility.InputBitUtility;

import java.io.IOException;

public enum ItemType {
    DEBUG_ITEM,
    DEBUG_ITEM_2,
    DEBUG_EQUIPMENT,

    ;

    public Item getItemFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        switch (this) {

            default:
                System.out.println("ERROR: Item not registered in itemType");
                return null;
        }

    }
}
