import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

//Class Use: Create an object.
public class Objects extends Rectangle{

	private int w;
	private int h;
	private Image img;
	private boolean visible = true;
	
	//constuctors 
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
	 
	//setter and getter methods for the visiblity status
	 public boolean getVisibility() {
		 return this.visible;
	 }
	 public void setVisibility(boolean visible) {
	 this.visible = visible;
	 }
	 
}
