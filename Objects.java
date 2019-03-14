import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

/**
 * Class: Objects - Child of Class: Rectangle
 * Class Use: Create an Object
 * Methods:
 * 		Constructors
 * 		getVisibility
 * 		setVisibility
 *
 */
public class Objects extends Rectangle {
	
	//Instance Variables
	private int w;
	private int h;
	private Image img;
	private boolean visible = true;
	
	//Constructors
	public Objects(int x, int y, int w, int h, Image img) {
		super(w,h);
		this.w = w;
		this.h = h;
		this.img = img;
		this.setTranslateX(x);
		this.setTranslateY(y);
		this.setFill(new ImagePattern(img));
	}
	public Objects(Objects toCopy) {
		super(toCopy.w,toCopy.h);
		this.w = toCopy.w;
		this.h = toCopy.h;
		this.setTranslateX(toCopy.getTranslateX());
		this.setTranslateY(toCopy.getTranslateY());
		this.setFill(new ImagePattern(toCopy.img));
		this.visible = toCopy.visible;
	}
	
	//Get and Set Visibility
	public boolean getVisibility() {
		return this.visible;
	}
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}
}
