package com.ninjaphase.pokered.entity.event;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.scene.Scene;
import com.ninjaphase.pokered.scene.SceneRandomSelection;

/**
 * <p>
 *     The {@code SceneMapEvent} opens a scene for the scene manager to process.
 * </p>
 */
public class SceneMapEvent extends MapEvent {

    private String scene;

    public SceneMapEvent(JsonValue params) {
        this.scene = params.getString("scene");
    }

    @Override
    public void begin(MapEventPlayer player) {
        Scene actualScene = null;
        if(scene.equalsIgnoreCase("random")) {
            actualScene = new SceneRandomSelection(PokemonApplication.getApplication());
        }
        if(actualScene != null) {
            player.getSceneManager().push(actualScene);
        }
    }

    @Override
    public boolean isFinished(MapEventPlayer player) {
        return true;
    }
}
