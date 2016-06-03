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

		window.engine.addSystem(new ControlSystem(
				window.engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()));
	}

	private void createPlayer() {
		Entity player = window.engine.createEntity();

		PositionComponent position = window.engine.createComponent(PositionComponent.class);
		position.x = 100;
		position.y = 50;
		position.z = 1;
		player.add(position);

		SizeComponent size = window.engine.createComponent(SizeComponent.class);
		size.width = Assets.player_idle.getRegionWidth();
		size.height = Assets.player_idle.getRegionHeight();
		player.add(size);

		TextureComponent texture = window.engine.createComponent(TextureComponent.class);
		texture.texture = Assets.player_idle;
		player.add(texture);

		VelocityComponent velocity = window.engine.createComponent(VelocityComponent.class);
		player.add(velocity);

		PlayerComponent playerComponent = window.engine.createComponent(PlayerComponent.class);
		player.add(playerComponent);

		AnimationComponent animation = window.engine.createComponent(AnimationComponent.class);
		animation.animations.put(PlayerState.running.ordinal(), new Animation(0.15f, Assets.player_run));
		animation.animations.put(PlayerState.jumping.ordinal(), new Animation(0.15f, Assets.player_jump));
		animation.animations.put(PlayerState.shooting.ordinal(), new Animation(0.3f, Assets.player_shoot));
		player.add(animation);

		StateComponent state = window.engine.createComponent(StateComponent.class);
		state.set(PlayerState.idle);
		player.add(state);

		this.player = player;
		window.engine.addEntity(player);
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();
		Mappers.cm.get(cam).target = player;

		BodyDef groundBodyDef = new BodyDef();
		Body groundBody = physicsWorld.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(size.width/2, size.height/2, new Vector2(position.x, position.y), position.rotation);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();
		groundBody.setUserData(player);
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

		VelocityComponent velocity = window.engine.createComponent(VelocityComponent.class);
		zombie.add(velocity);

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

		BodyDef groundBodyDef = new BodyDef();
		Body groundBody = physicsWorld.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(size.width, size.height, new Vector2(position.x, position.y), position.rotation);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();
		groundBody.setUserData(test);
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

		window.engine.addEntity(background);
	}

	private void initBox2D() {
		Box2D.init();
		physicsWorld = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();

		BodyDef groundBodyDef =new BodyDef();
		groundBodyDef.position.set(new Vector2(10, 0));
		Body groundBody = physicsWorld.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(10, 0);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();
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
