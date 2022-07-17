package de.nichtsroffler.world.core;

public class BitFilterDef {

    //Default Bit
    public static final byte NO_CLIP_BIT = 0;

    //Identity Bits - Category and Filter Mask Bits
    public static final short PLAYER_BIT = 1;
    public static final byte PLAYER_BIT_HEAD_INDEX = 1;
    public static final byte PLAYER_BIT_FOOT_INDEX = 2;
    public static final byte PLAYER_BIT_PLAYER_INDEX = 3;

    public static final short PLAYER_REVERSE_VEL_BIT = 2;
    public static final short GROUND_BIT = 8;
    public static final short ENEMY_HEAD_BIT = 16;
    public static final short ENEMY_BODY_BIT = 32;

    public static final short COLLECTIBLE_BIT = 64;
    public static final byte COLLECTIBLE_KEY_BIT = 4;
    public static final byte COLLECTIBLE_COIN_BIT = 5;
    public static final byte COLLECTIBLE_DEFAULT_BIT = 6;
    public static final byte COLLECTIBLE_DIAMOND_BIT = 11;

    public static final short ONE_WAY_GROUND = 128;
    public static final short ACTION_BLOCK = 256;

    public static final short INTERACTIVE_TILE_OBJECT = 512;
    public static final byte INTERACTIVE_LEVER = 7;
    public static final byte INTERACTIVE_JUMPPAD = 8;
    public static final byte INTERACTIVE_SPIKE = 9;
    public static final byte INTERACTIVE_BUTTON = 10;

    public static final byte DEFAULT_ACTION_BLOCK = 8;
    public static final byte LOCK_ACTION_BLOCK = 16;

    //Colliding Bits
    public static final byte PLAYER_REVERSE_VEL = PLAYER_BIT + GROUND_BIT;
    public static final short PLAYER_ONE_WAY_GROUND = PLAYER_BIT + ONE_WAY_GROUND;
    public static final short PLAYER_JUMPED_ACTION_BLOCK = PLAYER_BIT + ACTION_BLOCK;
    public static final short COLLECTIBLE_ONE_WAY_GROUND = COLLECTIBLE_BIT + ONE_WAY_GROUND;
    public static final short PLAYER_COLLIDE_COLLECTIBLE = PLAYER_BIT + COLLECTIBLE_BIT;
    public static final short PLAYER_HIT_ENEMY_BIT = PLAYER_BIT + ENEMY_BODY_BIT;
    public static final short PLAYER_KILL_ENEMY = PLAYER_BIT + ENEMY_HEAD_BIT;
    public static final short PLAYER_COLLIDE_INTERACTIVE = PLAYER_BIT + INTERACTIVE_TILE_OBJECT;

}
