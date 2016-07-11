package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.matthewmohandiss.zombiegame.GameLauncher;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.PhysicsWorld;
import com.matthewmohandiss.zombiegame.components.DraggableComponent;
import com.matthewmohandiss.zombiegame.components.NavConnectionComponent;
import com.matthewmohandiss.zombiegame.components.NavNodeComponent;
import com.matthewmohandiss.zombiegame.components.PositionComponent;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavMeshSystem extends IteratingSystem {
	PhysicsWorld world;
	GameLauncher window;
	ArrayList<Entity> nodes = new ArrayList<>();
	ArrayList<Entity> connections = new ArrayList<>();
	int nodeCount = 0;

	public NavMeshSystem(PhysicsWorld world, GameLauncher window) {
		super(Family.one(DraggableComponent.class).get());
		this.world = world;
		this.window = window;
	}

	public void createNavMesh(ImmutableArray<Entity> objects) {
		for (Entity object :
				objects) {
			Body body = Mappers.phm.get(object).physicsBody;
			ArrayList<Vector2> focalPoints = getBodyFocalPoints(body);

			for (Vector2 focalPoint :
					focalPoints) {
				Entity node = createNavNode(body, focalPoint, nodeCount);
				Mappers.dc.get(object).navNodes.add(node);
				nodeCount++;
				window.engine.addEntity(node);
			}
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ArrayList<Entity> nodes = Mappers.dc.get(entity).navNodes;
		for (int i = 0; i < nodes.size(); i++) {
			NavNodeComponent node = Mappers.nnc.get(nodes.get(i));
			Vector2 nodePosition = new Vector2(node.body.getPosition().x + node.original.x, node.body.getPosition().y + node.original.y);
			Vector2 newPoint = rotatePoint(nodePosition, Mappers.phm.get(entity).physicsBody.getPosition(), Mappers.phm.get(entity).physicsBody.getAngle());
			Mappers.pm.get(nodes.get(i)).x = newPoint.x;
			Mappers.pm.get(nodes.get(i)).y = newPoint.y;
		}
	}

	private Vector2 rotatePoint(Vector2 point, Vector2 origin, float angle) {
		Vector2 newPoint = new Vector2();
		newPoint.x = origin.x + (point.x - origin.x) * MathUtils.cos(angle) - (point.y - origin.y) * MathUtils.sin(angle);
		newPoint.y = origin.y + (point.x - origin.x) * MathUtils.sin(angle) + (point.y - origin.y) * MathUtils.cos(angle);
		return newPoint;
	}

	private ArrayList<Entity> getNodesForBody(Body body) {
		ArrayList<Entity> nodesForBody = new ArrayList<>();
		for (Entity node :
				nodes) {
			if (Mappers.nnc.get(node).body == body) {
				nodes.add(node);
			}
		}
		return nodesForBody;
	}

	public Entity createNavNode(Body body, Vector2 offset, int id) {
		Entity entity = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = body.getPosition().x - offset.x;
		position.y = body.getPosition().y - offset.y;
		position.z = -1;
		entity.add(position);

		NavNodeComponent navNodeComponent = new NavNodeComponent();
		navNodeComponent.id = id;
		navNodeComponent.body = body;
		navNodeComponent.original = offset;
		entity.add(navNodeComponent);

		return entity;
	}

	public Entity createNavConnection(Entity startingNode, Entity endingNode) {
		Entity entity = window.engine.createEntity();
		Vector2 startingPosition = new Vector2(Mappers.pm.get(startingNode).x, Mappers.pm.get(startingNode).y);
		Vector2 endingPosition = new Vector2(Mappers.pm.get(endingNode).x, Mappers.pm.get(endingNode).y);

		NavConnectionComponent navConnectionComponent = new NavConnectionComponent();
		navConnectionComponent.startingPosition = startingPosition;
		navConnectionComponent.endingPosition = endingPosition;
		entity.add(navConnectionComponent);

		return entity;
	}

	private ArrayList<Vector2> getBodyFocalPoints(Body body) {
		Array<Fixture> fixtures = body.getFixtureList();
		ArrayList<Vector2> focalPoints = new ArrayList<>();

		for (Fixture fixture :
				fixtures) {
			if (fixture.getShape().getType() == Shape.Type.Polygon) {

				PolygonShape shape = ((PolygonShape) fixture.getShape());
				Vector2 firstPoint = new Vector2();
				Vector2 secondPoint = new Vector2();
				Vector2 thirdPoint = new Vector2();

				ArrayList<Vector2> arr = new ArrayList<>(shape.getVertexCount());
				for (int i = 0; i < shape.getVertexCount(); i++) {
					arr.add(i, new Vector2(0, 0));
					shape.getVertex(i, arr.get(i));
				}
				System.out.println(arr.toString());

				for (int i = 2; i < shape.getVertexCount(); i++) {
					shape.getVertex(i - 2, firstPoint);
					shape.getVertex(i - 1, secondPoint);
					shape.getVertex(i, thirdPoint);
					focalPoints.add(getFocalPoint(firstPoint, secondPoint, thirdPoint));
				}

				shape.getVertex(shape.getVertexCount() - 2, firstPoint);
				shape.getVertex(shape.getVertexCount() - 1, secondPoint);
				shape.getVertex(0, thirdPoint);
				focalPoints.add(getFocalPoint(firstPoint, secondPoint, thirdPoint));

				shape.getVertex(shape.getVertexCount() - 1, firstPoint);
				shape.getVertex(0, secondPoint);
				shape.getVertex(1, thirdPoint);
				focalPoints.add(getFocalPoint(firstPoint, secondPoint, thirdPoint));
			}
		}
		return focalPoints;
	}

	private Vector2 getFocalPoint(Vector2 firstPoint, Vector2 secondPoint, Vector2 thirdPoint) {
		Vector2 lineOne = firstPoint.sub(secondPoint).nor();
		Vector2 lineTwo = thirdPoint.sub(secondPoint).nor();
		Vector2 vec = new Vector2(lineOne.add(lineTwo)).rotate(180);

		return vec.scl(25);
	}
}
