package spacegame.gui;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.item.Item;
import spacegame.util.MathUtil;

import java.util.ArrayList;

public final class CommandParser {
    public ArrayList<String> commands = new ArrayList<>();


    public CommandParser(){
        this.commands.add("/teleport");
        this.commands.add("/tp");
        this.commands.add("/giveBlock");
        this.commands.add("/giveItem");
        this.commands.add("/time");
        this.commands.add("/freeMove");
        this.commands.add("/speed");
        this.commands.add("/heal");
        this.commands.add("/the");
        this.commands.add("/kill");
        this.commands.add("/toggleTime");
    }

    public void parseCommand(String inputString){
        String[] contents = inputString.split(" ");

        if(!this.isCommandInList(contents[0])){
            GuiInGame.setMessageText("Not a valid command",  16777215);
            return;
        }

        switch (contents[0]){
            case "/teleport", "/tp" -> {
                this.parseTeleport(contents);
            }
            case "/giveBlock" -> {
                this.parseGiveBlock(contents);
            }
            case "/giveItem" -> {
                this.parseGiveItem(contents);
            }
            case "/time" -> {
                this.parseTime(contents);
            }
            case "/freeMove" -> {
                CosmicEvolution.instance.save.thePlayer.freeMove = !CosmicEvolution.instance.save.thePlayer.freeMove;
                String status = CosmicEvolution.instance.save.thePlayer.freeMove ? "enabled" : "disabled";
                GuiInGame.setMessageText("Freemove " + status, 16777215);
            }
            case "/speed" -> {
                this.parseSpeed(contents);
            }
            case "/heal" -> {
                CosmicEvolution.instance.save.thePlayer.health = CosmicEvolution.instance.save.thePlayer.maxHealth;
                CosmicEvolution.instance.save.thePlayer.saturation = CosmicEvolution.instance.save.thePlayer.maxSaturation;
                GuiInGame.setMessageText("Fully healed hunger and health", 16777215);
            }
            case "/the" -> {
                GuiInGame.setMessageText("game", 16777215);
            }
            case "/kill" -> {
                CosmicEvolution.instance.save.thePlayer.damage(100000f);
                GuiInGame.setMessageText("Killed the player", 16777215);
            }
            case "/toggleTime" -> {
                CosmicEvolution.instance.save.activeWorld.timeStopped = !CosmicEvolution.instance.save.activeWorld.timeStopped;
                String status = CosmicEvolution.instance.save.activeWorld.timeStopped ? "stopped" : "resumed";
                GuiInGame.setMessageText("Time " + status, 16777215);

            }

        }


    }

    private void parseSpeed(String[] commandArgs){
        if(commandArgs.length != 2){
            GuiInGame.setMessageText("Invalid number of arguments", 16777215);
            return;
        }

        double speed = 0;

        try{
            speed = Double.parseDouble(commandArgs[1]);
        } catch (NumberFormatException e){
            GuiInGame.setMessageText("Invalid number format, number must be a valid double", 16777215);
            return;
        }

        if(speed < 0.0){
            GuiInGame.setMessageText("Speed must be a positive value", 16777215);
            return;
        }

        speed /= 10;

        CosmicEvolution.instance.save.thePlayer.speed = speed;
        GuiInGame.setMessageText("Set player speed to " + (int)speed * 10, 16777215);
        CosmicEvolution.instance.save.thePlayer.speedOverride = true;
    }


    private void parseTeleport(String[] commandArgs){
        //Skip over the command, we already know what command is being parsed
        if(commandArgs.length != 4){
            GuiInGame.setMessageText("Invalid number of arguments", 16777215);
            return;
        }


        int[] coordinates = new int[3];
        int coordinate = 0;
        for(int i = 1; i < commandArgs.length; i++){
            try {
                coordinate = Integer.parseInt(commandArgs[i]);
            } catch (NumberFormatException e){
                GuiInGame.setMessageText("Invalid number format, number must be a valid integer", 16777215);
                return;
            }
            coordinates[i - 1] = coordinate;
        }

        CosmicEvolution.instance.save.thePlayer.setPos(coordinates[0], coordinates[1], coordinates[2]);

        CosmicEvolution.instance.save.activeWorld.chunkController.playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
        CosmicEvolution.instance.save.activeWorld.chunkController.playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
        CosmicEvolution.instance.save.activeWorld.chunkController.playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;

        CosmicEvolution.instance.save.activeWorld.chunkController.loadChunkColumn(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5, MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5);

        GuiInGame.setMessageText("Teleported the player to " + coordinates[0] + " " + coordinates[1] + " " + coordinates[2], 16777215);

    }

