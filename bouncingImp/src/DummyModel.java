import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

    private LinkedList<Ball> balls = new LinkedList<Ball>();

	public DummyModel(double width, double height) {
        Ball.areaWidth = width;
        Ball.areaHeight = height;
        balls.add(new Ball(width/2,height/2,2,2,1));
        balls.add(new Ball(2.7,2.7,-2,2,1));
	}

	@Override
	public void tick(double deltaT) {
        for(Ball b : balls){
            b.tick(deltaT, balls);
        }
    }

	@Override
	public List<Ellipse2D> getBalls() {
        List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
        for(Ball b : balls){
            // TODO: only send in the balls that are colliding. "the touch balls"
            myBalls.add(b.getGraphics());
        }
		return myBalls;
	}
}