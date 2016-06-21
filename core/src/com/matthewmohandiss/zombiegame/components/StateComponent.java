package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;

/**
 * Created by Matthew on 5/31/16.
 */
public class StateComponent implements Component {

	public float time = 0.0f;
	public PlayerState state;

	public void set(PlayerState newState) {
		this.state = newState;
		time = 0.0f;
	}
}
