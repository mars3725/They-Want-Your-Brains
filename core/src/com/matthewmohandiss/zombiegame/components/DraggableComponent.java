package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.matthewmohandiss.zombiegame.Enums.Objects;

import java.util.ArrayList;

/**
 * Created by Matthew on 6/27/16.
 */
public class DraggableComponent implements Component {
	public Objects objectType = Objects.error;
	public ArrayList<Entity> navNodes = new ArrayList<>();
}
