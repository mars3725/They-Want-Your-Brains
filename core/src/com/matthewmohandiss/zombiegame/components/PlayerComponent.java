package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Matthew on 5/28/16.
 */
public class PlayerComponent implements Component {
	enum state {
		running, jumping, falling, idle, shooting
	}
}
