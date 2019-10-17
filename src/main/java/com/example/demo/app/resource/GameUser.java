package com.example.demo.app.resource;

import java.io.Serializable;

public class GameUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ユーザーのハッシュコード */
    private String User;
    /** ユーザーのターン */
    private int userTurn;
    /** ゲームコード */
    private int gameCode;
    
    // デフォルトコンストラクタ
    public GameUser () {
    }
    
	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public int getUserTurn() {
		return userTurn;
	}

	public void setUserTurn(int userTurn) {
		this.userTurn = userTurn;
	}

	public int getGameCode() {
		return gameCode;
	}

	public void setGameCode(int gameCode) {
		this.gameCode = gameCode;
	}
}