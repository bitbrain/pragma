package de.bitbrain.pragma.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.bitbrain.pragma.PragmaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "pragma";
		config.vSyncEnabled = true;
		config.forceExit = false;
		config.samples = 8;
		new LwjglApplication(new PragmaGame(), config);
	}
}
