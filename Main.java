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

import java.io.*;

//Base Code: https://www.youtube.com/watch?v=lQEEby394qg

//Class Use: Running and Managing the Game
public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Objects> buttons = new ArrayList<Objects>();
    private ArrayList<Node> spikes = new ArrayList<Node>();
    private ArrayList<Node> doors = new ArrayList<Node>();

    private Pane appRoot = new Pane();

    private Avatar player;
    private int levelWidth;
    private boolean running = true;
    private int levelNumber = 0;
    
    private Rectangle bg = new Rectangle(LevelData.LEVEL1[0].length() * 32, 624);
    
    Image tile = new Image("tile.png");
    Image buttonImage = new Image("button.png");
    Image up_spike_image = new Image("upspikes.png");
    Image down_spike_image = new Image("downspikes.png");
    Image left_spike_image = new Image("leftspikes.png");
    Image background_image = new Image("background.png");
    Image door_image = new Image("door.png");
    Image button_pushed = new Image("button_pressed.png");
    
    String bgm_name = ("music.wav");
    Media sound = new Media(new File(bgm_name).toURI().toString());
    MediaPlayer bgm = new MediaPlayer(sound);
    String jump_name = ("jump.wav");
    Media jump_sound = new Media(new File(jump_name).toURI().toString());
    MediaPlayer jump = new MediaPlayer(jump_sound);
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
        
	    click = new MediaPlayer(click_sound);
	    
        levelWidth = LevelData.LEVEL1[0].length() * 32;
        
        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
        	String line;
        	 switch (levelNumber) {
        	 	case 0:
        	 		line = LevelData.LEVEL1[i];
        	 		break;
        	 	case 1:	
        	 		line = LevelData.Tunnel[i];
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
                        Objects platform = new Objects(j*32, i*32, 32, 32, tile);
                        platforms.add(platform);
                        appRoot.getChildren().add(platform);
                        break;
                    case '2':
                        Objects button = new Objects(j*32, i*32+24, 32, 8, buttonImage);
                        buttons.add(button);
                        appRoot.getChildren().add(button);
                        break;
                    case '3':
                        Objects up_spike = new Objects(j*32, i*32+11, 32, 32, up_spike_image);
                        spikes.add(up_spike);
                        appRoot.getChildren().add(up_spike);
                        break;
                    case '4':
                        Objects down_spike = new Objects(j*32, i*32, 32, 26, down_spike_image);
                        spikes.add(down_spike);
                        appRoot.getChildren().add(down_spike);
                        break;
                    case '5':
                        Objects left_spike = new Objects(j*32+10, i*32, 32, 32, left_spike_image);
                        spikes.add(left_spike);
                        appRoot.getChildren().add(left_spike);
                        break;
                    case '6':
                        Objects door = new Objects(j*32+10, i*32, 32, 32, door_image);
                        doors.add(door);
                        appRoot.getChildren().add(door);
                        break;
                }
            }
        }
    
        //Create Avatar
        player = new Avatar(0, 572, 32, 32, platforms, doors);
        appRoot.getChildren().add(player);
        
        

        
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
        	 jump.setOnEndOfMedia(new Runnable() {
        	        public void run() {
        	        	jump = new MediaPlayer(jump_sound);
        	        	
        	        }
        	    });
        	    jump.play();
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
                appRoot.getChildren().remove(player);
                player = new Avatar(0, 572, 32, 32, platforms, doors);
                appRoot.getChildren().add(player);
            }
        }
      
        //loop background music
        bgm.setOnEndOfMedia(new Runnable() {
            public void run() {
              bgm.seek(Duration.ZERO);
            }
        });
        
      //check if the player has reached the end of the level
        if (player.getTranslateX() > 1306  && player.getTranslateY() > 520) {
        	appRoot.getChildren().clear();
        	platforms.clear();
        	doors.clear();
        	buttons.clear();
        	spikes.clear();
        	switch(levelNumber) {
        	case 0:
        		levelNumber = 1;
        		initContent();
        		break;
        	case 1:
        		levelNumber = 0;
        		initContent();
        		break;
        	}
        	
        	
        }
        
        
    }


    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();

        
        Scene scene = new Scene(appRoot,LevelData.LEVEL1[0].length() * 32 - 15, 624);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
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

    public static void main(String[] args) {
    	System.out.println("'up arrow' - jump");
    	System.out.println("'right arrow' - move right");
    	System.out.println("'left arrow' - move left");
		System.out.println("Avoid touching the spikes");
        launch(args);
    }
}
