package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.components.StateComponent;

/**
 * Created by Matthew on 6/2/16.
 */
public class ControlSystem extends EntitySystem {
	Entity player = new Entity();
	Body physicsBody;
	AnimationComponent animationComponent;
	StateComponent stateComponent;

	public ControlSystem(Entity player) {
		this.player = player;
		physicsBody = Mappers.phm.get(player).physicsBody;
		animationComponent = Mappers.am.get(player);
		stateComponent = Mappers.stm.get(player);
	}

	public void moveRight() {
		if (stateComponent.state == PlayerState.idle) {
			stateComponent.set(PlayerState.running);
			animationComponent.loop = true;
			animationComponent.flipped = false;
		}
	}

	public void moveLeft() {
		if (stateComponent.state == PlayerState.idle) {
			stateComponent.set(PlayerState.running);
			animationComponent.loop = true;
			animationComponent.flipped = true;
		}
	}

	public void jump() {
		if (stateComponent.state != PlayerState.jumping && stateComponent.state != PlayerState.falling) {
			stateComponent.set(PlayerState.jumping);
			animationComponent.loop = false;
			physicsBody.applyLinearImpulse(0, 750, Mappers.pm.get(player).x, Mappers.pm.get(player).y, true);
		}
	}

	public void shoot() {
		if (stateComponent.state == PlayerState.idle || stateComponent.state == PlayerState.running) {
			stateComponent.set(PlayerState.shooting);

		}
	}

	public void idle() {
		stateComponent.set(PlayerState.idle);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		switch (stateComponent.state) {
			case idle:
				if (physicsBody.getLinearVelocity().y == 0 && Math.abs(physicsBody.getLinearVelocity().x) > 5) {
					physicsBody.setLinearVelocity(0, 0);
				}
				break;
			case running:
				if (animationComponent.flipped && physicsBody.getLinearVelocity().x > -Mappers.phm.get(player).maxVelocity) {
					physicsBody.applyLinearImpulse(-100f, 0, Mappers.pm.get(player).x, Mappers.pm.get(player).y, true);
				} else if (!animationComponent.flipped && physicsBody.getLinearVelocity().x < Mappers.phm.get(player).maxVelocity) {
					physicsBody.applyLinearImpulse(100f, 0, Mappers.pm.get(player).x, Mappers.pm.get(player).y, true);
				}
				break;
			case jumping:
				if (physicsBody.getLinearVelocity().y < 0) {
					stateComponent.set(PlayerState.falling);
				} else if (physicsBody.getLinearVelocity().y > 0) {
					Mappers.tm.get(player).texture = Mappers.am.get(player).idleTexture;
				}
				break;
			case falling:
				if (physicsBody.getLinearVelocity().y == 0) {
					idle();
				}
				break;
			case shooting:
				if (stateComponent.time > animationComponent.animations.get(PlayerState.shooting.ordinal()).getAnimationDuration()) {
					idle();
				}
				break;
		}
	}
}
