package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.matthewmohandiss.zombiegame.Enums.Objects;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;
import com.matthewmohandiss.zombiegame.Enums.ZombieState;
import com.matthewmohandiss.zombiegame.components.*;

/**
 * Created by Matthew on 6/19/16.
 */
public class ObjectCreator {
	private GameLauncher window;
	private World world;
	private GameScreen game;

	public ObjectCreator(GameScreen game) {
		this.game = game;
		this.world = game.physicsWorld.world;
		this.window = game.window;
	}

	public Entity crate(float x, float y) {
		Entity object = genericEntity(Assets.crate, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.crate;
		object.add(draggableComponent);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f;
		fixtureDef.friction = 0.9f;
		Vector2[] arr = new Vector2[10];
		for (int i = 0; i < shape.getVertexCount(); i++) {
			arr[i] = new Vector2(0, 0);
			shape.getVertex(i, arr[i]);
		}


		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		object.add(physicsComponent);

		return object;
	}

	public Entity canoe(float x, float y) {
		Entity object = genericEntity(Assets.canoe, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.canoe;
		object.add(draggableComponent);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

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

	public Entity log(float x, float y) {
		Entity object = genericEntity(Assets.log, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.log;
		object.add(new DraggableComponent());

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

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

	public Entity trophy(float x, float y) {
		Entity object = genericEntity(Assets.trophy, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.trophy;
		object.add(new DraggableComponent());

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

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

	public Entity zombieCorpse(float x, float y) {
		Entity object = genericEntity(Assets.zombie_corpse, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.zombieCorpse;
		object.add(new DraggableComponent());

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

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

	public Entity error(float x, float y) {
		Entity object = genericEntity(Assets.error, x, y);

		DraggableComponent draggableComponent = window.engine.createComponent(DraggableComponent.class);
		draggableComponent.objectType = Objects.error;
		object.add(new DraggableComponent());

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		PolygonShape shape = physicsBodyForObject(object);

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

	//Special objects

	public Entity player(float xPos, float yPos) {
		Entity player = genericEntity(Assets.player_idle, xPos, yPos);

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
		ObjectMap<PlayerState, Animation> animationMap = new ObjectMap<>();
		animationMap.put(PlayerState.RunRight, new Animation(0.15f, Assets.player_run));
		animationMap.put(PlayerState.RunLeft, new Animation(0.15f, Assets.player_run));
		animationMap.put(PlayerState.Shoot, new Animation(0.3f, Assets.player_shoot));
		animation.animations = animationMap;
		player.add(animation);

		PlayerComponent playerComponent = window.engine.createComponent(PlayerComponent.class);
		playerComponent.stateMachine = new DefaultStateMachine<>(player, PlayerState.Idle, PlayerState.Idle);
		player.add(playerComponent);

		player.add(window.engine.createComponent(PlayerComponent.class));

		return player;
	}

	public Entity zombie(float xPos, float yPos) {
		Entity zombie = genericEntity(Assets.zombie_idle, xPos, yPos);

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
		fixtureDef.friction = 0.0f;

		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		physicsComponent.maxVelocity = 30;
		shape.dispose();
		zombie.add(physicsComponent);

		AnimationComponent animation = window.engine.createComponent(AnimationComponent.class);
		ObjectMap<ZombieState, Animation> animationMap = new ObjectMap<>();
		animationMap.put(ZombieState.RunRight, new Animation(0.3f, Assets.zombie_run));
		animationMap.put(ZombieState.RunLeft, new Animation(0.3f, Assets.zombie_run));
		animationMap.put(ZombieState.Die, new Animation(0.3f, Assets.zombie_die));
		animationMap.put(ZombieState.Jump, new Animation(0.2f, Assets.zombie_jump));
		animationMap.put(ZombieState.Land, new Animation(0.3f, Assets.zombie_land));
		animation.animations = animationMap;
		zombie.add(animation);

		ZombieComponent zombieComponent = window.engine.createComponent(ZombieComponent.class);
		zombieComponent.stateMachine = new DefaultStateMachine<>(zombie, ZombieState.Idle, ZombieState.Idle);
		zombie.add(zombieComponent);

		SteeringComponent steeringComponent = window.engine.createComponent(SteeringComponent.class);
		SteerableEntity steerable = new SteerableEntity(zombie);
		steeringComponent.steerable = steerable;
		steeringComponent.steeringBehavior = new Seek<>(steerable);
		steerable.setSteeringBehavior(steeringComponent.steeringBehavior);
		zombie.add(steeringComponent);

		WorldComponent worldComponent = window.engine.createComponent(WorldComponent.class);
		worldComponent.window = window;
		worldComponent.game = game;
		worldComponent.physicsWorld = game.physicsWorld;
		zombie.add(worldComponent);

		zombie.add(window.engine.createComponent(ZombieComponent.class));
		Mappers.tm.get(zombie).texture = Assets.zombie_fall;

		return zombie;
	}

	public void bullet(Entity player) {
		Entity bullet;
		if (Mappers.am.get(player).flipped) {
			bullet = genericEntity(Assets.bullet, Mappers.phm.get(player).physicsBody.getPosition().x - 7, Mappers.phm.get(player).physicsBody.getPosition().y);
			if (!Mappers.tm.get(bullet).texture.isFlipX()) {
				Mappers.tm.get(bullet).texture.flip(true, false);
			}
		} else {
			bullet = genericEntity(Assets.bullet, Mappers.phm.get(player).physicsBody.getPosition().x + 7, Mappers.phm.get(player).physicsBody.getPosition().y);
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

	public Entity background() {
		Entity background = genericEntity(Assets.forest_background, Assets.forest_background.getRegionWidth() / 2, Assets.forest_background.getRegionHeight() / 2);
		Mappers.pm.get(background).z = 0;

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

	public Entity genericEntity(TextureRegion texture, float xPos, float yPos) {
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

	private PolygonShape physicsBodyForObject(Entity object) {
		PolygonShape shape = new PolygonShape();
		Array<Vector2> vertexes = new Array<>(Vector2.class);
		switch (Mappers.dc.get(object).objectType) {
			case trophy:
				vertexes.add(new Vector2(7, 9));
				vertexes.add(new Vector2(9, 2));
				vertexes.add(new Vector2(8, 0));
				vertexes.add(new Vector2(1, 0));
				vertexes.add(new Vector2(0, 1));
				vertexes.add(new Vector2(0, 3));
				vertexes.add(new Vector2(2, 9));
				shape.set(vertexes.toArray());
				offsetShape(shape, new Vector2(Assets.trophy.getRegionWidth() / 2, Assets.trophy.getRegionHeight() / 2));
				break;
			case zombieCorpse:
				vertexes.add(new Vector2(27, 4));
				vertexes.add(new Vector2(27, 2));
				vertexes.add(new Vector2(23, 0));
				vertexes.add(new Vector2(13, 0));
				vertexes.add(new Vector2(8, 3));
				vertexes.add(new Vector2(1, 4));
				shape.set(vertexes.toArray());
				offsetShape(shape, new Vector2(Assets.zombie_corpse.getRegionWidth() / 2, Assets.trophy.getRegionHeight() / 2));
				break;
			case canoe:
				vertexes.add(new Vector2(58, 12));
				vertexes.add(new Vector2(66, 7));
				vertexes.add(new Vector2(69, 0));
				vertexes.add(new Vector2(0, 0));
				vertexes.add(new Vector2(3, 7));
				vertexes.add(new Vector2(12, 12));
				shape.set(vertexes.toArray());
				offsetShape(shape, new Vector2(Assets.canoe.getRegionWidth() / 2, Assets.canoe.getRegionHeight() / 2));
				break;
			default:
				shape.setAsBox(Mappers.sm.get(object).width / 2, Mappers.sm.get(object).height / 2);
				break;
		}
		return shape;
	}

	private void offsetShape(PolygonShape shape, Vector2 offset) {
		Array<Vector2> newVertices = new Array<>(Vector2.class);
		for (int i = 0; i < shape.getVertexCount(); i++) {
			Vector2 vertex = new Vector2();
			shape.getVertex(i, vertex);
			vertex.x -= offset.x;
			vertex.y = offset.y - vertex.y;
			newVertices.add(vertex);
		}
		shape.set(newVertices.toArray());
	}
}
