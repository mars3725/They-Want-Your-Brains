package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.matthewmohandiss.zombiegame.components.HUDComponent;
import com.matthewmohandiss.zombiegame.components.PositionComponent;
import com.matthewmohandiss.zombiegame.components.TextComponent;

/**
 * Created by Matthew on 6/23/16.
 */
public class HUD {
	private GameLauncher window;
	private GameScreen game;

	private Entity playerLocation;
	private Entity playerState;
	private Entity windowSize;
	private Entity windowZoom;
	private Entity FPS;
	private Entity mousePosition;
	private Entity zombieState;

	public HUD(GameLauncher window, GameScreen game) {
		this.window = window;
		this.game = game;
	}

	public void createDevHUD() {
		float halfWindowWidth = (window.hudCamera.viewportWidth * window.hudCamera.zoom) / 2;
		float halfWindowHeight = (window.hudCamera.viewportHeight * window.hudCamera.zoom) / 2;

		playerLocation = createLabel(-halfWindowWidth, halfWindowHeight);
		window.engine.addEntity(playerLocation);

		playerState = createLabel(-halfWindowWidth, halfWindowHeight - 15);
		window.engine.addEntity(playerState);

		windowSize = createLabel(halfWindowWidth - 100, halfWindowHeight);
		window.engine.addEntity(windowSize);

		windowZoom = createLabel(halfWindowWidth - 100, halfWindowHeight - 15);
		window.engine.addEntity(windowZoom);

		FPS = createLabel(halfWindowWidth - 60, halfWindowHeight - 15);
		window.engine.addEntity(FPS);

		mousePosition = createLabel(-halfWindowWidth, halfWindowHeight - 30);
		window.engine.addEntity(mousePosition);

		zombieState = createLabel(Mappers.pm.get(game.zombie).x, Mappers.pm.get(game.zombie).y);
		Mappers.txm.get(zombieState).font = Assets.Gray12ptFont;
		window.engine.addEntity(zombieState);
		zombieState.remove(HUDComponent.class);
	}

	private Entity createLabel(float xPos, float yPos) {
		Entity label = window.engine.createEntity();
		label.add(window.engine.createComponent(TextComponent.class));
		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = xPos;
		position.y = yPos;
		label.add(position);

		label.add(window.engine.createComponent(HUDComponent.class));
		position.z = 2;

		return label;
	}

	public void updateDevHUD() {
		Mappers.txm.get(playerLocation).text = "(" + Mappers.pm.get(game.player).x + ", " + Mappers.pm.get(game.player).y + ")";
		Mappers.txm.get(playerState).text = Mappers.plm.get(game.player).stateMachine.getCurrentState().name();
		Mappers.txm.get(windowSize).text = "(" + window.hudCamera.viewportWidth + ", " + window.hudCamera.viewportHeight + ")";
		Mappers.txm.get(windowZoom).text = String.valueOf(window.camera.zoom);
		Mappers.txm.get(FPS).text = String.valueOf(Gdx.graphics.getFramesPerSecond());
		Vector3 location = window.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Mappers.txm.get(mousePosition).text = "(" + location.x + ", " + location.y + ")";
		if (game.zombie != null && Mappers.pm.get(game.zombie) != null) {
			Mappers.pm.get(zombieState).x = Mappers.pm.get(game.zombie).x - 15;
			Mappers.pm.get(zombieState).y = Mappers.pm.get(game.zombie).y + 50;
			if (Mappers.str.get(game.zombie) != null) {
				Mappers.txm.get(zombieState).text = "" + Mappers.str.get(game.zombie).steerable.steeringOutput.linear.y;
			} else {
				Mappers.txm.get(zombieState).text = "null";
			}
		} else {
			Mappers.txm.get(zombieState).text = "";
		}
	}

}
