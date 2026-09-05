package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.core.Timer;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.item.*;
import spacegame.item.itemstate.SeedState;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;
import spacegame.world.blockstate.Crop;

public abstract class GuiInventory extends Gui {
    public Inventory associatedInventory;
    public static int fillableColor;
    public static int transparentBackground;
    public GuiInventory(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
    }

    public void loadTexture(){
        fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    public void unloadTexture() {
        CosmicEvolution.instance.renderEngine.deleteTexture(fillableColor);
        CosmicEvolution.instance.renderEngine.deleteTexture(transparentBackground);
    }


    @Override
    public void handleInput() {
        if (KeyListener.isKeyPressed(GameSettings.inventoryKey.keyCode) && KeyListener.keyReleased[GameSettings.inventoryKey.keyCode] || KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            this.ce.setNewGui(new GuiInGame(this.ce));
            GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            KeyListener.setKeyReleased(GameSettings.inventoryKey.keyCode);
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)){
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_ESCAPE);
            }
        }
    }




    public void handleLeftClickOld() {
        if (!MouseListener.leftClickReleased) return;

        ItemStack stack;
        stack = this.getHoveredItemStack();
        if (stack != null) {
            if (ItemStack.itemStackOnMouse.item != null) {
                if (ItemStack.itemStackOnMouse.item.equals(stack.item) && (stack.metadata == ItemStack.itemStackOnMouse.metadata) && stack.doStatesMatch(ItemStack.itemStackOnMouse.itemState)) {
                    if (stack.count + ItemStack.itemStackOnMouse.count <= stack.item.stackLimit) {
                        stack.mergeStack(ItemStack.itemStackOnMouse);
                        ItemStack.itemStackOnMouse.clearDataFromStack();
                    } else {
                        while (stack.count < stack.item.stackLimit) {
                            stack.count++;
                            ItemStack.itemStackOnMouse.count--;
                        }
                    }
                } else if (stack.item == null) {
                    if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(ItemStack.itemStackOnMouse.item.itemType)) {
                        stack.copyDataToStack(ItemStack.itemStackOnMouse);
                        ItemStack.itemStackOnMouse.clearDataFromStack();
                        if (stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)) {
                            CosmicEvolution.instance.save.thePlayer.setPlayerStorageLevel((byte) stack.item.storageLevel);
                        }
                    } else if (!stack.usesExclusiveItem) {
                        stack.copyDataToStack(ItemStack.itemStackOnMouse);
                        ItemStack.itemStackOnMouse.clearDataFromStack();
                    }
                } else if (!stack.item.equals(ItemStack.itemStackOnMouse.item) || (stack.metadata != ItemStack.itemStackOnMouse.metadata) || !stack.doStatesMatch(ItemStack.itemStackOnMouse.itemState)) {
                    ItemStack tempStack = new ItemStack(null, (byte) 0, 0, 0);
                    tempStack.copyDataToStack(stack);
                    stack.clearDataFromStack();
                    ItemStack.itemStackOnMouse.copyDataToStack(tempStack);
                }
            } else if (stack.item != null) {
                ItemStack.itemStackOnMouse.copyDataToStack(stack);
                stack.clearDataFromStack();
                if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)) {
                    CosmicEvolution.instance.save.thePlayer.setPlayerStorageLevel((byte) 1);
                }
            }
        }
    }



    @Override
    public void handleLeftClick() {
        if (!MouseListener.leftClickReleased) return;

        ItemStack stack;
        stack = this.getHoveredItemStack();
        if (stack == null) return;




        if (ItemStack.itemStackOnMouse.item != null) {
            if (stack.canItemStackMerge(ItemStack.itemStackOnMouse)) {
                //For regular items if it can merge we directly merge the stack if the resulting value would be under the limit and then clear the mouse item stack
                //Otherwise we increment the slot's count until it equals the stack limit while decrementing the mouse item stack, we do not clear the mouse stack after this

                if (stack.count + ItemStack.itemStackOnMouse.count <= stack.item.stackLimit) {
                    stack.mergeStack(ItemStack.itemStackOnMouse);
                    ItemStack.itemStackOnMouse.clearDataFromStack();
                } else {
                    while (stack.count < stack.item.stackLimit) {
                        stack.count++;
                        ItemStack.itemStackOnMouse.count--;
                    }
                }
            } else if (stack.item == null) {
                //If the slot item is null we check if it uses exclusive item types and that the mouse stack matches, we then copy to the slot and clear the mouse
                //Otherwise directly copy from the mouse into the slot (i.e. placing an item in an empty slot)
                if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(ItemStack.itemStackOnMouse.item.itemType)) {
                    stack.copyDataToStack(ItemStack.itemStackOnMouse);
                    ItemStack.itemStackOnMouse.clearDataFromStack();
                    if (stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)) {
                        CosmicEvolution.instance.save.thePlayer.setPlayerStorageLevel((byte) stack.item.storageLevel);
                    }
                } else if (!stack.usesExclusiveItem) {
                    stack.copyDataToStack(ItemStack.itemStackOnMouse);
                    ItemStack.itemStackOnMouse.clearDataFromStack();
                }
            } else if (!stack.canItemStackMerge(ItemStack.itemStackOnMouse)) {
                //If the stacks cannot merge swap the item stacks between the mouse and slot
                ItemStack tempStack = new ItemStack(null, (byte) 0, 0, 0);
                tempStack.copyDataToStack(stack);
                stack.clearDataFromStack();
                stack.copyDataToStack(ItemStack.itemStackOnMouse);
                ItemStack.itemStackOnMouse.copyDataToStack(tempStack);
            }
        } else if (stack.item != null) {
            //If the mouse item stack is empty, copy from the clicked item stack (i.e. pickup to move around inventory)
            ItemStack.itemStackOnMouse.copyDataToStack(stack);
            stack.clearDataFromStack();
            if (stack.usesExclusiveItem && stack.exclusiveItemType.equals(Item.ITEM_TYPE_PLAYER_STORAGE)) {
                CosmicEvolution.instance.save.thePlayer.setPlayerStorageLevel((byte) 1);
            }
        }
    }

    public abstract ItemStack getHoveredItemStack();


    public void renderHoveredItemStackName(ItemStack stack){
        if(stack == null)return;
        if(stack.item == null)return;
        if(ItemStack.itemStackOnMouse.item != null && stack != ItemStack.itemStackOnMouse)return;

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;
        tessellator.toggleOrtho();
        String displayedName = stack.item.getDisplayName(stack.metadata, stack.metadata);
        float x = MathUtil.getOpenGLMouseX();
        float y = MathUtil.getOpenGLMouseY();
        int font = 50;

        boolean isDecayItem = stack.item instanceof IDecayItem;
        boolean isSeedMutating = stack.item instanceof ItemSeed && ((SeedState)stack.itemState).canMutate;

        float height = font + (isDecayItem || isSeedMutating ? font * 3 : 0);
        float width = font * ((displayedName.length() + 2) * 0.34f);

        StringBuilder stringBuilder = new StringBuilder();
        if(isDecayItem){
            long timeUntil = stack.decayTime - CosmicEvolution.instance.save.time;
            long daysUntil = timeUntil / Timer.GAME_DAY;
            long hoursUntil = (timeUntil % Timer.GAME_DAY) / Timer.GAME_HOUR;
            long minutesUntil = (timeUntil % Timer.GAME_HOUR) / Timer.GAME_MINUTE;

            stringBuilder.append("Decays In: ");
            if (daysUntil != 0) {
                stringBuilder.append(daysUntil).append(daysUntil != 1 ? " Days " : " Day ");
            }
            if (hoursUntil != 0 || (minutesUntil != 0 && daysUntil != 0)) {
                stringBuilder.append(hoursUntil).append(hoursUntil != 1 ? " Hours " : " Hour ");
            }
            if (minutesUntil != 0) {
                stringBuilder.append(minutesUntil).append(minutesUntil != 1 ? " Minutes" : " Minute");
            }
        }

        if(isSeedMutating){
            SeedState seedState = (SeedState)stack.itemState;

            stringBuilder.append("Domesticating towards ").append(Crop.getCropFromName(seedState.targetCrop).displayName).append(" ").append(seedState.percentToTargetCrop * 100).append("% Completed");
        }

        width += font * stringBuilder.length() * 0.34f;

        tessellator.addVertex2DTexture(0, x, y, -10, 3);
        tessellator.addVertex2DTexture(0, x + width, y + height, -10, 1);
        tessellator.addVertex2DTexture(0, x, y + height, -10, 2);
        tessellator.addVertex2DTexture(0, x + width, y, -10, 0);
        tessellator.addElementsCW();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();



        fontRenderer.drawString(displayedName, x, y, -9, 16777215, font, 255);

        if(isDecayItem || isSeedMutating){
            fontRenderer.drawString(stringBuilder.toString(), x, y + 120, -9, 16777215, font, 255);
        }
    }

}
