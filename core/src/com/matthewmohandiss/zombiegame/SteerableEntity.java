package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.matthewmohandiss.zombiegame.Enums.ZombieState;
import com.matthewmohandiss.zombiegame.systems.DebuggerSystem;

/**
 * Created by Matthew on 7/27/16.
 */
public class SteerableEntity implements Steerable<Vector2> {
	final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
	Body body;
	Entity entity;
	float boundingRadius = 0;
	boolean tagged;
	float maxLinearSpeed = 100;
	float maxLinearAcceleration = 100;
	float maxAngularSpeed = 100;
	float maxAngularAcceleration = 100;
	Box2dLocation targetLocation = new Box2dLocation();
	private SteeringBehavior steeringBehavior;

	public SteerableEntity(Entity entity) {
		this.entity = entity;
		body = Mappers.phm.get(entity).physicsBody;
	}

	public SteeringBehavior<Vector2> getSteeringBehavior() {
		return steeringBehavior;
	}

	public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return 0.001f;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		body.setTransform(getPosition(), orientation);
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return new Box2dLocation();
	}

	public void update(float delta) {
			Entity target = Mappers.str.get(entity).target;
			targetLocation.setPosition(Mappers.phm.get(target).physicsBody.getPosition());
			Mappers.str.get(entity).seekBehavior.setTarget(targetLocation);
		if (steeringBehavior != null) {
			steeringBehavior.calculateSteering(steeringOutput);
			applySteering(steeringOutput, delta);
		}
	}

	protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {

		final StateMachine<Entity, ZombieState> stateMachine = Mappers.zm.get(entity).stateMachine;

		if (steering.linear.x < -10 && (stateMachine.isInState(ZombieState.Idle) || stateMachine.isInState(ZombieState.RunRight))) {
			stateMachine.changeState(ZombieState.RunLeft);
		} else if (steering.linear.x > 10 && (stateMachine.isInState(ZombieState.Idle) || stateMachine.isInState(ZombieState.RunLeft))) {
			stateMachine.changeState(ZombieState.RunRight);
		}

		if (steering.linear.y > 75 && (stateMachine.isInState(ZombieState.RunLeft) || stateMachine.isInState(ZombieState.RunRight) || stateMachine.isInState(ZombieState.Idle))) {
			stateMachine.changeState(ZombieState.Jump);
		}

		RayCastCallback callback = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if ((stateMachine.isInState(ZombieState.RunLeft) || stateMachine.isInState(ZombieState.RunRight) || stateMachine.isInState(ZombieState.Idle))) {
					stateMachine.changeState(ZombieState.Jump);
				}
				return 1;
			}
		};

		Vector2 start = new Vector2(Mappers.pm.get(entity).x - 5, Mappers.pm.get(entity).y - 7);
		Vector2 end = new Vector2(Mappers.pm.get(entity).x + 3, Mappers.pm.get(entity).y - 7);


		if (Mappers.wc.get(entity).window.engine.getSystem(DebuggerSystem.class).rayCasts.size != 0) {
			Mappers.wc.get(entity).window.engine.getSystem(DebuggerSystem.class).rayCasts.set(0, start);
			Mappers.wc.get(entity).window.engine.getSystem(DebuggerSystem.class).rayCasts.set(1, end);
		} else {
			Mappers.wc.get(entity).window.engine.getSystem(DebuggerSystem.class).rayCasts.add(start);
			Mappers.wc.get(entity).window.engine.getSystem(DebuggerSystem.class).rayCasts.add(end);
		}

		Mappers.wc.get(entity).game.physicsWorld.world.rayCast(callback, start, end);

		if (steering.linear.isZero() && !stateMachine.isInState(ZombieState.Idle)) {
			stateMachine.changeState(ZombieState.Idle);
		}
	}
}
