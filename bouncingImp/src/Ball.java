import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Created by Gabriel on 5/4/2015.
 */
public class Ball {
    public static double areaWidth, areaHeight;

    public double x, y, vx, vy, r;
    public boolean collided = false;
    public Ball(double x, double y, double vx, double vy, double r){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
    }

    public void tick(double deltaT, LinkedList<Ball> collidingBalls){



        this.vy -= 9.82*deltaT;
        for(Ball b : collidingBalls){
            if(!this.collided){
                // TODO: Collision check on all balls.
                //
                double dx = this.x-b.x;
                double dy = this.y-b.y;
                double hyp= Math.sqrt(dy * dy + dx * dx);
                double angle = Math.asin(dx/hyp);

                Point2D.Double uThis = Ball.rectToPolar(this.vx,this.vy);
                Point2D.Double uB = Ball.rectToPolar(b.vx,b.vy);

//                Point2D.Double pol ;//= this.rectToPolar(vx,vy);
                Point2D.Double post;// = Ball.polarToRect(pol.getX(),pol.getY());

                double mThis = 4*Math.PI*Math.pow(this.r,3)/3;
                double mb = 4*Math.PI*Math.pow(b.r,3)/3;
                double I = -mThis*uThis.getX() + mb*uB.getX();
                double R = -(uB.getX() +uThis.getX());
//                double vB = Math.sq
                // mThis*vThis + mB(vThis + R) = I
                // mThis*vThis + mB*vThis + mB*R = I
                // vThis = (I-mB*R)/(mthis+mB)
                // vB = R+vThis
                double vThis = (I-mb*R)/(mThis+mb);
                double vB = R+vThis;
                //derping

//                pol = Ball.rectToPolar(this.vx, this.vy);
//                post = Ball.polarToRect(pol.getX(), (pol.getY() + Math.PI - 2*angle));
                post = Ball.polarToRect(vThis, (uThis.getY() + Math.PI - 2*angle));
                this.vx = post.getX();
                this.vy = post.getY();

                post = Ball.polarToRect(vB, (uB.getY() + Math.PI  - 2*angle));
//                pol = Ball.rectToPolar(b.vx, b.vy);
//                post = Ball.polarToRect(-pol.getX(), (pol.getY() + Math.PI  - 2*angle));
                b.vx = post.getX();
                b.vy = post.getY();






                b.collided = true;
            } else{
                this.collided = false;
            }
        }
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
