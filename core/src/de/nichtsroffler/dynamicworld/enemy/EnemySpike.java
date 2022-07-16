package de.nichtsroffler.dynamicworld.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.main.FinalStatics;

public class EnemySpike extends Enemy{

    public EnemySpike(World world, Rectangle rectangle) {
        super(world, rectangle);
        texture = new Texture("sprite/enemy/spike/enemyspike.png");
        textureRegions = TextureRegion.split(texture, 24, 24);

        waitS(FinalStatics.FPS);
    }

    @Override
    public void onHeadHit() {
        //nothing
    }
}
