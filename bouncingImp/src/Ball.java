import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

/**
 * Created by Gabriel on 5/4/2015.
 */
public class Ball {
    public static double areaWidth, areaHeight;

    public double x, y, vx, vy, r;

    public Ball(double x, double y, double vx, double vy, double r){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
    }

    public void tick(double deltaT, LinkedList<Ball> balls){
        // TODO: Collision check on all balls.
        //

        this.vy -= 9.82*deltaT;
        if (this.x < this.r || this.x > Ball.areaWidth - this.r) {
            this.vx *= -1;
        }
        if (this.y < this.r || this.y > Ball.areaHeight - this.r) {
            this.vy *= -1;
        }

        this.x += this.vx * deltaT;
        this.y += this.vy * deltaT;
    }

    public Ellipse2D getGraphics(){
        return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    }
}
