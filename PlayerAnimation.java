import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Class: PlayerAnimation - Child of Class: Transition
 * Class Use: Animating the Avatar
 * Methods:
 * 		Constructor
 * 		setOffsetX
 * 		setOffsetY
 * 		interpolate
 * Reference: https://www.youtube.com/watch?v=lJnPtR34FSg
 *
 */
public class PlayerAnimation extends Transition {
	//Instance Variables
	private final ImageView imageView;
	private final int COUNT;
	private final int COLUMNS;
	private final int WIDTH = 20;
	private final int HEIGHT = 32;
	private int offsetX;
	private int offsetY;
	
	/**Constructor
	 * Parameters:
	 * 		imageView
	 * 		duration
	 * 		count
	 * 		columns
	 * 		offsetX
	 * 		offsetY
	 * 		width
	 * 		height
	 */
	public PlayerAnimation (ImageView imageView, Duration duration, int count, int columns, int offsetX, int offsetY) {
		this.imageView = imageView;
		this.COUNT = count;
		this.COLUMNS = columns;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		//Length of time for one cycle of animation (in milliseconds)
		setCycleDuration(duration);
		//Number of Cycles in an animation
		setCycleCount(Animation.INDEFINITE);
		//Type of Interpolation (How the animation is read)
		setInterpolator(Interpolator.LINEAR);
		this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, WIDTH, HEIGHT));
	}
	
	//Setters
	public void setOffsetX(int x) {
		this.offsetX = x;
	}
	
	public void setOffsetY(int y) {
		this.offsetY = y;
	}
	
	/*
	 * Index = Frame #
	 * Columns = Co-ordinate of Frame, where each frame increases the column by one (In Linear Interpolation)
	 * Width = How wide each frame is
	 * Height = How tall each frame is
	 * OffsetX = How far in the X axis the frames should be shifted
	 * OffsetY = How far in the Y axis the frames should be shifted
	 * 
	 */
	protected void interpolate(double value) {
		final int index = Math.min((int)Math.floor(value*COUNT), COUNT-1);
		final int x = (index%COLUMNS) * WIDTH + offsetX;
		final int y = (index/COLUMNS) * HEIGHT + offsetY;
		imageView.setViewport(new Rectangle2D(x,y, WIDTH, HEIGHT));	
	}
}
