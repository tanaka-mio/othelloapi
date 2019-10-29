package com.example.demo.app.resource;

import java.io.Serializable;

public class GameUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/** ユーザーのハッシュコード */
	private String user;
	/** ユーザーの石情報 */
	private Stone userStone;
	/** ゲームコード */
	private int gameCode;

	// コンストラクタ
	public GameUser (String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public Stone getUserStone() {
		return userStone;
	}

	public void setUserStone(Stone userStone) {
		this.userStone = userStone;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}
}