package spacegame.core;

import org.lwjgl.glfw.GLFW;

public final class MouseListener {
    public static MouseListener instance;
    public double scrollX, scrollY;
    public double xPos, yPos, lastX, lastY;
    public boolean mouseButtonPressed[] = new boolean[3];
    public boolean isDragging;
    public static boolean leftClickReleased;
    public static boolean rightClickReleased;

    public MouseListener() {
        this.scrollX = 0.0F;
        this.scrollY = 0.0F;
        this.xPos = 0.0F;
        this.yPos = 0.0F;
        this.lastX = 0.0F;
        this.lastY = 0.0F;
    }


    public static MouseListener getMouseListener() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }


    public static void mousePosCallback(long window, double xpos, double ypos) {
        getMouseListener().lastX = getMouseListener().xPos;
        getMouseListener().lastY = getMouseListener().yPos;
        getMouseListener().xPos = xpos;
        getMouseListener().yPos = ypos;
        getMouseListener().isDragging = getMouseListener().mouseButtonPressed[0] || getMouseListener().mouseButtonPressed[1] || getMouseListener().mouseButtonPressed[2];
    }


    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
            if (button < getMouseListener().mouseButtonPressed.length) {
                getMouseListener().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            if (button < getMouseListener().mouseButtonPressed.length) {
                getMouseListener().mouseButtonPressed[button] = false;
                getMouseListener().isDragging = false;
            }
        }
    }


    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getMouseListener().scrollX = xOffset;
        getMouseListener().scrollY = yOffset;
    }

    public static void endFrame() {
        getMouseListener().scrollX = 0;
        getMouseListener().scrollY = 0;
        getMouseListener().lastX = getMouseListener().xPos;
        getMouseListener().lastY = getMouseListener().yPos;
    }


    public static float getX() {
        return (float) getMouseListener().xPos;
    }

    public static float getY() {
        return (float) getMouseListener().yPos;
    }

    public static float getDeltaX() {
        return (float) getMouseListener().lastX - (float) getMouseListener().xPos;
    }

    public static float getDeltaY() {
        return (float) getMouseListener().lastY - (float) getMouseListener().yPos;
    }


    public static float getScrollX() {
        return (float) getMouseListener().scrollX;
    }

    public static float getScrollY() {
        return (float) getMouseListener().scrollY;
    }


    public static boolean isDragging() {
        return getMouseListener().isDragging;
    }


    public static boolean mouseButtonDown(int button) {
        if (button < getMouseListener().mouseButtonPressed.length) {
            return getMouseListener().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
