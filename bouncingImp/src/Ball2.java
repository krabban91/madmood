import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * Created by Jibbs on 5/9/2015.
 */

public class Ball2 {
	public static double areaWidth, areaHeight;

	public double x, y, vx, vy, r;
	public boolean collided = false;
	public Ball2(double x, double y, double vx, double vy, double r){
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.r = r;
	}

	public void tick(double deltaT, LinkedList<Ball2> collidingBall2s){



		this.vy -= 9.82*deltaT;
		for(Ball2 b : collidingBall2s){
			if(!this.collided){

				System.out.println("\n-------------" + " \nBEFORE\n" + "\nthis.vx: " + this.vx + "\nthis.vy: "
						+ this.vy + "\nb.vx: " + b.vx + "\nb.vy: " + b.vy + "\nthis.x: " + this.x +
						"\nthis.y: " + this.y + "\nb.x: " + b.x + "\nb.y: " + b.y + "\n--------------");

				System.out.println("PANG!");

				double dx = this.x-b.x;
				double dy = this.y-b.y;
				double hyp= Math.sqrt(dy * dy + dx * dx);
				double angle;
				System.out.println("dx: " + dx + "    dy: " + dy + "    hyp: " + hyp);
				angle = Math.atan2(dy, dx);
				System.out.println("angle = " + angle);

				double mThis = 4*Math.PI*Math.pow(this.r,3)/3;
				double mb = 4*Math.PI*Math.pow(b.r,3)/3;

				Point2D.Double uThis = new Point2D.Double( (Math.cos(angle) * this.vx + Math.sin(angle) * this.vy) , (-Math.sin(angle) * this.vx + Math.cos(angle) * this.vy) );
				Point2D.Double uB = new Point2D.Double( (Math.cos(angle) * b.vx + Math.sin(angle) * b.vy) , (-Math.sin(angle) * b.vx + Math.cos(angle) * b.vy) );

				double I = mThis*(uThis.getX()) + mb*(uB.getX());
				double R = -(uB.getX() - uThis.getX());

				double thisXU = (I-mb*R)/(mThis+mb);
				double bXU = R+thisXU;

				double thisYU = uThis.getY();
				double bYU = uB.getY();


				this.x = b.x + (this.r + b.r) * (dx/hyp);
				this.y = b.y + (this.r + b.r) * (dy/hyp);

				this.vx = Math.cos(-angle) * thisXU + Math.sin(-angle) * thisYU;
				this.vy = -Math.sin(-angle) * thisXU + Math.cos(-angle) * thisYU;

				b.vx = Math.cos(-angle) * bXU + Math.sin(-angle) * bYU;
				b.vy = -Math.sin(-angle) * bXU + Math.cos(-angle) * bYU;

				System.out.println("-------------" + " \nAFTER\n" + "\nthis.vx: " + this.vx + "\nthis.vy: "
						+ this.vy + "\nb.vx: " + b.vx + "\nb.vy: " + b.vy + "\nthis.x: " + this.x +
						"\nthis.y: " + this.y + "\nb.x: " + b.x + "\nb.y: " + b.y + "\n--------------\n");

				this.collided = true;
				b.collided = true;

			} else {
				this.collided = false;
				b.collided = false;
			}
		}

		if ( this.x < this.r ) {
			this.vx *= -0.982;
			this.x = r;
		} else if ( this.x > Ball2.areaWidth - this.r ) {
			this.vx *= -0.982;
			this.x = Ball2.areaWidth - this.r;
		}

		if ( this.y < this.r ) {
			this.vy *= -0.982;
			this.y = r;
		} else if ( this.y > Ball2.areaHeight - this.r ) {
			this.vy *= -0.982;
			this.y = Ball2.areaHeight - this.r;
		}

		this.x += this.vx * deltaT;
		this.y += this.vy * deltaT;
	}

	public Ellipse2D getGraphics(){
		return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
	}

}
