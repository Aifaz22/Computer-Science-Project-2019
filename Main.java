import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.ImagePattern;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import java.io.*;

//Base Code: https://www.youtube.com/watch?v=lQEEby394qg

//Class Use: Running and Managing the Game
public class Main extends Application {
	
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    
    private ArrayList<Node> floors = new ArrayList<Node>();
    private ArrayList<Node> walls = new ArrayList<Node>();
    private ArrayList<Objects> buttons = new ArrayList<Objects>();
    private ArrayList<Node> spikes = new ArrayList<Node>();
    private ArrayList<Node> doors = new ArrayList<Node>();
    int deaths=0;
    private Pane appRoot = new Pane();
    private Date startTime=new Date();
    private Date currentTime=new Date();
    private Avatar player;
    private int levelWidth;
    private boolean running = true;
    private int levelNumber = 1;
    private HBox hbox1 = new HBox();
    private Rectangle bg = new Rectangle(672 * 2, 320*2);
    private Label deathCountMsg= new Label();
    private Label levelDetail= new Label();
    
    Image tile = new Image("Images/tile.png");
    Image buttonImage = new Image("Images/button.png");
    Image up_spike_image = new Image("Images/upspikes.png");
    Image down_spike_image = new Image("Images/downspikes.png");
    Image left_spike_image = new Image("Images/leftspikes.png");
    Image background_image = new Image("Images/background.png");
    Image door_image = new Image("Images/door.png");
    Image button_pushed = new Image("Images/button_pressed.png");
    Image right_wall_image = new Image("Images/rightwall.png");
    Image left_wall_image = new Image("Images/leftwall.png");
    Image block_image = new Image("Images/block.png");
    
    String bgm_name = ("music.wav");
    Media sound = new Media(new File(bgm_name).toURI().toString());
    MediaPlayer bgm = new MediaPlayer(sound);
    String click_name = ("Button_Push.wav");
    Media click_sound = new Media(new File(click_name).toURI().toString());
    MediaPlayer click;
    
    /**
     * Method: Create the Window and the Level according to LevelData.java
     * Currently, only LEVEL1 active.
     */
	
	//plays background music and sound effects 
	//refrence https://stackoverflow.com/questions/23498376/ahow-to-make-a-mp3-repeat-in-javafx
	//Background music from http://freemusicarchive.org/music/Kevin_MacLeod/Impact/Impact_Prelude_1765
	//sound effect from http://soundbible.com/1343-Jump.html
    
