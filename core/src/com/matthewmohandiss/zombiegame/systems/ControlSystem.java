package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.matthewmohandiss.zombiegame.PlayerState;
import com.matthewmohandiss.zombiegame.components.Mappers;

/**
 * Created by Matthew on 6/2/16.
 */
public class ControlSystem extends EntitySystem {
	Entity player = new Entity();

	public ControlSystem(Entity player) {
		this.player = player;
	}

	public void moveRight() {
		if (Mappers.stm.get(player).state != PlayerState.shooting) {
			Mappers.stm.get(player).set(PlayerState.running);
			Mappers.am.get(player).loop = true;
			Mappers.am.get(player).flipped = false;
		}
	}

	public void moveLeft() {
		if (Mappers.stm.get(player).state != PlayerState.shooting) {
			Mappers.stm.get(player).set(PlayerState.running);
			Mappers.am.get(player).loop = true;
			Mappers.am.get(player).flipped = true;
		}
	}

	public void jump() {
		if (Mappers.stm.get(player).state != PlayerState.jumping) {
			Mappers.stm.get(player).set(PlayerState.jumping);
			Mappers.am.get(player).loop = false;
			Mappers.pm.get(player).y += 10;
			//Mappers.stm.get(player).set(PlayerState.falling);
			Mappers.stm.get(player).set(PlayerState.idle);
		}
	}

	public void shoot() {
		if (Mappers.stm.get(player).state != PlayerState.shooting) {
			Mappers.stm.get(player).set(PlayerState.shooting);
		}
	}

	public void idle() {
		Mappers.stm.get(player).set(PlayerState.idle);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		switch (Mappers.stm.get(player).state) {
			case idle:
				break;
			case running:
				if (Mappers.am.get(player).flipped) {
					Mappers.pm.get(player).x -= 1;
				} else {
					Mappers.pm.get(player).x += 1;
				}
				break;
			case jumping:
				break;
			case falling:
				break;
			case shooting:
				if (Mappers.stm.get(player).time > Mappers.am.get(player).animations.get(PlayerState.shooting.ordinal()).getAnimationDuration()) {
					idle();
				}
				break;
		}
	}
}
