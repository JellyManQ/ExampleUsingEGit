package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

public class Window {

	private final String title;

	private int width;

	private int height;

	private long windowHandle;

	@SuppressWarnings("unused")
	private GLFWErrorCallback errorCallback;

	@SuppressWarnings("unused")
	private GLFWKeyCallback keyCallback;

	@SuppressWarnings("unused")
	private GLFWWindowSizeCallback windowSizeCallback;

	private boolean resized;

	private boolean vSync;

	public Window(String title, int width, int height, boolean vSync) {
		// Set all the local variables to be the same
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
	}

	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // set context target
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2); // set context minimum
														// target
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // set
																		// profile
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // No idea what
																// this means

		// Fullscreen vs Windowed
		windowHandle = createWindow(true);

		// Setup resize callback
		glfwSetWindowSizeCallback(windowHandle, windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				Window.this.width = width;
				Window.this.height = height;
				Window.this.setResized(true);
			}
		});

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(windowHandle, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
					glfwSetWindowShouldClose(window, true);
				}
				// for some reason I am unable to add another if statement here
				// I want to check and see if it wants to change from full to
				// window
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(windowHandle);

		if (isvSync()) {
			// Enable v-sync
			glfwSwapInterval(1);
		}

		// Make the window visible
		glfwShowWindow(windowHandle);

		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// I believe that is sort of like CULL_FACE where it sets all of the
		// depth values
		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * @param fullscreen
	 *            Set true if you want fullscreen or else windowed
	 * @author James
	 */
	public long createWindow(boolean fullscreen) {
		// Create the window
		// Abitrary value
		long windowHandle1 = 0;
		if (fullscreen) {
			long monitor = glfwGetPrimaryMonitor();
			GLFWVidMode mode = glfwGetVideoMode(monitor);
			width = mode.width();
			height = mode.height();
			windowHandle1 = glfwCreateWindow(width, height, title, monitor, NULL);
		} else {
			windowHandle1 = glfwCreateWindow(width, height, title, NULL, NULL);
			if (windowHandle1 == NULL) {
				throw new RuntimeException("Failed to create the GLFW window");
			}
		}
		return windowHandle1;
	}

	/**
	 * 
	 * @return long windowHandle
	 */
	public long getWindowHandle() {
		return windowHandle;
	}

	/**
	 * 
	 * @param r
	 *            red value from 0.0f to 1.0f
	 * @param g
	 *            green value
	 * @param b
	 *            blue value
	 * @param alpha
	 *            clearness value
	 */
	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}

	/**
	 * 
	 * @param keyCode
	 * @return boolean
	 */
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
	}

	public boolean windowShouldClose() {
		return glfwWindowShouldClose(windowHandle);
	}

	/**
	 * 
	 * @return String title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return int width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return int height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * @return boolean resized
	 */
	public boolean isResized() {
		return resized;
	}

	/**
	 * @param resized
	 *            boolean
	 * @return boolean resized
	 */
	public void setResized(boolean resized) {
		this.resized = resized;
	}

	/**
	 * 
	 * @return boolean vSync
	 */
	public boolean isvSync() {
		return vSync;
	}

	/**
	 * 
	 * @param vSync
	 *            set true or false
	 * @return boolean vSync
	 */
	public void setvSync(boolean vSync) {
		this.vSync = vSync;
	}

	/**
	 * @param NULL
	 *            Swaps Buffers and Polls Events in glfw
	 */
	public void update() {
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
	}
}