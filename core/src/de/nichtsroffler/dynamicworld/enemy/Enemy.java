package de.nichtsroffler.dynamicworld.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.dynamicworld.core.DynamicEntity;
import de.nichtsroffler.main.FinalStatics;
import de.nichtsroffler.world.core.BitFilterDef;

public abstract class Enemy extends DynamicEntity {

    protected Texture texture;
    protected TextureRegion[][] textureRegions;

    private int frameCount = 0;
    private int frame = 0;
    private final float leftBound;
    private final float rightBound;
    private boolean movesLeft = false;
    private float wait = 0;

    public Enemy(World world, Rectangle rectangle) {
        super(world, rectangle.x, rectangle.y);

        float roamWidth = rectangle.width / 2;
        this.leftBound = (rectangle.x - (roamWidth)) / FinalStatics.PPM + roamWidth / FinalStatics.PPM + 0.06f;
        this.rightBound = (rectangle.x + (roamWidth)) / FinalStatics.PPM + roamWidth / FinalStatics.PPM - 0.06f;
    }

    @Override
    protected void defineHitBox() {

        FixtureDef headFixtureDef = new FixtureDef();
        FixtureDef bodyFixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vertices = {
                new Vector2(-7.5f, 10f).scl(1f / FinalStatics.PPM),
                new Vector2(7.5f, 10f).scl(1f / FinalStatics.PPM),
                new Vector2(-3.5f, 3f).scl(1f / FinalStatics.PPM),
                new Vector2(3.5f, 3f).scl(1f / FinalStatics.PPM)
        };
        polygonShape.set(vertices);
        headFixtureDef.shape = polygonShape;
        headFixtureDef.filter.categoryBits = BitFilterDef.ENEMY_HEAD_BIT;
        headFixtureDef.filter.maskBits = BitFilterDef.PLAYER_BIT;
        headFixtureDef.isSensor = true;
        body.createFixture(headFixtureDef).setUserData(this);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6f / FinalStatics.PPM);
        bodyFixtureDef.filter.categoryBits = BitFilterDef.ENEMY_BODY_BIT;
        bodyFixtureDef.filter.maskBits = BitFilterDef.PLAYER_BIT | BitFilterDef.GROUND_BIT | BitFilterDef.ENEMY_BODY_BIT;
        bodyFixtureDef.shape = circleShape;

        fixture = body.createFixture(bodyFixtureDef);
        fixture.setUserData(this);

        circleShape.dispose();
    }

    @Override
    public void onHeadHit() {
        prepareDestroy();
    }

    @Override
    public void onBodyHit() {

    }

    @Override
    public void update(float dt) {

        if (toDestroy) {

            destroyBody();
            toDestroy = false;

        } else {

            if (!isDestroyed) {
                frameCount++;
                if ((frameCount % 50) == 0) {
                    frame++;
                    if (frame > 2) {
                        frame = 1;
                    }
                }

                if (wait <= 1) {
                    if (movesLeft) {
                        body.setLinearVelocity(-0.2f, 0f);
                    } else {
                        body.setLinearVelocity(0.2f, 0f);
                    }

                    if (body.getWorldCenter().x <= leftBound) {
                        movesLeft = false;
                    } else if (body.getWorldCenter().x >= rightBound) {
                        movesLeft = true;
                    }
                } else {
                    wait--;
                }
            }

        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        if (!isDestroyed() && !toDestroy) {

            if (body.getLinearVelocity().x > 0.1f) {
                if (!textureRegions[0][frame].isFlipX()) {
                    textureRegions[0][frame].flip(true, false);
                }
            }
            if (body.getLinearVelocity().x < -0.1f) {
                if (textureRegions[0][frame].isFlipX()) {
                    textureRegions[0][frame].flip(true, false);
                }
            }

            if (Math.abs(body.getLinearVelocity().x) >= 0.001f && Math.abs(body.getLinearVelocity().y) < 0.1f) {
                spriteBatch.draw(textureRegions[0][frame], body.getPosition().x - 0.12f,body.getPosition().y - 0.07f, 0.24f,0.24f);
            } else {
                spriteBatch.draw(textureRegions[0][0], body.getPosition().x - 0.12f ,body.getPosition().y - 0.07f, 0.24f,0.24f);
            }

        }
    }

    @Override
    public void dispose() {
    }

    protected void initialFlip() {
        for (TextureRegion textureRegion : textureRegions[0]) {
            textureRegion.flip(true, false);
        }
    }

    protected void waitS(float waitTime) {
        this.wait = waitTime;
    }
}
