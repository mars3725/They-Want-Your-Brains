package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.matthewmohandiss.zombiegame.Enums.ZombieState;

/**
 * Created by Matthew on 6/3/16.
 */
public class ZombieComponent implements Component {
	public StateMachine<Entity, ZombieState> stateMachine;
}
