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
	private boolean debugNavMesh = false;
	private boolean debugRayCasts = false;
	private boolean debugShapes = false;
	private Camera camera;

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		camera = Mappers.cm.get(engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first()).camera;
		shapeRenderer.setAutoShapeType(true);
	}

	public void setDebugPreferences(boolean navMesh, boolean rayCasts, boolean shapes) {
		this.debugNavMesh = navMesh;
		this.debugRayCasts = rayCasts;
		this.debugShapes = shapes;
	}

	@Override
	public void update(float deltaTime) {
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		if (debugNavMesh) {
			for (Entity node :
					navNodes) {
				shapeRenderer.begin();
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
				if (Mappers.nnc.get(node).active) {
					shapeRenderer.circle(Mappers.pm.get(node).x, Mappers.pm.get(node).y, 2, 6);
				}
				shapeRenderer.end();
			}

			for (Entity edge :
					navEdges) {
				shapeRenderer.begin();
				NavEdgeComponent edgeComponent = Mappers.ncc.get(edge);
				shapeRenderer.setColor(Color.YELLOW);
				if (Mappers.ncc.get(edge).viable) {
					shapeRenderer.line(Mappers.pm.get(edgeComponent.startingNode).x, Mappers.pm.get(edgeComponent.startingNode).y, Mappers.pm.get(edgeComponent.endingNode).x, Mappers.pm.get(edgeComponent.endingNode).y);
				}
				shapeRenderer.end();
			}
		}

		if (debugShapes) {
			for (Polygon shape :
					registeredShapes) {
				shapeRenderer.begin();
				shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
				float[] vertices = shape.getTransformedVertices();
				shapeRenderer.polygon(vertices);
				shapeRenderer.end();
			}
		}

		if (debugRayCasts) {
			for (int i = 0; i < rayCasts.size - 1; i += 2) {
				shapeRenderer.begin();
				shapeRenderer.setColor(Color.CORAL);
				shapeRenderer.line(rayCasts.get(i), rayCasts.get(i + 1));
				shapeRenderer.end();
			}
		}
	}
}
