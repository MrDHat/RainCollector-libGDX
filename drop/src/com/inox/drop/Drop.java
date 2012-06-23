package com.inox.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Drop implements ApplicationListener {
	
	Texture dropImage ;
	Texture bucketImage ;
	Sound dropSound ;
	Music rainMusic ;
	OrthographicCamera camera ;
	SpriteBatch batch ;
	Rectangle bucket ;
	
	@Override
	public void create() {		
		
		//load Images 
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		//loading sound effects
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav")) ;
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3")) ;
		
		//Starting the playback of rainMusic
		rainMusic.setLooping(true);
		rainMusic.play();
		
		//Setting up camera
		camera = new OrthographicCamera() ;
		camera.setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();

		bucket = new Rectangle() ;
		bucket.x =  800/2 - 48/2 ;
		bucket.y = 20 ;
		bucket.width = 48 ;
		bucket.height = 48;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1) ;
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update() ;
		
		//rendering the bucket
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();
		
		//Bucket movement
		if(Gdx.input.isTouched()){
			Vector3 touchpos = new Vector3();
			touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchpos);
			bucket.x = touchpos.x - 48/2;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			bucket.x -= 200*Gdx.graphics.getDeltaTime(); //Constant speed = 200
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			bucket.x += 200*Gdx.graphics.getDeltaTime();
		}
		
		if(bucket.x < 0)
			bucket.x = 0;
		if(bucket.y > 800 - 48)
			bucket.x = 800-48 ;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
