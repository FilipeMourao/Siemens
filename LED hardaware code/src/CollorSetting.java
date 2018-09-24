public class CollorSetting {
	String state;
	int brightness;
	Color color;
	String mode;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public CollorSetting(String state, int brightness, Color color, String mode) {
		super();
		this.state = state;
		this.brightness = brightness;
		this.color = color;
		this.mode = mode;
	}

}
