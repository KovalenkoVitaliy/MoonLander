import com.javarush.engine.cell.*;
import com.javarush.engine.cell.Color;

import java.util.ArrayList;
import java.util.List;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        List<int [][]> frames1 = new ArrayList<>();
        frames1.add(ShapeMatrix.FIRE_DOWN_1);
        frames1.add(ShapeMatrix.FIRE_DOWN_2);
        frames1.add(ShapeMatrix.FIRE_DOWN_3);

        List<int [][]> frames2 = new ArrayList<>();
        frames2.add(ShapeMatrix.FIRE_SIDE_1);
        frames2.add(ShapeMatrix.FIRE_SIDE_2);

        List<int [][]> frames3 = new ArrayList<>();
        frames3.add(ShapeMatrix.FIRE_SIDE_1);
        frames3.add(ShapeMatrix.FIRE_SIDE_2);

        downFire = new RocketFire(frames1);
        leftFire = new RocketFire(frames2);
        rightFire = new RocketFire(frames3);
    }

    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed){
        if (isLeftPressed) {
            leftFire.x = this.x + this.width;
            leftFire.y = this.y + this.height;
            leftFire.show();
        } else {
            leftFire.hide();
        }
        if (isRightPressed){
            rightFire.x = this.x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = this.y + this.height;
            rightFire.show();
        } else {
            rightFire.hide();
        }
        if (isUpPressed) {
            downFire.x = this.x + this.width / 2;
            downFire.y = this.y + this.height;
            downFire.show();
        } else {
            downFire.hide();
        }
    }

    public void land() {
        y= y -1;
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed, isLeftPressed, isRightPressed);
    }

    public void draw(Game game){
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }

    public void crash() {
        this.matrix = ShapeMatrix.ROCKET_CRASH;

    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }
}
