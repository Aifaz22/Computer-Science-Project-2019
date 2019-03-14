import javafx.scene.media.Media;
import java.io.*;

public class SoundEffect{
	
	String name;
	public SoundEffect(String name){
		this.name = name;
	}

	public Media playSound(){
		
	String soundEffect = (name);
    Media sound = new Media(new File(soundEffect).toURI().toString());
	
	return sound;
		
	}
}