package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.matthewmohandiss.zombiegame.GameLauncher;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.PhysicsWorld;
import com.matthewmohandiss.zombiegame.components.NavEdgeComponent;
import com.matthewmohandiss.zombiegame.components.NavNodeComponent;
import com.matthewmohandiss.zombiegame.components.PositionComponent;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavMeshSystem extends IntervalSystem {
	PhysicsWorld world;
	GameLauncher window;
	DebuggerSystem debugger;
	Array<Entity> nodes = new Array<>();
	Array<Entity> edges = new Array<>();
	Array<Entity> objects = new Array<>();

	public NavMeshSystem(PhysicsWorld world, GameLauncher window, DebuggerSystem debugger) {
		super(1 / 60f);
		this.world = world;
		this.window = window;
		this.debugger = debugger;
	}

	public void addObjectToMesh(Entity object) {
		genMeshForObject(object);

		for (Entity otherObject :
				objects) {
			genEdgesBetween(object, otherObject);
		}
		objects.add(object);
	}

	public void genEdgesBetween(Entity firstObject, Entity secondObject) {
		for (Entity firstNode :
				Mappers.dc.get(firstObject).navNodes) {
			for (Entity secondNode :
					Mappers.dc.get(secondObject).navNodes) {
				Entity edge = createEdge(firstNode, secondNode);
				edges.add(edge);
				window.engine.addEntity(edge);
				debugger.navEdges.add(edge);
			}
		}
	}

	public void genMeshForObject(Entity object) {
		Body body = Mappers.phm.get(object).physicsBody;
		ArrayList<Vector2> focalPoints = getBodyFocalPoints(body);

		for (Vector2 focalPoint :
				focalPoints) {
			Entity node = createNavNode(body, focalPoint);
			Mappers.dc.get(object).navNodes.add(node);
			window.engine.addEntity(node);
			nodes.add(node);
			debugger.navNodes.add(node);
		}

		Array<Entity> navNodes = Mappers.dc.get(object).navNodes;
		for (int i = 0; i < navNodes.size; i++) {
			Entity startingNode = navNodes.get(i);
			Entity endingNode = navNodes.get(wrapI(i + 1, navNodes.size));
			Entity edge = createEdge(startingNode, endingNode);
			window.engine.addEntity(edge);
			edges.add(edge);
			debugger.navEdges.add(edge);
		}
	}

	private Polygon box2dPolygonToLibgdxPolygon(Body body) {
		ArrayList<Float> vertices = new ArrayList<>();
		for (Fixture fixture :
				body.getFixtureList()) {
			PolygonShape shape = ((PolygonShape) fixture.getShape());
			for (int i = 0; i < shape.getVertexCount(); i++) {
				Vector2 vec = new Vector2();
				shape.getVertex(i, vec);
				vertices.add(vec.x);
				vertices.add(vec.y);
			}
		}

		float[] verts = new float[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			verts[i] = vertices.get(i);
		}
		Polygon polygon = new Polygon(verts);
		//polygon.setOrigin(0, 0);
		polygon.setPosition(body.getPosition().x, body.getPosition().y);
		polygon.setRotation(MathUtils.radiansToDegrees * body.getAngle());

		//getEngine().getSystem(DebuggerSystem.class).registeredShapes.add(polygon);
		return polygon;
	}

	@Override
	protected void updateInterval() {
		Array<Body> otherBodies = world.getDraggableBodies();

		for (Entity node :
				nodes) {
			NavNodeComponent navNode = Mappers.nnc.get(node);
			Vector2 nodePosition = new Vector2(navNode.body.getPosition().x + navNode.original.x, navNode.body.getPosition().y + navNode.original.y);
			Vector2 newPoint = rotatePoint(nodePosition, navNode.body.getPosition(), navNode.body.getAngle());
			Mappers.pm.get(node).x = newPoint.x;
			Mappers.pm.get(node).y = newPoint.y;
			navNode.active = true;

			otherBodies.removeValue(navNode.body, true);
			for (Body body :
					otherBodies) {
				for (Fixture fixture :
						body.getFixtureList()) {
					if (fixture.testPoint(Mappers.pm.get(node).x, Mappers.pm.get(node).y) || outOfGameWorld(Mappers.pm.get(node).x, Mappers.pm.get(node).y)) {
						navNode.active = false;
						break;
					}
				}
				if (!navNode.active) {
					break;
				}
			}
			otherBodies.add(navNode.body);
		}

		Array<Polygon> polygonBodies = new Array<>(otherBodies.size);
		for (Body body :
				otherBodies) {
			polygonBodies.add(box2dPolygonToLibgdxPolygon(body));
		}

		for (Entity edge :
				edges) {
			Mappers.ncc.get(edge).viable = true;
			for (Polygon body :
					polygonBodies) {
				Vector2 edgeStart = new Vector2(Mappers.pm.get(Mappers.ncc.get(edge).startingNode).x, Mappers.pm.get(Mappers.ncc.get(edge).startingNode).y);
				Vector2 edgeEnd = new Vector2(Mappers.pm.get(Mappers.ncc.get(edge).endingNode).x, Mappers.pm.get(Mappers.ncc.get(edge).endingNode).y);

				if (Intersector.intersectSegmentPolygon(edgeStart, edgeEnd, body) || !Mappers.nnc.get(Mappers.ncc.get(edge).startingNode).active || !Mappers.nnc.get(Mappers.ncc.get(edge).endingNode).active) {
					Mappers.ncc.get(edge).viable = false;
					break;
				}
			}
		}
	}

	private boolean outOfGameWorld(float xPos, float yPos) {
		return xPos < 0 || xPos > world.worldWidth || yPos < 0 || yPos > world.worldHeight;
	}

	private Vector2 rotatePoint(Vector2 point, Vector2 origin, float angle) {
		Vector2 newPoint = new Vector2();
		newPoint.x = origin.x + (point.x - origin.x) * MathUtils.cos(angle) - (point.y - origin.y) * MathUtils.sin(angle);
		newPoint.y = origin.y + (point.x - origin.x) * MathUtils.sin(angle) + (point.y - origin.y) * MathUtils.cos(angle);
		return newPoint;
	}

	public Entity createNavNode(Body body, Vector2 offset) {
		Entity entity = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = body.getPosition().x - offset.x;
		position.y = body.getPosition().y - offset.y;
		position.z = -1;
		entity.add(position);

		NavNodeComponent navNodeComponent = new NavNodeComponent();
		navNodeComponent.body = body;
		navNodeComponent.original = offset;
		entity.add(navNodeComponent);

		return entity;
	}

	public Entity createEdge(Entity startingNode, Entity endingNode) {
		Entity entity = window.engine.createEntity();

		NavEdgeComponent navEdgeComponent = new NavEdgeComponent();
		navEdgeComponent.startingNode = startingNode;
		navEdgeComponent.endingNode = endingNode;
		entity.add(navEdgeComponent);

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
					points.add(pt1);
					points.add(new Vector2((pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2));
				}
			}
		}
		return points;
	}

	private int wrapI(int i, int size) {
		int newI = i;

		while (newI < 0 || newI >= size) {
			if (newI >= size) {
				newI -= size;
			} else if (newI < 0) {
				newI += size;
			}
		}
		return newI;
	}
}
