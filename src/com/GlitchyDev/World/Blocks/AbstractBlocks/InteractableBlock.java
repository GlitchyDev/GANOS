package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Blocks.Enums.BlockInteractionResponce;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableBlock {

    BlockInteractionResponce interact(BlockBase blockbase, BlockInteractionType blockInteractionType);

    BlockInteractionResponce interact(EntityBase entity, EntityInteractionType entityInteractionType);
}
