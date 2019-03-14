import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Class: Main - Child of Class: Application
 * Class Use: Run and Manage Game
 * Methods:
 * 		initContent
 * 		update
 * 		isPressed
 * 		start
 * 		main
 *
 * Base Code: https://www.youtube.com/watch?v=lQEEby394qg
 */

public class Main extends Application {
	//Instance Variables
	
	//Menu
	private Pane appRoot = new Pane();
	private HBox hbox1 = new HBox();
	private Label deathCountMsg = new Label();
	private Label levelDetail = new Label();
	private Timer stopwatch = new Timer();
	private Label gameTimer = new Label();
	int deaths = 0;
	private int levelNumber = 1;
	
	//Sound & Music
	private SoundEffect bgm_name = new SoundEffect("music.wav");
	private MediaPlayer bgm = new MediaPlayer(bgm_name.playSound());
	private SoundEffect button = new SoundEffect("Button_Push.wav");
	private MediaPlayer click;
	
	//Graphics
	private Image image = new Image("Images/player.png");
	private Image[] blocks = {
		new Image("Images/tile.png"),
		new Image("Images/leftwall.png"),
		new Image("Images/rightwall.png"),
		new Image("Images/block.png"),
		new Image("Images/button.png"),
		new Image("Images/upspikes.png"),
		new Image("Images/downspikes.png"),
		new Image("Images/leftspikes.png"),
		new Image("Images/door.png"),
		new Image("Images/button_pressed.png"),
		new Image("Images/background.png"),
	};
	private ImageView imageView = new ImageView(image);
	
	//Game
	private boolean running = true;
	private int levelWidth;
	private Rectangle bg = new Rectangle(672 * 2, 320*2);
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Objects> floors = new ArrayList<Objects>();
	private ArrayList<Objects> walls = new ArrayList<Objects>();
	private ArrayList<Objects> buttons = new ArrayList<Objects>();
	private ArrayList<Objects> spikes = new ArrayList<Objects>();
	private ArrayList<Objects> doors = new ArrayList<Objects>();
	private Avatar player;
	
