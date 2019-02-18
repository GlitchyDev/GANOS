package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionResponce;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableEntity {

    BlockInteractionResponce interact(BlockBase blockbase, BlockInteractionType blockInteractionType);

    BlockInteractionResponce interact(EntityBase entity, EntityInteractionType entityInteractionType);

}
