package henu.soft.tankwar;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;

public class TankFactory implements EntityFactory {
    @Spawns("player")
    public Entity Player(SpawnData spawnData) {
        Entity player = FXGL.entityBuilder(spawnData)
                .at(200, 200)
                .viewWithBBox("tankR.gif")
                .with(new TankCompnent())
                .with(new CollidableComponent(true))
                .build();
        player.setRotationOrigin(new Point2D(25, 25));//设置旋转的中心点
        return player;
    }

    @Spawns("bricks")
    public Entity Bricks(SpawnData spawnData) {
        Entity bricks = FXGL.entityBuilder(spawnData)
                .viewWithBBox("bricks.gif")
                .type(Collision.BRICK)
                .neverUpdated()
                .collidable()
                .build();
        return bricks;
    }

    @Spawns("enemy")
    public Entity Enemy(SpawnData spawnData) {
        Entity enemy = FXGL.entityBuilder(spawnData)
                //绑定枚举类型，为下面的子弹和敌人碰撞做准备
                .type(Collision.ENEMY)
                //随机位置生成敌人
                .at(FXGL.random(49, 800 - 60), FXGL.random(49, 600 - 60))
                .viewWithBBox("tankR.gif")
                .with(new TankCompnent())
                .collidable()
                .with(new TankEnemyComppnent())
                .build();
        enemy.setRotationOrigin(new Point2D(25, 25));
        return enemy;
    }
    @Spawns("bullet")
    public Entity Bullet(SpawnData spawnData){
        Direction bullet_dir = spawnData.get("dir");
        Point2D pos = spawnData.get("pos");

        Entity bullet = FXGL.entityBuilder()
                //为下面的碰撞枚举类型添加检测条件
                .type(Collision.BULLET)
                .at(pos.getX(), pos.getY())
                .viewWithBBox("missileR.gif")
                //子弹组件,需要传两个参数，一个是子弹位置信息，一个是子弹的速度
                .with(new ProjectileComponent(bullet_dir.getVector(), Config.BULLET_SPEED))
                //超过屏幕的组件直接移出，不然会出现内存泄露
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
        return bullet;
    }
}
