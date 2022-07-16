package de.nichtsroffler.dynamicworld.collectable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicCoin extends Collectable {

    public DynamicCoin(World world, float posX, float posY) {
        super(world, posX, posY);

        texture = new Texture("sprite/collectible/coin.png");
    }
}
