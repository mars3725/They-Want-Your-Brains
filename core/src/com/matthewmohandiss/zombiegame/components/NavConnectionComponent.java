package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavConnectionComponent implements Component {
	public Vector2 startingPosition;
	public Vector2 endingPosition;
}
