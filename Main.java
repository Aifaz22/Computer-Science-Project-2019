import java.util.ArrayList;
import java.util.HashMap;
import sun.audio.*;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;

//we are going to use this code: https://www.youtube.com/watch?v=lQEEby394qg as our base project.

public class Main extends Application {

	
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> buttons = new ArrayList<Node>();
    private ArrayList<Node> spikes = new ArrayList<Node>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private Avatar player;
    private int levelWidth;
    private boolean running = true;
    
    //image files
    Image tile = new Image("tile.png");
    Image buttonImage = new Image("button.png");
    Image up_spike_image = new Image("upspikes.png");
    Image down_spike_image = new Image("downspikes.png");
    Image left_spike_image = new Image("leftspikes.png");



	//plays background music and sound effects 
	//refrence https://www.youtube.com/watch?v=3q4f6I5zi2w 
	//Background music from http://freemusicarchive.org/music/Kevin_MacLeod/Impact/Impact_Prelude_1765
	//sound effect from http://soundbible.com/1343-Jump.html
	public static void music(String name){
		InputStream playMusic;
		try{
		playMusic = new FileInputStream(new File(name));
		AudioStream audio = new AudioStream(playMusic);
		AudioPlayer.player.start(audio);
		
		}
		catch(Exception e){
			System.out.println("error");
			}
		}
	
    private void initContent() {
        Rectangle bg = new Rectangle(1245, 588);

        levelWidth = LevelData.LEVEL1[0].length() * 30;
        
        //Draw level
        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Objects platform = new Objects(j*30, i*30, 30, 30, tile);
                        platforms.add(platform);
                        gameRoot.getChildren().add(platform);
                        break;
                    case '2':
                        Objects button = new Objects(j*30, i*30, 30, 30, buttonImage);
                        buttons.add(button);
                        gameRoot.getChildren().add(button);
                        break;
                    case '3':
                        Objects up_spike = new Objects(j*30, i*30+10, 20, 20, up_spike_image);
                        spikes.add(up_spike);
                        gameRoot.getChildren().add(up_spike);
                        break;
                    case '4':
                        Objects down_spike = new Objects(j*30, i*30, 20, 20, down_spike_image);
                        spikes.add(down_spike);
                        gameRoot.getChildren().add(down_spike);
                        break;
                    case '5':
                        Objects left_spike = new Objects(j*30 +10, i*30+5, 20, 20, left_spike_image);
                        spikes.add(left_spike);
                        gameRoot.getChildren().add(left_spike);
                        break;
                }
            }
        }
        
        //create player
        player = new Avatar(0, 520, 30, 30);
        gameRoot.getChildren().add(player);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
               gameRoot.setLayoutX(-(offset - 640));
            }
        });

        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update() {
    	//checks for arrow key input
        if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
			music("jump.wav");
            player.jumpPlayer();
			//music("jump.wav");
        }

        if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
            player.movePlayerX(-4,platforms);
        }

        if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 30 <= levelWidth - 5) {
            player.movePlayerX(4,platforms);
        }

        if (player.velocity.getY() < 10) {
            player.velocity = player.velocity.add(0, 1);
        }

        player.movePlayerY((int)player.velocity.getY(),platforms);

        //Button*****************        
        for (Node button : buttons) {
            if (player.getBoundsInParent().intersects(button.getBoundsInParent())) {
                button.getProperties().put("alive", false);
              
            }
        }
        
        for (Iterator<Node> it = buttons.iterator(); it.hasNext(); ) {
            Node button = it.next();
            if (!(Boolean)button.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(button);
                System.exit(0);
            }
        }
        //Spikes*****************
        for (Node spike : spikes) {
            if (player.getBoundsInParent().intersects(spike.getBoundsInParent())) {
                spike.getProperties().put("alive", false);
              
            }
        }
        for (Iterator<Node> it = spikes.iterator(); it.hasNext(); ) {
            Node spike = it.next();
            if (!(Boolean)spike.getProperties().get("alive")) {
                System.out.println("you died!");
                gameRoot.getChildren().remove(player);
                player = new Avatar(0, 520, 30, 30);
                gameRoot.getChildren().add(player);
				spike.getProperties().put("alive", true);
				music("music.wav");

            }
        }
    }


    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();

        
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("My Nightmare");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

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
		music("music.wav");
        launch(args);
    }
}
