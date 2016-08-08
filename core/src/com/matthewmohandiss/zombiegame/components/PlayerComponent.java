package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.matthewmohandiss.zombiegame.Enums.PlayerState;

/**
 * Created by Matthew on 5/28/16.
 */
public class PlayerComponent implements Component {
	public StateMachine<Entity, PlayerState> stateMachine;
	public int ammo = 10;
}
