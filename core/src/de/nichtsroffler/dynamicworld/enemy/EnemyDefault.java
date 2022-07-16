package de.nichtsroffler.dynamicworld.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public final class EnemyDefault extends Enemy {

    public EnemyDefault(World world, Rectangle rectangle) {
        super(world, rectangle);
        texture = new Texture("sprite/enemy/default/enemydefault.png");
        textureRegions = TextureRegion.split(texture, 24, 24);
    }
}
