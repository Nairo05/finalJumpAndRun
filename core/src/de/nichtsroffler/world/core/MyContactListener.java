package de.nichtsroffler.world.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.nichtsroffler.dynamicworld.collectable.BlueDimaond;
import de.nichtsroffler.dynamicworld.collectable.Collectable;
import de.nichtsroffler.dynamicworld.collectable.DynamicCoin;
import de.nichtsroffler.dynamicworld.enemy.Enemy;
import de.nichtsroffler.dynamicworld.players.Key;
import de.nichtsroffler.dynamicworld.players.Player;
import de.nichtsroffler.screens.PlayScreen;
import de.nichtsroffler.world.DefaultQuestionBlock;
import de.nichtsroffler.world.InteractiveTileObject;
import de.nichtsroffler.world.LockQuestionBlock;
import de.nichtsroffler.world.StaticCoin;

public class MyContactListener implements ContactListener {

    private final PlayScreen playScreen;
    private int onGround = 0;

    public MyContactListener(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.PLAYER_BIT || contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.PLAYER_BIT) {
            if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_FOOT_INDEX
                || contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_FOOT_INDEX) {
                onGround++;
            }
        }


        int checksum = contact.getFixtureA().getFilterData().categoryBits + contact.getFixtureB().getFilterData().categoryBits;

        if (checksum == BitFilterDef.PLAYER_REVERSE_VEL) {
            playScreen.getPlayer().stopVelocity();
        }

        if (checksum == BitFilterDef.PLAYER_HIT_ENEMY_BIT) {
            playScreen.getPlayer().looseLife();
        }

        if (checksum == BitFilterDef.PLAYER_KILL_ENEMY) {
            if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.ENEMY_HEAD_BIT) {
                ((Enemy) contact.getFixtureA().getUserData()).onHeadHit();
            }
            if (contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.ENEMY_HEAD_BIT) {
                ((Enemy) contact.getFixtureB().getUserData()).onHeadHit();
            }
        }

        if (checksum == BitFilterDef.PLAYER_JUMPED_ACTION_BLOCK) {

            if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_HEAD_INDEX
                    || contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_HEAD_INDEX) {

                if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.ACTION_BLOCK) {
                    if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.DEFAULT_ACTION_BLOCK) {

                       ((DefaultQuestionBlock) contact.getFixtureA().getUserData()).getCell().setTile(null);

                        if (MathUtils.random(1) == 0) {
                            for (int i = 0; i < (MathUtils.random(20)+3); i++) {
                                playScreen.getCollectableManager().aSyncSpawn(DynamicCoin.class,
                                        ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getSpawnRectangle());
                            }
                        } else {
                            playScreen.getCollectableManager().aSyncSpawn(DynamicCoin.class,
                                    ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getSpawnRectangle());
                        }

                    } else if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.LOCK_ACTION_BLOCK) {

                        if (playScreen.getPlayer().hasKey() && !playScreen.getPlayer().keyIsUsed()) {
                            playScreen.getPlayer().setAdditionState(Player.AdditionState.normal);
                            if (((LockQuestionBlock) contact.getFixtureB().getUserData()).getCell().getTile() != null) {
                                ((LockQuestionBlock) contact.getFixtureA().getUserData()).getCell().setTile(null);
                                playScreen.getPlayer().useKey();
                            }
                        }

                    }
                }

                if (contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.ACTION_BLOCK) {

                    if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.DEFAULT_ACTION_BLOCK) {

                        if ( ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getCell().getTile() != null) {
                            ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getCell().setTile(null);

                            if (MathUtils.random(1) == 0) {
                                for (int i = 0; i < (MathUtils.random(20)+3); i++) {
                                    playScreen.getCollectableManager().aSyncSpawn(DynamicCoin.class,
                                            ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getSpawnRectangle());
                                }
                            } else {
                                playScreen.getCollectableManager().aSyncSpawn(DynamicCoin.class,
                                        ((DefaultQuestionBlock) contact.getFixtureB().getUserData()).getSpawnRectangle());
                            }
                        }

                    } else if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.LOCK_ACTION_BLOCK) {

                        if (playScreen.getPlayer().hasKey() && !playScreen.getPlayer().keyIsUsed()) {
                            playScreen.getPlayer().setAdditionState(Player.AdditionState.normal);
                            if (((LockQuestionBlock) contact.getFixtureB().getUserData()).getCell().getTile() != null) {
                                ((LockQuestionBlock) contact.getFixtureB().getUserData()).getCell().setTile(null);

                                playScreen.getCollectableManager().aSyncSpawn(BlueDimaond.class,
                                        ((LockQuestionBlock) contact.getFixtureB().getUserData()).getSpawnRectangle());

                                playScreen.getPlayer().useKey();
                            }
                        }
                    }
                }
            }
        }

        if (checksum == BitFilterDef.PLAYER_COLLIDE_COLLECTIBLE) {

            if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.COLLECTIBLE_BIT) {

                if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_KEY_BIT) {
                    if (!playScreen.getPlayer().hasKey()) {
                        ((Key) contact.getFixtureA().getUserData()).activeFollowing();
                        playScreen.getPlayer().setAdditionState(Player.AdditionState.key);
                    }
                }


                if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_COIN_BIT) {
                    if (((StaticCoin) contact.getFixtureA().getUserData()).getCell().getTile() != null){
                        ((StaticCoin) contact.getFixtureA().getUserData()).getCell().setTile(null);
                        playScreen.getPlayer().collectCoin();
                    }
                }


                if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_DEFAULT_BIT) {
                    ((Collectable) contact.getFixtureA().getUserData()).onBodyHit();
                    playScreen.getPlayer().collectCoin();
                }

                if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_DIAMOND_BIT) {
                    ((Collectable) contact.getFixtureA().getUserData()).onBodyHit();
                    System.out.println("gem");
                    playScreen.getPlayer().collectDiamond();
                }


            } else if (contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.COLLECTIBLE_BIT) {

                if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_KEY_BIT) {
                    if (!playScreen.getPlayer().hasKey()) {
                        ((Key) contact.getFixtureB().getUserData()).activeFollowing();
                        playScreen.getPlayer().setAdditionState(Player.AdditionState.key);
                    }
                }

                if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_COIN_BIT) {
                    if (((StaticCoin) contact.getFixtureB().getUserData()).getCell().getTile() != null) {
                        ((StaticCoin) contact.getFixtureB().getUserData()).getCell().setTile(null);
                        playScreen.getPlayer().collectCoin();
                    }
                }

                if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_DEFAULT_BIT) {
                    ((Collectable) contact.getFixtureB().getUserData()).onBodyHit();
                    playScreen.getPlayer().collectCoin();
                }

                if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.COLLECTIBLE_DIAMOND_BIT) {
                    ((Collectable) contact.getFixtureB().getUserData()).onBodyHit();
                    System.out.println("gem");
                    playScreen.getPlayer().collectDiamond();
                }

            }
        }

        if (checksum == BitFilterDef.PLAYER_COLLIDE_INTERACTIVE) {
            if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.INTERACTIVE_TILE_OBJECT) {
                if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_LEVER) {
                    switchLever(contact.getFixtureA());
                } else if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_JUMPPAD) {
                    jumpPad(contact.getFixtureA());
                } else if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_SPIKE) {
                    playScreen.getPlayer().spike();
                } else if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_BUTTON) {
                    switchButton(contact.getFixtureA());
                }
            } else if (contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.INTERACTIVE_TILE_OBJECT) {
                if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_LEVER) {
                    switchLever(contact.getFixtureB());
                } else if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_JUMPPAD) {
                    jumpPad(contact.getFixtureB());
                } else if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_SPIKE) {
                    playScreen.getPlayer().spike();
                } else if (contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.INTERACTIVE_BUTTON) {
                    switchButton(contact.getFixtureB());
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getFilterData().categoryBits == BitFilterDef.PLAYER_BIT || contact.getFixtureB().getFilterData().categoryBits == BitFilterDef.PLAYER_BIT) {
            if (contact.getFixtureA().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_FOOT_INDEX
                    || contact.getFixtureB().getFilterData().groupIndex == BitFilterDef.PLAYER_BIT_FOOT_INDEX) {
                onGround--;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        int checksum = contact.getFixtureA().getFilterData().categoryBits + contact.getFixtureB().getFilterData().categoryBits;

        if (checksum == BitFilterDef.PLAYER_ONE_WAY_GROUND && playScreen.getPlayer().isJumping()) {
            contact.setEnabled(false);
        }
        if (checksum == BitFilterDef.COLLECTIBLE_ONE_WAY_GROUND && playScreen.getPlayer().keyIsJumping()) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isPlayerOnGround() {
        System.out.println(onGround);
        return onGround > 0;
    }

    private void jumpPad(Fixture fixture) {

        InteractiveTileObject interactiveTileObject = ((InteractiveTileObject) fixture.getUserData());

        if (interactiveTileObject.getCell().getTile() != null) {
            int id = interactiveTileObject.getCell().getTile().getId();

            System.out.println(id);

            if (id == 109) {
                id = 108;
            }

            interactiveTileObject.onHit(id);

            interactiveTileObject.getCell().setTile(interactiveTileObject.getTiledMap().getTileSets().getTile(id));

            playScreen.getEventQueuer().queueEvent(interactiveTileObject, 15);

            playScreen.getPlayer().jumpPad();
        }
    }

    private void switchButton(Fixture fixture) {

        InteractiveTileObject interactiveTileObject = ((InteractiveTileObject) fixture.getUserData());

        if (interactiveTileObject.getCell().getTile() != null) {
            int id = interactiveTileObject.getCell().getTile().getId();

            if (id == 149) {
                id++;
            }

            interactiveTileObject.onHit(id);

            interactiveTileObject.getCell().setTile(interactiveTileObject.getTiledMap().getTileSets().getTile(id));

            playScreen.getEventQueuer().queueEvent(interactiveTileObject, 600);
        }
    }

    private void switchLever(Fixture fixture) {

        InteractiveTileObject interactiveTileObject = ((InteractiveTileObject) fixture.getUserData());

        if (interactiveTileObject.getCell().getTile() != null) {
            int id = interactiveTileObject.getCell().getTile().getId();

            if ((id == 66 || id == 65) && playScreen.getPlayer().isMovingRight()) {
                id = 67;
            }
            if ((id == 66 || id == 67) && !playScreen.getPlayer().isMovingRight()) {
                id = 65;
            }

            interactiveTileObject.onHit(id);

            interactiveTileObject.getCell().setTile(interactiveTileObject.getTiledMap().getTileSets().getTile(id));

            playScreen.getEventQueuer().queueEvent(interactiveTileObject, 200);

        }
    }
}
