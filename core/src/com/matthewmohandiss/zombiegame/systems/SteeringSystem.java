package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.matthewmohandiss.zombiegame.Box2dLocation;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.SteeringComponent;

/**
 * Created by Matthew on 7/22/16.
 */
public class SteeringSystem extends IteratingSystem {
	Box2dLocation location = new Box2dLocation();

	public SteeringSystem() {
		super(Family.all(SteeringComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mappers.str.get(entity).steerable.update(deltaTime);
	}
}
