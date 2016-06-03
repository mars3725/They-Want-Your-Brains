package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.Mappers;

/**
 * Created by Matthew on 5/28/16.
 */
public class CameraSystem extends IteratingSystem {

	public CameraSystem() {
		super(Family.all(CameraComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent cameraComponent = Mappers.cm.get(entity);
		if (cameraComponent.target != null) {
			cameraComponent.camera.position.set(Mappers.pm.get(cameraComponent.target).x,Mappers.pm.get(cameraComponent.target).y, 0);
		}
	}
}
