package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Matthew on 5/31/16.
 */

public class AnimationComponent implements Component {
	public IntMap<Animation> animations = new IntMap<Animation>();
	public boolean flipped = false;
	public boolean loop = true;
}

