import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.media.MediaPlayer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;

import javafx.scene.layout.Pane;


/**
 * Class: Avatar - Child of Class: Pane
 * Class Use: Create and Move Avatar Character
 * Methods:
 * 		Constructor
 * 		getVelocity
 * 		getDeathCount
 * 		checkMovement
 * 		setDeathCount
 * 		setAnimation
 * 		addVelocity
 * 		addDeathCount
 * 		updateObstacleState
 * 		movePlayerX
 * 		movePlayerY
 * 		jumpPlayer
 * 		
 */
public class Avatar extends Pane{
	
	//Instance Variables
	
	//General
	private int deathCount = 0;
	private boolean canJump = true;
	private ArrayList<Objects> obstacles = new ArrayList<Objects>();
	private Point2D velocity = new Point2D(0, 0); 
	public boolean movingRight;
	
	
	//Image and Animation
	private Image img = new Image("Images/player.png");
	private ImageView imageView;
	int count = 9;
	int columns = 10;
	int offsetX=1;
	int offsetY=1;
	PlayerAnimation animation;
	
	//Sound
    private SoundEffect jump_name = new SoundEffect("jump.wav");
    private MediaPlayer jump = new MediaPlayer(jump_name.playSound());
	
    
    
    //Constructor: (Location X, Location Y, Width, Height, Floors, Walls, Doors, ImageView)
    public Avatar(int x, int y, int w, int h, ArrayList<Objects> floors, ArrayList<Objects> walls, ArrayList<Objects> doors, ImageView imageView) {
		 
		this.imageView = imageView;
		this.getChildren().add(imageView);
		this.setTranslateX(x);
		this.setTranslateY(y);
		for (Objects floor : floors) {
			obstacles.add(new Objects(floor));
		}
		for (Objects wall : walls) {
			obstacles.add(new Objects(wall));
		}
		for (Objects door : doors) {
			obstacles.add(new Objects(door));
		}
	}
    
    //Getter Methods
    public Point2D getVelocity() {
    	return new Point2D(velocity.getX(),velocity.getY());
    }
    
    public int getDeathCount() {
    	return this.deathCount;
    }
    
    public boolean checkMovement(){
		if(movingRight == true)
			return true;
		else {
			return false;	
		}
	}
	//Setter Methods
	public void setDeathCount(int a) {
		this.deathCount=a;
	}
	public void setAnimation(String name) {		
		getChildren().remove(this.imageView);
		this.imageView = new ImageView(new Image(name));
		imageView.setViewport(new Rectangle2D(offsetX, offsetY, img.getWidth(), img.getHeight()));
		animation = new PlayerAnimation(imageView, Duration.millis(1000),count, columns, offsetX, offsetY, (int)img.getWidth(), (int)img.getHeight());
		animation.play();
		getChildren().addAll(imageView);
	}
	
	//Basic Mutators
	
	//Adds velocity based on X and Y positions
	public void addVelocity(double x, double y) {
		this.velocity = this.velocity.add(x,y);
	}
	
	public void addDeathCount() {
		this.deathCount+=1;
	}
	
	//Updates Where the Obstacles Are
	public void updateObstacleState(ArrayList<Objects> floors, ArrayList<Objects> walls, ArrayList<Objects> doors) {
		obstacles.clear();
		for (Objects floor : floors) {
			obstacles.add(new Objects(floor));
		}
		for (Objects wall : walls) {
			obstacles.add(new Objects(wall));
		}
		for (Objects door : doors) {
			obstacles.add(new Objects(door));
		}
	}
	/**
	* Method: Movement of Avatar along the X-Axis. 
	* Parameter: value (The higher this value, the faster the movement.)
	*/
	public void movePlayerX(int value) {
		
		//Finds if movement is Left or Right
		boolean movingRight;
			if (value > 0) {
				movingRight = true;
			} else {
				movingRight = false;
			}
			
		//For each step, will check if the front is clear; if so, move.
		for (int i = 0; i < Math.abs(value); i++) {
			for (Objects obstacle : obstacles) {
				if (this.intersects(
					this.sceneToLocal(obstacle.localToScene(
					obstacle.getBoundsInLocal()))) &&
					obstacle.getVisibility() == true) {
					if (movingRight) {
						if (this.getTranslateX() + img.getWidth() == obstacle.getTranslateX()) {
							this.setTranslateX(this.getTranslateX() - 1);
							return;
							}
					} else {
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
		
		//Checks if the Avatar is on the floor.
		for (int i = 0; i < Math.abs(value); i++) {
			for (Objects obstacle : obstacles) {
				if (this.intersects(
					this.sceneToLocal(obstacle.localToScene(
					obstacle.getBoundsInLocal()))) &&
					obstacle.getVisibility() == true) {
					if (movingDown) {
						if (this.getTranslateY() + img.getHeight() == obstacle.getTranslateY()) {
							this.setTranslateY(this.getTranslateY() - 1);
							canJump = true;
							return;
						}
					} else {
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

	//Jump Method
	public void jumpPlayer() {
		if (canJump) {
			jump.setOnEndOfMedia(new Runnable() {
				public void run() {
					jump  = new MediaPlayer(jump_name.playSound()); ;
				}
			});
		jump.play();
		velocity = velocity.add(0, -16.9);
		canJump = false;
		}
	}
}
