package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.matthewmohandiss.zombiegame.Assets;

/**
 * Created by Matthew on 5/27/16.
 */
public class TextureComponent implements Component {
	public TextureRegion texture = Assets.error;
}
