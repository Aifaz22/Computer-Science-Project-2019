public class TimerText extends Timer{
	public TimerText() {
		super();
	}
	//Getters (String)
	public String getStringSec() {
		
		if (super.getSec() < 10) {
			return "0" + String.valueOf(super.getSec());
		}
		return String.valueOf(super.getSec());
	}
	
	public String getStringMin() {
		if (super.getMin() < 10) {
			return "0" + String.valueOf(super.getMin());
		}
		return String.valueOf(super.getMin());
	}
	
	public String getStringHour() {
		if (super.getHour() < 10) {
			return "0" + String.valueOf(super.getHour());
		}
		return String.valueOf(super.getHour());
	}
	@Override
	public String toString() {
		return getStringHour()+":"+getStringMin()+":"+getStringSec();
	}
}
