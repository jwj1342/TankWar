package henu.soft.tankwar;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class TankEnemyComppnent extends Component {
    Direction enemy_move_dir = Direction.UP;

    @Override
    public void onUpdate(double tpf) {
        LocalTimer set_turn = FXGL.newLocalTimer();
        //设置坦克的概率(0.025)转向
        if (FXGLMath.randomBoolean(0.025)) {
            //使用随机把枚举类型中的值用get抽取
            enemy_move_dir = FXGLMath.random(Direction.values()).get();
            set_turn.capture();
        }
        //如果计时器的时间没到那就先不着急转向
        if (!set_turn.elapsed(Duration.seconds(2))) return;
        int speed = 2;
        switch (enemy_move_dir) {
            case RIGHT -> {
                this.entity.setRotation(0);
                this.entity.translateX(speed);
            }
            case LEFT -> {
                this.entity.setRotation(180);
                this.entity.translateX(-speed);
            }
            case UP -> {
                this.entity.setRotation(270);
                this.entity.translateY(speed);
            }
            case DOWN -> {
                this.entity.setRotation(90);
                this.entity.translateY(-speed);
            }
        }
    }
}
