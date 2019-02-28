package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionResponce;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableEntity {

    EntityInteractionResponce interact(EntityBase entity, EntityInteractionType entityInteractionType);

    EntityInteractionResponce interact(BlockBase blockbase, BlockInteractionType blockInteractionType);

}
