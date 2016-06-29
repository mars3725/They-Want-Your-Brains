package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewmohandiss.zombiegame.Enums.Objects;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;
import com.matthewmohandiss.zombiegame.components.*;

/**
 * Created by Matthew on 6/19/16.
 */
public class ObjectCreator {
	private GameLauncher window;
	private World world;

	public ObjectCreator(GameLauncher window, World world) {
		this.window = window;
		this.world = world;
	}

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
			case zombieCorpse:
				entity = createZombieCorpse(x, y);
				break;
			default:
				entity = createError(x, y);
				break;
		}
		return entity;
	}

	private Entity createCrate(float x, float y) {
		Entity object = createGenericEntity(Assets.crate, x, y);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		object.add(new DraggableComponent());

		return object;
	}

	private Entity createLog(float x, float y) {
		Entity object = createGenericEntity(Assets.log, x, y);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		object.add(new DraggableComponent());

		return object;
	}

	private Entity createTrophy(float x, float y) {
		Entity object = createGenericEntity(Assets.trophy, x, y);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		object.add(new DraggableComponent());

		return object;
	}

	private Entity createZombieCorpse(float x, float y) {
		Entity object = createGenericEntity(Assets.zombie_dead, x, y);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		object.add(new DraggableComponent());

		return object;
	}

	private Entity createError(float x, float y) {
		Entity object = createGenericEntity(Assets.error, x, y);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		object.add(new DraggableComponent());

		return object;
	}

	public Entity createPlayer(float xPos, float yPos) {
		Entity player = createGenericEntity(Assets.player_idle, xPos, yPos);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(xPos, yPos);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.5f;
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(player).width / 4, Mappers.sm.get(player).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = .13f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		player.add(physicsComponent);

		AnimationComponent animation = window.engine.createComponent(AnimationComponent.class);
		animation.animations.put(PlayerState.running.ordinal(), new Animation(0.15f, Assets.player_run));
		animation.animations.put(PlayerState.shooting.ordinal(), new Animation(0.3f, Assets.player_shoot));
		animation.idleTexture = Assets.player_idle;
		animation.jumpTexture = Assets.player_jump;
		animation.fallTexture = Assets.player_fall;
		player.add(animation);

		StateComponent state = window.engine.createComponent(StateComponent.class);
		state.set(PlayerState.idle);
		player.add(state);

		player.add(window.engine.createComponent(PlayerComponent.class));

		return player;
	}

	public void createZombie(float xPos, float yPos) {
		Entity zombie = createGenericEntity(Assets.zombie_idle, xPos, yPos);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(Mappers.pm.get(zombie).x, Mappers.pm.get(zombie).y);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.5f;
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(zombie).width / 6, Mappers.sm.get(zombie).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = .13f;
		fixtureDef.friction = 0.9f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		zombie.add(physicsComponent);

		AnimationComponent animation = window.engine.createComponent(AnimationComponent.class);
		animation.animations.put(PlayerState.running.ordinal(), new Animation(0.15f, Assets.zombie_run));
		animation.animations.put(PlayerState.dying.ordinal(), new Animation(0.3f, Assets.zombie_die));
		animation.idleTexture = Assets.zombie_idle;
		animation.jumpTexture = Assets.player_jump;
		animation.fallTexture = Assets.player_fall;
		zombie.add(animation);

		StateComponent state = window.engine.createComponent(StateComponent.class);
		state.set(PlayerState.idle);
		zombie.add(state);

		zombie.add(window.engine.createComponent(ZombieComponent.class));

		window.engine.addEntity(zombie);
	}

	public void createBullet(Entity player) {
		Entity bullet;
		if (Mappers.am.get(player).flipped) {
			bullet = createGenericEntity(Assets.bullet, Mappers.phm.get(player).physicsBody.getPosition().x - 7, Mappers.phm.get(player).physicsBody.getPosition().y);
			if (!Mappers.tm.get(bullet).texture.isFlipX()) {
				Mappers.tm.get(bullet).texture.flip(true, false);
			}
		} else {
			bullet = createGenericEntity(Assets.bullet, Mappers.phm.get(player).physicsBody.getPosition().x + 7, Mappers.phm.get(player).physicsBody.getPosition().y);
			if (Mappers.tm.get(bullet).texture.isFlipX()) {
				Mappers.tm.get(bullet).texture.flip(false, false);
			}
		}

		BulletComponent bulletComponent = window.engine.createComponent(BulletComponent.class);
		bullet.add(bulletComponent);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(Mappers.pm.get(bullet).x, Mappers.pm.get(bullet).y);
		Body body = world.createBody(bodyDef);
		body.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Mappers.sm.get(bullet).width / 2, Mappers.sm.get(bullet).height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = .5f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		if (Mappers.am.get(player).flipped) {
			physicsComponent.physicsBody.setLinearVelocity(-1500, 0);
		} else {
			physicsComponent.physicsBody.setLinearVelocity(1500, 0);
		}
		shape.dispose();
		bullet.add(physicsComponent);
		window.engine.addEntity(bullet);
	}

	public Entity createBackground() {
		Entity background = createGenericEntity(Assets.forest_background, Assets.forest_background.getRegionWidth() / 2, Assets.forest_background.getRegionHeight() / 2);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(Mappers.pm.get(background).x, Mappers.pm.get(background).y);
		Body body = world.createBody(bodyDef);
		ChainShape loop = new ChainShape();
		float width = Mappers.sm.get(background).width;
		float height = Mappers.sm.get(background).height;
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-width / 2, -height / 2);
		vertices[1] = new Vector2(-width / 2, height / 2);
		vertices[2] = new Vector2(width / 2, height / 2);
		vertices[3] = new Vector2(width / 2, -height / 2);
		loop.createLoop(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = loop;
		fixtureDef.friction = 1.5f;
		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		loop.dispose();
		background.add(physicsComponent);
		return background;
	}

	public Entity createGenericEntity(TextureRegion texture, float xPos, float yPos) {
		Entity entity = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = xPos;
		position.y = yPos;
		position.z = 1;
		entity.add(position);

		TextureComponent textureComponent = window.engine.createComponent(TextureComponent.class);
		textureComponent.texture = texture;
		entity.add(textureComponent);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = texture.getRegionWidth();
		size.height = texture.getRegionHeight();
		entity.add(size);

		return entity;
	}
}
