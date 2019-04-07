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
import javafx.scene.image.ImageView;
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
 * TO DO LIST:
 * Avatar has Death count, but Main has Death count. CHOOSE ONE.
 * Animation has issues. Have movements make sense, and animations make sense.
 * Categorize methods. The current methods are HUGE. Seperate them into reasonable chunks, with a theme.
 * Understand and create a Main Menu class. There is enough labels and buttons for it to be a class on its own. However, it interacts with Main too much, and must be taken with care.
 * Add levels/Ending(if required)
 * Add level title, and add it to the GUI
 * The image Array is titled blocks. There should only be blocks in there. Make another array with backgrounds/animations as needed.


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
	private TimerText stopwatch = new TimerText();
	private Label gameTimer = new Label();
	int deaths = 0;
	private int levelNumber = 1;
	
	//Sound & Music
	private SoundEffect bgm_name = new SoundEffect("music.wav");
	private MediaPlayer bgm = new MediaPlayer(bgm_name.playSound());
	private SoundEffect click_name = new SoundEffect("Button_Push.wav");
    private MediaPlayer click = new MediaPlayer(click_name.playSound());
	private SoundEffect death_name = new SoundEffect("death.wav");
    private MediaPlayer death = new MediaPlayer(death_name.playSound());
	
	//Graphics
	private Image image = new Image("Images/player.png");
	private Image gem = new Image("Images/gem1.png");
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
		new Image("Images/menubackground.png"),
		new Image("Images/button2.png"),
		new Image("Images/button_pressed2.png"),
		new Image("Images/background_inverted.png"),
		new Image("Images/fire_spritesheet.png"),
		new Image("Images/roof.png"),
		new Image("Images/black.png"),
		new Image("Images/credits.png")
	};
	private ImageView imageView = new ImageView(image);
	
	//Game
	private int levelWidth;
	private Button btnmenu= new Button("Main Menu");
	private Button restart = new Button("RESTART");
	private Button exit = new Button("EXIT");
	private Rectangle bg = new Rectangle(672 * 2, 354*2);
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
	private int totalDeathCount=0;
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
			bg.setFill(new ImagePattern(blocks[14]));	
		}
		else {
			bg.setFill(new ImagePattern(blocks[10]));
		}
		menubg.setFill(new ImagePattern(blocks[11]));
		appRoot.getChildren().addAll(bg);
		appRoot.getChildren().add(hbox1);
		appRoot.getChildren().add(btnmenu);
		appRoot.getChildren().add(gameTimer);
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
						Objects roof = new Objects(j*32, i*32, 32, 32, blocks[16]);
						walls.add(roof);
						appRoot.getChildren().add(roof);
						break;
					case 'b':
						Objects black = new Objects(j*32, i*32, 32, 32, blocks[17]);
						walls.add(black);
						appRoot.getChildren().add(black);
						break;
					case 'c':
						Objects gems = new Objects(j*32, i*32, 126, 126, gem);
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
						Objects button = new Objects(j*32, i*32, 32, 8, blocks[12]);
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
		player = new Avatar(0, 572, 20, 32, floors, walls, doors);
		appRoot.getChildren().add(player);
	}
	private void initGUI() {
		deathCountMsg.setText("Death Count: "+player.getDeathCount());
		deathCountMsg.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
		deathCountMsg.setTextFill(Color.GREY);
		levelDetail.setTextFill(Color.GREY);
		levelDetail.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
		levelDetail.setWrapText(true);
		if (this.levelNumber==0) {
			levelDetail.setText("Tunnel");
		} else if (this.levelNumber==2){
			levelDetail.setText("Level "+(this.levelNumber-1)+ "- Beginning");
		}
		else if (this.levelNumber==3){
			levelDetail.setText("Level "+(this.levelNumber-1)+ "- Wrapping World");
		}
		else if (this.levelNumber==4){
			levelDetail.setText("Level "+(this.levelNumber-1)+ "- Mirror World");
		}
		else if (this.levelNumber==5){
			levelDetail.setText("Level "+(this.levelNumber-1)+ "- Die, die and die");
	
		}else if (this.levelNumber==1){
			levelDetail.setText("Start "+(this.levelNumber-1)+ "- Touch Gem and start");
	
		}
		gameTimer.setTextFill(Color.GREY);
		gameTimer.setText(this.stopwatch.toString());
		gameTimer.setFont(Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20));
		player.setDeathCount(this.deaths);
		
		stop = false;
	}
	
	
	
	private void animate(String condition) {
		if (condition == "right") {
			player.animation.setOffsetY(0);
			player.animation.setOffsetX(0);
			player.animation.play();
		} else if (condition == "left") {
			player.animation.setOffsetY(70);
			player.animation.setOffsetX(0);
			player.animation.play();
		}  else {
			player.animation.stop();
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
		if (up && right) {
			player.animation.setOffsetY(100);
			player.animation.setOffsetX(0);
			player.animation.play();
			player.jumpPlayer();
			turnLeft = false;
		} else if (up) {
			moveUp();
			turnLeft = false;
		}
		if (left) {
			if (levelNumber == 4){
				moveRight();
				turnLeft = false;	
			} else {
			moveLeft();
			turnLeft= true;
			}
		}
		if (right == false && left == false && up == false && turnLeft == false) {
			player.animation.stop();
			player.animation.setOffsetY(32);
			player.animation.setOffsetX(0);
			player.animation.play();
		} else if (right == false && left == false && up == false && turnLeft == true) {
			player.animation.stop();
			player.animation.setOffsetY(134);
			player.animation.setOffsetX(0);
			player.animation.play();	
		}
	}
	private void updateDoor() {
		//Level 1
		for (Objects button : buttons) {
			if (player.getBoundsInParent().intersects(button.getBoundsInParent())&&stop==false) {
				if(levelNumber == 4) {
				button.setFill(new ImagePattern(blocks[13]));
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
				levelNumber=2;
				initContent();
			}
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
					player.addDeathCount();
					this.deaths=player.getDeathCount();
					appRoot.getChildren().remove(player);
					player = new Avatar(0, 572, 20, 32, floors, walls, doors);
					totalDeathCount++;
					appRoot.getChildren().add(player);
					player.setDeathCount(this.deaths);
			}
			count++;
		}	
	}
	public void endingScene(){
		StackPane stackPane = new StackPane();
		//appRoot.getChildren().addAll(bg);
		Label message = new Label("THE END");
		Label deathCount = new Label("Total Death Count: " + totalDeathCount);
		Label totalTime = new Label("Total Time: " + gameTimer.getText());
		Font font = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 30);
		Font mssgFont = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 45);
		

		deathCount.setTranslateX(535);
		deathCount.setTranslateY(430);
		deathCount.setFont(font);
		message.setFont(mssgFont);
		message.setTranslateX(580);
		message.setTranslateY(200);
		deathCount.setTextFill(Color.GREY);
		totalTime.setTranslateX(535);
		totalTime.setTranslateY(470);
		totalTime.setFont(font);
		deathCountMsg.setTextFill(Color.BLACK);
		levelDetail.setTextFill(Color.BLACK);
		totalTime.setTextFill(Color.GREY);
		message.setTextFill(Color.GREEN);
		appRoot.getChildren().remove(btnmenu);
		appRoot.getChildren().remove(deathCountMsg);
		appRoot.getChildren().remove(gameTimer);
		appRoot.getChildren().addAll(deathCount, totalTime,message, restart, exit);
		
	}
	
	/**
	 * Method: Initialize all Content - Avatar, LevelData, Objects, etc.
	 * 
	 */
	private void initContent() {
		this.initBackground();
		this.initLevelData();
		this.initAvatar();
		if(levelNumber == 6){
			endingScene();
		}
		else{
			this.initGUI();
		}
	}

	 //Player moving right
	 public void moveRight(){	
		if  (player.getTranslateX() + 32 <= levelWidth - 5) {
			animate("right");
			player.movePlayerX(4);
		}
	}
	
	//Player moving up
	public void moveUp(){
		if (player.getTranslateY() >= 5) {
			animate("up");
            player.jumpPlayer();   
		}
	}	
	
	//Player moving left
	public void moveLeft(){
		if( player.getTranslateX() >= 5) {
			animate("left");
			player.movePlayerX(-4);			
		}
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
		if(levelNumber == 6){
			appRoot.getChildren().remove(player);
		}
		else{
			updateKey();
		}
		if (player.getVelocity().getY() < 6) {
			player.addVelocity(0, .5);
		}
		player.movePlayerY((int)player.getVelocity().getY());
		
		//Timer/Counter
		this.stopwatch.setCurrentTime();
		gameTimer.setText(this.stopwatch.toString());
		deathCountMsg.setText("Death Count: "+player.getDeathCount());       
		updateDoor();
		updateSpike();
		updateGem();
		
		//Avatar Position - End Level
		if (player.getTranslateX() > 1306  && player.getTranslateY() > 520) {
			if (levelNumber != 0)
			tempLevel = levelNumber;
			appRoot.getChildren().clear();
			floors.clear();
			walls.clear();
			doors.clear();
			buttons.clear();
			spikes.clear();
			if(levelNumber == 0)
				levelNumber = tempLevel + 1;
			else
				levelNumber = 0;
		initContent();
		}
		
		//Avatar Position - Warping
		if (player.getTranslateX() > 1306  && player.getTranslateY() < 105) {
			player.setTranslateX(5);
			if(levelNumber == 3)
			levels.setPassedWrappingPoint(1);
		}
		if (player.getTranslateX() < 5  && player.getTranslateY() < 105) {
			player.setTranslateX(1305);
			if(levelNumber == 3)
				levels.setPassedWrappingPoint(2);
		}
		if (player.getTranslateY() > 600) {
			if(levelNumber == 3 && player.getTranslateX() > 700) {
				levels.setPassedWrappingPoint(3);
			}
			else if (levelNumber == 3 && player.getTranslateX() < 700){
				levels.setPassedWrappingPoint(4);
			}
			player.setTranslateX(player.getTranslateX());
			player.setTranslateY(2);
		}
		if (player.getTranslateY() < 1) {
			if(levelNumber == 3 && player.getTranslateX() > 700) {
				levels.setPassedWrappingPoint(5);
			}
			else if (levelNumber == 3 && player.getTranslateX() < 700){
				levels.setPassedWrappingPoint(0);
			}
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
	
	public void openDoor() {
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

	public void restartGame(){
		levelNumber = 1;
		deaths = 0;
		stopwatch = new TimerText();
		appRoot.getChildren().clear();
		initContent();	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initContent();
		Scene scene = new Scene(appRoot,LevelData.LEVEL1[0].length() * 32 - 15, 690);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		
		hbox1.relocate(20,650);
		hbox1.setSpacing(200);
		hbox1.getChildren().add(deathCountMsg);
		hbox1.getChildren().add(levelDetail);
		
		primaryStage.setTitle("Re:Curse");
		
		//Credits section
		StackPane crpane = new StackPane();
		HBox crroot = new HBox();
		crbg.setFill(new ImagePattern(blocks[18]));
		Button btnback = new Button("Back");
		btnback.setTranslateX(200);
		btnback.setTranslateY(210);
		Scene crscene = new Scene(crroot,LevelData.LEVEL1[0].length() * 32 - 15, 690);

		
		//Creating the Main Menu
		
		VBox menuroot = new VBox();
		StackPane stackPane = new StackPane();
		Button btnstart;
		Button btnexit;
		Button btncredit;
		Label title;
		
		//Title
		title = new Label(">Re:Curse");
		title.setTranslateX(10);
		title.setTranslateY(-150);
		
		//Main Menu Buttons
		btnstart = new Button("Start Game");
		btnexit = new Button("EXIT");
		btncredit = new Button("Credits");
		btnstart.setTranslateY(-30);
		btnstart.setTranslateX(12);
		btnexit.setTranslateY(130);
		btnexit.setTranslateX(10);
		btncredit.setTranslateX(11);
		btncredit.setTranslateY(50);
		
		//GUI Button
		BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("/Images/menu_button.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
	    Background background = new Background(backgroundImage);
		btnmenu.setBackground(background);
		btnmenu.setLayoutX(1100);
		btnmenu.setLayoutY(650);
		restart.setLayoutX(560);
		restart.setLayoutY(280);
		exit.setLayoutX(603);
		exit.setLayoutY(330);
		gameTimer.setLayoutX(700);
		gameTimer.setLayoutY(650);
		
		
		//Start Game
		btnstart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setResizable(false);
				btnstart.setText("Resume");
			}
		});
		
		//Exit
		btnexit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
		//Credit
		btncredit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(crscene);
				primaryStage.show();
				primaryStage.setResizable(false);
			}
		});
		
		Font fontMenu = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 20);
		Font fontStart = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 40);
		Font fontExit = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 30);
		Font fontTitle = Font.loadFont(getClass().getResourceAsStream("PixelOperator.ttf"), 70);
		btnmenu.setFont(fontMenu);
		btnstart.setFont(fontStart);
		btnexit.setFont(fontExit);
		exit.setFont(fontExit);
		restart.setFont(fontStart);
		title.setFont(fontTitle);
		btnmenu.setTextFill(Color.WHITE);
		btnstart.setTextFill(Color.WHITE);
		btnexit.setTextFill(Color.WHITE);
		title.setTextFill(Color.WHITE);
		exit.setTextFill(Color.WHITE);
		restart.setTextFill(Color.WHITE);
		btnstart.setBackground(Background.EMPTY);
		btnexit.setBackground(Background.EMPTY);
		btncredit.setBackground(Background.EMPTY);
		restart.setBackground(Background.EMPTY);
		exit.setBackground(Background.EMPTY);
		btncredit.setTextFill(Color.WHITE);
		btncredit.setFont(fontExit);
		btnback.setFont(fontExit);
		btnback.setTextFill(Color.WHITE);
		btnback.setBackground(Background.EMPTY);
		
		
		this.btnResize(btnmenu);
		this.btnResize(btnstart);
		this.btnResize(btnexit);
		this.btnResize(btncredit);
		this.btnResize(btnback);
		this.btnResize(restart);
		this.btnResize(exit);
		
		crpane.getChildren().addAll(crbg, btnback);
		crroot.getChildren().add(crpane);
			

		stackPane.getChildren().addAll(menubg, btnstart, btnexit, title, btncredit);
		menuroot.getChildren().add(stackPane);
		
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
		
		restart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				restartGame();
				
			}
		});
		
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
				
			}
		});
	
		
		btnback.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(gamemenu);
				primaryStage.show();
				primaryStage.setResizable(false);
			}
		});	
		
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
