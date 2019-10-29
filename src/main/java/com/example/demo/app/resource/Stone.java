package com.example.demo.app.resource;

public enum Stone {
	
	White(1),
	Black(-1),
	;
	private int no;
	private Stone(int no) {
		this.no = no;
	}
	public int getNo() {
		return no;
	}
}
