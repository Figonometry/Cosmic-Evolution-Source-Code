package spacegame.world;

import java.util.ArrayList;
import java.util.Random;

public final class GeologicProvince {
    public NoiseMap2D heightMap;
    public byte[][] rockTypes;

    private ArrayList<CoordinatePairs> queuedCoordinates = new ArrayList<>();
    private GeologicRegistry associatedRegistry;
    public int floor;

    // NEW: visited array
    private boolean[][] visited;

    public GeologicProvince(long seed, int floor, GeologicRegistry geologicRegistry){
        this.floor = floor;
        this.associatedRegistry = geologicRegistry;
        Random rand = new Random(seed);

        int mapSize = rand.nextInt(1024, 2048);

        this.heightMap = new NoiseMap2D(mapSize, mapSize, 4, 32, 1, floor, rand.nextLong());
        NoiseMap2D rockProvinceMap = new NoiseMap2D(mapSize, mapSize, 3, 1, 1, 0, rand.nextLong());

        this.rockTypes = new byte[mapSize][mapSize];
        int[][] rockTypes = new int[mapSize][mapSize];

        this.visited = new boolean[mapSize][mapSize];

        double noise;
        for(int x = 0; x < mapSize; x++){
            for(int z = 0; z < mapSize; z++){
                noise = rockProvinceMap.getNoiseRaw(x,z);
                rockTypes[x][z] = GeologicRegistry.getRockType(noise);
            }
        }

        // Flood fill each region
        for(int x = 0; x < mapSize; x++){
            for(int z = 0; z < mapSize; z++){
                if(!visited[x][z]){
                    floodFillFromCoordinate(x, z, rockTypes[x][z], rockTypes, mapSize);
                }
            }
        }
    }


    private void floodFillFromCoordinate(int x, int z, int rockType, int[][] rockTypes, int size){
        byte rockToFillWith = this.associatedRegistry.getStoneTypeAtRandom(rockType);

        queuedCoordinates.clear();
        queuedCoordinates.add(new CoordinatePairs(x, z));
        visited[x][z] = true;
        rockTypes[x][z] = rockType;
        this.rockTypes[x][z] = rockToFillWith;

        ArrayList<CoordinatePairs> localCopy = new ArrayList<>();

        while(!queuedCoordinates.isEmpty()){

            // Fill all queued cells
            for(CoordinatePairs cp : queuedCoordinates){
                this.rockTypes[cp.x][cp.z] = rockToFillWith;
            }

            // Move queue → localCopy
            localCopy.clear();
            localCopy.addAll(queuedCoordinates);
            queuedCoordinates.clear();

            // Expand flood fill
            for(CoordinatePairs cp : localCopy){
                queueSurroundingIndices(cp, rockTypes, rockType, size);
            }
        }
    }


    private void queueSurroundingIndices(CoordinatePairs cp, int[][] rockTypes, int currentRockType, int size){
        int x = cp.x;
        int z = cp.z;

        // LEFT
        if(isInBounds(x - 1, z, size) && !visited[x - 1][z] && rockTypes[x - 1][z] == currentRockType){
            visited[x - 1][z] = true;
            queuedCoordinates.add(new CoordinatePairs(x - 1, z));
        }

        // RIGHT
        if(isInBounds(x + 1, z, size) && !visited[x + 1][z] && rockTypes[x + 1][z] == currentRockType){
            visited[x + 1][z] = true;
            queuedCoordinates.add(new CoordinatePairs(x + 1, z));
        }

        // DOWN
        if(isInBounds(x, z - 1, size) && !visited[x][z - 1] && rockTypes[x][z - 1] == currentRockType){
            visited[x][z - 1] = true;
            queuedCoordinates.add(new CoordinatePairs(x, z - 1));
        }

        // UP
        if(isInBounds(x, z + 1, size) && !visited[x][z + 1] && rockTypes[x][z + 1] == currentRockType){
            visited[x][z + 1] = true;
            queuedCoordinates.add(new CoordinatePairs(x, z + 1));
        }
    }

    public byte getRockType(int x, int z){
        x &= this.rockTypes.length - 1;
        z &= this.rockTypes.length - 1;
        return this.rockTypes[x][z];
    }


    private boolean isInBounds(int x, int z, int size){
        return x >= 0 && x < size && z >= 0 && z < size;
    }
}
