import java.util.Date;

/**
 * Class: Timer
 * Class Use: Creating a Timer
 * Methods:
 * 		setStartTime
 * 		setCurrentTime
 * 		getStringSec
 * 		getStringMin
 * 		getStringHour
 * 		getSec
 * 		getMin
 * 		getHour
 *
 */
public class Timer {
	
	//Instance Variables
	private int sec;
	private int min;
	private int hour;
	private Date startTime;
	private Date currentTime;
	
	public Timer() {
		this.sec = 0;
		this.min = 0;
		this.hour = 0;
		this.startTime = new Date();
		this.currentTime = new Date();
	}
	
	//Setters
	public void setStartTime(Date time) {
		this.startTime = time;
	}
	public void setCurrentTime() {
		this.currentTime = new Date();
	}
	
	//Getters (String)
	public String getStringSec() {
		if (this.sec < 10) {
			return "0" + String.valueOf(getSec());
		}
		return String.valueOf(getSec());
	}
	
	public String getStringMin() {
		if (this.min < 10) {
			return "0" + String.valueOf(getMin());
		}
		return String.valueOf(getMin());
	}
	
	public String getStringHour() {
		if (this.hour < 10) {
			return "0" + String.valueOf(getHour());
		}
		return String.valueOf(getHour());
	}
	
	//Getters
	public int getSec() {
		if (this.sec%60 == 0 && this.sec > 0) {
			setStartTime(this.currentTime);
			this.min++;
		}
		this.sec=(int)((this.currentTime.getTime()-this.startTime.getTime()) / 1000);
		return this.sec;
	}
	public int getMin() {
		if (this.min%60==0 && this.min>0) {
			this.startTime=this.currentTime;
			this.hour++;
		}
		return this.min;
	}
	public int getHour() {
		return this.hour;
	}
}
