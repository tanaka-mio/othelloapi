package com.example.demo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.resource.OthelloStone;

@RestController
@RequestMapping("api/sample")
public class RestApiController {

    @RequestMapping(value = "/getOthelloStone", method = RequestMethod.GET)
    @ResponseBody
    public OthelloStone getOthelloStone() {
    	OthelloStone othelloStone = new OthelloStone("1", "黒");
        return othelloStone;
    }
}