package com.example.demo.app.resource;

import java.io.Serializable;

public class GameUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/** ユーザーのハッシュコード */
	private String user;
	/** ユーザーのターン */
	private Turn userTurn;
	/** ゲームコード */
	private int gameCode;

	// コンストラクタ
	public GameUser (String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public Turn getUserTurn() {
		return userTurn;
	}

	public void setUserTurn(Turn userTurn) {
		this.userTurn = userTurn;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}
}