import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

//a class for making the object Player
public class Avatar extends Rectangle{
	
	//instance variables
	private boolean canJump = true;
	Point2D velocity = new Point2D(0, 0);
	Image img = new Image("player.png");
	
	//constructor which takes the dimensions and the location of the rectangle player.
	 public Avatar(int x, int y, int w, int h) {
		 
		    super(w,h);
	        this.setTranslateX(x);
	        this.setTranslateY(y);
	        this.setFill(new ImagePattern(img));
	        this.getProperties().put("alive", true);
	    }
	 
    /*
     * method for moving the player on the X Axis. It takes two parameters: value (the higher the value, the faster player moves.)
	 and the array of platforms to detect the walls and the floor.
	 */
    public void movePlayerX(int value, ArrayList<Node> platforms) {
    	
    	//finds the direction of movement
        boolean movingRight;
        if (value > 0) {
        	movingRight = true;
        } else {
        	movingRight = false;
        }
        
        //for each step, checks if front is clear.
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

    /*
     * method for moving the player on the Y Axis. It takes two parameters: value (the higher the value, the higher player jumps.)
	 and the array of platforms to detect the walls and the floor.
	 */
    public void movePlayerY(int value,ArrayList<Node> platforms) {
    	 boolean movingDown;
         if (value > 0) {
         	movingDown = true;
         } else {
         	movingDown = false;
         }
         
       //check if the player is on the floor.
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

    //gravity effect
    public void jumpPlayer() {
    	   if (canJump) {
               velocity = velocity.add(0, -16.8);
               canJump = false;
           }
    }
    	     
    	       
 
}

