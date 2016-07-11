package com.matthewmohandiss.zombiegame;

import com.badlogic.ashley.core.ComponentMapper;
import com.matthewmohandiss.zombiegame.components.*;

/**
 * Created by Matthew on 5/27/16.
 */
public class Mappers {
	public static ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
	public static ComponentMapper<SizeComponent> sm = ComponentMapper.getFor(SizeComponent.class);
	public static ComponentMapper<CameraComponent> cm = ComponentMapper.getFor(CameraComponent.class);
	public static ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
	public static ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
	public static ComponentMapper<PhysicsComponent> phm = ComponentMapper.getFor(PhysicsComponent.class);
	public static ComponentMapper<HUDComponent> hm = ComponentMapper.getFor(HUDComponent.class);
	public static ComponentMapper<TextComponent> txm = ComponentMapper.getFor(TextComponent.class);
	public static ComponentMapper<BulletComponent> bm = ComponentMapper.getFor(BulletComponent.class);
	public static ComponentMapper<ZombieComponent> zm = ComponentMapper.getFor(ZombieComponent.class);
	public static ComponentMapper<PlayerComponent> plm = ComponentMapper.getFor(PlayerComponent.class);
	public static ComponentMapper<DraggableComponent> dc = ComponentMapper.getFor(DraggableComponent.class);
	public static ComponentMapper<NavNodeComponent> nnc = ComponentMapper.getFor(NavNodeComponent.class);
	public static ComponentMapper<NavConnectionComponent> ncc = ComponentMapper.getFor(NavConnectionComponent.class);
}
