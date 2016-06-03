package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.components.PositionComponent;
import com.matthewmohandiss.zombiegame.components.VelocityComponent;

/**
 * Created by Matthew on 5/27/16.
 */
public class MovementSystem extends IteratingSystem {

	public MovementSystem() {
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	public void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.pm.get(entity);
		VelocityComponent velocity = Mappers.vm.get(entity);

		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
	}
}