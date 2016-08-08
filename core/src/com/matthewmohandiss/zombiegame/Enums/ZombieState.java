package com.matthewmohandiss.zombiegame.Enums;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.matthewmohandiss.zombiegame.Assets;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;

/**
 * Created by Matthew on 8/6/16.
 */
public enum ZombieState implements State<Entity> {


	RunLeft() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(ZombieState.RunLeft);
			animation.loop = true;
			animation.flipped = true;
		}

		@Override
		public void update(Entity entity) {
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(-100f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void exit(Entity entity) {

		}
	},

	RunRight() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(RunRight);
			animation.loop = true;
			animation.flipped = false;
		}

		@Override
		public void update(Entity entity) {
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(100f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Climb() {
		@Override
		public void enter(Entity entity) {
			Mappers.am.get(entity).loop = false;
			Mappers.tm.get(entity).texture = Assets.player_jump;
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(0, 750, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void update(Entity entity) {

		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Idle() {
		@Override
		public void enter(Entity entity) {
			Mappers.tm.get(entity).texture = Assets.player_idle;
		}

		@Override
		public void update(Entity entity) {

		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Die() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Die);
			animation.loop = false;
		}

		@Override
		public void update(Entity entity) {

		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Fall() {
		@Override
		public void enter(Entity entity) {
			Mappers.tm.get(entity).texture = Assets.player_fall;

		}

		@Override
		public void update(Entity entity) {
			if (Mappers.phm.get(entity).physicsBody.getLinearVelocity().y == 0) {
				Mappers.zm.get(entity).stateMachine.changeState(Idle);
			}
		}

		@Override
		public void exit(Entity entity) {

		}
	};

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
}
