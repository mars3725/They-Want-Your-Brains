package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.systems.AnimationSystem;
import com.matthewmohandiss.zombiegame.systems.CameraSystem;
import com.matthewmohandiss.zombiegame.systems.RenderingSystem;

public class GameLauncher extends Game {
	public SpriteBatch batch;
	public PooledEngine engine;
	public float WINDOW_WIDTH = 16*5;
	public float WINDOW_HEIGHT = 9*5;
	public OrthographicCamera camera;
	private final int pixelsPerMeter = 10;

	@Override
	public void create() {
		Assets.load();
		batch = new SpriteBatch();
		engine = new PooledEngine();

//		WINDOW_WIDTH = Gdx.graphics.getWidth()/pixelsPerMeter;
//		WINDOW_HEIGHT = Gdx.graphics.getHeight()/pixelsPerMeter;

		Entity cam = engine.createEntity();
		cam.add(engine.createComponent(CameraComponent.class));
		engine.addEntity(cam);
		Mappers.cm.get(cam).camera = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
		Mappers.cm.get(cam).camera.update();
		camera = cam.getComponent(CameraComponent.class).camera;

		engine.addSystem(new CameraSystem());
		engine.addSystem(new RenderingSystem(this.batch));
		engine.addSystem(new AnimationSystem());

		setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = (WINDOW_WIDTH / width) * height;
		camera.update();
	}
}
