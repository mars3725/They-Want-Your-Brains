package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.matthewmohandiss.zombiegame.components.*;
import com.matthewmohandiss.zombiegame.systems.ControlSystem;
import com.matthewmohandiss.zombiegame.systems.PhysicsSystem;

/**
 * Created by Matthew on 5/27/16.
 */
public class MainMenu extends ScreenAdapter implements InputProcessor {
	public Entity player;
	Vector3 testPoint = new Vector3();
	private GameLauncher window;
	private World physicsWorld;
	private Box2DDebugRenderer debugRenderer;
	private MouseJointDef mouseJointDef = new MouseJointDef();
	private MouseJoint mouseJoint;
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			if (fixture.testPoint(testPoint.x, testPoint.y) && fixture.getBody() != Mappers.phm.get(player).physicsBody) {
				mouseJointDef.bodyB = fixture.getBody();
				mouseJointDef.target.set(testPoint.x, testPoint.y);
				mouseJoint = (MouseJoint) physicsWorld.createJoint(mouseJointDef);
				return true;
			}
			return false;
		}
	};
	private Vector2 touchLocation = new Vector2();

	public MainMenu(GameLauncher window) {
		Gdx.input.setInputProcessor(this);
		this.window = window;

		initBox2D();
		createBackground();
		createPlayer();
		//createZombie();
		createCrate();
		createBoard();

		ControlSystem controlSystem = new ControlSystem(window.engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first());
		controlSystem.priority = 0;
		window.engine.addSystem(controlSystem);
		PhysicsSystem physicsSystem = new PhysicsSystem(physicsWorld);
		physicsSystem.priority = 1;
		window.engine.addSystem(physicsSystem);
	}

	private void createPlayer() {
		Entity player = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 100;
		position.y = 20;
		position.z = 1;
		player.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.player_idle.getRegionWidth();
		size.height = Assets.player_idle.getRegionHeight();
		player.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.player_idle;
		player.add(texture);

		PlayerComponent playerComponent = window.engine.createComponent(PlayerComponent.class);
		player.add(playerComponent);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.5f;
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 4, size.height / 2);

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

		this.player = player;
		window.engine.addEntity(player);
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		Mappers.cm.get(cam).target = player;
	}

	private void createZombie() {
		Entity zombie = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 50;
		position.y = 20;
		position.z = 1;
		zombie.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.zombie_idle.getRegionWidth();
		size.height = Assets.zombie_idle.getRegionHeight();
		zombie.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.zombie_idle;
		zombie.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.5f;
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 4, size.height / 2);

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
		animation.idleTexture = Assets.zombie_idle;
		animation.jumpTexture = Assets.player_jump;
		animation.fallTexture = Assets.player_fall;
		zombie.add(animation);

		StateComponent state = window.engine.createComponent(StateComponent.class);
		state.set(PlayerState.idle);
		zombie.add(state);

		window.engine.addEntity(zombie);
	}

	private void createCrate() {
		Entity test = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 30;
		position.y = 50;
		position.z = 1;
		test.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.crate.getRegionWidth();
		size.height = Assets.crate.getRegionHeight();
		test.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.crate;
		test.add(texture);

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
		test.add(physicsComponent);

		window.engine.addEntity(test);
	}

	private void createBoard() {
		Entity test = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 50;
		position.y = 75;
		position.z = 1;
		test.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.board.getRegionWidth();
		size.height = Assets.board.getRegionHeight();
		test.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.board;
		test.add(texture);

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
		test.add(physicsComponent);

		window.engine.addEntity(test);
	}

	private void createBackground() {
		Entity background = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = Assets.forest_background.getRegionWidth() / 2;
		position.y = Assets.forest_background.getRegionHeight() / 2;
		position.z = 0;
		background.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.forest_background.getRegionWidth();
		size.height = Assets.forest_background.getRegionHeight();
		background.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.forest_background;
		background.add(texture);

		PhysicsComponent physicsComponent = window.engine.createComponent(PhysicsComponent.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		Body body = physicsWorld.createBody(bodyDef);
		ChainShape loop = new ChainShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-size.width / 2, -size.height / 2);
		vertices[1] = new Vector2(-size.width / 2, size.height / 2);
		vertices[2] = new Vector2(size.width / 2, size.height / 2);
		vertices[3] = new Vector2(size.width / 2, -size.height / 2);
		loop.createLoop(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = loop;
		fixtureDef.friction = 1.5f;
		body.createFixture(fixtureDef);
		physicsComponent.physicsBody = body;
		loop.dispose();
		background.add(physicsComponent);

		window.engine.addEntity(background);
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		Mappers.cm.get(cam).maxX = size.width;
		Mappers.cm.get(cam).maxY = size.height;
	}

	private void initBox2D() {
		Box2D.init();
		physicsWorld = new World(new Vector2(0, -12), true);
		debugRenderer = new Box2DDebugRenderer();

		mouseJointDef.bodyA = physicsWorld.createBody(new BodyDef());
		mouseJointDef.collideConnected = true;
		mouseJointDef.dampingRatio = 10;
		mouseJointDef.maxForce = 20000;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		Entity player = window.engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
		switch (keycode) {
			case 19: //up
				window.engine.getSystem(ControlSystem.class).jump();
				break;
			case 21: //left
				window.engine.getSystem(ControlSystem.class).moveLeft();
				break;
			case 22: //right
				window.engine.getSystem(ControlSystem.class).moveRight();
				break;
			case 62: //space
				window.engine.getSystem(ControlSystem.class).shoot();
				break;
			case 69: //minus
				window.camera.zoom += 0.25;
				break;
			case 70: //plus
				if (window.camera.zoom > 0) {
					window.camera.zoom -= 0.25;
				}
				break;
			default:
				System.out.println("no keybind for keycode " + keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == 21 && Mappers.stm.get(player).state == PlayerState.running) {
			window.engine.getSystem(ControlSystem.class).idle();
		} else if (keycode == 22 && Mappers.stm.get(player).state == PlayerState.running) {
			window.engine.getSystem(ControlSystem.class).idle();
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		testPoint = new Vector3(screenX, screenY, 0);
		window.camera.unproject(testPoint);
		System.out.println("touch at: " + testPoint);
		physicsWorld.QueryAABB(callback, testPoint.x, testPoint.y, testPoint.x, testPoint.y);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (mouseJoint != null) {
			window.camera.unproject(testPoint.set(screenX, screenY, 0));
			mouseJoint.setTarget(touchLocation.set(testPoint.x, testPoint.y));
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (mouseJoint != null) {
			physicsWorld.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		return false;
	}

	@Override
	public void render(float delta) {
		window.engine.update(delta);
		//http://gafferongames.com/game-physics/fix-your-timestep/
		debugRenderer.render(physicsWorld, window.camera.combined);
		physicsWorld.step(1 / 60f, 6, 2);
	}

}
