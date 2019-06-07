package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionResponce;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableEntity {

    EntityInteractionResponce interact(Entity entity, EntityInteractionType entityInteractionType);

    EntityInteractionResponce interact(Block blockbase, BlockInteractionType blockInteractionType);

}
