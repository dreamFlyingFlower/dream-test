package com.wy.test.captcha.web.contorller;

public class ImageCaptcha {

	String state;

	String image;

	public ImageCaptcha(String state, String image) {
		super();
		this.state = state;
		this.image = image;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
