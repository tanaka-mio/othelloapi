package com.example.demo.app.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.resource.GameBoard;
import com.example.demo.app.resource.GameUser;
import com.example.demo.app.resource.OthelloStone;
import com.example.demo.app.resource.OthelloStore;
import com.example.demo.app.resource.Stone;

@RestController
@RequestMapping("api/sample")
public class RestApiController {


	// メッセージ一覧
	private final String NOTSTARTGAME_USER_JOIN = "まだゲームに参加していません。";
	private final String NOTSTARTGAME_USER_WAIT = "まだ対戦相手が現れていません。";
	private final String STARTGAME_NOT_YOUR_TURN = "あなたのターンではありません。";
	private final String STARTGAME_YOUR_TURN = "あなたのターンです。";
	private final String STARTGAME_NOT_PUT = "その場所には置けません。";
	private final String STARTGAME_NEXT_TURN = "相手のターンを待っています。";

	/*
	 * （新）オセロゲーム情報取得用API
	 */
	@RequestMapping(value = "/getBoardStatus", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Map<Object, Object> getBoardStatus(@RequestParam("hashCode") String hashCode) {
		// 結果マップ
		Map<Object, Object> resultMap = new HashMap<>();

		// (1) ハッシュコードのチェック
		if (hashCode.equals("null")) {
			resultMap.put("OthelloStone", OthelloStore.othelloStone);
			resultMap.put("status", "001");
			return resultMap;
		}
		// (2) ユーザーリストにいるかチェック
		// ユーザー情報
		GameUser myGameUser = null;
		// ゲーム情報
		// GameBoard myGameBoard = new GameBoard();
		for (GameUser tempuser : OthelloStore.gameUserList) {
			if (tempuser.getUser().equals(hashCode)) {
				myGameUser = tempuser;
				break;
			}
		}
		// ゲームに参加していない場合
		if (myGameUser.getUser() == null) {
			resultMap.put("OthelloStone", OthelloStore.othelloStone);
			resultMap.put("status", "002");
			resultMap.put("message", NOTSTARTGAME_USER_JOIN);
			return resultMap;
		}
		// 相手がいない場合
		if (OthelloStore.gameTurn == null) {
			resultMap.put("OthelloStone", OthelloStore.othelloStone);
			resultMap.put("status", "003");
			resultMap.put("message", NOTSTARTGAME_USER_WAIT);
			return resultMap;
		}
		// 相手のターンの場合
		if (OthelloStore.gameTurn != myGameUser.getUserStone()) {
			resultMap.put("OthelloStone", OthelloStore.othelloStone);
			resultMap.put("status", "004");
			resultMap.put("message", STARTGAME_NOT_YOUR_TURN);
			return resultMap;
		}
		// 自分のターンの場合
		if (OthelloStore.gameTurn == myGameUser.getUserStone()) {
			resultMap.put("OthelloStone", OthelloStore.othelloStone);
			resultMap.put("status", "004");
			resultMap.put("message", STARTGAME_YOUR_TURN);
			return resultMap;
		}

		// (3) ゲームボードのチェック
		resultMap.put("OthelloStone", OthelloStore.othelloStone);
		resultMap.put("status", "005");
		return resultMap;
	}

	/*
	 * （新）オセロゲーム開始用API
	 */
	@RequestMapping(value = "/getGameStartCode", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Map<String, String> getGameStartCode() {
		// (1) ハッシュコードの生成 ===================================
		// 現在日時を取得する
		Calendar c = Calendar.getInstance();
		// フォーマットパターンを指定して表示する
		// ハッシュを生成したい元の文字列
		String source =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(c.getTime());
		// ハッシュ生成前にバイト配列に置き換える
		Charset charset = StandardCharsets.UTF_8;
		// ハッシュアルゴリズム
		String algorithm = "SHA-512";
		String userId = "";
		try {
			byte[] bytes;
			// ハッシュ生成処理
			bytes = MessageDigest.getInstance(algorithm).digest(source.getBytes(charset));
			userId = DatatypeConverter.printHexBinary(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("userIdの生成で失敗しました", e);
		}

		// (2) ユーザー情報、ゲーム情報の登録 ===================================
		if (OthelloStore.gameUserList.size() % 2 == 0) {
			OthelloStore.gameTurn = Stone.Black;
			OthelloStore.gameCode = OthelloStore.gameCode + 1;
			// 黒の時だけゲームボード作成する（初回なので）
			GameBoard registBoard = new GameBoard(OthelloStore.gameCode, OthelloStore.othelloStone);
			OthelloStore.gameBoardList.add(registBoard);
		} else {
			// 白の場合
			OthelloStore.gameTurn = Stone.White;
		}
		// ユーザー情報の作成
		GameUser registUser = new GameUser(userId);
		registUser.setUserStone(OthelloStore.gameTurn);
		registUser.setGameCode(OthelloStore.gameCode);
		OthelloStore.gameUserList.add(registUser);

		// 結果の詰め込み
		Map<String, String> resultmap = new HashMap<>();
		resultmap.put("hashCode", userId);
		resultmap.put("turn", (OthelloStore.gameTurn == Stone.Black) ? "-1" : "1");

		// (3) Turnを反転させる ===================================
		OthelloStore.gameTurn = (OthelloStore.gameTurn == Stone.Black) ? Stone.White : Stone.Black;
		return resultmap;
	}

	@RequestMapping(value = "/hitOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public String hitOthelloStone(@RequestParam("hitY") int hitY, @RequestParam("hitX") int hitX,
			@RequestParam("hashCode") String hashCode) {

		// (1) ハッシュコードのチェック
		// ユーザー情報
		GameUser myGameUser = null;
		// 自分のターン
		Stone myGameUserTurn = null;
		for (GameUser tempuser : OthelloStore.gameUserList) {
			if (tempuser.getUser().equals(hashCode)) {
				myGameUser = tempuser;
				myGameUserTurn = tempuser.getUserStone();
				break;
			}
		}
		// 参加しているかチェック
		if (myGameUser == null || myGameUser.getUser() == null) {
			return NOTSTARTGAME_USER_JOIN;
		}
		// 対戦相手がいるかチェック
		if (OthelloStore.gameUserList.size() == 1) {
			return NOTSTARTGAME_USER_WAIT;
		}
		// 自分のターンかチェック
		if (OthelloStore.gameTurn != myGameUserTurn) {
			return STARTGAME_NOT_YOUR_TURN;
		}

		// (2) ゲームボードの取得
		// (3) ゲームスタート
		// Resultメッセージ
		String resultMessage = "";

		// すでに石が置かれているかチェックする
		if (OthelloStore.othelloStone[hitY][hitX] != 0) {
			resultMessage = STARTGAME_NOT_PUT;
			return resultMessage;
		}
		// 取れる石があるかチェックする
		// possibilityStoneの初期化
		List<ArrayList<Integer>> possibilityStone = new ArrayList<>();
		for (int i = hitY - 1; i < hitY + 2; i++) {
			for (int k = hitX - 1; k < hitX + 2; k++) {
				// 敵のマスだった場合、チェック関数を呼び出す
				if ((i >= 0 && i < 8) && (k >= 0 && k < 8)) {
					// 方向を算出する（対象ー置こうとした場所）
					int directionY = i - hitY;
					int directionX = k - hitX;
					// 方向の先にある石が何色かチェックする関数
					// 自分と同じ色がある場合、確定石リストにリスト追加
					// 石が何も置かれていない場合、その方向のチェックは処理終了
					// 敵の色がある→候補石リストに追加し、再帰関数を呼び続ける
					checkCell(i, k, directionY, directionX, myGameUserTurn.getNo(), possibilityStone);
				}
				// possibilityStoneの初期化
				possibilityStone = new ArrayList<>();
			}
		}
		// 取ってきたリストが1件以上の場合更新する
		if (OthelloStore.confirmStone.size() != 0) {
			// クリックした場所を更新する
			OthelloStore.othelloStone[hitY][hitX] = myGameUserTurn.getNo();
			// 取れた範囲を更新する
			for (int k = 0; k < OthelloStore.confirmStone.size(); k++) {
				int updateY = OthelloStore.confirmStone.get(k).get(0);
				int updateX = OthelloStore.confirmStone.get(k).get(1);
				OthelloStore.othelloStone[updateY][updateX] = myGameUserTurn.getNo();
			}
			// 確定石リストの初期化
			OthelloStore.confirmStone = new ArrayList<ArrayList<Integer>>();
			// 更新成功メッセージ
			resultMessage = STARTGAME_NEXT_TURN;
			// Turnを反転させる
			OthelloStore.gameTurn = (OthelloStore.gameTurn == Stone.Black) ? Stone.White : Stone.Black;
		}
		return resultMessage;
	}

	private void checkCell(int centerI, int centerK, int directionY, int directionX, int myTurn,
			List<ArrayList<Integer>> possibilityStone) {

		// 敵の場合
		if (OthelloStore.othelloStone[centerI][centerK] == (myTurn * -1)) {
			// 可能性のあるリストに追加
			ArrayList<Integer> intList = new ArrayList<>();
			intList.add(centerI);
			intList.add(centerK);
			possibilityStone.add(intList);

			// 次に進めるかチェックする
			int targetY = centerI + directionY;
			int targetX = centerK + directionX;
			if (!(targetY >= 0 && targetY < 8) || !(targetX >= 0 && targetX < 8)) {
				// 候補石リストの初期化
				return;
			}
			// 再帰関数を呼ぶ
			checkCell(targetY, targetX, directionY, directionX, myTurn, possibilityStone);

		} else if (OthelloStore.othelloStone[centerI][centerK] == myTurn) {
			// 味方の場合
			// 今までの候補リストを追加する
			for (int m = 0; m < possibilityStone.size(); m++) {
				// 確定石リストに追加する
				OthelloStore.confirmStone.add(possibilityStone.get(m));
			}
		}
	}

	/*
	 * メッセージ取得用API
	 */
	@RequestMapping(value = "/getMessage", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public OthelloStone getMessage() {
		OthelloStone othelloStone = new OthelloStone("1", "黒");
		return othelloStone;
	}

	/*
	 * （旧）オセロの配列取得用API
	 */
	@RequestMapping(value = "/getOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public int[][] getOthelloStone() {
		return OthelloStore.othelloStone;
	}
}