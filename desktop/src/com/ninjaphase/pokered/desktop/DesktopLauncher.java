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
		config.title = "PokeDroid - A Pokemon Adventure Player";
		config.width = PokemonApplication.V_WIDTH;
		config.height = (int)(config.width*(9f/16f));
		config.resizable = false;
		PokemonApplication.setMidiPlayer(new DesktopMidiPlayer());
		new LwjglApplication(PokemonApplication.getApplication(), config);
	}
}
