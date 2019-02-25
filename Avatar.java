import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

//Class Use: Create and Move Avatar Character
public class Avatar extends Rectangle{
	
	//3 Instance Variables
	private boolean canJump = true;
	Point2D velocity = new Point2D(0, 0);
	Image img = new Image("player.png");
	
	//Constructor: (Location X, Location Y, Width, Height)
	 public Avatar(int x, int y, int w, int h) {
		 
		    super(w,h);
	        this.setTranslateX(x);
	        this.setTranslateY(y);
	        this.setFill(new ImagePattern(img));
	        this.getProperties().put("alive", true);
	    }
	 
    /**
     * Method: Movement of Avatar along the X-Axis. 
     * Parameter: value (The higher this value, the faster the movement.)
     * Parameter: platforms (Detect the locations of the platforms.)
	 */
    public void movePlayerX(int value, ArrayList<Node> platforms) {
    	
    	//Finds if movement is Left or Right
        boolean movingRight;
        if (value > 0) {
        	movingRight = true;
        } else {
        	movingRight = false;
        }
        
        //For each step, will check if the front is clear; if so, move.
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (this.getTranslateX() + 32 == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (this.getTranslateX() == platform.getTranslateX() + 32) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    /**
     * Method: Movement of Avatar along the Y-Axis. 
     * Parameter: value (The higher this value, the faster the jump.)
     * Parameter: platforms (Detect the locations of the platforms.)
	 */
    public void movePlayerY(int value,ArrayList<Node> platforms) {
    	 boolean movingDown;
         if (value > 0) {
         	movingDown = true;
         } else {
         	movingDown = false;
         }
         
       //Checks if the avatar is on the floor.
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getTranslateY() + 32 == platform.getTranslateY()) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (this.getTranslateY() == platform.getTranslateY() + 32) {
     
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    //Jump Method
    public void jumpPlayer() {
    	   if (canJump) {
               velocity = velocity.add(0, -16.8);
               canJump = false;
           }
    }
    	     
    	       
 
}
