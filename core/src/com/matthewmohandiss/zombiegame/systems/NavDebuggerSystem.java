package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.NavEdgeComponent;
import com.matthewmohandiss.zombiegame.components.NavNodeComponent;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavDebuggerSystem extends IteratingSystem {
	public ArrayList<Polygon> registeredShapes = new ArrayList<>();
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Camera camera;

	public NavDebuggerSystem() {
		super(Family.one(NavNodeComponent.class, NavEdgeComponent.class).get());
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
			NavEdgeComponent connection = Mappers.ncc.get(entity);
			if (Mappers.ncc.get(entity).viable) {
				shapeRenderer.setColor(Color.YELLOW);
			} else {
				shapeRenderer.setColor(Color.RED);
			}
			shapeRenderer.line(Mappers.pm.get(connection.startingNode).x, Mappers.pm.get(connection.startingNode).y, Mappers.pm.get(connection.endingNode).x, Mappers.pm.get(connection.endingNode).y);
			shapeRenderer.end();
		}

		for (Polygon shape :
				registeredShapes) {
			drawPolygon(shape);
		}
	}

	public void drawPolygon(Polygon polygon) {
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin();
		//shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		float[] vertices = polygon.getTransformedVertices();
		shapeRenderer.polygon(vertices);
		shapeRenderer.end();
	}
}
