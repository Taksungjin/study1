package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
	
	OK(1000, "Success"),
    FAIL(2000, "Fail");

    private final int statusCode;
    private final String statusMessage;

}
