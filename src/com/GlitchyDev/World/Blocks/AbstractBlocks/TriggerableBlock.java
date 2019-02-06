package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.AbstractEntities.MovementType;

public interface TriggerableBlock {

    /**
     * Triggered when an EntityBase enters a TriggerableBlock, is not final
     * Should only include logic that can determine if it can enter or not
     * @param movementType
     * @param entityBase
     * @return Success of movement
     */
    boolean enterBlock(MovementType movementType, EntityBase entityBase);

    /**
     * Triggered when an EntityBase exits a TriggerableBlock, is not final
     * Should only include logic that can determine if it can exit or not
     * @param movementType
     * @param entityBase
     * @return Success of movement
     */
    boolean exitBlock(MovementType movementType, EntityBase entityBase);




    /**
     * Triggered when an EntityBase enters a TriggerableBlock successfully
     * Include logic of successful entering
     * @param movementType
     * @param entityBase
     * @return Success of movement
     */
    void enterBlockSccessfully(MovementType movementType, EntityBase entityBase);

    /**
     * Triggered when an EntityBase exits a TriggerableBlock successfully
     * Include logic of successful exit
     * @param movementType
     * @param entityBase
     * @return Success of movement
     */
    void exitBlockSuccessfully(MovementType movementType, EntityBase entityBase);
}
