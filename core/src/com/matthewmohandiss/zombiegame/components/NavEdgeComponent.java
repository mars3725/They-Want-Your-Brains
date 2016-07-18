package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Matthew on 6/30/16.
 */
public class NavEdgeComponent implements Component {
	public Entity startingNode;
	public Entity endingNode;
	public boolean viable = true;
}
