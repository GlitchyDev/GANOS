package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Rendering.Assets.Mesh.PartialCubicInstanceMesh;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.TickableEffect;
import com.GlitchyDev.World.Entities.AbstractEntities.CustomRenderEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.General.CustomTransparentRenderable;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.World;

import java.util.*;

public abstract class WorldGameState extends EnvironmentGameState {
    private final HashMap<UUID, World> currentWorlds;

    public WorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        this.currentWorlds = new HashMap<>();
    }


    public void renderEnvironment(Camera camera, Collection<Region> regions, PartialCubicInstanceMesh partialCubicInstanceMesh) {
        HashMap<InstancedGridTexture,ArrayList<DesignerBlock>> designerBlocks = new HashMap();
        ArrayList<CustomTransparentRenderable> transparentRenderables = new ArrayList<>();

        for(Region region: regions) {
            for(Block block: region.getBlocksArray()) {
                if(block instanceof CustomRenderBlock) {
                    ((CustomRenderBlock) block).renderCustomBlock(renderer,camera);
                }
                if(block instanceof DesignerBlock) {

                    if(!designerBlocks.containsKey(((DesignerBlock) block).getInstancedGridTexture())) {
                        designerBlocks.put(((DesignerBlock) block).getInstancedGridTexture(),new ArrayList<>());
                    }
                    designerBlocks.get(((DesignerBlock) block).getInstancedGridTexture()).add((DesignerBlock) block);
                }
                if(block instanceof CustomTransparentRenderable) {
                    transparentRenderables.add((CustomTransparentRenderable) block);
                }
            }

            for(Entity entity: region.getEntities()) {
                if(entity instanceof CustomRenderEntity) {
                    ((CustomRenderEntity) entity).renderCustomEntity(renderer, camera);
                }
                if(entity instanceof CustomTransparentRenderable) {
                    transparentRenderables.add((CustomTransparentRenderable) entity);
                }
            }
        }

        for(InstancedGridTexture instancedGridTexture: designerBlocks.keySet()) {
            partialCubicInstanceMesh.setInstancedGridTexture(instancedGridTexture);
            renderer.renderDesignerBlocks(camera,designerBlocks.get(instancedGridTexture), partialCubicInstanceMesh, "Instance3D");
        }
        transparentRenderables.sort((o1, o2) -> (int) (o1.getDistance(camera.getPosition()) - o2.getDistance(camera.getPosition())));
        Collections.reverse(transparentRenderables);
        renderer.enableTransparency();
        for(CustomTransparentRenderable customTransparentRenderable : transparentRenderables) {
            customTransparentRenderable.renderTransparency(renderer,camera);
        }
        renderer.disableTransparency();
    }


    public boolean hasWorld(UUID worldUUID) {
        return currentWorlds.containsKey(worldUUID);
    }

    public World getWorld(UUID worldUUID) {
        return currentWorlds.get(worldUUID);
    }

    public Collection<UUID> getWorlds() {
        return currentWorlds.keySet();
    }

    public Region getRegion(UUID regionUUID, UUID worldUUID) {
        return getWorld(worldUUID).getRegions().get(regionUUID);
    }


    public boolean isARegionAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public int countRegionsAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).countRegionsAtLocation(location);
    }

    public ArrayList<Region> getRegionsAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).getRegionsAtLocation(location);
    }

    public Region getRegionAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).getRegionAtLocation(location);
    }

    public boolean isEntityAtLocation(Location location) {
        for (Region region : getRegionsAtLocation(location)) {
            for (Entity entity : region.getEntities()) {
                if (entity.getLocation().equals(location)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Entity getEntityAtLocation(Location location) {
        for (Region region : getRegionsAtLocation(location)) {
            for (Entity entity : region.getEntities()) {
                if (entity.getLocation().equals(location)) {
                    return entity;
                }
            }
        }
        return null;
    }


    public ArrayList<Entity> getEntitiesAtLocation(Location location) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Region region : getRegionsAtLocation(location)) {
            for (Entity entity : region.getEntities()) {
                if (entity.getLocation().equals(location)) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public boolean isBlockAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public boolean isLocationInSameRegion(Location location, UUID hostRegion) {
        ArrayList<Region> regions = getRegionsAtLocation(location);
        for(Region region: regions) {
            if(region.getRegionUUID().equals(hostRegion)) {
                return true;
            }
        }
        return false;
    }

    public Block getBlockAtLocation(Location location) {
        return getBlockAtLocation(location, getRegionAtLocation(location).getRegionUUID());
    }

    public Block getBlockAtLocation(Location location, UUID regionUUID) {
        Location offset = getRegion(regionUUID, location.getWorldUUID()).getLocation().getLocationDifference(location);
        return getRegion(regionUUID, location.getWorldUUID()).getBlockRelative(offset);
    }

    public HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> getRegionConnections(UUID worldUUID) {
        return getWorld(worldUUID).getRegionConnections();
    }

    public void linkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnection connectionType) {
        getWorld(worldUUID).linkRegion(hostRegion, connectedRegion, connectionType);
    }

    public void unlinkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnection connectionType) {
        getWorld(worldUUID).unlinkRegion(hostRegion, connectedRegion, connectionType);
    }


    public void addWorld(World world) {
        System.out.println("Added World " + world);
        currentWorlds.put(world.getWorldUUID(), world);
    }

    public void removeWorld(UUID worldUUID) {
        currentWorlds.remove(worldUUID);
    }

    // Replicate functions


    public void setBlock(Block block) {
        UUID regionUUID = getRegionAtLocation(block.getLocation()).getRegionUUID();
        setBlock(block, regionUUID);
    }

    public void setBlock(Block newBlock, UUID regionUUID) {
        UUID worldUUID = newBlock.getLocation().getWorldUUID();
        Region region = getRegion(regionUUID, worldUUID);
        Location difference = region.getLocation().getLocationDifference(newBlock.getLocation());


        newBlock.setRegionUUID(regionUUID);
        region.setBlockRelative(difference, newBlock);

    }

    public void addRegionToGame(Region region) {
        World world = getWorld(region.getWorldUUID());
        world.getRegions().put(region.getRegionUUID(), region);

        for (Entity entity : region.getEntities()) {
            world.addEntity(entity);
            entity.onSpawn(SpawnReason.REGION_MANUALLY_ADDED);
        }
    }

    public void removeRegionFromGame(UUID regionUUID, UUID worldUUID) {
        getWorld(worldUUID).getRegions().remove(regionUUID);
    }

    // Replicate Functions


    public void spawnEntity(Entity entity, SpawnReason spawnReason) {
        entity.onSpawn(spawnReason);
        getRegion(entity.getCurrentRegionUUID(), entity.getWorldUUID()).getEntities().add(entity);
        World world = getWorld(entity.getLocation().getWorldUUID());
        world.addEntity(entity);
    }


    public void despawnEntity(Entity entity, DespawnReason despawnReason) {
        despawnEntity(entity.getUUID(), entity.getWorldUUID(), despawnReason);
    }

    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        Entity entity = getWorld(worldUUID).getEntity(entityUUID);
        Region hostRegion = getRegion(entity.getCurrentRegionUUID(), worldUUID);
        hostRegion.getEntities().remove(entity);
        World world = getWorld(worldUUID);
        world.removeEntity(entity);
        if(entity instanceof TickableEntity) {
            world.getTickableEntities().remove(entity);
        }
        for(Effect effect: entity.getCurrentEffects()) {
            if(effect instanceof TickableEffect) {
                world.getTickableEffects().remove(effect);
            }
        }
        entity.onDespawn(despawnReason);
    }

    public Entity getEntity(UUID entityUUID, UUID worldUUID) {
        return getWorld(worldUUID).getEntity(entityUUID);
    }


}



