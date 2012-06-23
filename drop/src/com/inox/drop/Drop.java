package com.inox.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Drop implements ApplicationListener {
	
	Texture dropImage ;
	Texture bucketImage ;
	Sound dropSound ;
	Music rainMusic ;
	OrthographicCamera camera ;
	SpriteBatch batch ;
	Rectangle bucket ;
	Array <Rectangle> raindrops ;
	long lastDropTime ; //To record the last time a drop was spawned (in nanoseconds)
	
	public void spawnRaindrop(){ 	// Function to spawn rain drop at a random position 
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0,800-48);
		raindrop.y = 480 ;
		raindrop.width = 48 ;
		raindrop.height = 48 ;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void create() {		
		
		//load Images 
		dropImage = new Texture(Gdx.files.internal("data/droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("data/bucket.png"));
		
		//loading sound effects
		dropSound = Gdx.audio.newSound(Gdx.files.internal("data/drop.wav")) ;
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("data/rain.mp3")) ;
		
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
		
		//Load raindrops
		raindrops = new Array<Rectangle>() ;
		spawnRaindrop();
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
	    rainMusic.dispose();
	    batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1) ;
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update() ;
		
		//rendering the bucket and raindrop
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops)
			batch.draw(dropImage, raindrop.x, raindrop.y);
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
	
		
		if(TimeUtils.nanoTime()-lastDropTime > 1000000000)
			spawnRaindrop() ;
		
		java.util.Iterator<Rectangle> iter = raindrops.iterator() ;  //Movement of raindrops
		while(iter.hasNext()){
			Rectangle raindrop = iter.next() ;
			raindrop.y -= 200*Gdx.graphics.getDeltaTime();
			if(raindrop.y + 48 < 0)
				iter.remove();
			if(raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
		}
		
		
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
