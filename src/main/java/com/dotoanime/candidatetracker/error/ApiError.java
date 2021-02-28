package com.dotoanime.candidatetracker.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiError {

	private String message;

	private Boolean success = false;

	private String url;

	private Map<String, String> validationErrors;

	public ApiError(String message, String url) {
		super();
		this.message = message;
		this.url = url;
	}
}
