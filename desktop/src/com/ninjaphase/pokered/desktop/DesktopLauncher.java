package com.ninjaphase.pokered.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ninjaphase.pokered.PokemonApplication;

/**
 * <p>
 *     The {@code DesktopLauncher} launches the application using an Lwjgl Context.
 * </p>
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pokemon Red";
		config.width = 160;
		config.height = 144;
		new LwjglApplication(PokemonApplication.getApplication(), config);
	}
}
