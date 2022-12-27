package henu.soft.tankwar;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

public class TankApp extends GameApplication {
    private Entity player;
    private Direction dir;
    private boolean isMoving;
    private LocalTimer shoot_timer;

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
        player = FXGL.entityBuilder()
                .at(200, 200)
                /*//view是决定了实体的贴图
                .view("tankR.gif")
                //bbox决定了游戏实体的真实大小
                .bbox(BoundingShape.box(50,50))*/
                //viewWithBBox是同时执行上面两个东西
                .viewWithBBox("tankR.gif")
                .buildAndAttach();
        player.setRotationOrigin(new Point2D(25, 25));//设置旋转的中心点
        //初始化最开始的子弹以及坦克方向向右
        dir = Direction.RIGHT;
        //设置最开始的时侯方向锁定打开
        isMoving = false;
        //初始化射击间隔计时器
        shoot_timer = FXGL.newLocalTimer();

        createEnemy();
    }

    private void createEnemy() {
        FXGL.entityBuilder()
                //绑定枚举类型，为下面的子弹和敌人碰撞做准备
                .type(Collision.ENEMY)
                //随机位置生成敌人
                .at(FXGL.random(10, 500), FXGL.random(10, 500))
                .viewWithBBox("tankD.gif")
                //给敌人加上可碰撞属性（别忘了给子弹也加上可碰撞）
                .collidable()
                .buildAndAttach();
    }


    @Override
    protected void initUI() {
        Text text = FXGL.addVarText("Score", 20, 20);
        //首先接触绑定，之后再设置相关的属性
        text.fontProperty().unbind();
        text.setFill(Color.RED);

        text.setFont(Font.font(25));
//        FXGL.addUINode(text);
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            //下面的代码是锁定单方向移动
            if (isMoving) {
                return;
            }
            isMoving = true;
            //下面的代码是设置按键事件的旋转与移动
            player.setRotation(0);
            player.translateX(5); // move right 5 pixels

            //下面的代码是设置子弹射出的方向
            dir = Direction.RIGHT;
        });

        FXGL.onKey(KeyCode.A, () -> {
            if (isMoving) {
                return;
            }
            isMoving = true;
            player.setRotation(180);
            player.translateX(-5); // move left 5 pixels

            dir = Direction.LEFT;
        });

        FXGL.onKey(KeyCode.W, () -> {
            if (isMoving) {
                return;
            }
            isMoving = true;
            player.setRotation(270);
            player.translateY(-5); // move up 5 pixels

            dir = Direction.UP;
        });

        FXGL.onKey(KeyCode.S, () -> {
            if (isMoving) {
                return;
            }
            isMoving = true;
            player.setRotation(90);
            player.translateY(5); // move down 5 pixels

            dir = Direction.DOWN;
        });


        //为空格键添加匿名事件方法
        FXGL.getInput().addAction(new UserAction("shoot") {
            @Override
            protected void onAction() {
                //使用定时器判断两次子弹发射时间是否大于0.5秒
                if (!shoot_timer.elapsed(Duration.seconds(0.5))) {
                    return;
                }
                //反射子弹结束要对定时器归零
                shoot_timer.capture();
                //下面是子弹发射方向的判断
                Point2D bullet_dir = new Point2D(1, 0);
                switch (dir) {
                    case UP -> bullet_dir = new Point2D(0, -1);
                    case DOWN -> bullet_dir = new Point2D(0, 1);
                    case LEFT -> bullet_dir = new Point2D(-1, 0);
                    case RIGHT -> bullet_dir = new Point2D(1, 0);
                }
                //创建子弹实体，完成相应的设计事件
                Entity bullet = FXGL.entityBuilder()
                        //为下面的碰撞枚举类型添加检测条件
                        .type(Collision.BULLET)
                        .at(player.getCenter().getX(), player.getCenter().getY())
                        .viewWithBBox("missileR.gif")
                        //子弹组件
                        .with(new ProjectileComponent(bullet_dir, 500))
                        //超过屏幕的组件直接移出，不然会出现内存泄露
                        .with(new OffscreenCleanComponent())
                        .collidable()
                        .buildAndAttach();


            }
        }, KeyCode.SPACE);
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
        //当每次移动的时侯都要将这个变量设置为false这样就可以让坦克移动起来
        isMoving = false;

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Score", 0);
    }
}
