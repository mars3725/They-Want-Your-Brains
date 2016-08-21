package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.NavEdgeComponent;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/30/16.
 */
public class DebuggerSystem extends EntitySystem {
	public ArrayList<Polygon> registeredShapes = new ArrayList<>();
	public Array<Entity> navNodes = new Array<>();
	public Array<Entity> navEdges = new Array<>();
	public Array<Vector2> rayCasts = new Array<>();
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Camera camera;

	public DebuggerSystem() {
		//super(Family.one(NavNodeComponent.class, NavEdgeComponent.class).get());
		//super(1f/60f);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		camera = Mappers.cm.get(engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first()).camera;
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void update(float deltaTime) {
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		for (Entity node :
				navNodes) {
			shapeRenderer.begin();
			if (Mappers.nnc.get(node).active) {
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.circle(Mappers.pm.get(node).x, Mappers.pm.get(node).y, 2, 6);
			}
			shapeRenderer.end();
		}

		for (Entity edge :
				navEdges) {
			shapeRenderer.begin();
			NavEdgeComponent edgeComponent = Mappers.ncc.get(edge);

			if (Mappers.ncc.get(edge).viable) {
				shapeRenderer.setColor(Color.YELLOW);
				shapeRenderer.line(Mappers.pm.get(edgeComponent.startingNode).x, Mappers.pm.get(edgeComponent.startingNode).y, Mappers.pm.get(edgeComponent.endingNode).x, Mappers.pm.get(edgeComponent.endingNode).y);
			}
			shapeRenderer.end();
		}

		for (Polygon shape :
				registeredShapes) {
			shapeRenderer.begin();
			//shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
			float[] vertices = shape.getTransformedVertices();
			shapeRenderer.polygon(vertices);
			shapeRenderer.end();
		}

		for (int i = 0; i < rayCasts.size - 1; i += 2) {
			shapeRenderer.begin();
		}
	}
}
