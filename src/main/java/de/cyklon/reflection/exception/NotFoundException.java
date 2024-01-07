package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class NotFoundException extends RuntimeException {
	private final Class<?> parent;
	private final String name;

	public NotFoundException(@NotNull Class<?> parent, @NotNull String name) {
		this.name = name;
		this.parent = parent;
	}

	@NotNull
	public Class<?> getParent() {
		return parent;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
