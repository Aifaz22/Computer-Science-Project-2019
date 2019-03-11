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
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;

import java.io.*;

//Base Code: https://www.youtube.com/watch?v=lQEEby394qg

//Class Use: Running and Managing the Game
public class Main extends Application {
	
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private Timer stopwatch=new Timer();
    private ArrayList<Node> floors = new ArrayList<Node>();
    private ArrayList<Node> walls = new ArrayList<Node>();
    private ArrayList<Objects> buttons = new ArrayList<Objects>();
    private ArrayList<Node> spikes = new ArrayList<Node>();
    private ArrayList<Node> doors = new ArrayList<Node>();
    int deaths=0;
    private Pane appRoot = new Pane();
  ;
    private int levelWidth;
    private boolean running = true;
    private int levelNumber = 1;
    private HBox hbox1 = new HBox();
    private HBox hbox2= new HBox();
    private Rectangle bg = new Rectangle(672 * 2, 320*2);
    private Label deathCountMsg= new Label();
    private Label levelDetail= new Label();
    private Label gameTimer= new Label();
    
    private Image image = new Image("Images/player.png");
	private ImageView imageView = new ImageView(image);
	private Avatar player;
	
   	Image[] blocks = {new Image("Images/tile.png"), new Image("Images/button.png"), new Image("Images/upspikes.png"), new Image("Images/downspikes.png"),
	new Image("Images/leftspikes.png"), new Image("Images/background.png"), new Image("Images/door.png"), new Image("Images/button_pressed.png"), 
	new Image("Images/rightwall.png"), new Image("Images/leftwall.png"), new Image("Images/block.png")};
	
    
    SoundEffect bgm_name = new SoundEffect("music.wav");
	MediaPlayer bgm = new MediaPlayer(bgm_name.playSound());
	
