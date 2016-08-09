package com.matthewmohandiss.zombiegame.Enums;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.matthewmohandiss.zombiegame.Assets;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;
import com.matthewmohandiss.zombiegame.components.SteeringComponent;

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

			float time = (animation.runningTime % animation.activeAnimation.getAnimationDuration());
			if (animation.activeAnimation.getKeyFrameIndex(time) != 2 && animation.activeAnimation.getKeyFrameIndex(time) != 3 && animation.activeAnimation.getKeyFrameIndex(time) < 5) {
				Mappers.phm.get(entity).physicsBody.applyLinearImpulse(-15f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
			} else {
				Mappers.phm.get(entity).physicsBody.setLinearVelocity(0, Mappers.phm.get(entity).physicsBody.getLinearVelocity().y);
			}
		}

		@Override
		public void update(Entity entity) {
			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(-30f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
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
			AnimationComponent animation = Mappers.am.get(entity);

			float time = (animation.runningTime % animation.activeAnimation.getAnimationDuration());
			if (animation.activeAnimation.getKeyFrameIndex(time) != 2 && animation.activeAnimation.getKeyFrameIndex(time) != 3 && animation.activeAnimation.getKeyFrameIndex(time) < 5) {
				Mappers.phm.get(entity).physicsBody.applyLinearImpulse(15f, 0, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
			} else {
				Mappers.phm.get(entity).physicsBody.setLinearVelocity(0, Mappers.phm.get(entity).physicsBody.getLinearVelocity().y);
			}
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
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Jump);
			animation.loop = false;

			Mappers.phm.get(entity).physicsBody.applyLinearImpulse(0, 600, Mappers.pm.get(entity).x, Mappers.pm.get(entity).y, true);
		}

		@Override
		public void update(Entity entity) {
			if (Mappers.phm.get(entity).physicsBody.getLinearVelocity().y < 0) {
				Mappers.zm.get(entity).stateMachine.changeState(Fall);
			}
		}

		@Override
		public void exit(Entity entity) {
			Mappers.am.get(entity).activeAnimation = null;
			Mappers.am.get(entity).runningTime = 0;
		}
	},

	Idle() {
		@Override
		public void enter(Entity entity) {
			Mappers.tm.get(entity).texture = Assets.zombie_idle;
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
			entity.remove(SteeringComponent.class);
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Die);
			animation.loop = false;
		}

		@Override
		public void update(Entity entity) {
			if (Mappers.am.get(entity).activeAnimation.isAnimationFinished(Mappers.am.get(entity).runningTime)) {
				Mappers.wc.get(entity).game.removeEntity(entity);
			} else {
				Mappers.phm.get(entity).physicsBody.setAwake(false);
			}
		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Land() {
		@Override
		public void enter(Entity entity) {
			AnimationComponent animation = Mappers.am.get(entity);
			animation.activeAnimation = (Animation) animation.animations.get(Land);
			animation.loop = false;
		}

		@Override
		public void update(Entity entity) {
			if (Mappers.am.get(entity).activeAnimation.isAnimationFinished(Mappers.am.get(entity).runningTime)) {
				Mappers.zm.get(entity).stateMachine.changeState(Idle);
			}
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
			Mappers.tm.get(entity).texture = Assets.zombie_fall;

		}

		@Override
		public void update(Entity entity) {
			if (Mappers.phm.get(entity).physicsBody.getLinearVelocity().y < 1) {
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
