package henu.soft.tankwar;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

public class TankEnemyComppnent extends Component {

    Direction enemy_move_dir = Direction.UP;
    private TankCompnent tankCompnent;

    @Override
    public void onUpdate(double tpf) {
        enemy_move_dir = tankCompnent.getDir();
        if (FXGLMath.randomBoolean(0.025)) {
            //使用随机把枚举类型中的值用get抽取
            enemy_move_dir = FXGLMath.random(Direction.values()).get();
        }
        switch (enemy_move_dir) {
            case RIGHT -> tankCompnent.move_right();
            case LEFT -> tankCompnent.move_left();
            case UP -> tankCompnent.move_up();
            case DOWN -> tankCompnent.move_down();
            default -> {

            }
        }
        if (FXGLMath.randomBoolean(0.05)){
            tankCompnent.shoot();
        }
    }
}
