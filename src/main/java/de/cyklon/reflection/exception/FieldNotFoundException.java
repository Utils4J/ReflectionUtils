package de.cyklon.reflection.exception;

import de.cyklon.reflection.entities.ReflectClass;
import org.jetbrains.annotations.NotNull;

public class FieldNotFoundException extends MemberNotFoundException {
	public FieldNotFoundException(@NotNull ReflectClass<?> parent, @NotNull String name) {
		super(parent, name);
	}
}
