package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.DirectionalLight;
import engine.graph.Material;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.PointLight;
import engine.graph.SpotLight;
import engine.graph.Texture;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private Vector3f ambientLight;

    private PointLight[] pointLightList;

    private SpotLight[] spotLightList;

    private DirectionalLight directionalLight;

    private float lightAngle;

    private float spotAngle = 0;

    private float spotInc = 1;

	private static float CAMERA_POS_STEP = 0.25f;

	public ArrayList<GameItem> gameItemArray = new ArrayList<GameItem>();

	public DummyGame() {
		// Initialize everything
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
		lightAngle = -90;
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		float reflectance = 1f;
		float reflectance1 = 0.05f;
		
		Mesh mesh = OBJLoader.loadMesh("/TexturedCube.obj");
        Texture texture = new Texture("/cube.png");
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, 0, -5);
        
        Mesh mesh1 = OBJLoader.loadMesh("/cube.obj");
        Texture texture1 = new Texture("/grassblock.png");
        Material material1 = new Material(texture1, reflectance1);

        mesh1.setMaterial(material1);
        GameItem gameItem1 = new GameItem(mesh1);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(5, 0, -5);
        
       // Mesh mesh1 = OBJLoader.loadMesh("/cube.obj");
        ///GameItem gameItem1 = new GameItem(mesh1);
       // gameItem1.setScale(0.5f);
       // gameItem1.setPosition(0, 4, -5);
        //gameItem.setPosition(0, 0, -2);
        //gameItem.setScale(0.1f);
        //gameItem.setPosition(0, 0, -2);
        //gameItem.setPosition(0, 0, -0.2f);
        

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        // Point Light
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        pointLightList = new PointLight[]{pointLight};

        // Spot Light
        lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        spotLightList = new SpotLight[]{spotLight, new SpotLight(spotLight)};

        lightPosition = new Vector3f(-1, 0, 0);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
		/*
        Mesh mesh4 = OBJLoader.loadMesh("/bunny.obj");
		GameItem gameItem4 = new GameItem(mesh4);
		gameItem.setScale(1.5f);
		gameItem.setPosition(-10, 0, -2);

		GameItem gameItem1 = new GameItem(mesh);
		gameItem1.setScale(1.0f);
		gameItem1.setPosition(0, 10, -5);

		Mesh mesh2 = OBJLoader.loadMesh("/mesh.obj");
		GameItem gameItem2 = new GameItem(mesh2);
		gameItem2.setScale(1.0f);
		gameItem2.setPosition(5, 5, -5);

		Texture texture1 = new Texture("/cube.png");
		Mesh mesh3 = OBJLoader.loadMesh("/TexturedCube.obj");
		GameItem gameItem3 = new GameItem(mesh3);
		gameItem3.setPosition(0, 0, -10);
		

		Mesh torus = OBJLoader.loadMesh("/torus.obj");
		GameItem torusItem = new GameItem(torus);
		torusItem.setPosition(3, 4, -5);
		*/
		gameItems = new GameItem[]{gameItem, gameItem1};
	}

	/**
	 * When a button is pressed change how much the camera is supposed to move
	 * in that direction
	 */
	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			cameraInc.y = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_P)) {
			CAMERA_POS_STEP += 0.01f;
		} else if (window.isKeyPressed(GLFW_KEY_O)) {
			CAMERA_POS_STEP -= 0.01f;
		}
		 float lightPos = spotLightList[0].getPointLight().getPosition().z;
	        if (window.isKeyPressed(GLFW_KEY_N)) {
	            this.spotLightList[0].getPointLight().getPosition().z = lightPos + 0.1f;
	        } else if (window.isKeyPressed(GLFW_KEY_M)) {
	            this.spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;
	        }
	}

	/**
	 * Update the coordinates on the SCREEN depending on what keys have been
	 * pressed and by what direction the mouse is pointing
	 * 
	 * @param interval
	 *            not used
	 * @param mouseInput
	 *            the value for the current mouse input
	 */
	@Override
	public void update(float interval, MouseInput mouseInput) {
		// Update camera position
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);

		// Update camera's direction based on mouse
		if (mouseInput.isRightButtonPressed()) {
			Vector2f rotVec = mouseInput.getDisplVec();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
		 // Update spot light direction
        spotAngle += spotInc * 0.05f;
        if (spotAngle > 2) {
            spotInc = -1;
        } else if (spotAngle < -2) {
            spotInc = 1;
        }
        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = spotLightList[0].getConeDirection();
        coneDir.y = (float) Math.sin(spotAngleRad);

        // Update directional light direction, intensity and colour
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
	}

	/**
	 * @param window
	 *            value for windowHandler to be supplied to Renderer class
	 */
	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems, ambientLight, pointLightList, spotLightList, directionalLight);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanUp();
		}
	}

}