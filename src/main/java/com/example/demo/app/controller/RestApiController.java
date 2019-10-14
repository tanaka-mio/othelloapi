package com.example.demo.app.controller;

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
    
    @RequestMapping(value = "/hitOthelloStone", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String hitOthelloStone(
    		@RequestParam("hitX") int hitX,
    		@RequestParam("hitY") int hitY) {
    	OthelloStone[hitY][hitX] = -1;
        return "あなたの番です";
    }
}