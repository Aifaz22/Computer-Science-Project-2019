import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Class: Main - Child of Class: Application
 * Class Use: Run and Manage Game
 * Methods:
 * 		btnResize
 * 		initContent
 * 			initBackground
 * 			initLevelData
 * 			initAvatar
 * 			initGUI
 * 				initGUIDeath
 * 				initGUILevel
 * 				initGUITimer
 * 			initEnd
 * 				initEndMsg
 * 				initEndDeath
 * 				initEndTimer
 * 		update
 * 			updateKey
 * 				moveRight
 * 				moveUp
 * 				moveLeft
 * 			updateGravity
 * 			updateAnimation
 * 				animate
 * 			updateGUI
 * 			updateDoor
 * 				openDoor
 * 			updateSpike
 * 			updateGem
 * 			updateAvatar
 * 			updateLoop
 * 		restart
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
	private TimerText stopwatch = new TimerText();
	private Label gameTimer = new Label();
	private int levelNumber = 1;
	
	//Sound & Music
	private SoundEffect bgm_name = new SoundEffect("Sound/music.wav");
	private MediaPlayer bgm = new MediaPlayer(bgm_name.playSound());
	private SoundEffect click_name = new SoundEffect("Sound/Button_Push.wav");
    private MediaPlayer click = new MediaPlayer(click_name.playSound());
	private SoundEffect death_name = new SoundEffect("Sound/death.wav");
    private MediaPlayer death = new MediaPlayer(death_name.playSound());
	
	//Graphics
	private Image gem = new Image("Images/gem.png");
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
		new Image("Images/button2.png"),
		new Image("Images/button_pressed2.png"),
		new Image("Images/roof.png"),
		new Image("Images/black.png"),
	};
	private Image[] bgs = {
		new Image("Images/background.png"),
		new Image("Images/menubackground.png"),
		new Image("Images/background_inverted.png"),
		new Image("Images/credits.png")
	};
	
	//Game
	private int levelWidth;
	private Button btnmenu= new Button("Main Menu");
	private Button restart = new Button("RESTART");
	private Button exit = new Button("EXIT");
	private Rectangle bg = new Rectangle(672 * 2, 354 * 2);
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Objects> floors = new ArrayList<Objects>();
	private ArrayList<Objects> walls = new ArrayList<Objects>();
	private ArrayList<Objects> buttons = new ArrayList<Objects>();
	private ArrayList<Objects> spikes = new ArrayList<Objects>();
	private ArrayList<Objects> doors = new ArrayList<Objects>();
	private ArrayList<Objects> gemlist = new ArrayList<Objects>();
	private Avatar player;
	private ArrayList<Boolean> temp = new ArrayList<Boolean>();
	private Rectangle menubg = new Rectangle(672 * 2, 354*2);
	private Rectangle crbg = new Rectangle(672*2, 354*2);
	private LevelData levels = new LevelData();
	private int tempLevel = 0;
	private boolean stop = false;
	
	
	//player control 
	private boolean up = false;
	private boolean right = false;
	private boolean left = false;
	private boolean turnLeft = false;
	private int count = 0;
	
	
	
	private void btnResize(Button button) {
		button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				button.setTextFill(Color.LIGHTCYAN);
				button.setScaleX(1.5);
				button.setScaleY(1.5);
				}
		});
		
		button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				button.setTextFill(Color.WHITE);
				button.setScaleX(1);
				button.setScaleY(1);
			}
		});
	}
	
	private void initBackground() {
		if(levelNumber == 4) {
			bg.setFill(new ImagePattern(bgs[2]));	
		}
		else {
			bg.setFill(new ImagePattern(bgs[0]));
		}
		menubg.setFill(new ImagePattern(bgs[1]));
		appRoot.getChildren().addAll(bg, hbox1, gameTimer);
	}
	private void initLevelData() {
		levelWidth = LevelData.LEVEL1[0].length() * 32;
		for (int i = 0; i < LevelData.LEVEL1.length; i++) {
			String line;
			switch (levelNumber) {
				case 0:
					line = LevelData.Tunnel[i];
					break;
				case 1:
					line = LevelData.START[i];
					break;
				case 2:
					line = LevelData.LEVEL1[i];
					break;
				case 3:
					line = LevelData.LEVEL1[i];
					break;
				case 4:
					line = LevelData.LEVEL3[i];
					break;
				case 5:
					line = LevelData.LEVEL4[i];
					break;
				case 6:
					line = LevelData.END[i];
					break;
				default:
					line = LevelData.LEVEL1[i];
					break;
			}
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
					case 'a':
						Objects roof = new Objects(j*32, i*32, 32, 32, blocks[12]);
						walls.add(roof);
						appRoot.getChildren().add(roof);
						break;
					case 'b':
						Objects black = new Objects(j*32, i*32, 32, 32, blocks[13]);
						walls.add(black);
						appRoot.getChildren().add(black);
						break;
					case 'c':
						Objects gems = new Objects(j*32, i*32, 77, 57, gem);
						gemlist.add(gems);
						appRoot.getChildren().add(gems);
						break;	
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
						if(levelNumber == 4){
						Objects button = new Objects(j*32, i*32, 32, 8, blocks[10]);
						buttons.add(button);
						appRoot.getChildren().add(button);
						} else if (levelNumber != 3) {
						Objects button = new Objects(j*32, i*32+24, 32, 8, blocks[4]);
						buttons.add(button);
						appRoot.getChildren().add(button);
						}
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
			
			 for (int i = 0; i < spikes.size() ;i++) {
				temp.add(false);
		 }
	}
	private void initAvatar() {
		player = new Avatar(0, 572, 20, 32, floors, walls, doors, 0);
		appRoot.getChildren().add(player);
	}
	
	private void initGUIDeath() {
		deathCountMsg.setTextFill(Color.GREY);
		deathCountMsg.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
		deathCountMsg.setText("Death Count: "+player.getDeathCount());
	}
	private void initGUILevel() {
		levelDetail.setTextFill(Color.GREY);
		levelDetail.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
		levelDetail.setWrapText(true);
		if (this.levelNumber==0) {
			levelDetail.setText("Tunnel");
		} else if (this.levelNumber==1){
			levelDetail.setText("Level "+(this.levelNumber-1)+ " - A Mysterious Treasure");
		} else if (this.levelNumber==2){
			levelDetail.setText("Level "+(this.levelNumber-1)+ " - Beginning");
		} else if (this.levelNumber==3){
			levelDetail.setText("Level "+(this.levelNumber-1)+ " - Wrapping World");
		} else if (this.levelNumber==4){
			levelDetail.setText("Level "+(this.levelNumber-1)+ " - Mirror World");
		} else if (this.levelNumber==5){
			levelDetail.setText("Level "+(this.levelNumber-1)+ " - Die, Die, and Die Again");
		}
	}
	private void initGUITimer() {
		gameTimer.setLayoutX(700);
		gameTimer.setLayoutY(650);
		gameTimer.setTextFill(Color.GREY);
		gameTimer.setText(this.stopwatch.toString());
		gameTimer.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
	}
	private void initGUI() {
		hbox1.relocate(20,650);
		hbox1.setSpacing(200);
		hbox1.getChildren().clear();
		initGUIDeath();
		initGUILevel();
		initGUITimer();
		
		hbox1.getChildren().add(deathCountMsg);
		hbox1.getChildren().add(levelDetail);
		stop = false;
	}
	
	private void initEndMsg() {
		Label message = new Label("THE END");
		Font msgFont = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 45);
		message.setFont(msgFont);
		message.setTextFill(Color.GREEN);
		message.setTranslateX(580);
		message.setTranslateY(200);
		appRoot.getChildren().add(message);
	}
	private void initEndDeath() {
		Font fontDeath = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 30);
		Label deathCount = new Label("Total Death Count: " + player.getDeathCount());
		deathCount.setFont(fontDeath);
		deathCount.setTextFill(Color.GREY);
		deathCount.setTranslateX(535);
		deathCount.setTranslateY(430);
		appRoot.getChildren().add(deathCount);
	}
	private void initEndTimer() {
		Label totalTime = new Label("Total Time: " + gameTimer.getText());
		Font fontTimer = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 30);
		totalTime.setFont(fontTimer);
		totalTime.setTextFill(Color.GREY);
		totalTime.setTranslateX(535);
		totalTime.setTranslateY(470);
		appRoot.getChildren().add(totalTime);
	}
	private void initEnd() {
		initEndMsg();
		initEndDeath();
		initEndTimer();
		appRoot.getChildren().removeAll(btnmenu, deathCountMsg, gameTimer, levelDetail);
		appRoot.getChildren().addAll(restart, exit);
		
	}

	/**
	 * Method: Initialize all Content - Avatar, LevelData, Objects, etc.
	 * 
	 */
	private void initContent() {
		initBackground();
		initLevelData();
		initAvatar();
		if(levelNumber == 6){
			initEnd();
		} else {
			initGUI();
		}
	}


	private void moveRight() {
		if (player.getTranslateX() + 32 <= levelWidth - 5) {
			player.movePlayerX(4);
		}
	}
	private void moveUp(){
		if (player.getTranslateY() >= 5) {
            player.jumpPlayer();   
		}
	}
	private void moveLeft(){
		if( player.getTranslateX() >= 5) {
			player.movePlayerX(-4);			
		}
	}
	private void updateKey() {
		if (right) {
			if (levelNumber == 4){
				moveLeft();
				turnLeft = true;
			} else {
				moveRight();	
				turnLeft = false;
			}
		}
		if (up) {
			moveUp();
		}
		if (left) {
			if (levelNumber == 4){
				moveRight();
				turnLeft = false;	
			} else {
			moveLeft();
			turnLeft = true;
			}
		}
	}
	
	private void animate(String condition) {
		if (condition.equals("upleft")) {
			player.animation.setOffsetY(192);
			player.animation.setOffsetX(0);
		} else if (condition.equals("downleft")) {
			player.animation.setOffsetY(224);
			player.animation.setOffsetX(0);
		} else if (condition.equals("left")) {
			player.animation.setOffsetY(160);
			player.animation.setOffsetX(0);
		} else if (condition.equals("stillleft")) {
			player.animation.setOffsetY(128);
			player.animation.setOffsetX(0);
		} else if (condition.equals("upright")) {
			player.animation.setOffsetY(64);
			player.animation.setOffsetX(0);
		} else if (condition.equals("downright")) {
			player.animation.setOffsetY(96);
			player.animation.setOffsetX(0);
		} else if (condition.equals("right")) {
			player.animation.setOffsetY(32);
			player.animation.setOffsetX(0);
		} else if (condition.equals("stillright")) {
			player.animation.setOffsetY(0);
			player.animation.setOffsetX(0);
		}
		player.animation.play();
	}
	private void updateAnimation() {
		double gravVelocity = player.getVelocity().getY();
		if (turnLeft) {
			if (gravVelocity < 6) {
				animate("upleft");
			} else if (gravVelocity > 6 && !player.checkFloor()) {
				animate("downleft");
			} else if (left) {
				animate("left");
			} else {
				animate("stillleft");
			}
		} else {
			if (gravVelocity < 6) {
				animate("upright");
			} else if (gravVelocity > 6 && !player.checkFloor()) {
				animate("downright");
			} else if (right) {
				animate("right");
			} else {
				animate("stillright");
			}
		}
	}
	
	private void updateGravity() {
		if (player.getVelocity().getY() < 6) {
			player.addVelocity(0, .5);
		}
		player.movePlayerY((int)player.getVelocity().getY());
	}
	private void updateGUI() {
		this.stopwatch.setCurrentTime();
		gameTimer.setText(this.stopwatch.toString());
		deathCountMsg.setText("Death Count: " + player.getDeathCount());  
	}
	
	private void openDoor() {
		click.setOnEndOfMedia(new Runnable() {
			public void run() {
				click  = new MediaPlayer(click_name.playSound()); 
			}
		});
	    click.play();
		for (Objects door : doors) {
			appRoot.getChildren().remove(door);
			door.setVisibility(false);
		}
		player.updateObstacleState(floors, walls, doors);
	}
	private void updateDoor() {
		//Level 1
		for (Objects button : buttons) {
			if (player.getBoundsInParent().intersects(button.getBoundsInParent())&&stop==false) {
				if(levelNumber == 4) {
				button.setFill(new ImagePattern(blocks[11]));
				} else {
				button.setFill(new ImagePattern(blocks[9]));
				}
				openDoor();
				stop = true;
			}
		}
		//Level 2
		if (levelNumber == 3 && levels.checkIfAllPointsPassed() && stop == false) {
			openDoor();
			stop = true;
		}
		
		if(levelNumber == 5 && checkSpikes() == spikes.size() && stop == false){
			openDoor();
			stop = true; 
		}
	}

	private void updateSpike() {
		int count = 0;
		for (Node spike : spikes) {
			if (player.getBoundsInParent().intersects(spike.getBoundsInParent())) {
				death.setOnEndOfMedia(new Runnable() {
					public void run() {
						death  = new MediaPlayer(death_name.playSound()); ;
					}
				});
				if(levelNumber == 5){
					temp.set(count, true);
				}
				death.play();
				int deathCount = player.getDeathCount() + 1;
				appRoot.getChildren().remove(player);
				player = new Avatar(0, 572, 20, 32, floors, walls, doors, deathCount);
				appRoot.getChildren().add(player);
			}
			count++;
		}	
	}
	public int checkSpikes(){
		int val= 0;
		for(int i = 0; i< spikes.size(); i++){
			if(temp.get(i) == true){
				val++;
			}
		}
		return val;
	}
	
	private void updateGem() {
		for (Objects gem : gemlist) {	
			if (player.getBoundsInParent().intersects(gem.getBoundsInParent())) {
				appRoot.getChildren().clear();
				floors.clear();
				walls.clear();
				doors.clear();
				buttons.clear();
				spikes.clear();
				gemlist.clear();
				gemlist.add(new Objects(42*32, 20*32, 0, 0, new Image("Images/player.png")));
				levelNumber = 2;
				initContent();
			}
		}
	}
	
	private void updateAvatarEnd() {
		if (player.getTranslateX() > 1306  && player.getTranslateY() > 520) {
			if (levelNumber != 0) {
				tempLevel = levelNumber;
			}
			if (levelNumber == 0) {
				levelNumber = tempLevel + 1;
			} else {
				levelNumber = 0; 
			}
		appRoot.getChildren().clear();
		floors.clear();
		walls.clear();
		doors.clear();
		buttons.clear();
		gemlist.clear();
		spikes.clear();
		initContent();
		}
	}
	private void updateAvatarWarp() {
		if (player.getTranslateX() > 1306  && player.getTranslateY() < 105) {
			player.setTranslateX(5);
			if (levelNumber == 3) {
				levels.setPassedWrappingPoint(1);
			}
		}
		if (player.getTranslateX() < 5  && player.getTranslateY() < 105) {
			player.setTranslateX(1305);
			if (levelNumber == 3) {
				levels.setPassedWrappingPoint(2);
			}
		}
		if (player.getTranslateY() > 600) {
			if(levelNumber == 3 && player.getTranslateX() > 700) {
				levels.setPassedWrappingPoint(3);
			} else if (levelNumber == 3 && player.getTranslateX() < 700) {
				levels.setPassedWrappingPoint(4);
			}
			player.setTranslateX(player.getTranslateX());
			player.setTranslateY(2);
		}
		if (player.getTranslateY() < 1) {
			if (levelNumber == 3 && player.getTranslateX() > 700) {
				levels.setPassedWrappingPoint(5);
			} else if (levelNumber == 3 && player.getTranslateX() < 700) {
				levels.setPassedWrappingPoint(0);
			}
			player.setTranslateY(580);
			player.setTranslateX(player.getTranslateX());
			player.jumpPlayer();
		}
	}
	private void updateAvatar() {
		updateAvatarEnd();
		updateAvatarWarp();
	}
	
	private void updateLoop() {
		bgm.setOnEndOfMedia(new Runnable() {
			public void run() {
				bgm.seek(Duration.ZERO);
			}
		});
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
		if (levelNumber == 6) {
			appRoot.getChildren().remove(player);
		} else {
			updateKey();
		}
		updateGravity();
		updateAnimation();
		updateGUI();
		updateDoor();
		updateSpike();
		updateGem();
		updateAvatar();
		updateLoop();
	}
	

	private void restartGame(){
		levelNumber = 1;
		stopwatch = new TimerText();
		levels = new LevelData();
		temp = new ArrayList<Boolean>();
		appRoot.getChildren().clear();
		initContent();	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initContent();
		Scene scene = new Scene(appRoot,LevelData.LEVEL1[0].length() * 32 - 15, 690);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		
		primaryStage.setTitle("Re:Curse");
		
		//Main Menu
			VBox menuroot = new VBox();
			StackPane stackPane = new StackPane();
			Label title;
			Button btnstart;
			Button btnexit;
			Button btncredit;
		//Title
			title = new Label(">Re:Curse");
			Font fontTitle = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 70);
			title.setFont(fontTitle);
			title.setTextFill(Color.WHITE);
			title.setTranslateX(10);
			title.setTranslateY(-150);
		//Menu Button - Start
			btnstart = new Button("Start Game");
			Font fontStart = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 40);
			btnstart.setFont(fontStart);
			btnstart.setTextFill(Color.WHITE);
			btnstart.setBackground(Background.EMPTY);
			btnstart.setTranslateX(12);
			btnstart.setTranslateY(-30);
			btnResize(btnstart);
			//Action - Start
				btnstart.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						primaryStage.setScene(scene);
						primaryStage.show();
						primaryStage.setResizable(false);
						btnstart.setText("Resume");
					}
				});
		//Menu Button - Exit
			btnexit = new Button("EXIT");
			Font fontExit = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 30);
			btnexit.setFont(fontExit);
			btnexit.setTextFill(Color.WHITE);
			btnexit.setBackground(Background.EMPTY);
			btnexit.setTranslateX(10);
			btnexit.setTranslateY(130);
			btnResize(btnexit);
			//Action - Exit
				btnexit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.exit(0);
					}
				});
				
		Scene gamemenu = new Scene(menuroot, LevelData.LEVEL1[0].length() * 32 - 15, 685);

		//Credits
			StackPane crpane = new StackPane();
			HBox crroot = new HBox();
			Button btnback;
			crbg.setFill(new ImagePattern(bgs[3]));
			Scene crscene = new Scene(crroot,LevelData.LEVEL1[0].length() * 32 - 15, 690);
		//Menu Button - Credits
			btncredit = new Button("Credits");
			btncredit.setFont(fontExit);
			btncredit.setTextFill(Color.WHITE);
			btncredit.setBackground(Background.EMPTY);
			btncredit.setTranslateX(11);
			btncredit.setTranslateY(50);
			btnResize(btncredit);
			//Action - Credits
				btncredit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						primaryStage.setScene(crscene);
						primaryStage.show();
						primaryStage.setResizable(false);
					}
				});
				
		//Credits Button - Return
			btnback = new Button("Back");
			btnback.setFont(fontExit);
			btnback.setTextFill(Color.WHITE);
			btnback.setBackground(Background.EMPTY);
			btnback.setTranslateX(200);
			btnback.setTranslateY(210);
			btnResize(btnback);
			//Action - Return
				btnback.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						primaryStage.setScene(gamemenu);
						primaryStage.show();
						primaryStage.setResizable(false);
					}
				});	
		crpane.getChildren().addAll(crbg, btnback);
		crroot.getChildren().add(crpane);
		
		//Button - GUI Menu
			Font fontMenu = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20);
			btnmenu.setFont(fontMenu);
			BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("/Images/menu_button.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		    Background background = new Background(backgroundImage);
			btnmenu.setBackground(background);
			btnmenu.setTextFill(Color.WHITE);
			btnmenu.setLayoutX(1100);
			btnmenu.setLayoutY(650);
			btnResize(btnmenu);
			//Action - To Menu
				btnmenu.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						primaryStage.setScene(gamemenu);
						primaryStage.show();
						primaryStage.setResizable(false);
					}
				});
		
		//End Button - Restart
			restart.setFont(fontStart);
			restart.setTextFill(Color.WHITE);
			restart.setBackground(Background.EMPTY);
			restart.setLayoutX(560);
			restart.setLayoutY(280);
			btnResize(restart);
			//Action - Restart
				restart.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						restartGame();
					}
				});
		//End Button - Exit
			exit.setFont(fontExit);
			exit.setTextFill(Color.WHITE);
			exit.setBackground(Background.EMPTY);
			exit.setLayoutX(603);
			exit.setLayoutY(330);
			btnResize(exit);
				//Action - Exit
				exit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.exit(0);
						
					}
				});
		stackPane.getChildren().addAll(menubg, btnstart, btnexit, title, btncredit);
		menuroot.getChildren().add(stackPane);
		
		//Key Event handler
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case RIGHT: right= true; break;
                    case UP: up = true; break;
					case LEFT: left = true; break;
                }
            }
        });
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
				if(count==0){
				count++;
				}
                switch (event.getCode()) {
                    case RIGHT: right = false; break;
                    case UP: up= false; break;
					case LEFT: left= false; break; 
                }
            }
        });
		primaryStage.setScene(gamemenu);
		primaryStage.show();
		primaryStage.setResizable(false);
		bgm.play();
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		};
		timer.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
