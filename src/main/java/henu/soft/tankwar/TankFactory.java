package henu.soft.tankwar;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;

public class TankFactory implements EntityFactory {
    @Spawns("player")
    public Entity Player(SpawnData spawnData) {
        Entity player = FXGL.entityBuilder(spawnData)
                .at(200, 200)
                .viewWithBBox("tankR.gif")
                .with(new TankCompnent())
                .build();
        player.setRotationOrigin(new Point2D(25, 25));//设置旋转的中心点
        return player;
    }
}
