package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class NotLoadedException extends RuntimeException {
	private final String name;

	public NotLoadedException(@NotNull String name, @NotNull String type, @NotNull String context) {
		super(type + " '" + name + "' is not loaded yet" + (context.isEmpty() ? "" : " in " + context) + "!");
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
