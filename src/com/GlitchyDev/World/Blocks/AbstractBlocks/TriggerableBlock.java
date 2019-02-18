package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;

public interface TriggerableBlock {

    /**
     * Triggered when an EntityBase enters a TriggerableBlock, is not final
     * Should only include logic that can determine if it can enter or not
     * @param entityMovementType
     * @param entityBase
     * @return Success of movement
     */
    boolean attemptEnterBlock(EntityMovementType entityMovementType, EntityBase entityBase);

    /**
     * Triggered when an EntityBase exits a TriggerableBlock, is not final
     * Should only include logic that can determine if it can exit or not
     * @param entityMovementType
     * @param entityBase
     * @return Success of movement
     */
    boolean attemptExitBlock(EntityMovementType entityMovementType, EntityBase entityBase);




    /**
     * Triggered when an EntityBase enters a TriggerableBlock successfully
     * Include logic of successful entering
     * @param entityMovementType
     * @param entityBase
     * @return Success of movement
     */
    void enterBlockSccessfully(EntityMovementType entityMovementType, EntityBase entityBase);

    /**
     * Triggered when an EntityBase exits a TriggerableBlock successfully
     * Include logic of successful exit
     * @param entityMovementType
     * @param entityBase
     * @return Success of movement
     */
    void exitBlockSuccessfully(EntityMovementType entityMovementType, EntityBase entityBase);
}
