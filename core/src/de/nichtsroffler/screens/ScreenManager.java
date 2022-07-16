package de.nichtsroffler.screens;

import com.badlogic.gdx.Screen;

import java.util.HashMap;

import de.nichtsroffler.main.JumpAndRunMain;
import de.nichtsroffler.main.NotFinalStatics;

public class ScreenManager {

    private final JumpAndRunMain jumpAndRunMain;
    private SCREEN currentScreen;

    public enum SCREEN {
        INIT_LOADING,
        MAIN_MENU,
        LEVEL_LOADING,
        PLAY,
        GAME_OVER
    }

    private final HashMap<SCREEN, Screen> screenMap;

    public ScreenManager(JumpAndRunMain jumpAndRunMain) {
        this.jumpAndRunMain = jumpAndRunMain;
        this.currentScreen = SCREEN.INIT_LOADING;
        this.screenMap = new HashMap<>();

        screenMap.put(SCREEN.INIT_LOADING, new InitLoadingScreen(jumpAndRunMain));

        jumpAndRunMain.setScreen(screenMap.get(SCREEN.INIT_LOADING));

        nextScreen();
        nextScreen();
        nextScreen();
    }

    public void nextScreen() {

        if (NotFinalStatics.debug) {
            System.out.println("-- Switch Screen command, current: " + currentScreen.name());
        }

        switch (currentScreen) {
            case INIT_LOADING:
            case GAME_OVER:
                switchScreens(SCREEN.MAIN_MENU, new MainMenuScreen(jumpAndRunMain));
                break;
            case MAIN_MENU:
                switchScreens(SCREEN.LEVEL_LOADING, new LevelLoadingScreen(jumpAndRunMain));
                break;
            case LEVEL_LOADING:
                switchScreens(SCREEN.PLAY, new PlayScreen(jumpAndRunMain));
                break;
            case PLAY:
                switchScreens(SCREEN.GAME_OVER, new GameOverScreen(jumpAndRunMain));
                break;
        }

        if (NotFinalStatics.debug) {
            System.out.println("-- Switch Screen command, to: " + currentScreen.name());
        }
    }

    private void switchScreens(SCREEN newScreen, Screen screen) {
        SCREEN oldScreen = getCurrentScreen();
        setCurrentScreen(newScreen);
        screenMap.put(newScreen, screen);
        jumpAndRunMain.setScreen(screen);
        screenMap.get(oldScreen).dispose();
    }

    public SCREEN getCurrentScreen() {
        return currentScreen;
    }
    public void setCurrentScreen(SCREEN currentScreen) {
        this.currentScreen = currentScreen;
    }
}

