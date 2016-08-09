package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.matthewmohandiss.zombiegame.Mappers;
import com.matthewmohandiss.zombiegame.components.*;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private SpriteBatch batch;

	public RenderingSystem(SpriteBatch batch) {
		super(
				Family.all(PositionComponent.class)
						.one(TextureComponent.class, TextComponent.class)
						.get(), new ZComparator());
		this.batch = batch;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		Entity cam = engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		Entity hudCam = engine.getEntitiesFor(Family.all(CameraComponent.class).get()).get(1);
		camera = Mappers.cm.get(cam).camera;
		hudCamera = Mappers.cm.get(hudCam).camera;
	}

	protected void processEntity(Entity entity, float deltaTime) {
		camera.update();
		hudCamera.update();

		if (Mappers.hm.get(entity) == null) {
			batch.setProjectionMatrix(camera.combined);
		} else {
			batch.setProjectionMatrix(hudCamera.combined);
		}

		batch.begin();
		if (Mappers.tm.get(entity) != null) {
			renderTexture(entity);
		} else if (Mappers.txm.get(entity) != null) {
			renderText(entity);
		}
		batch.end();
	}

	private void renderTexture(Entity entity) {
		TextureRegion texture = Mappers.tm.get(entity).texture;
		PositionComponent position = Mappers.pm.get(entity);
		SizeComponent size = Mappers.sm.get(entity);

		if (Mappers.am.get(entity) != null) {
			if (Mappers.am.get(entity).flipped != texture.isFlipX()) {
				texture.flip(true, false);
			}
		}

		batch.draw(texture,                                                  //texture
				position.x - size.width / 2, position.y - size.height / 2,   //bottom left corner
				size.width / 2, size.height / 2,                             //origin offset
				size.width, size.height,                                     //size
				size.xScale, size.yScale,                                    //scale
				position.rotation);                                          //rotation
	}

	private void renderText(Entity entity) {
		TextComponent textComponent = Mappers.txm.get(entity);
		PositionComponent positionComponent = Mappers.pm.get(entity);

		Mappers.txm.get(entity).font.draw(batch, textComponent.text, positionComponent.x, positionComponent.y);
	}

	private static class ZComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int) Math.signum(Mappers.pm.get(e1).z - Mappers.pm.get(e2).z);
		}
	}
}