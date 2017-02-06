package game;

import engine.GameEngine;
import engine.IGameLogic;
 
public class LWJGLMain {
 
    public static void main(String[] args) {
        //Creates game and sets the initial parameters for it
    	try {
    		//use vsync or nah
            boolean vSync = true;
            //Give gameLogic a place to be used
            IGameLogic gameLogic = new DummyGame();
            //Initial values for the game engine
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            //Begin the game itself with the start thread
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}