package henu.soft.tankwar;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;

public class TankCompnent extends Component {
    private LocalTimer shoot_timer = FXGL.newLocalTimer();
    ;
    private boolean isMoving = false;


    //使用动态距离，来定义每一帧移动多少
    private double distence;

    public Direction getDir() {
        return dir;
    }

    private Direction dir = Direction.RIGHT;
    private LazyValue<EntityGroup> entityGroupLazyValue
            = new LazyValue<>(() -> FXGL.getGameWorld().getGroup(Collision.ENEMY, Collision.BRICK));


    public void move_up() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(270);
//        entity.translateY(-distence);
        move();
        dir = Direction.UP;
    }

    public void move_down() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(90);
//        entity.translateY(distence);
        move();
        dir = Direction.DOWN;
    }

    public void move_left() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(180);
//        entity.translateX(-distence);
        move();
        dir = Direction.LEFT;
    }

    public void move_right() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(0);
        //entity.translateX(distence);
        move();
        dir = Direction.RIGHT;
    }

    public void shoot() {
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
                .at(entity.getCenter().getX(), entity.getCenter().getY())
                .viewWithBBox("missileR.gif")
                //子弹组件
                .with(new ProjectileComponent(bullet_dir, 500))
                //超过屏幕的组件直接移出，不然会出现内存泄露
                .with(new OffscreenCleanComponent())
                .collidable()
                .buildAndAttach();
    }

    @Override
    public void onUpdate(double tpf) {
        isMoving = false;
        distence = tpf * Config.TANK_SPEED;
    }

    public void move() {
        int len = (int) distence;
        List<Entity> blockList = entityGroupLazyValue.get().getEntitiesCopy();
        boolean isCollision = false;
        for (int i = 0; i < len; i++) {
            entity.translate(dir.getVector().getX() * len /*Config.SPEED_NOR*/, dir.getVector().getY() * len/*Config.SPEED_NOR*/);
            System.out.println("finish");
            for (var j : blockList) {
                if (entity.isColliding(j) && j != entity) {
                    isCollision = true;
                    break;
                }
            }
            if (isCollision) {
                entity.translate(-dir.getVector().getX() * len/*Config.SPEED_NOR*/, -dir.getVector().getY() * len/*Config.SPEED_NOR*/);
                break;
            }
        }
    }
}
