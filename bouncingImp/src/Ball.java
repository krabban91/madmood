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
    //Used to give a unique id for each ball
    private static int counter = 0;
    private int id;
    //Used to see if two balls has collided with each other.
    private static boolean[][] collisions;

    public double x, y, vx, vy, r;
    public Ball(double x, double y, double vx, double vy, double r){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.id = counter++;
    }

    //Clearing the collisions
    public static void resetCollisions(){
        Ball.collisions = new boolean[counter][counter];
    }

    public void tick(double deltaT, LinkedList<Ball> collidingBalls){

        //Collision with each ball in contact
        for(Ball b : collidingBalls){
            //Check if the collision already has been corrected.
            if(!Ball.collisions[this.id][b.id]){

                //angle
                double dx = this.x-b.x;
                double dy = this.y-b.y;
                Point2D.Double anglePoint = Ball.rectToPolar(dx, dy);
                double angle = anglePoint.getY();

                //Correct distance between balls.
                if(Math.abs(anglePoint.getX())<this.r+b.r) {
                    Point2D.Double post = Ball.polarToRect(this.r + b.r-Math.abs(anglePoint.getX()),angle);
                    if(this.x + post.getX() > r && this.x + post.getX() < Ball.areaWidth - r &&
                            this.y + post.getY() > r && this.y + post.getY() < Ball.areaHeight - r) {
                        this.x += post.getX();
                        this.y += post.getY();
                    }else{
                        post = Ball.polarToRect(this.r + b.r-Math.abs(anglePoint.getX()),-angle);
                        b.x += post.getX();
                        b.y += post.getY();
                    }
                }

                //turn up angle (adjusting to a rotated axis system.
                Point2D.Double thisXVectors = polarToRect(this.vx, -angle);
                Point2D.Double thisYVectors = polarToRect(this.vy, Math.PI/2-angle);
                double thisXTotal = thisXVectors.getX()+thisYVectors.getX();

                Point2D.Double bXVectors = polarToRect(b.vx, -angle);
                Point2D.Double bYVectors = polarToRect(b.vy, Math.PI/2-angle);
                double bXTotal = bXVectors.getX()+bYVectors.getX();

                //Inertia & energy
                double mThis = 4*Math.PI*Math.pow(this.r,3)/3;
                double mb = 4*Math.PI*Math.pow(b.r,3)/3;
                double I = mThis*thisXTotal + mb*bXTotal;
                double R = -(bXTotal - thisXTotal);
                double vThis = (I-mb*R)/(mThis+mb);
                double vB = R+vThis;

                //Converting back to the real x and y system
                Point2D.Double thisNewXVectors = polarToRect(vThis,angle);
                Point2D.Double thisNewYVectors = polarToRect(thisXVectors.getY()+thisYVectors.getY(),Math.PI/2+angle);
                this.vx = thisNewXVectors.getX()+thisNewYVectors.getX();
                this.vy = thisNewXVectors.getY()+thisNewYVectors.getY();

                Point2D.Double bNewXVectors = polarToRect(vB,+angle);
                Point2D.Double bNewYVectors = polarToRect(bXVectors.getY()+bYVectors.getY(),Math.PI/2+angle);
                b.vx = bNewXVectors.getX()+bNewYVectors.getX();
                b.vy = bNewXVectors.getY()+bNewYVectors.getY();

                //collision happened.
                collisions[b.id][this.id] = true;
            }
        }

        // Collision with walls on the sides. adjusting for balls closing onto walls.
        if ( this.x < this.r ) {
            this.vx *= -1;
            this.x = r;
        } else if ( this.x > Ball.areaWidth - this.r ) {
            this.vx *= -1;
            this.x = Ball.areaWidth - this.r;
        }

        // Collision with floor and roof. adjusting for balls being to close.
        if ( this.y < this.r ) {
            this.vy *= -1;
            this.y = r;
        } else if ( this.y > Ball.areaHeight - this.r ) {
            this.vy *= -1;
            this.y = Ball.areaHeight - this.r;
        }
        //gravity
        this.vy -= 9.82*deltaT;

        //Calculating new positions.
        this.x += this.vx * deltaT;
        this.y += this.vy * deltaT;
    }

    public Ellipse2D getGraphics(){
        return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    }

    //Takes in a velocity and an angle and converts it into two velocities along the x and y axis.
    public static Point2D.Double polarToRect(double vel, double angle){
        //vx and vy.
        double x = Math.cos(angle)*vel;
        double y = Math.sin(angle)*vel;
        return new Point2D.Double(x,y);
    }

    //Takes in the x and y velocities, returns their combined velocity (positive value and an angle)
    public static Point2D.Double rectToPolar(double x, double y){
        //velocity and angle
        double v = Math.sqrt(x*x+y*y);
        double angle = Math.atan2(y ,x);
        return new Point2D.Double(v,angle);
    }
}