	SoundEffect button = new SoundEffect("Button_Push.wav");
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
	    bg.setFill(new ImagePattern(blocks[5]));
	    appRoot.getChildren().addAll(bg);
        appRoot.getChildren().add(hbox1);
	    click = new MediaPlayer(button.playSound());
	    
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
                        Objects floor = new Objects(j*32, i*32, 32, 32, blocks[0]);
                        floors.add(floor);
                        appRoot.getChildren().add(floor);
                        break;
                    case '2':
                        Objects left_wall = new Objects(j*32, i*32, 32, 32, blocks[9]);
                        walls.add(left_wall);
                        appRoot.getChildren().add(left_wall);
                        break;
                    case '3':
                        Objects right_wall = new Objects(j*32, i*32, 32, 32, blocks[8]);
                        walls.add(right_wall);
                        appRoot.getChildren().add(right_wall);
                        break;   
                    case '4':
                        Objects block = new Objects(j*32, i*32, 32, 32, blocks[10]);
                        walls.add(block);
                        appRoot.getChildren().add(block);
                        break;      
                    case '5':
                        Objects button = new Objects(j*32, i*32+24, 32, 8, blocks[1]);
                        buttons.add(button);
                        appRoot.getChildren().add(button);
                        break;
                    case '6':
                        Objects up_spike = new Objects(j*32, i*32+11, 32, 32, blocks[2]);
                        spikes.add(up_spike);
                        appRoot.getChildren().add(up_spike);
                        break;
                    case '7':
                        Objects down_spike = new Objects(j*32, i*32, 32, 26, blocks[3]);
                        spikes.add(down_spike);
                        appRoot.getChildren().add(down_spike);
                        break;
                    case '8':
                        Objects left_spike = new Objects(j*32+10, i*32, 32, 32, blocks[4]);
                        spikes.add(left_spike);
                        appRoot.getChildren().add(left_spike);
                        break;
                    case '9':
                        Objects door = new Objects(j*32+10, i*32, 32, 32, blocks[6]);
                        doors.add(door);
                        appRoot.getChildren().add(door);
                        break;
                }
            }
        }
    
    
        //Create Avatar
        player = new Avatar(0, 572, 20, 32, floors, walls, doors, imageView);

        appRoot.getChildren().add(player);
        deathCountMsg.setText("Death Count: "+player.getDeathCount());
        levelDetail.setText("Level "+this.levelNumber);
        gameTimer.setText(this.stopwatch.getStringHour()+":"+this.stopwatch.getStringMin()+":"+this.stopwatch.getStringSec());
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
			player.setAnimation("Images/walk1.png");
            player.movePlayerX(4);
        }

        if (player.velocity.getY() < 6) {
            player.velocity = player.velocity.add(0, .5);
        }

        player.movePlayerY((int)player.velocity.getY());
        
        deathCountMsg.setText("Death Count: "+player.getDeathCount());
        
        this.stopwatch.setCurrentTime();
        gameTimer.setText(this.stopwatch.getStringHour()+":"+this.stopwatch.getStringMin()+":"+this.stopwatch.getStringSec());
        

        //Button       
        for (Objects button : buttons) {
            if (player.getBoundsInParent().intersects(button.getBoundsInParent())) {
            	click.play();
            	button.setFill(new ImagePattern(blocks[7]));
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
                player = new Avatar(0, 572, 20, 32, floors, walls, doors, imageView);
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
        if (player.getTranslateX() > 1306  && player.getTranslateY() > 520) {
        	appRoot.getChildren().clear();
        	floors.clear();
        	walls.clear();
        	doors.clear();
        	buttons.clear();
        	spikes.clear();
        	switch (levelNumber) {
    	 	case 0:
    	 		levelNumber = 1;
    	 		break;
    	 	case 1:	
    	 		levelNumber = 0;
    	 		break;
        	}
        	initContent();
        	
        	
        	
        	}
        
        
       //Check if it's a wrapping point.
        if (player.getTranslateX() > 1306  && player.getTranslateY() < 100) {
        	player.setTranslateX(5);	
        	
        }
        if (player.getTranslateX() < 4  && player.getTranslateY() < 100) {
        	player.setTranslateX(1305);	
        }
        if (player.getTranslateY() > 600) {
        	player.setTranslateX(player.getTranslateX() - 64);
        	player.setTranslateY(2);
        }
        if (player.getTranslateY() < 1) {
        	player.setTranslateY(580);
        	player.setTranslateX(player.getTranslateX() + 64);
        	player.jumpPlayer();
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
        
        
        hbox1.relocate(20,650);
        hbox1.setSpacing(350);
        hbox1.getChildren().add(deathCountMsg);
        hbox1.getChildren().add(levelDetail);
        hbox1.getChildren().add(gameTimer);

        
        primaryStage.setTitle("Test Game Demo 2");
		
	//Creating the Mainmenu	
	VBox menuroot = new VBox();
		Button btnstart;
		Button btnexit;
	
	//Buttons
	btnstart = new Button("Start Game");
	btnexit = new Button("EXIT");
	btnstart.setTranslateY(250);
	btnstart.setTranslateX(622);
	btnexit.setTranslateY(350);
	btnexit.setTranslateX(640);
	
	//in the game
	Button btnmenu= new Button("Main Menu");
	hbox1.setSpacing(300);   
	hbox1.getChildren().add(btnmenu);
	
	//Enter the game if click Start Game
	btnstart.setOnAction(new EventHandler<ActionEvent>()
       {
	@Override
	public void handle(ActionEvent event)
	{
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    primaryStage.setResizable(false);
	}
       }
      );	
	
	//Close the game if click Exit
	btnexit.setOnAction(new EventHandler<ActionEvent>()
       {
        @Override
	public void handle(ActionEvent event)
	{
	    System.exit(0);
	}
       }
      );		
			
	
	
	menuroot.getChildren().add(btnstart);
	menuroot.getChildren().add(btnexit);

	Scene gamemenu = new Scene(menuroot, LevelData.LEVEL1[0].length() * 32 - 15, 685);
       
	btnmenu.setOnAction(new EventHandler<ActionEvent>()
    {
	@Override
	public void handle(ActionEvent event)
	{
	    primaryStage.setScene(gamemenu);
	    primaryStage.show();
	    primaryStage.setResizable(false);
	}
    }
   );	
	
	primaryStage.setScene(gamemenu);
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
