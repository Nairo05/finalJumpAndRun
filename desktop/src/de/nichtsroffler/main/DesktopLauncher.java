package de.nichtsroffler.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(FinalStatics.FPS);
		config.setWindowedMode(FinalStatics.WINDOW_WIDTH, FinalStatics.WINDOW_HEIGHT);
		config.setTitle("JumpAndRun");
		new Lwjgl3Application(new JumpAndRunMain(), config);
	}
}
