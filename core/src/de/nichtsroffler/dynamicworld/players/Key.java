package de.nichtsroffler.dynamicworld.players;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;

import de.nichtsroffler.dynamicworld.core.DynamicEntity;
import de.nichtsroffler.main.FinalStatics;
import de.nichtsroffler.world.core.BitFilterDef;

public class Key extends DynamicEntity {

    private Texture texture;
    private int preFill = 20;
    private Queue<Float> playerY = new Queue<>(preFill);
    private float playerX;
    private boolean isFollowing = false;

    public Key(World world, float posX, float posY) {
        super(world, posX, posY);

        texture = new Texture("sprite/tiled/tile_0027.png");
    }


    @Override
    protected void defineHitBox() {
        FixtureDef fixtureDef = new FixtureDef();

        CircleShape bodyCircleShape = new CircleShape();
        bodyCircleShape.setRadius(8f / PPM);
        fixtureDef.shape = bodyCircleShape;
        fixtureDef.filter.categoryBits = BitFilterDef.COLLECTIBLE_BIT;
        fixtureDef.filter.maskBits = BitFilterDef.GROUND_BIT | BitFilterDef.ACTION_BLOCK | BitFilterDef.ONE_WAY_GROUND;
        fixture = body.createFixture(fixtureDef);

        FixtureDef fDefHit = new FixtureDef();
        PolygonShape shapeHit = new PolygonShape();
        shapeHit.setAsBox(12f / FinalStatics.PPM,12f / FinalStatics.PPM);
        fDefHit.shape = shapeHit;
        fDefHit.filter.categoryBits = BitFilterDef.COLLECTIBLE_BIT;
        fDefHit.filter.groupIndex = BitFilterDef.COLLECTIBLE_KEY_BIT;
        fDefHit.filter.maskBits = BitFilterDef.PLAYER_BIT;
        fDefHit.isSensor = true;
        body.createFixture(fDefHit).setUserData(this);

        body.setLinearDamping(2f);

        body.applyLinearImpulse(new Vector2(0.01f, 0.01f), body.getWorldCenter(), true);
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float dt) {
        if (isFollowing) {
            if (preFill > 0) {
                preFill--;
            } else {
                if (playerY.first() > 0) {
                    body.applyLinearImpulse(new Vector2(0, playerY.first()), body.getWorldCenter(), true);
                }
                playerY.removeFirst();
            }

            if (playerX - body.getPosition().x > 0.32f) {
                if (body.getLinearVelocity().x < 1.5f) {
                    float force = 10f * dt;
                    body.applyLinearImpulse(new Vector2(force, 0), body.getWorldCenter(), true);
                }
            }
            if (playerX - body.getPosition().x < -0.16f) {
                if (body.getLinearVelocity().x > -1.5f) {
                    float force = -10f * dt;
                    body.applyLinearImpulse(new Vector2(force, 0), body.getWorldCenter(), true);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, body.getPosition().x - 0.08f, body.getPosition().y  - 0.08f, 0.16f, 0.16f);
    }


    public void setPlayerX(float playerX) {
        if (isFollowing) {
            this.playerX = playerX;
        }
    }

    public void addPlayerY(float playerY) {
        if (isFollowing) {
            playerY -= 0.12f;

            this.playerY.addLast(playerY);
        }
    }

    public boolean isJumping() {
        return body.getLinearVelocity().y > 0.1f;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void activeFollowing() {
        isFollowing = true;
    }

    public void setFollowing() {
        isFollowing = !isFollowing;
    }
}
