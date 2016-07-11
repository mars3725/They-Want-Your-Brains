package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavNodeComponent implements Component {
	public int id = -1;
	public Body body;
	public Vector2 original;
}
