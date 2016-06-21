package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.systems.AnimationSystem;
import com.matthewmohandiss.zombiegame.systems.CameraSystem;
import com.matthewmohandiss.zombiegame.systems.RenderingSystem;

public class GameLauncher extends Game {
	public SpriteBatch batch;
	public PooledEngine engine;
	public OrthographicCamera camera;

	@Override
	public void create() {
		Assets.load();
		batch = new SpriteBatch();
		engine = new PooledEngine();

		Entity cam = engine.createEntity();
		cam.add(engine.createComponent(CameraComponent.class));
		engine.addEntity(cam);
		Mappers.cm.get(cam).camera = new OrthographicCamera(80f, 45f);
		Mappers.cm.get(cam).camera.update();
		camera = cam.getComponent(CameraComponent.class).camera;
		camera.zoom = 5;

		CameraSystem cameraSystem = new CameraSystem();
		cameraSystem.priority = 4;
		engine.addSystem(cameraSystem);
		RenderingSystem renderingSystem = new RenderingSystem(this.batch);
		renderingSystem.priority = 3;
		engine.addSystem(renderingSystem);
		AnimationSystem animationSystem = new AnimationSystem();
		animationSystem.priority = 2;
		engine.addSystem(animationSystem);

		setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = (80f / width) * height;
		camera.update();
	}
}
