package com.ninjaphase.pokered.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ninjaphase.pokered.PokemonApplication;

/**
 * <p>This is the android application wrapper, it is used to run the
 * LibGDX project on an android device.</p>
 *
 * @author NinjaPhase
 */

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useWakelock = true;
        PokemonApplication.setMidiPlayer(new AndroidMidiPlayer());
        initialize(PokemonApplication.getApplication(), config);
    }

}
