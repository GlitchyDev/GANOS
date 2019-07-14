package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Blocks.Enums.BlockInteractionResponce;
import com.GlitchyDev.World.Blocks.Enums.BlockInteractionType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.EntityInteractionType;

public interface InteractableBlock {

    BlockInteractionResponce interact(Block block, BlockInteractionType blockInteractionType);

    BlockInteractionResponce interact(Entity entity, EntityInteractionType entityInteractionType);
}
