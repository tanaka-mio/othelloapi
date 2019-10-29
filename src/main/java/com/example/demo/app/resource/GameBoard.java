package com.example.demo.app.resource;

import java.io.Serializable;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ゲームコード */
	private int gameCode;
	/** オセロの配列 */
	private int [][] othelloStone;

	// コンストラクタ
	public GameBoard (int argGameCode, int[][] argOthelloStone) {
		this.gameCode = argGameCode;
		this.othelloStone = argOthelloStone;
	}

	public int[][] getOthelloStone() {
		return othelloStone;
	}

	public void setOthelloStone(int[][] othelloStone) {
		this.othelloStone = othelloStone;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}

}