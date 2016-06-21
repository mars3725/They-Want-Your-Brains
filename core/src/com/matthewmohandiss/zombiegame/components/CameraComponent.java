package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Matthew on 5/27/16.
 */
public class CameraComponent implements Component {
	public OrthographicCamera camera;
	public Entity target = null;
	public float maxX = 1000;
	public float maxY = 1000;
}
