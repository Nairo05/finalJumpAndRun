package de.nichtsroffler.world;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.world.core.BitFilterDef;
import de.nichtsroffler.world.core.StaticObject;

public class StaticCoin extends StaticObject {

    private TiledMap tiledMap;

    public StaticCoin(TiledMap tiledMap, World world, Rectangle rectangle) {
        super(world, rectangle, true);

        this.tiledMap = tiledMap;
    }

    @Override
    protected void defineStaticObject() {
        setFilter(BitFilterDef.COLLECTIBLE_BIT, BitFilterDef.COLLECTIBLE_COIN_BIT);
        setUserData(StaticCoin.this);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer lay = (TiledMapTileLayer) tiledMap.getLayers().get(2);
        return lay.getCell((int)(body.getPosition().x * PPM / 18),
                (int)(body.getPosition().y * PPM / 18));
    }
}
