package com.example.demo.app.resource;

import java.io.Serializable;
import java.util.ArrayList;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ゲームコード */
	private int gameCode;
	/** オセロの配列 */
	private int [][] othelloStone;
	/** 取れる可能性のある石の配列 */
	private ArrayList<ArrayList<Integer>> possibilityStone = new ArrayList<ArrayList<Integer>>();
	/** 取れる可能性のある石の配列 */
	private ArrayList<ArrayList<Integer>> confirmStone = new ArrayList<ArrayList<Integer>>();

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

	public ArrayList<ArrayList<Integer>> getPossibilityStone() {
		return possibilityStone;
	}

	public void setPossibilityStone(ArrayList<ArrayList<Integer>> possibilityStone) {
		this.possibilityStone = possibilityStone;
	}

	public ArrayList<ArrayList<Integer>> getConfirmStone() {
		return confirmStone;
	}

	public void setConfirmStone(ArrayList<ArrayList<Integer>> confirmStone) {
		this.confirmStone = confirmStone;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}

}