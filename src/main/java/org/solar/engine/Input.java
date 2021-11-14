package org.solar.engine;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2i;

public class Input {
    private Input() {}

    private static long m_windowHandle;

    private static boolean mouse_captured = false;
    private static int m_xpos = 0;
    private static int m_ypos = 0;

    private static Vector2i m_lastMousePos;
    private static Vector2i m_deltaMousePos;
    public static Vector2i getMousePosDelta() {
        return m_deltaMousePos;
    }

    public static void initialise(long windowHandle){
        m_windowHandle = windowHandle;
        m_lastMousePos = new Vector2i(0,0);
        m_deltaMousePos = new Vector2i(0,0);

        glfwSetCursorPosCallback(m_windowHandle, (window, xpos, ypos) -> {
            m_xpos = (int)xpos;
            m_ypos = (int)ypos;
        });

        glfwSetKeyCallback(m_windowHandle, (window, key, scancode, action, mods) -> {
        // Press C to capture the mouse, release the mouse when C is released
            System.out.println("Key callback");
            if (key == GLFW_KEY_C) {
                if (action == GLFW_PRESS) {
                    captureMouse(true);
                } else { // if release
                    captureMouse(false);
                }
            }
        });
    }

    public static boolean isKeyDown(int keyCode) {
        return glfwGetKey(m_windowHandle, keyCode) == 1;
    }

    public static void captureMouse(boolean enable) {
        if (enable) {
            if (!mouse_captured) {
                glfwSetInputMode(m_windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                mouse_captured = true;
                if (glfwGetInputMode(m_windowHandle, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
                    System.out.println("Cursor successfully captured");
                } else {
                    System.out.println("Error while trying to capture cursor");
                }
            }
        } else {
            if (mouse_captured) {
                glfwSetInputMode(m_windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                mouse_captured = false;
                if (glfwGetInputMode(m_windowHandle, GLFW_CURSOR) == GLFW_CURSOR_NORMAL) {
                    System.out.println("Cursor successfully released");
                } else {
                    System.out.println("Error while trying to release cursor");
                }
            }
        }
    }

    public static Vector2i getMousePosition() {
        return new Vector2i(m_xpos, m_ypos);
    }

    public static void update() {

        Vector2i mousePos = getMousePosition();
        m_deltaMousePos = m_lastMousePos.sub(mousePos);
        // To prevent huge jumps when the mouse leaves the application and re-enters at a different point
        /*if (Math.abs(m_deltaMousePos.get(0)) > 150 || Math.abs(m_deltaMousePos.get(1)) > 150) {
            m_deltaMousePos.zero();
        }*/
        m_deltaMousePos.mul(-1, 1);
        m_lastMousePos = mousePos;
    }
}
