package com.roi.model;

public class Marker {
	
	private int markerId;
	private String barcode;
	private String createdByUser;
	private String description;
	private float xPos;
	private float yPos;
	private String isShown;
	
	public Marker(int markerId, String barcode, String createdByUser,
			String description, float xPos, float yPos, String isShown) {
		this.markerId = markerId;
		this.barcode = barcode;
		this.createdByUser = createdByUser;
		this.description = description;
		this.xPos = xPos;
		this.yPos = yPos;
		this.isShown = isShown;
	}
	
	public Marker(String barcode, String createdByUser,
			String description, float xPos, float yPos, String isShown) {
		this.barcode = barcode;
		this.createdByUser = createdByUser;
		this.description = description;
		this.xPos = xPos;
		this.yPos = yPos;
		this.isShown = isShown;
	}
	
	public int getMarkerId() {
		return markerId;
	}

	public void setMarkerId(int markerId) {
		this.markerId = markerId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getIsShown() {
		return isShown;
	}

	public void setIsShown(String isShown) {
		this.isShown = isShown;
	}

	@Override
	public String toString() {
		return "Marker [markerId=" + markerId + ", barcode=" + barcode
				+ ", createdByUser=" + createdByUser + ", description="
				+ description + ", xPos=" + xPos + ", yPos=" + yPos
				+ ", isShown=" + isShown + "]";
	}

	
	
	
}
