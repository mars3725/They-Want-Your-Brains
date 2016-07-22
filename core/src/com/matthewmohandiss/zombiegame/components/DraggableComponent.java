package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.matthewmohandiss.zombiegame.Enums.Objects;

/**
 * Created by Matthew on 6/27/16.
 */
public class DraggableComponent implements Component {
	public Objects objectType = Objects.error;
	public Array<Entity> navNodes = new Array<>();
}
