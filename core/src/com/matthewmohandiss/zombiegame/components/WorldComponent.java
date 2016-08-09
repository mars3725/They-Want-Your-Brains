package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.matthewmohandiss.zombiegame.GameLauncher;
import com.matthewmohandiss.zombiegame.GameScreen;
import com.matthewmohandiss.zombiegame.PhysicsWorld;

/**
 * Created by Matthew on 8/8/16.
 */
public class WorldComponent implements Component {
	public GameLauncher window;
	public GameScreen game;
	public PhysicsWorld physicsWorld;
}
