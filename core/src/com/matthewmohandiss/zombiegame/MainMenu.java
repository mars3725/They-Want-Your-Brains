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
import com.matthewmohandiss.zombiegame.components.*;
import com.matthewmohandiss.zombiegame.systems.ControlSystem;
import com.matthewmohandiss.zombiegame.systems.PhysicsSystem;

/**
 * Created by Matthew on 5/27/16.
 */
public class MainMenu extends ScreenAdapter implements InputProcessor {
	private GameLauncher window;
	private World physicsWorld;
	private Box2DDebugRenderer debugRenderer;
	public Entity player;

	public MainMenu(GameLauncher window) {
		Gdx.input.setInputProcessor(this);
		this.window = window;

		initBox2D();
		createPlayer();
		createBackground();
		createTest();

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
		position.x = 30;
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
		Body body = physicsWorld.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.25f;
		fixtureDef.friction = 0.6f;

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
		position.x = 200;
		position.y = 50;
		position.z = 1;
		zombie.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.player_run.first().getRegionWidth();
		size.height = Assets.player_run.first().getRegionHeight();
		zombie.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.player_idle;
		zombie.add(texture);

		AnimationComponent animation = window.engine.createComponent(AnimationComponent.class);
		animation.animations.put(PlayerState.running.ordinal(), new Animation(0.15f, Assets.zombie_run));
		zombie.add(animation);

		StateComponent state = window.engine.createComponent(StateComponent.class);
		state.set(PlayerState.idle);
		zombie.add(state);

		this.player = zombie;
		window.engine.addEntity(zombie);
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		Mappers.cm.get(cam).target = zombie;
	}

	private void createTest() {
		Entity test = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 50;
		position.y = 50;
		position.z = 1;
		test.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.test.getRegionWidth();
		size.height = Assets.test.getRegionHeight();
		test.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.test;
		test.add(texture);

		window.engine.addEntity(test);
	}

	private void createBackground() {
		Entity background = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = Assets.background.getRegionWidth()/2;
		position.y = Assets.background.getRegionHeight()/2;
		position.z = 0;
		background.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.background.getRegionWidth();
		size.height = Assets.background.getRegionHeight();
		background.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.background;
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
		body.createFixture(loop, 0.0f);
		physicsComponent.physicsBody = body;
		loop.dispose();
		background.add(physicsComponent);

		window.engine.addEntity(background);
	}

	private void initBox2D() {
		Box2D.init();
		physicsWorld = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
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
		Vector3 touch = new Vector3();
		Vector3 location = window.camera.unproject(touch.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		System.out.println("touch at (" + location.x + ", " + location.y + ")");
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public void render(float delta) {
		window.engine.update(delta);
		//http://gafferongames.com/game-physics/fix-your-timestep/
		debugRenderer.render(physicsWorld, window.camera.combined);
		physicsWorld.step(1/60f, 6, 2);
	}

}
