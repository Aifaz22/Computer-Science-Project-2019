import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.awt.Graphics;
import javafx.util.Duration;
import javafx.util.Duration;

//import java.awt.Image;
import javax.imageio.ImageIO;
import javafx.scene.layout.Pane;


//Class Use: Create and Move Avatar Character
public class Avatar extends Pane{
	
	//Instance Variables
	private int deathCount = 0;
	private boolean canJump = true;
	private ArrayList<Node> obstacles = new ArrayList<Node>();
	Point2D velocity = new Point2D(0, 0); 
	private int x,y,w,h;
	Image img = new Image("Images/player.png");
	public boolean movingRight;

	private ImageView imageView;
	int count = 9;
	int colunms = 10;
	int offsetX=1;
	int offsetY=1;
	
	PlayerAnimation animation;
	
	Image img1  = new Image("Images/tile.png");
	
    String jump_name = ("jump.wav");
    Media jump_sound = new Media(new File(jump_name).toURI().toString());
    MediaPlayer jump = new MediaPlayer(jump_sound);
	
	//Constructor: (Location X, Location Y, Width, Height)
	 public Avatar(int x, int y, int w, int h, ArrayList<Node> floors, ArrayList<Node> walls, ArrayList<Node> doors, ImageView imageView) {
		 
		 this.imageView = imageView;
		 this.getChildren().add(imageView);
		 
			this.x =x;
			this.y =y;
			this.w =w;
			this.h =h;
	        this.setTranslateX(x);
	        this.setTranslateY(y);
	   
	        this.getProperties().put("alive", true);
	        for (Node floor : floors) {
	        	obstacles.add(floor);
	        }
	        for (Node wall : walls) {
	        	obstacles.add(wall);
	        }
	        for (Node door : doors) {
	        	obstacles.add(door);
	        }
	    }
	 
    /**
     * Method: Movement of Avatar along the X-Axis. 
     * Parameter: value (The higher this value, the faster the movement.)
     * Parameter: platforms (Detect the locations of the platforms.)
	 */
	 
	public void setAnimation(String name){
		
		getChildren().remove(this.imageView);
		this.imageView = new ImageView(new Image(name));
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, w, h));

		animation = new PlayerAnimation(imageView, Duration.millis(1000),count, colunms, offsetX, offsetY,w, h);
		animation.play();
        getChildren().addAll(imageView);
		
	}
	
    public void movePlayerX(int value) {
   

    	//Finds if movement is Left or Right
        boolean movingRight;
        if (value > 0) {
        	movingRight = true;
			
		}	
         else {
        	movingRight = false;
        }
        
        //For each step, will check if the front is clear; if so, move.
        for (int i = 0; i < Math.abs(value); i++) {
			
            for (Node obstacle : obstacles) {
                if (this.intersects(
                		this.sceneToLocal(obstacle.localToScene(
                			obstacle.getBoundsInLocal()))) &&
                				obstacle.isVisible() == true) {
                    if (movingRight) {
						
                        if (this.getTranslateX() + img.getWidth() == obstacle.getTranslateX()) {
                        	this.setTranslateX(this.getTranslateX() - 1);

                            return;
                        }
                    }
                    else {
                        if (this.getTranslateX() == obstacle.getTranslateX() + 32) {
                        	this.setTranslateX(this.getTranslateX() + 1);

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
	 */
    public void movePlayerY(int value) {
    	 boolean movingDown;
         if (value > 0) {
         	movingDown = true;
			
		
         } else {
         	movingDown = false;
			
         }
       //Checks if the avatar is on the floor.
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node obstacle : obstacles) {
                if (this.intersects(
                		this.sceneToLocal(obstacle.localToScene(
                			obstacle.getBoundsInLocal()))) &&
                				obstacle.isVisible() == true) {
                    if (movingDown) {
                        if (this.getTranslateY() + img.getHeight() == obstacle.getTranslateY()) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
							
                        }
                    }
                    else {
                        if (this.getTranslateY() == obstacle.getTranslateY() + 32) {
                        	this.setTranslateY(this.getTranslateY() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }
	
		
    }

	public boolean checkMovement(){
		if(movingRight == true)
			return true;
		else
			return false;	
	}

    //Jump Method
    public void jumpPlayer() {
    	if (canJump) {
    		jump.setOnEndOfMedia(new Runnable() {
    			public void run() {
    			jump = new MediaPlayer(jump_sound);	
     	        }
     	   });
     	   jump.play();
               velocity = velocity.add(0, -16.9);
               canJump = false;
           }
    }
	
    public int getDeathCount() {
    	return this.deathCount;
    }
    public void addDeathCount() {
    	this.deathCount+=1;
    }
    public void setDeathCount(int a) {
    	this.deathCount=a;
    }
}