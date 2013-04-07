package com.roi.model;

public class MarkerPosition {
	
	private String barcode;
	private float xPos;
	private float yPos;
	
	public MarkerPosition(String barcode, float xPos, float yPos) {
		super();
		this.barcode = barcode;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public float getxPos() {
		return xPos;
	}
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}
	public float getyPos() {
		return yPos;
	}
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	
	
}
