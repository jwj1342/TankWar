package henu.soft.tankwar;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

public class TankApp extends GameApplication {
    private Entity player;

    public static void main(String[] args) {
        launch(args);
    }

    boolean is_end_game;
    boolean is_win_game;

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
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setHeight(600);
        gameSettings.setWidth(800);
        gameSettings.setAppIcon("Tank_player.png");
        gameSettings.setTitle("TankWar");
        gameSettings.setVersion("1.0");

        //重写一下开始菜单的方法，显示作者
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                FXGLMenu before = super.newMainMenu();

                Text text = new Text("Author : jwj1342");
                text.setFill(Color.WHITE);
                text.setFont(Font.font(20));
                StackPane stackPane = new StackPane(text);
                before.getContentRoot().getChildren().addAll(stackPane);
                return before;
            }
        });

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


        is_end_game = false;
        is_win_game = false;
        for (int i = 0; i < 5; i++) {
            createEnemy();
        }

        FXGL.getip("Score").addListener((ob, ov, nv) -> {
            if (nv.intValue() > 20) {
                FXGL.getSceneService().pushSubScene(new SuccessScence());
            }
        });

    }

    @Override
    protected void initPhysics() {
        //先创建一个敌人和子弹碰撞的句柄
        CollisionHandler enemy_and_bullet = new CollisionHandler(Collision.ENEMY, Collision.BULLET) {
            @Override
            protected void onCollisionBegin(Entity enemy, Entity bullet) {
                FXGL.spawn("boom", new SpawnData().put("pos", enemy.getCenter().subtract(32, 32)));

                enemy.removeFromWorld();
                bullet.removeFromWorld();
                //对该分数，每次消灭敌人都自增10
                FXGL.inc("Score", +10);

                //createEnemy();
            }
        };
        //再把这个碰撞句柄添加到物理世界
        FXGL.getPhysicsWorld().addCollisionHandler(enemy_and_bullet);
        CollisionHandler player_and_bullet = new CollisionHandler(Collision.PLAYER, Collision.BULLET) {
            @Override
            protected void onCollisionBegin(Entity player, Entity bullet) {
                HealthIntComponent hp = player.getComponent(HealthIntComponent.class);
                hp.damage(1);
                if (hp.isZero()) {
                    FXGL.spawn("boom", new SpawnData().put("pos", player.getCenter().subtract(32, 32)));
                    player.removeFromWorld();
                    is_end_game = true;
                }
                bullet.removeFromWorld();

            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(player_and_bullet);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (is_end_game) {
            FXGL.getGameController().gotoMainMenu();
        }
        is_end_game = false;
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Score", 0);
    }
}
