package henu.soft.tankwar;

import com.almasb.fxgl.dsl.FXGL;
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
                .with(new TankEnemyComppnent())
                .with(new CollidableComponent(true))
                .collidable()
                .build();
        enemy.setRotationOrigin(new Point2D(25, 25));//设置旋转的中心点
        return enemy;
    }
}
