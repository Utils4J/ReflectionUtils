package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class MethodNotFoundException extends MemberNotFoundException {
	public MethodNotFoundException(@NotNull Class<?> parent, @NotNull String name) {
		super(parent, name);
	}
}
