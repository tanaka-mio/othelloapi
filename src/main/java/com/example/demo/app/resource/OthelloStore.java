package com.example.demo.app.resource;

import java.util.ArrayList;

public class OthelloStore {

	// オセロの配列
	public static int[][] othelloStone = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, -1, 0, 0, 0 }, { 0, 0, 0, -1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
	// ゲームの判定を決めるTurn
	public static Turn gameTurn = null;
	// ゲームコード
	public static int gameCode = 0;

	// ゲームユーザーの登録ListMap
	public static ArrayList<GameUser> gameUserList = new ArrayList<GameUser>();
	// ゲームユーザーの登録ListMap
	public static ArrayList<GameBoard> gameBoardList = new ArrayList<GameBoard>();

	// 取れる可能性のある石の配列
	public static ArrayList<ArrayList<Integer>> possibilityStone = new ArrayList<ArrayList<Integer>>();
	// 取れる石の配列
	public static ArrayList<ArrayList<Integer>> confirmStone = new ArrayList<ArrayList<Integer>>();

}
