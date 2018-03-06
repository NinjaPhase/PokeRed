package com.ninjaphase.pokered;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.data.DataManager;
import com.ninjaphase.pokered.data.Pokemon;
import com.ninjaphase.pokered.scene.SceneManager;
import com.ninjaphase.pokered.scene.SceneTitle;

/**
 * <p>
 *     	The {@code PokemonApplication} is the application handler for the game.
 * </p>
 *
 * <p>
 *     This will handle the main operations.
 * </p>
 */
public class PokemonApplication extends ApplicationAdapter {

	public final Color bgColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	private SpriteBatch batch;
	private SceneManager sceneManager;
	private DataManager dataManager;

	/**
	 * <p>
	 *     Private Constructor to act as a singleton.
	 * </p>
	 */
	private PokemonApplication() {}
	
	@Override
	public void create () {
		this.dataManager = new DataManager();
		this.dataManager.loadMoves();
		this.dataManager.loadSpecies();
		this.dataManager.loadMaps();
		this.sceneManager = new SceneManager();
		this.batch = new SpriteBatch();
		this.sceneManager.push(new SceneTitle(this));
		Gdx.input.setInputProcessor(this.sceneManager);
	}

	@Override
	public void render () {
		this.sceneManager.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(this.bgColor.r, this.bgColor.g, this.bgColor.b, this.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.batch.begin();
		this.sceneManager.render(batch);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.dataManager.dispose();
		this.sceneManager.dispose();
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

	private static PokemonApplication SINGLETON;

	/**
	 * @return The only instance of the application.
	 */
	public static PokemonApplication getApplication() {
		if(PokemonApplication.SINGLETON == null)
			PokemonApplication.SINGLETON = new PokemonApplication();
		return PokemonApplication.SINGLETON;
	}

}
