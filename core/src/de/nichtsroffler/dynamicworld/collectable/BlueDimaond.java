package de.nichtsroffler.dynamicworld.collectable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class BlueDimaond extends Collectable {

    public BlueDimaond(World world, float posX, float posY) {
        super(world, posX, posY);

        texture = new Texture("sprite/collectible/bluediamong.png");
    }
}
