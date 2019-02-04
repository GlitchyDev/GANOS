package com.GlitchyDev.Old.World.Entities;

import com.GlitchyDev.Old.Rendering.Renderer;
import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;
import com.GlitchyDev.Old.World.Blocks.Abstract.TriggerableBlock;
import com.GlitchyDev.Old.World.Direction;
import com.GlitchyDev.Old.World.Location;

import java.util.UUID;

public abstract class EntityBase {
    private final UUID uuid;
    private final EntityType entityType;
    private Location location;
    private Direction direction;
    private int entityHeight = 1;
    private int entityWidth = 1;
    private int entityLength = 1;





    public EntityBase(EntityType entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
        this.direction = Direction.NORTH;
        this.uuid = UUID.randomUUID();
    }

    public EntityBase(EntityType entityType, Location location, Direction direction) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        this.uuid = UUID.randomUUID();
    }


    public EntityBase(EntityType entityType, Location location, UUID uuid) {
        this.entityType = entityType;
        this.location = location;
        this.direction = Direction.NORTH;
        this.uuid = uuid;
    }

    public EntityBase(EntityType entityType, Location location, Direction direction, UUID uuid) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        this.uuid = uuid;
    }
    //
    public EntityBase(EntityType entityType, Location location, int entityHeight, int entityWidth, int entityLength) {
        this.entityType = entityType;
        this.location = location;
        this.direction = Direction.NORTH;
        this.uuid = UUID.randomUUID();
        this.entityHeight = entityHeight;
        this.entityWidth = entityWidth;
        this.entityLength = entityLength;
    }

    public EntityBase(EntityType entityType, Location location, Direction direction, int entityHeight, int entityWidth, int entityLength) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        this.uuid = UUID.randomUUID();
        this.entityHeight = entityHeight;
        this.entityWidth = entityWidth;
        this.entityLength = entityLength;
    }


    public EntityBase(EntityType entityType, Location location, UUID uuid, int entityHeight, int entityWidth, int entityLength) {
        this.entityType = entityType;
        this.location = location;
        this.direction = Direction.NORTH;
        this.uuid = uuid;
        this.entityHeight = entityHeight;
        this.entityWidth = entityWidth;
        this.entityLength = entityLength;
    }

    public EntityBase(EntityType entityType, Location location, Direction direction, UUID uuid, int entityHeight, int entityWidth, int entityLength) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        this.uuid = uuid;
        this.entityHeight = entityHeight;
        this.entityWidth = entityWidth;
        this.entityLength = entityLength;
    }



    /**
     * Implement Rendering Logic
     */
    public abstract void render(Renderer renderer);

    public abstract void tick();

    // Add Type of movement



    /**
     * Attempts to move Entity to Location, first by attempting to exit and enter new blocks, then calling a successful call
     *
     * @param movementType
     * @param newLocation
     * @param moveDirection
     * @return Success
     */
    public boolean move(MovementType movementType, Location newLocation, Direction moveDirection) {
        // Exit current Blocks
        switch(direction) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).exitBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).exitBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).exitBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).exitBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).exitBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }

        }


        // Enter New Blocks
        switch(moveDirection) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).enterBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).enterBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).enterBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).enterBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                if(!((TriggerableBlock) blockAtDestination).enterBlock(movementType,this)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
        }



        // If it passes here, the movement was successful, now will call final movement calls


        switch(direction) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }

        }




        // Enter New Blocks
        switch(moveDirection) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
        }

        location = new Location(newLocation);

        return true;
    }


    /**
     * A way to forcefully Move the Character, ignoring any Block denials/physics
     * @param movementType
     * @param newLocation
     * @param newDirection
     */
    public void forceMove(MovementType movementType, Location newLocation, Direction newDirection) {
        switch(direction) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).exitBlockSuccessfully(movementType,this);
                            }
                        }
                    }
                }

        }




        // Enter New Blocks
        switch(newDirection) {
            case NORTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z < entityWidth; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x > -entityLength; x--) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityLength; x++) {
                        for(int z = 0; z > -entityWidth; z--) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < entityHeight; y++) {
                    for(int x = 0; x < entityWidth; x++) {
                        for(int z = 0; z < entityLength; z++) {
                            BlockBase blockAtDestination = newLocation.getWorld().getBlock(newLocation.getOffsetLocation(x,y,z));
                            if(blockAtDestination instanceof TriggerableBlock) {
                                ((TriggerableBlock) blockAtDestination).enterBlockSccessfully(movementType,this);
                            }
                        }
                    }
                }
        }

    }



    public UUID getUuid() {
        return uuid;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Direction getDirection() {
        return direction;
    }

    public Location getLocation() {
        return location.clone();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
