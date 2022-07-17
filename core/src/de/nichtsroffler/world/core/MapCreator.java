package de.nichtsroffler.world.core;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import de.nichtsroffler.dynamicworld.core.ToSpawnObjectDefinition;
import de.nichtsroffler.dynamicworld.enemy.EnemyDefault;
import de.nichtsroffler.dynamicworld.enemy.EnemySpike;
import de.nichtsroffler.main.NotFinalStatics;
import de.nichtsroffler.screens.PlayScreen;
import de.nichtsroffler.world.DefaultQuestionBlock;
import de.nichtsroffler.world.Ground;
import de.nichtsroffler.world.InteractiveTileObject;
import de.nichtsroffler.world.LockQuestionBlock;
import de.nichtsroffler.world.OneWayGround;
import de.nichtsroffler.world.StaticCoin;

public class MapCreator {

    private TiledMap tiledMap;
    private World world;
    private PlayScreen playScreen;
    private Rectangle playerRectangle, keyRectangle;
    private ArrayList<AnimatedStaticObject> animators = new ArrayList<>();

    public MapCreator(PlayScreen playScreen) {
        this.playScreen = playScreen;

        tiledMap = playScreen.getMap();
        world = playScreen.getWorld();

        playerRectangle = new Rectangle(100,100,0,0);
        keyRectangle = new Rectangle(0,0,0,0);

        createWorld();
    }

    private void createWorld() {
        //HitBox-Layer
        //Rectangle-Shapes
        for (RectangleMapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            if (mapObject.getName() != null) {
                if (mapObject.getName().equalsIgnoreCase("OneWay"))
                    new OneWayGround(world, mapObject.getRectangle());
            } else {
                new Ground(world, mapObject.getRectangle());
            }
        }

        //HitBox-Layer
        //Polygon-Shapes
        for (PolygonMapObject mapObject : tiledMap.getLayers().get(3).getObjects().getByType(PolygonMapObject.class)) {

            float[] vertices = mapObject.getPolygon().getTransformedVertices();

            new Ground(world, vertices);

        }

        //HitBox-Layer
        //Action-Blocks
        for (RectangleMapObject mapObject : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            if (mapObject.getName() != null) {
                if (mapObject.getName().equalsIgnoreCase("lock")){
                    new LockQuestionBlock(world, tiledMap, mapObject.getRectangle());
                } else {
                    new DefaultQuestionBlock(world, tiledMap, mapObject.getRectangle());
                }
            }
        }

        //HitBox-Layer
        //Entities EnemyDefault
        for (RectangleMapObject mapObject : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            if (mapObject.getName() != null) {

                if (mapObject.getName().equalsIgnoreCase("Key")) {
                    keyRectangle = mapObject.getRectangle();
                    System.out.println("found key");

                } else if (mapObject.getName().equalsIgnoreCase("Player")) {
                    playerRectangle = mapObject.getRectangle();

                } else if (mapObject.getName().equalsIgnoreCase("EnemyDefault")) {
                    if (NotFinalStatics.debug) {
                        System.out.println("Found an EnemyDefault " + (mapObject.getRectangle().x + mapObject.getRectangle().width / 2) / PPM + " - " + (mapObject.getRectangle().y + 16) / PPM + " with width of: " + mapObject.getRectangle().width);
                    }
                    playScreen.getEntityManager().spawnDynamicEntity(new ToSpawnObjectDefinition<>(EnemyDefault.class, mapObject.getRectangle()));

                } else if (mapObject.getName().equalsIgnoreCase("EnemySpike")) {
                    if (NotFinalStatics.debug) {
                        System.out.println("Found an EnemyDefault " + (mapObject.getRectangle().x + mapObject.getRectangle().width / 2) / PPM + " - " + (mapObject.getRectangle().y + 16) / PPM + " with width of: " + mapObject.getRectangle().width);
                    }
                    playScreen.getEntityManager().spawnDynamicEntity(new ToSpawnObjectDefinition<>(EnemySpike.class, mapObject.getRectangle()));

                }
            }

        }

        //HitBox-Layer
        //Animators
        for (RectangleMapObject mapObject : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            if (mapObject.getName() != null) {
                if (mapObject.getName().equalsIgnoreCase("Flag")){
                    animators.add(new AnimatedStaticObject(tiledMap, mapObject.getRectangle()));
                }
            }
        }

        //HitBox-Layer
        //StaticCoins
        for (RectangleMapObject mapObject : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            new StaticCoin(tiledMap, world, mapObject.getRectangle());
        }

        for (RectangleMapObject mapObject : tiledMap.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            if (mapObject.getName() != null) {
                if (mapObject.getName().equalsIgnoreCase("Lever")) {
                    new InteractiveTileObject(world, tiledMap, mapObject.getRectangle()).setType(BitFilterDef.INTERACTIVE_LEVER);
                }
                if (mapObject.getName().equalsIgnoreCase("JumpPad")) {
                    new InteractiveTileObject(world, tiledMap, mapObject.getRectangle()).setType(BitFilterDef.INTERACTIVE_JUMPPAD);
                }
                if (mapObject.getName().equalsIgnoreCase("Spike")) {
                    new InteractiveTileObject(world, tiledMap, mapObject.getRectangle()).setType(BitFilterDef.INTERACTIVE_SPIKE);
                }
                if (mapObject.getName().equalsIgnoreCase("Button")) {
                    new InteractiveTileObject(world, tiledMap, mapObject.getRectangle()).setType(BitFilterDef.INTERACTIVE_BUTTON);
                }
            }
        }
    }

    public ArrayList<AnimatedStaticObject> getAnimators() {
        return animators;
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public Rectangle getKeyRectangle() {
        return keyRectangle;
    }
}
