package com.example.demo.app.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.resource.OthelloStone;
import com.example.demo.app.resource.GameBoard;
import com.example.demo.app.resource.GameUser;


@RestController
@RequestMapping("api/sample")
public class RestApiController {

	// オセロの配列
	public int [][] OthelloStone = {
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 1, -1, 0, 0, 0},
			{0, 0, 0, -1, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	// ターン
	public int turn = -1;
	// ゲームコード
	public int gameCode = 0;
	
	// ゲームユーザーの登録ListMap
	public ArrayList<GameUser> gameUserList = new ArrayList<GameUser>();	
	// ゲームユーザーの登録ListMap
	public ArrayList<GameBoard> gameBoardList = new ArrayList<GameBoard>();
	
	// 取れる可能性のある石の配列
	public ArrayList<ArrayList<Integer>> PossibilityStone = new ArrayList<ArrayList<Integer>>();
	// 取れる石の配列
	public ArrayList<ArrayList<Integer>> ConfirmStone = new ArrayList<ArrayList<Integer>>();
	
	/*
	 * （新）オセロゲーム情報取得用API
	 * */
	@RequestMapping(value = "/getBoardStatus", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Map<Object, Object> getBoardStatus() {
		// 結果マップ
		Map<Object, Object> resultMap = new HashMap<>();
		/*
		// ユーザー情報
		GameUser myGameUser = new GameUser();
		// ゲーム情報
		GameBoard myGameBoard = new GameBoard();
		
		// (1) ハッシュコードのチェック ======================
        for (GameUser tempuser : gameUserList) {
        	if (tempuser.getUser().equals(hashCode)) {
        		myGameUser = tempuser;
        		break;
        	}
        }
        // ない場合、初期表示のやつを返そう
        if (null == myGameUser.getUser()) {
    		resultMap.put("OthelloStone", OthelloStone);
    		resultMap.put("status", "ready");
    		return resultMap;
        }
        
        // (2) ゲームボードの取得 ======================
        for (GameBoard tempBoard : gameBoardList) {
        	if (tempBoard.getGameCode() == myGameUser.getGameCode()) {
        		myGameBoard = tempBoard;
        		break;
        	}
        }
        */
		resultMap.put("OthelloStone", OthelloStone);
		resultMap.put("status", "ready");
		return resultMap;
	}
	
	/*
	 * （新）オセロゲーム開始用API
	 * */
	@RequestMapping(value = "/getGameStartCode", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Map<String, String> getGameStartCode() {
        byte[] bytes;
        String resultCode = "";
        GameUser registUser = new GameUser();
        GameBoard registBoard = new GameBoard();
        Map<String,String> resultmap = new HashMap<>();
		try {
			// (1) ハッシュコードの生成 ===================================
	        //現在日時を取得する
	        Calendar c = Calendar.getInstance();
	        //フォーマットパターンを指定して表示する
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	        //ハッシュを生成したい元の文字列
	        String source = sdf.format(c.getTime());
	        //ハッシュ生成前にバイト配列に置き換える
	        Charset charset = StandardCharsets.UTF_8;
	        //ハッシュアルゴリズム
	        String algorithm = "SHA-512";
	        //ハッシュ生成処理
			bytes = MessageDigest.getInstance(algorithm).digest(source.getBytes(charset));
	        resultCode = DatatypeConverter.printHexBinary(bytes);
	        
	        // (2) ユーザー情報、ゲーム情報の登録 ===================================
	        // 自分が白の場合、ルームコードを取得してINSERT
	        // 自分が黒の場合、ルームコード・ゲームコードを＋１してINSERT
	        if (turn == -1) {
	        	gameCode = gameCode + 1;
	        	
	        	// 黒の時だけゲームボード作成する（初回なので）
	        	registBoard.setOthelloStone(OthelloStone);
	        	registBoard.setGameCode(gameCode);
	        	registBoard.setConfirmStone(new ArrayList<ArrayList<Integer>>());
	        	registBoard.setPossibilityStone(new ArrayList<ArrayList<Integer>>());
	        	gameBoardList.add(registBoard);
	        }
	        // ユーザー情報の作成
        	registUser.setUser(resultCode);
        	registUser.setUserTurn(turn);
        	registUser.setGameCode(gameCode);	
        	gameUserList.add(registUser);
        	
    		// 結果の詰め込み
    		resultmap.put("hashCode", resultCode);
    		resultmap.put("turn", (turn == -1)? "-1" : "1" );
	        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
		// (3) Turnを反転させる ===================================
        turn *= -1;
		
		return resultmap;
	}

	@RequestMapping(value = "/hitOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public String hitOthelloStone(
			@RequestParam("hitY") int hitY,
			@RequestParam("hitX") int hitX,
			@RequestParam("hashCode") String hashCode) {
		
		// ユーザー情報
		GameUser myGameUser = new GameUser();
		/*
		// ゲーム情報
		GameBoard myGameBoard = new GameBoard();
		// オセロ盤面
		int [][] myOthelloStone;
		// 可能性石リスト
		ArrayList<ArrayList<Integer>> myPossibilityStone;
		// 確定石リスト
		ArrayList<ArrayList<Integer>> myConfirmStone;
		*/
		// Resultメッセージ
		String resultMessage = "";
		// 更新を実施するか判定する変数
		boolean result = false;
		// 自分のターン
		int myTurn = 0;
		
		// (1) ハッシュコードのチェック ======================
        for (GameUser tempuser : gameUserList) {
        	if (tempuser.getUser().equals(hashCode)) {
        		myGameUser = tempuser;
        		myTurn = tempuser.getUserTurn();
        		break;
        	}
        }
        if (null == myGameUser.getUser()) {
        	resultMessage = "ゲームに参加していません";
			return resultMessage; 
        }
        // TODO:自分のターンかどうかチェック
        
        /*
        // (2) ゲームボードの取得 ======================
        for (GameBoard tempBoard : gameBoardList) {
        	if (tempBoard.getGameCode() == myGameUser.getGameCode()) {
        		myGameBoard = tempBoard;
        		break;
        	}
        }
        myOthelloStone = myGameBoard.getOthelloStone();
        myPossibilityStone = myGameBoard.getPossibilityStone();
        myConfirmStone = myGameBoard.getConfirmStone();
        */
        
		// (3) ゲームスタート          ======================
		// すでに石が置かれているかチェックする
		if (OthelloStone[hitY][hitX] != 0) {
			resultMessage = "その場所には置けません";
			return resultMessage;
		}
		// 周りのマスに石が1つでもあるかチェックする
		for (int i = hitY - 1; i < hitY + 2; i++) {
			for (int k = hitX - 1; k < hitX + 2; k++) {
				if ((i >= 0 && i < 8) && (k >= 0 && k < 8 ) && OthelloStone[i][k] != 0) {
					result = true;
					break;
				}
			}
		}
		
		// 取れる石があるかチェックする
		for (int i = hitY - 1; i < hitY + 2; i++) {
			for (int k = hitX - 1; k < hitX + 2; k++) {
				// 敵のマスだった場合、チェック関数を呼び出す
				if ((i >= 0 && i < 8) && (k >= 0 && k < 8 ) && OthelloStone[i][k] == (myTurn * -1)) {
					// 方向を算出する（対象ー置こうとした場所）
					int directionY = i - hitY;
					int directionX = k - hitX;
					// 方向の先にある石が何色かチェックする関数
					// 自分と同じ色がある場合、確定石リストにリスト追加
					// 石が何も置かれていない場合、その方向のチェックは処理終了
					// 敵の色がある→候補石リストに追加し、再帰関数を呼び続ける
					checkCell(i, k, directionY, directionX, myTurn);
				}
			}
		}
		// 取ってきたリストが０件の場合更新しない
		if (ConfirmStone.size() == 0) {
			result = false;
		}

		// (4) 石の更新処理　          ======================
		if (result) {
			// クリックした場所を更新する
			OthelloStone[hitY][hitX] = myTurn;
			// 取れた範囲を更新する
			for (int k = 0 ; k < ConfirmStone.size(); k++) {
				int updateY = ConfirmStone.get(k).get(0);
				int updateX = ConfirmStone.get(k).get(1);
				OthelloStone[updateY][updateX] = myTurn;
			}
			// 確定石リストの初期化
			ConfirmStone = new ArrayList<ArrayList<Integer>>();
			// 更新成功メッセージ
			resultMessage = "あなたの番です";
		}
		return resultMessage;
	}
	
	public void checkCell (int centerI, int centerK, int directionY, int directionX, int myTurn) {
		// 対象の石座標に方向の座標を足しこんでチェック対象を取得する
		int targetY = centerI + directionY;
		int targetX = centerK + directionX;
		
		// ここで、配列要素外にアクセスしようとしていたら処理終了する
		if (!(targetY >= 0 && targetY < 8) && !(targetX >= 0 && targetX < 8 )){
			// 候補石リストの初期化
			PossibilityStone = new ArrayList<ArrayList<Integer>>();
			return;
		}
		int target = OthelloStone[targetY][targetX];
		
		// 終了になる条件：自分と同じ色（リスト追加）
		if (target == myTurn) {
			// 確定石リストに追加する
			ArrayList<Integer> intList = new ArrayList<Integer>();
			intList.add(centerI);
			intList.add(centerK);
			ConfirmStone.add(intList);
			// 今までの候補リストを追加する
			if (PossibilityStone.size() != 0) {
				for (int m = 0; m < PossibilityStone.size(); m++) {
					// 確定石リストに追加する
					ConfirmStone.add(PossibilityStone.get(m));
				}
			}
			// 候補石リストの初期化
			PossibilityStone = new ArrayList<ArrayList<Integer>>();
			
		// 再帰になる条件：ライバルと同じ色
		} else if (target == (myTurn * -1)) {
			// 方向の算出
			int paramDirectionY = targetY - centerI;
			int paramDirectionX = targetX - centerK;
			// 候補石リストに追加する
			ArrayList<Integer> intList = new ArrayList<Integer>();
			intList.add(paramDirectionY);
			intList.add(paramDirectionX);
			PossibilityStone.add(intList);
			// 再帰関数を呼ぶ
			checkCell(targetY, targetX, paramDirectionY, paramDirectionX, myTurn);
			
		// 処理を終了させる条件：先に何もない
		} else if (target == 0) {
			// 候補石リストの初期化
			PossibilityStone = new ArrayList<ArrayList<Integer>>();
		}
	}
	
	/*
	 * メッセージ取得用API
	 * */
	@RequestMapping(value = "/getMessage", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public OthelloStone getMessage() {
		OthelloStone othelloStone = new OthelloStone("1", "黒");
		return othelloStone;
	}

	/*
	 * （旧）オセロの配列取得用API
	 * */
	@RequestMapping(value = "/getOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public int[][] getOthelloStone() {
		return OthelloStone;
	}
}