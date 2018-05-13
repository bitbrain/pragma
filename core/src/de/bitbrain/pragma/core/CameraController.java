package de.bitbrain.pragma.core;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.pragma.graphics.ScreenShake;

public class CameraController {


    private final GameCamera camera;

    public CameraController(GameCamera gameCamera) {
        this.camera = gameCamera;
    }

    public void update(float delta) {
        camera.getInternal().position.x += ScreenShake.getShake().x;
        camera.getInternal().position.y += ScreenShake.getShake().y;
    }
}