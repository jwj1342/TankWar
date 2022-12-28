package henu.soft.tankwar;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;

public class FailScence extends SubScene {
    public FailScence() {
        Texture texture = new Texture(FXGL.image("gameover.png"));
        getContentRoot().getChildren().add(texture);
        texture.setLayoutX(400 - 240);
        texture.setLayoutY(300 - 160);

    }

    @Override
    public void onCreate() {
        new FailScence();

        this.onDestroy();

    }
}
