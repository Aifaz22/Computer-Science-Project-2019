import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

//refrence: https://www.youtube.com/watch?v=lJnPtR34FSg
public class PlayerAnimation extends Transition{
	
	private final ImageView imageView;
	private final int COUNT;
	private final int COLUMNS;
	private int offsetX;
	private int offsetY;
	private final int WIDTH;
	private final int HEIGHT;
	
	//private int lastIndex;
	
	public PlayerAnimation (ImageView imageView, Duration duration, int count, int columns, int offsetX, int offsetY, int width, int height){
		this.imageView = imageView;
		this.COUNT = count;
		this.COLUMNS = columns;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.WIDTH = width;
		this.HEIGHT = height;
		
		//duration of one cycle of animation (time that it plays and the time that it ends)
		setCycleDuration(duration);
		//defines how much cycles are in your animation 
		setCycleCount(Animation.INDEFINITE);
		
		setInterpolator(Interpolator.LINEAR);
		this.imageView.setViewport(new Rectangle2D(offsetX, offsetY,width, height));
	}
	protected void interpolate(double value){
		final int index = Math.min((int)Math.floor(value*COUNT), COUNT-1);

				final int x = (index%COLUMNS)*WIDTH + offsetX;
				final int y = (index/COLUMNS) * HEIGHT + offsetY;
				imageView.setViewport(new Rectangle2D(x,y, WIDTH, HEIGHT));
			
	}
	
	public void setOffsetX(int x){
		this.offsetX = x;
		
	}
	
	public void setOffsetY(int y){
		this.offsetY = y;
		
	}
	
	
}
