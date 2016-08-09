package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.AnimationComponent;
import com.matthewmohandiss.zombiegame.components.TextureComponent;

/**
 * Created by Matthew on 5/31/16.
 */
public class AnimationSystem extends IteratingSystem {

	public AnimationSystem() {
		super(Family.all(TextureComponent.class, AnimationComponent.class).get());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		AnimationComponent animation = Mappers.am.get(entity);
		if (animation.activeAnimation != null) {
			animation.runningTime += deltaTime;
			TextureRegion texture = animation.activeAnimation.getKeyFrame(animation.runningTime, animation.loop);

			if (animation.flipped && !texture.isFlipX()) {
				texture.flip(true, false);
			} else if (!animation.flipped && texture.isFlipX()) {
				texture.flip(true, false);
			}

			Mappers.tm.get(entity).texture = texture;
		}
	}
}
