package com.matthewmohandiss.zombiegame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

/**
 * Created by Matthew on 6/27/16.
 */
public class PhysicsWorld implements ContactListener {
	public World world;
	public MouseJointDef mouseJointDef = new MouseJointDef();
	public MouseJoint mouseJoint;
	public Vector3 touchPoint = new Vector3();
	public Vector2 location = new Vector2();
	private GameScreen game;
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			if (fixture.testPoint(touchPoint.x, touchPoint.y) && Mappers.dc.get(game.getEntityForPhysicsBody(fixture.getBody())) != null) {
				mouseJointDef.bodyB = fixture.getBody();
				mouseJointDef.target.set(touchPoint.x, touchPoint.y);
				mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
				return true;
			}
			return false;
		}
	};

	public PhysicsWorld(GameScreen game) {
		this.game = game;
		Box2D.init();
		world = new World(new Vector2(0, -12), true);
		world.setContactListener(this);

		mouseJointDef.bodyA = world.createBody(new BodyDef());
		mouseJointDef.collideConnected = true;
		mouseJointDef.dampingRatio = 10;
		mouseJointDef.maxForce = 20000;
	}

	public void touch(Vector3 testPoint) {
		//System.out.println("touch at: " + testPoint);
		world.QueryAABB(callback, testPoint.x, testPoint.y, testPoint.x, testPoint.y);
	}


	@Override
	public void beginContact(Contact contact) {
		game.contactResolver(game.getEntityForPhysicsBody(contact.getFixtureA().getBody()), game.getEntityForPhysicsBody(contact.getFixtureB().getBody()));
		game.contactResolver(game.getEntityForPhysicsBody(contact.getFixtureB().getBody()), game.getEntityForPhysicsBody(contact.getFixtureA().getBody()));
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
