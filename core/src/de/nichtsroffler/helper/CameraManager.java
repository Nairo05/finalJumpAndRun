package de.nichtsroffler.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.nichtsroffler.dynamicworld.players.Player;
import de.nichtsroffler.main.NotFinalStatics;

public class CameraManager {

    private enum CamState {
        DOWN,
        UP,
        LOCKED
    }

    private static final float LEVEL_START = 2.46f;
    private static final float LEVEL_END = 29.9f;
    private static final float FINAL_CAM_POSITION = LEVEL_END + 2f;

    private final OrthographicCamera camera;

    private CamState camState;
    private int waitCount = 75;

    public CameraManager() {
        this.camera = new OrthographicCamera();

        camera.position.set(LEVEL_START, 1.28f,0f);
        camera.zoom = 1f;
        camera.update();
    }

    public void update(Player player, float dt) {
        if (NotFinalStatics.debug) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
                if (camera.zoom == 1f) {
                    camera.zoom = 4f;
                } else {
                    camera.zoom = 1f;
                }
            }
        }

        if (player.getX() > LEVEL_END) {
            waitCount--;
            if (waitCount <= 0) {
                if (camera.position.x < FINAL_CAM_POSITION) {
                    camera.position.x += dt / 2;
                }
            }
        }

        /*if (player.getY() > 1.5f) {
            camState = CamState.DOWN;
        } else if (player.getY() < 1.25 ) {
            camState = CamState.UP;
        }

        if (camState == CamState.DOWN) {
            if (camera.position.y < 2.1f) {
                camera.position.y += dt * 1.2f;
            }
        }*/

        if (camState == CamState.UP) {
            if (camera.position.y > 1.06f) {
                camera.position.y -= dt * 1.4;
            }
        }

        if (player.getX() > LEVEL_START && player.getX() < LEVEL_END) {
            camera.position.x = player.getX();
        }

        camera.update();

    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
