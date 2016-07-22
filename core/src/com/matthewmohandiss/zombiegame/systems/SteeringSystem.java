package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.matthewmohandiss.zombiegame.components.ZombieComponent;

/**
 * Created by Matthew on 6/3/16.
 */
public class SteeringSystem extends IteratingSystem implements Steerable<Vector2> {

	SteeringSystem() {
		super(Family.all(ZombieComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}

	@Override
	public Vector2 getLinearVelocity() {
		return null;
	}

	@Override
	public float getAngularVelocity() {
		return 0;
	}

	@Override
	public float getBoundingRadius() {
		return 0;
	}

	@Override
	public boolean isTagged() {
		return false;
	}

	@Override
	public void setTagged(boolean tagged) {

	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return 0;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {

	}

	@Override
	public float getMaxLinearSpeed() {
		return 0;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {

	}

	@Override
	public float getMaxLinearAcceleration() {
		return 0;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {

	}

	@Override
	public float getMaxAngularSpeed() {
		return 0;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {

	}

	@Override
	public float getMaxAngularAcceleration() {
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {

	}

	@Override
	public Vector2 getPosition() {
		return null;
	}

	@Override
	public float getOrientation() {
		return 0;
	}

	@Override
	public void setOrientation(float orientation) {

	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return 0;
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return null;
	}

	@Override
	public Location<Vector2> newLocation() {
		return null;
	}
}
