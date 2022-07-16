package de.nichtsroffler.world;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.world.core.BitFilterDef;
import de.nichtsroffler.world.core.StaticObject;

public class DefaultQuestionBlock extends StaticObject {

    private TiledMap map;

    public DefaultQuestionBlock(World world, TiledMap map, Rectangle rectangle) {
        super(world, rectangle,false);

        this.map = map;
    }

    @Override
    protected void defineStaticObject() {
        setFilter(BitFilterDef.ACTION_BLOCK, BitFilterDef.DEFAULT_ACTION_BLOCK);
        setUserData(DefaultQuestionBlock.this);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer lay = (TiledMapTileLayer) map.getLayers().get(2);
        return lay.getCell((int)(body.getPosition().x * PPM / 18),
                (int)(body.getPosition().y * PPM / 18));
    }

    public Rectangle getSpawnRectangle() {
        return new Rectangle(body.getPosition().x, body.getPosition().y + 0.18f, 18,18);
    }
}
