package com.matthewmohandiss.zombiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Matthew on 5/26/16.
 */
public class Assets {
	public static TextureAtlas pack;
	public static TextureRegion background;
	public static TextureRegion forest_background;
	public static TextureRegion health;
	public static TextureRegion mountains;
	public static TextureRegion pause;
	public static TextureRegion error;
	public static TextureRegion test;

	public static Array<TextureAtlas.AtlasRegion> player_run;
	public static Array<TextureAtlas.AtlasRegion> player_shoot;
	public static Array<TextureAtlas.AtlasRegion> player_down;
	public static TextureRegion player_idle;
	public static TextureRegion player_jump;
	public static TextureRegion player_fall;

	public static Array<TextureAtlas.AtlasRegion> zombie_run;
	public static Array<TextureAtlas.AtlasRegion> zombie_die;
	public static Array<TextureAtlas.AtlasRegion> zombie_land;
	public static Array<TextureAtlas.AtlasRegion> zombie_jump;
	public static Array<TextureAtlas.AtlasRegion> zombie_attack;
	public static TextureRegion zombie_fall;
	public static TextureRegion zombie_idle;

	public static TextureRegion crate;
	public static TextureRegion log;
	public static TextureRegion bullet;
	public static TextureRegion trophy;
	public static TextureRegion zombie_corpse;
	public static TextureRegion canoe;

	//	public static Music music;
	public static Sound clickSound;

	public static BitmapFont Gray16ptFont;
	public static BitmapFont Gray12ptFont;

	public static void load() {
		TexturePacker.process(Gdx.files.getLocalStoragePath().concat("/textures/unpacked"), Gdx.files.getLocalStoragePath().concat("/textures/packed"), "pack");
		pack = new TextureAtlas(Gdx.files.internal("textures/packed/pack.atlas"));

		background = loadTexture("background");
		forest_background = loadTexture("forest_background");
		health = loadTexture("health");
		mountains = loadTexture("mountains");
		pause = loadTexture("pause");
		error = loadTexture("error");
		test = loadTexture("test");

		player_idle = loadTexture("player_idle");
		player_jump = loadTexture("player_jump");
		player_run = pack.findRegions("player_run");
		player_shoot = pack.findRegions("player_shoot");
		player_fall = loadTexture("player_fall");
		player_down = pack.findRegions("player_down");

		zombie_die = pack.findRegions("zombie_die");
		zombie_run = pack.findRegions("zombie_run");
		zombie_idle = loadTexture("zombie_idle");
		zombie_fall = loadTexture("zombie_fall");
		zombie_land = pack.findRegions("zombie_land");
		zombie_jump = pack.findRegions("zombie_jump");
		zombie_attack = pack.findRegions("zombie_attack");

		crate = loadTexture("crate");
		log = loadTexture("log");
		bullet = loadTexture("bullet");
		trophy = loadTexture("trophy");
		zombie_corpse = loadTexture("zombie_corpse");
		canoe = loadTexture("canoe");

		clickSound = Gdx.audio.newSound(Gdx.files.internal("Sound/click.wav"));

		SmartFontGenerator fontGen = new SmartFontGenerator();

		Gray16ptFont = fontGen.createFont(Gdx.files.internal("fonts/font.ttf"), "Gray", 14);
		Gray16ptFont.setColor(Color.GRAY);

		Gray12ptFont = fontGen.createFont(Gdx.files.internal("fonts/font.ttf"), "Red", 14);
		Gray12ptFont.setColor(Color.RED);
	}

	public static TextureRegion loadTexture (String file) {
		return new TextureRegion(pack.findRegion(file));
	}

	public static void playSound (Sound sound) {
		sound.play(1);
	}

}

