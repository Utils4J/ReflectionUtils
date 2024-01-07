package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class NotFoundException extends RuntimeException {
	private final String name;

	public NotFoundException(@NotNull String name, @NotNull String type, @NotNull String context) {
		super("Could not find " + type + " '" + name + "'" + (context.isEmpty() ? "" : " in " + context));
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
