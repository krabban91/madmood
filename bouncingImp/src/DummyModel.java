import javax.lang.model.type.IntersectionType;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

    private LinkedList<Ball> balls = new LinkedList<Ball>();

	public DummyModel(double width, double height) {
        Ball.areaWidth = width;
        Ball.areaHeight = height;
        //TODO: Add balls here
        balls.add(new Ball(1,1,2,2,1));
        balls.add(new Ball(width-1,height-1,-2,2,2));
        balls.add(new Ball(2,2,2,0,1));
        balls.add(new Ball(3,7,0,1,1));
		balls.add(new Ball(width/3,height/3,3,6,1));
//        balls.add(new Ball(5,6,1,-2,2));
//        balls.add(new Ball(5,2,-1,0,1));
//        balls.add(new Ball(2,1,0,0,1));
//        balls.add(new Ball(4,1,-2,0,1));
	}

    //Checks each ball if colliding with another and runs all the balls functions.
	@Override
	public void tick(double deltaT) {
        Ball.resetCollisions();
        for(Ball b : balls){
            //Checking collision
            LinkedList<Ball> collidingBalls = new LinkedList<Ball>();
            for(Ball o : balls){
                if(o != b && colliding(o,b)){
                    collidingBalls.add(o);
                }
            }
            //Calling the tick function for each ball. collidingBalls contains the ones that are relevant.
            b.tick(deltaT, collidingBalls);
        }
    }

    // Gets the graphic objects for all balls.
	@Override
	public List<Ellipse2D> getBalls() {

        List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
        for(Ball b : balls){
            myBalls.add(b.getGraphics());
        }
		return myBalls;
	}

    //Checks if the distance between two balls are smaller than the sum of their radius.
    public static boolean colliding(Ball fst, Ball snd){
        double x = Math.abs(fst.x - snd.x);
        double y = Math.abs(fst.y - snd.y);
        double dist = Math.sqrt(x*x + y*y);
        return dist <= (fst.r + snd.r);
    }
}