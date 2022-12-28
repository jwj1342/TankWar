package henu.soft.tankwar;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

public class TankApp extends GameApplication {
    private Entity player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setHeight(600);
        gameSettings.setWidth(800);
        gameSettings.setAppIcon("10.gif");
        gameSettings.setTitle("TankWar");
        gameSettings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new TankFactory());
        player = FXGL.spawn("player");
        for (int i = 0; i < 800; i += 59) {
            FXGL.spawn("bricks", i, 1);
        }
        for (int i = 0; i < 600; i += 59) {
            FXGL.spawn("bricks", 1, i);
        }
        for (int i = 0; i < 800; i += 59) {
            FXGL.spawn("bricks", i, 600 - 59);
        }
        for (int i = 0; i < 600; i += 59) {
            FXGL.spawn("bricks", 800 - 59, i);
        }


        for (int i = 0; i < 5; i++) {
            createEnemy();
        }
    }

    private void createEnemy() {
        FXGL.spawn("enemy");
    }


    @Override
    protected void initUI() {
        Text text = FXGL.addVarText("Score", 20, 20);
        //首先解除绑定，之后再设置相关的属性
        text.fontProperty().unbind();
        text.setFill(Color.RED);

        text.setFont(Font.font(25));
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            TankCompnent playerCompnent = player.getComponent(TankCompnent.class);
            playerCompnent.move_right();

        });

        FXGL.onKey(KeyCode.A, () -> {
            TankCompnent playerCompnent = player.getComponent(TankCompnent.class);
            playerCompnent.move_left();
        });

        FXGL.onKey(KeyCode.W, () -> {
            TankCompnent playerCompnent = player.getComponent(TankCompnent.class);
            playerCompnent.move_up();
        });

        FXGL.onKey(KeyCode.S, () -> {
            TankCompnent playerCompnent = player.getComponent(TankCompnent.class);
            playerCompnent.move_down();
        });

        //为空格键添加匿名事件方法
        FXGL.onKey(KeyCode.SPACE, () -> {
            TankCompnent playerCompnent = player.getComponent(TankCompnent.class);
            playerCompnent.shoot();
        });
    }

    @Override
    protected void initPhysics() {
        //添加物理的世界中的碰撞处理器
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(Collision.ENEMY, Collision.BULLET) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity bullet) {
                enemy.removeFromWorld();
                bullet.removeFromWorld();
                //对该分数，每次消灭敌人都自增10
                FXGL.inc("Score", +10);
                createEnemy();
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Score", 0);
    }
}
