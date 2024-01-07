package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class ExecutionException extends RuntimeException {
	private final Throwable cause;

	public ExecutionException(@NotNull Throwable cause) {
		this.cause = cause;
	}

	@NotNull
	public Throwable getCause() {
		return cause;
	}
}
