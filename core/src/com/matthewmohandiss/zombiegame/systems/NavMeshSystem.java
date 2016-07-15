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
		Array<Body> otherBodies = world.getDraggableBodies();

		for (int i = 0; i < nodes.size(); i++) {
			NavNodeComponent node = Mappers.nnc.get(nodes.get(i));
			Vector2 nodePosition = new Vector2(node.body.getPosition().x + node.original.x, node.body.getPosition().y + node.original.y);
			Vector2 newPoint = rotatePoint(nodePosition, Mappers.phm.get(entity).physicsBody.getPosition(), Mappers.phm.get(entity).physicsBody.getAngle());
			Mappers.pm.get(nodes.get(i)).x = newPoint.x;
			Mappers.pm.get(nodes.get(i)).y = newPoint.y;

			otherBodies.removeValue(Mappers.phm.get(entity).physicsBody, true);
			for (Body body :
					otherBodies) {
				for (Fixture fixture :
						body.getFixtureList()) {
					Mappers.nnc.get(nodes.get(i)).active = !fixture.testPoint(Mappers.pm.get(nodes.get(i)).x, Mappers.pm.get(nodes.get(i)).y);
				}
			}
			otherBodies.add(Mappers.phm.get(entity).physicsBody);
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
		ArrayList<Vector2> points = new ArrayList<>();

		for (Fixture fixture :
				fixtures) {
			if (fixture.getShape().getType() == Shape.Type.Polygon) {
				PolygonShape shape = ((PolygonShape) fixture.getShape());
				int nextI;
				int previousI;

				Vector2 pointA = new Vector2();
				Vector2 pointB = new Vector2();
				Vector2 pointC = new Vector2();

				Vector2 lineBA = new Vector2();
				Vector2 lineBC = new Vector2();

				Vector2 bisector = new Vector2();

				Vector2 edgePoint = new Vector2();

				for (int i = 0; i < shape.getVertexCount(); i++) {
					previousI = wrapI(i - 1, shape.getVertexCount());
					nextI = wrapI(i + 1, shape.getVertexCount());

					shape.getVertex(previousI, pointA);
					shape.getVertex(i, pointB);
					shape.getVertex(nextI, pointC);

					lineBA.set(pointA.x - pointB.x, pointA.y - pointB.y);
					lineBA.nor();
					lineBC.set(pointC.x - pointB.x, pointC.y - pointB.y);
					lineBC.nor();

					bisector.set(lineBA.x + lineBC.x, lineBA.y + lineBC.y);
					bisector.nor();
					bisector.scl(-10);

					edgePoint.set(pointB.x + bisector.x, pointB.y + bisector.y);
					focalPoints.add(edgePoint.cpy());
				}
				for (int i = 0; i < focalPoints.size(); i++) {
					Vector2 pt1 = focalPoints.get(wrapI(i, focalPoints.size()));
					Vector2 pt2 = focalPoints.get(wrapI(i + 1, focalPoints.size()));
					points.add(new Vector2((pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2));
				}
			}
		}
		System.out.println(points.toString());
		return points;
	}

	private int wrapI(int i, int size) {
		int newI = i;

		while (newI < 0 || newI >= size) {
			if (newI >= size) {
				newI -= size;
			} else if (i < 0) {
				newI += size;
			}
		}
		return newI;
	}
}
