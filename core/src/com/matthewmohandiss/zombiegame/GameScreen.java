package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.matthewmohandiss.zombiegame.Enums.CollisionMask;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;
import com.matthewmohandiss.zombiegame.Enums.ZombieState;
import com.matthewmohandiss.zombiegame.components.CameraComponent;
import com.matthewmohandiss.zombiegame.components.PhysicsComponent;
import com.matthewmohandiss.zombiegame.systems.*;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/21/16.
 */
public class GameScreen extends ScreenAdapter implements InputProcessor {
	public Entity player;
	public GameLauncher window;
	public PhysicsWorld physicsWorld;
	public Entity zombie;
	private Box2DDebugRenderer debugRenderer;
	private ArrayList<Entity> entitiesForRemoval = new ArrayList<>();
	private ObjectCreator objectCreator;
	private HUD hud;

	public GameScreen(GameLauncher window) {
		Gdx.input.setInputProcessor(this);
		this.window = window;
		physicsWorld = new PhysicsWorld(this);
		debugRenderer = new Box2DDebugRenderer();
		objectCreator = new ObjectCreator(this);
		hud = new HUD(window, this);
		Entity cam = window.engine.getEntitiesFor(Family.all(CameraComponent.class).get()).first();

		Entity background = objectCreator.background();
		window.engine.addEntity(background);
		Mappers.cm.get(cam).maxX = Mappers.sm.get(background).width;
		Mappers.cm.get(cam).maxY = Mappers.sm.get(background).height;

		physicsWorld.setWorldSize(Mappers.cm.get(cam).maxX, Mappers.cm.get(cam).maxY);

		player = objectCreator.player(150, 30);
		window.engine.addEntity(player);
		Mappers.cm.get(cam).target = player;

		Mappers.plm.get(player).stateMachine = new DefaultStateMachine<>(player, PlayerState.Idle);

		PhysicsSystem physicsSystem = new PhysicsSystem(physicsWorld.world);
		physicsSystem.priority = 1;
		window.engine.addSystem(physicsSystem);

		FSMSystem fsmSystem = new FSMSystem();
		fsmSystem.priority = 2;
		window.engine.addSystem(fsmSystem);

		NavMeshSystem navMeshSystem = new NavMeshSystem(physicsWorld, window);
		navMeshSystem.priority = 5;
		window.engine.addSystem(navMeshSystem);

		NavDebuggerSystem navDebuggerSystem = new NavDebuggerSystem();
		navDebuggerSystem.priority = 6;
		window.engine.addSystem(navDebuggerSystem);

		SteeringSystem steeringSystem = new SteeringSystem();
		steeringSystem.priority = 7;
		window.engine.addSystem(steeringSystem);

		play();
	}

	private void play() {
		Entity crate = objectCreator.crate(70, 20);
		window.engine.addEntity(crate);
		window.engine.getSystem(NavMeshSystem.class).addObjectToMesh(crate);

//		Entity canoe = objectCreator.canoe(100, 20);
//		window.engine.addEntity(canoe);
//		window.engine.getSystem(NavMeshSystem.class).addObjectToMesh(canoe);
//
//		Entity trophy = objectCreator.trophy(50, 20);
//		window.engine.addEntity(trophy);
//		window.engine.getSystem(NavMeshSystem.class).addObjectToMesh(trophy);
//
//		Entity zombieCorpse = objectCreator.zombieCorpse(20, 50);
//		window.engine.addEntity(zombieCorpse);
//		window.engine.getSystem(NavMeshSystem.class).addObjectToMesh(zombieCorpse);

		Entity zombie = objectCreator.zombie(50, 25);
		Mappers.str.get(zombie).target = player;
		Mappers.zm.get(zombie).stateMachine = new DefaultStateMachine<>(zombie, ZombieState.Fall, ZombieState.Global);
		window.engine.addEntity(zombie);
		this.zombie = zombie;

//		ImmutableArray<Entity> objects = window.engine.getEntitiesFor(Family.all(DraggableComponent.class).get());
//		window.engine.getSystem(NavMeshSystem.class).createNavMesh(objects);
		hud.createDevHUD();
	}

	public void beginContactResolver(Fixture fixtureA, Fixture fixtureB) {
		int maskA = fixtureA.getFilterData().categoryBits;
		int maskB = fixtureB.getFilterData().categoryBits;
		Entity entityA = getEntityForPhysicsBody(fixtureA.getBody());
		Entity entityB = getEntityForPhysicsBody(fixtureB.getBody());

		if (maskA == CollisionMask.zombie_body.ordinal() && (maskB == CollisionMask.player_right.ordinal() || maskB == CollisionMask.player_left.ordinal())) {
			Mappers.zm.get(entityA).stateMachine.changeState(ZombieState.Attack);
		}

		if (maskA == CollisionMask.zombie_body.ordinal() && maskB == CollisionMask.bullet.ordinal()) {
			if (!Mappers.zm.get(entityA).stateMachine.isInState(ZombieState.Die)) {
				Mappers.zm.get(entityA).stateMachine.changeState(ZombieState.Die);
			}
			entitiesForRemoval.add(entityB);
		}
	}

	public void endContactResolver(Fixture fixtureA, Fixture fixtureB) {
		int maskA = fixtureA.getFilterData().categoryBits;
		int maskB = fixtureB.getFilterData().categoryBits;
		Entity entityA = getEntityForPhysicsBody(fixtureA.getBody());
		Entity entityB = getEntityForPhysicsBody(fixtureB.getBody());

		if (maskA == CollisionMask.zombie_body.ordinal() && (maskB == CollisionMask.player_right.ordinal() || maskB == CollisionMask.player_left.ordinal())) {
			Mappers.zm.get(entityA).stateMachine.revertToPreviousState();
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
		StateMachine<Entity, PlayerState> stateMachine = Mappers.plm.get(player).stateMachine;
		switch (keycode) {
			case 19: //up
				if (stateMachine.isInState(PlayerState.Idle) || stateMachine.isInState(PlayerState.RunLeft) || stateMachine.isInState(PlayerState.RunRight)) {
					stateMachine.changeState(PlayerState.Jump);
				}
				break;
			case 21: //left
				if (stateMachine.isInState(PlayerState.Idle) || stateMachine.isInState(PlayerState.RunRight)) {
					stateMachine.changeState(PlayerState.RunLeft);
				}
				break;
			case 22: //right
				if (stateMachine.isInState(PlayerState.Idle) || stateMachine.isInState(PlayerState.RunLeft)) {
					stateMachine.changeState(PlayerState.RunRight);
				}
				break;
			case 62: //space
				stateMachine.changeState(PlayerState.Shoot);
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
		StateMachine<Entity, PlayerState> stateMachine = Mappers.plm.get(player).stateMachine;
		if (stateMachine.isInState(PlayerState.RunLeft) || stateMachine.isInState(PlayerState.RunRight)) {
			stateMachine.changeState(PlayerState.Idle);
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

	public void removeEntity(Entity entity) {
		entitiesForRemoval.add(entity);
	}
}
