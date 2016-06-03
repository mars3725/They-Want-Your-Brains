package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.matthewmohandiss.zombiegame.Assets;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;
import com.matthewmohandiss.zombiegame.components.Mappers;
import com.matthewmohandiss.zombiegame.components.StateComponent;
import com.matthewmohandiss.zombiegame.components.TextureComponent;

/**
 * Created by Matthew on 5/31/16.
 */
public class AnimationSystem extends IteratingSystem {

	public AnimationSystem() {
		super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		AnimationComponent animationComponent = Mappers.am.get(entity);
		TextureRegion texture = Assets.player_idle;

		if (animationComponent.animations.containsKey(Mappers.stm.get(entity).state.ordinal())) {
			Animation animation = animationComponent.animations.get(Mappers.stm.get(entity).state.ordinal());
			texture = animation.getKeyFrame(Mappers.stm.get(entity).time, animationComponent.loop);
			Mappers.stm.get(entity).time += deltaTime;
		} else {
			switch (Mappers.stm.get(entity).state) {
				case idle:
					texture = Assets.player_idle;

					break;
				default:
					System.out.println("Error picking player texture");
					break;
			}
		}
		if (animationComponent.flipped && !texture.isFlipX()) {
			texture.flip(true, false);
		} else if (!animationComponent.flipped && texture.isFlipX()) {
			texture.flip(true, false);
		}
		Mappers.tm.get(entity).texture = texture;
	}
}
