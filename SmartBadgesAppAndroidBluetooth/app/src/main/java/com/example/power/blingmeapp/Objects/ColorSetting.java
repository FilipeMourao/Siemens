package com.example.power.blingmeapp.Objects;
// this class contains other parameters from the json format of the wifi badge, most of the paramaeters are not used anymore
// but the code was reused from the previous version
public class ColorSetting {
	private String state;
	private int brightness;
	private ColorCustomized color;
	private String mode;
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
	public ColorCustomized getColor() {
		return color;
	}
	public void setColor(ColorCustomized color) {
		this.color = color;
	}

	public String getMode() {
		return mode;
	}
	public void setMode
			(String mode) {
		this.mode = mode;
	}
	public ColorSetting(String state, int brightness, ColorCustomized color, String mode) {
		super();
		this.state = state;
		this.brightness = brightness;
		this.color = color;
		this.mode = mode;
	}
	public ColorSetting( ColorCustomized color) {
		super();
		this.state = "ON";
		this.brightness = 75;
		this.color = color;
		this.mode = "SOLID";
	}

}
