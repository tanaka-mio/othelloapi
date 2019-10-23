package com.example.demo.app.resource;

public enum Turn {
	
	White(1),
	Black(-1),
	;
	private int no;
	private Turn(int no) {
		this.no = no;
	}
	public int getNo() {
		return no;
	}
}
