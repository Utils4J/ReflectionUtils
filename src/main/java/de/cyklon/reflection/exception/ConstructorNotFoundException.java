package de.cyklon.reflection.exception;

import de.cyklon.reflection.entities.ReflectClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConstructorNotFoundException extends MemberNotFoundException {
	public ConstructorNotFoundException(@NotNull ReflectClass<?> parent, @NotNull Class<?>[] types) {
		super(parent, Arrays.stream(types).map(Class::getName).collect(Collectors.joining(",", "(", ")")), "constructor");
	}
}
