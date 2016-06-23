package com.matthewmohandiss.zombiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
	public static TextureRegion player_idle;
	public static TextureRegion player_jump;
	public static TextureRegion player_fall;
	public static Array<TextureAtlas.AtlasRegion> player_run;
	public static Array<TextureAtlas.AtlasRegion> player_shoot;
	public static Array<TextureAtlas.AtlasRegion> zombie_run;
	public static TextureRegion zombie_idle;
	public static TextureRegion crate;
	public static TextureRegion log;
	public static TextureRegion bullet;
	public static TextureRegion trophy;

	//	public static Music music;
	public static Sound clickSound;

	public static BitmapFont font;

	public static void load() {
		TexturePacker.process(Gdx.files.getLocalStoragePath(), Gdx.files.getLocalStoragePath().concat("/pack"), "pack");
		pack = new TextureAtlas(Gdx.files.internal("pack/pack.atlas"));

		background = loadTexture("background");
		health = loadTexture("health");
		mountains = loadTexture("mountains");
		pause = loadTexture("pause");
		error = loadTexture("error");
		test = loadTexture("test");
		player_idle = loadTexture("player_idle");
		player_jump = loadTexture("player_jump");
		player_run = pack.findRegions("player_run");
		player_shoot = pack.findRegions("player_shoot");
		zombie_run = pack.findRegions("zombie_run");
		zombie_idle = loadTexture("zombie_idle");
		player_fall = loadTexture("player_fall");
		forest_background = loadTexture("forest_background");
		crate = loadTexture("crate");
		log = loadTexture("log");
		bullet = loadTexture("bullet");
		trophy = loadTexture("trophy");

		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		//parameter.size = 23;
		font = generator.generateFont(parameter);
		font.setColor(Color.GRAY);
	}

	public static TextureRegion loadTexture (String file) {
		return new TextureRegion(pack.findRegion(file));
	}

	public static void playSound (Sound sound) {
		sound.play(1);
	}

}

