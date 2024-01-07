package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class NotFoundException extends RuntimeException {
	private final String name;

	public NotFoundException(@NotNull String name) {
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
