package com.matthewmohandiss.zombiegame.Enums;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.matthewmohandiss.zombiegame.Assets;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;

/**
 * Created by Matthew on 7/31/16.
 */
public enum PlayerState implements State<Entity> {


	RunLeft() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(RunLeft);
			animation.loop = true;
			animation.flipped = true;
		}

		@Override
		public void update(Entity entity) {
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(-50f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void exit(Entity entity) {
			Mappers.am.get(entity).activeAnimation = null;
			Mappers.am.get(entity).runningTime = 0;
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
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(50f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void exit(Entity entity) {
			Mappers.am.get(entity).activeAnimation = null;
			Mappers.am.get(entity).runningTime = 0;
		}
	},

	Jump() {
		@Override
		public void enter(Entity entity) {
			Mappers.am.get(entity).loop = false;
			Mappers.tm.get(entity).texture = Assets.player_jump;
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(0, 750, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void update(Entity entity) {
			if (Mappers.phm.get(entity).physicsBody.getLinearVelocity().y < 0) {
				Mappers.plm.get(entity).stateMachine.changeState(Fall);
			}
		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Idle() {
		@Override
		public void enter(Entity entity) {
			Mappers.phm.get(entity).physicsBody.setLinearVelocity(0, 0);
			Mappers.tm.get(entity).texture = Assets.player_idle;
		}

		@Override
		public void update(Entity entity) {

		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Shoot() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Shoot);
			animation.loop = false;
		}

		@Override
		public void update(Entity entity) {
			if (Mappers.am.get(entity).runningTime > Mappers.am.get(entity).activeAnimation.getAnimationDuration()) {
				Mappers.plm.get(entity).stateMachine.revertToPreviousState();
			}
		}

		@Override
		public void exit(Entity entity) {
			Mappers.am.get(entity).activeAnimation = null;
			Mappers.am.get(entity).runningTime = 0;
		}
	},

	Down() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Down);
			animation.loop = false;
		}

		@Override
		public void update(Entity entity) {

		}

		@Override
		public void exit(Entity entity) {
			Mappers.am.get(entity).activeAnimation = null;
			Mappers.am.get(entity).runningTime = 0;
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
				Mappers.plm.get(entity).stateMachine.changeState(Idle);
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
