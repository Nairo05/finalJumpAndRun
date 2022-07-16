package de.nichtsroffler.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.world.core.BitFilterDef;
import de.nichtsroffler.world.core.StaticObject;

public class OneWayGround extends StaticObject {

    public OneWayGround(World world, float[] vertices) {
        super(world, vertices);
    }

    public OneWayGround(World world, Rectangle rectangle) {
        super(world, rectangle, false);
    }

    @Override
    protected void defineStaticObject() {
        setFilter(BitFilterDef.ONE_WAY_GROUND);
    }
}
