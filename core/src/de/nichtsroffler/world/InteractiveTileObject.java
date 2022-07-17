package de.nichtsroffler.world;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.world.core.BitFilterDef;
import de.nichtsroffler.world.core.StaticObject;

public class InteractiveTileObject extends StaticObject {

    private TiledMap tiledMap;

    public InteractiveTileObject(World world, TiledMap map, Rectangle rectangle) {
        super(world, rectangle, true);

        this.tiledMap = map;
    }

    @Override
    protected void defineStaticObject() {
        setUserData(this);
    }

    public void setType(byte type) {
        setFilter(BitFilterDef.INTERACTIVE_TILE_OBJECT, type);
    }

    public void onHit(int meta) {
        System.out.println(meta);
    }

    public void onEvent() {
        if (getCell().getTile().getId() == 65 || getCell().getTile().getId() == 67) {
            getCell().setTile(getTiledMap().getTileSets().getTile(66));
        }
        if (getCell().getTile().getId() == 108) {
            getCell().setTile(getTiledMap().getTileSets().getTile(109));
        }
        if (getCell().getTile().getId() == 150) {
            getCell().setTile(getTiledMap().getTileSets().getTile(149));
        }
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer lay = (TiledMapTileLayer) tiledMap.getLayers().get(2);
        return lay.getCell((int)(body.getPosition().x * PPM / 18),
                (int)(body.getPosition().y * PPM / 18));
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
