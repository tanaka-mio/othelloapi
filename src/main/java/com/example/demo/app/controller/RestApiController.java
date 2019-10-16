package com.example.demo.app.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


@RestController
@RequestMapping("api/sample")
public class RestApiController {

	// オセロの配列
	public int [][] OthelloStone = {
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, -1, 0, 0, 0, 0},
			{0, 0, 0, 1, -1, 0, 0, 0},
			{0, 0, 0, -1, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	// ターン
	public int turn = -1;
	
	// 取れる可能性のある石の配列
	public ArrayList<ArrayList<Integer>> PossibilityStone = new ArrayList<ArrayList<Integer>>();
	// 取れる石の配列
	public ArrayList<ArrayList<Integer>> ConfirmStone = new ArrayList<ArrayList<Integer>>();
	

	@RequestMapping(value = "/getMessage", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public OthelloStone getMessage() {
		OthelloStone othelloStone = new OthelloStone("1", "黒");
		return othelloStone;
	}

	@RequestMapping(value = "/getOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public int[][] getOthelloStone() {
		return OthelloStone;
	}
	
	@RequestMapping(value = "/getGameStartCode", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Map<String, String> getGameStartCode() {
        byte[] bytes;
        String resultCode = "";
        Map<String,String> resultmap = new HashMap<>();
		try {
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
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		resultmap.put("hashCode", resultCode);
		resultmap.put("turn", (turn == -1)? "-1" : "1" );
		
		return resultmap;
	}

	@RequestMapping(value = "/hitOthelloStone", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public String hitOthelloStone(
			@RequestParam("hitY") int hitY,
			@RequestParam("hitX") int hitX) {
        
		// Resultメッセージ
		String resultMessage = "";
		// 更新を実施するか判定する変数
		boolean result = false;
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
				if ((i >= 0 && i < 8) && (k >= 0 && k < 8 ) && OthelloStone[i][k] == (turn * -1)) {
					// 方向を算出する（対象ー置こうとした場所）
					int directionY = i - hitY;
					int directionX = k - hitX;
					// 方向の先にある石が何色かチェックする関数
					// 自分と同じ色がある場合、確定石リストにリスト追加
					// 石が何も置かれていない場合、その方向のチェックは処理終了
					// 敵の色がある→候補石リストに追加し、再帰関数を呼び続ける
					checkCell(i, k, directionY, directionX);
				}
			}
		}
		// 取ってきたリストが０件の場合更新しない
		if (ConfirmStone.size() == 0) {
			result = false;
		}

		// 石の更新処理
		if (result) {
			// クリックした場所を更新する
			OthelloStone[hitY][hitX] = turn;
			// 取れた範囲を更新する
			for (int k = 0 ; k < ConfirmStone.size(); k++) {
				int updateY = ConfirmStone.get(k).get(0);
				int updateX = ConfirmStone.get(k).get(1);
				OthelloStone[updateY][updateX] = turn;
			}
			// 確定石リストの初期化
			ConfirmStone = new ArrayList<ArrayList<Integer>>();
			// turnを判定させる
			turn *= -1;
			// 更新成功メッセージ
			resultMessage = "あなたの番です";
		}
		return resultMessage;
	}
	
	public void checkCell (int centerI, int centerK, int directionY, int directionX) {
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
		if (target == turn) {
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
		} else if (target == (turn * -1)) {
			// 方向の算出
			int paramDirectionY = targetY - centerI;
			int paramDirectionX = targetX - centerK;
			// 候補石リストに追加する
			ArrayList<Integer> intList = new ArrayList<Integer>();
			intList.add(paramDirectionY);
			intList.add(paramDirectionX);
			PossibilityStone.add(intList);
			
			// 再帰関数を呼ぶ
			checkCell(targetY, targetX, paramDirectionY, paramDirectionX);
		// 処理を終了させる条件：先に何もない
		} else if (target == 0) {
			// 候補石リストの初期化
			PossibilityStone = new ArrayList<ArrayList<Integer>>();
		}
	}
}