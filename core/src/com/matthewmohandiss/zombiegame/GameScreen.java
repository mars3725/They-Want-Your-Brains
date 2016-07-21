package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Timer;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.DraggableComponent;
import com.matthewmohandiss.zombiegame.components.PhysicsComponent;
import com.matthewmohandiss.zombiegame.components.PlayerComponent;
import com.matthewmohandiss.zombiegame.systems.ControlSystem;
import com.matthewmohandiss.zombiegame.systems.NavDebuggerSystem;
import com.matthewmohandiss.zombiegame.systems.NavMeshSystem;
import com.matthewmohandiss.zombiegame.systems.PhysicsSystem;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/21/16.
 */
public class GameScreen extends ScreenAdapter implements InputProcessor {
	public Entity player;
	public GameLauncher window;
	private PhysicsWorld physicsWorld;
	private Box2DDebugRenderer debugRenderer;
	private ArrayList<Entity> entitiesForRemoval = new ArrayList<>();
	private ObjectCreator objectCreator;
	private HUD hud;

	public GameScreen(GameLauncher window) {
		Gdx.input.setInputProcessor(this);
		this.window = window;
		physicsWorld = new PhysicsWorld(this);
		debugRenderer = new Box2DDebugRenderer();
		objectCreator = new ObjectCreator(window, physicsWorld.world);
		hud = new HUD(window, this);
		hud.createDevHUD();
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();

		Entity background = objectCreator.background();
		window.engine.addEntity(background);
		Mappers.cm.get(cam).maxX = Mappers.sm.get(background).width;
		Mappers.cm.get(cam).maxY = Mappers.sm.get(background).height;

		player = objectCreator.player(150, 30);
		window.engine.addEntity(player);
		Mappers.cm.get(cam).target = player;

		ControlSystem controlSystem = new ControlSystem(window.engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first());
		controlSystem.priority = 7;
		window.engine.addSystem(controlSystem);
		PhysicsSystem physicsSystem = new PhysicsSystem(physicsWorld.world);
		physicsSystem.priority = 1;
		window.engine.addSystem(physicsSystem);
		NavMeshSystem navMeshSystem = new NavMeshSystem(physicsWorld, window);
		navMeshSystem.priority = 5;
		window.engine.addSystem(navMeshSystem);
		NavDebuggerSystem navDebuggerSystem = new NavDebuggerSystem();
		navDebuggerSystem.priority = 6;
		window.engine.addSystem(navDebuggerSystem);

		play();
	}

	private void play() {
		window.engine.addEntity(objectCreator.crate(20, 20));
		window.engine.addEntity(objectCreator.canoe(100, 50));
		window.engine.addEntity(objectCreator.trophy(100, 50));
		//window.engine.addEntity(objectCreator.zombieCorpse(100, 50));

		ImmutableArray<Entity> objects = window.engine.getEntitiesFor(Family.all(DraggableComponent.class).get());
		window.engine.getSystem(NavMeshSystem.class).createNavMesh(objects);
	}

	public void contactResolver(final Entity entityA, Entity entityB) {
		if (Mappers.zm.get(entityA) != null && Mappers.bm.get(entityB) != null) {
			Mappers.stm.get(entityA).set(PlayerState.dying);
			Mappers.am.get(entityA).loop = false;
			entitiesForRemoval.add(entityB);

			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					window.engine.addEntity(objectCreator.zombieCorpse(Mappers.pm.get(entityA).x, Mappers.pm.get(entityA).y));
					entitiesForRemoval.add(entityA);
				}
			}, 1.5f);
		}
	}

	public Entity getEntityForPhysicsBody(Body body) { //replace with physicsBody.getUserData()
		for (Entity entity :
				window.engine.getEntitiesFor(Family.all(PhysicsComponent.class).get())) {
			if (body == Mappers.phm.get(entity).physicsBody) {
				return entity;
			}
		}
		//System.out.println("failed to find body");
		return null;
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
				objectCreator.bullet(player);
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
		physicsWorld.touchPoint.set(screenX, screenY, 0);
		window.camera.unproject(physicsWorld.touchPoint);
		physicsWorld.touch(physicsWorld.touchPoint);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (physicsWorld.mouseJoint != null) {
			window.camera.unproject(physicsWorld.touchPoint.set(screenX, screenY, 0));
			physicsWorld.mouseJoint.setTarget(physicsWorld.location.set(physicsWorld.touchPoint.x, physicsWorld.touchPoint.y));
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (physicsWorld.mouseJoint != null) {
			physicsWorld.world.destroyJoint(physicsWorld.mouseJoint);
			physicsWorld.mouseJoint = null;
		}
		return false;
	}

	@Override
	public void render(float delta) {
		window.engine.update(delta);
		//http://gafferongames.com/game-physics/fix-your-timestep/
		debugRenderer.render(physicsWorld.world, window.camera.combined);
		physicsWorld.world.step(1 / 60f, 6, 2);
		removeEntities();
		hud.updateDevHUD();
	}

	private void removeEntities() {
		for (Entity entity :
				entitiesForRemoval) {
			if (Mappers.phm.get(entity) != null) {
				physicsWorld.world.destroyBody(Mappers.phm.get(entity).physicsBody);
			}
			window.engine.removeEntity(entity);
		}
		entitiesForRemoval.clear();
	}
}
