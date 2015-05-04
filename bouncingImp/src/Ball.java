import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Dictionary;
import java.util.LinkedList;

/**
 * Created by Gabriel on 5/4/2015.
 */
public class Ball {
    public static double areaWidth, areaHeight;
    private static int counter = 0;
    private static boolean[][] collisions;
    private int id;
    public double x, y, vx, vy, r;
    public boolean collided = false;
    public Ball(double x, double y, double vx, double vy, double r){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.id = counter++;
    }

    public static void resetCollisions(){
        Ball.collisions = new boolean[counter][counter];
    }

    public void tick(double deltaT, LinkedList<Ball> collidingBalls){



        for(Ball b : collidingBalls){
            if(!Ball.collisions[this.id][b.id]){

                //angle
                double dx = this.x-b.x;
                double dy = this.y-b.y;
                double hyp= Math.sqrt(dy * dy + dx * dx);
                double angle = Math.asin(dx/hyp) + Math.PI/2;

                //velocity before
                Point2D.Double uThis = Ball.rectToPolar(this.vx,this.vy);
                Point2D.Double uB = Ball.rectToPolar(b.vx,b.vy);

                Point2D.Double post;

                //energy and inertia
                double mThis = 4*Math.PI*Math.pow(this.r,3)/3;
                double mb = 4*Math.PI*Math.pow(b.r,3)/3;
                double I = mThis*uThis.getX() - mb*uB.getX();
                double R = -(uB.getX() + uThis.getX());
                //new velocities
//                mThis*vThis - mB(vThis + R) = I
//                mThis*vThis - mB*vThis + mB*R = I
//                vThis = (I-mB*R)/(mthis-mB)
                double vThis = (I-mb*R)/(mThis+mb);
//                vB = R+vThis
                double vB = R+vThis;

                //set new values.
                post = Ball.polarToRect(vThis, (uThis.getY() - (Math.PI/2 + angle)));
                this.vx = post.getX();
                this.vy = post.getY();

                post = Ball.polarToRect(vB, (uB.getY() - (Math.PI/2 + angle)));
                b.vx = post.getX();
                b.vy = post.getY();
                //collision happened.
                collisions[b.id][this.id] = true;
            }
        }

        if ( this.x < this.r ) {
            this.vx *= -1;
            this.x = r;
        } else if ( this.x > Ball.areaWidth - this.r ) {
            this.vx *= -1;
            this.x = Ball.areaWidth - this.r;
        }

        this.vy -= 9.82*deltaT;
        if ( this.y < this.r ) {
            this.vy *= -0.999;
            this.y = r;
        } else if ( this.y > Ball.areaHeight - this.r ) {
            this.vy *= -0.999;
            this.y = Ball.areaHeight - this.r;
        }

        this.x += this.vx * deltaT;
        this.y += this.vy * deltaT;
    }

    public Ellipse2D getGraphics(){
        return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    }

    public static Point2D.Double polarToRect(double vel, double angle){
        //vx and vy.
        double x = Math.sin(angle)*vel;
        double y = Math.cos(angle)*vel;
        return new Point2D.Double(x,y);
    }

    public static Point2D.Double rectToPolar(double x, double y){
        double v = Math.sqrt(x*x+y*y);
        double angle = Math.asin(x / v);
        //velocity and angle
        return new Point2D.Double(v,angle);
    }
}
