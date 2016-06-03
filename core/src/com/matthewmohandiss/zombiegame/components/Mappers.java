package com.matthewmohandiss.zombiegame.components;

import com.badlogic.ashley.core.ComponentMapper;

/**
 * Created by Matthew on 5/27/16.
 */
public class Mappers {
	public static ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	public static ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
	public static ComponentMapper<SizeComponent> sm = ComponentMapper.getFor(SizeComponent.class);
	public static ComponentMapper<CameraComponent> cm = ComponentMapper.getFor(CameraComponent.class);
	public static ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
	public static ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
}
