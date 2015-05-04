import javax.lang.model.type.IntersectionType;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

    private LinkedList<Ball> balls = new LinkedList<Ball>();

	public DummyModel(double width, double height) {
        Ball.areaWidth = width;
        Ball.areaHeight = height;
//        balls.add(new Ball(3,7,0,1,0.5));
//        balls.add(new Ball(3,2,0,1,1));
        balls.add(new Ball(width/2,height/2,2,2,1));
        balls.add(new Ball(width-1,height-1,-2,2,1));
        balls.add(new Ball(2,2,2,0,1));
        balls.add(new Ball(3,7,0,1,1));
		balls.add(new Ball(width/3,height/3,3,6,2));
		//balls.add(new Ball(6.7,2.7,-2,2,0.5));
	}

	@Override
	public void tick(double deltaT) {
        Ball.resetCollisions();
        for(Ball b : balls){
            LinkedList<Ball> collidingBalls = new LinkedList<Ball>();
            for(Ball o : balls){
                if(o != b && colliding(o,b)){
                    collidingBalls.add(o);
                }
            }
            b.tick(deltaT, collidingBalls);
        }
        for(Ball b : balls){
           // b.collided = false;
        }
    }

	@Override
	public List<Ellipse2D> getBalls() {

        List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
        for(Ball b : balls){
            myBalls.add(b.getGraphics());
        }
		return myBalls;
	}

    public static boolean colliding(Ball fst, Ball snd){
        double x = Math.abs(fst.x - snd.x);
        double y = Math.abs(fst.y - snd.y);
        double dist = Math.sqrt(x*x + y*y);
        return dist <= (fst.r + snd.r);
    }
}