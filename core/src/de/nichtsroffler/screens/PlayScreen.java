package de.nichtsroffler.screens;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import de.nichtsroffler.dynamicworld.collectable.CollectableManager;
import de.nichtsroffler.dynamicworld.core.EntityManager;
import de.nichtsroffler.dynamicworld.players.Player;
import de.nichtsroffler.helper.CameraManager;
import de.nichtsroffler.main.FinalStatics;
import de.nichtsroffler.main.JumpAndRunMain;
import de.nichtsroffler.scene2d.Hud;
import de.nichtsroffler.world.core.AnimatedStaticObject;
import de.nichtsroffler.world.core.EventQueuer;
import de.nichtsroffler.world.core.MapCreator;
import de.nichtsroffler.world.core.MyContactListener;

public class PlayScreen implements Screen {

    private final JumpAndRunMain jumpAndRunMain;

    private final CameraManager cameraManager;
    private final Viewport viewport;

    private final World world;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private final EntityManager entityManager;
    private final CollectableManager collectableManager;

    private final Player player;

    private final Box2DDebugRenderer box2DDebugRenderer;

    private final ArrayList<AnimatedStaticObject> animators = new ArrayList<>();
    private EventQueuer eventQueuer;

    private final Hud hud;

    public PlayScreen(JumpAndRunMain jumpAndRunMain) {
        this.jumpAndRunMain = jumpAndRunMain;

        cameraManager = new CameraManager();
        viewport = new ExtendViewport(FinalStatics.VIRTUAL_WIDTH / PPM, FinalStatics.VIRTUAL_HEIGHT / PPM, cameraManager.getCamera());

        world = new World(new Vector2(0f,-9.81f), true);
        ContactListener contactListener = new MyContactListener(this);
        world.setContactListener(contactListener);

        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        map = tmxMapLoader.load("tmx/0-1.tmx");

        entityManager = new EntityManager(this);
        collectableManager = new CollectableManager(this);

        MapCreator mapCreator = new MapCreator(this);

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map,1f / PPM);
        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(world, mapCreator.getPlayerRectangle().x, mapCreator.getPlayerRectangle().y, mapCreator.getKeyRectangle().x, mapCreator.getKeyRectangle().y);

        animators.addAll(mapCreator.getAnimators());
        eventQueuer = new EventQueuer();

        hud = new Hud(this, jumpAndRunMain.spriteBatch);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        cameraManager.update(getPlayer(), delta);

        player.update(delta);

        entityManager.update(delta);
        collectableManager.update(delta, cameraManager.getCamera());

        for (AnimatedStaticObject animatedStaticObject : animators) {
            animatedStaticObject.update();
        }
        eventQueuer.update(delta);

        hud.update(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0.180f, 0.353f, 0.537f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        orthogonalTiledMapRenderer.setView(cameraManager.getCamera());
        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, cameraManager.getCamera().combined);

        jumpAndRunMain.spriteBatch.setProjectionMatrix(cameraManager.getCamera().combined);
        jumpAndRunMain.spriteBatch.begin();
        player.render(jumpAndRunMain.spriteBatch);
        entityManager.render(jumpAndRunMain.spriteBatch, cameraManager.getCamera());
        collectableManager.render(jumpAndRunMain.spriteBatch);
        jumpAndRunMain.spriteBatch.end();

        hud.renderStage();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    //Getter
    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    //Getter Manager
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public CollectableManager getCollectableManager() {
        return collectableManager;
    }

    public EventQueuer getEventQueuer() {
        return eventQueuer;
    }
}
