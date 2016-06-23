package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
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

	public HUD(GameLauncher window, GameScreen game) {
		this.window = window;
		this.game = game;
	}

	public void createDevHUD() {
		playerLocation = window.engine.createEntity();
		TextComponent textComponent = window.engine.createComponent(TextComponent.class);
		textComponent.text = "player location";
		textComponent.font = Assets.font;
		playerLocation.add(textComponent);
		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = -(window.hudCamera.viewportWidth * window.hudCamera.zoom) / 2;
		position.y = (window.hudCamera.viewportHeight * window.hudCamera.zoom) / 2;
		playerLocation.add(position);
		playerLocation.add(window.engine.createComponent(HUDComponent.class));
		window.engine.addEntity(playerLocation);

		playerState = window.engine.createEntity();
		TextComponent textComponent2 = window.engine.createComponent(TextComponent.class);
		textComponent2.text = "player state";
		textComponent2.font = Assets.font;
		playerState.add(textComponent2);
		PositionComponent position2 = window.engine.createComponent(PositionComponent.class);
		position2.x = -(window.hudCamera.viewportWidth * window.hudCamera.zoom) / 2;
		position2.y = (window.hudCamera.viewportHeight * window.hudCamera.zoom) / 2 - 15;
		playerState.add(position2);
		playerState.add(window.engine.createComponent(HUDComponent.class));
		window.engine.addEntity(playerState);

	}

	public void updateDevHUD() {
		Mappers.txm.get(playerLocation).text = "(" + Mappers.pm.get(game.player).x + ", " + Mappers.pm.get(game.player).x + ")";
		Mappers.txm.get(playerState).text = Mappers.stm.get(game.player).state.name();
	}

}
