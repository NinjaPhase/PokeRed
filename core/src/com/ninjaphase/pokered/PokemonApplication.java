package com.ninjaphase.pokered;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ninjaphase.pokered.data.*;
import com.ninjaphase.pokered.input.OnscreenControls;
import com.ninjaphase.pokered.scene.SceneManager;
import com.ninjaphase.pokered.scene.SceneTitle;
import com.ninjaphase.pokered.util.Constants;
import com.ninjaphase.pokered.util.ResourceManager;
import com.ninjaphase.pokered.util.audio.MidiPlayerInterface;

import java.util.Arrays;

/**
 * <p>
 *     	The {@code PokemonApplication} is the application handler for the game.
 * </p>
 *
 * <p>
 *     This will handle the main operations.
 * </p>
 */
public final class PokemonApplication extends ApplicationAdapter {
	public static final int V_WIDTH = 320;

	public final Color bgColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	private SpriteBatch batch;
	private SceneManager sceneManager;
	private DataManager dataManager;
	private ResourceManager resourceManager;
	private OrthographicCamera defaultCamera;
	private OnscreenControls controls;

	/**
	 * <p>
	 *     Private Constructor to act as a singleton.
	 * </p>
	 */
	private PokemonApplication() {}
	
	@Override
	public void create () {
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			this.controls = new OnscreenControls();
			Gdx.input.setInputProcessor(this.controls);
		}
		this.defaultCamera = PokemonApplication.createCamera(false);
		this.resourceManager = new ResourceManager();
		this.loadDefaultResources();
		this.dataManager = new DataManager(this.resourceManager);
		this.dataManager.loadMoves();
		this.dataManager.loadSpecies();
		this.dataManager.loadRandomMapData();
		this.dataManager.loadStories();
		this.sceneManager = new SceneManager();
		this.batch = new SpriteBatch();
		this.sceneManager.push(new SceneTitle(this));
		if(Gdx.input.getInputProcessor() != null) {
			Gdx.input.setInputProcessor(new InputMultiplexer(Gdx.input.getInputProcessor(), this.sceneManager));
		} else {
			Gdx.input.setInputProcessor(this.sceneManager);
		}
	}

	@Override
	public void render () {
		float t = 1.0f;
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
			t *= 10.0f;
		this.sceneManager.update(Gdx.graphics.getDeltaTime() * t);

		Gdx.gl.glClearColor(this.bgColor.r, this.bgColor.g, this.bgColor.b, this.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.batch.begin();
		this.batch.setProjectionMatrix(defaultCamera.combined);
		this.sceneManager.render(batch);
		if(this.controls != null) this.controls.render(batch);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.sceneManager.dispose();
		this.dataManager.dispose();
		this.resourceManager.dispose();
	}

	/**
	 * <p>
	 *     Loads the default resources into the resource manager.
	 * </p>
	 */
	private void loadDefaultResources() {
		this.getResourceManager().add(
				Constants.DEFAULT_FONT, new BitmapFont(Gdx.files.internal("font/black_font.fnt")));
		this.getResourceManager().add(
				Constants.DEFAULT_WINDOWSKIN, new Texture("img/system/windowskin.png"));
		this.getResourceManager().add(
				Constants.DEFAULT_MISC, new TextureAtlas("img/system/misc.pack"));
	}

	/**
	 * @return The {@code SceneManager} for the application.
	 */
	public SceneManager getSceneManager() {
		return sceneManager;
	}

	/**
	 * @return The {@code DataManager} for the application.
	 */
	public DataManager getDataManager() {
		return this.dataManager;
	}

	/**
	 * @return The {@code ResourceManager} for the application.
	 */
	public ResourceManager getResourceManager() {
		return this.resourceManager;
	}

	/**
	 * @return The default camera used.
	 */
	public Camera getDefaultCamera() {
		return this.defaultCamera;
	}

	private static PokemonApplication SINGLETON;
	private static MidiPlayerInterface MIDI_PLAYER;

	/**
	 * @return The only instance of the application.
	 */
	public static PokemonApplication getApplication() {
		if(PokemonApplication.SINGLETON == null)
			PokemonApplication.SINGLETON = new PokemonApplication();
		return PokemonApplication.SINGLETON;
	}

	/**
	 * <p>
	 *     Sets the given midi player for the application.
	 * </p>
	 *
	 * @param midiPlayer The midi player.
	 */
	public static void setMidiPlayer(MidiPlayerInterface midiPlayer) {
		PokemonApplication.MIDI_PLAYER = midiPlayer;
	}

	/**
	 * @return The given {@code MidiPlayerInterface} for the application.
	 */
	public static MidiPlayerInterface getMidiPlayer() {
		return MIDI_PLAYER;
	}

	/**
	 * <p>
	 *     Consructs a camera that conforms to the width and height of the application.
	 * </p>
	 *
	 * @param center Whether to keep the camera centered or to place it at the bottom left.
	 * @return The camera that's just created.
	 */
	public static OrthographicCamera createCamera(boolean center) {
		return PokemonApplication.createCamera(center, V_WIDTH);
	}

	/**
	 * <p>
	 *     Consructs a camera that conforms to the width and height of the application.
	 * </p>
	 *
	 * @param center Whether to keep the camera centered or to place it at the bottom left.
	 * @return The camera that's just created.
	 */
	public static OrthographicCamera createCamera(boolean center, float vWidth) {
		float aspectRatio = ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
		OrthographicCamera newCamera = new OrthographicCamera(vWidth, vWidth*aspectRatio);
		if(!center) {
			newCamera.position.set(newCamera.viewportWidth/2f, newCamera.viewportHeight/2f, 0.0f);
		}
		newCamera.update();
		return newCamera;
	}

}
