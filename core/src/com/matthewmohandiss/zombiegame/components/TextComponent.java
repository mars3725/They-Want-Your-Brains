package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.matthewmohandiss.zombiegame.Assets;

/**
 * Created by Matthew on 6/22/16.
 */
public class TextComponent implements Component {
	public String text = "Default Text";
	public BitmapFont font = Assets.font;
}
