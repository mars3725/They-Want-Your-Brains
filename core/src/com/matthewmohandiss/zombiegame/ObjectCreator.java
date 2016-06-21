package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewmohandiss.zombiegame.Enums.Objects;
import com.matthewmohandiss.zombiegame.components.PhysicsComponent;
import com.matthewmohandiss.zombiegame.components.PositionComponent;
import com.matthewmohandiss.zombiegame.components.SizeComponent;
import com.matthewmohandiss.zombiegame.components.TextureComponent;

/**
 * Created by Matthew on 6/19/16.
 */
public class ObjectCreator {
	private GameLauncher window;
	private World physicsWorld;

	public Entity create(Objects object, float x, float y) {
		Entity entity;
		switch (object) {
			case crate:
				entity = createCrate(x, y);
				break;
			case log:
				entity = createLog(x, y);
				break;
			case trophy:
				entity = createTrophy(x, y);
				break;
			default:
				entity = createError(x, y);
				break;
		}
		return entity;
	}

	private Entity createCrate(float x, float y) {
		Entity object = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = x;
		position.y = y;
		position.z = 1;
		object.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.crate.getRegionWidth();
		size.height = Assets.crate.getRegionHeight();
		object.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.crate;
		object.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		return object;
	}

	private Entity createLog(float x, float y) {
		Entity object = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = x;
		position.y = y;
		position.z = 1;
		object.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.log.getRegionWidth();
		size.height = Assets.log.getRegionHeight();
		object.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.log;
		object.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		return object;
	}

	private Entity createTrophy(float x, float y) {
		Entity object = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = x;
		position.y = y;
		position.z = 1;
		object.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.trophy.getRegionWidth();
		size.height = Assets.trophy.getRegionHeight();
		object.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.log;
		object.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		return object;
	}

	private Entity createError(float x, float y) {
		Entity object = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = x;
		position.y = y;
		position.z = 1;
		object.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.error.getRegionWidth();
		size.height = Assets.error.getRegionHeight();
		object.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.log;
		object.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		return object;
	}
}
