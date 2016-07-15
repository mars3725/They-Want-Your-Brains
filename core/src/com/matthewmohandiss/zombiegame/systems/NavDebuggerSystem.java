package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.NavConnectionComponent;
import com.matthewmohandiss.zombiegame.components.NavNodeComponent;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavDebuggerSystem extends IteratingSystem {
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Camera camera;

	public NavDebuggerSystem() {
		super(Family.one(NavNodeComponent.class, NavConnectionComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		camera = Mappers.cm.get(engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first()).camera;
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		if (Mappers.nnc.get(entity) != null) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			if (Mappers.nnc.get(entity).active) {
				shapeRenderer.setColor(Color.GREEN);
			} else {
				shapeRenderer.setColor(Color.RED);
			}
			shapeRenderer.circle(Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, 2, 12);
			shapeRenderer.end();
		} else if (Mappers.ncc.get(entity) != null) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			NavConnectionComponent connection = Mappers.ncc.get(entity);
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.line(connection.startingPosition.x, connection.startingPosition.y, connection.endingPosition.x, connection.endingPosition.y);
			shapeRenderer.end();
		}
	}
}
