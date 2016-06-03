package com.matthewmohandiss.zombiegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.matthewmohandiss.zombiegame.components.*;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(TextureComponent.class, PositionComponent.class, SizeComponent.class).get(), new ZComparator());
		this.batch = batch;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		Entity entity = engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		camera = Mappers.cm.get(entity).camera;
		camera.zoom = 2.5f;
	}

	protected void processEntity(Entity entity, float deltaTime) {
		camera.update();

		TextureRegion texture = Mappers.tm.get(entity).texture;
		if (texture != null) {
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			PositionComponent position = Mappers.pm.get(entity);
			SizeComponent size = Mappers.sm.get(entity);

			batch.draw(texture,              //texture
					position.x - size.width/2,position.y - size.height/2,   //bottom left corner
					position.x,position.y,   //origin offset
					size.width,size.height,  //size
					size.xScale,size.yScale, //scale
					position.rotation);      //rotation
			batch.end();
		}
	}

	private static class ZComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int)Math.signum(Mappers.pm.get(e1).z - Mappers.pm.get(e2).z);
		}
	}
}