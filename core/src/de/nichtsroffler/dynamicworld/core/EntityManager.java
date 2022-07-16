package de.nichtsroffler.dynamicworld.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;

import de.nichtsroffler.dynamicworld.enemy.EnemyDefault;
import de.nichtsroffler.dynamicworld.enemy.EnemySpike;
import de.nichtsroffler.main.NotFinalStatics;
import de.nichtsroffler.screens.PlayScreen;

public class EntityManager implements Disposable {

    private static final int MAX_ENTITIES_IN_WORLD = 16;
    private static final int MAX_QUEUED_ENTITIES = 32;

    private final PlayScreen playScreen;
    private final Array<DynamicEntity> dynamicEntityArrayList;
    private final Queue<ToSpawnObjectDefinition<? extends DynamicEntity>> queuedEntities;

    public EntityManager(PlayScreen playScreen) {
        queuedEntities = new Queue<>(MAX_QUEUED_ENTITIES);
        dynamicEntityArrayList = new Array<>();
        this.playScreen = playScreen;
    }

    public void spawnDynamicEntity(ToSpawnObjectDefinition<? extends DynamicEntity> toSpawnObjectDefinition) {
        if (queuedEntities.size < MAX_QUEUED_ENTITIES) {
            queuedEntities.addLast(toSpawnObjectDefinition);
        }
    }

    private void updateDynamicEntities(float dt) {
        if (!queuedEntities.isEmpty()) {
            if (dynamicEntityArrayList.size < MAX_ENTITIES_IN_WORLD) {

                ToSpawnObjectDefinition<? extends DynamicEntity> currentSpawnDef = queuedEntities.first();
                queuedEntities.removeFirst();

                if (currentSpawnDef.getBlueprint() == EnemyDefault.class) {
                    dynamicEntityArrayList.add(new EnemyDefault(playScreen.getWorld(), currentSpawnDef.getRectangle()));
                    if (NotFinalStatics.debug) {
                        System.out.println("Spawned new Default-Enemy at " + currentSpawnDef.getRectangle().x + " " + currentSpawnDef.getRectangle().y);
                    }
                } else if (currentSpawnDef.getBlueprint() == EnemySpike.class) {
                    dynamicEntityArrayList.add(new EnemySpike(playScreen.getWorld(), currentSpawnDef.getRectangle()));
                    if (NotFinalStatics.debug) {
                        System.out.println("Spawned new Spike-Enemy at " + currentSpawnDef.getRectangle().x + " " + currentSpawnDef.getRectangle().y);
                    }
                }
            }
        }
        for (int i = 0; i < dynamicEntityArrayList.size; i++) {
            dynamicEntityArrayList.get(i).update(dt);
            if (dynamicEntityArrayList.get(i).isDestroyed()) {
                dynamicEntityArrayList.removeIndex(i);
            }
        }
    }

    public void update(float dt) {
        updateDynamicEntities(dt);
    }

    public void render(SpriteBatch spriteBatch, Camera camera) {

        float leftViewPortX = camera.position.x - camera.viewportWidth;
        float rightViewPortX = camera.position.x + camera.viewportWidth;

        for (DynamicEntity entity : dynamicEntityArrayList) {
            if (!(entity.getX() < 0f) && (entity.getX() > leftViewPortX && entity.getX() < rightViewPortX)) {
                entity.render(spriteBatch);
            }
        }
    }

    public int getMaxEntitiesInWorld() {
        return MAX_ENTITIES_IN_WORLD;
    }

    public int getMaxQueuedEntities() {
        return MAX_QUEUED_ENTITIES;
    }

    public int getSpawnedSize() {
        return this.dynamicEntityArrayList.size;
    }

    public int getQueuedSize() {
        return this.queuedEntities.size;
    }

    @Override
    public void dispose() {
        for (DynamicEntity dynamicEntity : dynamicEntityArrayList) {
            dynamicEntity.dispose();
        }
    }
}