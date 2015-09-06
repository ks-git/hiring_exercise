package com.drmtx.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FrequencyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FrequencyNotFoundException(long id) {
		super(id + " not available");
	}
}
