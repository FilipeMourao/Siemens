package com.example.power.blingmeapp;

import android.graphics.Color;

public class ColorCustomized {
	String mode = "rgb";
	int r,g,b;
	public ColorCustomized( int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}
	ColorCustomized(String colorString){
        int color = Color.parseColor(colorString);
        this.r = Color.red(color);
        this.g = Color.green(color);
        this.b = Color.blue(color);
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
