package henu.soft.tankwar;

public interface Config {
    //把每一次的移动距离，不用像素来移动，而是使用动态的距离
    int TANK_SPEED = 300;
    //子弹发射速度一般来说要大于坦克移动速度
    int BULLET_SPEED = 500;
}
