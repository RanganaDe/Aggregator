package nl.kramp.assignment.controller.common;

import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CompletionException.class)
	public ResponseEntity<Object> handleCompletionException(CompletionException ex) {
		Throwable cause = ex.getCause();
		if (cause instanceof TimeoutException) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
					.body(Map.of("Error", "Catalog service timed out"));
		}
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of("Error", "Catalog service unavailable"));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}
}
