package com.example.power.ledcode;

public class Color {
	String mode;
	int r,g,b;
	public Color(String mode, int r, int g, int b) {
		super();
		this.mode = mode;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}

}
