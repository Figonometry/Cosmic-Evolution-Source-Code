package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.block.ITickable;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.core.MathUtil;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityDeer;
import spacegame.entity.EntityItem;
import spacegame.gui.GuiWorldLoadingScreen;
import spacegame.item.Inventory;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderWorldScene;
import spacegame.render.ThreadRebuildChunk;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    public ChunkRegion[] regions = new ChunkRegion[128];
    public ArrayList<Chunk> dirtyChunks = new ArrayList<>();
    public ArrayList<Chunk> nonPopulatedChunks = new ArrayList<>();
    public ArrayList<Chunk> bindingChunks = new ArrayList<>();
    public ArrayList<Chunk> removeChunks = new ArrayList<>();
    public ArrayList<Thread> threadQueue = new ArrayList<>();
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
            this.loadChunkColumn(this.playerChunkX, this.playerChunkZ);
            if(!this.parentWorld.ce.save.thePlayer.loadedFromFile) {
                while (Block.list[this.parentWorld.getBlockID(MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.x), MathUtil.floorDouble(parentWorld.ce.save.thePlayer.y), MathUtil.floorDouble(this.parentWorld.ce.save.thePlayer.z))].isSolid) {
                    this.parentWorld.ce.save.thePlayer.y++;
                }
                this.parentWorld.ce.save.thePlayer.y += 3;
                if(CosmicEvolution.instance.save.spawnY == Double.NEGATIVE_INFINITY){
                    CosmicEvolution.instance.save.spawnY = CosmicEvolution.instance.save.thePlayer.y;
                    CosmicEvolution.instance.save.saveDataToFileWithoutChunkUnload();
                }
            }
            this.loadedInitialColumn = true;
        }
        this.loadOrUnloadChunks();
        this.numberOfLoadedChunks = this.numberOfLoadedChunks();
        if(this.numberOfLoadedChunks != this.prevNumberOfLoadedChunks) {
            this.populateChunks();
        }
        this.rebuildDirtyChunks();
        if(this.timer <= 0){
            this.checkForMissedChunksAndCheckForUpdateTime();
            this.timer = 2400;
        } else {
            this.timer--;
        }

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
            this.entityCap = this.numberOfLoadedChunks / 500;
            this.numLoadedEntities = 0;
            ChunkRegion region;
            Chunk chunk;
            int xOffset;
            int yOffset;
            int zOffset;
            for (int i = 0; i < this.regions.length; i++) {
                region = this.regions[i];
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
                            this.numLoadedEntities += chunk.entities.size();
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
                            if (chunk1 != null) {
                                chunk1.addEntityToList(new EntityDeer(spawnX + 0.5, spawnY + 1.5, spawnZ + 0.5, false, true));
                            }
                        }
                    }

                }
            }
        }

        while (Thread.activeCount() < 10 && !this.threadQueue.isEmpty()){
            this.threadQueue.get(0).start();
            this.threadQueue.remove(0);
            this.threadQueue.trimToSize();
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
            Chunk[] renderableChunks = new Chunk[this.regions.length * 512];

            ChunkRegion region;
            Chunk chunk;
            int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
            int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
            int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
            int xOffset;
            int yOffset;
            int zOffset;
            int index = 0;
            for (int i = 0; i < this.regions.length; i++) {
                region = this.regions[i];
                if (region != null) {
                    for (int k = 0; k < region.chunks.length; k++) {
                        chunk = region.chunks[k];
                        if (chunk != null) {
                            if (chunk.shouldRender && !chunk.empty) {
                                xOffset = (chunk.x - playerChunkX) << 5;
                                yOffset = (chunk.y - playerChunkY) << 5;
                                zOffset = (chunk.z - playerChunkZ) << 5;
                                if (CosmicEvolution.camera.doesBoundingBoxIntersectFrustum(xOffset, yOffset, zOffset, ((xOffset + 31)), ((yOffset + 31)), ((zOffset + 31))) && !chunk.empty && chunk.shouldRender) {
                                    xOffset >>= 5;
                                    yOffset >>= 5;
                                    zOffset >>= 5;
                                    chunk.distanceFromPlayer = (float) Math.sqrt(xOffset * xOffset + yOffset * yOffset + zOffset * zOffset);
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

        if(this.renderWorldScene.renderWithChunks) {
            this.renderWorldScene.renderWorldWithChunks(this.sortedChunks);
        } else {
            this.renderWorldScene.renderWorldWithoutChunks();
        }
    }

    public void populateChunks() {
        synchronized (this.nonPopulatedChunks) {
            Chunk chunk;
            this.numberOfChunksToPopulate = this.nonPopulatedChunks.size();
            for (int i = 0; i < this.nonPopulatedChunks.size(); i++) {
                chunk = this.nonPopulatedChunks.get(i);
                if (chunk != null) {
                    if (this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y, chunk.z) && !chunk.populated) {
                        this.chunkTerrainHandler.populateChunk(chunk);
                        this.nonPopulatedChunks.remove(chunk);
                    }
                }
            }

            if (this.numberOfChunksToPopulate == this.prevNumberOfChunksToPopulate) {
                this.nonPopulatedChunks.trimToSize();
                WorldGen.markAllChunksInRebuildQueueDirty();
            }

            this.prevNumberOfChunksToPopulate = this.numberOfChunksToPopulate;
        }
    }

    public void checkForMissedChunksAndCheckForUpdateTime(){
        ChunkRegion region;
        Chunk chunk;
        for(int i = 0; i < this.regions.length; i++){
            region = this.regions[i];
            if(region != null){
                for(int j = 0; j < region.chunks.length; j++){
                    chunk = region.chunks[j];
                    if(chunk != null){
                        if(!chunk.shouldRender && !chunk.empty && this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y ,chunk.z) && chunk.populated && (chunk.containsAir || chunk.containsWater)){
                            if(chunk.blocks != null) {
                                chunk.notifyAllBlocks();
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
        synchronized (this.dirtyChunks) {
            Chunk chunk;
            boolean surrounded = false;
            for (int i = 0; i < this.dirtyChunks.size(); i++) {
                chunk = this.dirtyChunks.get(i);
                if (chunk == null) {
                    continue;
                }
                surrounded = this.parentWorld.chunkFullySurrounded(chunk.x, chunk.y, chunk.z);
                if (surrounded && !chunk.populated) {
                    chunk.populated = true;
                }
                if (chunk.populated && !chunk.empty && !chunk.updating && surrounded) {
                    chunk.elementOffsetOpaque = 0;
                    chunk.elementOffsetTransparent = 0;
                    chunk.needsToUpdate = false;
                    chunk.updating = true;
                    Thread thread = new Thread(new ThreadRebuildChunk(chunk, this.parentWorld));
                    thread.setName("Chunk Rebuild Thread for X: " + chunk.x + " Y: " + chunk.y + " Z: " + chunk.z);
                    thread.setPriority(World.worldLoadPhase == 3 ? 1 : 10);
                    this.threadQueue.add(thread);
                } else if (!surrounded) {
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
            boolean canAdd = true;
            for (int i = 0; i < this.nonPopulatedChunks.size(); i++) {
                chunk = this.nonPopulatedChunks.get(i);
                if (chunk != null) {
                    if (chunk.equals(addedChunk)) {
                        canAdd = false;
                        return;
                    }
                }
            }
            this.nonPopulatedChunks.add(addedChunk);
        }
    }


    public void markAllChunksDirty() {
        Chunk chunk;
        ChunkRegion region;
        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
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
        ChunkRegion region;
        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
            if (region == null) {continue;}

            for (int k = 0; k < region.chunks.length; k++) {
                chunk = region.chunks[k];
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

        ChunkRegion region = this.getChunkRegionFromChunkCoordinates(x, y, z);
        Chunk chunk;

        for (int i = 0; i < region.chunks.length; i++) {
            chunk = region.chunks[i];
            if (chunk == null) {continue;}
            if (chunk.x != x || chunk.y != y || chunk.z != z) {continue;}

            this.lastQueuedX = x;
            this.lastQueuedZ = y;
            this.lastQueuedY = z;
            this.lastQueuedChunk = chunk;
            return chunk;
        }
        return null;
    }

    public ChunkRegion getChunkRegionFromChunkCoordinates(int x, int y, int z) {
        x >>= 3;
        y >>= 3;
        z >>= 3;
        ChunkRegion region;
        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
            if (region == null) {continue;}
            if (region.x != x || region.y != y || region.z != z) {continue;}
            return region;
        }
        region = new ChunkRegion(x, y, z);
        this.addChunkRegion(region);
        return region;
        //throw new RuntimeException("Region not found at " + x + " " + y + " " + z);
    }

    private void addChunkRegion(ChunkRegion region) {
        for (int i = 0; i < this.regions.length; i++) {
            if (this.regions[i] == null) {
                this.regions[i] = region;
                return;
            }
        }
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



    public void removeChunkFromLists(Chunk chunk, ChunkRegion region) {
        this.nonPopulatedChunks.remove(chunk);
        this.dirtyChunks.remove(chunk);
        Thread thread = new Thread(new ThreadChunkUnloader(chunk, region));
        thread.setName("Chunk Unloader Thread For Chunk: " + chunk.x + " " + chunk.y + " " + chunk.z);
        thread.setPriority(1);
        thread.start();
    }


    public void addChunk(Chunk chunk) {
        this.chunkTerrainHandler.setTerrain(chunk.blocks, chunk);

        if (chunk.containsWater) {
            chunk.setLightValueUnderWater();
        }

        if (chunk.empty) {
            chunk.emptyChunk();
        }


        if(!chunk.empty && (chunk.containsWater || chunk.containsAir)) {
            chunk.markDirty();
            chunk.markToPopulate();
        }
        ChunkRegion region = this.getChunkRegionFromChunkCoordinates(chunk.x, chunk.y, chunk.z);
        region.addChunk(chunk);
    }

    public void addChunkFromFile(Chunk chunk) {
        ChunkColumnSkylightMap lightMap = this.findChunkSkyLightMap(chunk.x, chunk.z);
        if(!chunk.empty && (chunk.containsWater || chunk.containsAir)) {
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
                                        entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"));
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityDeer" -> {
                                        entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
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
                                Inventory chestInventory = new Inventory(((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryWidth, ((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryHeight);
                                for (int j = 0; j < chestInventory.itemStacks.length; j++) {
                                    item = inventory.getCompoundTag("slot " + j);
                                    if (item != null) {
                                        short id = item.getShort("id");
                                        byte count = item.getByte("count");
                                        short durability = item.getShort("durability");
                                        short metadata = item.getShort("metadata");
                                        chestInventory.loadItemToInventory(id, metadata, count, durability, j);
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
        if (this.playerChunkX != prevPlayerChunkX || this.playerChunkY != this.prevPlayerChunkY || this.playerChunkZ != this.prevPlayerChunkZ) {
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
        if (this.generateChunksDistance > GameSettings.renderDistance) {
            this.loadChunks = false;
            if(CosmicEvolution.instance.currentGui instanceof GuiWorldLoadingScreen){
                World.worldLoadPhase = 2;
            }
        } else {
            if (!this.isChunkColumnFullyLoaded(this.generateChunksX, this.generateChunksZ)) {
                this.addColumnLightMap(this.generateChunksX, this.generateChunksZ);
                Thread thread = new Thread(new ThreadChunkColumnLoader(this.generateChunksX, this.generateChunksZ, this));
                thread.setName("Chunk Column Loader Thread for X: " + this.generateChunksX + " Z: " + this.generateChunksZ);
                thread.setPriority(1);
                this.threadQueue.add(thread);
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
        ChunkRegion region;

        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
            if (region != null) {
                for (int k = 0; k < region.chunks.length; k++) {
                    chunk = region.chunks[k];
                    if (chunk != null) {
                        if (this.shouldChunkUnload(chunk)) {
                            this.removeChunkFromLists(chunk, region);
                        }
                    }
                }
            }
        }

        ChunkColumnSkylightMap lightmap;
        for(int i = 0; i < this.columnLightMaps.length; i++){
            if(this.columnLightMaps[i] != null){
                lightmap = this.columnLightMaps[i];
                if(!this.isChunkColumnFullyLoaded(lightmap.x, lightmap.z)){
                    this.columnLightMaps[i] = null;
                }
            }
        }


        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
            if (region != null) {
                if(region.isEmpty()){
                    this.regions[i] = null;
                }
            }
        }
    }

    public int numberOfLoadedChunks() {
        int result = 0;
        Chunk chunk;
        ChunkRegion region;
        for (int i = 0; i < this.regions.length; i++) {
            region = this.regions[i];
            if (region != null) {
                for (int k = 0; k < region.chunks.length; k++) {
                    chunk = region.chunks[k];
                    if (chunk != null) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public int numberOfLoadedRegions(){
        int result = 0;
        ChunkRegion region;
        for(int i = 0; i < this.regions.length; i++){
            region = this.regions[i];
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
        ChunkRegion region;
        for(int i = 0; i < this.regions.length; i++){
            region = this.regions[i];
            if(region != null){
                this.saveRegion(region);
            }
        }
    }

    public void saveRegion(ChunkRegion region) {
        Chunk chunk;
        for (int i = 0; i < region.chunks.length; i++) {
            chunk = region.chunks[i];
            if (chunk != null) {
                this.removeChunkFromLists(chunk, region);
            }
        }
    }

    public void saveAllRegionsWithoutUnload() {
        ChunkRegion region;
        for(int i = 0; i < this.regions.length; i++){
            region = this.regions[i];
            if(region != null){
                this.saveRegionWithoutUnload(region);
            }
        }
    }

    public void saveRegionWithoutUnload(ChunkRegion region) {
        Chunk chunk;
        for (int i = 0; i < region.chunks.length; i++) {
            chunk = region.chunks[i];
            if (chunk != null) {
                Thread thread = new Thread(new ThreadChunkSave(chunk));
                thread.setName("Chunk Saving Thread For Chunk: " + chunk.x + " " + chunk.y + " " + chunk.z);
                thread.setPriority(1);
                thread.start();
            }
        }
    }



}
