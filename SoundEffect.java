import java.io.*;
import javafx.scene.media.Media;

/**
 * Class: SoundEffect
 * Class Use: Holding and Playing a Sound
 * Methods:
 * 		Constructor
 * 		playSound
 *
 */
public class SoundEffect {
	//Instance Variable
	String name;
	public SoundEffect(String name) {
		this.name = name;
	}

	public Media playSound() {
		String soundEffect = (name);
		Media sound = new Media(new File(soundEffect).toURI().toString());
		return sound;
	}
}
