package de.nichtsroffler.dynamicworld.collectable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import de.nichtsroffler.dynamicworld.core.DynamicEntity;
import de.nichtsroffler.dynamicworld.core.ToSpawnObjectDefinition;
import de.nichtsroffler.screens.PlayScreen;

public class CollectableManager {

    private final PlayScreen playScreen;
    private final Queue<ToSpawnObjectDefinition<? extends Collectable>> queuedEntities;
    private final Array<Collectable> inWorldObjects;
    private final int MAX_IN_WORLD = 64;
    private final int MAX_QUEUED_ENTITIES = 128;

    private int activeCount = 0;
    private int initCount = 0;


    public CollectableManager(PlayScreen playScreen) {
        this.playScreen = playScreen;

        inWorldObjects = new Array<>(MAX_IN_WORLD);
        queuedEntities = new Queue<>(MAX_QUEUED_ENTITIES);
    }

    public void update(float dt, Camera camera) {

        if (!queuedEntities.isEmpty()) {
            if (inWorldObjects.size < MAX_IN_WORLD) {
                ToSpawnObjectDefinition<? extends DynamicEntity> currentSpawnDef = queuedEntities.first();
                queuedEntities.removeFirst();

                if (currentSpawnDef.getBlueprint() == BlueDimaond.class) {
                    BlueDimaond blueDimaond = new BlueDimaond(playScreen.getWorld(), currentSpawnDef.getRectangle().x * 100, currentSpawnDef.getRectangle().y * 100);

                    inWorldObjects.add(blueDimaond);
                }
                if (currentSpawnDef.getBlueprint() == DynamicCoin.class) {
                    DynamicCoin dynamicCoin = new DynamicCoin(playScreen.getWorld(), currentSpawnDef.getRectangle().x * 100, currentSpawnDef.getRectangle().y * 100);

                    inWorldObjects.add(dynamicCoin);
                }


                System.out.println("spawned Diamond at" + currentSpawnDef.getRectangle().x + " " + currentSpawnDef.getRectangle().y);
            }
        }

        float camCordsLeft = camera.position.x - camera.viewportWidth / 2f;
        float camCordsRight = camera.position.x + camera.viewportWidth / 2f;


        for (int i = 0; i < inWorldObjects.size ; i++) {

            Collectable collectable = inWorldObjects.get(i);

            if (collectable.isDestroyed() && !collectable.isInAnimation()) {
                inWorldObjects.removeIndex(i);
                activeCount--;
                return;
            }

            if (collectable.isFreeze()) {
                if (collectable.getPositionInWorld() > camCordsLeft && collectable.getPositionInWorld() < camCordsRight) {
                    collectable.unFreeze();
                    activeCount++;
                }
            }
            if (!collectable.isFreeze()) {
                if (collectable.getPositionInWorld() < camCordsLeft || collectable.getPositionInWorld() > camCordsRight) {
                    collectable.freeze();
                    activeCount--;
                }
            }

            collectable.update(dt);

        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (Collectable collectable : inWorldObjects) {
            if (!collectable.isFreeze() || collectable.isInAnimation()) {
                collectable.render(spriteBatch);
            }
        }
    }

    public Collectable createDiamondAndRegister(float x, float y) {
        Collectable dynamicCoin = new Collectable(playScreen.getWorld(), x,y);

        inWorldObjects.add(dynamicCoin);
        initCount++;

        return dynamicCoin;
    }

    public void aSyncSpawn(Class<? extends Collectable> className, Rectangle rectangle) {
        queuedEntities.addLast(new ToSpawnObjectDefinition<>(className, rectangle));
    }

    public int getActiveCount() {
        return activeCount;
    }

    public int getCount() {
        return inWorldObjects.size;
    }

    public int getSize() {
        return MAX_IN_WORLD;
    }

    public int getQueued() {
        return queuedEntities.size;
    }

    public int getQueuedMaxSize() {
        return MAX_QUEUED_ENTITIES;
    }

    public int getInitCount() {
        return initCount;
    }
}
