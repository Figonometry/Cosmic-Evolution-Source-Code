package spacegame.world;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public final class GeologicRegistry {
    public static final int ROCK_TYPE_IGNEOUS = 0;
    public static final int ROCK_TYPE_METAMORPHIC = 1;
    public static final int ROCK_TYPE_SEDIMENTARY = 2;
    public static final byte ANDESITE = 0;
    public static final byte GRANITE = 1;
    public static final byte PERIODITE = 2;
    public static final byte OBSIDIAN = 3;
    public static final byte BASALT = 4;
    public static final byte GABBRO = 5;
    public static final byte CHALK = 6;
    public static final byte CHERT = 7;
    public static final byte CLAYSTONE = 8;
    public static final byte CONGLOMERATE = 9;
    public static final byte SHALE = 10;
    public static final byte LIMESTONE = 11;
    public static final byte SANDSTONE = 12;
    public static final byte MARBLE = 13;
    public static final byte SLATE = 14;
    public static final byte PHYLLITE = 15;
    public static final byte SERPENTINITE = 16;
    public ConcurrentHashMap<Byte, RockType> rockTypes = new ConcurrentHashMap<>();


    //Register and create the rock types when the world starts, use the cosnstants at the top of the class as the keys to the map
    public GeologicRegistry(){
        this.registerAllStoneTypes();
    }
    private void registerAllStoneTypes(){
        this.rockTypes.put(ANDESITE, new RockType(Block.andesiteSand.ID, Block.andesiteGravel.ID, Block.andesiteStone.ID, ROCK_TYPE_IGNEOUS));
        this.rockTypes.put(GRANITE, new RockType(Block.graniteSand.ID, Block.graniteGravel.ID, Block.graniteStone.ID, ROCK_TYPE_IGNEOUS));
        this.rockTypes.put(PERIODITE, new RockType(Block.perioditeSand.ID, Block.perioditeGravel.ID, Block.perioditeStone.ID, ROCK_TYPE_IGNEOUS));
        this.rockTypes.put(OBSIDIAN, new RockType(Block.obsidianSand.ID, Block.obsidianGravel.ID, Block.obsidianStone.ID, ROCK_TYPE_IGNEOUS));
        this.rockTypes.put(BASALT, new RockType(Block.basaltSand.ID, Block.basaltGravel.ID, Block.basaltStone.ID, ROCK_TYPE_IGNEOUS));
        this.rockTypes.put(GABBRO, new RockType(Block.gabbroSand.ID, Block.gabbroGravel.ID, Block.gabbroStone.ID, ROCK_TYPE_IGNEOUS));

        this.rockTypes.put(CHALK, new RockType(Block.chalkSand.ID, Block.chalkGravel.ID, Block.chalkStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(CHERT, new RockType(Block.chertSand.ID, Block.chertGravel.ID, Block.chertStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(CLAYSTONE, new RockType(Block.claystoneSand.ID, Block.claystoneGravel.ID, Block.claystoneStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(CONGLOMERATE, new RockType(Block.conglomerateSand.ID, Block.conglomerateGravel.ID, Block.conglomerateStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(SHALE, new RockType(Block.shaleSand.ID, Block.shaleGravel.ID, Block.shaleStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(LIMESTONE, new RockType(Block.limestoneSand.ID, Block.limestoneGravel.ID, Block.limestoneStone.ID, ROCK_TYPE_SEDIMENTARY));
        this.rockTypes.put(SANDSTONE, new RockType(Block.sandstoneSand.ID, Block.sandstoneGravel.ID, Block.sandstoneStone.ID, ROCK_TYPE_SEDIMENTARY));

        this.rockTypes.put(MARBLE, new RockType(Block.marbleSand.ID, Block.marbleGravel.ID, Block.marbleStone.ID, ROCK_TYPE_METAMORPHIC));
        this.rockTypes.put(SLATE, new RockType(Block.slateSand.ID, Block.slateGravel.ID, Block.slateStone.ID, ROCK_TYPE_METAMORPHIC));
        this.rockTypes.put(PHYLLITE, new RockType(Block.phylliteSand.ID, Block.phylliteGravel.ID, Block.phylliteStone.ID, ROCK_TYPE_METAMORPHIC));
        this.rockTypes.put(SERPENTINITE, new RockType(Block.serpentiniteSand.ID, Block.serpentiniteGravel.ID, Block.serpentiniteStone.ID, ROCK_TYPE_METAMORPHIC));
    }

    public short getStoneTypeID(byte key){
        return this.rockTypes.get(key).stoneType();
    }

    public short getSandTypeID(byte key) {
        return this.rockTypes.get(key).sandType();
    }

    public short getGravelTypeID(byte key){
        return this.rockTypes.get(key).gravelType();
    }


    public static int getRockType(double noiseVal){
        noiseVal += 1;

        if(noiseVal < 0.66){
            return ROCK_TYPE_IGNEOUS;
        } else if(noiseVal < 1.32){
            return ROCK_TYPE_METAMORPHIC;
        } else if(noiseVal <= 2){
            return ROCK_TYPE_SEDIMENTARY;
        }

        throw new IllegalStateException("Invalid value to get rock type " + (noiseVal - 1));
    }


    public byte getStoneTypeAtRandom(int rockType, long provinceSeed){
        long combined = provinceSeed ^ (rockType * 0x9E3779B974A7C15L);
        Random rand = new Random(combined);
        byte key;
        while(true){
            key = (byte)rand.nextInt(SERPENTINITE + 1);
            RockType rockType1 = this.getRockTypeAtRandom(rand.nextFloat(1f), rockType);
            if(rockType1.rockType() == rockType){
                return key;
            }
        }
    }


    private RockType getRockTypeAtRandom(float chance, int rockType){
            switch (rockType){
                case ROCK_TYPE_IGNEOUS -> {
                    if(chance < 0.03f){
                        return this.rockTypes.get(OBSIDIAN);
                    }

                    if(chance < 0.1f){
                        return this.rockTypes.get(PERIODITE);
                    }

                    if(chance < 0.2f){
                        return this.rockTypes.get(GABBRO);
                    }

                    if(chance < 0.35f){
                        return this.rockTypes.get(ANDESITE);
                    }

                    if(chance < 0.65f){
                        return this.rockTypes.get(GRANITE);
                    }

                    if(chance < 1f){
                        return this.rockTypes.get(BASALT);
                    }
                }

                case ROCK_TYPE_SEDIMENTARY -> {
                    if(chance < 0.02f){
                        return this.rockTypes.get(CHALK);
                    }

                    if(chance < 0.07f){
                        return this.rockTypes.get(CHERT);
                    }

                    if(chance < 0.15f){
                        return this.rockTypes.get(CONGLOMERATE);
                    }

                    if(chance < 0.25f){
                        return this.rockTypes.get(CLAYSTONE);
                    }

                    if(chance < 0.45f){
                        return this.rockTypes.get(SANDSTONE);
                    }

                    if(chance < 0.7f){
                        return this.rockTypes.get(LIMESTONE);
                    }

                    if(chance < 1){
                        return this.rockTypes.get(SHALE);
                    }
                }

                case ROCK_TYPE_METAMORPHIC -> {
                    if(chance < 0.05f){
                        return this.rockTypes.get(SERPENTINITE); //Extremely rare
                    }

                    if(chance < 0.33f){
                        return this.rockTypes.get(PHYLLITE);
                    }

                    if(chance < 0.66f){
                        return this.rockTypes.get(SLATE);
                    }

                    if(chance < 1){
                        return this.rockTypes.get(MARBLE);
                    }
                }
            }

            return null;
    }


}
