package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class FieldNotFoundException extends NotFoundException {
	public FieldNotFoundException(@NotNull Class<?> parent, @NotNull String name) {
		super(parent, name);
	}
}
