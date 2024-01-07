package de.cyklon.reflection.exception;

import de.cyklon.reflection.entities.ReflectClass;
import org.jetbrains.annotations.NotNull;

public class MethodNotFoundException extends MemberNotFoundException {
	public MethodNotFoundException(@NotNull ReflectClass<?> parent, @NotNull String name) {
		super(parent, name);
	}
}
