package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.matthewmohandiss.zombiegame.SteerableEntity;

/**
 * Created by Matthew on 7/26/16.
 */
public class SteeringComponent implements Component {
	public SteerableEntity steerable;
	public Seek seekBehavior;
	public SteeringBehavior<Vector2> steeringBehavior;
	public Entity target;
}
