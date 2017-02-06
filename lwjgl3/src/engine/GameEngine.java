package engine;

public class GameEngine implements Runnable {

	public static final int TARGET_FPS = 100;

	public static final int TARGET_UPS = 30;

	private final Window window;

	private final Thread gameLoopThread;

	private final Timer timer;

	private final IGameLogic gameLogic;

	private final MouseInput mouseInput;

	// Constructor used to set everything up by setting the values
	public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Window(windowTitle, width, height, vSync);
		mouseInput = new MouseInput();
		this.gameLogic = gameLogic;
		timer = new Timer();
	}

	public void start() {
		// Mac and Windows Compatability
		String osName = System.getProperty("os.name");
		if (osName.contains("Mac")) {
			gameLoopThread.run();
		} else {
			gameLoopThread.start();
		}
	}

	// Thread from GAME_LOOP_THREAD
	@Override
	public void run() {
		try {
			// Initialize
			init();
			// When complete run gameLoop until it throws an exception or exits
			gameLoop();
		} catch (Exception excp) {
			excp.printStackTrace();
		} finally {
			// Destroy all the resources and make garbage collection easier on
			// the system
			cleanup();
		}
	}

	protected void init() throws Exception {
		// Initiallize fucking everything!
		window.init();
		timer.init();
		mouseInput.init(window);
		gameLogic.init(window);
		// Go to gameLoop
	}

	/**
	 * main thread where items are done every "tick"
	 * 
	 */
	protected void gameLoop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;

		boolean running = true;
		while (running && !window.windowShouldClose()) {
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			// updates the mouse position in Mouse and IGameLogic(aka run input in DummyGame)
			input();

			//keeps everything in time according to refresh rate if my interpretation is correct
			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}

			//Goes to IGameLogic and calls window.update
			render();

			//run vSync if turned on
			if (!window.isvSync()) {
				sync();
			}
		}
	}

	protected void cleanup() {
		gameLogic.cleanup();
	}
	//If the system finished its processes before the target refresh rate then pause for 1ms
	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	protected void input() {
		mouseInput.input(window);
		gameLogic.input(window, mouseInput);
	}

	protected void update(float interval) {
		gameLogic.update(interval, mouseInput);
	}

	protected void render() {
		gameLogic.render(window);
		window.update();
	}
}