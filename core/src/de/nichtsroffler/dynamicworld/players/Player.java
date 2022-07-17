package de.nichtsroffler.dynamicworld.players;

import static de.nichtsroffler.main.FinalStatics.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import de.nichtsroffler.dynamicworld.core.DynamicEntity;
import de.nichtsroffler.screens.PlayScreen;
import de.nichtsroffler.world.core.BitFilterDef;

public class Player extends DynamicEntity {

    private PlayScreen playScreen;

    public enum PrimaryState {
        normal,
        itemdouble;
    }


    public enum AdditionState {
        normal,
        key;
    }

    private PrimaryState primaryState;
    private AdditionState additionState;
    private Key key;

    private short lives = 6;
    private short diamonds = 0;
    private short coins = 0;

    private int immunCount = 288;

    private boolean hitted = false;
    private boolean jumppad = false;
    private boolean spikeHitted = false;

    public Player(PlayScreen playScreen, float posX, float posY, float posKeyX, float posKeyY) {
        super(playScreen.getWorld(), posX, posY);

        this.playScreen = playScreen;

        primaryState = PrimaryState.normal;
        additionState = AdditionState.key;

        key = new Key(world, posKeyX, posKeyY);
    }

    @Override
    public void update(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            diamonds++;
            System.out.println(diamonds);
        }

        if (immunCount >= 0) {
            immunCount--;
        }

        if (spikeHitted) {
            body.setLinearVelocity(Vector2.Zero);
            body.applyLinearImpulse(new Vector2(-1.5f, 1.5f), body.getWorldCenter(), true);
            spikeHitted = false;
        }

        if (jumppad) {
            stopVelocity();
            if (body.getLinearVelocity().y < 4f) {
                body.applyLinearImpulse(new Vector2(0f, 5.5f), body.getWorldCenter(), true);
            }
            jumppad = false;
        }

        if(hitted) {
            if (body.getLinearVelocity().x >= 0f) {
                body.applyLinearImpulse(-2f, 1.5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
            } else {
                body.applyLinearImpulse(2f, 1.5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
            }
            hitted = false;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && body.getPosition().x <= 39f) {
            if (body.getLinearVelocity().x < 1.5f) {
                float force = 10f * dt;
                body.applyLinearImpulse(new Vector2(force, 0), body.getWorldCenter(), true);
            }
        } else if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && body.getPosition().x <= 39f) {
            if (body.getLinearVelocity().x > -1.5f) {
                float force = -10f * dt;
                body.applyLinearImpulse(new Vector2(force, 0), body.getWorldCenter(), true);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (primaryState == PrimaryState.normal) {
                if (isPlayerOnGround()) {
                    body.applyLinearImpulse(new Vector2(0f, 4.4f), body.getWorldCenter(), true);
                    if (additionState == AdditionState.key) {
                        key.addPlayerY(4.4f);
                    }
                }
            }
        } else {
            if (additionState == AdditionState.key) {
                key.addPlayerY(0);
            }
        }

        if (additionState == AdditionState.key) {
            key.setPlayerX(body.getPosition().x);
            key.update(dt);
        }

        // ------------------------------------------------- Android ---------------------------------------------------------------------
            for (int i = 0; i < 3; i++) {

                if (Gdx.input.isTouched(i)) {

                    if (Gdx.input.getX(i) > 0 && Gdx.input.getX(i) < 300) {
                        if (body.getLinearVelocity().x < 1.5f) {
                            body.applyLinearImpulse(new Vector2(10f * dt, 0), body.getWorldCenter(), true);
                        }
                    } else if (Gdx.input.getX(i) > 300) {
                        body.applyLinearImpulse(new Vector2(0f, 4.4f), body.getWorldCenter(), true);
                    }
                }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (additionState == AdditionState.key) {
            key.render(spriteBatch);
        }
    }

    @Override
    protected void defineHitBox() {

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape bodyCircleShape = new CircleShape();
        bodyCircleShape.setRadius(8f / PPM);
        fixtureDef.shape = bodyCircleShape;
        fixtureDef.filter.categoryBits = BitFilterDef.PLAYER_BIT;
        fixtureDef.filter.groupIndex = BitFilterDef.PLAYER_BIT_PLAYER_INDEX;
        fixtureDef.filter.maskBits = BitFilterDef.GROUND_BIT | BitFilterDef.ENEMY_BODY_BIT
                | BitFilterDef.ENEMY_HEAD_BIT
                | BitFilterDef.COLLECTIBLE_BIT
                | BitFilterDef.ONE_WAY_GROUND
                | BitFilterDef.ACTION_BLOCK
                | BitFilterDef.INTERACTIVE_TILE_OBJECT;
        fixture = body.createFixture(fixtureDef);

        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-5f / PPM, -9f / PPM), new Vector2(5f / PPM, -9f / PPM));
        fixtureDef.shape = foot;
        fixtureDef.isSensor = true;
        fixtureDef.filter.groupIndex = BitFilterDef.PLAYER_BIT_FOOT_INDEX;
        fixtureDef.filter.categoryBits = BitFilterDef.PLAYER_BIT;
        body.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-3f / PPM, 9f / PPM), new Vector2(3f / PPM, 9f / PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BitFilterDef.PLAYER_BIT;
        fixtureDef.filter.groupIndex = BitFilterDef.PLAYER_BIT_HEAD_INDEX;
        body.createFixture(fixtureDef);

        EdgeShape foot2 = new EdgeShape();
        foot2.set(new Vector2(-2f / PPM, -10f / PPM), new Vector2(2f / PPM, -10f / PPM));
        fixtureDef.shape = foot2;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BitFilterDef.PLAYER_REVERSE_VEL_BIT;
        body.createFixture(fixtureDef);

        bodyCircleShape.dispose();

        body.setLinearDamping(2f);
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

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void stopVelocity() {
        if (body.getLinearVelocity().y < -1f) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
        }
    }

    public boolean isJumping() {
        return body.getLinearVelocity().y > 0.1;
    }

    public boolean keyIsJumping() {
        return key.isJumping();
    }

    public void setAdditionState(AdditionState additionState) {
        System.out.println(additionState);
        this.additionState = additionState;
    }

    public AdditionState getAdditionState() {
        return additionState;
    }

    public boolean hasKey() {
        return key.isFollowing();
    }

    public void useKey() {
        key.setUsed(true);
    }

    public boolean keyIsUsed() {
        return key.isUsed();
    }

    public int getLives() {
        return lives;
    }

    public short getDiamonds() {
        return diamonds;
    }

    public void looseLife() {
        if (!(immunCount > 0)) {
            lives--;
            immunCount = 144;
        }
        if (!hitted) {
            hitted = true;
        }
    }

    public void collectDiamond() {
        diamonds++;
    }
    public void collectCoin() {
        coins++;
        System.out.println(coins);
    }

    public void spike() {
        if (!(immunCount > 0)) {
            immunCount = 300;
            lives -= 2;
        }
        if (!spikeHitted) {
            spikeHitted = true;
        }
    }

    public boolean isMovingRight() {
        return body.getLinearVelocity().x > 0.1f;
    }

    public void jumpPad() {
        jumppad = true;
    }

    public boolean isPlayerOnGround() {
        if (playScreen.getContactListener().isPlayerOnGround()){
            if (Math.abs(body.getLinearVelocity().y) < 0.1f) {
                System.out.println("Ground");
                return true;
            }
        }
        return false;
    }
}
