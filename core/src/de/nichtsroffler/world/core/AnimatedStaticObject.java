package de.nichtsroffler.world.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;

public class AnimatedStaticObject {

    private TiledMap map;
    private int frameCount = 0;
    private int frame = 0;
    private Rectangle rectangle;
    private TextureRegion oldTile, newTile;

    public AnimatedStaticObject(TiledMap map, Rectangle rectangle) {
        this.rectangle = rectangle;
        this.map = map;

        oldTile = getCell(rectangle).getTile().getTextureRegion();
        newTile = map.getTileSets().getTile(getCell(rectangle).getTile().getId()+1).getTextureRegion();
    }

    public TiledMapTileLayer.Cell getCell(Rectangle rectangle){
        TiledMapTileLayer lay = (TiledMapTileLayer) map.getLayers().get(2);
        return lay.getCell((int)(rectangle.x / 18),
                (int)(rectangle.y / 18));
    }

    public void update() {
        if (getCell(rectangle).getTile() != null) {
            frameCount++;

            if (frameCount % 50 == 0) {
                frame++;
            }

            if (frame % 2 == 0) {
                getCell(rectangle).setTile(new StaticTiledMapTile(oldTile));
            } else {
                getCell(rectangle).setTile(new StaticTiledMapTile(newTile));
            }
        }
    }
}
