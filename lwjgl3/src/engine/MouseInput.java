package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInput {

	private final Vector2d previousPos;

	private final Vector2d currentPos;

	private final Vector2f displVec;

	private boolean inWindow = false;

	private boolean leftButtonPressed = false;

	private boolean rightButtonPressed = false;

	@SuppressWarnings("unused")
	private GLFWCursorPosCallback cursorPosCallback;

	@SuppressWarnings("unused")
	private GLFWCursorEnterCallback cursorEnterCallback;

	@SuppressWarnings("unused")
	private GLFWMouseButtonCallback mouseButtonCallback;

	public MouseInput() {
		// uses vectors from JOML
		previousPos = new Vector2d(-1, -1); // (-1, -1)
		currentPos = new Vector2d(0, 0); // (0,0)
		displVec = new Vector2f(); // No components
	}

	public void init(Window window) {
		// While the mouse is moved in the window set x and y
		glfwSetCursorPosCallback(window.getWindowHandle(), cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				currentPos.x = xpos;
				currentPos.y = ypos;
			}
		});
		// Detects when mouse has entered the window frame
		glfwSetCursorEnterCallback(window.getWindowHandle(), cursorEnterCallback = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long window, boolean entered) {
				inWindow = entered;
			}
		});
		// Detect when either mouse button is pressed
		glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
				rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
			}
		});
	}

	/**
	 * 
	 * @return displVec Vector2f
	 */
	public Vector2f getDisplVec() {
		return displVec;
	}

	/**
	 * For the mouse: if its coordinates have changed from -1,-1 and the change
	 * is greater than 0 then set the displVec components to the value of delta
	 * then set the previous to be the current
	 * 
	 * @param window
	 *            windowHandle of main
	 */
	public void input(Window window) {
		displVec.x = 0;
		displVec.y = 0;
		if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
			double deltax = currentPos.x - previousPos.x;
			double deltay = currentPos.y - previousPos.y;
			boolean rotateX = deltax != 0;
			boolean rotateY = deltay != 0;
			if (rotateX) {
				displVec.y = (float) deltax;
			}
			if (rotateY) {
				displVec.x = (float) deltay;
			}
		}
		previousPos.x = currentPos.x;
		previousPos.y = currentPos.y;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}
}