	/**
	 * Method: Initialize all Content - Avatar, LevelData, Objects, etc.
	 * 
	 */
	private void initContent() {
		//Creates Background
		bg.setFill(new ImagePattern(blocks[10]));
		appRoot.getChildren().addAll(bg);
		appRoot.getChildren().add(hbox1);
		
		//Reads Level and Data
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
						Objects left_wall = new Objects(j*32, i*32, 32, 32, blocks[1]);
						walls.add(left_wall);
						appRoot.getChildren().add(left_wall);
						break;
					case '3':
						Objects right_wall = new Objects(j*32, i*32, 32, 32, blocks[2]);
						walls.add(right_wall);
						appRoot.getChildren().add(right_wall);
						break;
					case '4':
						Objects block = new Objects(j*32, i*32, 32, 32, blocks[3]);
						walls.add(block);
						appRoot.getChildren().add(block);
						break;
					case '5':
						Objects button = new Objects(j*32, i*32+24, 32, 8, blocks[4]);
						buttons.add(button);
						appRoot.getChildren().add(button);
						break;
					case '6':
						Objects up_spike = new Objects(j*32, i*32+11, 32, 32, blocks[5]);
						spikes.add(up_spike);
						appRoot.getChildren().add(up_spike);
						break;
					case '7':
						Objects down_spike = new Objects(j*32, i*32, 32, 26, blocks[6]);
						spikes.add(down_spike);
						appRoot.getChildren().add(down_spike);
						break;
					case '8':
						Objects left_spike = new Objects(j*32+10, i*32, 32, 32, blocks[7]);
						spikes.add(left_spike);
						appRoot.getChildren().add(left_spike);
						break;
					case '9':
						Objects door = new Objects(j*32+10, i*32, 32, 32, blocks[8]);
						doors.add(door);
						appRoot.getChildren().add(door);
						break;
					}
				}
			}
		
		//Create Avatar
		player = new Avatar(0, 572, 20, 32, floors, walls, doors, imageView);
		appRoot.getChildren().add(player);
		
		//Create GUI
		deathCountMsg.setText("Death Count: "+player.getDeathCount());
		deathCountMsg.setFont(Font.font ("Old English Text MT", 20));
		deathCountMsg.setTextFill(Color.RED);
		levelDetail.setTextFill(Color.RED);
		levelDetail.setFont(Font.font ("Old English Text MT", 20));
		levelDetail.setWrapText(true);
		if (this.levelNumber==0) {
			levelDetail.setText("Tunnel");
			hbox1.setSpacing(267);
		} else {
			levelDetail.setText("Level "+this.levelNumber);
			hbox1.setSpacing(300);
		}
		gameTimer.setTextFill(Color.RED);
		gameTimer.setText(this.stopwatch.getStringHour()+":"+this.stopwatch.getStringMin()+":"+this.stopwatch.getStringSec());
		gameTimer.setFont(Font.font ("Old English Text MT", 20));
		player.setDeathCount(this.deaths);
		click = new MediaPlayer(button.playSound());
	}
	
	/**
	 * Method: update: Checks for anything in the game (Key Presses, Timer/Counter, Avatar Position, Spike/Button Collision, Music)
	 * Keyboard: Tracks Left/Right Movement, and Jump
	 * Timer/Counter: Updates GUI
	 * Button: Once pressed, something happens.
	 * Spikes: Once touched, re-spawn at start.
	 * Avatar Position: Warping, and Level End
	 * Music: Once it ends, it will loop.
	 * 		Reference: https://stackoverflow.com/questions/23498376/ahow-to-make-a-mp3-repeat-in-javafx
	 * 		Music Source: http://freemusicarchive.org/music/Kevin_MacLeod/Impact/Impact_Prelude_1765
	 * Sound Source: http://soundbible.com/1343-Jump.html  
	 * 
	 */
	private void update() {
		//Keyboard
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
		if (player.getVelocity().getY() < 6) {
			player.addVelocity(0, .5);
		}
		player.movePlayerY((int)player.getVelocity().getY());
		
		//Timer/Counter
		this.stopwatch.setCurrentTime();
		gameTimer.setText(this.stopwatch.getStringHour()+":"+this.stopwatch.getStringMin()+":"+this.stopwatch.getStringSec());
		deathCountMsg.setText("Death Count: "+player.getDeathCount());       
		
		//Button
		for (Objects button : buttons) {
			if (player.getBoundsInParent().intersects(button.getBoundsInParent())) {
				click.play();
				button.setFill(new ImagePattern(blocks[9]));
				for (Objects door : doors) {
					appRoot.getChildren().remove(door);
					door.setVisibility(false);
				}
				player.updateObstacleState(floors, walls, doors);
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
		
		//Avatar Position - End Level
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
		
		//Avatar Position - Warping
		if (player.getTranslateX() > 1306  && player.getTranslateY() < 100) {
			player.setTranslateX(5);
		}
		if (player.getTranslateX() < 4  && player.getTranslateY() < 100) {
			player.setTranslateX(1305);
		}
		if (player.getTranslateY() > 600) {
			player.setTranslateX(player.getTranslateX());
			player.setTranslateY(2);
		}
		if (player.getTranslateY() < 1) {
			player.setTranslateY(580);
			player.setTranslateX(player.getTranslateX());
			player.jumpPlayer();
		}
		
		//Loop Music
		bgm.setOnEndOfMedia(new Runnable() {
			public void run() {
				bgm.seek(Duration.ZERO);
			}
		});
	}
	
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initContent();
		Scene scene = new Scene(appRoot,LevelData.LEVEL1[0].length() * 32 - 15, 690);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		
		hbox1.relocate(20,650);
		hbox1.setSpacing(300);
		hbox1.getChildren().add(deathCountMsg);
		hbox1.getChildren().add(levelDetail);
		hbox1.getChildren().add(gameTimer);
		
		primaryStage.setTitle("Test Game Demo 3");
		
		//Creating the Main Menu
		VBox menuroot = new VBox();
		Button btnstart;
		Button btnexit;
	
		//Main Menu Buttons
		btnstart = new Button("Start Game");
		btnexit = new Button("EXIT");
		btnstart.setTranslateY(250);
		btnstart.setTranslateX(622);
		btnexit.setTranslateY(350);
		btnexit.setTranslateX(640);
		
		//To Main Menu
		Button btnmenu= new Button("Main Menu");
		hbox1.setSpacing(300);
		hbox1.getChildren().add(btnmenu);
		
		//Start Game
		btnstart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setResizable(false);
			}
		});
		
		//Exit
		btnexit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
		
		btnmenu.setFont(Font.font ("Lucida Calligraphy", 15));
		btnstart.setFont(Font.font ("Lucida Calligraphy", 20));
		btnexit.setFont(Font.font ("Lucida Calligraphy", 20));
		btnmenu.setTextFill(Color.RED);
		btnstart.setTextFill(Color.RED);
		btnexit.setTextFill(Color.RED);
		
		//Button Size Increase if Hovered Over
		btnmenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.BLUE);
				btnmenu.setScaleX(1.5);
				btnmenu.setScaleY(1.5);
				}
		});
		
		//Button Size Return
		btnmenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.RED);
				btnmenu.setScaleX(1);
				btnmenu.setScaleY(1);
			}
		});
		
		//Button Size Increase if Hovered Over
		btnmenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.BLUE);
				btnmenu.setScaleX(1.5);
				btnmenu.setScaleY(1.5);
				}
		});
		
		//Button Size Return
		btnmenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.RED);
				btnmenu.setScaleX(1);
				btnmenu.setScaleY(1);
			}
		});
			
		//Button Size Increase if Hovered Over
		btnmenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.BLUE);
				btnmenu.setScaleX(1.5);
				btnmenu.setScaleY(1.5);
				}
		});
		
		//Button Size Return
		btnmenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnmenu.setTextFill(Color.RED);
				btnmenu.setScaleX(1);
				btnmenu.setScaleY(1);
			}
		});
		menuroot.getChildren().add(btnstart);
		menuroot.getChildren().add(btnexit);
		
		//Running Game
		Scene gamemenu = new Scene(menuroot, LevelData.LEVEL1[0].length() * 32 - 15, 685);
		btnmenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(gamemenu);
				primaryStage.show();
				primaryStage.setResizable(false);
			}
		});	
		
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
		System.out.println("'Arrow Keys' - Movement");
		launch(args);
	}
}
