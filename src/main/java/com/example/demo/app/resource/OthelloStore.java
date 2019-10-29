package com.example.demo.app.resource;

import java.util.ArrayList;
import java.util.List;

public class OthelloStore {

	// オセロの配列
	public static int[][] othelloStone = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, -1, 0, 0, 0 }, { 0, 0, 0, -1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
	// ゲームの判定を決めるTurn
	public static Stone gameTurn = null;
	// ゲームコード
	public static int gameCode = 0;

	// ゲームユーザーの登録ListMap
	public static List<GameUser> gameUserList = new ArrayList<>();
	// ゲームユーザーの登録ListMap
	public static List<GameBoard> gameBoardList = new ArrayList<>();

	// 取れる石の配列
	public static List<ArrayList<Integer>> confirmStone = new ArrayList<>();

}
