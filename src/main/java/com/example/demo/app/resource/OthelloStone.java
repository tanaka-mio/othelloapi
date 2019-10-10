package com.example.demo.app.resource;

import java.io.Serializable;

public class OthelloStone implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 数字 */
    private String number;

    /** 色 */
    private String color;

    // getter
    public OthelloStone (String number, String color) {
        this.setNumber(number);
        this.setColor(color);
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}