package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Matthew on 5/31/16.
 */

public class AnimationComponent implements Component {
	public ObjectMap animations;
	public Animation activeAnimation = null;
	public float runningTime = 0;
	public boolean flipped = false;
	public boolean loop = true;
}

