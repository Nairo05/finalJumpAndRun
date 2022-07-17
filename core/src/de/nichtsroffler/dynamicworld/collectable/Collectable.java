package de.nichtsroffler.dynamicworld.collectable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.dynamicworld.core.DynamicEntity;
import de.nichtsroffler.main.FinalStatics;
import de.nichtsroffler.world.core.BitFilterDef;

public class Collectable extends DynamicEntity {

    protected Texture texture;

    private final Vector2 speed = new Vector2(0f,0f);

    private Vector2 textureDistort = new Vector2(0f,0f);
    private int animationCount = 60;
    private boolean freeze = false;
    private boolean inAnimation = false;


    public Collectable(World world, float posX, float posY) {
        super(world, posX, posY);

        Vector2 impulse = new Vector2(1f,2f);

        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    @Override
    protected void defineHitBox() {
        FixtureDef bodyFixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6f / FinalStatics.PPM);
        bodyFixtureDef.filter.categoryBits = BitFilterDef.COLLECTIBLE_BIT;
        bodyFixtureDef.filter.maskBits = BitFilterDef.PLAYER_BIT | BitFilterDef.GROUND_BIT | BitFilterDef.ENEMY_BODY_BIT | BitFilterDef.ACTION_BLOCK;
        bodyFixtureDef.filter.groupIndex = BitFilterDef.COLLECTIBLE_DEFAULT_BIT;
        bodyFixtureDef.shape = circleShape;

        body.createFixture(bodyFixtureDef).setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.1f,0.1f);
        fixtureDef.filter.categoryBits = BitFilterDef.COLLECTIBLE_BIT;
        fixtureDef.filter.maskBits = BitFilterDef.PLAYER_BIT | BitFilterDef.GROUND_BIT | BitFilterDef.ENEMY_BODY_BIT | BitFilterDef.ACTION_BLOCK;
        fixtureDef.filter.groupIndex = BitFilterDef.COLLECTIBLE_DEFAULT_BIT;
        fixtureDef.isSensor = true;
        fixtureDef.shape = polygonShape;

        fixture = body.createFixture(fixtureDef);

        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onBodyHit() {
        prepareDestroy();
        inAnimation = true;
        System.out.println("destroy");
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float dt) {
        if (toDestroy && !isDestroyed) {

            textureDistort = body.getPosition();

            destroyBody();
            toDestroy = false;

        }

        if (inAnimation) {

            animationCount--;

            speed.add(-0.005f, 0.004f);
            textureDistort.add(speed);

            if (animationCount < 0) {
                inAnimation = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (!toDestroy && !isDestroyed) {
            spriteBatch.draw(texture, body.getPosition().x - 0.08f, body.getPosition().y - 0.08f, 0.16f, 0.16f);
        }
        if (inAnimation) {
            spriteBatch.draw(texture, textureDistort.x - 0.08f, textureDistort.y - 0.08f, 0.16f, 0.16f);
        }
    }

    public void freeze() {
        if (!toDestroy && !isDestroyed) {
            body.setActive(false);
            freeze = true;
        }
    }

    public void unFreeze() {
        if (!toDestroy && !isDestroyed) {
            body.setActive(true);
            freeze = false;
        }
    }

    public boolean isFreeze() {
        if (!toDestroy && !isDestroyed) {
            return freeze;
        }
        return true;
    }

    public float getPositionInWorld() {
        if (!toDestroy && !isDestroyed) {
            return body.getPosition().x;
        } else {
            return -1;
        }
    }

    public boolean isInAnimation() {
        return inAnimation;
    }

    protected void overrideGroupIndex(byte group) {
        Filter filter = fixture.getFilterData();
        filter.groupIndex = group;
        fixture.setFilterData(filter);
    }
}