    private void initContent() {
    	
		//Create background and fill it with the image
	    bg.setFill(new ImagePattern(background_image));
	    appRoot.getChildren().addAll(bg);
        appRoot.getChildren().add(hbox1);
        
	    click = new MediaPlayer(click_sound);
	    
        levelWidth = LevelData.LEVEL1[0].length() * 32;
        
        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
        	String line;
        	 switch (levelNumber) {
        	 	case 0:
        	 		line = LevelData.Tunnel[i];
        	 		break;
        	 	case 1:	
        	 		line = LevelData.LEVEL1[i];
        	 		break;
        	 	default:
        	 		line = LevelData.LEVEL1[i];
        	 		break;
        	 }
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Objects floor = new Objects(j*32, i*32, 32, 32, tile);
                        floors.add(floor);
                        appRoot.getChildren().add(floor);
                        break;
                    case '2':
                        Objects left_wall = new Objects(j*32, i*32, 32, 32, left_wall_image);
                        walls.add(left_wall);
                        appRoot.getChildren().add(left_wall);
                        break;
                    case '3':
                        Objects right_wall = new Objects(j*32, i*32, 32, 32, right_wall_image);
                        walls.add(right_wall);
                        appRoot.getChildren().add(right_wall);
                        break;   
                    case '4':
                        Objects block = new Objects(j*32, i*32, 32, 32, block_image);
                        walls.add(block);
                        appRoot.getChildren().add(block);
                        break;      
                    case '5':
                        Objects button = new Objects(j*32, i*32+24, 32, 8, buttonImage);
                        buttons.add(button);
                        appRoot.getChildren().add(button);
                        break;
                    case '6':
                        Objects up_spike = new Objects(j*32, i*32+11, 32, 32, up_spike_image);
                        spikes.add(up_spike);
                        appRoot.getChildren().add(up_spike);
                        break;
                    case '7':
                        Objects down_spike = new Objects(j*32, i*32, 32, 26, down_spike_image);
                        spikes.add(down_spike);
                        appRoot.getChildren().add(down_spike);
                        break;
                    case '8':
                        Objects left_spike = new Objects(j*32+10, i*32, 32, 32, left_spike_image);
                        spikes.add(left_spike);
                        appRoot.getChildren().add(left_spike);
                        break;
                    case '9':
                        Objects door = new Objects(j*32+10, i*32, 32, 32, door_image);
                        doors.add(door);
                        appRoot.getChildren().add(door);
                        break;
                }
            }
        }
    
        //Create Avatar
        if (levelNumber == 0) {
        	player = new Avatar(0, 572, 20, 32, floors, walls, doors);
        } else {
            player = new Avatar(350, 1, 20, 32, floors, walls, doors);
        }
        appRoot.getChildren().add(player);
        deathCountMsg.setText("Death Count: "+player.getDeathCount());
        levelDetail.setText("Level "+this.levelNumber);
        player.setDeathCount(this.deaths);
        
        

        
    }
    
    /**
     * Method: update: Tracks keyboard press, as well as Button and Spikes.
     * Keyboard: Tracks Left/Right Movement, and Jump
     * Button: Once pressed, something happens.
     * Spikes: Once touched, re-spawn at start.
     */
   
  
    private void update() {
    	//checks for arrow key input
        if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
            player.jumpPlayer();
            
        }

        if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
            player.movePlayerX(-4);
        }

        if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 32 <= levelWidth - 5) {
            player.movePlayerX(4);
        }

        if (player.velocity.getY() < 6) {
            player.velocity = player.velocity.add(0, .5);
        }

        player.movePlayerY((int)player.velocity.getY());
        
        deathCountMsg.setText("Death Count: "+player.getDeathCount());
        
        this.currentTime=new Date();
        

        //Button       
        for (Objects button : buttons) {
            if (player.getBoundsInParent().intersects(button.getBoundsInParent())) {
            	click.play();
            	button.setFill(new ImagePattern(button_pushed));
            	for (Node door : doors) {
            		appRoot.getChildren().remove(door);
            		door.setVisible(false);
                }
              
            }
        }
        
        
        //Spikes
        for (Node spike : spikes) {
            if (player.getBoundsInParent().intersects(spike.getBoundsInParent())) {
                System.out.println("You died!");
                player.addDeathCount();
                System.out.println(player.getDeathCount());
                this.deaths=player.getDeathCount();
                appRoot.getChildren().remove(player);
                player = new Avatar(0, 572, 20, 32, floors, walls, doors);
                appRoot.getChildren().add(player);
                player.setDeathCount(this.deaths);
            }
        }
      
        //Loop Music
        bgm.setOnEndOfMedia(new Runnable() {
            public void run() {
              bgm.seek(Duration.ZERO);
            }
        });
        
      //Check if the player has reached the end of the level.
        if (player.getTranslateX() > 1306  && player.getTranslateY() > 520 && levelNumber != 0) {
        	appRoot.getChildren().clear();
        	floors.clear();
        	walls.clear();
        	doors.clear();
        	buttons.clear();
        	spikes.clear();
        	levelNumber = 0;
        	initContent();
        	
        	
        	
        	}
        else if (player.getTranslateY() > 600 && levelNumber == 0) {
        	appRoot.getChildren().clear();
        	floors.clear();
        	walls.clear();
        	doors.clear();
        	buttons.clear();
        	spikes.clear();
        	levelNumber = 1;
        	initContent();
        	
        	
        }
    
        	
        
       //Check if it's a wrapping point.
        if (player.getTranslateX() > 1306  && player.getTranslateY() < 100) {
        	//player.setTranslateY(player.getTranslateY());
        	player.setTranslateX(5);	
        	
        }
        if (player.getTranslateX() < 4  && player.getTranslateY() < 100) {
        	//player.setTranslateY(player.getTranslateY());
        	player.setTranslateX(1305);	
        }
        
        
        
    }


    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();

        
        Scene scene = new Scene(appRoot,LevelData.LEVEL1[0].length() * 32 - 15, 685);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        
        hbox1.relocate(20,660);
        hbox1.setSpacing(((LevelData.LEVEL1[0].length() * 32 - 15)/2)-130);
        hbox1.getChildren().add(deathCountMsg);
        hbox1.getChildren().add(levelDetail);

        
        primaryStage.setTitle("Test Game Demo 2");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
       
        bgm.play();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                }
				

            
            }
        };
        timer.start();
    }
    public int getSec() {
    	return (int)((this.currentTime.getTime()-this.startTime.getTime())/1000)
    }

    public static void main(String[] args) {
    	System.out.println("'up arrow' - jump");
    	System.out.println("'right arrow' - move right");
    	System.out.println("'left arrow' - move left");
		System.out.println("Avoid touching the spikes");
        launch(args);
    }
}
