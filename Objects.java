import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

//Class Use: Create an object.
public class Objects extends Rectangle{
	
	 public Objects(int x, int y, int w, int h, Image img) {
		 
		    super(w,h);
	        this.setTranslateX(x);
	        this.setTranslateY(y);
	        this.setFill(new ImagePattern(img));
	        this.getProperties().put("alive", true);
	    }
}