    private void parseGiveBlock(String[] commandArgs) {
        if (commandArgs.length != 3) {
            GuiInGame.setMessageText("Invalid number of arguments", 16777215);
            return;
        }

        short blockID = 0;
        byte quantity = 0;

        for(int i = 1; i < commandArgs.length; i++){
            if(i == 1){
                try {
                    blockID = Short.parseShort(commandArgs[i]);
                } catch (NumberFormatException e){
                    GuiInGame.setMessageText("ID must be a valid short in the range 0 to 32767", 16777215);
                    return;
                }
            } else {
                try {
                    quantity = Byte.parseByte(commandArgs[i]);
                } catch (NumberFormatException e){
                    GuiInGame.setMessageText("Block quantity must be a valid byte in the range 0 - 127", 16777215);
                    return;
                }
            }
        }

        if(blockID < 0){
            GuiInGame.setMessageText("ID " + blockID + " is not a valid ID", 16777215);
            return;
        }

        if(Block.list[blockID] == null){
            GuiInGame.setMessageText("ID " + blockID + " is not a valid ID", 16777215);
            return;
        }

        if(!CosmicEvolution.instance.save.thePlayer.addItemToInventory(Item.block.ID, blockID, quantity, Item.NULL_ITEM_DURABILITY, 0)){
            CosmicEvolution.instance.save.activeWorld.addEntity(new EntityBlock(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, blockID, quantity));
        }

        GuiInGame.setMessageText("Gave the player "  + quantity + " " + Item.list[Item.block.ID].getDisplayName(blockID), 16777215);

    }

    private void parseGiveItem(String[] commandArgs){
        if (commandArgs.length != 3) {
            GuiInGame.setMessageText("Invalid number of arguments", 16777215);
            return;
        }

        short itemID = 0;
        byte quantity = 0;

        for(int i = 1; i < commandArgs.length; i++){
            if(i == 1){
                try {
                    itemID = Short.parseShort(commandArgs[i]);
                } catch (NumberFormatException e){
                    GuiInGame.setMessageText("ID must be a valid short in the range 0 to 32767", 16777215);
                    return;
                }
            } else {
                try {
                    quantity = Byte.parseByte(commandArgs[i]);
                } catch (NumberFormatException e){
                    GuiInGame.setMessageText("Item quantity must be a valid byte in the range 0 - 127", 16777215);
                    return;
                }
            }
        }

        if(itemID < 0){
            GuiInGame.setMessageText("ID " + itemID + " is not a valid ID", 16777215);
            return;
        }

        if(itemID == Item.block.ID){
            GuiInGame.setMessageText("ID 0 is the block item, use command giveBlock for blocks", 16777215);
            return;
        }

        if(Item.list[itemID] == null){
            GuiInGame.setMessageText("ID " + itemID + " is not a valid ID", 16777215);
            return;
        }


        if(!CosmicEvolution.instance.save.thePlayer.addItemToInventory(itemID, Item.NULL_ITEM_METADATA, quantity, Item.list[itemID].durability, 0)){
            CosmicEvolution.instance.save.activeWorld.addEntity(new EntityItem(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, itemID, Item.NULL_ITEM_METADATA, quantity, Item.list[itemID].durability, 0));
        }

        GuiInGame.setMessageText("Gave the player " + quantity + " " + Item.list[itemID].getDisplayName(Item.NULL_ITEM_REFERENCE), 16777215);
    }

    private void parseTime(String[] commandArgs){
        if(commandArgs.length != 3){
            GuiInGame.setMessageText("Invalid number of arguments", 16777215);
            return;
        }

        String subCommand = " ";
        long value = 0;
        for(int i = 1; i < commandArgs.length; i++){
            if(i == 1){
                subCommand = commandArgs[i];

                if(!subCommand.equals("set") && !subCommand.equals("add") && !subCommand.equals("sub")){
                    GuiInGame.setMessageText("The command " + subCommand + "is not a a valid command for this", 16777215);
                    return;
                }
            } else {
                try {
                    value = Long.parseLong(commandArgs[i]);
                } catch (NumberFormatException e){
                    GuiInGame.setMessageText("Invalid number format, number must be a valid long", 16777215);
                    return;
                }
            }
        }

        String message = "";
        switch (subCommand){
            case "set" -> {
                CosmicEvolution.instance.save.time = value;
                message = "Set the time to " + value + " ticks";
            }
            case "add" -> {
                CosmicEvolution.instance.save.time += value;
                message = "Added " + value + " ticks to the time";
            }
            case "sub" -> {
                CosmicEvolution.instance.save.time -= value;
                message = "Subtracted " + value + " ticks from the time";
            }
        }

        GuiInGame.setMessageText(message, 16777215);
    }

    public boolean isCommandInList(String command){
        for(int i = 0; i < this.commands.size(); i++){
            if(this.commands.get(i).equals(command)){
                return  true;
            }
        }
        return false;
    }
}
