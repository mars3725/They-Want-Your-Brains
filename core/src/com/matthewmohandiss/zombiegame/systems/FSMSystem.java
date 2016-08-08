package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.PlayerComponent;
import com.matthewmohandiss.zombiegame.components.ZombieComponent;

/**
 * Created by Matthew on 8/7/16.
 */
public class FSMSystem extends IteratingSystem {

	public FSMSystem() {
		super(Family.one(PlayerComponent.class, ZombieComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (Mappers.plm.get(entity) != null) {
			Mappers.plm.get(entity).stateMachine.update();
		} else if (Mappers.zm.get(entity) != null) {
			Mappers.zm.get(entity).stateMachine.update();
		}
	}
}
