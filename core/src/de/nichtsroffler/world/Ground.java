package de.nichtsroffler.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.world.core.BitFilterDef;
import de.nichtsroffler.world.core.StaticObject;

public class Ground extends StaticObject {

    public Ground(World world, Rectangle rectangle) {
        super(world, rectangle,false);
    }

    public Ground(World world, float[] vertices) {
        super(world, vertices);
    }

    @Override
    protected void defineStaticObject() {
        setFilter(BitFilterDef.GROUND_BIT);
    }
}
