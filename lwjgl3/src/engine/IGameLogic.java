package engine;

public interface IGameLogic {

	//The game must run these items so easy to set them all here to be sure it does run them
    void init(Window window) throws Exception;
    
    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);
    
    void render(Window window);
    
    void cleanup();
}