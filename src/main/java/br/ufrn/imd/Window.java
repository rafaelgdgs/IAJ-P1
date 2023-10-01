package br.ufrn.imd;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String tittle;
    private long glfwWindow;
    private float r, g, b, a;

    private static Window window = null;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.tittle = "IAJ";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.tittle, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {
        while(!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


//            if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
//                r = 1;
//                g = 0;
//                b = 0;
//            } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
//                r = 0;
//                g = 1;
//                b = 0;
//            } else if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
//                r = 0;
//                g = 0;
//                b = 1;
//            }




            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
            } else {
                r = Math.min(r + 0.01f, 1);
                g = Math.min(g + 0.01f, 1);
                b = Math.min(b + 0.01f, 1);
            }

            r = KeyListener.isKeyPressed(GLFW_KEY_LEFT) ? 1 : r;
            g = KeyListener.isKeyPressed(GLFW_KEY_DOWN) ? 1 : g;
            b = KeyListener.isKeyPressed(GLFW_KEY_RIGHT) ? 1 : b;


            glfwSwapBuffers(glfwWindow);
        }
    }
}
