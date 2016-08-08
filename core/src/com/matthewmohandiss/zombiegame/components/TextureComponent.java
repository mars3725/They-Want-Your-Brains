package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Matthew on 5/27/16.
 */
public class TextureComponent implements Component {
	public TextureRegion texture;
	public ObjectMap textures;
}
