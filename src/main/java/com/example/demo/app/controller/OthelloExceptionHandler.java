package com.example.demo.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OthelloExceptionHandler extends ResponseEntityExceptionHandler {
	// RuntimeExceptionの例外をキャッチする
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
		return super.handleExceptionInternal(ex, "tanakamio", null, HttpStatus.NOT_FOUND, request);
	}

	// すべての例外をキャッチする
	// どこにもキャッチされなかったらこれが呼ばれる
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		return super.handleExceptionInternal(ex, "handleAllException", null, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	// すべてのハンドリングに共通する処理を挟みたい場合はオーバーライドする
	// ex) ログを吐くなど
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}