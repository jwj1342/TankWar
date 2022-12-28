package henu.soft.tankwar;

import com.almasb.fxgl.animation.Animatable;
import com.almasb.fxgl.animation.AnimatedColor;
import com.almasb.fxgl.animation.AnimatedPath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class TankFactory implements EntityFactory {
    @Spawns("player")
    public Entity Player(SpawnData spawnData) {
        Entity player = FXGL.entityBuilder(spawnData)
                .type(Collision.PLAYER)
                .at(200, 200)
                .viewWithBBox("TankPlayer.png")
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
                .at(FXGL.random(50, 800 - 60*2), FXGL.random(50, 600 - 60*2))
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

        CollidableComponent collidableComponent = new CollidableComponent(true);
        collidableComponent.addIgnoredType(spawnData.get("type"));
        Entity bullet = FXGL.entityBuilder()
                //为下面的碰撞枚举类型添加检测条件
                .type(Collision.BULLET)
                .at(pos.getX(), pos.getY())
                .viewWithBBox("missileR.gif")
                //子弹组件,需要传两个参数，一个是子弹位置信息，一个是子弹的速度
                .with(new ProjectileComponent(bullet_dir.getVector(), Config.BULLET_SPEED))
                //超过屏幕的组件直接移出，不然会出现内存泄露
                .with(new OffscreenCleanComponent())
                .with(collidableComponent)
                .collidable()
                .build();
        return bullet;
    }

    @Spawns("boom")
    public Entity Boom(SpawnData spawnData){
        //加载爆炸的动画
        AnimationChannel animationChannel = new AnimationChannel(FXGL.image("boom.png"),
                Duration.seconds(0.2),7);
        AnimatedTexture animatedTexture = new AnimatedTexture(animationChannel);

        //播放爆炸音效
        FXGL.play("boom_sound.wav");
        Point2D pos = spawnData.get("pos");
        Entity boom = FXGL.entityBuilder()
                .view(animatedTexture.play())
                .at(pos.getX(), pos.getY())
                //组件超市消除
                .with(new ExpireCleanComponent(Duration.seconds(0.7)))
                .build();

        return boom;
    }
}
