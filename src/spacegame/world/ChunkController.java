package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.block.ITickable;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.entity.*;
import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.item.crafting.InWorld3DCraftingItem;
import spacegame.item.crafting.InWorldCraftingItem;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.util.MathUtil;
import spacegame.gui.GuiWorldLoading;
import spacegame.item.Inventory;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderWorldScene;
import spacegame.render.ThreadRebuildChunk;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class ChunkController {
    public World parentWorld;
    private int lastQueuedX;
    private int lastQueuedY;
    private int lastQueuedZ;
    private Chunk lastQueuedChunk;
    public Chunk[] sortedChunks;
    private int lastQueuedLightMapX = 10;
    private int lastQueuedLightMapZ = 10;
    private ChunkColumnSkylightMap lastQueuedLightMap;
    public final ConcurrentHashMap<Long, ChunkRegion> regionMap = new ConcurrentHashMap<>();
    public ArrayList<Chunk> dirtyChunks = new ArrayList<>();
    public ArrayList<Chunk> nonPopulatedChunks = new ArrayList<>();
    public ArrayList<Chunk> bindingChunks = new ArrayList<>();
    public ArrayList<Chunk> removeChunks = new ArrayList<>();
    public ChunkColumnSkylightMap[] columnLightMaps = new ChunkColumnSkylightMap[1024];
    public int playerChunkX;
    public int playerChunkY;
    public int playerChunkZ;
    private int prevPlayerChunkX = 0;
    private int prevPlayerChunkY = 0;
    private int prevPlayerChunkZ = 0;
    private boolean loadChunks;
    private int generateChunksDistance = 0;
    private int generateChunksX = 0;
    private int generateChunksZ = 0;
    private byte sideOfLoop = 0;
    private boolean loadedInitialColumn;
    public int prevNumberOfLoadedChunks;
    public int numberOfLoadedChunks;
    public int numberOfChunksToPopulate;
    public int prevNumberOfChunksToPopulate;
    public int drawCalls;
    public int timer = 2400;
    public RenderWorldScene renderWorldScene = new RenderWorldScene(this);
    public ChunkTerrainHandler chunkTerrainHandler;
    public int entityCap;
    public int numLoadedEntities;

    public ChunkController(World parentWorld) {
        this.parentWorld = parentWorld;
        this.chunkTerrainHandler = new ChunkTerrainHandler(this, this.parentWorld);
    }


    public void update() {
        this.playerChunkX = MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.x) >> 5;
        this.playerChunkY = MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.y) >> 5;
        this.playerChunkZ = MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.z) >> 5;
        if (!this.loadedInitialColumn) {

            if(!CosmicEvolution.instance.save.thePlayer.loadedFromFile) {
                int count = 0;
                WorldEarth earth = (WorldEarth) this.parentWorld;
                while (earth.globalElevationMap.elevation[earth.convertBlockZToGlobalMap(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z))][earth.convertBlockXToGlobalMap(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x))] < 0
                || this.isDesert(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x), 0, MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z))) {
                        CosmicEvolution.instance.save.thePlayer.z = 489.1328125 * count * 10;

                        if(CosmicEvolution.instance.save.thePlayer.z >= (double) this.parentWorld.size / 4){
                            count = 0;
                            CosmicEvolution.instance.save.thePlayer.x -= 25000;
                            continue;
                        }
                        count++;
                }

                CosmicEvolution.instance.save.spawnX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
                CosmicEvolution.instance.save.thePlayer.spawnX = CosmicEvolution.instance.save.spawnX;

                CosmicEvolution.instance.save.spawnZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);
                CosmicEvolution.instance.save.thePlayer.spawnZ = CosmicEvolution.instance.save.spawnZ;

                //After putting the player in the right Z position we need to adjust the time forward

                long playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);
                if(CosmicEvolution.instance.save.thePlayer.z < 0.0){
                    playerZ += this.parentWorld.size; //half the world size
                }

                double ratio = (double) playerZ / this.parentWorld.size;


                CosmicEvolution.instance.save.time = (long) (CosmicEvolution.instance.everything.getObjectAssociatedWithWorld(this.parentWorld).rotationPeriod *  ratio);

                this.playerChunkX = MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.x) >> 5;
                this.playerChunkZ = MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.z) >> 5;
            }

            this.loadChunkColumn(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5, MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5);

            if(!CosmicEvolution.instance.save.thePlayer.loadedFromFile) {
                while (Block.list[this.parentWorld.getBlockID(MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.x), MathUtil.floorDouble(parentWorld.ce.save.thePlayer.y), MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.z))].isSolid) {
                    CosmicEvolution.instance.save.thePlayer.y++;
                }
                CosmicEvolution.instance.save.thePlayer.y += 3;
                if(CosmicEvolution.instance.save.spawnY == Integer.MIN_VALUE){
                    CosmicEvolution.instance.save.spawnY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
                    CosmicEvolution.instance.save.thePlayer.spawnY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
                    CosmicEvolution.instance.save.saveDataToFileWithoutChunkUnload();
                }
            }
            this.loadedInitialColumn = true;
        }
        this.loadOrUnloadChunks();
        this.numberOfLoadedChunks = this.numberOfLoadedChunks();
        this.populateChunks();
        this.rebuildDirtyChunks();

        this.checkForMissedChunksAndCheckForUpdateTime();

        synchronized (this.bindingChunks) {
            if (this.bindingChunks.size() > 0) {
                this.bindingChunks.get(0).bindRenderData();
                this.bindingChunks.remove(0);
                this.bindingChunks.trimToSize();
            }
        }
    }

    public void tick(){
        if(!this.parentWorld.paused) {
            this.renderWorldScene.chunksThatContainEntities.clear();
            this.entityCap = this.numberOfLoadedChunks / 100;
            this.numLoadedEntities = 0;

            Chunk chunk;
            int xOffset;
            int yOffset;
            int zOffset;
            List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
            for (ChunkRegion region : snapshot) {
                if (region != null) {
                    for (int k = 0; k < region.chunks.length; k++) {
                        chunk = region.chunks[k];
                        if (chunk != null) {
                            if (chunk.doesChunkContainEntities()) {
                                chunk.checkIfEntitiesAreStillInChunk();
                                chunk.tickEntities();
                                xOffset = (chunk.x - this.playerChunkX) << 5;
                                yOffset = (chunk.y - this.playerChunkY) << 5;
                                zOffset = (chunk.z - this.playerChunkZ) << 5;
                                if(CosmicEvolution.camera.doesBoundingBoxIntersectFrustum(xOffset, yOffset, zOffset, ((xOffset + 31)), ((yOffset + 31)), ((zOffset + 31))) && chunk.doesChunkContainEntities()){
                                    this.renderWorldScene.chunksThatContainEntities.add(chunk);
                                }
                            }
                            chunk.tick();
                            this.numLoadedEntities += chunk.getLivingEntitiesInChunk();
                        }
                    }
                }
            }
            //Set target to spawn entities
            if (this.numLoadedEntities < this.entityCap) {
                if(CosmicEvolution.instance.save.time % 2400 == 0) { //Roughly every 40 seconds
                    int x = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x + (CosmicEvolution.globalRand.nextBoolean() ? CosmicEvolution.globalRand.nextInt(32, 64) : CosmicEvolution.globalRand.nextInt(-64, -32)));
                    int z = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z + (CosmicEvolution.globalRand.nextBoolean() ? CosmicEvolution.globalRand.nextInt(32, 64) : CosmicEvolution.globalRand.nextInt(-64, -32)));

                    int spawnX;
                    int spawnY;
                    int spawnZ;
                    int numberDeer = CosmicEvolution.globalRand.nextInt(2, 5);
                    for (int i = 0; i <= numberDeer; i++) {
                        spawnX = x + (CosmicEvolution.globalRand.nextBoolean() ? CosmicEvolution.globalRand.nextInt(5, 10) : CosmicEvolution.globalRand.nextInt(-10, -5));
                        spawnZ = z + (CosmicEvolution.globalRand.nextBoolean() ? CosmicEvolution.globalRand.nextInt(5, 10) : CosmicEvolution.globalRand.nextInt(-10, -5));
                        spawnY = this.findChunkSkyLightMap(spawnX >> 5, spawnZ >> 5).getHeightValue(spawnX, spawnZ);
                        if (this.parentWorld.getBlockID(spawnX, spawnY, spawnZ) == Block.grass.ID && this.parentWorld.getBlockID(spawnX, spawnY + 1, spawnZ) == Block.air.ID) {
                            Chunk chunk1 = this.findChunkFromChunkCoordinates(spawnX >> 5, spawnY >> 5, spawnZ >> 5);
                            if (chunk1 != null && this.renderWorldScene.sunYVector > 0) {
                                chunk1.addEntityToList(new EntityDeer(spawnX + 0.5, spawnY + 1.5, spawnZ + 0.5, false, true));
                            } else if(chunk1 != null){
                                chunk1.addEntityToList(new EntityWolf(spawnX + 0.5, spawnY + 1.5, spawnZ + 0.5, false, true));
                            }
                        }
                    }

                }
            }
        }


        synchronized (this.removeChunks){
            Chunk chunk;
            for(int i = 0; i < this.removeChunks.size(); i++){
                chunk = this.removeChunks.get(i);
                chunk.deleteGLObjects();
                this.getChunkRegionFromChunkCoordinates(chunk.x, chunk.y, chunk.z).removeChunk(chunk);
            }
            this.removeChunks.clear();
        }

    }

    public void renderWorld(){
        if(this.renderWorldScene.recalculateQueries) {
            this.renderWorldScene.renderWithChunks = true;
            Chunk[] renderableChunks = new Chunk[this.regionMap.size() * 512];


            Chunk chunk;
            int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
            int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
            int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
            int xOffset;
            int yOffset;
            int zOffset;
            int index = 0;

            List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
            for (ChunkRegion region : snapshot) {
                if (region != null) {
                    for (int k = 0; k < region.chunks.length; k++) {
                        chunk = region.chunks[k];
                        if (chunk != null) {
                            if (chunk.shouldRender && !chunk.empty) {
                                xOffset = (chunk.x - playerChunkX) << 5;
                                yOffset = (chunk.y - playerChunkY) << 5;
                                zOffset = (chunk.z - playerChunkZ) << 5;
                                if (CosmicEvolution.camera.doesBoundingBoxIntersectFrustum(xOffset - 32, yOffset - 32, zOffset - 32, ((xOffset + 63)), ((yOffset + 63)), ((zOffset + 63))) &&
                                        !chunk.empty && chunk.shouldRender && !chunk.chunkWillUnload) {
                                    xOffset >>= 5;
                                    yOffset >>= 5;
                                    zOffset >>= 5;
                                    chunk.distanceFromPlayer = (float) ((xOffset * xOffset) + (yOffset * yOffset) + (zOffset * zOffset));
                                    renderableChunks[index] = chunk;
                                    index++;
                                }
                            }
                        }
                    }
                }
            }

            Chunk[] sortedChunks = new Chunk[index];
            for (int i = 0; i < index; i++) {
                sortedChunks[i] = renderableChunks[i];
                if (this.renderWorldScene.recalculateQueries) {
                    sortedChunks[i].occluded = false;
                }
            }

            this.renderWorldScene.recalculateQueries = false;

            Arrays.sort(sortedChunks);

            this.sortedChunks = sortedChunks;
        }

        this.renderWorldScene.renderWorldWithChunks(this.sortedChunks);
    }

    public void populateChunks() {
        synchronized (this.nonPopulatedChunks) {
            Chunk chunk;
            this.numberOfChunksToPopulate = this.nonPopulatedChunks.size();
            int i = 0;
            for (i = 0; i < this.nonPopulatedChunks.size(); i++) {
                chunk = this.nonPopulatedChunks.get(i);
                if (chunk != null) {
                    if (this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y, chunk.z) && !chunk.populated) {
                        this.chunkTerrainHandler.populateChunk(chunk);
                        this.nonPopulatedChunks.remove(chunk);
                    }
                }
            }

            if (this.numberOfChunksToPopulate == this.prevNumberOfChunksToPopulate) {
                WorldGen.markAllChunksInRebuildQueueDirty();
                this.nonPopulatedChunks.trimToSize();
            }

            this.prevNumberOfChunksToPopulate = this.numberOfChunksToPopulate;
        }
    }

    public void checkForMissedChunksAndCheckForUpdateTime(){
        Chunk chunk;
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for(ChunkRegion region : snapshot){
            if(region != null){
                for(int j = 0; j < region.chunks.length; j++){
                    chunk = region.chunks[j];
                    if(chunk != null){


                        if(!chunk.shouldRender && !chunk.empty && this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y ,chunk.z) && chunk.populated){

                            if(chunk.blocks != null) { //Since all chunks will now be populated this means that fully surrounded chunks that wont ever render will repeatedly tick to notify all blocks, this causes substantial lag
                                chunk.markDirty();
                            }
                        }

                        if(chunk.updateTime <= CosmicEvolution.instance.save.time && chunk.updateTime != 0){
                            chunk.markDirty();
                            chunk.updateTime = 0;
                        }
                    }
                }
            }
        }
    }

    public byte[] setSkyLightArray(byte[] skyLight, Chunk chunk){
        for(int i = 0; i < skyLight.length; i++){
            skyLight[i] = (byte) (this.parentWorld.doesBlockHaveSkyAccess(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i), chunk.getBlockZFromIndex(i)) ? 15 : 0);
        }
        return skyLight;
    }


    private void rebuildDirtyChunks() {

        if(this.dirtyChunks.size() == 0)return;

        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;

        int maxNewRebuildsThisTick = 10;
        int rebuildsStarted = 0;

        synchronized (this.dirtyChunks) {
            Chunk chunk;
            boolean surrounded = false;
            for (int i = 0; i < this.dirtyChunks.size(); i++) {
                chunk = this.dirtyChunks.get(i);
                if (chunk == null)continue;

                if(chunk.updating)continue;
                if(chunk.empty)continue;

                surrounded = this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y, chunk.z);

                if (surrounded && !chunk.populated) {
                    chunk.populated = true;
                }

                if (chunk.populated && surrounded) {
                    chunk.elementOffsetOpaque = 0;
                    chunk.elementOffsetTransparent = 0;
                    chunk.needsToUpdate = false;
                    chunk.updating = true;
                    CosmicEvolution.threadJobs.incrementAndGet();
                    ChunkJobThreadScheduler.chunkJobQueue.add(new ChunkJob((float) MathUtil.distance3DSquared(playerChunkX << 5, playerChunkY << 5, playerChunkZ << 5, chunk.x << 5, chunk.y << 5, chunk.z << 5), new ThreadRebuildChunk(chunk, this.parentWorld)));
                    rebuildsStarted++;
                    if(rebuildsStarted >=  maxNewRebuildsThisTick)break;
                } else {
                    chunk.needsToUpdate = true;
                }
            }

            int subtract = 0;
            for (int i = 0; i < this.dirtyChunks.size() - subtract; i++) {
                chunk = this.dirtyChunks.get(i);
                if (chunk == null) {
                    continue;
                }
                if (!chunk.needsToUpdate) {
                    this.dirtyChunks.remove(i);
                    i--;
                    subtract++;
                }
            }

            this.dirtyChunks.trimToSize();

            this.numberOfLoadedChunks = this.numberOfLoadedChunks();
            if ((!this.loadChunks && this.numberOfLoadedChunks >= ((GameSettings.renderDistance * 2 + 1) * (GameSettings.renderDistance * 2 + 1) * (GameSettings.chunkColumnHeight * 2))) || (this.numberOfLoadedChunks == this.prevNumberOfLoadedChunks)) {
                this.dirtyChunks.clear();
            }
            this.prevNumberOfLoadedChunks = this.numberOfLoadedChunks;
        }
    }

    public void addChunkToRebuildQueue(Chunk addedChunk) {
        if (addedChunk == null) {return;}
        synchronized (this.dirtyChunks) {
            Chunk chunk;
            boolean canAdd = true;
            for (int i = 0; i < this.dirtyChunks.size(); i++) {
                chunk = this.dirtyChunks.get(i);
                if (chunk == null) {
                    continue;
                }

                if (chunk.equals(addedChunk)) {
                    canAdd = false;
                    break;
                }
            }
            if (canAdd) {
                this.dirtyChunks.add(addedChunk);
            }
        }
    }

    public void addChunkToPopulationQueue(Chunk addedChunk) {
        if (addedChunk == null) {return;}
        synchronized (this.nonPopulatedChunks) {
            Chunk chunk;
            for (int i = 0; i < this.nonPopulatedChunks.size(); i++) {
                chunk = this.nonPopulatedChunks.get(i);
                if (chunk != null) {
                    if (chunk.equals(addedChunk)) {
                        return;
                    }
                }
            }
            this.nonPopulatedChunks.add(addedChunk);
        }
    }


    public void markAllChunksDirty() {
        Chunk chunk;
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for (ChunkRegion region : snapshot) {
            if (region == null) {continue;}

                for (int k = 0; k < region.chunks.length; k++) {
                    chunk = region.chunks[k];
                    if (chunk == null) {continue;}
                    chunk.markDirty();
                }
            }
        }


    public void updateChunksWithLeaves() {
        Chunk chunk;
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for (ChunkRegion region : snapshot) {
            if (region == null) {continue;}

            for (int i = 0; i < region.chunks.length; i++) {
                chunk = region.chunks[i];
                if (chunk == null) {continue;}
                for(int j = 0; j < chunk.blocks.length; j++){
                    if(chunk.blocks[j] == Block.leaf.ID){
                        chunk.notifyBlock(chunk.getBlockXFromIndex(j), chunk.getBlockYFromIndex(j), chunk.getBlockZFromIndex(j));
                    }
                }
                chunk.markDirty();
            }
        }
    }


    //all block coordinates must be right-hand bitshifted by 5 before calling this
    public Chunk findChunkFromChunkCoordinates(int x, int y, int z) {
        if (this.lastQueuedX == x && this.lastQueuedY == y && this.lastQueuedZ == z) {
            return this.lastQueuedChunk;
        }
        Chunk chunk = this.getChunkRegionFromChunkCoordinates(x, y, z).getChunk(x,y,z);

        if(chunk != null) {
            this.lastQueuedX = x;
            this.lastQueuedY = y;
            this.lastQueuedZ = z;
            this.lastQueuedChunk = chunk;
        }


        return chunk;

    }

    public ChunkRegion getChunkRegionFromChunkCoordinates(int x, int y, int z) {
        int rx = x >> 3;
        int ry = y >> 3;
        int rz = z >> 3;

        long key = ChunkRegion.regionKey(rx, ry, rz);

        ChunkRegion region = regionMap.get(key);
        if (region != null) return region;

        region = new ChunkRegion(rx, ry, rz);
        regionMap.put(key, region);
        return region;
    }



    public ChunkColumnSkylightMap findChunkSkyLightMap(int x, int z) {
        if (this.lastQueuedLightMapX == x && this.lastQueuedLightMapZ == z) {
            return this.lastQueuedLightMap;
        }

        for (int i = 0; i < this.columnLightMaps.length; i++) {
            if (this.columnLightMaps[i] != null) {
                if (this.columnLightMaps[i].x == x && this.columnLightMaps[i].z == z) {
                    this.lastQueuedLightMapX = x;
                    this.lastQueuedLightMapZ = z;
                    this.lastQueuedLightMap = this.columnLightMaps[i];
                    return this.columnLightMaps[i];
                }
            }
        }

        ChunkColumnSkylightMap lightMap = new ChunkColumnSkylightMap(x,z);
        this.addColumnLightMap(lightMap);
        return lightMap;
    }



    public void removeChunkFromLists(Chunk chunk) {
        this.nonPopulatedChunks.remove(chunk);
        this.dirtyChunks.remove(chunk);
    }


    public void addChunk(Chunk chunk) {
        this.chunkTerrainHandler.setTerrain(chunk.blocks, chunk);

        if (chunk.containsWater) {
            chunk.setLightValueUnderWater();
        }

        if (chunk.empty) {
            chunk.emptyChunk();
        }


        if(!chunk.empty) {
            chunk.markDirty();
            chunk.markToPopulate();
        }
        ChunkRegion region = this.getChunkRegionFromChunkCoordinates(chunk.x, chunk.y, chunk.z);
        region.addChunk(chunk);
    }

    public void addChunkFromFile(Chunk chunk) {
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(chunk.x, chunk.z);
        if(!chunk.empty) {
            chunk.markDirty();
            if(!chunk.populated){
                chunk.markToPopulate();
            }
            if (chunk.containsWater) {
                chunk.setLightValueUnderWater();
            }
        }

        int x = 0;
        int y = 0;
        int z = 0;
        chunk.tickableBlockIndex = new short[32768];
        int tickableIndex = 0;
        for(int i = 0; i < chunk.blocks.length; i++){
            chunk.lightColor[i] = new Color(this.parentWorld.skyLightColor[0], this.parentWorld.skyLightColor[1], this.parentWorld.skyLightColor[2]).getRGB(); //This is here for efficiency reasons despite not being related to terrain
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            if(Block.list[chunk.blocks[i]].isSolid) {
                if (lightMap.isHeightGreater(x, y, z)) {
                    lightMap.updateLightMap(x, y, z);
                    chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                    chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                    chunk.updateSkylight = true;
                }
            }
            if(chunk.blocks[i] == Block.air.ID){
                chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 15;
            }
            if(chunk.blocks[i] == Block.tallGrass.ID){
                chunk.tallGrassCount++;
            }
            if(Block.list[chunk.blocks[i]] instanceof ITickable){
                chunk.tickableBlockIndex[tickableIndex] = (short) i;
                tickableIndex++;
            }
        }
        boolean empty = tickableIndex == 0;
        chunk.truncateTickableIndexArray(tickableIndex + 1, empty);

        ChunkRegion region = this.getChunkRegionFromChunkCoordinates(chunk.x, chunk.y, chunk.z);
        region.addChunk(chunk);
    }

    public boolean doesChunkExitAtPos(int x, int y, int z) {
        return this.findChunkFromChunkCoordinates(x, y, z) != null;
    }
    public boolean isDesert(int x, int y, int z){
        double rainfall = this.parentWorld.getAverageRainfall(x,z);
        return rainfall < 0.3;
    }


    private boolean shouldChunkUnload(Chunk chunk) {
        int dx = Math.abs(chunk.x - this.playerChunkX);
        int dy = Math.abs(chunk.y - this.playerChunkY);
        int dz = Math.abs(chunk.z - this.playerChunkZ);

        boolean outsideHorizontal = (dx + dz) > GameSettings.renderDistance;
        boolean outsideVertical = dy > GameSettings.chunkColumnHeight;

        return outsideHorizontal || outsideVertical;
    }

    private boolean isChunkColumnFullyLoaded(int x, int z) {
        for (int y = this.playerChunkY - GameSettings.chunkColumnHeight; y < this.playerChunkY + GameSettings.chunkColumnHeight; y++) {
            if (!this.doesChunkExitAtPos(x, y, z)) {
                return false;
            }
        }
        return true;
    }

    private boolean doChunksExistInColumn(int x, int z){
        for (int y = this.playerChunkY - GameSettings.chunkColumnHeight; y < this.playerChunkY + GameSettings.chunkColumnHeight; y++) {
            if (this.doesChunkExitAtPos(x, y, z)) {
                return true;
            }
        }
        return false;
    }

    public void addColumnLightMap(int x, int z) {
        for (int i = 0; i < this.columnLightMaps.length; i++) {
            if (this.columnLightMaps[i] == null) {
                this.columnLightMaps[i] = new ChunkColumnSkylightMap(x, z);
                return;
            }
        }
        throw new RuntimeException("Out of space in the chunk column skylight Array");
    }


    public void addColumnLightMap(ChunkColumnSkylightMap lightMap) {
        for (int i = 0; i < this.columnLightMaps.length; i++) {
            if (this.columnLightMaps[i] == null) {
                this.columnLightMaps[i] = lightMap;
                return;
            }
        }
        throw new RuntimeException("Out of space in the chunk column skylight Array");
    }



    public void loadChunkColumn(int x, int z) {
        this.parentWorld.generateWeatherSystems(x,z);
        this.addColumnLightMap(x, z);
        for (int y = this.playerChunkY - GameSettings.chunkColumnHeight; y < this.playerChunkY + GameSettings.chunkColumnHeight; y++) {
            if (!this.doesChunkExitAtPos(x, y, z)) {
                Chunk chunk = null;
                File file = new File(this.parentWorld.worldFolder + "/Chunk." + x + "." + y + "." + z + ".dat");
                try {
                    if(file.exists()) {
                        FileInputStream inputStream = new FileInputStream(file);
                        NBTTagCompound chunkTag = NBTIO.readCompressed(inputStream);
                        NBTTagCompound chunkData = chunkTag.getCompoundTag("Chunk");
                        NBTTagCompound entity = chunkData.getCompoundTag("Entity");
                        NBTTagCompound chest = chunkData.getCompoundTag("Chest");
                        NBTTagCompound timeEvents = chunkData.getCompoundTag("TimeEvents");
                        NBTTagCompound heatableBlocks = chunkData.getCompoundTag("HeatableBlocks");
                        NBTTagCompound crafting3DItems = chunkData.getCompoundTag("Crafting3DItems");
                        NBTTagCompound craftingItems = chunkData.getCompoundTag("CraftingItems");

                        chunk = new Chunk(x, y, z, CosmicEvolution.instance.save.activeWorld);

                        chunk.containsWater = chunkData.getBoolean("containsWater");
                        chunk.containsAir = chunkData.getBoolean("containsAir");
                        chunk.populated = chunkData.getBoolean("populated");
                        chunk.empty = chunkData.getBoolean("empty");
                        if (!chunk.empty) {
                            chunk.blocks = chunkData.getShortArray("blocks");
                            chunk.decayableLeaves = chunkData.getShortArray("decayableLeaves");
                            chunk.topFaceBitMask = chunkData.getIntArray("topFaceBitMask");
                            chunk.bottomFaceBitMask = chunkData.getIntArray("bottomFaceBitMask");
                            chunk.northFaceBitMask = chunkData.getIntArray("northFaceBitMask");
                            chunk.southFaceBitMask = chunkData.getIntArray("southFaceBitMask");
                            chunk.eastFaceBitMask = chunkData.getIntArray("eastFaceBitMask");
                            chunk.westFaceBitMask = chunkData.getIntArray("westFaceBitMask");
                        }

                        if(entity != null) {
                            int entityCount = entity.getInteger("entityCount");
                            NBTTagCompound entityLoadedTag;
                            Entity entityLoaded;
                            for (int i = 0; i < entityCount; i++) {
                                entityLoadedTag = entity.getCompoundTag("entity" + i);
                                switch (entityLoadedTag.getString("entityType")) {
                                    case "EntityBlock" -> {
                                        entityLoaded = new EntityBlock(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("blockType"), entityLoadedTag.getByte("count"));
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityItem" -> {
                                        entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"), 0);
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityDeer" -> {
                                        entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                                        ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                                        ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                                        ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityWolf" -> {
                                        entityLoaded = new EntityWolf(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                                        ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                                        ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                                        ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                }
                            }
                        }

                        if(chest != null) {
                            int chestCount = chest.getInteger("chestCount");
                            NBTTagCompound chestLoadedTag;
                            for (int i = 0; i < chestCount; i++) {
                                chestLoadedTag = chest.getCompoundTag("chest" + i);
                                NBTTagCompound inventory = chestLoadedTag.getCompoundTag("Inventory");
                                short index = chestLoadedTag.getShort("index");
                                NBTTagCompound item;
                                if(!(Block.list[chunk.blocks[index]] instanceof BlockContainer))continue;
                                Inventory chestInventory = new Inventory(((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryWidth, ((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryHeight);
                                for (int j = 0; j < chestInventory.itemStacks.length; j++) {
                                    item = inventory.getCompoundTag("slot " + j);
                                    if (item != null) {
                                        short id = item.getShort("id");
                                        byte count = item.getByte("count");
                                        short durability = item.getShort("durability");
                                        short metadata = item.getShort("metadata");
                                        long decayTime = item.getLong("decayTime");
                                        chestInventory.loadItemToInventory(id, metadata, count, durability, j, decayTime);
                                    }
                                }
                                chunk.addChestLocation(index, chestInventory);
                            }
                        }

                        if(timeEvents != null) {
                            int eventCount = timeEvents.getInteger("eventCount");
                            NBTTagCompound eventLoadedTag;
                            for (int i = 0; i < eventCount; i++) {
                                eventLoadedTag = timeEvents.getCompoundTag("event" + i);
                                short index = eventLoadedTag.getShort("index");
                                long updateTime = eventLoadedTag.getLong("updateTime");
                                chunk.addTimeUpdateEvent(index, updateTime);
                            }
                        }


                        if(heatableBlocks != null){
                            int heatableBlockCount = heatableBlocks.getInteger("heatableBlockCount");
                            NBTTagCompound heatableBlockLoadedTag;
                            HeatableBlockLocation heatableBlockLocation;
                            for(int i = 0; i < heatableBlockCount; i++){
                                heatableBlockLoadedTag = heatableBlocks.getCompoundTag("heatableBlock" + i);
                                short index = heatableBlockLoadedTag.getShort("index");
                                float currentTemperature = heatableBlockLoadedTag.getFloat("currentTemperature");
                                short currentFuelBurning = heatableBlockLoadedTag.getShort("currentFuelBurning");
                                long fuelBurnoutTime = heatableBlockLoadedTag.getLong("fuelBurnoutTime");
                                long heatingFinishTime = heatableBlockLoadedTag.getLong("heatingFinishTime");
                                long fuelStartTime = heatableBlockLoadedTag.getLong("fuelStartTime");
                                long heatStartTime = heatableBlockLoadedTag.getLong("heatStartTime");
                                boolean heating = heatableBlockLoadedTag.getBoolean("heating");
                                heatableBlockLocation = new HeatableBlockLocation(index);
                                heatableBlockLocation.currentTemperature = currentTemperature;
                                heatableBlockLocation.currentFuelBurning = currentFuelBurning;
                                heatableBlockLocation.fuelBurnoutTime = fuelBurnoutTime;
                                heatableBlockLocation.heatingFinishTime = heatingFinishTime;
                                heatableBlockLocation.heating = heating;
                                heatableBlockLocation.fuelStartTime = fuelStartTime;
                                heatableBlockLocation.heatStartTime = heatStartTime;

                                chunk.addHeatableBlock(heatableBlockLocation);
                            }
                        }

                        if(crafting3DItems != null){
                            int craftingBlockCount = crafting3DItems.getInteger("inWorldCrafting3DItemCount");
                            NBTTagCompound inWorldCrafting3DItemLoadedTag;
                            InWorld3DCraftingItem inWorld3DCraftingItem;
                            for(int i = 0; i < craftingBlockCount; i++){
                                inWorldCrafting3DItemLoadedTag = crafting3DItems.getCompoundTag("inWorldCrafting3DItem" + i);
                                int index = inWorldCrafting3DItemLoadedTag.getInteger("index");
                                short materialBlockID = inWorldCrafting3DItemLoadedTag.getShort("materialBlockID");

                                inWorld3DCraftingItem = new InWorld3DCraftingItem(index, materialBlockID, InWorldCraftingRecipe.findInWorldCraftingRecipeFromName(inWorldCrafting3DItemLoadedTag.getString("craftingRecipeName")), chunk);
                                inWorld3DCraftingItem.activeCraftingLayer = inWorldCrafting3DItemLoadedTag.getInteger("activeCraftingLayer");

                                for(int j = 0; j < 16; j++){
                                    inWorld3DCraftingItem.subVoxelIndices[j] = inWorldCrafting3DItemLoadedTag.getIntArray("craftingLayer" + j);
                                }

                                chunk.crafting3DItems.add(inWorld3DCraftingItem);
                            }
                        }

                        if(craftingItems != null){
                            int craftingItemCount = craftingItems.getInteger("inWorldCraftingItemCount");
                            NBTTagCompound inWorldCraftingItemLoadedTag;
                            InWorldCraftingItem inWorldCraftingItem;
                            for(int i = 0; i < craftingItemCount; i++){
                                inWorldCraftingItemLoadedTag = craftingItems.getCompoundTag("inWorldCraftingItem" + i);

                                int index =  inWorldCraftingItemLoadedTag.getInteger("index");
                                int recipeID = inWorldCraftingItemLoadedTag.getInteger("outputRecipeID");
                                boolean hasBeenBound = inWorldCraftingItemLoadedTag.getBoolean("hasBeenBound");

                                inWorldCraftingItem = new InWorldCraftingItem(CraftingBlockRecipes.list[recipeID], index, chunk);
                                inWorldCraftingItem.hasBeenBound = hasBeenBound;

                                int filledItemIndex = inWorldCraftingItemLoadedTag.getInteger("filledItemIndex");

                                for(int j = 0; j < filledItemIndex; j++){
                                    inWorldCraftingItem.itemsFilled[j] = inWorldCraftingItemLoadedTag.getBoolean("item" + j + "Filled");
                                }


                                chunk.craftingItems.add(inWorldCraftingItem);
                            }
                        }

                        inputStream.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                if(chunk != null){
                    this.addChunkFromFile(chunk);
                } else {
                    this.addChunk(new Chunk(x, y, z, this.parentWorld));
                }
            }
        }
    }

    public void resetChunkLoading(){
        this.loadChunks = true;
        this.unloadChunks();
        this.generateChunksDistance = 1;
        this.generateChunksX = this.playerChunkX - this.generateChunksDistance;
        this.generateChunksZ = this.playerChunkZ;
        this.sideOfLoop = 0;
    }

    public void loadOrUnloadChunks() {
        if (this.playerChunkX != this.prevPlayerChunkX || this.playerChunkY != this.prevPlayerChunkY || this.playerChunkZ != this.prevPlayerChunkZ) {
            this.resetChunkLoading();
        }
        if (this.loadChunks) {
            this.loadChunks();
        }
        this.prevPlayerChunkX = this.playerChunkX;
        this.prevPlayerChunkY = this.playerChunkY;
        this.prevPlayerChunkZ = this.playerChunkZ;
    }

    private void loadChunks() {
        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
        if (this.generateChunksDistance > GameSettings.renderDistance) {
            this.loadChunks = false;
            if(CosmicEvolution.instance.currentGui instanceof GuiWorldLoading){
                World.worldLoadPhase = 3;
            }
        } else {
            if (!this.isChunkColumnFullyLoaded(this.generateChunksX, this.generateChunksZ)) {
                this.findChunkSkyLightMap(this.generateChunksX, this.generateChunksZ);
                CosmicEvolution.threadJobs.incrementAndGet();
                ChunkJobThreadScheduler.chunkJobQueue.add(new ChunkJob((float) MathUtil.distance2DSquared(playerChunkX << 5, playerChunkZ << 5, this.generateChunksX << 5, this.generateChunksZ << 5), new ThreadChunkColumnLoader(this.generateChunksX, this.generateChunksZ, this)));
            }

            switch (this.sideOfLoop) {
                case 0 -> {
                    this.generateChunksX++;
                    this.generateChunksZ--;
                    if (this.generateChunksX == this.playerChunkX) {
                        this.sideOfLoop = 1;
                    }
                }
                case 1 -> {
                    this.generateChunksZ++;
                    this.generateChunksX++;
                    if (this.generateChunksZ == this.playerChunkZ) {
                        this.sideOfLoop = 2;
                    }
                }
                case 2 -> {
                    this.generateChunksX--;
                    this.generateChunksZ++;
                    if (this.generateChunksX == this.playerChunkX) {
                        this.sideOfLoop = 3;
                    }
                }
                case 3 -> {
                    this.generateChunksZ--;
                    this.generateChunksX--;
                    if (this.generateChunksZ == this.playerChunkZ) {
                        this.generateChunksDistance++;
                        this.generateChunksX = this.playerChunkX - this.generateChunksDistance;
                        this.generateChunksZ = this.playerChunkZ;
                        this.sideOfLoop = 0;
                    }
                }
            }


        }
    }

    private void unloadChunks() {
        Chunk chunk;
        Chunk[] unloadingChunks = new Chunk[this.regionMap.size() * 512];
        int unloadingChunksIndex = 0;

        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for (ChunkRegion region : snapshot) {
            if (region != null) {
                for (int k = 0; k < region.chunks.length; k++) {
                    chunk = region.chunks[k];
                    if (chunk != null) {
                        if (this.shouldChunkUnload(chunk)) {
                            chunk.chunkWillUnload = true;
                            this.removeChunkFromLists(chunk);
                            unloadingChunks[unloadingChunksIndex] = chunk;
                            unloadingChunksIndex++;
                        }
                    }
                }
            }
        }
        CosmicEvolution.threadJobs.incrementAndGet();
        ChunkJobThreadScheduler.chunkJobQueue.add(new ChunkJob(10000 ,new ThreadChunkUnloader(unloadingChunks)));



        ChunkColumnSkylightMap lightmap;
        for(int i = 0; i < this.columnLightMaps.length; i++){
            if(this.columnLightMaps[i] != null){
                lightmap = this.columnLightMaps[i];
                if(!this.doChunksExistInColumn(lightmap.x, lightmap.z)){
                    this.columnLightMaps[i] = null;
                }
            }
        }


        Iterator<Map.Entry<Long, ChunkRegion>> it = regionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, ChunkRegion> entry = it.next();
            ChunkRegion region = entry.getValue();

            if (region.isEmpty()) {
                it.remove(); // removes from the map safely
            }
        }

    }

    public int numberOfLoadedChunks() {
        int result = 0;

        // Snapshot the regions to avoid CME
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());

        for (ChunkRegion region : snapshot) {
            if (region == null) continue;

            for (Chunk chunk : region.chunks) {
                if (chunk != null) result++;
            }
        }

        return result;
    }


    public int numberOfLoadedRegions(){
        int result = 0;
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for(ChunkRegion region : snapshot){
            if(region != null){
                result++;
            }
        }
        return result;
    }

    public int numberOfLoadedLightMaps(){
        int result = 0;
        ChunkColumnSkylightMap map;
        for(int i = 0; i < this.columnLightMaps.length; i++){
            map = this.columnLightMaps[i];
            if(map != null){
                result++;
            }
        }
        return result;
    }


    public void saveAllRegions() {
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for(ChunkRegion region : snapshot){
            if(region != null){
                this.saveRegion(region);
            }
        }
    }

    public void saveRegion(ChunkRegion region) {
        Chunk chunk;
        Chunk[] unloadingChunks = new Chunk[region.chunks.length];
        int unloadingChunksIndex = 0;
        for (int i = 0; i < region.chunks.length; i++) {
            chunk = region.chunks[i];
            if (chunk != null) {
                this.removeChunkFromLists(chunk);
                unloadingChunks[unloadingChunksIndex] = chunk;
                unloadingChunksIndex++;
            }
        }
        CosmicEvolution.threadJobs.incrementAndGet();
        ChunkJobThreadScheduler.chunkJobQueue.add(new ChunkJob(10000, new ThreadChunkUnloader(unloadingChunks)));
    }

    public void saveAllRegionsWithoutUnload() {
        List<ChunkRegion> snapshot = new ArrayList<>(regionMap.values());
        for(ChunkRegion region : snapshot){

            if(region != null){
                this.saveRegionWithoutUnload(region);
            }
        }
    }

    public void saveRegionWithoutUnload(ChunkRegion region) {
        Chunk chunk;
        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
        for (int i = 0; i < region.chunks.length; i++) {
            chunk = region.chunks[i];
            if (chunk != null) {
                if (chunk.modifiedSinceLastSave) {
                    CosmicEvolution.threadJobs.incrementAndGet();
                    ChunkJobThreadScheduler.chunkJobQueue.add(new ChunkJob((float) MathUtil.distance3DSquared(playerChunkX << 5, playerChunkY << 5, playerChunkZ << 5, chunk.x << 5, chunk.y << 5, chunk.z << 5), new ThreadChunkSave(chunk)));
                    chunk.modifiedSinceLastSave = false;
                }
            }
        }
    }



}
