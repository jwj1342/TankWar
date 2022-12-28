package henu.soft.tankwar;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityGroup;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import java.util.List;

public class TankCompnent extends Component {
    //shoot_timer用来计算子弹发射的间隔
    private final LocalTimer shoot_timer = FXGL.newLocalTimer();

    //isMoving用来判断用户是否一次按两个键
    private boolean isMoving = false;


    //使用动态距离，来定义每一帧移动多少
    private double distence;

    //getDir是Dir的获取器，dir用于初始化坦克的朝向
    public Direction getDir() {
        return dir;
    }
    private Direction dir = Direction.RIGHT;

    //下面这个懒值是用来记录可碰撞的实体名单，方便移动的时侯检测。
    private final LazyValue<EntityGroup> entityGroupLazyValue
            = new LazyValue<>(() -> FXGL.getGameWorld().getGroup(Collision.ENEMY, Collision.BRICK,Collision.PLAYER));


    public void move_up() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(270);
        move();
        dir = Direction.UP;
    }

    public void move_down() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(90);
        move();
        dir = Direction.DOWN;
    }

    public void move_left() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(180);
        move();
        dir = Direction.LEFT;
    }

    public void move_right() {
        if (isMoving) {
            return;
        }
        isMoving = true;
        entity.setRotation(0);
        move();
        dir = Direction.RIGHT;
    }

    public void shoot() {
        //子弹发射计时器，如果间隔时间还没到就别发射
        if (!shoot_timer.elapsed(Duration.seconds(0.5))) {
            return;
        }
        //子弹结束要对定时器归零
        shoot_timer.capture();
        //从工厂中抽出一个子弹试题，给两个参数，一个是发射子弹的方向，一个是子弹开始的位置
        FXGL.spawn("bullet",new SpawnData()
                .put("dir",dir)
                .put("pos",entity.getCenter())
                .put("type",entity.getType()));

    }

    @Override
    public void onUpdate(double tpf) {
        //每次更新的时侯都要对子弹是否可以持续射出做判断
        isMoving = false;
        //distence 因为帧率的更新，所以每次移动的距离要相应的更新。
        distence = tpf * Config.TANK_SPEED;
    }

    public void move() {
        int len = (int) distence;
        //首先将全局变量中的碰撞清单获取到方法内部
        List<Entity> blockList = entityGroupLazyValue.get().getEntitiesCopy();
        //isCollision用来记录是否发生了碰撞
        boolean isCollision = false;
        //第一个for用来遍历行走的方向，可以理解为一个雷达
        for (int i = 0; i < len; i++) {
            //实体首先先进行移动
            entity.translate(dir.getVector().getX() * len , dir.getVector().getY() * len);
            //对刚才移动距离内是否有碰撞的产生做判断，迭代一下可碰撞的清单
            for (var j : blockList) {
                //一旦发现了碰撞并且不是自己碰撞自己，那么就记录一下赶紧break，不要在雷达的方向上再移动了
                if (entity.isColliding(j) && j != entity) {
                    isCollision = true;
                    break;
                }
            }
            //当发生碰撞了之后，要把之前走过的举例反向走一遍
            if (isCollision) {
                entity.translate(-dir.getVector().getX() * len, -dir.getVector().getY() * len);
                break;
            }
        }
    }
}
