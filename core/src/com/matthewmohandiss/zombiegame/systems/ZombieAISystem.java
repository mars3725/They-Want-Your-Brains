package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.matthewmohandiss.zombiegame.components.ZombieComponent;

/**
 * Created by Matthew on 6/3/16.
 */
public class ZombieAISystem extends IteratingSystem {

	ZombieAISystem() {
		super(Family.all(ZombieComponent.class).get());
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}
}
