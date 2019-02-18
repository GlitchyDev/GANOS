package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionResponce;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableBlock {

    EntityInteractionResponce interact(BlockBase blockbase, BlockInteractionType blockInteractionType);

    EntityInteractionResponce interact(EntityBase entity, EntityInteractionType entityInteractionType);
}
