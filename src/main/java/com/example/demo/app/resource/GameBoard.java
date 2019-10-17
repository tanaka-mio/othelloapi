package com.example.demo.app.resource;

import java.io.Serializable;
import java.util.ArrayList;

public class GameBoard implements Serializable {

    private static final long serialVersionUID = 1L;

	/** ゲームコード */
    private int gameCode;
    /** オセロの配列 */
    private int [][] OthelloStone;
    /** 取れる可能性のある石の配列 */
	private ArrayList<ArrayList<Integer>> PossibilityStone;
	/** 取れる可能性のある石の配列 */
	private ArrayList<ArrayList<Integer>> ConfirmStone;

    // コンストラクタ
    public GameBoard () {
    }

	public int[][] getOthelloStone() {
		return OthelloStone;
	}

	public void setOthelloStone(int[][] othelloStone) {
		OthelloStone = othelloStone;
	}

	public ArrayList<ArrayList<Integer>> getPossibilityStone() {
		return PossibilityStone;
	}

	public void setPossibilityStone(ArrayList<ArrayList<Integer>> possibilityStone) {
		PossibilityStone = possibilityStone;
	}

	public ArrayList<ArrayList<Integer>> getConfirmStone() {
		return ConfirmStone;
	}

	public void setConfirmStone(ArrayList<ArrayList<Integer>> confirmStone) {
		ConfirmStone = confirmStone;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}
    
}