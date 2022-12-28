package henu.soft.tankwar;

import com.almasb.fxgl.core.math.FXGLMath;

public class TankEnemyComppnent extends TankCompnent {

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
        //todo:需要解决的问题
        //todo:解决敌人坦克的移动问题
        //todo:解决敌人坦克的子弹发射问题
        //todo:解决自己坦克的血量问题
        //todo:解决最后游戏胜利问题
    }
}
