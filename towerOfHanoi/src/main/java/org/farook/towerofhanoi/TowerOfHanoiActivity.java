package org.farook.towerofhanoi;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class TowerOfHanoiActivity extends SimpleBaseGameActivity {
	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion, mRing1, mRing2, mRing3;
	private Sprite mTower1, mTower2, mTower3, backgroundSprite;
	private Stack mStack1, mStack2, mStack3;

	private final String PNG_BACKGROUND = "gfx/background.png";
	private final String PNG_RING1 = "gfx/ring1.png";
	private final String PNG_RING2 = "gfx/ring2.png";
	private final String PNG_RING3 = "gfx/ring3.png";
	private final String PNG_TAWER = "gfx/tower.png";

	private final int TABLE_HEIGHT = 52;
	private final int TOWER_1_X_POS = 192;
	private final int TOWER_2_X_POS = 400;
	private final int TOWER_3_X_POS = 604;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// Create camera and set it to display screen at point (0,0) and size (CAMERA_WIDTH,CAMERA_HEIGHT)
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		// Create an Engine and define behavior
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		try {
			// 1 - Set up bitmap textures
			ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open(PNG_BACKGROUND);
				}
			});
			ITexture towerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open(PNG_TAWER);
				}
			});
			ITexture ring1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open(PNG_RING1);
				}
			});
			ITexture ring2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open(PNG_RING2);
				}
			});
			ITexture ring3 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open(PNG_RING3);
				}
			});

			// 2 - Load bitmap textures into VRAM
			backgroundTexture.load();
			towerTexture.load();
			ring1.load();
			ring2.load();
			ring3.load();

			// 3 - Set up texture regions
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			this.mTowerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
			this.mRing1 = TextureRegionFactory.extractFromTexture(ring1);
			this.mRing2 = TextureRegionFactory.extractFromTexture(ring2);
			this.mRing3 = TextureRegionFactory.extractFromTexture(ring3);

			// 4 - Create the stacks to represent the towers
			this.mStack1 = new Stack();
			this.mStack2 = new Stack();
			this.mStack3 = new Stack();

		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	protected Scene onCreateScene() {
		// 1 - Create new scene
		final Scene scene = new Scene();

		// 2 a - Add the towers
		backgroundSprite = new Sprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		mTower1 = new Sprite(TOWER_1_X_POS, CAMERA_HEIGHT / 2 + TABLE_HEIGHT, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower2 = new Sprite(TOWER_2_X_POS, CAMERA_HEIGHT / 2 + TABLE_HEIGHT, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower3 = new Sprite(TOWER_3_X_POS, CAMERA_HEIGHT / 2 + TABLE_HEIGHT, this.mTowerTextureRegion, getVertexBufferObjectManager());

		// 2 b - Add sprites to the scene
		scene.attachChild(backgroundSprite);

		scene.attachChild(mTower1);
		scene.attachChild(mTower2);
		scene.attachChild(mTower3);

		// 3 a - calculate rings initial position
		int tower1xPos = (int) (mTower1.getX());
		int tower3yPos = (int) (mTower1.getY() - mTower1.getHeight() / 2 + this.mRing3.getHeight() / 2);
		int tower2yPos = (int) (mTower1.getY() - mTower1.getHeight() / 2 - 3 + this.mRing3.getHeight() + this.mRing2.getHeight() / 2);
		int tower1yPos = (int) (mTower1.getY() - mTower1.getHeight() / 2 - 6 + this.mRing3.getHeight() + this.mRing2.getHeight() + this.mRing1.getHeight() / 2);

		// 3 b - Create the rings
		Ring ring1 = new Ring(1, tower1xPos, tower1yPos, this.mRing1, getVertexBufferObjectManager()) {
			// Ring is type of Sprint -> type of Entity and has onAreaTouched
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				return OnRingTouch(this, pSceneTouchEvent);
			}
		};
		Ring ring2 = new Ring(2, tower1xPos, tower2yPos, this.mRing2, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				return OnRingTouch(this, pSceneTouchEvent);
			}
		};
		Ring ring3 = new Ring(3, tower1xPos, tower3yPos, this.mRing3, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				return OnRingTouch(this, pSceneTouchEvent);
			}
		};

		// Add rings to the scene
		scene.attachChild(ring1);
		scene.attachChild(ring2);
		scene.attachChild(ring3);

		// 4 - Add all rings to stack one
		this.mStack1.add(ring3);
		this.mStack1.add(ring2);
		this.mStack1.add(ring1);

		// 5 - Initialize starting position for each ring
		ring1.setmStack(mStack1);
		ring2.setmStack(mStack1);
		ring3.setmStack(mStack1);
		ring1.setmTower(mTower1);
		ring2.setmTower(mTower1);
		ring3.setmTower(mTower1);

		// 6 - Add touch handlers
		scene.registerTouchArea(ring1);
		scene.registerTouchArea(ring2);
		scene.registerTouchArea(ring3);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		return scene;
	}

	/**
	 * On touch move the ring and place it on new tower if has collision
	 * @param ring
	 * @param pSceneTouchEvent
	 * @return
	 */
	private boolean OnRingTouch(Ring ring, TouchEvent pSceneTouchEvent)
	{
		if (((Ring) ring.getmStack().peek()).getmWeight() != ring.getmWeight())
			return false;
		// Move according the touch
		ring.setPosition(pSceneTouchEvent.getX() - ring.getWidth() / 2, pSceneTouchEvent.getY() - ring.getHeight() / 2);

		// Drop
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			checkForCollisionsWithTowers(ring);
		}
		return true;
	}

	/**
	 * Check if ring has collision with tower and move it
	 * @param ring
     */
	private void checkForCollisionsWithTowers(Ring ring) {
		Stack stack = null;
		Sprite tower = null;
		// Find the tower you collide with & Make sure this is a legal move
		if (ring.collidesWith(mTower1) && (mStack1.size() == 0 || ring.getmWeight() < ((Ring) mStack1.peek()).getmWeight())) {
			stack = mStack1;
			tower = mTower1;
		} else if (ring.collidesWith(mTower2) && (mStack2.size() == 0 || ring.getmWeight() < ((Ring) mStack2.peek()).getmWeight())) {
			stack = mStack2;
			tower = mTower2;
		} else if (ring.collidesWith(mTower3) && (mStack3.size() == 0 || ring.getmWeight() < ((Ring) mStack3.peek()).getmWeight())) {
			stack = mStack3;
			tower = mTower3;
		} else {
			stack = ring.getmStack();
			tower = ring.getmTower();
		}
		// Update the stacks
		ring.getmStack().remove(ring);

		// Set position
		if (stack != null && tower != null && stack.size() == 0) {
			ring.setPosition(tower.getX(), tower.getY() - tower.getHeight() / 2 + ring.getHeight() / 2);
		} else if (stack != null && tower != null && stack.size() > 0) {
			ring.setPosition(tower.getX(), ((Ring) stack.peek()).getY() + ((Ring) stack.peek()).getHeight() / 2 + ring.getHeight() / 2 - 3);
		}
		stack.add(ring);
		ring.setmStack(stack);
		ring.setmTower(tower);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		/*client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW,
				"TowerOfHanoi Page",
				Uri.parse("http://host/path"),
				Uri.parse("android-app://org.farook.towerofhanoi/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);*/
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		/*Action viewAction = Action.newAction(
				Action.TYPE_VIEW,
				"TowerOfHanoi Page",
				Uri.parse("http://host/path"),
				Uri.parse("android-app://org.farook.towerofhanoi/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();*/
	}
}