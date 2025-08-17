package spacegame.gui;

import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class Tech {
    public static final Tech[] list = new Tech[256];
    public static final Tech foraging1 = new Tech(1, "src/spacegame/assets/techFiles/foraging1.txt");
    public static final Tech foraging2 = new Tech(2, "src/spacegame/assets/techFiles/foraging2.txt");
    public static final Tech foraging3 = new Tech(3, "src/spacegame/assets/techFiles/foraging3.txt");
    public static final Tech grassDomestication = new Tech(4, "src/spacegame/assets/techFiles/grassDomestication.txt");
    public static final Tech animalHidePreservation = new Tech(5, "src/spacegame/assets/techFiles/animalHidePreservation.txt");
    public static final Tech animalHusbandry = new Tech(6, "src/spacegame/assets/techFiles/animalHusbandry.txt");
    public static final Tech backpacks = new Tech(7, "src/spacegame/assets/techFiles/backpacks.txt");
    public static final Tech basicClothing = new Tech(8, "src/spacegame/assets/techFiles/basicClothing.txt");
    public static final Tech basicWoodworking = new Tech(9, "src/spacegame/assets/techFiles/basicWoodworking.txt");
    public static final Tech bread1 = new Tech(10, "src/spacegame/assets/techFiles/bread1.txt");
    public static final Tech bread2 = new Tech(11, "src/spacegame/assets/techFiles/bread2.txt");
    public static final Tech bread3 = new Tech(12, "src/spacegame/assets/techFiles/bread3.txt");
    public static final Tech carpentry1 = new Tech(13, "src/spacegame/assets/techFiles/carpentry1.txt");
    public static final Tech carpentry2 = new Tech(14, "src/spacegame/assets/techFiles/carpentry2.txt");
    public static final Tech catDomestication = new Tech(15, "src/spacegame/assets/techFiles/catDomestication.txt");
    public static final Tech chalcolithicPottery = new Tech(16, "src/spacegame/assets/techFiles/chalcolithicPottery.txt");
    public static final Tech hunting1 = new Tech(17, "src/spacegame/assets/techFiles/hunting1.txt");
    public static final Tech hunting2 = new Tech(18, "src/spacegame/assets/techFiles/hunting2.txt");
    public static final Tech hunting3 = new Tech(19, "src/spacegame/assets/techFiles/hunting3.txt");
    public static final Tech leatherArmor = new Tech(20, "src/spacegame/assets/techFiles/leatherArmor.txt");
    public static final Tech leatherworking1 = new Tech(21, "src/spacegame/assets/techFiles/leatherworking1.txt");
    public static final Tech leatherworking2 = new Tech(22, "src/spacegame/assets/techFiles/leatherworking2.txt");
    public static final Tech leatherworking3 = new Tech(23, "src/spacegame/assets/techFiles/leatherworking3.txt");
    public static final Tech neolithicEra = new Tech(24, "src/spacegame/assets/techFiles/neolithic.txt");
    public static final Tech polishedStoneTools = new Tech(25, "src/spacegame/assets/techFiles/polishedStoneTools.txt");
    public static final Tech pottery1 = new Tech(26, "src/spacegame/assets/techFiles/pottery1.txt");
    public static final Tech pottery2 = new Tech(27, "src/spacegame/assets/techFiles/pottery2.txt");
    public static final Tech pottery3 = new Tech(28, "src/spacegame/assets/techFiles/pottery3.txt");
    public static final Tech primitiveClothing = new Tech(29, "src/spacegame/assets/techFiles/primitiveClothing.txt");
    public static final Tech primitiveWoodworking = new Tech(30, "src/spacegame/assets/techFiles/primitiveWoodworking.txt");
    public static final Tech refinedStoneTools = new Tech(31, "src/spacegame/assets/techFiles/refinedStoneTools.txt");
    public static final Tech stoneHandTools = new Tech(32, "src/spacegame/assets/techFiles/stoneHandTools.txt");
    public static final Tech trapping1 = new Tech(33, "src/spacegame/assets/techFiles/trapping1.txt");
    public static final Tech trapping2 = new Tech(34, "src/spacegame/assets/techFiles/trapping2.txt");
    public static final Tech trapping3 = new Tech(35, "src/spacegame/assets/techFiles/trapping3.txt");
    public static final Tech wheat1 = new Tech(36, "src/spacegame/assets/techFiles/wheat1.txt");
    public static final Tech wheat2 = new Tech(37, "src/spacegame/assets/techFiles/wheat2.txt");
    public static final Tech wheat3 = new Tech(38, "src/spacegame/assets/techFiles/wheat3.txt");
    public static final Tech wolfDomestication = new Tech(39, "src/spacegame/assets/techFiles/wolfDomestication.txt");
    public int state;
    public int progressAmountCompleted;
    public int unlockRequirementAmount;
    public boolean isRootNode;
    public final int techID;
    public String techName;
    public String techUpdateEvent;
    public String era;
    public ArrayList<String> requiredTechs = new ArrayList<>();
    public ArrayList<String> unlockedTech = new ArrayList<>();
    public ArrayList<String> infoText = new ArrayList<>();
    public ArrayList<String> unlockDescriptions = new ArrayList<>();
    public static final int UNKNOWN = 0;
    public static final int LOCKED = 1;
    public static final int LOCKED_KNOWN = 2;
    public static final int UNLOCKED = 3;
    public static final int NEOLITHIC_ERA = 1;
    public static final String UPDATE_EVENT_BERRY_COLLECTION = "Berry Collection";
    public static final String UPDATE_EVENT_CRAFT_STONE_HAND_TOOL = "Crafting Stone Hand Tools";


    private Tech(int techID, String filepath) {
        this.techID = techID;
        if (list[techID] != null) {
            throw new RuntimeException("Tech List already occupied at index: " + techID);
        }
        list[techID] = this;

        File techFile = new File(filepath);
        if (!techFile.exists()) {
            throw new RuntimeException("Missing Tech File at: " + filepath);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(techFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = "";
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] properties = line.split(":");

            if (properties[0].equals("unlockRequirementAmount")) {
                this.unlockRequirementAmount = Integer.parseInt(properties[1]);
            }

            if (properties[0].equals("techName")) {
                this.techName = properties[1];
            }

            if (properties[0].equals("era")) {
                this.era = properties[1];
            }

            if (properties[0].equals("techUpdateEvent")) {
                this.techUpdateEvent = properties[1];
            }

            if (properties[0].equals("requiredTech")) {
                this.requiredTechs.add(properties[1]);
            }

            if (properties[0].equals("isRootNode")) {
                this.isRootNode = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("unlockedTech")) {
                this.unlockedTech.add(properties[1]);
            }

            if (properties[0].equals("unlockDescription")) {
                this.unlockDescriptions.add(properties[1]);
            }

            if (properties[0].equals("infoText")) {
                this.infoText = this.splitInfoText(properties[1]);
            }
        }
    }

    private ArrayList<String> splitInfoText(String text){
        ArrayList<String> info = new ArrayList<>();
        char[] characters = text.toCharArray();

        char[] stringLength = new char[45];
        for(int i = 0; i < characters.length; i++){
            stringLength[i % 45] = characters[i];
            if(i % 45 == 44){
                info.add(new String(stringLength));
                Arrays.fill(stringLength, ' ');
            }
        }

        info.add(new String(stringLength));

        return info;
    }

    public ArrayList<String> getRequiredTechs(){
        return this.requiredTechs;
    }


    public void unlockTech(){
        this.state = UNLOCKED; //Unlock this tech and then change the state of all child techs from unknown to locked if requirements are met

        Tech unlockedTech;
        for(int i = 0; i < this.unlockedTech.size(); i++){
            unlockedTech = getTechFromName(this.unlockedTech.get(i));
           if(this.canTechBeResearched(unlockedTech)){
               unlockedTech.state = LOCKED;
           }
        }


    }

    public void unlockChildrenFromBaseNode() {
        Tech unlockedTech;
        for (int i = 0; i < this.unlockedTech.size(); i++) {
            unlockedTech = getTechFromName(this.unlockedTech.get(i));
            unlockedTech.unlockTech();
        }
    }

    private boolean canTechBeResearched(Tech tech){
        ArrayList<String> requiredTechs = tech.getRequiredTechs();

        Tech prequisiteTech;
        for(int i = 0; i < requiredTechs.size(); i++){
            prequisiteTech = getTechFromName(requiredTechs.get(i));
            if(prequisiteTech.state != UNLOCKED){
                return false;
            }
        }

        return true;
    }

    public static Tech getTechFromName(String name){
        for(int i = 0; i < list.length; i++){
            if(list[i] == null)continue;

            if(list[i].techName.equals(name)){
                return list[i];
            }
        }
        throw new RuntimeException("Error at Tech Name Lookup Function");
    }


    public static void techUpdateEvent(String event){
        for(int i = 0; i < list.length; i++){
            if(list[i] == null)continue;
            if(list[i].state == UNKNOWN || list[i].state == UNLOCKED)continue;
            if(!list[i].techUpdateEvent.equals(event))continue;

            list[i].progressAmountCompleted++;
            if(list[i].state == LOCKED)list[i].state = LOCKED_KNOWN;
            if(list[i].progressAmountCompleted >= list[i].unlockRequirementAmount)list[i].unlockTech();
        }
    }

    public static void loadEraBaseNodes(){
        Tech.neolithicEra.unlockTech();
        Tech.neolithicEra.unlockChildrenFromBaseNode();
    }

    public static void loadSaveTechnologies(File saveFolder){
        try{
            FileInputStream inputStream = new FileInputStream(saveFolder + "/tech.dat");
            NBTTagCompound compoundTag = NBTIO.readCompressed(inputStream);
            NBTTagCompound techs = compoundTag.getCompoundTag("Techs");

            NBTTagCompound tech;
            for(int i = 0; i < list.length; i++){
                if(list[i] == null)continue;
                if(list[i].state == UNKNOWN)continue;

                tech = techs.getCompoundTag("tech" + i);
                if(tech != null){
                    int state = tech.getInteger("state");
                    int progress = tech.getInteger("progress");
                    String name = tech.getString("name");
                    Tech tech1 = getTechFromName(name);
                    tech1.state = state;
                    tech1.progressAmountCompleted = progress;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveAllTechnologiesToFile(File saveFolder){
        try{
            FileOutputStream outputStream = new FileOutputStream(saveFolder + "/tech.dat");
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound techs = new NBTTagCompound();
            data.setTag("Techs", techs);

            NBTTagCompound[] techData = new NBTTagCompound[list.length];
            for(int i = 0; i < list.length; i++){
                if(list[i] == null)continue;
                if(list[i].state == UNKNOWN)continue;

                techData[i] = new NBTTagCompound();
                techData[i].setString("name", list[i].techName);
                techData[i].setInteger("state", list[i].state);
                techData[i].setInteger("progress", list[i].progressAmountCompleted);
                techs.setTag("tech" + i, techData[i]);
            }

            NBTIO.writeCompressed(data, outputStream);
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTechName(){
        return this.state == UNKNOWN ? "???" : this.techName;
    }

    public static ArrayList<Tech> getEraTechList(int era){
        ArrayList<Tech> techsInEra = new ArrayList<>();
        switch (era){
            case NEOLITHIC_ERA -> {
                for(int i = 0; i < list.length; i++){
                    if(list[i] == null)continue;

                    if(list[i].era.equals("Neolithic")){
                        techsInEra.add(list[i]);
                    }
                }
            }
        }

        return techsInEra;
    }

}
