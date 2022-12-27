package henu.soft.tankwar;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class TankCompnent extends Component {
    private LocalTimer shoot_timer = FXGL.newLocalTimer();
    ;
    private boolean isMoving = false;
    private Direction dir = Direction.RIGHT;

    public void move_up() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(270);
        entity.translateY(-5);
        dir = Direction.UP;
    }

    public void move_down() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(90);
        entity.translateY(5);
        dir = Direction.DOWN;
    }

    public void move_left() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(180);
        entity.translateX(-5);
        dir = Direction.LEFT;
    }

    public void move_right() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(0);
        entity.translateX(5);
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
    }
}
