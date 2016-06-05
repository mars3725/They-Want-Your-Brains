package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Matthew on 6/3/16.
 */
public class PhysicsComponent implements Component {
	public Body physicsBody;
	public float maxVelocity;
}
