package com.matthewmohandiss.zombiegame.systems;

/**
 * Created by Matthew on 6/3/16.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.components.PhysicsComponent;
import com.matthewmohandiss.zombiegame.components.PositionComponent;

/**
 * Created by barry on 12/8/15 @ 10:11 PM.
 */
public class PhysicsSystem extends IteratingSystem {

	private static final float MAX_STEP_TIME = 1 / 60f;
	private static float accumulator = 0f;

	private World world;
	private Array<Entity> bodiesQueue;

	public PhysicsSystem(World world) {
		super(Family.all(PhysicsComponent.class, PositionComponent.class).get());

		this.world = world;
		this.bodiesQueue = new Array<>();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		if (accumulator >= MAX_STEP_TIME) {
			world.step(MAX_STEP_TIME, 6, 2);
			accumulator -= MAX_STEP_TIME;

			//Entity Queue
			for (Entity entity : bodiesQueue) {
				PositionComponent position = Mappers.pm.get(entity);
				PhysicsComponent physicsBody = Mappers.phm.get(entity);
				Vector2 pos = physicsBody.physicsBody.getPosition();
				position.x = pos.x;
				position.y = pos.y;
				position.rotation = physicsBody.physicsBody.getAngle() * MathUtils.radiansToDegrees;
			}
		}


		bodiesQueue.clear();

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		bodiesQueue.add(entity);
	}
}
